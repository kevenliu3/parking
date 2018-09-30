package com.hibox.carclient;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class RequestUtils {

    private static OkHttpClient mClient;

    private static RequestUtils INSTANCE;

    public static RequestUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestUtils();

            X509TrustManager trustManager = null;
            SSLSocketFactory sslSocketFactory = null;
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                trustManager = (X509TrustManager) trustManagers[0];
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }


            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            if (trustManager != null && sslSocketFactory != null) {
                builder.sslSocketFactory(sslSocketFactory, trustManager);
            }
            mClient = builder.build();
        }
        return INSTANCE;
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public JSONObject post(String url, Map<String, Object> params) throws Exception {
        String result = null;
        int count = 0;
        while (result == null && count < 3) {
            count++;
            try {
                Response response = mClient.newCall(getRequest(url, params)).execute();
                result = response.body().string();
            } catch (Exception e) {
                result = null;
                if (count == 2) {
                    throw e;
                }
            }
        }

        return (JSONObject) JSON.parse(result);
    }

    public JSONObject post(String url, String json) throws Exception {
        String result = null;
        int count = 0;
        while (result == null && count < 3) {
            count++;
            try {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = mClient.newCall(request).execute();
                result = response.body().string();
            } catch (Exception e) {
                result = null;
                if (count == 2) {
                    throw e;
                }
            }
        }

        return (JSONObject) JSON.parse(result);
    }

    public JSONObject post(String url) throws Exception {
        return post(url, new HashMap<String, Object>());
    }

    public void postSync(String url, Map<String, Object> params) throws Exception {
        mClient.newCall(getRequest(url, params)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private Request getRequest(String url, Map<String, Object> params) throws Exception {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    formBody.add(key, String.valueOf(params.get(key)));
                }
            }
        }

        FormBody body = formBody.build();
        return new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
    }

}

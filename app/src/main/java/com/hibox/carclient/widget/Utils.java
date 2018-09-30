package com.hibox.carclient.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import com.hibox.carclient.BaseApplication;

import java.io.*;
import java.util.List;

/**
 * Everyday is another day, keep going.
 * author:  Ramo
 * email:   327300401@qq.com
 * date:    2017/6/8 13:20
 * desc:
 */

public class Utils {
    public static DisplayMetrics getDisplayMetrics() {
        return getAppContext().getResources().getDisplayMetrics();
    }

    public static int getStatusBarHeight() {
        int statusBarHeight = Utils.dp2px(24);
        //获取status_bar_height资源的ID
        int resourceId = getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    //获取应用实例
    public static Context getAppContext() {
        return BaseApplication.getContext();
    }

    public static SharedPreferences getSharePreference() {
        return getAppContext().getSharedPreferences("mario", Context.MODE_PRIVATE);
    }

    //获取应用的缓存目录
    public static File getExternalCacheDir() {
        return getAppContext().getExternalCacheDir();
    }

    //获取版本
    public static String getVersion() {
        PackageManager manager = getAppContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getAppContext().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "获取版本信息失败";
        }
    }

    //dp转px
    public static int dp2px(float dp) {
        return (int) (dp * getDisplayMetrics().density + 0.5f);
    }

    //px转dp
    public static int px2dp(float px) {
        return (int) (px / getDisplayMetrics().scaledDensity + 0.5f);
    }

    //sp转px
    public static int sp2px(float sp) {
        return (int) (sp * getDisplayMetrics().scaledDensity + 0.5f);
    }

    //px转sp
    public static int px2sp(float px) {
        return (int) (px / getDisplayMetrics().scaledDensity + 0.5f);
    }

    //深度copy数组
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    //解决中文url地址
    public static String urlEncoder(String source) {
        String data = "";
        if (null == source) {
            return data;
        }
        try {
            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                if (c + "".getBytes().length > 1 && c != ':' && c != '/') {
                    data = data + java.net.URLEncoder.encode(c + "", "utf-8");
                } else {
                    data = data + c;
                }
            }
        } catch (UnsupportedEncodingException e) {
        }
        return data;
    }

    public static long calculateLength(CharSequence cs) {
        double len = 0;
        for (int i = 0; i < cs.length(); i++) {
            int tmp = (int) cs.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
}


package com.hibox.carclient;

import android.app.Application;
import android.graphics.Bitmap;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/19 15:25
 * desc:
 */
public class BaseApplication extends Application {
    private static BaseApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Picasso build = new Picasso.Builder(this)
                .memoryCache(new LruCache(20 * 1024 * 1024))
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .loggingEnabled(false)
                .build();
        Picasso.setSingletonInstance(build);
    }

    public static BaseApplication getContext() {
        return INSTANCE;
    }
}

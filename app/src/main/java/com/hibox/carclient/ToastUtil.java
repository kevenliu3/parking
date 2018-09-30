package com.hibox.carclient;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Ramo on 2017/3/6.
 * Everyday is another day, keep going.
 */

public class ToastUtil {
    private static String oldMsg;
    private static Toast toast;
    private static long oldTime = 0L;
    private static long curTime = 0L;
    private static int preDuration = 0;
    private static TextView textView;

    public static void show(Context context, String msg) {
        show(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        if (msg == null) {
            return;
        }
        if (toast == null) {

            toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.view_transient_notification, null, false);
            textView = view.findViewById(R.id.message);
            textView.setText(msg);
            toast.setView(view);
            toast.setDuration(duration);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            oldTime = System.currentTimeMillis();
            oldMsg = msg;
            preDuration = duration;
        } else {
            curTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (curTime - oldTime > preDuration) {
                    toast.setDuration(duration);
                    preDuration = duration;
                }
            } else {
                oldMsg = msg;
//                toast.setText(msg);
                textView.setText(msg);
                toast.setDuration(duration);
                preDuration = duration;
            }
        }
        oldTime = curTime;
        toast.show();
    }

    public static void show(Context context, @StringRes int resId) {
        show(context.getApplicationContext(), context.getApplicationContext().getString(resId));
    }

    public static void show(Context context, @StringRes int resId, int duration) {
        show(context.getApplicationContext(), context.getApplicationContext().getString(resId), duration);
    }
}

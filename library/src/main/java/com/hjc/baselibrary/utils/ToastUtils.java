package com.hjc.baselibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.widget.Toast;


/**
 * Toast统一管理类
 */
public class ToastUtils {

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    private static Toast toast;
    private static Handler handler;
    private static Runnable run = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };
    private static boolean isNightMode = false;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ToastUtils cannot be instantiated");
    }

    /**
     * 后台弹出toast
     *
     * @param ctx      ctx
     * @param msg      msg
     * @param duration duration
     */
    private static void toast(Context ctx, CharSequence msg, int duration) {
        if (ctx == null || msg == null || msg.length() == 0) {
            return;
        }

        if (handler == null) {
            handler = new Handler();
        }

        handler.removeCallbacks(run);
        // handler的duration不能直接对应Toast的常量时长，在此针对Toast的常量相应定义时长
        switch (duration) {
            case LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
                duration = Toast.LENGTH_SHORT;
                break;
            case LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
                duration = Toast.LENGTH_LONG;
                break;
            default:
                break;
        }

        if (null != toast) {
            toast.setText(msg);
        } else {
            toast = Toast.makeText(ctx, msg, duration);
        }

        handler.postDelayed(run, duration);
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context context
     * @param message message
     */
    public static void showShort(Context context, CharSequence message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context context
     * @param message message
     */
    public static void showShort(Context context, int message) {
        toast(context, context.getResources().getString(message), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context context
     * @param message message
     */
    public static void showLong(Context context, CharSequence message) {
        toast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context context
     * @param message message
     */
    public static void showLong(Context context, int message) {
        toast(context, context.getResources().getString(message), Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  message
     * @param duration duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        toast(context, message, duration);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  message
     * @param duration duration
     */
    public static void show(Context context, int message, int duration) {
        toast(context, context.getResources().getString(message), duration);
    }
}

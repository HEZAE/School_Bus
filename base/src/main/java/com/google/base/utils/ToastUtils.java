package com.google.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.base.R;

@SuppressLint({"StaticFieldLeak","InflateParams"})
public class ToastUtils {
    private static View view;
    private static Toast toast;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    private static void show(Context context, String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, dip2px(context));
        //设置显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        }
        TextView textView = view.findViewById(R.id.toast_text);
        textView.setText(text);
        toast.setView(view);
        toast.show();
    }

    public static void showToast(Context context,String content) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(context, content);
        } else {
            mHandler.post(() -> show(context, content));
        }
    }

    /**
     * dp转px
     */
    static int dip2px(Context context) {
        return (int) (((float) 80 * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}

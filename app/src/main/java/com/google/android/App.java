package com.google.android;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.base.mmkv.KVConfigImpl;

import org.litepal.LitePal;

public class App extends Application implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化KV
        KVConfigImpl.init(this,"zhtr");
        //初始化LitePal
        LitePal.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

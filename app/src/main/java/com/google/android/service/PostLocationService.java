package com.google.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.base.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

public class PostLocationService extends Service {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public static AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    //声明定位回调监听器

    public AMapLocationListener mLocationListener = aMapLocation -> {
        if (aMapLocation != null){
            EventBus.getDefault().post(aMapLocation);
        }

    };


    public PostLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null){
            mLocationClient.stopLocation();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getLocation() {
        AMapLocation amapLocation = null;
        mLocationListener.onLocationChanged(amapLocation);
        AMapLocationClient.updatePrivacyShow(getApplicationContext(),true,true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(),true);
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            ToastUtils.showToast(getApplicationContext(),e.toString());
            e.printStackTrace();
        }
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);

        Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_SHORT).show();

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        if(null != mLocationClient){
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

}
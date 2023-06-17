package com.google.android.ui.main;



import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.blankj.utilcode.util.AppUtils;
import com.google.android.GlobalConfig;
import com.google.android.R;
import com.google.android.api.ApiCar;
import com.google.android.service.PostLocationService;
import com.google.android.ui.mine.bean.DriverBean;
import com.google.base.fragment.BaseFragment;
import com.google.android.databinding.FragmentMainBinding;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import java.text.DecimalFormat;

public class MainFragment extends BaseFragment<FragmentMainBinding> {

    //初始化地图控制器对象
    private AMap aMap;
    private boolean isWork = false;
    private Handler handler = new Handler();
    private DriverBean driverBean;
    private String Lat="123",Lng="123";
    MyLocationStyle myLocationStyle = new MyLocationStyle();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.map.onCreate(savedInstanceState);
        for (int i=0;i<AppUtils.getAppSignaturesSHA1().size();i++){
            Log.v("SHA1", AppUtils.getAppSignaturesSHA1().get(i));
        }

    }

    @Override
    protected void initFragment() {
        EventBus.getDefault().register(this);
        initMap();
        driverBean = LitePal.findLast(DriverBean.class);

        switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS,-1)){
            case 0:
                binding.startWork.setVisibility(View.GONE);
                handler.post(runnable);
                break;
            case 1:
                binding.startWork.setVisibility(View.VISIBLE);
                binding.startWork.setOnClickListener(v -> {
                    if (!isWork){
                        isWork = true;
                        binding.startWork.setText("停止工作");
                        activity.startService(new Intent(activity, PostLocationService.class));
                    }else {
                        isWork = false;
                        binding.startWork.setText("开始工作");
                        activity.stopService(new Intent(activity, PostLocationService.class));
                        /////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////////////////////////
                        ApiCar.INSTANCE.postLatLng(String.valueOf(LitePal.findLast(DriverBean.class).getCar_id()), "", "", (success, errCode, data) -> {
                            if (data != null){
                                LogUtils.d(data.getMessage());
                            }
                        });
                    }
                });
                break;
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ApiCar.INSTANCE.getLatLng(
                    (success, errCode, data) -> {
                if (data != null) {
                    for (int i = 0; i < data.getData().size(); i++) {
                        if (!data.getData().get(i).getCar_lat().isEmpty() && !data.getData().get(i).getCar_lng().isEmpty()) {
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.car));
                            final Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.getData().get(i).getCar_lat()), Double.parseDouble(data.getData().get(i).getCar_lng()))).title(data.getData().get(i).getCar_name()).icon(bitmapDescriptor));
                            marker.setVisible(true);
                        }
                    }
                  }
                }
            );
            handler.postDelayed(this,5000);
        }
    };
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.map.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    private void initMap(){


        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.strokeWidth(10) // 多边形的边框
                .strokeColor(Color.parseColor("#6BB200")) // 边框颜色
                .fillColor(Color.parseColor("#9ED7F4AD"));   // 多边形的填充色

        polygonOptions.add(new LatLng(23.035579,114.424355));
        polygonOptions.add(new LatLng(23.034562, 114.422375));
        polygonOptions.add(new LatLng(23.036478, 114.420905));
        polygonOptions.add(new LatLng(23.036576, 114.420498));
        polygonOptions.add(new LatLng(23.035703, 114.41819));
        polygonOptions.add(new LatLng(23.034907, 114.417738));
        polygonOptions.add(new LatLng(23.03466, 114.418135));
        polygonOptions.add(new LatLng(23.034342, 114.418024));
        polygonOptions.add(new LatLng(23.034115, 114.417815));
        polygonOptions.add(new LatLng( 23.033735, 114.418298));
        polygonOptions.add(new LatLng( 23.033044, 114.418282));
        polygonOptions.add(new LatLng( 23.032841, 114.41796));
        polygonOptions.add(new LatLng( 23.033779 , 114.416699));
        polygonOptions.add(new LatLng( 23.033443 , 114.416163));
        polygonOptions.add(new LatLng( 23.031992 , 114.415782));
        polygonOptions.add(new LatLng( 23.031646 , 114.416206));
        polygonOptions.add(new LatLng( 23.031168 , 114.416512));
        polygonOptions.add(new LatLng( 23.030841 , 114.415792));
        polygonOptions.add(new LatLng( 23.032135 , 114.415025));
        polygonOptions.add(new LatLng( 23.032332 , 114.415213));
        polygonOptions.add(new LatLng( 23.033014 , 114.414832));
        polygonOptions.add(new LatLng( 23.03252 , 114.413582));
        polygonOptions.add(new LatLng( 23.033685 , 114.413685));
        polygonOptions.add(new LatLng( 23.034025 , 114.414554));
        polygonOptions.add(new LatLng( 23.036365 , 114.414742));
        polygonOptions.add(new LatLng( 23.036494 , 114.415745));
        polygonOptions.add(new LatLng( 23.037625 , 114.415397));
        polygonOptions.add(new LatLng( 23.037783 , 114.415692));
        polygonOptions.add(new LatLng( 23.038677 , 114.416073));
        polygonOptions.add(new LatLng( 23.039267 , 114.415873));
        polygonOptions.add(new LatLng( 23.039376 , 114.415637));
        polygonOptions.add(new LatLng( 23.039391 , 114.415283));
        polygonOptions.add(new LatLng( 23.040317 , 114.415177));
        polygonOptions.add(new LatLng( 23.040401 , 114.416201));
        polygonOptions.add(new LatLng( 23.041077 , 114.418106));
        polygonOptions.add(new LatLng( 23.041872 , 114.418771));
        polygonOptions.add(new LatLng( 23.042617 , 114.420026));
        if (aMap == null) {
            aMap = binding.map.getMap();
        }
        //小蓝点初始化
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点
        //定位方式
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        aMap.addPolygon(polygonOptions);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.038999,114.417939),15));
        Toast.makeText(context, "初始化中心成功", Toast.LENGTH_SHORT).show();
    }

    DecimalFormat format = new java.text.DecimalFormat("0.000000");
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLocation(AMapLocation aMapLocation) {
        if (aMapLocation.getLongitude() != 0 && aMapLocation.getLatitude() != 0){
            ApiCar.INSTANCE.postLatLng(String.valueOf(driverBean.getCar_id()), format.format(aMapLocation.getLatitude()), format.format(aMapLocation.getLongitude()), (success, errCode, data) -> {
                if (data != null){
                    LogUtils.d(data.getMessage());
                }
            });
        }
    }


}
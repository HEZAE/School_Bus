package com.google.android.api;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.ui.mine.bean.CarBean;
import com.google.android.ui.mine.bean.DriverBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : lancer 🐢
 * e-mail : lancer2ooo@qq.com
 * desc   : hello word
 */
public class ApiCar {
    public static String API_BASE_URL = "http://47.115.230.91:8080";
    public static final ApiCar INSTANCE = new ApiCar();
    public static String ADD = "/accountInfo/updateCar";//发布动态
    public static String GETBY = "/accountInfo/getBy";//发布动态
    public static String POST = "/accountInfo/postLatLng";//发布动态
    public static String GETLATLNG = "/accountInfo/getLatLng";//发布动态

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 根据司机ID查询动态
     * 🐢
     */
    public void getBy(String car_id, Api.Callback<ApiResult<CarBean>> callback) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("car_id", car_id);

        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + GETBY + getBodyParams(bodyParams)).get().build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<CarBean> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<CarBean>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 绑定车辆
     * 🐢
     */
    public void addInfo(String driver_id, String car_id, String car_name, String car_info, Callback<ApiResult<DriverBean>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("driver_id", driver_id)
                .add("car_id", car_id)
                .add("car_name", car_name)
                .add("car_info", car_info)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + ADD).post(formBody).build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<DriverBean> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<DriverBean>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 司机端上传经纬度数据
     * 🐢
     */
    public void postLatLng(String car_id, String lat, String lng, Callback<ApiResult<String>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("car_id", car_id)
                .add("car_lat", lat)
                .add("car_lng", lng)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + POST).post(formBody).build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<String> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<String>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 学生端获取所有校车的经纬度
     * 🐢
     */
    public void getLatLng(Api.Callback<ApiResult<List<CarBean>>> callback) {

        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + GETLATLNG).get().build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<List<CarBean>> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<List<CarBean>>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    public interface Callback<T> {
        void result(boolean success, int errCode, @Nullable T data);
    }

    /**
     * GET请求添加参数
     */
    private String getBodyParams(Map<String, String> bodyParams) {
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder("?");
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key)) && stringBuffer.length() > 2) {
                    //如果参数不是null并且不是""，就拼接起来
                    stringBuffer.append("&");
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                } else if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                }
            }

            return stringBuffer.toString();
        } else {
            return "";
        }
    }
}

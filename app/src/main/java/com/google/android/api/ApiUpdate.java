package com.google.android.api;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.ui.forum.bean.Update;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : lancer 🐢
 * e-mail : lancer2ooo@qq.com
 * desc   : hello word
 */
public class ApiUpdate {
    public static String API_BASE_URL = "http://47.115.230.91:8080";
    public static final ApiUpdate INSTANCE = new ApiUpdate();
    public static String GETALL = "/update/updateAll";//查询所有动态
    public static String GETBY = "/update/updateBy";//查询指定司机动态
    public static String ADD = "/update/updateAdd";//发布动态
    public static String CHANGE = "/update/updateChange";//修改动态
    public static String IMAGE = "/file/updateImg";//修改动态

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
    public void getAll(Api.Callback<ApiResult<List<Update>>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + GETALL).get().build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<List<Update>> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<List<Update>>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 根据司机ID查询动态
     * 🐢
     */
    public void getBy(String driver_id, Api.Callback<ApiResult<List<Update>>> callback) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("driver_id", driver_id);

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
                    ApiResult<List<Update>> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<List<Update>>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 发布动态
     * 🐢
     */
    public void addInfo(String driver_id,String driver_name,String title,String content,String time, Callback<ApiResult<Integer>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("driver_id", driver_id)
                .add("driver_name", driver_name)
                .add("title", title)
                .add("content", content)
                .add("time", time)
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
                    ApiResult<Integer> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<Integer>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 上传动态图片
     * 🐢
     */
    public void upload(File file, String update_id, Callback<ApiResult<String>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("file",file.getName(), RequestBody.create(file, MediaType.parse("image/*")));
        multipartBodyBuilder.addFormDataPart("update_id",update_id);

        Request request = new Request.Builder().url(API_BASE_URL + IMAGE).post(multipartBodyBuilder.build()).build();

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

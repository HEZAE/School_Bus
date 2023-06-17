package com.google.android.api;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.ui.mine.bean.DriverBean;
import com.google.android.ui.mine.bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
public class Api {
    public static String API_BASE_URL = "http://47.115.230.91:8080";
    public static final Api INSTANCE = new Api();
    public static String SELECTONE = "/account/selectOne";//根据ID查询用户
    public static String LOGIN = "/account/login";//用户登录
    public static String REGISTER = "/account/register";//用户注册
    public static String UPDATE = "/accountInfo/updateUser";//用户修改信息
    public static String USERHEAD = "/file/userHead";//用户修改头像
    public static String ADDINFO = "/accountInfo/addInfo";//添加默认用户信息
    public static String SELECTD = "/account/selectDriver";//根据ID查询司机
    public static String DLOGIN = "/account/driverLogin";//司机登录
    public static String DREGISTER = "/account/driverRegister";//司机注册
    public static String UPDATED = "/accountInfo/updateDriver";//司机修改信息
    public static String DRIVERHEAD = "/file/driverHead";//用户修改头像
    public static String ADDINFOD = "/accountInfo/addDriverInfo";//添加默认司机信息

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 用户插入基本信息
     * 🐢
     */
    public void addInfo(String id, Callback<ApiResult<String>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("id", id)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + ADDINFO).post(formBody).build();

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
     * 用户注册
     * 🐢
     */
    public void register(String user_acc, String password, Callback<ApiResult<String>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("user_acc", user_acc)
                .add("password", password)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + REGISTER).post(formBody).build();

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
     * 用户登陆
     * 🐢
     */
    public void login(String user_acc, String password, Callback<ApiResult<Integer>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("user_acc", user_acc)
                .add("password", password)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + LOGIN).post(formBody).build();

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
     * 根据ID查询用户
     * 🐢
     */
    public void selectOne(String id, Callback<ApiResult<UserBean>> callback) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("id", id);

        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + SELECTONE + getBodyParams(bodyParams)).get().build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<UserBean> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<UserBean>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 用户头像上传
     * 🐢
     */
    public void upload(File file, String id, Callback<ApiResult<String>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("file",file.getName(), RequestBody.create(file, MediaType.parse("image/*")));
        multipartBodyBuilder.addFormDataPart("id",id);

        Request request = new Request.Builder().url(API_BASE_URL + USERHEAD).post(multipartBodyBuilder.build()).build();

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
     * 司机插入基本信息
     * 🐢
     */
    public void addInfod(String driver_id, Callback<ApiResult<String>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("driver_id", driver_id)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + ADDINFOD).post(formBody).build();

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
     * 用户修改信息
     * 🐢
     */
    public void update(String id, String username, String sex, String age,Callback<ApiResult<UserBean>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();

        FormBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("username", username)
                .add("sex", sex)
                .add("age", age)
                .build();
        Request request = new Request.Builder().url(API_BASE_URL + UPDATE).post(formBody).build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<UserBean> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<UserBean>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    /**
     * 司机注册
     * 🐢
     */
    public void registerDriver(String driver_acc, String driver_password, Callback<ApiResult<String>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("driver_acc", driver_acc)
                .add("driver_password", driver_password)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + DREGISTER).post(formBody).build();

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
     * 司机登陆
     * 🐢
     */
    public void driverLogin(String driver_acc, String driver_password, Callback<ApiResult<Integer>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("driver_acc", driver_acc)
                .add("driver_password", driver_password)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + DLOGIN).post(formBody).build();

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
     * 根据ID查询司机
     * 🐢
     */
    public void selectDriver(String driver_id, Callback<ApiResult<DriverBean>> callback) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("driver_id", driver_id);

        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL + SELECTD + getBodyParams(bodyParams)).get().build();

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
     * 用户头像上传
     * 🐢
     */
    public void uploadD(File file, String driver_id, Callback<ApiResult<String>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("file",file.getName(), RequestBody.create(file, MediaType.parse("image/*")));
        multipartBodyBuilder.addFormDataPart("driver_id",driver_id);

        Request request = new Request.Builder().url(API_BASE_URL + DRIVERHEAD).post(multipartBodyBuilder.build()).build();

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
     * 用户修改信息
     * 🐢
     */
    public void updateD(String driver_id, String username, String sex, String age,Callback<ApiResult<DriverBean>> callback) {
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();

        FormBody formBody = new FormBody.Builder()
                .add("driver_id", driver_id)
                .add("username", username)
                .add("sex", sex)
                .add("age", age)
                .build();
        Request request = new Request.Builder().url(API_BASE_URL + UPDATED).post(formBody).build();

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

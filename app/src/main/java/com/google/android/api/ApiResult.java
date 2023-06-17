package com.google.android.api;

import androidx.annotation.NonNull;

/**
 * author : lancer 🐢
 * e-mail : lancer2ooo@qq.com
 * desc   : hello word
 */
public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    @NonNull
    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

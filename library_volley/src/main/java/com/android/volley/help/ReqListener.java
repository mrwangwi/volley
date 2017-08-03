package com.android.volley.help;

import com.android.volley.manager.RequestManager;

import java.util.Map;

/**
 * Created by lenovo on 2016/6/6.
 */
public class ReqListener<T> implements RequestManager.RequestListener {

    private RequestSuccessError<T> requestSuccessError;
    private SuccessError<T> successError;
    private Success<T> success;
    private Class<T> tClass;

    public ReqListener(Class<T> tClass, RequestSuccessError<T> on) {
        requestSuccessError = on;
        this.tClass = tClass;
    }

    public ReqListener(Class<T> tClass, SuccessError<T> on) {
        successError = on;
        this.tClass = tClass;
    }

    public ReqListener(Class<T> tClass, Success<T> on) {
        success = on;
        this.tClass = tClass;
    }


    @Override
    public void onRequest() {
        if (requestSuccessError != null) {
            requestSuccessError.onRequest();
        } else {

        }
    }

    @Override
    public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
        if (requestSuccessError != null) {
            requestSuccessError.onSuccess(JsonUtil.fastBean(response, tClass));
        } else if (successError != null) {
            successError.onSuccess(JsonUtil.fastBean(response, tClass));
        } else {
            success.onSuccess(JsonUtil.fastBean(response, tClass));
        }
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        if (requestSuccessError != null) {
            requestSuccessError.onError(errorMsg);
        } else if (successError != null) {
            successError.onError(errorMsg);
        }
    }
}

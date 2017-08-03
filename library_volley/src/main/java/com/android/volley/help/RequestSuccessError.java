package com.android.volley.help;

/**
 * Created by lenovo on 2016/6/6.
 */
public interface RequestSuccessError<T> {

    void onRequest();

    void onSuccess(T t);

    void onError(String errorMsg);

}

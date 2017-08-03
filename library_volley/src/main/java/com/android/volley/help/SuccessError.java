package com.android.volley.help;

/**
 * Created by lenovo on 2016/6/6.
 */
public interface SuccessError<T> {

    void onSuccess(T t);

    void onError(String errorMsg);

}

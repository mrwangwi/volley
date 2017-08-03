package com.zy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.help.ReqListener;
import com.android.volley.help.RequestSuccessError;
import com.android.volley.help.Success;
import com.android.volley.help.SuccessError;
import com.android.volley.manager.RequestManager;
import com.zy.myapplication.bean.Test;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String url = "https://api.domi360.com/index.php/v1/config/getTimeStamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, String> data = new HashMap<>();
        data.put("key", "value");

        //post Success
        RequestManager.getInstance().post(url, data, new ReqListener<>(Test.class, new Success<Test>() {
            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }
        }));


        //post Success和Error
        RequestManager.getInstance().post(url, data, new ReqListener<>(Test.class, new SuccessError<Test>() {
            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }

            @Override
            public void onError(String errorMsg) {

            }
        }));


        //post Request和Success和Error
        RequestManager.getInstance().post(url, data, new ReqListener<>(Test.class, new RequestSuccessError<Test>() {
            @Override
            public void onRequest() {

            }

            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }

            @Override
            public void onError(String errorMsg) {

            }
        }));


        //get
        RequestManager.getInstance().get(url, new ReqListener<>(Test.class, new SuccessError<Test>() {
            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }

            @Override
            public void onError(String errorMsg) {

            }
        }));

        //添加header
        HashMap<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        //post
        RequestManager.getInstance().post(url, data, new ReqListener<>(Test.class, new Success<Test>() {
            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }
        }), headers);
        //get
        RequestManager.getInstance().get(url, data, new ReqListener<>(Test.class, new Success<Test>() {
            @Override
            public void onSuccess(Test test) {
                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());
            }
        }), headers);
    }
}

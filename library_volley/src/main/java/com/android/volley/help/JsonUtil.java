package com.android.volley.help;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/6/6.
 */
public class JsonUtil {


    /**
     * 解析得到Code
     *
     * @param response
     * @return
     */
    public static int getCode(String response) {
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            return object.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * response字符串转JSONObject
     *
     * @param response
     * @return
     */
    public static JSONObject getJSONObject(String response) {
        JSONObject object = null;
        try {
            object = new JSONObject(response);
        } catch (JSONException e) {

        }
        return object;
    }


    /**
     * data字符串转JSONObject
     *
     * @param response
     * @return
     */
    public static JSONObject getData(String response) {
        try {
            return getJSONObject(response).getJSONObject("data");
        } catch (JSONException e) {

        }
        return null;
    }


    /**
     * 通过键获取对应值
     *
     * @param object JSONObject对象
     * @param key    键
     * @return
     */
    public static String getString(JSONObject object, String key) {

        try {
            return object.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过键获取对应值
     *
     * @param object JSONObject对象
     * @param key    键
     * @return
     */
    public static boolean getBoolean(JSONObject object, String key) {

        try {
            return object.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 提示错误信息
     *
     * @param response
     * @return
     */
    public static String getMessage(String response) {
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            return object.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取存管url
     *
     * @param response
     * @return
     */
    public static String getDepositoryUrl(String response) {
        JSONObject object = null;
        try {
            object = getData(response);
            return object.getString("redirect");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析实体
     *
     * @param response
     * @return
     */
    public static <T> T fastBean(String response, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(response, classOfT);
    }


    public static <T> List<T> fastBeanList(String response, Class<T> classOfT) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JSONArray jsonArray = null;
        try {
            jsonArray = getData(response).getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(gson.fromJson(jsonArray.get(i).toString(), classOfT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}

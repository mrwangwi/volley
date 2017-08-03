package com.android.volley.manager;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RequestManager
 *
 * @author steven pan
 */
public class RequestManager {

    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String GZIP = "gzip";

    private static final int TIMEOUT_COUNT = 10 * 1000;
    private static final int RETRY_TIMES = 1;

    private volatile static RequestManager INSTANCE = null;
    private RequestQueue mRequestQueue = null;

    /**
     * Request Result Callback
     */
    public interface RequestListener {

        void onRequest();

        void onSuccess(String response, Map<String, String> headers,
                       String url, int actionId);

        void onError(String errorMsg, String url, int actionId);
    }

    private RequestManager() {

    }

    public void init(Context context) {
        this.mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * SingleTon
     *
     * @return single Instance
     */
    public static RequestManager getInstance() {
        if (null == INSTANCE) {
            synchronized (RequestManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new RequestManager();
                }
            }
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    /**
     * default get method
     *
     * @param url
     * @param requestListener
     * @param actionId
     * @return LoadController object
     */
    public LoadController get(String url, RequestListener requestListener,
                              int actionId) {
        return this.get(url, requestListener, true, actionId);
    }

    public LoadController get(String url, RequestListener requestListener) {
        return this.get(url, requestListener, true, 0);
    }

    public LoadController get(String url, RequestListener requestListener,
                              boolean shouldCache, int actionId) {
        return this.request(Method.GET, url, null, null, requestListener,
                shouldCache, TIMEOUT_COUNT, RETRY_TIMES, actionId);
    }


    //post 无 action
    public LoadController post(final String url, Object data,
                               final RequestListener requestListener) {
        return this.post(url, data, requestListener, false, TIMEOUT_COUNT,
                RETRY_TIMES, 0);
    }

    //post 带 action
    public LoadController post(final String url, Object data,
                               final RequestListener requestListener, int actionId) {
        return this.post(url, data, requestListener, false, TIMEOUT_COUNT,
                RETRY_TIMES, actionId);
    }

    //post 带 headers
    public LoadController post(final String url, Object data,
                               final RequestListener requestListener, final Map<String, String> headers) {
        return this.post(url, data, requestListener, false, TIMEOUT_COUNT,
                RETRY_TIMES, headers);
    }

    //get 无 headers
    public LoadController get(final String url, Object data,
                              final RequestListener requestListener) {
        return request(Method.GET, url, data, null, requestListener,
                false, TIMEOUT_COUNT, RETRY_TIMES, null);
    }

    //get 带 headers
    public LoadController get(final String url, Object data,
                              final RequestListener requestListener, final Map<String, String> headers) {
        return request(Method.GET, url, data, null, requestListener,
                false, TIMEOUT_COUNT, RETRY_TIMES, headers);
    }

    /**
     * @param url
     * @param data            String, Map<String, String> or RequestMap(with file)
     * @param requestListener
     * @param shouldCache
     * @param timeoutCount
     * @param retryTimes
     * @param actionId
     * @return LoadController object
     */
    public LoadController post(final String url, Object data,
                               final RequestListener requestListener, boolean shouldCache,
                               int timeoutCount, int retryTimes, int actionId) {
        return request(Method.POST, url, data, null, requestListener,
                shouldCache, timeoutCount, retryTimes, actionId);
    }

    public LoadController post(final String url, Object data,
                               final RequestListener requestListener, boolean shouldCache,
                               int timeoutCount, int retryTimes, final Map<String, String> headers) {
        return request(Method.POST, url, data, null, requestListener,
                shouldCache, timeoutCount, retryTimes, headers);
    }

    /**
     * request
     *
     * @param method          mainly Method.POST and Method.GET
     * @param url             target url
     * @param data            request params
     * @param headers         request headers
     * @param requestListener request callback
     * @param shouldCache     useCache
     * @param timeoutCount    reqeust timeout count
     * @param retryTimes      reqeust retry times
     * @param actionId        request id
     * @return LoadController object
     */
    public LoadController request(int method, final String url, Object data,
                                  final Map<String, String> headers,
                                  final RequestListener requestListener, boolean shouldCache,
                                  int timeoutCount, int retryTimes, int actionId) {
        return this.sendRequest(method, url, data, headers,
                new RequestListenerHolder(requestListener), shouldCache,
                timeoutCount, retryTimes, actionId);
    }

    public LoadController request(int method, final String url, Object data,
                                  final Map<String, String> headers,
                                  final RequestListener requestListener, boolean shouldCache,
                                  int timeoutCount, int retryTimes, final Map<String, String> headers1) {
        return this.sendRequest(method, url, data, headers,
                new RequestListenerHolder(requestListener), shouldCache,
                timeoutCount, retryTimes, headers1);
    }

    /**
     * @param method
     * @param url
     * @param data
     * @param headers
     * @param requestListener
     * @param shouldCache
     * @param timeoutCount
     * @param retryTimes
     * @param actionId
     * @return LoadController object
     */
    public LoadController sendRequest(int method, final String url, Object data,
                                      final Map<String, String> headers,
                                      final LoadListener requestListener, boolean shouldCache,
                                      int timeoutCount, int retryTimes, int actionId) {
        if (requestListener == null)
            throw new NullPointerException();

        final ByteArrayLoadController loadController = new ByteArrayLoadController(
                requestListener, actionId);

        Request<?> request = null;
        if (data != null) {
            if (Method.POST == method) { // force POST and No Cache
                request = new ByteArrayRequest(Method.POST, url, data,
                        loadController, loadController);
                request.setShouldCache(false);
            } else if (Method.GET == method) {
                String combineUrl = url;
                if (data instanceof RequestMap) {
                    combineUrl = getCombineUrl(url, (RequestMap) data);
                }
                request = new ByteArrayRequest(method, combineUrl, null, loadController, loadController);
                request.setShouldCache(shouldCache);
            }
        } else {
            request = new ByteArrayRequest(method, url, null, loadController, loadController);
            request.setShouldCache(shouldCache);
        }

        //set request header
        Map<String, String> requestHeaders = new HashMap<String, String>();
        if (headers != null && !headers.isEmpty()) {
            requestHeaders.putAll(headers);
        }


        if (requestHeaders.get(ACCEPT_ENCODING) == null) {
            requestHeaders.put(ACCEPT_ENCODING, GZIP);
        }
        try {
            request.getHeaders().putAll(requestHeaders);
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }

        //set request retry policy
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutCount,
                retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        loadController.bindRequest(request);

        if (this.mRequestQueue == null)
            throw new NullPointerException();
        requestListener.onStart();
        this.mRequestQueue.add(request);

        return loadController;
    }

    private String getCombineUrl(String url, RequestMap data) {
        StringBuilder sb = new StringBuilder(url);
        String paramString = data.getParamsString(true);
        if (!TextUtils.isEmpty(paramString)) {
            sb.append(paramString);
        }
        return sb.toString();
    }


    public LoadController sendRequest(int method, final String url, Object data,
                                      final Map<String, String> headers,
                                      final LoadListener requestListener, boolean shouldCache,
                                      int timeoutCount, int retryTimes, final Map<String, String> headers1) {
        if (requestListener == null)
            throw new NullPointerException();

        final ByteArrayLoadController loadController = new ByteArrayLoadController(
                requestListener, 0);

        Request<?> request = null;
        if (data != null) {
            if (Method.POST == method) { // force POST and No Cache
                request = new ByteArrayRequest(Method.POST, url, data,
                        loadController, loadController);
                request.setShouldCache(false);
            } else if (Method.GET == method) {
                String combineUrl = url;
                HashMap<String, String> hashMap = (HashMap<String, String>) data;
                RequestMap requestMap = new RequestMap();
                Set<String> set = hashMap.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    requestMap.put(s, hashMap.get(s));
                }
                combineUrl = getCombineUrl(url, requestMap);
                request = new ByteArrayRequest(method, combineUrl, null, loadController, loadController);
                request.setShouldCache(shouldCache);
            }
        } else {
            request = new ByteArrayRequest(method, url, null, loadController, loadController);
            request.setShouldCache(shouldCache);
        }

        //set request header
        Map<String, String> requestHeaders = new HashMap<String, String>();
        if (headers1 != null && !headers1.isEmpty()) {
            requestHeaders.putAll(headers1);
        }
        //自定义加头信息

        if (requestHeaders.get(ACCEPT_ENCODING) == null) {
            requestHeaders.put(ACCEPT_ENCODING, GZIP);
        }
        try {
            request.getHeaders().putAll(requestHeaders);
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }

        //set request retry policy
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutCount,
                retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        loadController.bindRequest(request);

        if (this.mRequestQueue == null)
            throw new NullPointerException();
        requestListener.onStart();
        this.mRequestQueue.add(request);

        return loadController;
    }

}

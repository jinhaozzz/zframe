package me.zjh.zframe.net.asynchttpclient;

import android.content.Context;


import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.entity.StringEntity;
import me.zjh.zframe.base.AppManager;
import me.zjh.zframe.base.BaseApplication;
import me.zjh.zframe.net.BaseNetCenter;
import me.zjh.zframe.net.NetResponseListener;
import me.zjh.zframe.net.vo.BaseRequest;
import me.zjh.zframe.net.vo.BaseResponse;


/**
 * AsyncHttpClient实现的网络请求管理
 * <p/>
 * Created by zjh on 2015/12/3.
 */
public class AsyncHttpNetCenter extends BaseNetCenter {

    private static AsyncHttpNetCenter instance;

    private AsyncHttpNetCenter() {
        super();
    }

    public static AsyncHttpNetCenter getInstance() {
        if (instance == null) {
            instance = new AsyncHttpNetCenter();
        }

        return instance;
    }

    private AsyncHttpClient mAsyncHttpClient;

    @Override
    protected void initHttpClient() {
        mAsyncHttpClient = new AsyncHttpClient();
        mAsyncHttpClient.setConnectTimeout(CONNECT_TIMEOUT);
        mAsyncHttpClient.setMaxConnections(MAX_CONNECTIONS);
        mAsyncHttpClient.setMaxRetriesAndTimeout(MAX_RETRIES, RETRIES_TIMEOUT);
        mAsyncHttpClient.setResponseTimeout(RESPONSE_TIMEOUT);
    }

    @Override
    public void clearRequestQueue(Context context) {
        mAsyncHttpClient.cancelRequests(context, true);
    }

    /**
     * 发送GET请求
     */
    public <T extends BaseResponse> void get(BaseRequest request, final Class<T> response, final NetResponseListener<T> listener) {
        String url = getUrlWithParam(BaseApplication.getInstance().appBaseUrl() + request.getPath(), request.getMapParams());
        Map<String, String> headParam = request.getHeader().exportAsDictionary();
        insertAllHeaders(headParam);

        Context context = AppManager.getAppManager().currentActivity();
        mAsyncHttpClient.get(context, url, new JSONHttpResponseHandler<T>(CONTENT_ENCODING, response, listener));
    }

    /**
     * 发送POST请求
     */
    public <T extends BaseResponse> void post(BaseRequest request, final Class<T> response, final NetResponseListener<T> listener) {
        String url = BaseApplication.getInstance().appBaseUrl() + request.getPath();
        Map<String, String> headParam = request.getHeader().exportAsDictionary();
        insertAllHeaders(headParam);

        Map<String, Object> reqParam = request.getMapParams();

        Context context = AppManager.getAppManager().currentActivity();
        mAsyncHttpClient.post(context, url, getRequestEntity(reqParam), CONTENT_TYPE, new JSONHttpResponseHandler<T>(CONTENT_ENCODING, response, listener));
    }

    /**
     * 发送PUT请求
     */
    public <T extends BaseResponse> void put(BaseRequest request, final Class<T> response, final NetResponseListener<T> listener) {
        String url = BaseApplication.getInstance().appBaseUrl() + request.getPath();
        Map<String, String> headParam = request.getHeader().exportAsDictionary();
        insertAllHeaders(headParam);

        Map<String, Object> reqParam = request.getMapParams();

        Context context = AppManager.getAppManager().currentActivity();
        mAsyncHttpClient.put(context, url, getRequestEntity(reqParam), CONTENT_TYPE, new JSONHttpResponseHandler<T>(CONTENT_ENCODING, response, listener));
    }

    /**
     * 发送DELETE请求
     */
    public <T extends BaseResponse> void delete(BaseRequest request, final Class<T> response, final NetResponseListener<T> listener) {
        String url = BaseApplication.getInstance().appBaseUrl() + request.getPath();
        Map<String, String> headParam = request.getHeader().exportAsDictionary();
        insertAllHeaders(headParam);

        Map<String, Object> reqParam = request.getMapParams();

        Context context = AppManager.getAppManager().currentActivity();
        mAsyncHttpClient.delete(context, url, getRequestEntity(reqParam), CONTENT_TYPE, new JSONHttpResponseHandler<T>(CONTENT_ENCODING, response, listener));
    }
    
    private void insertAllHeaders(Map<String, String> header) {
        Set<String> headerKey = header.keySet();
        Iterator<String> iterator = headerKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = header.get(key);

            mAsyncHttpClient.addHeader(key, value);
        }
    }

    private StringEntity getRequestEntity(Map<String, Object> reqParam) {
        String bodyJson = JSON.toJSONString(reqParam);
        StringEntity entity = new StringEntity(bodyJson, CONTENT_ENCODING);

        return entity;
    }

    public static String getUrlWithParam(String url, Map<String, Object> param) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (result.length() > 0)
                result.append("&");
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        // Only add the query string if it isn't empty and it
        // isn't equal to '?'.
        String paramString = result.toString();
        if (!paramString.equals("") && !paramString.equals("?")) {
            url += url.contains("?") ? "&" : "?";
            url += paramString;
        }
        return url;
    }

}

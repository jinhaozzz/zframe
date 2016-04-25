package me.zjh.zframe.net.asynchttpclient;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import me.zjh.zframe.net.NetResponseListener;
import me.zjh.zframe.net.vo.BaseResponse;

/**
 * 使用asynchttpclient网络请求的响应处理
 *
 * Created by zjh on 2015/12/4.
 */
public class JSONHttpResponseHandler<T> extends TextHttpResponseHandler {

    private final Class<T> mClazz;

    private final NetResponseListener<T> mListener;

    public JSONHttpResponseHandler(String contentEncoding, Class<T> clazz, NetResponseListener<T> listener) {
        setCharset(contentEncoding);
        this.mClazz = clazz;
        this.mListener = listener;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        T t = JSON.parseObject(responseString, mClazz);
        if (t instanceof BaseResponse) {
            BaseResponse resp = (BaseResponse) t;

            // TODO mmm 需要根据不同的接口返回类型进行自定义
            // --------------儿童机-----------------------
            if (1 == resp.result) {
                mListener.onSuccess(t);
            } else {
                mListener.onFail(resp.messageCode + " " + resp.message);
            }
            // --------------儿童机-----------------------

            // --------------leancloud-----------------------
                    /*if (0 == resp.code) {
                        listener.onSuccess(t);
                    } else {
                        listener.onFail(resp.code + " " + resp.error);
                    }*/
            // --------------leancloud-----------------------
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        mListener.onFail(responseString);
    }
}

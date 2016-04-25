package me.zjh.zframe.net;

/**
 * 网络请求的相应接听
 *
 * @author ZJH
 *         created at 2015/11/13 17:15
 */
public interface NetResponseListener<T> {

    public void onSuccess(T t);

    public void onFail(String error);

}

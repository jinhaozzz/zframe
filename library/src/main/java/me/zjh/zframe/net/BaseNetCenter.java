package me.zjh.zframe.net;

import android.content.Context;

/**
 * 基于第三方网络框架封装的网络访问类
 * 网络访问控制中心 用于统一管理网络访问及初始化网络相关配置
 *
 * @author zjh
 */
public abstract class BaseNetCenter {

    protected final int CONNECT_TIMEOUT = 5 * 1000;
    protected final int MAX_CONNECTIONS = 5;
    protected final int MAX_RETRIES = 0;
    protected final int RETRIES_TIMEOUT = 5 * 1000;
    protected final int RESPONSE_TIMEOUT = 15 * 1000;

    protected final String CONTENT_ENCODING = "UTF-8";
    protected final String CONTENT_TYPE = "application/json";

    public BaseNetCenter() {
        initHttpClient();
    }

    protected abstract void initHttpClient();

    public abstract void clearRequestQueue(Context context);
}

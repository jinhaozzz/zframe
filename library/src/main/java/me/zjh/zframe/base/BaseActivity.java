package me.zjh.zframe.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.zjh.zframe.net.asynchttpclient.AsyncHttpNetCenter;


/**
 * Activity基类
 *
 * @author ZJH
 *         created at 2015/10/29 15:57
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        this.mActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AsyncHttpNetCenter.getInstance().clearRequestQueue(this);
        AppManager.getAppManager().finishActivity(this);
    }

}

package me.zjh.zframe.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.Locale;

/**
 * application基类，提供一些基础方法
 *
 * @author ZJH
 *         created at 2015/10/31 14:50
 */
public abstract class BaseApplication extends Application {

    private static BaseApplication mInstance;

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    /**
     * SD卡的路径
     */
    private String sdCardPath;

    /**
     * app工程目录
     */
    private String projPath;

    /**
     * app文件及缓存目录
     *
     * @return 目录名称
     */
    public abstract String appCacheFolderName();

    /**
     * app的网络请求基础url
     *
     * @return
     */
    public abstract String appBaseUrl();

    /**
     * 是否是开发模式。
     *
     * @return 开发中true。打包false。
     */
    public abstract boolean devMode();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        initScreenSize();
        initCacheFolder();

        initException();
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * 初始化屏幕尺寸
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

    /**
     * 初始化缓存目录
     */
    private void initCacheFolder() {
        projPath = sdCardPath + File.separator + getFolderName() + File.separator;

        String logPath = projPath + AppConfig.FOLDER_CHILD_LOG;
        String imagePath = projPath + AppConfig.FOLDER_CHILD_IMAGE;

        File logFile = new File(logPath);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
    }

    /**
     * 初始化异常
     */
    private void initException() {
        AppException.getInstance().init(getFolderName() + ".log");
    }

    /**
     * 获取工程sd卡目录
     * @return
     */
    private String getFolderName() {
        String folderName;
        if (!TextUtils.isEmpty(appCacheFolderName().trim())) {
            folderName = appCacheFolderName();
        } else {
            folderName = AppConfig.DEFAULT_APP_FOLDER;
        }

        return folderName;
    }

    /**
     * 获取项目路径
     * @return
     */
    public String getProjPath() {
        return projPath;
    }

    public String getVersion() {
        try {
            PackageManager manager = mInstance.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mInstance.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLanguage() {
        Locale locale = mInstance.getResources().getConfiguration().locale;
        String language = locale.getDefault().toString();
        return language;
    }


}

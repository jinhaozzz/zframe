package me.zjh.zframe.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * App程序异常：用于捕获异常和提示错误信息
 *
 * @author zjh
 */
public class AppException extends Exception implements UncaughtExceptionHandler {
    public static final String TAG = "AppException";

    private String exLogName;

    private UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    private static AppException INSTANCE = new AppException();

    private AppException() {
    }

    public static AppException getInstance() {
        return INSTANCE;
    }

    public void init(String exLogName) {
        this.exLogName = exLogName;

        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 用于弹出框和获取手机信息
        mContext = AppManager.getAppManager().currentActivity();

        // 如果异常处理失败，则使用系统默认的handler进行处理
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果错误日志处理失败，则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            forceCloseApp();
        }
    }

    /**
     * 处理错误日志,打印到控制台并保存到sd卡中
     *
     * @param ex 异常
     * @return
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        boolean success = true;
        try {
            // 打印错误日志
            Log.e(TAG, Log.getStackTraceString(ex));

            // 保存到sd卡
            success = saveToSDCard(ex);
        } catch (Exception e) {
        }

        return success;
    }

    /**
     * 强制关闭app
     */
    private void forceCloseApp() {

        // 显示异常信息&发送报告
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出了点问题...", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        // android5.0后,有一个问题,如果在以上looper中使用dialog或者他oast,不睡眠1s中,则在出现异常的Activity的OnCreate中会无法展示dialog.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AppManager.getAppManager().finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(-1);
    }

    private boolean saveToSDCard(Throwable ex)
            throws Exception {
        boolean append = false;
        File file = getSaveFile(exLogName);
        if (System.currentTimeMillis() - file.lastModified() > 5000) {
            append = true;
        }
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
        // 导出发生异常的时间
        pw.println(getDataTime("yyyy-MM-dd-HH-mm-ss"));
        // 导出手机信息
        dumpPhoneInfo(pw);
        pw.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(pw);
        pw.println();
        pw.close();
        return append;
    }

    private void dumpPhoneInfo(PrintWriter pw)
            throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }

    private String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    private File getSaveFile(String fileNmae) {
        File file = new File(getSaveFolderPath() + File.separator + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private String getSaveFolderPath() {
        File dirFile = new File(BaseApplication.getInstance().getProjPath() + AppConfig.FOLDER_CHILD_LOG);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        return dirFile.getAbsolutePath();
    }

}

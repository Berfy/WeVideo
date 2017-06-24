package we.video.wevideo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.ui.my.LoginActivity;

/**
 * @author Berfy
 */
public class DeviceUtil {

    /**
     * get IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();

        return imsi;
    }

    /**
     * get IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();

        if (imei == null || imei.length() <= 0) {

            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);

                imei = (String) get.invoke(c, "ro.serialno");
            } catch (SecurityException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            } catch (IllegalArgumentException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            } catch (ClassNotFoundException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            } catch (NoSuchMethodException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            } catch (InvocationTargetException e) {
                LogUtil.e("DeviceUtil", e.getLocalizedMessage());
            }
        }
        return imei;
    }

    // 获取包名
    public static String getPackageName(Context ctx) {
        return ctx.getPackageName();
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    public static String getSimCardNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    public static String getSimProvider(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        return IMSI;
    }

    /**
     * get sdk version
     *
     * @return
     */
    public static int getSdkversion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * get package version
     *
     * @return
     */
    public static int getPackageVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * get package version
     *
     * @return
     */
    public static String getPackageVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "1.0";
        }
    }

    /**
     * get package version
     *
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    /**
     * get Screen Width
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        return getDisplay(context).getWidth();
    }

    /**
     * 获取4.x以上系统menu导航栏高度
     */
    @SuppressLint("NewApi")
    public static int getBottoMenuHeight(Context context) {
        Resources resources = context.getResources();
        if (checkDeviceHasNavigationBar(context)) {
            LogUtil.e("有导航栏", "");
            int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid > 0) {
                LogUtil.e("显示导航栏", "");
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    LogUtil.e("导航栏高度", "" + resources.getDimensionPixelSize(resourceId));
                    return resources.getDimensionPixelSize(resourceId);
                }
            }
        }
        return 0;
    }

    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context context) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * get Screen Heght
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        return getDisplay(context).getHeight();
    }

    /**
     * get Screen Density
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.densityDpi;
    }

    /**
     * get Screen Density
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static float getScreenScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /** 获取状态栏高度
     * @param v
     * @return
     */
    public static int getStatusBarHeight(View v) {
        if (v == null) {
            return 0;
        }
        Rect frame = new Rect();
        v.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * get Display
     *
     * @param context
     * @return
     */
    private static Display getDisplay(Context context) {
        Display display = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display;
    }

    /**
     * 打开键盘
     *
     * @param context
     */
    public static void openKeyboard(Context context) {
        ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 传�?token关闭键盘
     *
     * @param context
     * @param token
     */
    public static void closeKeyboard(Context context, IBinder token) {
        ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(token, 0);
    }

    public static boolean isAliveIme(Context context) {
        InputMethodManager m = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m.isActive()) {
            return true;
        }
        return false;
    }

    /**
     * 获取本机ip
     */
//    public static InetAddress getLocalIpAddress(Context context)
//            throws UnknownHostException {
//        WifiManager wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        return InetAddress.getByName(String.format("%d.%d.%d.%d",
//                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
//                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
//    }
    public static String logMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        return "total:" + maxMemory + ",current:" + totalMemory;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * (getScreenDensity(context) / 160f));
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue * 160) / getScreenDensity(context));
    }

    public static float sp2px(Context context, float sp) {
        return sp * getScreenScaledDensity(context);
    }

    public static void fillScree(Activity activity) {
        // 无title
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static int getUid(Context context) {
        int uid = 0;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = context.getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                uid = runningProcess.uid;
                break;
            }
        }
        return uid;
    }

    public static void toLogin(Context context, boolean isTip) {
        if (isTip)
            ToastUtil.getInstance().showToast(context.getString(R.string.tip_login));
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}

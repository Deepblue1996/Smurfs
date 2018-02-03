package com.prohua.smurfs.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prohua.smurfs.application.SmurfsApplication;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.FormBody;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * UtilTools 工具类
 * Created by Deep on 2016/11/1 0001.
 */

public class UtilTools {

    /**
     * 不可以实例
     */
    private UtilTools() {
    }

    /**
     * 获取主视图
     *
     * @param _mActivity 主Activity
     * @return
     */
    public static View getMainActivityView(SupportActivity _mActivity) {
        return _mActivity.getSupportFragmentManager().getFragments().get(1).getView();
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 检查当前网络是否可用
     *
     * @param activity 传入Activity
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        // 获取资源图片

        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, opt);

    }

    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String getDateDiff(String dateStr) throws ParseException {

        long dateTimeStamp = stringToLong(dateStr, "yyyy-MM-dd HH:mm:ss");
        int minute = 1000 * 60;
        int hour = minute * 60;
        int day = hour * 24;
        int month = day * 30;
        long now = new Date().getTime();
        long diffValue = now - dateTimeStamp;
        if (diffValue <= 0)
            return "刚刚";
        long monthC = diffValue / month;
        long weekC = diffValue / (7 * day);
        long dayC = diffValue / day;
        long hourC = diffValue / hour;
        long minC = diffValue / minute;
        String result;
        if (monthC >= 1) {
            result = "" + monthC + "月前";
        } else if (weekC >= 1) {
            result = "" + weekC + "周前";
        } else if (dayC >= 1) {
            result = "" + dayC + "天前";
        } else if (hourC >= 1) {
            result = "" + hourC + "小时前";
        } else if (minC >= 1) {
            result = "" + minC + "分钟前";
        } else
            result = "刚刚";
        return result;
    }

    /**
     * 分割字符串
     *
     * @param str 源字符串
     * @param s   分隔符
     * @return 字符串数组
     */
    public static String[] getSourceStrArray(String str, String s) {
        return str.split(s);
    }

    /**
     * 简化
     *
     * @param context
     * @param imageView
     */
    public static void setGlideInto(Context context, Integer resourceId, ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(false)
                .into(imageView);
    }

    public static String getCookieString(String str) {
        return str.substring(0, str.indexOf(';') + 1);
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            //versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 封装参数
     *
     * @param activity
     * @return
     */
    public static FormBody.Builder paramFromBody() {

        FormBody.Builder builder = new FormBody.Builder()
                .add("appid", SmurfsApplication.applicationInfoBean.getAppId())
                .add("sys_version", SmurfsApplication.applicationInfoBean.getSysVersion())
                .add("version", SmurfsApplication.applicationInfoBean.getVersion())
                .add("imei", "" + SmurfsApplication.applicationInfoBean.getImei())
                .add("platform", SmurfsApplication.applicationInfoBean.getPlatform())
                .add("model", SmurfsApplication.applicationInfoBean.getModel())
                .add("app_version", SmurfsApplication.applicationInfoBean.getAppVersion())
                .add("plus_version", SmurfsApplication.applicationInfoBean.getPlusVersion())
                .add("network_type", SmurfsApplication.applicationInfoBean.getNetWorkType())
                .add("clientid", SmurfsApplication.applicationInfoBean.getClientId())
                .add("iToken", SmurfsApplication.applicationInfoBean.getiToken());

        if (studentInfo != null)
            builder.add("token", studentInfo.getToken());

        return builder;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
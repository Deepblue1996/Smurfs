package com.prohua.smurfs.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.prohua.smurfs.R;
import com.prohua.smurfs.base.MyEventBusIndex;
import com.prohua.smurfs.bean.ApplicationInfoBean;
import com.prohua.smurfs.database.DaoMaster;
import com.prohua.smurfs.database.DaoSession;
import com.prohua.smurfs.database.StudentInfo;
import com.prohua.smurfs.database.StudentInfoDao;
import com.prohua.smurfs.util.PersistentCookieStore;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * SmurfsApplication 应用程序主入口
 * Created by Deep on 2016/12/11 0011.
 */

public class SmurfsApplication extends Application {

    // 全局请求单例
    public static OkHttpClient okHttpClient;
    // 主单例
    public static SmurfsApplication instances;
    // 全局用户信息
    public static StudentInfo studentInfo;
    // 全局cookie自动化本地管理
    public static CookiesManager cookiesManager;

    public static ApplicationInfoBean applicationInfoBean;

    // GreenDao数据库
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static SmurfsApplication getInstances(){
        return instances;
    }

    private class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=30";
            }
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    }

    private OkHttpClient genericClient() {

        //设置缓存路径
        File httpCacheDirectory = new File(getApplicationContext().getCacheDir(), "responses");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        cookiesManager = new CookiesManager();

        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request;
                    request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Connection", "keep-alive")
                            .addHeader("X-Requested-With", "XMLHttpRequest")
                            .build();
                    return chain.proceed(request);
                }).cookieJar(cookiesManager)
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "smurfs.db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        okHttpClient = genericClient();
        // 启用EventBus3.0加速功能
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

        instances = this;

        setDatabase();

        StudentInfoDao studentInfoDao = getDaoSession().getStudentInfoDao();

        studentInfo = studentInfoDao.load(1L);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消所有请求
        okHttpClient.dispatcher().cancelAll();
    }

    /**
     * 自动管理Cookies
     */
    public class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return cookieStore.get(url);
        }

        public PersistentCookieStore getCookieStore() {
            return cookieStore;
        }
    }
}

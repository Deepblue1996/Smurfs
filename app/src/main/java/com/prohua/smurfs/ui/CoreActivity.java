package com.prohua.smurfs.ui;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.prohua.smurfs.R;
import com.prohua.smurfs.application.SmurfsApplication;
import com.prohua.smurfs.bean.ApplicationInfoBean;
import com.prohua.smurfs.ui.logo.LogoFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static com.prohua.smurfs.util.UtilTools.getAppVersionName;

/**
 * 单Activity + 多Fragment ui层设计结构
 * Created by Deep on 2016/12/11 0011.
 */

public class CoreActivity extends SupportActivity {

    /**
     * 初始化参数
     */
    private void initInfo() {

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        String deviceId = "";
        try {
            deviceId = tm.getDeviceId();
        } catch (SecurityException se) {
            se.printStackTrace();
        }

        SmurfsApplication.applicationInfoBean = new ApplicationInfoBean(getString(R.string.app_name),
                "" + android.os.Build.MODEL, getAppVersionName(this), deviceId, "Android", android.os.Build.MODEL,
                getAppVersionName(this), getAppVersionName(this), "" + tm.getNetworkType(), "156", "156");
    }

    /**
     * 单Activity设计 全局初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // 设置沉浸式状态栏，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 5.0以后状态栏官方提供的是变色状态栏方案，所以我们需要另外设置5.0+状态栏为透明色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        // 隐藏标题栏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        // 取消全屏
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mPreferences.edit().putBoolean("fullScreen", false).apply();

        // 加载首个Fragment
        if (savedInstanceState == null)
            loadRootFragment(R.id.fl_container, LogoFragment.newInstance());

        initInfo();
    }

    /**
     * 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
     */
    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    /**
     * 设置竖向(和安卓4.x动画相同)
     * @return
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
}

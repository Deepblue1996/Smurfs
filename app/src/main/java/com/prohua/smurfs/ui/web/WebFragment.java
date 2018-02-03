package com.prohua.smurfs.ui.web;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prohua.smurfs.R;
import com.prohua.smurfs.util.UtilTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * WebFragment 内部WebView
 * Created by Deep on 2017/4/14 0014.
 */

public class WebFragment extends SupportFragment {

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.webs)
    WebView webView;
    @BindView(R.id.back)
    ImageView backImageView;
    @BindView(R.id.titles)
    TextView titleTextView;

    private static String title;
    private static String urls;

    /**
     * 懒加载
     */
    public static WebFragment newInstance(String tit, String str) {

        Bundle args = new Bundle();

        urls = str;
        title = tit;

        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * 创建新的Fragment视图
     *
     * @param inflater           加载布局
     * @param container          view组
     * @param savedInstanceState 通讯（HashMap）
     * @return 视图
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.web_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        webView.loadUrl(urls);
        //设置Web视图
        webView.setWebViewClient(new HelloWebViewClient ());

        backImageView.setOnClickListener(view1 -> pop());

        titleTextView.setText(title);

        return view;
    }

    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);
    }

 /*   *//**
     * 处理回退事件
     *
     * @return true
     *//*
    @Override
    public boolean onBackPressedSupport() {
        webView.goBack(); //goBack()表示返回WebView的上一页面
        return true;
    }*/

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

package com.prohua.smurfs.ui.logo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prohua.smurfs.R;
import com.prohua.smurfs.ui.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * LogoFragment 启动图
 * Created by Deep on 2016/12/11 0011.
 */

public class LogoFragment extends SupportFragment {

    @BindView(R.id.startScreen)
    ImageView startScreen;
    private boolean okStart = true;

    /**
     * 懒加载
     */
    public static LogoFragment newInstance() {

        Bundle args = new Bundle();

        LogoFragment fragment = new LogoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * 创建新的Fragment视图
     * @param inflater 加载布局
     * @param container view组
     * @param savedInstanceState 通讯（HashMap）
     * @return 视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.logo_fragment, container, false);

        ButterKnife.bind(this, view);

        Glide.with(getContext())
                .load(R.mipmap.startscreen)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(startScreen);

        // 这里我做了个判断，如果用户在启动界面显示途中退出，就不执行主界面
        new Handler().postDelayed(() -> {
            if (okStart)
                start(MainFragment.newInstance());
        }, 1000);

        return view;
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        okStart = false;
    }
}

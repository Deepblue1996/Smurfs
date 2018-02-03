package com.prohua.smurfs.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prohua.smurfs.R;
import com.prohua.smurfs.adapter.MyReleaseAdapter;
import com.prohua.smurfs.presenter.ThirdTabPresenter;
import com.prohua.smurfs.util.UtilTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * ThirdTabFragment 互助平台
 * Created by Deep on 2017/3/22 0022.
 */

public class ThirdTabFragment extends SupportFragment implements IThirdTabFragment {

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayoutHeight;
    @BindView(R.id.re)
    RecyclerView recyclerView;

    private ThirdTabPresenter thirdTabPresenter = new ThirdTabPresenter(this);

    /**
     * 懒加载
     */
    public static ThirdTabFragment newInstance() {

        Bundle args = new Bundle();

        ThirdTabFragment fragment = new ThirdTabFragment();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_main_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initView();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyReleaseAdapter myReleaseAdapter = new MyReleaseAdapter(getContext());

        recyclerView.setAdapter(myReleaseAdapter);

        setRefresh();

        return view;
    }

    public void setRefresh() {
        thirdTabPresenter.refresh();
    }

    private void initView() {
        ViewGroup.LayoutParams lp = statusLinearLayoutHeight.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayoutHeight.setLayoutParams(lp);
    }

    @Override
    public void setRecyclerViewVisibility(int visibility) {
        _mActivity.runOnUiThread(() -> recyclerView.setVisibility(visibility));
    }
}

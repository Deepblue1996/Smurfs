package com.prohua.smurfs.presenter;

import android.view.View;

import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.ReleaseTaskListNet;
import com.prohua.smurfs.ui.main.IThirdTabFragment;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class ThirdTabPresenter {
    private IThirdTabFragment mThirdTabFragment;

    public ThirdTabPresenter(IThirdTabFragment mThirdTabFragment) {
        this.mThirdTabFragment = mThirdTabFragment;
    }

    public void refresh() {
        if (studentInfo != null) {
            new ReleaseTaskListNet(new CallBackInterface() {
                @Override
                public void complete() {
                    if ("1".equals(ReleaseTaskListNet.releaseTaskListOutBean.getCode())) {
                        mThirdTabFragment.setRecyclerViewVisibility(View.VISIBLE);
                    } else
                        mThirdTabFragment.setRecyclerViewVisibility(View.GONE);
                }

                @Override
                public void error() {

                }
            }).pullDown();
        } else {
            mThirdTabFragment.setRecyclerViewVisibility(View.GONE);
        }
    }
}

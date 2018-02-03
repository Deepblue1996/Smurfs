package com.prohua.smurfs.ui.main;

import com.prohua.smurfs.ui.widgt.RecyclerBanner;

import java.util.List;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public interface IFirstTabFragment {

    void setNoticeText(String msg);

    void setRecyclerBannerData(List<RecyclerBanner.BannerEntity> data);
}

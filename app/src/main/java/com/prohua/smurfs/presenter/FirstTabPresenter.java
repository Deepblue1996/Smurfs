package com.prohua.smurfs.presenter;

import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.CarouselFigureNet;
import com.prohua.smurfs.net.FirstNoticeNet;
import com.prohua.smurfs.ui.main.FirstTabFragment;
import com.prohua.smurfs.ui.main.IFirstTabFragment;
import com.prohua.smurfs.ui.widgt.RecyclerBanner;

import java.util.ArrayList;
import java.util.List;

import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class FirstTabPresenter {

    private IFirstTabFragment mFirstTabFragment;

    public FirstTabPresenter(IFirstTabFragment iFirstTabFragment) {
        mFirstTabFragment = iFirstTabFragment;
    }

    // 通知
    public void changeNotice() {
        new FirstNoticeNet(new CallBackInterface() {
            @Override
            public void complete() {
                mFirstTabFragment.setNoticeText(FirstNoticeNet.noticeOutBean.getData().getContent());
            }

            @Override
            public void error() {
                mFirstTabFragment.setNoticeText("啊啊啊，加载出错了");
            }
        }).pullDown();
    }

    // 轮播图
    public void changeCarouselFigure() {

        new CarouselFigureNet(new CallBackInterface() {
            @Override
            public void complete() {
                List<RecyclerBanner.BannerEntity> urls = new ArrayList<>();
                for (int i = 0; i < CarouselFigureNet.carouselFigureOutBean.getData().size(); i++)
                    urls.add(new Entity(SEVER_ADDRESS + CarouselFigureNet.carouselFigureOutBean.getData().get(i).getSrc(),
                            CarouselFigureNet.carouselFigureOutBean.getData().get(i).getTitle(),
                            CarouselFigureNet.carouselFigureOutBean.getData().get(i).getHref()));
                mFirstTabFragment.setRecyclerBannerData(urls);
            }

            @Override
            public void error() {

            }
        }).pullDown();
    }

    private class Entity implements RecyclerBanner.BannerEntity {

        String imgUrl;
        String url;
        String text;

        public Entity(String imgUrl, String text, String url) {
            this.imgUrl = imgUrl;
            this.url = url;
            this.text = text;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public String getImgUrl() {
            return imgUrl;
        }


    }
}

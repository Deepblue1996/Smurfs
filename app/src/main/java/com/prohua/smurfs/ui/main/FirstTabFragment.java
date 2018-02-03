package com.prohua.smurfs.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prohua.smurfs.R;
import com.prohua.smurfs.event.StartBrotherEvent;
import com.prohua.smurfs.net.FirstNoticeNet;
import com.prohua.smurfs.presenter.FirstTabPresenter;
import com.prohua.smurfs.ui.other.ErrorNetDialogFragment;
import com.prohua.smurfs.ui.web.WebFragment;
import com.prohua.smurfs.ui.widgt.NoticePopupWindow;
import com.prohua.smurfs.ui.widgt.RecyclerBanner;
import com.prohua.smurfs.util.UtilTools;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static android.support.v4.app.DialogFragment.STYLE_NO_FRAME;

/**
 * FirstTabFragment 首页
 * Created by Deep on 2017/3/22 0022.
 */

public class FirstTabFragment extends SupportFragment implements IFirstTabFragment {

    @BindView(R.id.i_information)
    ImageView i_information;
    @BindView(R.id.i_searchbook)
    ImageView i_searchbook;
    @BindView(R.id.i_lostfound)
    ImageView i_lostfound;
    @BindView(R.id.i_bus)
    ImageView i_bus;

    @BindView(R.id.i_tongzhi)
    ImageView i_tongzhi;
    @BindView(R.id.i_achievement)
    ImageView i_achievement;
    @BindView(R.id.i_location)
    ImageView i_location;
    @BindView(R.id.i_electricity)
    ImageView i_electricity;
    @BindView(R.id.i_repair)
    ImageView i_repair;
    @BindView(R.id.i_rest)
    ImageView i_rest;
    @BindView(R.id.i_schedule)
    ImageView i_schedule;
    @BindView(R.id.i_calendar)
    ImageView i_calendar;
    @BindView(R.id.i_customer)
    ImageView i_customer;

    @BindView(R.id.i_message)
    ImageView i_message;
    @BindView(R.id.i_qrcode)
    ImageView i_qrcode;

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.topPanel)
    LinearLayout topPanelLinearLayout;
    @BindView(R.id.topPanelT)
    LinearLayout topPanelTLinearLayout;
    @BindView(R.id.icv_topView)
    RecyclerBanner recyclerBanner;
    @BindView(R.id.Notice)
    TextView noticeTextView;

    @BindView(R.id.to_notice)
    LinearLayout toNotice;

    private NoticePopupWindow noticePopupWindow;

    private FirstTabPresenter firstTabPresenter = new FirstTabPresenter(this);
    /**
     * 懒加载
     */
    public static FirstTabFragment newInstance() {

        Bundle args = new Bundle();

        FirstTabFragment fragment = new FirstTabFragment();
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
        View view = inflater.inflate(R.layout.first_main_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        initViewData();

        initView();

        return view;
    }

    private void initViewData() {

        ViewGroup.LayoutParams lp = topPanelTLinearLayout.getLayoutParams();
        // 关键地方，必须先调用measure测量，才能获取高度
        topPanelLinearLayout.measure(0, 0);
        lp.height = topPanelLinearLayout.getMeasuredHeight();
        topPanelTLinearLayout.setLayoutParams(lp);

        topPanelLinearLayout.setBackgroundColor(Color.argb(245, 102, 153, 255));

        noticeTextView.setText("好像没通知哦。");

        // 获取通知
        firstTabPresenter.changeNotice();

        // 获取轮播图
        firstTabPresenter.changeCarouselFigure();

        recyclerBanner.setOnPagerClickListener(entity ->
                EventBus.getDefault().post(new StartBrotherEvent(WebFragment.newInstance(entity.getText(), entity.getUrl()))));

        View noticeView = LayoutInflater.from(getActivity()).inflate(R.layout.notice_layout, null);
        TextView textView = (TextView) noticeView.findViewById(R.id.text);
        TextView textTimeView = (TextView) noticeView.findViewById(R.id.textTime);

        noticePopupWindow = new NoticePopupWindow(getActivity());
        noticePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        noticePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        noticePopupWindow.setContentView(noticeView);
        noticePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        noticePopupWindow.setOutsideTouchable(true);
        noticePopupWindow.setFocusable(true);

        toNotice.setOnClickListener(view1 -> {
            if(FirstNoticeNet.noticeOutBean != null) {
                textView.setText(FirstNoticeNet.noticeOutBean.getData().getContent());
                textTimeView.setText(FirstNoticeNet.noticeOutBean.getData().getCreate_date());
                noticePopupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
            }
        });

        i_qrcode.setOnClickListener(view12 -> {
            Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        });

        if(!UtilTools.isNetworkAvailable(getActivity())) {

            ErrorNetDialogFragment errorNetDialogFragment = new ErrorNetDialogFragment();
            errorNetDialogFragment.setStyle(STYLE_NO_FRAME,R.style.NobackDialogError);
            errorNetDialogFragment.show(getFragmentManager(),"ErrorNetDialogFragment");
            errorNetDialogFragment.setCancelable(false);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            EventBus.getDefault().post(new StartBrotherEvent(WebFragment.newInstance("扫描详情", bundle.getString("result"))));
        }
    }

    private void initStatusView() {

        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);

    }

    private void initView() {

        UtilTools.setGlideInto(getContext(), R.mipmap.information, i_information);
        UtilTools.setGlideInto(getContext(), R.mipmap.searchbook, i_searchbook);
        UtilTools.setGlideInto(getContext(), R.mipmap.lostfound, i_lostfound);
        UtilTools.setGlideInto(getContext(), R.mipmap.bus, i_bus);

        UtilTools.setGlideInto(getContext(), R.mipmap.tongzhi, i_tongzhi);

        UtilTools.setGlideInto(getContext(), R.mipmap.achievement, i_achievement);
        UtilTools.setGlideInto(getContext(), R.mipmap.location, i_location);
        UtilTools.setGlideInto(getContext(), R.mipmap.electricity, i_electricity);
        UtilTools.setGlideInto(getContext(), R.mipmap.repair, i_repair);
        UtilTools.setGlideInto(getContext(), R.mipmap.rest, i_rest);
        UtilTools.setGlideInto(getContext(), R.mipmap.schedule, i_schedule);
        UtilTools.setGlideInto(getContext(), R.mipmap.calendar, i_calendar);
        UtilTools.setGlideInto(getContext(), R.mipmap.customer, i_customer);

        UtilTools.setGlideInto(getContext(), R.mipmap.message, i_message);
        UtilTools.setGlideInto(getContext(), R.mipmap.qrcode, i_qrcode);
    }

    @Override
    public void setNoticeText(String msg) {
        _mActivity.runOnUiThread(() -> noticeTextView.setText(msg));
    }

    @Override
    public void setRecyclerBannerData(List<RecyclerBanner.BannerEntity> data) {
        _mActivity.runOnUiThread(() -> recyclerBanner.setDatas(data));
    }
}

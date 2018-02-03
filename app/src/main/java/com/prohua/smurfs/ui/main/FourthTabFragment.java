package com.prohua.smurfs.ui.main;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.prohua.smurfs.R;
import com.prohua.smurfs.application.SmurfsApplication;
import com.prohua.smurfs.database.StudentInfoDao;
import com.prohua.smurfs.event.StartBrotherResultEvent;
import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.LogOutNet;
import com.prohua.smurfs.net.LoginDataNet;
import com.prohua.smurfs.ui.MainFragment;
import com.prohua.smurfs.ui.other.LoginFragment;
import com.prohua.smurfs.ui.widgt.GlideCircleTransform;
import com.prohua.smurfs.util.UtilTools;
import com.tapadoo.alerter.Alerter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.smurfs.application.SmurfsApplication.cookiesManager;
import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * FourthTabFragment 我的
 * Created by Deep on 2017/3/22 0022.
 */

public class FourthTabFragment extends SupportFragment implements IFourthTabFragment {

    public static final int REQ_MODIFY_FRAGMENT = 100;

    public static final String KEY_RESULT_NICKNAME = "nickName";

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayoutHeight;

    @BindView(R.id.kenview)
    KenBurnsView kenBurnsView;
    @BindView(R.id.roundImageView)
    ImageView roundImageView;
    @BindView(R.id.f_user)
    ImageView f_user;
    @BindView(R.id.f_remind)
    ImageView f_remind;
    @BindView(R.id.f_edit)
    ImageView f_edit;
    @BindView(R.id.f_mess)
    ImageView f_mess;
    @BindView(R.id.f_about)
    ImageView f_about;
    @BindView(R.id.f_switch_s)
    ImageView f_switch_s;

    @BindView(R.id.login)
    LinearLayout loginLinearLayout;
    @BindView(R.id.switchx)
    LinearLayout switchxLinearLayout;

    @BindView(R.id.nickname)
    TextView nickName;
    @BindView(R.id.grade)
    TextView grade;

    /**
     * 懒加载
     */
    public static FourthTabFragment newInstance() {

        Bundle args = new Bundle();

        FourthTabFragment fragment = new FourthTabFragment();
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
        View view = inflater.inflate(R.layout.fourth_main_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        initView();

        return view;
    }

    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayoutHeight.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayoutHeight.setLayoutParams(lp);
    }

    private void initView() {

        Glide.with(getContext())
                .load(R.mipmap.my_bg)
                .skipMemoryCache(true)
                .into(kenBurnsView);

        UtilTools.setGlideInto(getContext(), R.mipmap.user, f_user);
        UtilTools.setGlideInto(getContext(), R.mipmap.remind, f_remind);
        UtilTools.setGlideInto(getContext(), R.mipmap.edit, f_edit);
        UtilTools.setGlideInto(getContext(), R.mipmap.mess, f_mess);
        UtilTools.setGlideInto(getContext(), R.mipmap.about, f_about);
        UtilTools.setGlideInto(getContext(), R.mipmap.switch_s, f_switch_s);

        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (studentInfo == null) {
                    EventBus.getDefault().post(new StartBrotherResultEvent(LoginFragment.newInstance(), REQ_MODIFY_FRAGMENT));
                }
            }
        });

        switchxLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (studentInfo != null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                    dialog.setMessage(_mActivity.getString(R.string.exit_the_current_account))
                            .setPositiveButton(_mActivity.getString(R.string.determine), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StudentInfoDao studentInfoDao = SmurfsApplication.getInstances().getDaoSession().getStudentInfoDao();
                                    studentInfoDao.deleteAll();
                                    //studentInfo = studentInfoDao.load(1L);
                                    studentInfo = null;

                                    nickName.setText(_mActivity.getString(R.string.click_login));
                                    grade.setText("木知道额");
                                    Glide.with(getContext())
                                            .load(R.mipmap.guest)
                                            .transform(new GlideCircleTransform(getContext()))
                                            .into(roundImageView);

                                    new LogOutNet(new CallBackInterface() {
                                        @Override
                                        public void complete() {
                                            Alerter.create(getActivity())
                                                    .setText(_mActivity.getString(R.string.exiting_current_account))
                                                    .showIcon(false)
                                                    .setBackgroundColor(R.color.showbg)
                                                    .show();
                                        }

                                        @Override
                                        public void error() {
                                            Alerter.create(getActivity())
                                                    .setText(_mActivity.getString(R.string.exiting_current_account_fail))
                                                    .showIcon(false)
                                                    .setBackgroundColor(R.color.showbg)
                                                    .show();
                                        }
                                    }).pullDown();

                                    if (LoginDataNet.studentInfoOutBean != null)
                                        LoginDataNet.studentInfoOutBean.getStudentInfoBean().setState(false);
                                    MainFragment.isSeRefresh = 1;
                                    MainFragment.isThRefresh = 1;
                                    // 清空cookie
                                    cookiesManager.getCookieStore().removeAll();
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton(_mActivity.getString(R.string.wrong_point), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        });

        if (studentInfo == null) {
            nickName.setText(_mActivity.getString(R.string.click_login));
            grade.setText("木知道额");
            Glide.with(getContext())
                    .load(R.mipmap.guest)
                    .transform(new GlideCircleTransform(getContext()))
                    .into(roundImageView);
        } else {
            nickName.setText(studentInfo.getNickName());
            grade.setText(getMoreInfo());
            Glide.with(getContext())
                    .load(SEVER_ADDRESS + studentInfo.getHeadImgUrl())
                    .transform(new GlideCircleTransform(getContext()))
                    .into(roundImageView);
        }
    }

    public void change() {
        nickName.setText(studentInfo.getNickName());
        grade.setText(getMoreInfo());
        Glide.with(getContext())
                .load(SEVER_ADDRESS + studentInfo.getHeadImgUrl())
                .transform(new GlideCircleTransform(getContext()))
                .into(roundImageView);

    }

    public String getMoreInfo() {

        Calendar now = Calendar.getInstance();

        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH);

        y = y - Integer.parseInt(studentInfo.getGrade());

        if (m >= 9) y++;
        if (y == 0) y = 1;

        switch (y) {
            case 1:
                return "大一小鲜肉";
            case 2:
                return "大二" + (Integer.parseInt(studentInfo.getSex()) == 2 ? "学姐" : "学长");
            case 3:
                return "大三" + (Integer.parseInt(studentInfo.getSex()) == 2 ? "学姐" : "学长");
            case 4:
                if (Integer.parseInt(studentInfo.getLengthOfSchooling()) == 4)
                    return "大四" + (Integer.parseInt(studentInfo.getSex()) == 2 ? "学姐" : "学长");
            default:
                return "毕业" + (y - Integer.parseInt(studentInfo.getLengthOfSchooling())) + "年";
        }
    }
}

package com.prohua.smurfs.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prohua.smurfs.R;
import com.prohua.smurfs.event.StartBrotherEvent;
import com.prohua.smurfs.event.StartBrotherResultEvent;
import com.prohua.smurfs.event.TabSelectedEvent;
import com.prohua.smurfs.net.LoginDataNet;
import com.prohua.smurfs.ui.main.FirstTabFragment;
import com.prohua.smurfs.ui.main.FourthTabFragment;
import com.prohua.smurfs.ui.main.SecondTabFragment;
import com.prohua.smurfs.ui.main.ThirdTabFragment;
import com.prohua.smurfs.ui.view.BottomBar;
import com.prohua.smurfs.ui.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.tapadoo.alerter.Alerter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * MainFragment 主Fragment 负责管理四大Tap
 * Created by Deep on 2016/12/11 0011.
 */

public class MainFragment extends SupportFragment {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    public static int tapPosition;

    /**
     * 懒加载
     */
    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
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

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        if (savedInstanceState == null) {
            mFragments[FIRST] = FirstTabFragment.newInstance();
            mFragments[SECOND] = SecondTabFragment.newInstance();
            mFragments[THIRD] = ThirdTabFragment.newInstance();
            mFragments[FOURTH] = FourthTabFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(FirstTabFragment.class);
            mFragments[SECOND] = findChildFragment(SecondTabFragment.class);
            mFragments[THIRD] = findChildFragment(ThirdTabFragment.class);
            mFragments[FOURTH] = findChildFragment(FourthTabFragment.class);
        }

        initBottomBarView();

        return view;
    }

    /**
     * 判断是否已刷新
     */
    public static int isSeRefresh = 0;
    public static int isThRefresh = 0;

    /**
     * 初始化底部栏
     */
    private void initBottomBarView() {

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.home, this.getString(R.string.home)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.circle, this.getString(R.string.alumni_circle)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.mutual, this.getString(R.string.mutual_platform)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.me, this.getString(R.string.me)));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if(position == SECOND && studentInfo == null && isSeRefresh == 1) {
                    SecondTabFragment secondTabFragment = findChildFragment(SecondTabFragment.class);
                    secondTabFragment.setRefresh();
                    isSeRefresh = 0;
                }
                if(position == THIRD && studentInfo == null && isThRefresh == 1) {
                    ThirdTabFragment thirdTabFragment = findChildFragment(ThirdTabFragment.class);
                    thirdTabFragment.setRefresh();
                    isThRefresh = 0;
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
                tapPosition = position;
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /**
     * 处理回退事件
     *
     * @return true
     */
    @Override
    public boolean onBackPressedSupport() {

        // LogoFragment
        if (_mActivity.getSupportFragmentManager().getBackStackEntryCount() > 2) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                _mActivity.finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                // TODO: UI提示
                Alerter.create(getActivity())
                        .setText(_mActivity.getString(R.string.exit_msg))
                        .showIcon(false)
                        .setBackgroundColor(R.color.showbg)
                        .show();
            }
        }

        return true;
    }


    /**
     * 主视图接受管理子视图数据更新
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == FourthTabFragment.REQ_MODIFY_FRAGMENT && resultCode == RESULT_OK && data != null) {
            // 改变
            FourthTabFragment fourthTabFragment = findChildFragment(FourthTabFragment.class);
            fourthTabFragment.change();
            SecondTabFragment secondTabFragment = findChildFragment(SecondTabFragment.class);
            secondTabFragment.setRefresh();
            ThirdTabFragment thirdTabFragment = findChildFragment(ThirdTabFragment.class);
            thirdTabFragment.setRefresh();
        }
        if(requestCode == SecondTabFragment.REQ_RELEASE_FRAGMENT && resultCode == RESULT_OK && data != null) {
            SecondTabFragment secondTabFragment = findChildFragment(SecondTabFragment.class);
            secondTabFragment.setRefresh();
        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrotherResult(StartBrotherResultEvent event) {
        startForResult(event.targetFragment, event.requestCode);
    }

}

package com.prohua.smurfs.ui.main;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.prohua.smurfs.R;
import com.prohua.smurfs.adapter.CircleNineAdapter;
import com.prohua.smurfs.bean.CircleNoteBean;
import com.prohua.smurfs.bean.CommentsBean;
import com.prohua.smurfs.bean.LikesBean;
import com.prohua.smurfs.event.StartBrotherEvent;
import com.prohua.smurfs.event.StartBrotherResultEvent;
import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.CheckMsgNet;
import com.prohua.smurfs.net.CircleCommentNet;
import com.prohua.smurfs.net.CircleDelNet;
import com.prohua.smurfs.net.CircleLikesNet;
import com.prohua.smurfs.net.CircleNetOperation;
import com.prohua.smurfs.ui.MainFragment;
import com.prohua.smurfs.ui.other.CircleEditDialogFragment;
import com.prohua.smurfs.ui.other.CircleMsgFragment;
import com.prohua.smurfs.ui.other.ReleaseMsgFragment;
import com.prohua.smurfs.ui.widgt.CircleItemTouchPopupWindow;
import com.prohua.smurfs.ui.widgt.CircleMenuPopupWindow;
import com.prohua.smurfs.ui.widgt.CommentModel;
import com.prohua.smurfs.ui.widgt.CommentTextView;
import com.prohua.smurfs.ui.widgt.FastScrollLinearLayoutManager;
import com.prohua.smurfs.ui.widgt.GlideCircleTransform;
import com.prohua.smurfs.ui.widgt.NoticePopupWindow;
import com.prohua.smurfs.ui.widgt.SoftKeyBoardListener;
import com.prohua.smurfs.ui.widgt.SuperRefreshLayout;
import com.prohua.smurfs.ui.widgt.TextBlankClickListener;
import com.prohua.smurfs.ui.widgt.TextTopicClickListener;
import com.prohua.smurfs.ui.widgt.TopicTextView;
import com.prohua.smurfs.util.UtilTools;
import com.tapadoo.alerter.Alerter;
import com.timqi.collapsibletextview.CollapsibleTextView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static android.support.v4.app.DialogFragment.STYLE_NO_FRAME;
import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;
import static com.prohua.smurfs.base.GlobalConfig.FILE_SERVER_URL;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * SecondTabFragment 校友圈-MVC
 * Created by Deep on 2017/3/22 0022.
 */

public class SecondTabFragment extends SupportFragment {

    public static final int REQ_RELEASE_FRAGMENT = 200;

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.refresh)
    SuperRefreshLayout refreshLayout;
    @BindView(R.id.re)
    RecyclerView recyclerView;
    @BindView(R.id.s_photograph)
    ImageView s_photograph;
    @BindView(R.id.toptv)
    TextView toptv;

    @BindView(R.id.c_check_lin)
    RelativeLayout checkLinearLayout;
    @BindView(R.id.s_check_last_img)
    ImageView checkLastImageView;
    @BindView(R.id.t_check_count)
    TextView checkCountTextView;

    private FastScrollLinearLayoutManager fastScrollLinearLayoutManager;
    private CircleMenuPopupWindow circleMenuPopupWindow;
    private MyAdapter adapter;

    private CircleEditDialogFragment circleEditDialogFragment;

    /**
     * 懒加载
     */
    public static SecondTabFragment newInstance() {

        Bundle args = new Bundle();

        SecondTabFragment fragment = new SecondTabFragment();
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
        View view = inflater.inflate(R.layout.second_main_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        UtilTools.setGlideInto(getContext(), R.mipmap.photograph, s_photograph);

        NineGridView.setImageLoader(new GlideImageLoader());

        initListView();

        initKeyboardHeightObserver();

        initView();

        return view;
    }

    private void initView() {
        View noticeView = LayoutInflater.from(getActivity()).inflate(R.layout.circle_menu_layout, null);
        //noticeView.measure(0, 0);
        circleMenuPopupWindow = new CircleMenuPopupWindow(getActivity());
        circleMenuPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        circleMenuPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        circleMenuPopupWindow.setContentView(noticeView);
        //circleMenuPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        circleMenuPopupWindow.setOutsideTouchable(true);
        circleMenuPopupWindow.setFocusable(true);
        circleMenuPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        // 菜单
        TextView textTextView = (TextView) noticeView.findViewById(R.id.menu_text);
        TextView cameraTextView = (TextView) noticeView.findViewById(R.id.menu_camera);
        TextView photosTextView = (TextView) noticeView.findViewById(R.id.menu_photos);
        TextView cancelTextView = (TextView) noticeView.findViewById(R.id.menu_cancel);


        textTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenuPopupWindow.dismiss();
                if (studentInfo != null) {
                    MainFragment.tapPosition = 5;
                    ReleaseMsgFragment.startType = 0;
                    EventBus.getDefault().post(new StartBrotherResultEvent(ReleaseMsgFragment.newInstance(),REQ_RELEASE_FRAGMENT));
                } else {
                    Alerter.create(getActivity())
                            .setText(_mActivity.getString(R.string.logged_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                }
            }
        });

        cameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenuPopupWindow.dismiss();
                if (studentInfo != null) {
                    MainFragment.tapPosition = 5;
                    ReleaseMsgFragment.startType = 1;
                    EventBus.getDefault().post(new StartBrotherResultEvent(ReleaseMsgFragment.newInstance(),REQ_RELEASE_FRAGMENT));
                } else {
                    Alerter.create(getActivity())
                            .setText(_mActivity.getString(R.string.logged_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                }
            }
        });

        photosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenuPopupWindow.dismiss();
                if (studentInfo != null) {
                    MainFragment.tapPosition = 5;
                    ReleaseMsgFragment.startType = 2;
                    EventBus.getDefault().post(new StartBrotherResultEvent(ReleaseMsgFragment.newInstance(),REQ_RELEASE_FRAGMENT));
                } else {
                    Alerter.create(getActivity())
                            .setText(_mActivity.getString(R.string.logged_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                }
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenuPopupWindow.dismiss();
            }
        });

        // 右上角按钮
        s_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenuPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });

        // 评论
        checkLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StartBrotherEvent(CircleMsgFragment.newInstance()));
                //checkLinearLayout.setVisibility(View.GONE);
                checkCountTextView.setText("");
                Glide.with(getContext())
                        .load(R.mipmap.about_message)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(checkLastImageView);
                checkLastImageView.setAlpha(255);
            }
        });
    }

    public static int sodo = 0;

    // 实时监控键盘, 自动定位
    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        SoftKeyBoardListener.setListener(_mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (MainFragment.tapPosition == 1)
                    alignCommentBoxToView(height);
            }

            @Override
            public void keyBoardHide(int height) {
                if (MainFragment.tapPosition == 1 && sodo == 0) {
                    circleEditDialogFragment.dismiss();
                } else {
                    sodo = 0;
                }
            }
        });
    }

    // 点击的view
    private int itemPos;
    // 点击的评论
    private int itemComPos;
    // 弹出框高度
    private int popHeight;

    /**
     * 定位评论框到点击的view
     */
    private void alignCommentBoxToView(int height) {

        // 第一个可见位置
        int firstVisibleItem = fastScrollLinearLayoutManager.findFirstVisibleItemPosition();

        // 缓存中的位置
        int ys = itemPos - firstVisibleItem;

        // 缓存中的视图
        View v = fastScrollLinearLayoutManager.getChildAt(ys);

        // 点击的视图高度
        if (v != null) {
            int h = v.getHeight();

            // 点击的视图左上角坐标
            float y = v.getY();

            // recyclerView在屏幕的坐标
            int[] location = new int[2];
            recyclerView.getLocationOnScreen(location);

            // 屏幕高度
            WindowManager wm = _mActivity.getWindowManager();
            int sh = wm.getDefaultDisplay().getHeight();

            // 草图a
            int a = (int) y + location[1] + h;
            // 最终得出键盘弹出时,recyclerView应该滚动的距离
            // 如果底部距离小于键盘高度则
            if (sh - a < height) {
                // 要滚动的距离
                int scroll = height - (sh - a);
                recyclerView.smoothScrollBy(0, scroll + popHeight);
            } else {
                int scroll = sh - a - height;
                recyclerView.smoothScrollBy(0, -scroll + popHeight);
            }
        } else {
            //circleEditPopupWindow.dismiss();
            circleEditDialogFragment.dismiss();
            closeKeyboard();
        }
    }

    private void closeKeyboard() {
        View view = _mActivity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);
    }

    private void initListView() {

        fastScrollLinearLayoutManager = new FastScrollLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(fastScrollLinearLayoutManager);

        // 解决动画闪烁问题
        recyclerView.getItemAnimator().setChangeDuration(0);

        adapter = new MyAdapter(getActivity());
        refreshLayout.setAdapter(recyclerView, adapter);
        refreshLayout.setOnRefreshHandler(new SuperRefreshLayout.OnRefreshHandler() {
            @Override
            public void refresh() {
                CircleNetOperation.beforeId = 0;

                if (studentInfo != null) {
                    // 检测消息数量
                    new CheckMsgNet(new CallBackInterface() {
                        @Override
                        public void complete() {

                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (CheckMsgNet.checkMsgDataOutBean.getData() != null) {
                                        checkLinearLayout.setVisibility(View.VISIBLE);
                                        if (Integer.parseInt(CheckMsgNet.checkMsgDataOutBean.getData().get(0).getMsgCount()) != 0) {
                                            Glide.with(getContext())
                                                    .load(SEVER_ADDRESS +
                                                            CheckMsgNet.checkMsgDataOutBean.getData().get(0).getHeadImgUrl())
                                                    .transform(new GlideCircleTransform(getContext()))
                                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                    .into(checkLastImageView);
                                            checkCountTextView.setText("" +
                                                    CheckMsgNet.checkMsgDataOutBean.getData().get(0).getMsgCount() + "");
                                            checkLastImageView.setAlpha(150);
//                                        StudentInfoDao studentInfoDao = SmurfsApplication.getInstances().getDaoSession().getStudentInfoDao();
//                                        studentInfoDao.deleteAll();
//                                        //studentInfo.setLastId(CheckMsgNet.checkMsgDataOutBean.getData().get(0).getId());
//                                        studentInfoDao.insert(studentInfo);
                                            Alerter.create(getActivity())
                                                    .setText(getString(R.string.you_have) + " " +
                                                            CheckMsgNet.checkMsgDataOutBean.getData().get(0).getMsgCount() + " " +
                                                            getString(R.string.click_left_corner))
                                                    .showIcon(false)
                                                    .setBackgroundColor(R.color.showbg)
                                                    .show();
                                        } else {
                                            Glide.with(getContext())
                                                    .load(R.mipmap.about_message)
                                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                    .into(checkLastImageView);
                                            checkCountTextView.setText("");
                                            checkLastImageView.setAlpha(255);
                                        }
                                    } else {
                                        Alerter.create(getActivity())
                                                .setText(CheckMsgNet.checkMsgDataOutBean.getMsg())
                                                .showIcon(false)
                                                .setBackgroundColor(R.color.showbg)
                                                .show();
                                        checkLinearLayout.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }

                        @Override
                        public void error() {

                        }
                    }).pullDown(studentInfo.getLastId());
                } else {
                    Alerter.create(getActivity())
                            .setText(getString(R.string.logged_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                    checkLinearLayout.setVisibility(View.GONE);
                }

                // 获取校友圈列表数据
                new CircleNetOperation(new CallBackInterface() {
                    @Override
                    public void complete() {
                        _mActivity.runOnUiThread(() -> {
                            Collections.reverse(CircleNetOperation.circleNoteOutBean.getData());
                            adapter.setDatas(CircleNetOperation.circleNoteOutBean.getData());
                            refreshLayout.setRefreshing(false);
                        });
                    }

                    @Override
                    public void error() {

                    }
                }).pullDownRefresh("0");
            }

            @Override
            public void loadMore() {
                super.loadMore();

                new Handler().postDelayed(() -> new CircleNetOperation(new CallBackInterface() {
                    @Override
                    public void complete() {
                        _mActivity.runOnUiThread(() -> {
                            adapter.addDatas(CircleNetOperation.circleNoteOutBean.getData());
                            refreshLayout.loadComplete(true);
                        });
                    }

                    @Override
                    public void error() {

                    }
                }).pullDownRefresh("0"), 400);
            }
        });

        toptv.setOnTouchListener((view, motionEvent) -> {
            recyclerView.smoothScrollToPosition(0);

            return false;
        });

        refreshLayout.autoRefresh();
    }

    public void setRefresh() {
        recyclerView.smoothScrollToPosition(0);
        refreshLayout.autoRefresh();
    }

    private class MyAdapter extends SuperRefreshLayout.Adapter {

        CircleItemTouchPopupWindow circleItemTouchPopupWindow;
        NoticePopupWindow itemLongTouchPopupWinwow;

        List<CircleNoteBean> datas = new ArrayList<>();

        TextView copyTextView;
        TextView delTextView;

        String replyName = "0";
        String replyId = "0";

        private void initCircleEdit() {
            // 初始化 输入框
            circleEditDialogFragment = new CircleEditDialogFragment();
            circleEditDialogFragment.setStyle(STYLE_NO_FRAME, R.style.NobackDialog);
            popHeight = 140;
            circleEditDialogFragment.setCircleEditActionListener((v, actionId, event) -> {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!"".equals(v.getText().toString())) {
                        circleEditDialogFragment.dismiss();
                        new CircleCommentNet(new CallBackInterface() {
                            @Override
                            public void complete() {
                                _mActivity.runOnUiThread(() -> {
                                    if ("0".equals(CircleCommentNet.circleCommentOutBean.getCode())) {
                                        Alerter.create(getActivity())
                                                .setText(CircleCommentNet.circleCommentOutBean.getMsg())
                                                .showIcon(false)
                                                .setBackgroundColor(R.color.showbg)
                                                .show();
                                        v.setText("");
                                        closeKeyboard();

                                        replyId = "0";
                                        replyName = "0";
                                    } else {
                                        CommentsBean commentsBean = new CommentsBean();
                                        commentsBean.setAcid(datas.get(itemPos - 1).getId());
                                        commentsBean.setNickname(studentInfo.getNickName());
                                        commentsBean.setText(v.getText().toString());
                                        commentsBean.setId(CircleCommentNet.circleCommentOutBean.getData());
                                        commentsBean.setUid(replyId);
                                        commentsBean.setReply(replyName);

                                        if (datas.get(itemPos - 1).getComments() == null) {
                                            List<CommentsBean> commentsBeanList = new ArrayList<>();
                                            commentsBeanList.add(commentsBean);
                                            datas.get(itemPos - 1).setComments(commentsBeanList);
                                        } else {
                                            datas.get(itemPos - 1).getComments().add(commentsBean);
                                        }
                                        v.setText("");
                                        closeKeyboard();
                                        adapter.notifyItemChanged(itemPos);

                                        replyId = "0";
                                        replyName = "0";
                                    }
                                });
                            }

                            @Override
                            public void error() {

                            }
                        }).pullDown(datas.get(itemPos - 1).getId(), v.getText().toString(), "0", replyId);
                    } else {
                        Alerter.create(getActivity())
                                .setText(_mActivity.getString(R.string.written_yet))
                                .showIcon(false)
                                .setBackgroundColor(R.color.showbg)
                                .show();
                    }
                    return true;
                }
                return false;
            });

            circleEditDialogFragment.setCircleTextOnClickListener((v, editText) -> {
                if (!"".equals(editText.getText().toString())) {
                    circleEditDialogFragment.dismiss();
                    new CircleCommentNet(new CallBackInterface() {
                        @Override
                        public void complete() {
                            _mActivity.runOnUiThread(() -> {
                                if ("0".equals(CircleCommentNet.circleCommentOutBean.getCode())) {
                                    Alerter.create(getActivity())
                                            .setText(CircleCommentNet.circleCommentOutBean.getMsg())
                                            .showIcon(false)
                                            .setBackgroundColor(R.color.showbg)
                                            .show();
                                    editText.setText("");
                                    closeKeyboard();

                                    replyId = "0";
                                    replyName = "0";
                                } else {
                                    CommentsBean commentsBean = new CommentsBean();
                                    commentsBean.setAcid(datas.get(itemPos - 1).getId());
                                    commentsBean.setNickname(studentInfo.getNickName());
                                    commentsBean.setText(editText.getText().toString());
                                    commentsBean.setId(CircleCommentNet.circleCommentOutBean.getData());
                                    commentsBean.setUid(replyId);
                                    commentsBean.setReply(replyName);

                                    if (datas.get(itemPos - 1).getComments() == null) {
                                        List<CommentsBean> commentsBeanList = new ArrayList<>();
                                        commentsBeanList.add(commentsBean);
                                        datas.get(itemPos - 1).setComments(commentsBeanList);
                                    } else {
                                        datas.get(itemPos - 1).getComments().add(commentsBean);
                                    }
                                    editText.setText("");
                                    closeKeyboard();
                                    adapter.notifyItemChanged(itemPos);

                                    replyId = "0";
                                    replyName = "0";
                                }
                            });
                        }

                        @Override
                        public void error() {

                        }
                    }).pullDown(datas.get(itemPos - 1).getId(), editText.getText().toString(), "0", replyId);
                } else {
                    Alerter.create(getActivity())
                            .setText(_mActivity.getString(R.string.written_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                }
            });
        }

        public MyAdapter(Context context) {
            super(context);

            // 初始化 扩展按钮
            View noticeView = LayoutInflater.from(getActivity()).inflate(R.layout.circle_item_touch_layout, null);
            //noticeView.measure(0, 0);
            circleItemTouchPopupWindow = new CircleItemTouchPopupWindow(getActivity());
            circleItemTouchPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            circleItemTouchPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            circleItemTouchPopupWindow.setContentView(noticeView);
            //circleItemTouchPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            circleItemTouchPopupWindow.setOutsideTouchable(true);
            circleItemTouchPopupWindow.setFocusable(true);

            // 初始化 长按按钮
            View noticeView3 = LayoutInflater.from(getActivity()).inflate(R.layout.circle_item_long_layout, null);
            //noticeView.measure(0, 0);
            itemLongTouchPopupWinwow = new

                    NoticePopupWindow(getActivity());
            itemLongTouchPopupWinwow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLongTouchPopupWinwow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLongTouchPopupWinwow.setContentView(noticeView3);
            //itemLongTouchPopupWinwow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            itemLongTouchPopupWinwow.setOutsideTouchable(true);
            itemLongTouchPopupWinwow.setFocusable(true);

            copyTextView = (TextView) noticeView3.findViewById(R.id.c_copy);
            // 复制
            copyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(datas.get(itemPos - 1).getComments().get(itemComPos).getText());
                    Alerter.create(getActivity())
                            .setText(_mActivity.getString(R.string.copy_to_clipboard))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                    itemLongTouchPopupWinwow.dismiss();
                }
            });
            delTextView = (TextView) noticeView3.findViewById(R.id.c_del);
            // 删除
            delTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CircleCommentNet(new CallBackInterface() {
                        @Override
                        public void complete() {
                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("".equals(CircleCommentNet.circleCommentOutBean.getMsg())) {
                                        Alerter.create(getActivity())
                                                .setText(_mActivity.getString(R.string.delete_success))
                                                .showIcon(false)
                                                .setBackgroundColor(R.color.showbg)
                                                .show();
                                        datas.get(itemPos - 1).getComments().remove(itemComPos);
                                        if (datas.get(itemPos - 1).getComments().size() == 0)
                                            datas.get(itemPos - 1).setComments(null);
                                        itemLongTouchPopupWinwow.dismiss();
                                        adapter.notifyItemChanged(itemPos);
                                    } else {
                                        Alerter.create(getActivity())
                                                .setText(CircleCommentNet.circleCommentOutBean.getMsg())
                                                .showIcon(false)
                                                .setBackgroundColor(R.color.showbg)
                                                .show();
                                        itemLongTouchPopupWinwow.dismiss();
                                    }
                                }
                            });
                        }

                        @Override
                        public void error() {
                            Alerter.create(getActivity())
                                    .setText(CircleCommentNet.circleCommentOutBean.getMsg())
                                    .showIcon(false)
                                    .setBackgroundColor(R.color.showbg)
                                    .show();
                            itemLongTouchPopupWinwow.dismiss();
                        }
                    }).pullDown("0", "", datas.get(itemPos - 1).getComments().get(itemComPos).getId(), "0");
                }
            });
        }

        public void setDatas(List<CircleNoteBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public void addDatas(List<CircleNoteBean> datas) {
            if (datas != null)
                this.datas.addAll(datas);
            else
                setState(2);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.second_item, parent, false)) {
            };
        }

        int jJu = 0;
        String flagJu = "0";

        @Override
        public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {

            // Everyone find id
            CollapsibleTextView collapsibleTextView = (CollapsibleTextView) holder.itemView.findViewById(R.id.id_info);
            TextView times = (TextView) holder.itemView.findViewById(R.id.times);
            TextView name = (TextView) holder.itemView.findViewById(R.id.name);
            ImageView imageViewt = (ImageView) holder.itemView.findViewById(R.id.imaget);
            NineGridView nineGridView = (NineGridView) holder.itemView.findViewById(R.id.nineGrid);

            TopicTextView tv_topic = (TopicTextView) holder.itemView.findViewById(R.id.tv_topic);
            LinearLayout likesLinearLayout = (LinearLayout) holder.itemView.findViewById(R.id.likes);
            LinearLayout commentsLinearLayout = (LinearLayout) holder.itemView.findViewById(R.id.comments);

            ImageView touchIs = (ImageView) holder.itemView.findViewById(R.id.touchIs);

            TextView addressTextView = (TextView) holder.itemView.findViewById(R.id.address);
            LinearLayout addressLinearLayout = (LinearLayout) holder.itemView.findViewById(R.id.m_location);

            TextView delCTextView = (TextView) holder.itemView.findViewById(R.id.c_del);

            // 删除
            if (studentInfo != null) {
                if (datas.get(position - 1).getUid().equals(studentInfo.getUId())) {
                    delCTextView.setText(_mActivity.getString(R.string.delete));

                    delCTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemPos = position;
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                            dialog.setMessage(_mActivity.getString(R.string.want_to_delete))
                                    .setPositiveButton(_mActivity.getString(R.string.delete), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new CircleDelNet(new CallBackInterface() {
                                                @Override
                                                public void complete() {
                                                    _mActivity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if ("".equals(CircleDelNet.msg)) {
                                                                Alerter.create(getActivity())
                                                                        .setText(_mActivity.getString(R.string.delete_success))
                                                                        .showIcon(false)
                                                                        .setBackgroundColor(R.color.showbg)
                                                                        .show();
                                                                datas.remove(itemPos - 1);
                                                                adapter.notifyItemRemoved(itemPos);
                                                            } else
                                                                Alerter.create(getActivity())
                                                                        .setText(CircleDelNet.msg)
                                                                        .showIcon(false)
                                                                        .setBackgroundColor(R.color.showbg)
                                                                        .show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void error() {

                                                }
                                            }).pullDown(datas.get(itemPos - 1).getId());
                                        }
                                    })
                                    .setNegativeButton(_mActivity.getString(R.string.wrong_point), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create().show();
                        }
                    });

                    delCTextView.setVisibility(View.VISIBLE);
                } else {
                    delCTextView.setVisibility(View.GONE);
                }
            }

            // 定位
            if (!"".equals(datas.get(position - 1).getAddress())) {
                addressTextView.setText(datas.get(position - 1).getAddress());
                addressLinearLayout.setVisibility(View.VISIBLE);
            } else {
                addressLinearLayout.setVisibility(View.GONE);
            }
            // 扩展按钮
            touchIs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] location = new int[2];

                    itemPos = position;

                    TextView smallLike = (TextView) circleItemTouchPopupWindow.getContentView().findViewById(R.id.c_like);
                    TextView smallComment = (TextView) circleItemTouchPopupWindow.getContentView().findViewById(R.id.c_com);

                    if (studentInfo != null) {
                        // 判断是否已赞
                        if (datas.get(itemPos - 1).getLikes() != null) {
                            if (datas.get(itemPos - 1).getLikes().size() != 0) {
                                for (int i = 0; i < datas.get(itemPos - 1).getLikes().size(); i++) {
                                    if (studentInfo.getNickName().equals(datas.get(itemPos - 1).getLikes().get(i).getNickname())) {
                                        jJu = i;
                                        flagJu = "0";
                                        break;
                                    } else {
                                        flagJu = "1";
                                    }
                                }
                            } else {
                                flagJu = "1";
                            }
                        } else {
                            flagJu = "1";
                        }
                    } else {
                        flagJu = "1";
                    }

                    if ("0".equals(flagJu))
                        smallLike.setText(_mActivity.getString(R.string.cancel));
                    else
                        smallLike.setText(_mActivity.getString(R.string.like));

                    // 赞按钮
                    smallLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (studentInfo != null) {

                                // 判断是否已赞
                                if (datas.get(itemPos - 1).getLikes() != null) {
                                    if (datas.get(itemPos - 1).getLikes().size() != 0) {
                                        for (int i = 0; i < datas.get(itemPos - 1).getLikes().size(); i++) {
                                            if (studentInfo.getNickName().equals(datas.get(itemPos - 1).getLikes().get(i).getNickname())) {
                                                jJu = i;
                                                flagJu = "0";
                                                break;
                                            } else {
                                                flagJu = "1";
                                            }
                                        }
                                    } else {
                                        flagJu = "1";
                                    }
                                } else {
                                    flagJu = "1";
                                }

                                new CircleLikesNet(new CallBackInterface() {
                                    @Override
                                    public void complete() {

                                        _mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if ("null".equals(CircleLikesNet.msg)) {
                                                    LikesBean likesBean = new LikesBean();
                                                    if ("1".equals(flagJu)) {
                                                        // 如果没有赞列表
                                                        if (datas.get(itemPos - 1).getLikes() == null) {
                                                            List<LikesBean> likesBeanList = new ArrayList<>();
                                                            likesBean.setNickname(studentInfo.getNickName());
                                                            likesBean.setUid(datas.get(itemPos - 1).getId());
                                                            likesBean.setAcid(studentInfo.getUId());
                                                            likesBeanList.add(likesBean);
                                                            datas.get(itemPos - 1).setLikes(likesBeanList);
                                                        } else {
                                                            likesBean.setNickname(studentInfo.getNickName());
                                                            likesBean.setUid(datas.get(itemPos - 1).getId());
                                                            likesBean.setAcid(studentInfo.getUId());
                                                            datas.get(itemPos - 1).getLikes().add(likesBean);
                                                        }
                                                        smallLike.setText(_mActivity.getString(R.string.cancel));
                                                        circleItemTouchPopupWindow.dismiss();
                                                    } else {
                                                        if (datas.get(itemPos - 1).getLikes().size() > 0) {
                                                            datas.get(itemPos - 1).getLikes().remove(jJu);
                                                            if (datas.get(itemPos - 1).getLikes().size() == 0) {
                                                                likesLinearLayout.setVisibility(View.GONE);
                                                            }
                                                        }
                                                        smallLike.setText(_mActivity.getString(R.string.like));
                                                        circleItemTouchPopupWindow.dismiss();
                                                    }
                                                    adapter.notifyItemChanged(itemPos);
                                                } else {
                                                    Alerter.create(getActivity())
                                                            .setText(CircleLikesNet.msg)
                                                            .showIcon(false)
                                                            .setBackgroundColor(R.color.showbg)
                                                            .show();
                                                    circleItemTouchPopupWindow.dismiss();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void error() {

                                    }
                                }).pullDown(datas.get(itemPos - 1).getId(), flagJu);
                            } else {
                                Alerter.create(getActivity())
                                        .setText(_mActivity.getString(R.string.logged_yet))
                                        .showIcon(false)
                                        .setBackgroundColor(R.color.showbg)
                                        .show();
                                circleItemTouchPopupWindow.dismiss();
                            }
                        }
                    });
                    // 评论
                    smallComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (studentInfo != null) {
                                replyId = "0";
                                replyName = "0";
                                // 隐藏
                                circleItemTouchPopupWindow.dismiss();

                                initCircleEdit();

                                CircleEditDialogFragment.hint= getString(R.string.comment);

                                circleEditDialogFragment.show(getFragmentManager(), "ErrorNetDialogFragment");

                            } else {
                                Alerter.create(getActivity())
                                        .setText(_mActivity.getString(R.string.logged_yet))
                                        .showIcon(false)
                                        .setBackgroundColor(R.color.showbg)
                                        .show();
                                circleItemTouchPopupWindow.dismiss();
                            }
                        }
                    });
                    view.getLocationOnScreen(location);
                    circleItemTouchPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - 470, location[1] - 30);
                }
            });


            // 设置赞
            if (datas.get(position - 1).getLikes() != null) {
                if (datas.get(position - 1).getLikes().size() != 0) {
                    List<String> likeUsers = new ArrayList<>();

                    likesLinearLayout.setVisibility(View.VISIBLE);

                    for (int i = 0; i < datas.get(position - 1).getLikes().size(); i++) {
                        likeUsers.add(datas.get(position - 1).getLikes().get(i).getNickname());
                    }

                    tv_topic.setTextTopicClickListener(new TextTopicClickListener() {
                        @Override
                        public void onTopicClick(View view, String topic) {
                            Alerter.create(getActivity())
                                    .setText(topic)
                                    .showIcon(false)
                                    .setBackgroundColor(R.color.showbg)
                                    .show();
                        }
                    });
                    //先设置监听再设置文字
                    tv_topic.setTopics(likeUsers);
                } else {
                    likesLinearLayout.setVisibility(View.GONE);
                }
            } else {
                likesLinearLayout.setVisibility(View.GONE);
            }

            // 设置评论
            if (datas.get(position - 1).getComments() != null) {
                commentsLinearLayout.removeAllViews();

                LinearLayout linearLayout = new LinearLayout(getContext());

                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                lps.setMargins(0, 10, 0, 20);
                lps.height = 1;
                linearLayout.setLayoutParams(lps);

                linearLayout.setBackgroundColor(Color.rgb(220, 220, 220));

                commentsLinearLayout.addView(linearLayout);

                for (int i = 0; i < datas.get(position - 1).getComments().size(); i++) {

                    CommentTextView commentTextView = new CommentTextView(getContext());
                    //commentTextView.setLineSpacing(1.2f, 1.2f);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 0, 10, 0);
                    commentTextView.setLayoutParams(params);
                    commentTextView.setPadding(0, 10, 0, 10);

                    CommentModel model = new CommentModel();
                    model.setReviewUName(datas.get(position - 1).getComments().get(i).getNickname());
                    model.setReviewUid(Long.parseLong(datas.get(position - 1).getComments().get(i).getUid()));
                    model.setCommentId(datas.get(position - 1).getComments().get(i).getId());

                    if (!"0".equals(datas.get(position - 1).getComments().get(i).getReply())) {
                        model.setReplyUid(Long.parseLong(datas.get(position - 1).getComments().get(i).getAcid()));
                        model.setReplyUName(datas.get(position - 1).getComments().get(i).getReply());
                    }

                    model.setReviewContent(datas.get(position - 1).getComments().get(i).getText());
                    commentTextView.setReply(model);

                    commentTextView.setListener(new TextBlankClickListener() {
                        @Override
                        public void onBlankClick(View view) {

                            itemPos = position;

                            if (studentInfo != null) {
                                if (!studentInfo.getNickName().equals(commentTextView.getCommentModels().getReviewUName())) {

                                    replyId = String.valueOf(commentTextView.getCommentModels().getReviewUid());
                                    replyName = commentTextView.getCommentModels().getReviewUName();

                                    initCircleEdit();

                                    CircleEditDialogFragment.hint = _mActivity.getString(R.string.reply) + commentTextView.getCommentModels().getReviewUName();

                                    circleEditDialogFragment.show(getFragmentManager(), "ErrorNetDialogFragment");
                                } else {
                                    Alerter.create(getActivity())
                                            .setText(_mActivity.getString(R.string.reply_own))
                                            .showIcon(false)
                                            .setBackgroundColor(R.color.showbg)
                                            .show();
                                }
                            } else {
                                Alerter.create(getActivity())
                                        .setText(_mActivity.getString(R.string.logged_yet))
                                        .showIcon(false)
                                        .setBackgroundColor(R.color.showbg)
                                        .show();
                            }
                        }

                        @Override
                        public void onLongClick(View view) {

                            itemPos = position;
                            for (int i = 0; i < datas.get(position - 1).getComments().size(); i++) {
                                if (datas.get(position - 1).getComments().get(i).getId().equals(commentTextView.getCommentModels().getCommentId())) {
                                    itemComPos = i;
                                    break;
                                }
                            }
                            if (studentInfo != null) {
                                if (!datas.get(position - 1).getComments().get(itemComPos).getNickname().equals(studentInfo.getNickName())) {
                                    delTextView.setVisibility(View.GONE);
                                } else {
                                    delTextView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                delTextView.setVisibility(View.GONE);
                            }
                            itemLongTouchPopupWinwow.showAsDropDown(view);
                        }
                    });

                    commentsLinearLayout.addView(commentTextView);

                }
                commentsLinearLayout.setVisibility(View.VISIBLE);
            } else {
                commentsLinearLayout.setVisibility(View.GONE);
            }

            // 设置九宫格
            if (!"".equals(datas.get(position - 1).getImages())) {
                List<ImageInfo> imageInfos = new ArrayList<>();

                String[] sourceStrArray = datas.get(position - 1).getImages().split(";");

                nineGridView.setVisibility(View.VISIBLE);

                for (int i = 0; i < sourceStrArray.length; i++) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setBigImageUrl(FILE_SERVER_URL + sourceStrArray[i]);
                    if (sourceStrArray.length == 1)
                        imageInfo.setThumbnailUrl(FILE_SERVER_URL + sourceStrArray[i] + "?imageView2/0/w/200/interlace/1/q/100");
                    else
                        imageInfo.setThumbnailUrl(FILE_SERVER_URL + sourceStrArray[i] + "?imageView2/1/w/140/interlace/1/q/100");

                    imageInfos.add(imageInfo);
                }
                nineGridView.setAdapter(new CircleNineAdapter(getContext(), imageInfos));
            } else {
                nineGridView.setVisibility(View.GONE);
            }

            // 设置头像
            Glide.with(getContext())
                    .load(SEVER_ADDRESS + datas.get(position - 1).getHeadimgurl())
                    .transform(new GlideCircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageViewt);

            // 设置正文
            collapsibleTextView.setFullString(datas.get(position - 1).getContent());
            //collapsibleTextView.setExpanded(true);

            // 设置昵称
            name.setText(datas.get(position - 1).getNickname());

            // 设置时间
            try {
                times.setText(UtilTools.getDateDiff(datas.get(position - 1).getCreate_date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

    }

    /**
     * 使用Glide缓存加载器 加载九宫格
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

}


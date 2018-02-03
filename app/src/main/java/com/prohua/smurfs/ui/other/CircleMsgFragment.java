package com.prohua.smurfs.ui.other;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prohua.smurfs.R;
import com.prohua.smurfs.application.SmurfsApplication;
import com.prohua.smurfs.database.StudentInfoDao;
import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.CircleMsgNet;
import com.prohua.smurfs.ui.widgt.GlideCircleTransform;
import com.prohua.smurfs.util.UtilTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;
import static com.prohua.smurfs.base.GlobalConfig.FILE_SERVER_URL;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * CircleMsgFragment 校友圈消息界面
 * Created by Deep on 2017/4/26 0026.
 */

public class CircleMsgFragment extends SupportFragment {

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.re)
    RecyclerView recyclerView;

    private MsgAdapter msgAdapter;

    /**
     * 懒加载
     */
    public static CircleMsgFragment newInstance() {

        Bundle args = new Bundle();

        CircleMsgFragment fragment = new CircleMsgFragment();
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

        View view = inflater.inflate(R.layout.circle_msg_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initView();

        return view;
    }

    private void initView() {

        new CircleMsgNet(new CallBackInterface() {
            @Override
            public void complete() {
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(msgAdapter = new MsgAdapter());

                        studentInfo.setLastId(CircleMsgNet.circleMsgDataOutBean.getData().get(0).getId());
                        StudentInfoDao studentInfoDao = SmurfsApplication.getInstances().getDaoSession().getStudentInfoDao();
                        studentInfoDao.deleteAll();
                        studentInfoDao.insert(studentInfo);
                    }
                });
            }

            @Override
            public void error() {

            }
        }).pullDown("0");
    }

    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);
    }

    class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.circle_msg_item_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Glide.with(getActivity())
                    .load(SEVER_ADDRESS + CircleMsgNet.circleMsgDataOutBean.getData().get(position).getHeadImgUrl())
                    .transform(new GlideCircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.head);
            if (!"".equals(CircleMsgNet.circleMsgDataOutBean.getData().get(position).getImages())) {
                String[] sourceStrArray = CircleMsgNet.circleMsgDataOutBean.getData().get(position).getImages().split(";");

                Glide.with(getActivity())
                        .load(FILE_SERVER_URL + sourceStrArray[0] + "?imageView2/1/w/140/interlace/1/q/100")
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);
            }
            holder.nickName.setText(CircleMsgNet.circleMsgDataOutBean.getData().get(position).getNickName());
            holder.text.setText(CircleMsgNet.circleMsgDataOutBean.getData().get(position).getText());
            holder.content.setText(CircleMsgNet.circleMsgDataOutBean.getData().get(position).getContent());
            holder.time.setText(CircleMsgNet.circleMsgDataOutBean.getData().get(position).getCreateDate());
        }

        @Override
        public int getItemCount() {
            return CircleMsgNet.circleMsgDataOutBean.getData().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.headimg)
            ImageView head;
            @BindView(R.id.images)
            ImageView image;
            @BindView(R.id.nickname)
            TextView nickName;
            @BindView(R.id.text)
            TextView text;
            @BindView(R.id.content)
            TextView content;
            @BindView(R.id.time)
            TextView time;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}

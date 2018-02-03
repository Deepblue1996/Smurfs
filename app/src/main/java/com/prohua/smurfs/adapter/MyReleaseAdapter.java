package com.prohua.smurfs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prohua.smurfs.R;
import com.prohua.smurfs.net.ReleaseTaskListNet;
import com.prohua.smurfs.ui.widgt.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class MyReleaseAdapter extends RecyclerView.Adapter<MyReleaseAdapter.MyViewHolder> {

    private Context mContext;

    public MyReleaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_release_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load(SEVER_ADDRESS + ReleaseTaskListNet.releaseTaskListOutBean.getData().get(position).getHeadimgurl())
                .transform(new GlideCircleTransform(mContext))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.head);
        holder.nickname.setText(ReleaseTaskListNet.releaseTaskListOutBean.getData().get(position).getNickname());
        if("1".equals(ReleaseTaskListNet.releaseTaskListOutBean.getData().get(position).getSex()))
            holder.sexTextView.setText("男");
        else
            holder.sexTextView.setText("女");
        if("2".equals(ReleaseTaskListNet.releaseTaskListOutBean.getData().get(position).getType()))
            holder.typeTextView.setText("修电脑");
        else
            holder.typeTextView.setText("快递代拿");
        holder.moneyTextView.setText("￥"+ReleaseTaskListNet.releaseTaskListOutBean.getData().get(position).getReward()+"");
    }

    @Override
    public int getItemCount() {
        if(ReleaseTaskListNet.releaseTaskListOutBean == null) {
            return 0;
        }
        return ReleaseTaskListNet.releaseTaskListOutBean.getData().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.headimg)
        ImageView head;
        @BindView(R.id.nickname)
        TextView nickname;
        @BindView(R.id.t_sex)
        TextView sexTextView;
        @BindView(R.id.type)
        TextView typeTextView;
        @BindView(R.id.money)
        TextView moneyTextView;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
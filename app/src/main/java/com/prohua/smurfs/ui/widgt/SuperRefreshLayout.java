package com.prohua.smurfs.ui.widgt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prohua.smurfs.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * Created by Deep on 2017/3/31 0031.
 */

public class SuperRefreshLayout extends SwipeRefreshLayout {

    private static OnRefreshHandler onRefreshHandler;
    private static boolean isRefresh = false;
    private Adapter adapter;
    private int mTouchSlop;
    private float mPrevX;

    public SuperRefreshLayout(Context context) {
        this(context, null);
    }

    public SuperRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(0xff3b93eb);
        setProgressBackgroundColorSchemeColor(0xffffffff);
        float scale = context.getResources().getDisplayMetrics().density;
        setProgressViewEndTarget(true, (int) (64 * scale + 0.5f));
        //refreshLayout.setProgressViewOffset(false,dip2px(this,-40),dip2px(this,64));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 监听器
     */
    public void setOnRefreshHandler(OnRefreshHandler handler) {
        onRefreshHandler = handler;
        super.setOnRefreshListener(new OnRefreshCallBack());
    }

    /**
     * 自动刷新,原生不支持,通过反射修改字段属性
     */
    public void autoRefresh() {
        try {
            setRefreshing(true);
            Field field = SwipeRefreshLayout.class.getDeclaredField("mNotify");
            field.setAccessible(true);
            field.set(this, true);
        } catch (Exception e) {
            if (onRefreshHandler != null) {
                onRefreshHandler.refresh();
            }
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        isRefresh = isRefreshing();
    }

    /**
     * 加载完毕
     *
     * @param hasMore 是否还有下一页
     */
    public void loadComplete(boolean hasMore) {
        if (adapter == null) {
            throw new RuntimeException("must call method setAdapter to bind data");
        }
        adapter.setState(hasMore ? Adapter.STATE_MORE : Adapter.STATE_END);
    }

    /**
     * 加载出错
     */
    public void loadError() {
        if (adapter == null) {
            throw new RuntimeException("must call method setAdapter to bind data");
        }
        adapter.setState(Adapter.STATE_ERROR);
    }

    /**
     * 只支持 RecyclerView 加载更多,且需要通过此方法设置适配器
     */
    public void setAdapter(@NonNull RecyclerView recyclerView, @NonNull SuperRefreshLayout.Adapter mAdapter) {
        adapter = mAdapter;
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onRefreshHandler != null
                        && !isRefreshing()
                        && (adapter.getState() == Adapter.STATE_MORE || adapter.getState() == Adapter.STATE_ERROR)
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && !ViewCompat.canScrollVertically(recyclerView, 1)
                        ) {
                    adapter.setState(Adapter.STATE_LOAIND);
                    onRefreshHandler.loadMore();
                }
            }
        });
    }

    /**
     * 如果滑动控件嵌套过深,可通过该方法控制是否可以下拉
     */
    public void setRefreshEnable(boolean enable) {
        // boolean e = !ViewCompat.canScrollVertically(scrollView,-1);
        if (isEnabled() && !enable) {
            setEnabled(false);
        } else if (!isEnabled() && enable) {
            setEnabled(true);
        }
    }

    /**
     * 解决水平滑动冲突
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 代理回调(底层回调)
     */
    private class OnRefreshCallBack implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            if (adapter != null && adapter.getState() != Adapter.STATE_MORE) {
                adapter.setState(Adapter.STATE_MORE);
            }
            if (onRefreshHandler != null) {
                onRefreshHandler.refresh();
            }
        }
    }

    /**
     * 对方开放的回调
     */
    public static abstract class OnRefreshHandler {

        public abstract void refresh();

        public void loadMore() {

        }
    }

    /**
     * 支持加载更多的代理适配器
     */
    public static abstract class Adapter extends RecyclerView.Adapter {

        // 类型ITEM
        private static final int TYPE_HEADER = 0;
        // 类型ITEM
        private static final int TYPE_ITEM = 1;
        // 类型FOOTER
        private static final int TYPE_FOOTER = 2;

        static final int STATE_MORE = 0, STATE_LOAIND = 1, STATE_END = 2, STATE_ERROR = 3;

        int state = STATE_MORE;
        // 实例
        private LayoutInflater mInflater;
        // 上下文
        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setState(int state) {
            if (this.state != state) {
                this.state = state;
                notifyItemChanged(getItemCount() - 1);
            }
        }

        public int getState() {
            return state;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_HEADER) {
                View view = mInflater.inflate(R.layout.second_header, parent, false);
                return new HeaderViewHolder(view);
            } else if (viewType == TYPE_ITEM) {
                return onCreateItemHolder(parent, viewType);
            } else if (viewType == TYPE_FOOTER) {
                View view = mInflater.inflate(R.layout.loadmore_default_footer, parent, false);
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

                if (studentInfo == null) {
                    headerViewHolder.textView.setText(mContext.getString(R.string.polytechnic_student));
                    Glide.with(mContext)
                            .load(R.mipmap.guest)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(headerViewHolder.imageView);
                } else {
                    headerViewHolder.textView.setText(studentInfo.getNickName());
                    Glide.with(mContext)
                            .load(SEVER_ADDRESS + studentInfo.getHeadImgUrl())
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(headerViewHolder.imageView);
                }

                Glide.with(mContext)
                        .load(R.mipmap.xy_bg)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(headerViewHolder.headerView);

            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if (state == STATE_END) {
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.textView.setText(mContext.getString(R.string.no_more));
                } else if (state == STATE_MORE) {
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.textView.setText(mContext.getString(R.string.click_load));
                } else if (state == STATE_LOAIND) {
                    footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                    footerViewHolder.textView.setText(mContext.getString(R.string.hard_loading));
                } else if (state == STATE_ERROR) {
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.textView.setText(mContext.getString(R.string.load_fail_click));
                }
                footerViewHolder.itemView.setOnClickListener(view -> {
                    if (onRefreshHandler != null && !isRefresh && (state == STATE_MORE || state == STATE_ERROR)) {
                        setState(STATE_LOAIND);
                        onRefreshHandler.loadMore();
                    }
                });
            } else {
                onBindItemHolder(holder, position);
            }
        }

        /**
         * 顶部的item的ViewHolder
         */
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.roundImageView)
            ImageView imageView;
            @BindView(R.id.header)
            ImageView headerView;
            @BindView(R.id.textView)
            TextView textView;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        /**
         * 底部的item的ViewHolder
         */
        public class FooterViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.loadmore_default_footer_progressbar)
            ProgressBar progressBar;
            @BindView(R.id.loadmore_default_footer_tv)
            TextView textView;

            public FooterViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public int getItemCount() {
            return getCount() + 2;
        }

        public abstract RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType);

        public abstract void onBindItemHolder(RecyclerView.ViewHolder holder, int position);

        public abstract int getCount();

    }

}

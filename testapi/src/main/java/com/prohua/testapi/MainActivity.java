package com.prohua.testapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CircleBean circleBean;
    ShowAdapter showAdapter;

    public final static String str = "http://file.glxzs.gdlgxy.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NineGridView.setImageLoader(new PicassoImageLoader());

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        showAdapter = new ShowAdapter();
        recyclerView.setAdapter(showAdapter);
        POST();
    }

    private class ShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<CircleNoteBean> datas = new ArrayList<>();//数据

        public void addDatas(List<CircleNoteBean> datas) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            //holder.setIsRecyclable(false);

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            if (!"".equals(datas.get(position).getImages())) {
                List<ImageInfo> imageInfos = new ArrayList<>();

                String[] sourceStrArray = datas.get(position).getImages().split(";");

                for (int i = 0; i < sourceStrArray.length; i++) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setBigImageUrl(str + sourceStrArray[i]);
                    imageInfo.setThumbnailUrl(str + sourceStrArray[i] + "?imageView2/1/w/140/interlace/1/q/100");
                    imageInfos.add(imageInfo);
                }

                myViewHolder.nineGridView.setAdapter(new NineAdapter(getApplicationContext(), imageInfos));
            } else {
                List<ImageInfo> imageInfos = new ArrayList<>();
                myViewHolder.nineGridView.setAdapter(new NineAdapter(getApplicationContext(), imageInfos));
            }

            Glide.with(getApplicationContext())
                    .load("https://glxzs.gdlgxy.net/public/" + datas.get(position).getHeadimgurl())
                    .into(myViewHolder.imageView);

            myViewHolder.name.setText(datas.get(position).getNickname());
            myViewHolder.textView.setText(datas.get(position).getContent());


            //保存view对象到ObjectClass类中

        }


        @Override
        public int getItemCount() {
            return this.datas.size();
        }

        //自定义ViewHolder，用于显示页数
        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            TextView name;
            ImageView imageView;
            NineGridView nineGridView;

            public MyViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.content);
                name = (TextView) view.findViewById(R.id.name);
                imageView = (ImageView) view.findViewById(R.id.imaget);
                nineGridView = (NineGridView) view.findViewById(R.id.nineGrid);

            }
        }
    }

    public static int iou = 0;

    private void show() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (iou == 0)
                    Collections.reverse(circleBean.getData());
                showAdapter.addDatas(circleBean.getData());
                if (iou < 5) {
                    POST();
                    iou++;
                }
            }
        });

    }

    /**
     * Picasso 加载
     */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    public class NineAdapter extends NineGridViewAdapter {
        private int statusHeight;

        public NineAdapter(Context context, List<ImageInfo> imageInfo) {
            super(context, imageInfo);
            statusHeight = getStatusHeight(context);
        }

        @Override
        protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
            for (int i = 0; i < imageInfo.size(); i++) {
                ImageInfo info = imageInfo.get(i);
                View imageView;
                if (i < nineGridView.getMaxSize()) {
                    imageView = nineGridView.getChildAt(i);
                } else {
                    //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
                    imageView = nineGridView.getChildAt(nineGridView.getMaxSize() - 1);
                }
                info.imageViewWidth = imageView.getWidth();
                info.imageViewHeight = imageView.getHeight();
                int[] points = new int[2];
                imageView.getLocationInWindow(points);
                info.imageViewX = points[0];
                info.imageViewY = points[1] - statusHeight;
            }

            Intent intent = new Intent(context, ImagePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        }

        /**
         * 获得状态栏的高度
         */
        public int getStatusHeight(Context context) {
            int statusHeight = -1;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
                statusHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return statusHeight;
        }

    }

    public static int beforeId = 0;

    //异步post
    public void POST() {
        //添加表单内容
        RequestBody formBody = new FormBody.Builder()
                .add("before", String.valueOf(beforeId))
                .add("personalId", "0")
                .build();

        Request request = new Request.Builder()
                .url("https://glxzs.gdlgxy.net/public/index/AlumniCorcle/index.html")
                .post(formBody)
                .build();

        genericClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string();

                circleBean = new CircleBean();

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    circleBean.setCode(jsonObject.getString("code"));
                    circleBean.setMsg(jsonObject.getString("msg"));
                    circleBean.setUrl(jsonObject.getString("url"));
                    circleBean.setWait(jsonObject.getString("wait"));

                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                    List<CircleNoteBean> data = new ArrayList<>();

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        CircleNoteBean circleNoteBean = new CircleNoteBean();

                        circleNoteBean.setNickname(item.getString("nickname"));
                        circleNoteBean.setIntro(item.getString("intro"));
                        circleNoteBean.setHeadimgurl(item.getString("headimgurl"));
                        circleNoteBean.setId(item.getString("id"));
                        circleNoteBean.setUid(item.getString("uid"));
                        circleNoteBean.setType(item.getString("type"));
                        circleNoteBean.setContent(item.getString("content"));
                        circleNoteBean.setImages(item.getString("images"));
                        circleNoteBean.setAddress(item.getString("address"));
                        circleNoteBean.setCreate_date(item.getString("create_date"));

                        if (beforeId != 0)
                            beforeId = Integer.valueOf(item.getString("id")) <= beforeId ?
                                    Integer.valueOf(item.getString("id")) : beforeId;
                        else
                            beforeId = Integer.valueOf(item.getString("id"));

                        if (item.has("comments")) {

                            JSONArray jsonArray2 = new JSONArray(item.getString("comments"));

                            List<CommentsBean> commentsBeanList = new ArrayList<>();

                            for (int j = 0; j < jsonArray2.length(); j++) {

                                JSONObject item2 = jsonArray2.getJSONObject(j);

                                CommentsBean commentsBean = new CommentsBean();

                                commentsBean.setId(item2.getString("id"));
                                commentsBean.setAcid(item2.getString("acid"));
                                commentsBean.setUid(item2.getString("uid"));
                                commentsBean.setNickname(item2.getString("nickname"));
                                commentsBean.setReply(item2.getString("reply"));
                                commentsBean.setText(item2.getString("text"));
                                commentsBean.setCreate_date(item2.getString("create_date"));

                                commentsBeanList.add(commentsBean);
                            }

                            circleNoteBean.setComments(commentsBeanList);
                        }

                        if (item.has("likes")) {

                            JSONArray jsonArray3 = new JSONArray(item.getString("likes"));

                            List<LikesBean> likesBeanList = new ArrayList<>();

                            for (int k = 0; k < jsonArray3.length(); k++) {

                                JSONObject item3 = jsonArray3.getJSONObject(k);

                                LikesBean likesBean = new LikesBean();

                                likesBean.setAcid(item3.getString("acid"));
                                likesBean.setUid(item3.getString("uid"));
                                likesBean.setNickname(item3.getString("nickname"));

                                likesBeanList.add(likesBean);
                            }

                            circleNoteBean.setLikes(likesBeanList);
                        }

                        data.add(circleNoteBean);
                    }

                    circleBean.setData(data);

                    show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("X-Requested-With", "XMLHttpRequest")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }
}

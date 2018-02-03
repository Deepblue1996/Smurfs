package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.CircleNoteBean;
import com.prohua.smurfs.bean.CircleNoteOutBean;
import com.prohua.smurfs.bean.CommentsBean;
import com.prohua.smurfs.bean.LikesBean;
import com.prohua.smurfs.util.UtilTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.prohua.smurfs.application.SmurfsApplication.okHttpClient;
import static com.prohua.smurfs.base.GlobalConfig.CIRCLE_BBS_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * CircleNetOperation 获取朋友圈列表数据
 * Created by Deep on 2017/4/6 0006.
 */

public class CircleNetOperation {

    public static CircleNoteOutBean circleNoteOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public static int beforeId = 0;

    public CircleNetOperation(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDownRefresh(String str) {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .add("before", String.valueOf(beforeId))
                .add("personalId", str)
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS+CIRCLE_BBS_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                circleNoteOutBean = analysisCircleJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    /**
     * 解析校友圈json
     */

    public CircleNoteOutBean analysisCircleJson(String dataJson) {

        CircleNoteOutBean circleNoteOutBean = new CircleNoteOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            circleNoteOutBean.setCode(jsonObject.getString("code"));
            circleNoteOutBean.setMsg(jsonObject.getString("msg"));
            circleNoteOutBean.setUrl(jsonObject.getString("url"));
            circleNoteOutBean.setWait(jsonObject.getString("wait"));

            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

            List<CircleNoteBean> data = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
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

                if(beforeId != 0)
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
            circleNoteOutBean.setData(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return circleNoteOutBean;
    }
}

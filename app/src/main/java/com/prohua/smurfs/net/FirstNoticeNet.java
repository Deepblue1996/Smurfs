package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.NoticeOutBean;
import com.prohua.smurfs.bean.NoticeBean;
import com.prohua.smurfs.util.UtilTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.prohua.smurfs.application.SmurfsApplication.okHttpClient;
import static com.prohua.smurfs.base.GlobalConfig.FIRST_NOTICE_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * FirstNoticeNet 获取通知
 * Created by Deep on 2017/4/7 0007.
 */

public class FirstNoticeNet {

    public static NoticeOutBean noticeOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public FirstNoticeNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown() {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .add("last_notice_id", "0")
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + FIRST_NOTICE_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                noticeOutBean = analysisCircleJson(response.body().string());


                mInterface.complete();
            }
        });
    }

    private NoticeOutBean analysisCircleJson(String dataJson) {

        NoticeOutBean noticeOutBean = new NoticeOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            noticeOutBean.setCode(jsonObject.getString("code"));
            noticeOutBean.setMsg(jsonObject.getString("msg"));
            noticeOutBean.setUrl(jsonObject.getString("url"));
            noticeOutBean.setWait(jsonObject.getString("wait"));

            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));

            NoticeBean noticeBean = new NoticeBean();

            noticeBean.setId(jsonObject1.getString("id"));
            noticeBean.setTitle(jsonObject1.getString("title"));
            noticeBean.setHref(jsonObject1.getString("href"));
            noticeBean.setContent(jsonObject1.getString("content"));
            noticeBean.setCreate_date(jsonObject1.getString("create_date"));

            noticeOutBean.setData(noticeBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return noticeOutBean;
    }
}

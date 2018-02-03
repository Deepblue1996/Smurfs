package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.CircleMsgDataBean;
import com.prohua.smurfs.bean.CircleMsgDataOutBean;
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
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.USER_CIRCLE_CHECK_MSG_ADDRESS;

/**
 * Created by Deep on 2017/4/26 0026.
 */

public class CircleMsgNet {
    //持有接口变量
    private CallBackInterface mInterface;
    public static CircleMsgDataOutBean circleMsgDataOutBean;

    public CircleMsgNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown(String acMsgLastId) {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容

        RequestBody formBody = UtilTools.paramFromBody()
                .add("lastId", acMsgLastId)
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_CIRCLE_CHECK_MSG_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                circleMsgDataOutBean = analysisCircleMsgDataJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    /**
     * 解析校友圈json
     */

    public CircleMsgDataOutBean analysisCircleMsgDataJson(String dataJson) {

        CircleMsgDataOutBean circleMsgDataOutBean = new CircleMsgDataOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            circleMsgDataOutBean.setCode(jsonObject.getString("code"));
            circleMsgDataOutBean.setMsg(jsonObject.getString("msg"));
            circleMsgDataOutBean.setUrl(jsonObject.getString("url"));
            circleMsgDataOutBean.setWait(jsonObject.getString("wait"));

            if ("1".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                List<CircleMsgDataBean> data = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    CircleMsgDataBean circleMsgDataBean = new CircleMsgDataBean();

                    circleMsgDataBean.setAcId(item.getString("acid"));
                    circleMsgDataBean.setHeadImgUrl(item.getString("headimgurl"));
                    circleMsgDataBean.setContent(item.getString("content"));
                    circleMsgDataBean.setCreateDate(item.getString("create_date"));
                    circleMsgDataBean.setId(item.getString("id"));
                    circleMsgDataBean.setImages(item.getString("images"));
                    circleMsgDataBean.setNickName(item.getString("nickname"));
                    circleMsgDataBean.setText(item.getString("text"));
                    circleMsgDataBean.setuId(item.getString("uid"));

                    data.add(circleMsgDataBean);
                }
                circleMsgDataOutBean.setData(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return circleMsgDataOutBean;
    }
}

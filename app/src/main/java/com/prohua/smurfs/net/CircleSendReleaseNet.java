package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.CircleSendReleaseOutBean;
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
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.USER_CIRCLE_RELEASE_ADDRESS;

/**
 * Created by Deep on 2017/4/29 0029.
 */

public class CircleSendReleaseNet {

    public static CircleSendReleaseOutBean circleSendReleaseOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public CircleSendReleaseNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown(String content, String address, String locationJson, int type) {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .add("content",content)
                .add("address", address)
                .add("locationJson",locationJson)
                .add("type",""+type)
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_CIRCLE_RELEASE_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                circleSendReleaseOutBean = analysisCircleSendReleaseJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    private CircleSendReleaseOutBean analysisCircleSendReleaseJson(String dataJson) {

        CircleSendReleaseOutBean circleSendReleaseOutBean = new CircleSendReleaseOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            circleSendReleaseOutBean.setCode(jsonObject.getString("code"));
            circleSendReleaseOutBean.setMsg(jsonObject.getString("msg"));
            circleSendReleaseOutBean.setUrl(jsonObject.getString("url"));
            circleSendReleaseOutBean.setWait(jsonObject.getString("wait"));
            circleSendReleaseOutBean.setData(jsonObject.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return circleSendReleaseOutBean;
    }
}

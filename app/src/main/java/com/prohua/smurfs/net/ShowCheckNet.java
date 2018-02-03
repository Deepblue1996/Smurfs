package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.ShowCheckOutBean;
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
import static com.prohua.smurfs.base.GlobalConfig.USER_INFO_IS_SHOW_CHECK_ADDRESS;

/**
 * ShowCheckNet 获取是否显示验证码
 * Created by Deep on 2017/4/21 0021.
 */

public class ShowCheckNet {

    public static ShowCheckOutBean showCheckOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public ShowCheckNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown(String account) {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .add("account", account)
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_INFO_IS_SHOW_CHECK_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showCheckOutBean = analysisShowCheckJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    private ShowCheckOutBean analysisShowCheckJson(String dataJson) {

        ShowCheckOutBean showCheckOutBean = new ShowCheckOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            showCheckOutBean.setCode(jsonObject.getString("code"));
            showCheckOutBean.setMsg(jsonObject.getString("msg"));
            showCheckOutBean.setData(jsonObject.getString("data"));
            showCheckOutBean.setUrl(jsonObject.getString("url"));
            showCheckOutBean.setWait(jsonObject.getString("wait"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return showCheckOutBean;
    }
}

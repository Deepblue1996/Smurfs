package com.prohua.smurfs.net;

import com.prohua.smurfs.util.UtilTools;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.prohua.smurfs.application.SmurfsApplication.okHttpClient;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.USER_LOG_OUT_ADDRESS;

/**
 * LogOutNet 退出账号
 * Created by Deep on 2017/4/23 0023.
 */

public class LogOutNet {
    //持有接口变量
    private CallBackInterface mInterface;

    public LogOutNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown() {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_LOG_OUT_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mInterface.complete();
            }
        });
    }
}

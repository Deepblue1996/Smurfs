package com.prohua.smurfs.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Deep on 2017/4/30 0030.
 */

public class ReleaseImageFileNet {

    //持有接口变量
    private CallBackInterface mInterface;

    public ReleaseImageFileNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullUp(String path, String token) {

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", file.getName(), body);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("token", token);

        Request request = new Request.Builder().url("http://up-z2.qiniu.com/").post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq" ,"onFailure");
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);
                    mInterface.complete();
                } else {
                    mInterface.error();
                    Log.i("lfq" ,response.message() + " error : body " + response.body().string());
                }
            }
        });

    }
}

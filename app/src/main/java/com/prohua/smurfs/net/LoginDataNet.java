package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.StudentInfoBean;
import com.prohua.smurfs.bean.StudentInfoOutBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.prohua.smurfs.application.SmurfsApplication.okHttpClient;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.USER_INFO_ADDRESS;

/**
 * LoginDataNet 登录
 * Created by Deep on 2017/4/20 0020.
 */

public class LoginDataNet {

    public static StudentInfoOutBean studentInfoOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public LoginDataNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown(String account, String passWord, String check) {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = new FormBody.Builder()
                .add("account", account)
                .add("password", passWord)
                .add("check", check)
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_INFO_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                studentInfoOutBean = analysisStudentInfoJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    private StudentInfoOutBean analysisStudentInfoJson(String dataJson) {

        StudentInfoOutBean studentInfoOutBean = new StudentInfoOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            studentInfoOutBean.setCode(jsonObject.getString("code"));
            studentInfoOutBean.setMsg(jsonObject.getString("msg"));
            studentInfoOutBean.setUrl(jsonObject.getString("url"));
            studentInfoOutBean.setWait(jsonObject.getString("wait"));

            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));

            StudentInfoBean studentInfoBean = new StudentInfoBean();

            studentInfoBean.setStudentNo(jsonObject1.getString("stud_no"));
            studentInfoBean.setLengthOfSchooling(jsonObject1.getString("length_of_schooling"));
            studentInfoBean.setNickName(jsonObject1.getString("nickname"));
            studentInfoBean.setSex(jsonObject1.getString("sex"));
            studentInfoBean.setuId(jsonObject1.getString("uid"));
            studentInfoBean.setPhone(jsonObject1.getString("phone"));
            studentInfoBean.setHeadImgUrl(jsonObject1.getString("headimgurl"));
            studentInfoBean.setIntro(jsonObject1.getString("intro"));
            studentInfoBean.setGrade(jsonObject1.getString("grade"));
            studentInfoBean.setClientId(jsonObject1.getString("clientid"));
            studentInfoBean.setIntegral(jsonObject1.getString("integral"));
            studentInfoBean.setToken(jsonObject.getString("msg"));
            studentInfoBean.setState(true);

            studentInfoOutBean.setStudentInfoBean(studentInfoBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return studentInfoOutBean;
    }
}

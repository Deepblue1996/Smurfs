package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.ReleaseTaskListBean;
import com.prohua.smurfs.bean.ReleaseTaskListOutBean;
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
import static com.prohua.smurfs.base.GlobalConfig.USER_EXPRESS_TASK_LIST_ADDRESS;

/**
 * Created by Deep on 2017/4/27 0027.
 */

public class ReleaseTaskListNet {

    public static ReleaseTaskListOutBean releaseTaskListOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public ReleaseTaskListNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown() {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + USER_EXPRESS_TASK_LIST_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mInterface.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                releaseTaskListOutBean = analysisReleaseTaskListJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    public ReleaseTaskListOutBean analysisReleaseTaskListJson(String dataJson) {

        ReleaseTaskListOutBean releaseTaskListOutBean = new ReleaseTaskListOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            releaseTaskListOutBean.setCode(jsonObject.getString("code"));
            releaseTaskListOutBean.setMsg(jsonObject.getString("msg"));
            releaseTaskListOutBean.setUrl(jsonObject.getString("url"));
            releaseTaskListOutBean.setWait(jsonObject.getString("wait"));

            if ("1".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                List<ReleaseTaskListBean> data = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    ReleaseTaskListBean releaseTaskListBean = new ReleaseTaskListBean();

                    releaseTaskListBean.setId(item.getString("id"));
                    releaseTaskListBean.setUid(item.getString("uid"));
                    releaseTaskListBean.setType(item.getString("type"));
                    releaseTaskListBean.setContact_phone(item.getString("contact_phone"));
                    releaseTaskListBean.setRequirement_sex(item.getString("requirement_sex"));
                    releaseTaskListBean.setReward(item.getString("reward"));
                    releaseTaskListBean.setRemarks(item.getString("remarks"));
                    releaseTaskListBean.setHand_over_time_start(item.getString("hand_over_time_start"));
                    releaseTaskListBean.setHand_over_time_end(item.getString("hand_over_time_end"));
                    releaseTaskListBean.setCreate_date(item.getString("create_date"));
                    releaseTaskListBean.setAccept_time(item.getString("accept_time"));
                    releaseTaskListBean.setAccept_uid(item.getString("accept_uid"));
                    releaseTaskListBean.setFinish_time(item.getString("finish_time"));
                    releaseTaskListBean.setClose_time(item.getString("close_time"));
                    releaseTaskListBean.setState(item.getString("state"));
                    releaseTaskListBean.setStr1(item.getString("str1"));
                    releaseTaskListBean.setStr2(item.getString("str2"));
                    releaseTaskListBean.setStr3(item.getString("str3"));
                    releaseTaskListBean.setText1(item.getString("text1"));
                    releaseTaskListBean.setText2(item.getString("text2"));
                    releaseTaskListBean.setDate1(item.getString("date1"));
                    releaseTaskListBean.setDate2(item.getString("date2"));
                    releaseTaskListBean.setDate3(item.getString("date3"));
                    releaseTaskListBean.setInt1(item.getString("int1"));
                    releaseTaskListBean.setInt2(item.getString("int2"));
                    releaseTaskListBean.setInt3(item.getString("int3"));
                    releaseTaskListBean.setFloat1(item.getString("float1"));
                    releaseTaskListBean.setFloat2(item.getString("float2"));
                    releaseTaskListBean.setFloat3(item.getString("float3"));
                    releaseTaskListBean.setNickname(item.getString("nickname"));
                    releaseTaskListBean.setHeadimgurl(item.getString("headimgurl"));
                    releaseTaskListBean.setSex(item.getString("sex"));

                    data.add(releaseTaskListBean);
                }
                releaseTaskListOutBean.setData(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return releaseTaskListOutBean;
    }
}

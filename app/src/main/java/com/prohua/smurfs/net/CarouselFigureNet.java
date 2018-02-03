package com.prohua.smurfs.net;

import com.prohua.smurfs.bean.CarouselFigureOutBean;
import com.prohua.smurfs.bean.CarouselFigureBean;
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
import static com.prohua.smurfs.base.GlobalConfig.FIRST_CAROUSEL_ADDRESS;
import static com.prohua.smurfs.base.GlobalConfig.SEVER_ADDRESS;

/**
 * CarouselFigureNet 获取滚动图
 * Created by Deep on 2017/4/7 0007.
 */

public class CarouselFigureNet {

    public static CarouselFigureOutBean carouselFigureOutBean;
    //持有接口变量
    private CallBackInterface mInterface;

    public CarouselFigureNet(CallBackInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
    }

    public void pullDown() {

        // 备注：circleId 零为全校 其它个人圈子id
        //添加表单内容
        RequestBody formBody = UtilTools.paramFromBody()
                .add("last_slider_img_id", "0")
                .build();

        Request request = new Request.Builder()
                .url(SEVER_ADDRESS + FIRST_CAROUSEL_ADDRESS)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                carouselFigureOutBean = analysisCircleJson(response.body().string());
                mInterface.complete();
            }
        });
    }

    private CarouselFigureOutBean analysisCircleJson(String dataJson) {

        CarouselFigureOutBean carouselFigureOutBean = new CarouselFigureOutBean();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);

            carouselFigureOutBean.setCode(jsonObject.getString("code"));
            carouselFigureOutBean.setMsg(jsonObject.getString("msg"));
            carouselFigureOutBean.setUrl(jsonObject.getString("url"));
            carouselFigureOutBean.setWait(jsonObject.getString("wait"));

            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

            List<CarouselFigureBean> carouselFigureBeanList = new ArrayList<>();

            for (int k = 0; k < jsonArray.length(); k++) {

                JSONObject item = jsonArray.getJSONObject(k);

                CarouselFigureBean carouselFigureBean = new CarouselFigureBean();

                carouselFigureBean.setId(item.getString("id"));
                carouselFigureBean.setTitle(item.getString("title"));
                carouselFigureBean.setHref(item.getString("href"));
                carouselFigureBean.setSrc(item.getString("src"));

                carouselFigureBeanList.add(carouselFigureBean);
            }

            carouselFigureOutBean.setData(carouselFigureBeanList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carouselFigureOutBean;
    }
}

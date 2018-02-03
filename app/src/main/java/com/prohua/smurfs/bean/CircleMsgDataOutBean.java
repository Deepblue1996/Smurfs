package com.prohua.smurfs.bean;

import java.util.List;

/**
 * Created by Deep on 2017/4/26 0026.
 */

public class CircleMsgDataOutBean {

    private String code;
    private String msg;
    private List<CircleMsgDataBean> data;
    private String url;
    private String wait;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CircleMsgDataBean> getData() {
        return data;
    }

    public void setData(List<CircleMsgDataBean> data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWait() {
        return wait;
    }

    public void setWait(String wait) {
        this.wait = wait;
    }
}

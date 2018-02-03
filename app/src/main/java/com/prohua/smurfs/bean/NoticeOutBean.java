package com.prohua.smurfs.bean;

/**
 * 首页通知外壳 json
 * Created by Deep on 2017/4/7 0007.
 */

public class NoticeOutBean {

    // 代码 默认1
    private String code;
    // 代码 默认null
    private String msg;
    // 通知
    private NoticeBean data;
    // 响应地址
    private String url;
    // 等待时间
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

    public NoticeBean getData() {
        return data;
    }

    public void setData(NoticeBean data) {
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

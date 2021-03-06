package com.prohua.testapi;

import java.util.List;

/**
 * Created by Deep on 2017/4/6 0006.
 */

public class CircleBean {

    // 代码 默认1
    private String code;
    // 代码 默认null
    private String msg;
    // 帖子列表
    private List<CircleNoteBean> data;
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

    public List<CircleNoteBean> getData() {
        return data;
    }

    public void setData(List<CircleNoteBean> data) {
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

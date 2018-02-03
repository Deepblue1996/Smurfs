package com.prohua.smurfs.bean;

/**
 * Created by Deep on 2017/4/20 0020.
 */

public class StudentInfoOutBean {

    private String code;
    private String msg;
    private StudentInfoBean studentInfoBean;
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

    public StudentInfoBean getStudentInfoBean() {
        return studentInfoBean;
    }

    public void setStudentInfoBean(StudentInfoBean studentInfoBean) {
        this.studentInfoBean = studentInfoBean;
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

package com.prohua.smurfs.bean;

/**
 * Created by Deep on 2017/4/20 0020.
 */

public class StudentInfoBean {
    // 学生ID
    private String studentNo;
    // 没搞懂, 区别专科，本科？
    private String LengthOfSchooling;
    // 昵称
    private String nickName;
    // 性别
    private String sex;
    // id
    private String uId;
    // 手机号码
    private String phone;
    // 头像地址
    private String headImgUrl;
    // 个人签名
    private String intro;
    // 第几届
    private String grade;
    // 客户端id null?
    private String clientId;
    // 记录
    private String token;
    // 0
    private String integral;
    // 判断登录状态
    private boolean state;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getLengthOfSchooling() {
        return LengthOfSchooling;
    }

    public void setLengthOfSchooling(String lengthOfSchooling) {
        LengthOfSchooling = lengthOfSchooling;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}

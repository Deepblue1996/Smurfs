package com.prohua.smurfs.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * StudentInfo 学生信息类
 * Created by Deep on 2017/4/20 0020.
 */

@Entity
public class StudentInfo {

    // 主键
    @Id
    private Long id;

    // 学生ID
    @Property(nameInDb = "stud_no")
    private String studentNo;

    // 没搞懂, 区别专科，本科？
    @Property(nameInDb = "length_of_schooling")
    private String LengthOfSchooling;

    // 昵称
    @Property(nameInDb = "nickname")
    private String nickName;

    // 性别
    @Property(nameInDb = "sex")
    private String sex;

    // id
    @Property(nameInDb = "uid")
    private String uId;

    // 手机号码
    @Property(nameInDb = "phone")
    private String phone;

    // 头像地址
    @Property(nameInDb = "headimgurl")
    private String headImgUrl;

    // 个人签名
    @Property(nameInDb = "intro")
    private String intro;

    // 第几届
    @Property(nameInDb = "grade")
    private String grade;

    // 客户端id null?
    @Property(nameInDb = "clientid")
    private String clientId;

    // 消息最后id
    @Property(nameInDb = "lastid")
    private String lastId;
    // 0
    @Property(nameInDb = "integral")
    private String integral;

    // 记录
    @Property(nameInDb = "token")
    private String token;
    // 判断登录状态
    @Property(nameInDb = "state")
    private boolean state;

    @Generated(hash = 48472733)
    public StudentInfo(Long id, String studentNo, String LengthOfSchooling,
            String nickName, String sex, String uId, String phone,
            String headImgUrl, String intro, String grade, String clientId,
            String lastId, String integral, String token, boolean state) {
        this.id = id;
        this.studentNo = studentNo;
        this.LengthOfSchooling = LengthOfSchooling;
        this.nickName = nickName;
        this.sex = sex;
        this.uId = uId;
        this.phone = phone;
        this.headImgUrl = headImgUrl;
        this.intro = intro;
        this.grade = grade;
        this.clientId = clientId;
        this.lastId = lastId;
        this.integral = integral;
        this.token = token;
        this.state = state;
    }

    @Generated(hash = 2016856731)
    public StudentInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentNo() {
        return this.studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getLengthOfSchooling() {
        return this.LengthOfSchooling;
    }

    public void setLengthOfSchooling(String LengthOfSchooling) {
        this.LengthOfSchooling = LengthOfSchooling;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImgUrl() {
        return this.headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIntegral() {
        return this.integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastId() {
        return this.lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
}

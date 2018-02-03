package com.prohua.smurfs.bean;

/**
 * Created by Deep on 2017/4/26 0026.
 */

public class CircleMsgDataBean {

    // 学生id
    private String id;
    // 帖子id
    private String acId;
    // 回复人id
    private String uId;
    // 日期
    private String createDate;
    // 回复正版
    private String text;
    // 图片
    private String images;
    // 你的内容
    private String content;
    // 回复人昵称
    private String nickName;
    // 回复人头像地址
    private String headImgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}

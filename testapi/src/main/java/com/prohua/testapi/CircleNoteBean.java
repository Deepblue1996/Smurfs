package com.prohua.testapi;

import java.util.List;

/**
 * Created by Deep on 2017/4/6 0006.
 */

public class CircleNoteBean {

    // 昵称
    private String nickname;
    // 个性签名
    private String intro;
    // 头像地址
    private String headimgurl;
    // 帖子id
    private String id;
    // 个人圈子id
    private String uid;
    // 未知，默认2
    private String type;
    // 帖子内容
    private String content;
    // 图片地址，分号隔开
    private String images;
    // 物理地址目前为空
    private String address;
    // 发布日期
    private String create_date;
    // 赞列表
    private List<LikesBean> likes;
    // 评论列表
    private List<CommentsBean> comments;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public List<LikesBean> getLikes() {
        return likes;
    }

    public void setLikes(List<LikesBean> likes) {
        this.likes = likes;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }
}

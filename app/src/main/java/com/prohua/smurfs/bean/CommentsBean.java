package com.prohua.smurfs.bean;

/**
 * Created by Deep on 2017/4/6 0006.
 */

public class CommentsBean {

    // 评论id
    private String id;
    // 帖子id
    private String acid;
    // 圈子id
    private String uid;
    // 昵称
    private String nickname;
    // 要回复人昵称
    private String reply;
    // 内容
    private String text;
    // 创建日期
    private String create_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcid() {
        return acid;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}

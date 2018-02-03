package com.prohua.smurfs.model;

import com.prohua.smurfs.bean.LoginUserBean;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class LoginUserModel implements ILoginUserModel {

    private LoginUserBean mLoginUserBean = new LoginUserBean();

    @Override
    public String getUserName() {
        return mLoginUserBean.getUserName();
    }

    @Override
    public String getPassWord() {
        return mLoginUserBean.getPassWord();
    }

    @Override
    public String getCheckWord() {
        return mLoginUserBean.getCheckCode();
    }

    @Override
    public void setUserName(String userName) {
        mLoginUserBean.setUserName(userName);
    }

    @Override
    public void setPassWord(String passWord) {
        mLoginUserBean.setPassWord(passWord);
    }

    @Override
    public void setCheckWord(String checkCode) {
        mLoginUserBean.setCheckCode(checkCode);
    }
}

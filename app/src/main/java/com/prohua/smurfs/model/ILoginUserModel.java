package com.prohua.smurfs.model;

import com.prohua.smurfs.bean.LoginUserBean;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public interface ILoginUserModel {

    String getUserName();

    String getPassWord();

    String getCheckWord();

    void setUserName(String userName);

    void setPassWord(String passWord);

    void setCheckWord(String checkCode);
}

package com.prohua.smurfs.ui.other;

import android.graphics.Bitmap;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public interface ILoginUserView {

    String getUserName();

    String getPassWord();

    String getCheckWord();

    void setUserName(String userName);

    void setPassWord(String passWord);

    void setCheckWord(String passWord);

    void setCheckRelativeLayout(int visibility);

    void setCheckImageViewImageBitmap(Bitmap bitmap);

    void setPop();

    void showAlerter(String msg);

    void setReturnFragmentResult();

    void goneDialog();
}

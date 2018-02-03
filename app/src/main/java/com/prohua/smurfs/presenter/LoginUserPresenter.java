package com.prohua.smurfs.presenter;

import android.view.View;

import com.prohua.smurfs.application.SmurfsApplication;
import com.prohua.smurfs.database.StudentInfo;
import com.prohua.smurfs.database.StudentInfoDao;
import com.prohua.smurfs.model.ILoginUserModel;
import com.prohua.smurfs.model.LoginUserModel;
import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.GetCheckImgNet;
import com.prohua.smurfs.net.LoginDataNet;
import com.prohua.smurfs.net.ShowCheckNet;
import com.prohua.smurfs.ui.other.ILoginUserView;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class LoginUserPresenter {

    private ILoginUserView mLoginUserView;
    private ILoginUserModel mLoginUserModel;

    public LoginUserPresenter(ILoginUserView view) {
        mLoginUserView = view;
        mLoginUserModel = new LoginUserModel();
    }

    public void login() {

        if ("".equals(mLoginUserView.getUserName())) {
            mLoginUserView.goneDialog();
            mLoginUserView.showAlerter("用户名不能为空");
        } else if ("".equals(mLoginUserView.getPassWord())) {
            mLoginUserView.goneDialog();
            mLoginUserView.showAlerter("密码不能为空");
        } else {
            new LoginDataNet(new CallBackInterface() {
                @Override
                public void complete() {
                    if (LoginDataNet.studentInfoOutBean.getStudentInfoBean() != null) {
                        if (studentInfo == null) {
                            studentInfo = new StudentInfo();
                        }
                        studentInfo.setNickName(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getNickName());
                        studentInfo.setIntegral(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getIntegral());
                        studentInfo.setClientId(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getClientId());
                        studentInfo.setGrade(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getGrade());
                        studentInfo.setHeadImgUrl(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getHeadImgUrl());
                        studentInfo.setUId(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getuId());
                        studentInfo.setIntro(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getIntro());
                        studentInfo.setLengthOfSchooling(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getLengthOfSchooling());
                        studentInfo.setPhone(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getPhone());
                        studentInfo.setSex(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getSex());
                        studentInfo.setStudentNo(LoginDataNet.studentInfoOutBean.getStudentInfoBean().getStudentNo());
                        studentInfo.setToken(LoginDataNet.studentInfoOutBean.getMsg());
                        studentInfo.setLastId("0");
                        studentInfo.setState(true);

                        // 载入数据库
                        StudentInfoDao studentInfoDao = SmurfsApplication.getInstances().getDaoSession().getStudentInfoDao();
                        studentInfoDao.deleteAll();
                        studentInfoDao.insert(studentInfo);

                        mLoginUserView.goneDialog();
                        // 视图显示
                        mLoginUserView.showAlerter("登录成功");

                        mLoginUserView.setReturnFragmentResult();

                        mLoginUserView.setPop();
                    } else {

                        mLoginUserView.goneDialog();

                        mLoginUserView.showAlerter(LoginDataNet.studentInfoOutBean.getMsg());
                        // 检测是否显示验证码
                        new ShowCheckNet(new CallBackInterface() {
                            @Override
                            public void complete() {
                                // 200 服务器回应成功
                                // 代码 1 需要验证码
                                if ("1".equals(ShowCheckNet.showCheckOutBean.getCode())) {
                                    // 显示
                                    mLoginUserView.setCheckRelativeLayout(View.VISIBLE);
                                    // 获取验证码
                                    new GetCheckImgNet(new CallBackInterface() {
                                        @Override
                                        public void complete() {
                                            // 设置验证码
                                            mLoginUserView.setCheckImageViewImageBitmap(GetCheckImgNet.bitmap);
                                        }

                                        @Override
                                        public void error() {
                                            mLoginUserView.showAlerter("获取验证码失败");
                                        }
                                    }).pullDown();
                                } else {
                                    // 否则隐藏
                                    mLoginUserView.setCheckRelativeLayout(View.GONE);
                                }
                            }

                            @Override
                            public void error() {
                                mLoginUserView.showAlerter("获取失败");
                            }
                        }).pullDown(mLoginUserView.getUserName());
                    }
                }

                @Override
                public void error() {
                    mLoginUserView.showAlerter("没有网络");
                }
            }).pullDown(mLoginUserView.getUserName(), mLoginUserView.getPassWord(), mLoginUserView.getCheckWord());
        }
    }

    public void checkImgVisibility() {
        // 检测是否显示验证码
        new ShowCheckNet(new CallBackInterface() {
            @Override
            public void complete() {
                // 200 服务器回应成功
                // 代码 1 需要验证码
                if ("1".equals(ShowCheckNet.showCheckOutBean.getCode())) {
                    // 显示
                    mLoginUserView.setCheckRelativeLayout(View.VISIBLE);
                    // 获取验证码
                    new GetCheckImgNet(new CallBackInterface() {
                        @Override
                        public void complete() {
                            // 设置验证码
                            mLoginUserView.setCheckImageViewImageBitmap(GetCheckImgNet.bitmap);
                        }

                        @Override
                        public void error() {
                            mLoginUserView.showAlerter("获取验证码失败");
                        }
                    }).pullDown();
                } else {
                    // 否则隐藏
                    mLoginUserView.setCheckRelativeLayout(View.GONE);
                }
            }

            @Override
            public void error() {
                mLoginUserView.showAlerter("获取失败");
            }
        }).pullDown(mLoginUserView.getUserName());
    }

    public void GetCheckImgNet() {

        new GetCheckImgNet(new CallBackInterface() {
            @Override
            public void complete() {
                mLoginUserView.setCheckImageViewImageBitmap(GetCheckImgNet.bitmap);
            }

            @Override
            public void error() {
                mLoginUserView.showAlerter("获取验证码失败");
            }
        }).pullDown();

    }

    public void saveUser(String userName, String passWord, String checkCode) {
        mLoginUserModel.setUserName(userName);
        mLoginUserModel.setPassWord(passWord);
        mLoginUserModel.setCheckWord(checkCode);
    }

}

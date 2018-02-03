package com.prohua.smurfs.ui.other;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.prohua.smurfs.R;
import com.prohua.smurfs.presenter.LoginUserPresenter;
import com.prohua.smurfs.ui.main.FourthTabFragment;
import com.prohua.smurfs.ui.widgt.SweetAlertDialog;
import com.prohua.smurfs.util.UtilTools;
import com.tapadoo.alerter.Alerter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * LoginFragment 登录界面 MVP设计
 * Created by Deep on 2017/4/20 0020.
 */

public class LoginFragment extends SupportFragment implements ILoginUserView {

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.back)
    ImageView backImageView;
    @BindView(R.id.e_user)
    EditText userEdit;
    @BindView(R.id.e_pwd)
    EditText pwdEdit;

    @BindView(R.id.relCheck)
    RelativeLayout checkRelativeLayout;
    @BindView(R.id.checkbox)
    EditText checkEdit;
    @BindView(R.id.checkImg)
    ImageView checkImageView;

    private LoginUserPresenter mLoginUserPresenter = new LoginUserPresenter(this);

    private SweetAlertDialog dialog;
    /**
     * 懒加载
     */
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * 创建新的Fragment视图
     *
     * @param inflater           加载布局
     * @param container          view组
     * @param savedInstanceState 通讯（HashMap）
     * @return 视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_layout_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        backImageView.setOnClickListener(view1 -> setPop());

        // 失去焦点
        userEdit.setOnFocusChangeListener((view12, b) -> {
            // !b 失去焦点响应
            if (!b)
                if(!"".equals(userEdit.getText().toString()))
                    mLoginUserPresenter.checkImgVisibility();
        });

        // 点击更换验证码
        checkImageView.setOnClickListener(view13 -> mLoginUserPresenter.GetCheckImgNet());

        return view;
    }

    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);
    }

    @OnClick(R.id.login_bt)   //给 button1 设置一个点击事件
    public void loginClick() {
        dialog = new SweetAlertDialog(getActivity());
        dialog.setTitleText("请稍候...");
        dialog.show();
        mLoginUserPresenter.login();
        mLoginUserPresenter.saveUser(userEdit.getText().toString(), pwdEdit.getText().toString(), checkEdit.getText().toString());
    }

    @Override
    public void goneDialog() {
        _mActivity.runOnUiThread(() -> dialog.cancel());
    }

    @Override
    public String getUserName() {
        return userEdit.getText().toString();
    }

    @Override
    public String getPassWord() {
        return pwdEdit.getText().toString();
    }

    @Override
    public String getCheckWord() {
        return checkEdit.getText().toString();
    }

    @Override
    public void setUserName(String userName) {
        _mActivity.runOnUiThread(() -> userEdit.setText(userName));
    }

    @Override
    public void setPassWord(String passWord) {
        _mActivity.runOnUiThread(() -> pwdEdit.setText(passWord));
    }

    @Override
    public void setCheckWord(String passWord) {
        _mActivity.runOnUiThread(() -> checkEdit.setText(passWord));
    }

    @Override
    public void setCheckRelativeLayout(int visibility) {
        _mActivity.runOnUiThread(() -> checkRelativeLayout.setVisibility(visibility));
    }

    @Override
    public void setCheckImageViewImageBitmap(Bitmap bitmap) {
        _mActivity.runOnUiThread(() -> checkImageView.setImageBitmap(bitmap));
    }

    @Override
    public void setPop() {
        _mActivity.runOnUiThread(() -> {
            // 隐藏键盘
            hideSoftInput();
            // 回退
            pop();
        });
    }

    @Override
    public void showAlerter(String msg) {
        _mActivity.runOnUiThread(() -> Alerter.create(getActivity())
                .setText(msg)
                .showIcon(false)
                .setBackgroundColor(R.color.showbg)
                .show());
    }

    @Override
    public void setReturnFragmentResult() {
        // 返回数据给主fragment
        Bundle bundle = new Bundle();
        bundle.putString(FourthTabFragment.KEY_RESULT_NICKNAME, studentInfo.getNickName());
        setFragmentResult(RESULT_OK, bundle);
    }
}

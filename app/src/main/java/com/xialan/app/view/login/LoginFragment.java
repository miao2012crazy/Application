package com.xialan.app.view.login;

import android.widget.EditText;

import com.xialan.app.R;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.LoginContract;
import com.xialan.app.presenter.LoginPresenter;
import com.xialan.app.utils.CheckLoginUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.StringUtil;
import com.xialan.app.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/26.
 */
public class LoginFragment extends BaseFragment implements LoginContract.View {

    @BindView(R.id.et_user_name_1)
    EditText etUserName1;
    @BindView(R.id.et_user_psd_1)
    EditText etUserPsd1;
    private String user_tel;
    private String user_pwd;
    private LoginPresenter loginPresenter;

    @Override
    protected int getContentId() {
        return R.layout.login;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        loginPresenter = new LoginPresenter(this);
        return loginPresenter;
    }


    @OnClick(R.id.btn_login_act)
    public void onViewClicked() {
        user_tel = etUserName1.getText().toString();
        user_pwd = etUserPsd1.getText().toString();
        if (StringUtil.isEmpty(user_tel)) {
            UIUtils.showToast(getActivity(), "用户名不能为空");
            return;
        }
        if (StringUtil.isEmpty(user_pwd)) {
            UIUtils.showToast(getActivity(), "密码不能为空");
            return;
        }
        loginPresenter.setLoginInfo(user_tel,user_pwd);

    }

    @Override
    public void loginSuccessed(String data) {
        String[] dataForspild = CommonUtil.getDataForspild(data);
        CheckLoginUtil.parseLogin(dataForspild,user_tel);
    }

    @Override
    public void loginFailed() {
        UIUtils.showToast(getActivity(),"网络连接失败");
    }
}

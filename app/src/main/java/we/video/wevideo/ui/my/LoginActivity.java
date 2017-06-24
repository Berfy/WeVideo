package we.video.wevideo.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.bean.UserInfo;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.util.CheckUtil;
import we.video.wevideo.util.ShareUtil;

/**
 * Created by Berfy on 2016/4/13.
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        shareUtil = new ShareUtil(mContext);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_findpwd).setOnClickListener(this);
        findViewById(R.id.btn_reg).setOnClickListener(this);
        findViewById(R.id.btn_weixin).setOnClickListener(this);
        findViewById(R.id.btn_weibo).setOnClickListener(this);
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String user = ((EditText) findViewById(R.id.edt_mobile)).getText().toString().trim();
                String pwd = ((EditText) findViewById(R.id.edt_pwd)).getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    showToast(getString(R.string.tip_mobile_null));
                } else if (!CheckUtil.isMobile(user)) {
                    showToast(getString(R.string.tip_mobile_format_error));
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.tip_pwd_null));
                } else {
                    ServerApi.getInstances().login(user, pwd
                            , new RequestCallBack<UserInfo>() {
                                @Override
                                public void start() {
                                    showLoading();
                                }

                                @Override
                                public void onProgress(float percent) {

                                }

                                @Override
                                public void finish(NetResponse<UserInfo> result) {
                                    dismissLoading();
                                    if (result.netMsg.code == 1) {
                                        UserTemp.getInstances(mContext).setUserId(result.content.getUserId());
                                        UserTemp.getInstances(mContext).setIsLogin(true);
                                        UserTemp.getInstances(mContext).setName(result.content.getName());
                                        UserTemp.getInstances(mContext).setImg(result.content.getImg());
                                        showToast(getString(R.string.tip_login_suc));
                                        LoginActivity.this.finish();
                                    }
                                }
                            });
                }
                break;
            case R.id.tv_findpwd:
                startActivity(new Intent(mContext, FindPwdActivity.class));
                break;
            case R.id.btn_reg:
                startActivity(new Intent(mContext, RegActivity.class));
                break;
            case R.id.btn_weixin:
                shareUtil.wx_login();
                break;
            case R.id.btn_weibo:
                shareUtil.sina_login();
                break;
        }
    }
}

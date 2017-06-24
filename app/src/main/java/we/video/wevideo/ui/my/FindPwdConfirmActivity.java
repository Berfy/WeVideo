package we.video.wevideo.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import we.video.wevideo.ActivityManager;
import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;

/**
 * Created by Berfy on 2016/4/13.
 * 注册
 */
public class FindPwdConfirmActivity extends BaseActivity {

    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_findpwd_newpwd);
    }

    @Override
    protected void initView() {
        mobile = getIntent().getStringExtra("mobile");
        findViewById(R.id.btn_ok).setOnClickListener(this);
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String pwd = ((EditText) findViewById(R.id.edt_pwd)).getText().toString().trim();
                String edt_confirm_pwd = ((EditText) findViewById(R.id.edt_confirm_pwd)).getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.tip_newpwd_null));
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.tip_confirm_pwd_null));
                } else if (!pwd.equals(edt_confirm_pwd)) {
                    showToast(getString(R.string.tip_pwd_not_same));
                } else {
                    ServerApi.getInstances().findPwd(mobile, pwd
                            , new RequestCallBack<String>() {
                                @Override
                                public void start() {
                                    showLoading();
                                }

                                @Override
                                public void onProgress(float percent) {

                                }

                                @Override
                                public void finish(NetResponse<String> result) {
                                    dismissLoading();
                                    if (result.netMsg.code == 1) {
                                        showToast(getString(R.string.tip_modify_pwd_suc));
                                        FindPwdConfirmActivity.this.finish();
                                        ActivityManager.getInstance().popActivity(FindPwdActivity.class);
                                        startActivity(new Intent(mContext, LoginActivity.class));
                                    }
                                }
                            });
                }
                break;
        }
    }
}

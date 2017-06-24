package we.video.wevideo.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.bean.VerifyCode;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.util.CheckUtil;

/**
 * Created by Berfy on 2016/4/13.
 * 注册
 */
public class RegActivity extends BaseActivity {

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_reg);
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_reg).setOnClickListener(this);
        findViewById(R.id.btn_code).setOnClickListener(this);
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                getCode();
                break;
            case R.id.btn_reg:
                reg();
                break;
        }
    }

    private void getCode() {
        String mobile = ((EditText) findViewById(R.id.edt_mobile)).getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showToast(getString(R.string.tip_mobile_null));
        } else {
            ServerApi.getInstances().getCode(mobile, 0, new RequestCallBack<VerifyCode>() {
                @Override
                public void start() {
                    showLoading();
                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<VerifyCode> result) {
                    dismissLoading();
                    if (result.netMsg.code == 1) {
                        code = result.content.getCode();
                    }
                }
            });
        }
    }

    private void reg() {
        String mobile = ((EditText) findViewById(R.id.edt_mobile)).getText().toString().trim();
        String edt_code = ((EditText) findViewById(R.id.edt_code)).getText().toString().trim();
        String pwd = ((EditText) findViewById(R.id.edt_pwd)).getText().toString().trim();
        String confirm_pwd = ((EditText) findViewById(R.id.edt_confirm_pwd)).getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showToast(getString(R.string.tip_mobile_null));
        } else if (!CheckUtil.isMobile(mobile)) {
            showToast(getString(R.string.tip_mobile_format_error));
        } else if (TextUtils.isEmpty(edt_code)) {
            showToast(getString(R.string.tip_code_null));
        } else if (!code.equals(edt_code)) {
            showToast(getString(R.string.tip_code_err));
        } else if (TextUtils.isEmpty(pwd)) {
            showToast(getString(R.string.tip_pwd_null));
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            showToast(getString(R.string.tip_confirm_pwd_null));
        } else if (!confirm_pwd.equals(pwd)) {
            showToast(getString(R.string.tip_pwd_not_same));
        } else {
            ServerApi.getInstances().reg(mobile, pwd
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
                                showToast(getString(R.string.tip_reg_suc));
                                RegActivity.this.finish();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        }
                    });
        }
    }
}

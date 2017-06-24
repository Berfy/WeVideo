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
public class FindPwdActivity extends BaseActivity {

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_findpwd);
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_code).setOnClickListener(this);
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                getCode();
                break;
            case R.id.btn_next:
                next();
                break;
        }
    }

    private void getCode() {
        String mobile = ((EditText) findViewById(R.id.edt_mobile)).getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showToast(getString(R.string.tip_mobile_null));
        } else {
            ServerApi.getInstances().getCode(mobile, 1, new RequestCallBack<VerifyCode>() {
                @Override
                public void start() {
                    showLoading();
                }

                @Override
                public void finish(NetResponse<VerifyCode> result) {
                    dismissLoading();
                    if (result.netMsg.code == 1) {
                        code = result.content.getCode();
                    }
                }

                @Override
                public void onProgress(float percent) {

                }
            });
        }
    }

    private void next() {
        String user = ((EditText) findViewById(R.id.edt_mobile)).getText().toString().trim();
        String edt_code = ((EditText) findViewById(R.id.edt_code)).getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            showToast(getString(R.string.tip_mobile_null));
        } else if (!CheckUtil.isMobile(user)) {
            showToast(getString(R.string.tip_mobile_format_error));
        } else if (TextUtils.isEmpty(edt_code)) {
            showToast(getString(R.string.tip_code_null));
        } else if (!code.equals(edt_code)) {
            showToast(getString(R.string.tip_code_err));
        } else {
            Intent intent = new Intent(mContext, FindPwdConfirmActivity.class);
            intent.putExtra("mobile", user);
            startActivity(intent);
        }
    }
}

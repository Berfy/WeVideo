package we.video.wevideo.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.util.ToastUtil;

/**
 * Created by Berfy on 2016/6/29.
 */
public class EditNameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_edit_name);
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_save).setOnClickListener(this);
        if(!TextUtils.isEmpty(UserTemp.getInstances(mContext).getName())){//设置原来的昵称，光标位置设置到最后
            ((EditText) findViewById(R.id.edt_name)).setText(UserTemp.getInstances(mContext).getName());
            ((EditText) findViewById(R.id.edt_name)).setSelection(UserTemp.getInstances(mContext).getName().length());
        }
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                final String name = ((EditText) findViewById(R.id.edt_name)).getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.getInstance().showToast(getString(R.string.tip_nick));
                } else {
                    ServerApi.getInstances().updateUserInfo(mContext, name, new RequestCallBack<String>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void finish(NetResponse<String> result) {
                            if (result.netMsg.code == 1) {
                                UserTemp.getInstances(mContext).setName(name);
                                EditNameActivity.this.finish();
                            }
                        }

                        @Override
                        public void onProgress(float percent) {

                        }
                    });
                }
                break;
        }
    }
}

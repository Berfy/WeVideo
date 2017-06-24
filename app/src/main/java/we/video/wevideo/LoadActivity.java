package we.video.wevideo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by Berfy on 2016/3/2.
 * 引导页
 */
public class LoadActivity extends BaseActivityNoTitle {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
    }

    @Override
    protected void initView() {
        handler.sendEmptyMessageDelayed(1, 2000);
        findViewById(R.id.tv_jump).setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
            startActivity(new Intent(mContext, MainActivity.class));
            overridePendingTransition(R.anim.load_show, R.anim.load_dismiss);
        }
    };

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                handler.removeMessages(1);
                finish();
                startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
    }
}

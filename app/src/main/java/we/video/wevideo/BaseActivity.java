package we.video.wevideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import we.video.wevideo.ui.support.NewView1;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.ToastUtil;

/**
 * Created by Berfy on 2016/3/1.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity parent = getParent();
        if (null != parent) {
            mContext = parent;
        } else {
            mContext = this;
        }
        ActivityManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_base);
        findViewById(R.id.layout_left).setOnClickListener(this);
        setTopTitle(getTitle() + "");
        setLeftTitle(getIntent().getStringExtra("system_title"));
    }

    protected void setContent(int layoutId) {
        ((LinearLayout) findViewById(R.id.layout)).addView(View.inflate(mContext, layoutId, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
    }

    protected void setContent(View view) {
        ((LinearLayout) findViewById(R.id.layout)).addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
    }

    protected void showLoading() {
        mHandler.removeMessages(0);
        findViewById(R.id.layout_loading).setBackgroundColor(getResources().getColor(R.color.translate_black2));
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        ((NewView1) findViewById(R.id.loading)).startAnim();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            findViewById(R.id.layout_loading).setBackgroundColor(getResources().getColor(R.color.translate));
            findViewById(R.id.loading).setVisibility(View.GONE);
            ((NewView1) findViewById(R.id.loading)).stopAnim();
        }
    };

    protected void dismissLoading() {
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void setTopTitle(String title) {
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }


    public void setTopTitle(int textResId) {
        ((TextView) findViewById(R.id.tv_title)).setText(textResId);
    }

    public void setLeftTitle(String title) {
        ((TextView) findViewById(R.id.tv_left)).setText(title);
    }

    public String getLeftTitle() {
        return ((TextView) findViewById(R.id.tv_left)).getText().toString().trim();
    }

    public void setLeftTitle(int textResId) {
        ((TextView) findViewById(R.id.tv_left)).setText(textResId);
    }

    public void showLeft(boolean isVisible) {
        findViewById(R.id.layout_left).setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    abstract protected void initView();

    abstract protected void doClickEvent(View v);

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                finish();
                break;
        }
        doClickEvent(view);
    }

    public void showToast(String msg) {
        ToastUtil.getInstance().showToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceUtil.closeKeyboard(mContext, getWindow().getDecorView().getWindowToken());
        ActivityManager.getInstance().popActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra("system_title", getTitle() + "");
        DeviceUtil.closeKeyboard(mContext, getWindow().getDecorView().getWindowToken());
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("system_title", getTitle() + "");
        DeviceUtil.closeKeyboard(mContext, getWindow().getDecorView().getWindowToken());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void finish() {
        super.finish();
        DeviceUtil.closeKeyboard(mContext, getWindow().getDecorView().getWindowToken());
    }
}

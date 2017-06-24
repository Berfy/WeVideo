package we.video.wevideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import we.video.wevideo.ui.support.lib.app.SlidingFragmentActivity;

/**
 * Created by Berfy on 2016/3/31.
 */
public abstract class BaseFragmentActivity extends SlidingFragmentActivity implements View.OnClickListener {

    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity parent = getParent();
        if (null != parent) {
            mContext = parent;
        } else {
            mContext = this;
        }
        ActivityManager.getInstance().pushActivity(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
    }

    @Override
    public void startActivity(Intent intent) {
        if (!getClass().equals(LoadActivity.class)) {
            intent.putExtra("system_title", getTitle() + "");
        }
        super.startActivity(intent);
    }

    abstract protected void initView();

    abstract protected void doClickEvent(View v);

    @Override
    public void onClick(View view) {
        doClickEvent(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().popActivity(this);
    }
}

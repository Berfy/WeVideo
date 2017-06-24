package we.video.wevideo.ui.special;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.ui.support.NewView;
import we.video.wevideo.ui.support.NewView1;

/**
 * Created by Berfy on 2016/7/21.
 */
public class NewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_newview);
        ((LinearLayout) findViewById(R.id.layout1)).addView(new NewView(mContext), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout) findViewById(R.id.layout2)).addView(new NewView1(mContext), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doClickEvent(View v) {

    }
}

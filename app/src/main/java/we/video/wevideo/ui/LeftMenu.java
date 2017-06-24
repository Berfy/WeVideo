package we.video.wevideo.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.ui.support.round.RoundImageView;
import we.video.wevideo.ui.support.lib.SlidingMenu;
import we.video.wevideo.util.ImageUtil;

/**
 * Created by Berfy on 2016/4/7.
 */
public class LeftMenu extends BaseView {

    private SlidingMenu slidingMenu;
    private OnSelectListener onSelectListener;

    private static final int MY = 0;
    private static final int ALL = 1;
    private static final int SET = 2;
    private int MENU = 1;

    public LeftMenu(Context context) {
        super(context);
        setContent(R.layout.view_menu);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void updateData() {
        if (UserTemp.getInstances(mContext).isLogin()) {
            ImageUtil.getInstances().displayDefault((RoundImageView) findViewById(R.id.iv_icon), UserTemp.getInstances(mContext).getImg());
            if (TextUtils.isEmpty(UserTemp.getInstances(mContext).getName())) {
                ((TextView) findViewById(R.id.tv_name)).setText(mContext.getString(R.string.noset));
            } else {
                ((TextView) findViewById(R.id.tv_name)).setText(UserTemp.getInstances(mContext).getName());
            }
        } else {
            ((TextView) findViewById(R.id.tv_name)).setText(mContext.getString(R.string.nologin));
        }
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_all).setOnClickListener(this);
        findViewById(R.id.btn_set).setOnClickListener(this);
        findViewById(R.id.layout_icon).setOnClickListener(this);
        updateMenu(MENU);
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                updateMenu(ALL);
                break;
            case R.id.btn_set:
                updateMenu(SET);
                break;
            case R.id.layout_icon:
                updateMenu(MY);
                break;
        }
    }

    public void updateMenu(int menu) {
        findViewById(R.id.btn_all).setBackgroundResource(R.mipmap.bg_leftmenu_all);
        findViewById(R.id.btn_set).setBackgroundResource(R.mipmap.bg_leftmenu_set);
        findViewById(R.id.layout_icon).setBackgroundColor(0);
        switch (menu) {
            case MY:
                if (null != onSelectListener)
                    onSelectListener.select(MY);
                findViewById(R.id.layout_icon).setBackgroundResource(R.drawable.bg_shape_left_menu_bg);
                break;
            case SET:
                if (null != onSelectListener)
                    onSelectListener.select(SET);
                findViewById(R.id.btn_set).setBackgroundResource(R.mipmap.bg_leftmenu_set_press);
                break;
            case ALL:
                if (null != onSelectListener)
                    onSelectListener.select(ALL);
                findViewById(R.id.btn_all).setBackgroundResource(R.mipmap.bg_leftmenu_all_press);
                break;
        }
        MENU = menu;
    }

    public interface OnSelectListener {
        void select(int which);
    }
}

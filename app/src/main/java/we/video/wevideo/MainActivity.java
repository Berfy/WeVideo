package we.video.wevideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.IndexView;
import we.video.wevideo.ui.LeftMenu;
import we.video.wevideo.ui.MyView;
import we.video.wevideo.ui.SetView;
import we.video.wevideo.ui.support.lib.SlidingMenu;
import we.video.wevideo.ui.my.CollectActivity;
import we.video.wevideo.ui.special.NewActivity;
import we.video.wevideo.util.CameraUtil;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.LogUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.ToastUtil;
import we.video.wevideo.util.ViewUtil;

/**
 * Created by Berfy on 2016/4/5.
 * 首页框架
 */
public class MainActivity extends BaseFragmentActivity implements LeftMenu.OnSelectListener {

    private LeftMenu leftMenu;
    private ViewUtil viewUtil;
    private CameraUtil cameraUtil;
    private PopupWindowUtil popupWindowUtil;

    private IndexView indexView;
    private MyView myView;
    private SetView setView;

    private static final int MY = 0;
    private static final int SPECIAL = 1;
    private static final int SET = 2;
    private int PAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setBehindWidth(DeviceUtil.dip2px(mContext, 120));
        leftMenu = new LeftMenu(mContext);
        leftMenu.setOnSelectListener(this);
        leftMenu.getView().setLayoutParams(new LinearLayout.LayoutParams(DeviceUtil.dip2px(mContext, 80), ViewGroup.LayoutParams.MATCH_PARENT));
        setBehindContentView(leftMenu.getView());
    }

    private byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    @Override
    protected void initView() {
        popupWindowUtil = new PopupWindowUtil(mContext);
        cameraUtil = new CameraUtil(mContext);
        cameraUtil.setConfig(true, 1, 1, 200, 200);
        cameraUtil.setOnPhotoListener(new CameraUtil.OnPhotoListener() {
            @Override
            public void getPhotoPath(String filePath) {
                LogUtil.e("文件大小", new File(filePath).length() + "");
                ServerApi.getInstances().uploadImg(mContext, new File(filePath), new RequestCallBack<String>() {
                    @Override
                    public void start() {
                    }

                    @Override
                    public void finish(NetResponse<String> result) {
                        if (result.netMsg.code == 1) {
                            if (PAGE == MY) {//我的页面需要判断刷新
                                setView(MY);
                            }
                            UserTemp.getInstances(mContext).setImg(result.content);
                            leftMenu.updateData();
                        }
                    }

                    @Override
                    public void onProgress(float percent) {

                    }
                });
            }

            @Override
            public void getPhotoCropData(byte[] data) {

            }
        });
        findViewById(R.id.layout_left).setOnClickListener(this);
        findViewById(R.id.layout_right).setOnClickListener(this);
        findViewById(R.id.iv_logo).setOnClickListener(this);
        viewUtil = new ViewUtil();
        LogUtil.e("走几次", "");
        setView(SPECIAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PAGE == MY) {//我的页面需要判断刷新
            setView(MY);
        }
        leftMenu.updateData();
    }

    private void setView(int which) {
        findViewById(R.id.tv_title).setBackgroundResource(0);
        ((TextView) findViewById(R.id.tv_title)).setText("");
        setTitle("");
        switch (which) {
            case SPECIAL:
                LogUtil.e("专辑", "");
                findViewById(R.id.tv_title).setVisibility(View.GONE);
                findViewById(R.id.iv_logo).setVisibility(View.VISIBLE);
                setTitle(getString(R.string.index));
                if (null == indexView) {
                    indexView = new IndexView(mContext);
                    indexView
                            .setSlidingMenu(getSlidingMenu());
                    indexView.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                viewUtil.updateView(mContext, indexView.getView(), (LinearLayout) findViewById(R.id.layout_content));
                break;
            case MY:
                LogUtil.e("我的", "");
                findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_logo).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tv_title)).setText("我的");
                setTitle("我的");
                if (null == myView) {
                    myView = new MyView(mContext);
                    myView.setOnLogoutListener(new MyView.OnLogoutListener() {
                        @Override
                        public void logout() {
                            leftMenu.updateData();
                            popupWindowUtil.showChoosePop(getWindow().getDecorView(), mContext.getString(R.string.tip_logout), mContext.getString(R.string.ok), mContext.getString(R.string.cancel), new PopupWindowUtil.OnPopListener() {
                                @Override
                                public void ok() {
                                    UserTemp.getInstances(mContext).setUserId("");
                                    UserTemp.getInstances(mContext).setIsLogin(false);
                                    myView.setData();
                                }

                                @Override
                                public void cancel() {

                                }
                            });
                        }
                    });
                    myView.setOnSelectPhotoListener(new MyView.OnSelectPhotoListener() {
                        @Override
                        public void toSelectPhoto() {
                            popupWindowUtil.showPhotoPop(getWindow().getDecorView(), new PopupWindowUtil.OnPopPhotoListener() {
                                @Override
                                public void photo() {
                                    cameraUtil.pickPhoto();
                                }

                                @Override
                                public void camera() {
                                    cameraUtil.tackPhoto();
                                }
                            });
                        }
                    });
                    myView.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                myView.setData();
                viewUtil.updateView(mContext, myView.getView(), (LinearLayout) findViewById(R.id.layout_content));
                break;
            case SET:
                LogUtil.e("设置", "");
                findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_logo).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tv_title)).setText("设置");
                setTitle("设置");
                if (null == setView) {
                    setView = new SetView(mContext);
                    setView.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                setView.getView().post(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setView.updateData();
                            }
                        });
                    }
                });
                viewUtil.updateView(mContext, setView.getView(), (LinearLayout) findViewById(R.id.layout_content));
                break;
        }
        PAGE = which;
        getSlidingMenu().showContent();
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.layout_left:
                getSlidingMenu().showMenu();
                break;
            case R.id.layout_right:
                if (UserTemp.getInstances(mContext).isLogin()) {
                    startActivity(new Intent(mContext, CollectActivity.class));
                } else {
                    DeviceUtil.toLogin(mContext, true);
                }
                break;
            case R.id.iv_logo:
                startActivity(new Intent(mContext, NewActivity.class));
                break;
        }
    }

    @Override
    public void select(int which) {
        setView(which);
    }

    private long clickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSlidingMenu().isMenuShowing()) {
                getSlidingMenu().showContent();
            } else if (PAGE != SPECIAL) {
                setView(SPECIAL);
                leftMenu.updateMenu(SPECIAL);
            } else {
                if (System.currentTimeMillis() - clickTime < 1500) {
                    LogUtil.e("退出", "");
                    return super.onKeyDown(keyCode, event);
                } else {
                    clickTime = System.currentTimeMillis();
                    LogUtil.e("点击退出", "");
                    ToastUtil.getInstance().showToast("再次点击退出");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

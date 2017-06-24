package we.video.wevideo.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.bean.UserInfo;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.my.CollectActivity;
import we.video.wevideo.ui.my.MyCommentActivity;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.ImageUtil;

/**
 * Created by Berfy on 2016/4/7.
 * 我的
 */
public class MyView extends BaseView {

    private OnLogoutListener onLogoutListener;
    private OnSelectPhotoListener onSelectePhotoListener;

    public MyView(Context context) {
        super(context);
        setContent(R.layout.view_my);
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_icon).setOnClickListener(this);
        findViewById(R.id.tv_name).setOnClickListener(this);
        findViewById(R.id.layout_follow).setOnClickListener(this);
        findViewById(R.id.layout_comment).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        if (TextUtils.isEmpty(UserTemp.getInstances(mContext).getName())) {
            ((TextView) findViewById(R.id.tv_name)).setText(mContext.getString(R.string.noset));
        } else {
            ((TextView) findViewById(R.id.tv_name)).setText(UserTemp.getInstances(mContext).getName());
        }
    }

    public void setData() {
        getData();
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    public void setOnSelectPhotoListener(OnSelectPhotoListener onSelectePhotoListener) {
        this.onSelectePhotoListener = onSelectePhotoListener;
    }

    private void getData() {
        if (UserTemp.getInstances(mContext).isLogin()) {
            findViewById(R.id.layout_follow).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);
            //登录可获取资料
            ((Button) findViewById(R.id.btn_logout)).setText(mContext.getString(R.string.my_logout));
            ServerApi.getInstances().getUserInfo(UserTemp.getInstances(mContext).getUserId(), new RequestCallBack<UserInfo>() {
                @Override
                public void start() {
                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<UserInfo> result) {
                    if (result.netMsg.code == 1) {
                        if (TextUtils.isEmpty(result.content.getName())) {
                            ((TextView) findViewById(R.id.tv_name)).setText(mContext.getString(R.string.noset));
                        } else {
                            ((TextView) findViewById(R.id.tv_name)).setText(result.content.getName());
                        }
                        UserTemp.getInstances(mContext).setName(result.content.getName());
                        UserTemp.getInstances(mContext).setImg(result.content.getImg());
                        ImageUtil.getInstances().displayDefault((ImageView) findViewById(R.id.iv_icon), result.content.getImg());
                    }
                }
            });
        } else {
            findViewById(R.id.layout_follow).setVisibility(View.GONE);
            findViewById(R.id.layout_comment).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_name)).setText(mContext.getString(R.string.nologin));
            ((Button) findViewById(R.id.btn_logout)).setText(mContext.getString(R.string.login));
        }
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                if (null != onSelectePhotoListener) {
                    onSelectePhotoListener.toSelectPhoto();
                }
                break;
            case R.id.tv_name:
                startActivity(new Intent(mContext, EditNameActivity.class));
                break;
            case R.id.layout_follow:
                if (UserTemp.getInstances(mContext).isLogin()) {
                    startActivity(new Intent(mContext, CollectActivity.class));
                } else {
                    DeviceUtil.toLogin(mContext, true);
                }
                break;
            case R.id.layout_comment:
                if (UserTemp.getInstances(mContext).isLogin()) {
                    startActivity(new Intent(mContext, MyCommentActivity.class));
                } else {
                    DeviceUtil.toLogin(mContext, true);
                }
                break;
            case R.id.btn_logout:
                if (UserTemp.getInstances(mContext).isLogin()) {
                    onLogoutListener.logout();
                } else {
                    DeviceUtil.toLogin(mContext, false);
                }
                break;
        }
    }

    public interface OnLogoutListener {
        void logout();
    }

    public interface OnSelectPhotoListener {
        void toSelectPhoto();
    }
}

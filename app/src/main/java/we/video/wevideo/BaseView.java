package we.video.wevideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.ToastUtil;

/**
 * Created by Berfy on 2016/3/1.
 */
public abstract class BaseView implements View.OnClickListener {

    protected Context mContext;
    private View mView;
    private PopupWindowUtil popupWindowUtil;

    public BaseView(Context context) {
        mContext = context;
    }

    protected void setContent(int layoutId) {
        mView = View.inflate(mContext, layoutId, null);
        initView();
        popupWindowUtil = new PopupWindowUtil(mContext);
    }

    public View getView() {
        return mView;
    }

    protected void showLoading() {
        popupWindowUtil.showLoadingPop(mView);
    }

    protected void dismissLoading() {
        popupWindowUtil.dismiss();
    }

    protected View findViewById(int id) {
        return mView.findViewById(id);
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

    public void startActivity(Intent intent) {
        intent.putExtra("system_title", ((Activity) mContext).getTitle());
        DeviceUtil.closeKeyboard(mContext, mView.getWindowToken());
        ((Activity) mContext).startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("system_title", ((Activity) mContext).getTitle());
        DeviceUtil.closeKeyboard(mContext, mView.getWindowToken());
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }

    public void finish() {
        ((Activity) mContext).finish();
        DeviceUtil.closeKeyboard(mContext, mView.getWindowToken());
    }
}

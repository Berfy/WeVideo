package we.video.wevideo.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import we.video.wevideo.R;

/**
 * Toast
 *
 * @author Berfy
 */
public class ToastUtil {

    private static ToastUtil instance = null;
    private Toast toast;
    private View view;
    private Context mContext;

    public static ToastUtil getInstance() {
        if (instance == null) {
            throw new NullPointerException("未初始化ToastUtil，请在第一个启动Activity中调用ToastUtil.init(Context context)方法");
        }
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new ToastUtil(context);
        }
    }

    public ToastUtil(Context context) {
        mContext = context;
    }

    /**
     * toast string消息,时间2秒
     *
     * @param msg
     */
    public void showToast(String msg) {
        init();
        ((TextView) view.findViewById(R.id.tv_toast)).setText(msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * toast string消息,时间2秒
     *
     * @param stringId
     */
    public void showToast(int stringId) {
        init();
        ((TextView) view.findViewById(R.id.tv_toast)).setText(mContext.getString(stringId));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * @param message
     * @param time
     * @see
     */
    public void show(String message, int time) {
        init();
        ((TextView) view.findViewById(R.id.tv_toast)).setText(message);
        toast.setDuration(time);
        toast.show();
    }

    /**
     * @param messageResId
     * @param time
     * @see
     */
    public void show(int messageResId, int time) {
        init();
        String message = mContext.getResources().getString(messageResId);
        ((TextView) view.findViewById(R.id.tv_toast)).setText(message);
        toast.setDuration(time);
        toast.show();
    }

    private void init() {
        if (null == toast) {
            toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
            view = View.inflate(mContext, R.layout.view_toast, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(DeviceUtil.getScreenWidth(mContext), LinearLayout.LayoutParams.MATCH_PARENT));
            toast.setView(view);
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        }
    }

    public void setGravity(Context ctx, int gravity) {
        toast.setGravity(gravity, 0, 0);
    }
}

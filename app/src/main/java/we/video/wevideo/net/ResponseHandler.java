package we.video.wevideo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import we.video.wevideo.R;
import we.video.wevideo.util.LogUtil;
import we.video.wevideo.util.ToastUtil;

public abstract class ResponseHandler<T> {

    private Context mContext;
    private RequestCallBack<T> mCallBack;
    private long mStartTime;
    /**
     * 是否显示来自服务器的信息
     */
    protected boolean mShowErrorMsgFromServer = true;
    protected boolean mShowSucMsgFromServer = false;

    public ResponseHandler(Context context) {
        mContext = context;
    }

    public ResponseHandler(Context context, RequestCallBack<T> callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    /**
     * @param showErrorMsgFromServer 是否显示错误Toast提示
     * @param showSucMsgFromServer   是否显示成功Toast提示
     */
    public ResponseHandler(Context context, RequestCallBack<T> callBack,
                           boolean showErrorMsgFromServer, boolean showSucMsgFromServer) {
        mContext = context;
        mCallBack = callBack;
        mShowErrorMsgFromServer = showErrorMsgFromServer;
        mShowSucMsgFromServer = showSucMsgFromServer;
    }

    public void onProgress(int bytesWritten, int totalSize) {
        if (mCallBack != null && mCallBack instanceof RequestCallBakProgress) {
            ((RequestCallBakProgress<T>) mCallBack).onProgress(bytesWritten,
                    totalSize);
        }
    }

    ;

    public void start() {
        mCallBack.start();
    }

    public void finish(String content) {
        NetResponse<T> result;
        if (null == content) {
            result = new NetResponse<T>();
            mCallBack.finish(result);
            onDataReturn(result);
        } else {
            LogUtil.e("onSuccess():", " 标记 " + mStartTime + " 网络请求耗时："
                    + (System.currentTimeMillis() - mStartTime) + "ms"
                    + "  content:" + content);
            if (mCallBack != null) {
                long DecodeStartTime = System.currentTimeMillis();
                result = getResponse(content);
                LogUtil.d("onSuccess():", " 标记 " + mStartTime + "数据解析耗时："
                        + (System.currentTimeMillis() - DecodeStartTime) + "ms");
                onDataReturn(result);
                this.mCallBack.finish(result);
            }
        }
    }

    public void error(VolleyError error) {
        NetResponse<T> result = new NetResponse<T>();
        NetMessage netMessage = new NetMessage();
        result.netMsg = netMessage;
        LogUtil.e("onFailure():",
                " 标记 " + mStartTime + "error:" + error.getMessage());
        if (mCallBack != null) {
            netMessage.code = netMessage.NETWORK_ERROR;
            netMessage.msg = mContext.getResources().getString(
                    R.string.request_api_error);
            onDataReturn(result);
            this.mCallBack.finish(result);
        }
        if (mShowErrorMsgFromServer)//默认显示，但是可以关闭提示
            ToastUtil.getInstance().showToast(
                    mContext.getResources().getString(
                            R.string.request_api_error));
    }

    /**
     * 不callback处理，自己处理返回数据
     */
    protected void onDataReturn(NetResponse<T> result) {
    }

    public NetResponse<T> getResponse(String content) {
        NetResponse<T> result = new NetResponse<T>();
        result.netMsg = getBaseNetMessage(content);
        result.content = getContent(result.netMsg.data);
        return result;
    }

    /**
     * 实现具体的数据与实体的转换 通过Gson即可
     *
     * @param json
     * @return
     */
    abstract public T getContent(String json);

    /**
     * 获取通用格式的返回值
     *
     * @param content
     * @return
     */
    public NetMessage getBaseNetMessage(String content) {
        NetMessage netMessage = new NetMessage();
        try {
            JSONObject json = new JSONObject(content);
            if (json.has("code"))
                netMessage.code = json.optInt("code");
            else
                netMessage.code = json.optString("status").equals("ok") ? 1 : 0;
            netMessage.msg = json.optString("msg");
            netMessage.data = json.optString("data");
        } catch (Exception e) {
            e.printStackTrace();
            netMessage.msg = "返回数据错误";
            netMessage.code = -1;
            Log.e("getBaseNetMessage", e.toString());
        }
        LogUtil.e("返回数据", netMessage.code + " " + netMessage.msg);
        if (!TextUtils.isEmpty(netMessage.msg)) {
            if (netMessage.code != 1) {
                if (mShowErrorMsgFromServer)//默认显示，但是可以关闭提示
                    ToastUtil.getInstance().showToast(netMessage.msg);
            } else {
                if (mShowSucMsgFromServer)
                    ToastUtil.getInstance().showToast(netMessage.msg);
            }
        }
        return netMessage;
    }
}

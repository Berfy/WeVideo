package we.video.wevideo.net;

/**
 * @author Berfy
 * @ClassName: RequestCallBack
 * @Description: 接口回调处理
 */
public interface RequestCallBack<T> {
    /**
     * 请求开始
     */
    void start();

    /**
     * 请求结束 回传需要处理的类型
     *
     * @param result
     */
    void finish(NetResponse<T> result);

    void onProgress(float percent);
}

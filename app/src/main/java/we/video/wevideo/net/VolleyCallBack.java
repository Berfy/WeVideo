package we.video.wevideo.net;

import com.android.volley.VolleyError;

/**
 * 
 * @ClassName: RequestCallBack
 * @Description: 接口回调处理
 * @author Berfy
 *
 */
public interface VolleyCallBack {
	/**
	 * 请求结束 回传需要处理的类型
	 * 
	 * @param result
	 */
	void finish(String result);

	void error(VolleyError volleyError);
}

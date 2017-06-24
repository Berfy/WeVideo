package we.video.wevideo.net;

/**
 * 
 * @ClassName: NetMessage
 * @Description: 网络请求基类
 * @author heiyue heiyue623@126.com
 * @date 2014-5-19 上午10:57:04
 * 
 */
public class NetMessage {

	public static final int NETWORK_ERROR = 404;
	/**
	 * 接口调用错误信息 用于Log查看信息
	 */
	public String msg;
	/**
	 * 错误代码
	 */
	public int code;
	/**
	 * 接口返回数据信息
	 */
	public String data;
}

package we.video.wevideo.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Gson
 * 避免Gson实例化太多
 * @author Berfy
 */
public class GsonUtil {

	private static GsonUtil mGsonUtil;
	private Gson mGson;

	public static GsonUtil getInstance() {
		if (null == mGsonUtil) {
			mGsonUtil = new GsonUtil();
		}
		return mGsonUtil;
	}

	private GsonUtil() {
		mGson = new Gson();
	}

	public <T> String toJson(T classa) {
		return mGson.toJson(classa);
	}

	public <T> T toClass(String json, Class<T> classname) {
		return mGson.fromJson(json, classname);
	}

	public <T> T toClass(String json, Type type) {
		return mGson.fromJson(json, type);
	}
}

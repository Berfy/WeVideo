package we.video.wevideo;

import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.VolleyHttp;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.ToastUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyHttp.init(getApplicationContext());
        ServerApi.init(getApplicationContext());
        ImageUtil.init(getApplicationContext());
        ToastUtil.init(getApplicationContext());
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

        PlatformConfig.setWeixin("wx3ad8fe073ba1c178", "578b0baa629816ac51ed74936f0096b8");
        PlatformConfig.setSinaWeibo("1361565708", "110fc88186c904adce50166673536231");
    }
}

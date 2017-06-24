package we.video.wevideo.util;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.Map;

import we.video.wevideo.R;

/**
 * Created by Berfy on 2016/4/11.
 */
public class ShareUtil {

    private Context mContext;
    public static ShareUtil mWeixinUtil;

    public static ShareUtil getInstances(Context context) {
        if (null == mWeixinUtil) {
            mWeixinUtil = new ShareUtil(context);
        }
        return mWeixinUtil;
    }

    public ShareUtil(Context context) {
        mContext = context;
    }

    public void wx(String title, String content, String url) {
        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .share();
    }

    public void wxCirCle(String title, String content, String url) {
        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .share();
    }

    public void sina(String title, String content, String url) {
        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                .withTitle(title)
                .withText(content)
                .share();
    }

    public void wx_login() {
        UMShareAPI.get((Activity) mContext).doOauthVerify((Activity) mContext, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    public void sina_login() {
        UMShareAPI.get((Activity) mContext).doOauthVerify((Activity) mContext, SHARE_MEDIA.SINA, umAuthListener);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            LogUtil.e("分享成功", share_media.toString());
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            LogUtil.e("分享错误", share_media.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            LogUtil.e("分享错误", share_media.toString());
        }
    };

    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            LogUtil.e("登录成功", share_media.toString() + "  " + map.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            LogUtil.e("用户取消", share_media.toString());
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            LogUtil.e("登录失败", share_media.toString());
        }
    };
}

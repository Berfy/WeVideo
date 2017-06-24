package we.video.wevideo.cons;

import android.content.Context;

import we.video.wevideo.util.TempSharedData;

/**
 * Created by Berfy on 2016/4/13.
 * 用户状态存储
 */
public class UserTemp {

    private static UserTemp userTemp;
    private Context context;
    private String userId = "userId";
    private String isLogin = "isLogin";
    private String img = "img";
    private String name = "name";

    public static UserTemp getInstances(Context context) {
        if (null == userTemp) {
            userTemp = new UserTemp(context);
        }
        return userTemp;
    }

    public UserTemp(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return TempSharedData.getInstance(context).get(this.userId);
    }

    public void setUserId(String userId) {
        TempSharedData.getInstance(context).put(this.userId, userId);
    }

    public String getName() {
        return TempSharedData.getInstance(context).get(this.name);
    }

    public void setName(String name) {
        TempSharedData.getInstance(context).put(this.name, name);
    }

    public String getImg() {
        return TempSharedData.getInstance(context).get(this.img);
    }

    public void setImg(String img) {
        TempSharedData.getInstance(context).put(this.img, img);
    }

    public boolean isLogin() {
        return TempSharedData.getInstance(context).get(this.isLogin, false);
    }

    public void setIsLogin(boolean isLogin) {
        TempSharedData.getInstance(context).put(this.isLogin, isLogin);
    }
}

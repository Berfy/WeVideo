package we.video.wevideo.dao;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import we.video.wevideo.bean.Ad;
import we.video.wevideo.bean.AuthorInfo;
import we.video.wevideo.bean.Collect;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.bean.Product;
import we.video.wevideo.bean.Special;
import we.video.wevideo.bean.UserInfo;
import we.video.wevideo.bean.VerifyCode;
import we.video.wevideo.bean.Video;
import we.video.wevideo.cons.Urls;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.net.HttpParams;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.net.ResponseHandler;
import we.video.wevideo.net.VolleyHttp;
import we.video.wevideo.util.GsonUtil;

/**
 * Created by Berfy on 2016/4/14.
 * 接口访问
 */
public class ServerApi implements ServerDao {

    private static ServerApi serverApi;
    private Context context;

    public static ServerApi init(Context context) {
        if (null == serverApi) {
            serverApi = new ServerApi(context);
        }
        return serverApi;
    }

    public static ServerApi getInstances() {
        if (null == serverApi) {
            try {
                throw new NullPointerException(
                        "未初始化ServerApi，请在Application中调用ServerApi.init(Context context)方法");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return serverApi;
    }

    public ServerApi(Context context) {
        this.context = context;
    }


    @Override
    public void login(String mobile, String pwd, RequestCallBack<UserInfo> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.LOGIN);
        httpParams.put("mobile", mobile);
        httpParams.put("pwd", pwd);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<UserInfo>(this.context, requestCallBack) {

            @Override
            public UserInfo getContent(String json) {
                return GsonUtil.getInstance().toClass(json, UserInfo.class);
            }
        });
    }

    @Override
    public void reg(String mobile, String pwd, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.REG);
        httpParams.put("mobile", mobile);
        httpParams.put("pwd", pwd);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }

    @Override
    public void findPwd(String mobile, String pwd, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.FINDPWD);
        httpParams.put("user", mobile);
        httpParams.put("pwd", pwd);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }

    @Override
    public void getSpecialList(int page, int pageSize, RequestCallBack<List<Special>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_SPECIAL_LIST);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Special>>(this.context, requestCallBack) {

            @Override
            public List<Special> getContent(String json) {
                Type type = new TypeToken<List<Special>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getSpecialListByAuthor(String authorId, int page, int pageSize, RequestCallBack<List<Special>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_SPECIAL_LIST);
        httpParams.put("userId", authorId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Special>>(this.context, requestCallBack) {

            @Override
            public List<Special> getContent(String json) {
                Type type = new TypeToken<List<Special>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getSpecialDetail(String specialId, RequestCallBack<Special> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_SPECIAL_DETAIL);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("specialId", specialId);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<Special>(this.context, requestCallBack) {

            @Override
            public Special getContent(String json) {
                return GsonUtil.getInstance().toClass(json, Special.class);
            }
        });
    }

    @Override
    public void getVideoDetail(String videoId, RequestCallBack<Video> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_VIDEO_DETAIL);
        httpParams.put("videoId", videoId);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<Video>(this.context, requestCallBack) {

            @Override
            public Video getContent(String json) {
                return GsonUtil.getInstance().toClass(json, Video.class);
            }
        });
    }

    @Override
    public void getAdList(RequestCallBack<List<Ad>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_AD);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Ad>>(this.context, requestCallBack) {

            @Override
            public List<Ad> getContent(String json) {
                Type type = new TypeToken<List<Ad>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getUserInfo(String userId, RequestCallBack<UserInfo> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_USER_INFO);
        httpParams.put("userId", userId);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<UserInfo>(this.context, requestCallBack) {

            @Override
            public UserInfo getContent(String json) {
                return GsonUtil.getInstance().toClass(json, UserInfo.class);
            }
        });
    }

    @Override
    public void getAuthorInfo(String userId, RequestCallBack<AuthorInfo> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_AUTHOR_INFO);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("authorId", userId);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<AuthorInfo>(this.context, requestCallBack) {

            @Override
            public AuthorInfo getContent(String json) {
                return GsonUtil.getInstance().toClass(json, AuthorInfo.class);
            }
        });
    }

    @Override
    public void getVideoList(int page, int pageSize, String specialId, RequestCallBack<List<Video>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_VIDEO_LIST);
        httpParams.put("specialId", specialId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Video>>(this.context, requestCallBack) {

            @Override
            public List<Video> getContent(String json) {
                Type type = new TypeToken<List<Video>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getComment(String videoId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_COMMENT_LIST);
        httpParams.put("videoId", videoId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Comment>>(this.context, requestCallBack) {

            @Override
            public List<Comment> getContent(String json) {
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getUserComment(String userId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_USER_COMMENT_LIST);
        httpParams.put("userId", userId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Comment>>(this.context, requestCallBack) {

            @Override
            public List<Comment> getContent(String json) {
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getCommentBySpecial(String specialId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_COMMENT_LIST_BY_SPECIAL);
        httpParams.put("specialId", specialId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Comment>>(this.context, requestCallBack) {

            @Override
            public List<Comment> getContent(String json) {
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getCommentByAuthor(String authorId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_COMMENT_LIST_BY_AUTHOR);
        httpParams.put("authorId", authorId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Comment>>(this.context, requestCallBack) {

            @Override
            public List<Comment> getContent(String json) {
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void comment(String videoId, String content, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.COMMENT);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("videoId", videoId);
        httpParams.put("content", content);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack, true, true) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }

    @Override
    public void collect(int isCollect, String type, String typeId, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.COLLECT);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("isCollect", isCollect + "");
        httpParams.put("type", type);
        httpParams.put("typeId", typeId);
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack, true, true) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }

    @Override
    public void getCollect(int page, int pageSize, RequestCallBack<List<Collect>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_COLLECT);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Collect>>(this.context, requestCallBack) {

            @Override
            public List<Collect> getContent(String json) {
                Type type = new TypeToken<List<Collect>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getProductList(String videoId, int page, int pageSize, RequestCallBack<List<Product>> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_PRODUCT_LIST);
        httpParams.put("videoId", videoId);
        httpParams.put("page", page + "");
        httpParams.put("pageSize", pageSize + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<List<Product>>(this.context, requestCallBack) {

            @Override
            public List<Product> getContent(String json) {
                Type type = new TypeToken<List<Product>>() {
                }.getType();
                return GsonUtil.getInstance().toClass(json, type);
            }
        });
    }

    @Override
    public void getCode(String mobile, int type, RequestCallBack<VerifyCode> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.GET_CODE);
        httpParams.put("mobile", mobile);
        httpParams.put("type", type + "");
        VolleyHttp.getInstances().post(Urls.HOST, httpParams, new ResponseHandler<VerifyCode>(this.context, requestCallBack, true, true) {

            @Override
            public VerifyCode getContent(String json) {
                return GsonUtil.getInstance().toClass(json, VerifyCode.class);
            }
        });
    }

    @Override
    public void uploadImg(Context context, File file, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.UPLOADIMG);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("data", file);
        VolleyHttp.getInstances().postFile(context, Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack, true, true) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }

    @Override
    public void updateUserInfo(Context context, String name, RequestCallBack<String> requestCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("action", Urls.UPLOADUSERINFO);
        httpParams.put("userId", UserTemp.getInstances(context).getUserId());
        httpParams.put("name", name);
        VolleyHttp.getInstances().postFile(context, Urls.HOST, httpParams, new ResponseHandler<String>(this.context, requestCallBack, true, true) {

            @Override
            public String getContent(String json) {
                return json;
            }
        });
    }
}

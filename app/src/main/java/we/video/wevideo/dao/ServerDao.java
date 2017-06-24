package we.video.wevideo.dao;

import android.content.Context;

import java.io.File;
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
import we.video.wevideo.net.RequestCallBack;

/**
 * Created by Berfy on 2016/4/14.
 */
public interface ServerDao {

    void login(String mobile, String pwd, RequestCallBack<UserInfo> requestCallBack);

    void reg(String mobile, String pwd, RequestCallBack<String> requestCallBack);

    void findPwd(String mobile, String pwd, RequestCallBack<String> requestCallBack);

    void getSpecialList(int page, int pageSize, RequestCallBack<List<Special>> requestCallBack);

    void getSpecialListByAuthor(String authorId, int page, int pageSize, RequestCallBack<List<Special>> requestCallBack);

    void getSpecialDetail(String specialId, RequestCallBack<Special> requestCallBack);

    void getVideoDetail(String videoId, RequestCallBack<Video> requestCallBack);

    void getAdList(RequestCallBack<List<Ad>> requestCallBack);

    void getVideoList(int page, int pageSize, String specialId, RequestCallBack<List<Video>> requestCallBack);

    void getUserInfo(String userId, RequestCallBack<UserInfo> requestCallBack);

    void getAuthorInfo(String userId, RequestCallBack<AuthorInfo> requestCallBack);

    void getComment(String videoId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack);

    void getCommentByAuthor(String authorId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack);

    void getCommentBySpecial(String specialId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack);

    void getUserComment(String userId, int page, int pageSize, RequestCallBack<List<Comment>> requestCallBack);

    void comment(String videoId, String content, RequestCallBack<String> requestCallBack);

    void collect(int isCollect, String type, String typeId, RequestCallBack<String> requestCallBack);

    void getCollect(int page, int pageSize, RequestCallBack<List<Collect>> requestCallBack);

    void getProductList(String videoId, int page, int pageSize, RequestCallBack<List<Product>> requestCallBack);

    void getCode(String mobile, int type, RequestCallBack<VerifyCode> requestCallBack);

    void uploadImg(Context context, File file, RequestCallBack<String> requestCallBack);

    void updateUserInfo(Context context, String name, RequestCallBack<String> requestCallBack);

}

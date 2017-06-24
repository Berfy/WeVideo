package we.video.wevideo.bean;

import java.io.Serializable;

/**
 * Created by Berfy on 2016/4/5.
 * 专辑
 */
public class Special implements Serializable {

    private String id;//专辑id
    private String userId;//创建者Id
    private String thumb;//封面
    private String title;//标题
    private String intro;//简介
    private Video video;//视频
    private int collectNum;//收藏数量
    private int isCollect;//是否收藏 0未收藏 1已收藏
    private String createTime;//创建时间
    private AuthorInfo author;//作者信息

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public AuthorInfo getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInfo author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }
}

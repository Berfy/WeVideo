package we.video.wevideo.bean;

/**
 * Created by Berfy on 2016/4/6.
 * 评论
 */
public class Comment {

    private String id;
    private String userId;
    private String videoId;
    private String name;
    private String img;
    private String content;
    private String createTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String userImg) {
        this.img = userImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

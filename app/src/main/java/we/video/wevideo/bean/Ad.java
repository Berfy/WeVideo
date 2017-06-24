package we.video.wevideo.bean;

import java.io.Serializable;

/**
 * Created by Berfy on 2016/4/5.
 * 广告
 */
public class Ad implements Serializable {

    private String id;
    private String type;//0专辑1作者2外链
    private String type_id;//链接和id
    private String title;//标题
    private String thumb;//封面
    private String createTine;//创建时间

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setCreateTine(String createTine) {
        this.createTine = createTine;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getType_id() {
        return type_id;
    }

    public String getThumb() {
        return thumb;
    }

    public String getCreateTine() {
        return createTine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

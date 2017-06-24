package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.Gallery;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;

/**
 * Created by Berfy on 2016/5/17.
 */
public class GalleryView extends BaseView {

    private Gallery gallery;

    public GalleryView(Context context) {
        super(context);
        setContent(R.layout.view_special_gallery);
    }

    @Override
    protected void initView() {
        gallery = (Gallery) findViewById(R.id.gallery);
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

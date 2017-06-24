package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.VideoAdapter;
import we.video.wevideo.bean.Video;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.ScrollGridView;
import we.video.wevideo.util.DeviceUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑视频Grid列表
 */
public class VideoGridView extends BaseView {

    private ScrollGridView gridView;
    private VideoAdapter videoAdapter;
    private String specialId;
    private int itemWidth;

    public VideoGridView(Context context) {
        super(context);
        setContent(R.layout.view_gridview);
        itemWidth = DeviceUtil.getScreenWidth(mContext) / 3 * 2;
    }

    public void setSpecialId(String specialId) {
        if (this.specialId != specialId) {
            this.specialId = specialId;
            getData();
        }
    }

    public void setOnVideoSelectListener(VideoAdapter.OnVideoSelectListener videoSelectListener) {
        videoAdapter.setOnVideoSelectListener(videoSelectListener);
    }

    @Override
    protected void initView() {
        gridView = (ScrollGridView) findViewById(R.id.gridView);
        videoAdapter = new VideoAdapter(mContext);
        gridView.setAdapter(videoAdapter);
    }

    public void setSelectPostion(int selectPostion) {
        if (null != videoAdapter) {
            videoAdapter.setSelectPosition(selectPostion);
        }
    }

    private void getData() {
        ServerApi.getInstances().getVideoList(1, 100, specialId, new RequestCallBack<List<Video>>() {
            @Override
            public void start() {

            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Video>> result) {
                videoAdapter.getData().clear();
                if (result.netMsg.code == 1) {
                    videoAdapter.getData().addAll(result.content);
                    int size = result.content.size();
                    int allWidth = itemWidth * size;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.FILL_PARENT);
                    gridView.setColumnWidth(itemWidth);
                    gridView.setLayoutParams(params);
                    gridView.setStretchMode(GridView.NO_STRETCH);
                    gridView.setNumColumns(size);
                }
                videoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

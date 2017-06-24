package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.VideoAdapter;
import we.video.wevideo.bean.Video;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑视频列表
 */
public class VideoListView extends BaseView {

    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private VideoAdapter videoAdapter;
    private int PAGE = 1;
    private String specialId;

    private VideoAdapter.OnVideoSelectListener onVideoSelectListener;

    public VideoListView(Context context) {
        super(context);
        setContent(R.layout.view_list);
    }

    public void setData(String specialId) {
        this.specialId = specialId;
        materialRefreshLayout.autoRefresh();
    }

    public void setOnVideoSelectListener(VideoAdapter.OnVideoSelectListener onVideoSelectListener) {
        this.onVideoSelectListener = onVideoSelectListener;
        videoAdapter.setOnVideoSelectListener(onVideoSelectListener);
    }

    @Override
    protected void initView() {
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshView);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getData(false);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getData(true);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        videoAdapter = new VideoAdapter(mContext);
        listView.setAdapter(videoAdapter);
    }

    private void getData(boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
            videoAdapter.getData().clear();
        }
        ServerApi.getInstances().getVideoList(PAGE, 10, specialId, new RequestCallBack<List<Video>>() {
            @Override
            public void start() {


            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Video>> result) {
                if (result.netMsg.code == 1) {
                    videoAdapter.getData().addAll(result.content);
                    videoAdapter.notifyDataSetChanged();
                    PAGE++;
                }
            }
        });
        videoAdapter.notifyDataSetChanged();
        materialRefreshLayout.finishRefresh();
        materialRefreshLayout.finishRefreshLoadMore();
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

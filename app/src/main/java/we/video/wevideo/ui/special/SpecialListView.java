package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.SpecialAdapter;
import we.video.wevideo.bean.Special;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑视频列表
 */
public class SpecialListView extends BaseView {

    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private SpecialAdapter specialAdapter;
    private int PAGE = 1;
    private String authorId;

    private SpecialAdapter.OnSpeicalSelectListener onSpecialSelectListener;

    public SpecialListView(Context context) {
        super(context);
        setContent(R.layout.view_list);
    }

    public void setData(String authorId) {
        this.authorId = authorId;
        materialRefreshLayout.autoRefresh();
    }

    public void setOnSpecialSelectListener(SpecialAdapter.OnSpeicalSelectListener specialSelectListener) {
        this.onSpecialSelectListener = specialSelectListener;
        specialAdapter = new SpecialAdapter(mContext, onSpecialSelectListener);
        listView.setAdapter(specialAdapter);
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
    }

    private void getData(boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
            specialAdapter.getData().clear();
        }
        ServerApi.getInstances().getSpecialListByAuthor(authorId, PAGE, 10, new RequestCallBack<List<Special>>() {
            @Override
            public void start() {

            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Special>> result) {
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
                if (result.netMsg.code == 1) {
                    specialAdapter.getData().addAll(result.content);
                    specialAdapter.notifyDataSetChanged();
                    PAGE++;
                    specialAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

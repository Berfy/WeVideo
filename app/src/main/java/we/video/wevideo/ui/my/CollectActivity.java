package we.video.wevideo.ui.my;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.adapter.CollectAdapter;
import we.video.wevideo.bean.Collect;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;
import we.video.wevideo.ui.support.swipe.SwipeListView;

/**
 * Created by Berfy on 2016/6/3.
 * 收藏列表
 */
public class CollectActivity extends BaseActivity {

    private MaterialRefreshLayout refreshLayout;
    private SwipeListView listView;
    private CollectAdapter adapter;
    private int PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.view_swipe_list);
    }

    @Override
    protected void initView() {
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshView);
        listView = (SwipeListView) findViewById(R.id.listView);
        adapter = new CollectAdapter(mContext, listView.getRightViewWidth());
        listView.setAdapter(adapter);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
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
        refreshLayout.autoRefresh();
    }

    @Override
    protected void doClickEvent(View v) {

    }

    private void getData(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }
        ServerApi.getInstances().getCollect(PAGE, 20, new RequestCallBack<List<Collect>>() {
            @Override
            public void start() {
                showLoading();
            }

            @Override
            public void finish(NetResponse<List<Collect>> result) {
                dismissLoading();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                if (result.netMsg.code == 1) {
                    if (!isLoadMore) {
                        adapter.getData().clear();
                    }
                    adapter.getData().addAll(result.content);
                    PAGE++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onProgress(float percent) {

            }
        });
    }
}

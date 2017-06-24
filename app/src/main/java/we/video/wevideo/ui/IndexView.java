package we.video.wevideo.ui;

import android.content.Context;
import android.view.View;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.IndexAdapter;
import we.video.wevideo.bean.Ad;
import we.video.wevideo.bean.Special;
import we.video.wevideo.cons.Constants;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;
import we.video.wevideo.ui.support.lib.SlidingMenu;
import we.video.wevideo.ui.support.ScrollListView;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/4/7.
 * 主页
 */
public class IndexView extends BaseView implements ScrollListView.OnScrollListener {

    private MaterialRefreshLayout refreshLayout;
    private ScrollListView listView;
    private IndexAdapter adapter;
    private int PAGE = 1;

    public IndexView(Context context) {
        super(context);
        setContent(R.layout.view_list);
        Constants.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                for (int i = 0; i < 10000; i++) {
                    LogUtil.e("循环", i + "   " + (System.currentTimeMillis() - time) + "");
                    if(System.currentTimeMillis() - time>1000){
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void initView() {
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshView);
        listView = (ScrollListView) findViewById(R.id.listView);
        adapter = new IndexAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
//        refreshLayout.setSunStyle(true);
        refreshLayout.autoRefresh();
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getData(false);
            }

            @Override
            public void onfinish() {
                super.onfinish();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                refreshLayout.finishRefreshLoadMore();
                getData(true);
            }
        });
    }

    public void setSlidingMenu(SlidingMenu slidingMenu) {
        adapter.setSlidingMenu(slidingMenu);
    }

    private void getData(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }
        if (!isLoadMore) {
            ServerApi.getInstances().getAdList(new RequestCallBack<List<Ad>>() {
                @Override
                public void start() {

                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<List<Ad>> result) {
                    refreshLayout.finishRefresh();
                    if (result.netMsg.code == 1) {
                        adapter.setBannerData(result.content);
                        adapter.notifyDataSetChanged();
                    }
                    ServerApi.getInstances().getSpecialList(PAGE, 20, new RequestCallBack<List<Special>>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void onProgress(float percent) {

                        }

                        @Override
                        public void finish(NetResponse<List<Special>> result) {
                            if (result.netMsg.code == 1) {
                                if (!isLoadMore) {
                                    adapter.clear();
                                }
                                adapter.addAll(result.content);
                                adapter.notifyDataSetChanged();
                                PAGE++;
                            }
                        }
                    });
                }
            });
        } else {
            ServerApi.getInstances().getSpecialList(PAGE, 20, new RequestCallBack<List<Special>>() {
                @Override
                public void start() {

                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<List<Special>> result) {
                    refreshLayout.finishRefresh();
                    if (result.netMsg.code == 1) {
                        int size = result.content.size();
                        if (size > 0) {
                            adapter.addAll(result.content);
                            adapter.notifyDataSetChanged();
                        } else {
                            showToast("没有更多专辑了");
                        }
                        PAGE++;
                    }
                }
            });
        }
    }

    @Override
    protected void doClickEvent(View v) {

    }

    @Override
    public void onScroll(int y, int oldY) {
//        LogUtil.e("滑动", y + "  " + oldY);
    }
}

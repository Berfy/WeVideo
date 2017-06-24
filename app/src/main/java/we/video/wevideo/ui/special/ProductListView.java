package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.ProductAdapter;
import we.video.wevideo.bean.Product;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;

/**
 * Created by Berfy on 2016/4/6.
 * 评论列表
 */
public class ProductListView extends BaseView {

    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private ProductAdapter productAdapter;
    private int PAGE = 1;
    private String videoId;

    public ProductListView(Context context) {
        super(context);
        setContent(R.layout.view_list);
    }

    public void setData(String videoId) {
        this.videoId = videoId;
        materialRefreshLayout.autoRefresh();
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
        materialRefreshLayout.autoRefresh();
        listView = (ListView) findViewById(R.id.listView);
        productAdapter = new ProductAdapter(mContext);
        listView.setAdapter(productAdapter);
    }

    private void getData(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }
        ServerApi.getInstances().getProductList(videoId, PAGE, 10, new RequestCallBack<List<Product>>() {
            @Override
            public void start() {

            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Product>> result) {
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
                if (result.netMsg.code == 1) {
                    if (!isLoadMore) {
                        productAdapter.getData().clear();
                    }
                    productAdapter.getData().addAll(result.content);
                    productAdapter.notifyDataSetChanged();
                    PAGE++;
                }
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

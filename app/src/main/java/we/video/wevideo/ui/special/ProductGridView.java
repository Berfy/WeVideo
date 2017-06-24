package we.video.wevideo.ui.special;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.ProductAdapter;
import we.video.wevideo.bean.Product;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.util.DeviceUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 评论列表
 */
public class ProductGridView extends BaseView {

    private GridView gridView;
    private ProductAdapter productAdapter;
    private String videoId;
    private int itemWidth;

    public ProductGridView(Context context) {
        super(context);
        setContent(R.layout.view_gridview);
        itemWidth = DeviceUtil.getScreenWidth(mContext) / 2;
    }

    public void setData(String videoId) {
        this.videoId = videoId;
        getData();
    }

    @Override
    protected void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        productAdapter = new ProductAdapter(mContext);
        gridView.setAdapter(productAdapter);
    }

    private void getData() {
        ServerApi.getInstances().getProductList(videoId, 1, 100, new RequestCallBack<List<Product>>() {
            @Override
            public void start() {

            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Product>> result) {
                productAdapter.getData().clear();
                if (result.netMsg.code == 1) {
                    productAdapter.getData().addAll(result.content);
                    int size = result.content.size();
                    int allWidth = itemWidth * size;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.FILL_PARENT);
                    gridView.setColumnWidth(itemWidth);
                    gridView.setLayoutParams(params);
                    gridView.setStretchMode(GridView.NO_STRETCH);
                    gridView.setNumColumns(size);
                }
                productAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

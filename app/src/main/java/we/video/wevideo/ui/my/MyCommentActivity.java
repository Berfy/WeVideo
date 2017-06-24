package we.video.wevideo.ui.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.adapter.CommentAdapter;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;

/**
 * Created by Berfy on 2016/6/3.
 * 我的评论
 */
public class MyCommentActivity extends BaseActivity {

    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private CommentAdapter commentAdapter;
    private int PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.view_list);
    }

    @Override
    protected void initView() {
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshView);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getComment(false);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getComment(true);
            }
        });
        materialRefreshLayout.autoRefresh();
        listView = (ListView) findViewById(R.id.listView);
        commentAdapter = new CommentAdapter(mContext);
        listView.setAdapter(commentAdapter);
    }

    private void getComment(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }
        showLoading();
        ServerApi.getInstances().getUserComment(UserTemp.getInstances(mContext).getUserId(), PAGE, 20, new RequestCallBack<List<Comment>>() {
            @Override
            public void start() {
            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Comment>> result) {
                dismissLoading();
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
                if (result.netMsg.code == 1) {
                    if (!isLoadMore) {
                        commentAdapter.getData().clear();
                    }
                    if (result.content.size() > 0) {
                        commentAdapter.getData().addAll(result.content);
                        commentAdapter.notifyDataSetChanged();
                        PAGE++;
                    } else {
                        showToast("没有更多评论了");
                    }
                }
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }
}

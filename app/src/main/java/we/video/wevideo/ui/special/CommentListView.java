package we.video.wevideo.ui.special;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.adapter.CommentAdapter;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;

/**
 * Created by Berfy on 2016/4/6.
 * 评论列表(专栏所有评论)
 */
public class CommentListView extends BaseView {

    private MaterialRefreshLayout materialRefreshLayout;
    private ListView listView;
    private CommentAdapter commentAdapter;
    private int PAGE = 1;
    private String authorId, specialId;

    public CommentListView(Context context) {
        super(context);
        setContent(R.layout.view_comment_list);
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
        materialRefreshLayout.autoRefresh();
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
        materialRefreshLayout.autoRefresh();
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
        findViewById(R.id.layout_comment).setVisibility(View.GONE);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        commentAdapter = new CommentAdapter(mContext);
        listView.setAdapter(commentAdapter);
    }

    private void getComment(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }

        if (!TextUtils.isEmpty(specialId)) {
            ServerApi.getInstances().getCommentBySpecial(specialId, PAGE, 20, new RequestCallBack<List<Comment>>() {
                @Override
                public void start() {

                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<List<Comment>> result) {
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
        } else {
            ServerApi.getInstances().getCommentByAuthor(authorId, PAGE, 20, new RequestCallBack<List<Comment>>() {
                @Override
                public void start() {

                }

                @Override
                public void onProgress(float percent) {

                }

                @Override
                public void finish(NetResponse<List<Comment>> result) {
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
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
        }
    }
}

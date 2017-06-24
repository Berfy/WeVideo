package we.video.wevideo.ui.special;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import we.video.wevideo.ActivityManager;
import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.adapter.SpecialAdapter;
import we.video.wevideo.bean.AuthorInfo;
import we.video.wevideo.bean.Special;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.round.RoundImageView;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.ViewUtil;

/**
 * Created by Berfy on 2016/4/7.
 * 作者详情
 */
public class AuthorActivity extends BaseActivity {

    private CommentListView commentListView;
    private SpecialListView specialListView;
    private static final int COMMENT = 0;
    private static final int SPECIAL = 1;
    private int WITCH = COMMENT;

    private PopupWindowUtil popupWindowUtil;
    private RoundImageView iv_icon;
    private ViewUtil viewUtil;
    private String authorId;
    private AuthorInfo authorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_author);
    }

    @Override
    protected void initView() {
        authorId = getIntent().getStringExtra("id");
        popupWindowUtil = new PopupWindowUtil(mContext);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        findViewById(R.id.btn_special).setOnClickListener(this);
        findViewById(R.id.btn_collect).setOnClickListener(this);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        iv_icon.setOnClickListener(this);
        viewUtil = new ViewUtil();
        getBottomData(SPECIAL);
        getData();
    }

    private void updateStatus() {
        ((Button) findViewById(R.id.btn_collect)).setText(authorInfo.getIsCollect() == 1 ? getString(R.string.hascollect) : getString(R.string.collect));
    }

    private void getData() {
        ServerApi.getInstances().getAuthorInfo(authorId, new RequestCallBack<AuthorInfo>() {
            @Override
            public void start() {
                showLoading();
            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<AuthorInfo> result) {
                dismissLoading();
                if (result.netMsg.code == 1) {
                    authorInfo = result.content;
                    updateStatus();
                    ((TextView) findViewById(R.id.tv_name)).setText(authorInfo.getName());
                    setTopTitle(authorInfo.getName());
                    setTitle(authorInfo.getName());
                    ((TextView) findViewById(R.id.tv_name)).setText(authorInfo.getName());
                    ((TextView) findViewById(R.id.tv_intro)).setText(authorInfo.getIntro());
                    ImageUtil.getInstances().displayDefault(iv_icon, authorInfo.getImg());
                }
            }
        });
    }

    private void getBottomData(int which) {
        findViewById(R.id.btn_comment).setBackgroundResource(R.drawable.bg_shape_main_green_btn_press);
        findViewById(R.id.btn_special).setBackgroundResource(R.drawable.bg_shape_main_green_btn_press);
        switch (which) {
            case COMMENT:
                if (null == commentListView) {
                    commentListView = new CommentListView(mContext);
                    commentListView.setAuthorId(authorId);
                }
                commentListView.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewUtil.updateView(mContext, commentListView.getView(), ((LinearLayout) findViewById(R.id.layout_bottom_content)));
                findViewById(R.id.btn_comment).setBackgroundResource(R.drawable.bg_shape_main_green_btn_normal);
                break;
            case SPECIAL:
                if (null == specialListView) {
                    specialListView = new SpecialListView(mContext);
                    specialListView.setOnSpecialSelectListener(new SpecialAdapter.OnSpeicalSelectListener() {
                        @Override
                        public void onSelect(Special special) {
                            ActivityManager.getInstance().popActivity(SpecialActivity.class);
                            Intent intent = new Intent(mContext, SpecialActivity.class);
                            intent.putExtra("id", special.getId());
                            startActivity(intent);
                        }
                    });
                    specialListView.setData(authorId);
                }
                specialListView.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewUtil.updateView(mContext, specialListView.getView(), ((LinearLayout) findViewById(R.id.layout_bottom_content)));
                findViewById(R.id.btn_special).setBackgroundResource(R.drawable.bg_shape_main_green_btn_normal);
                break;
        }
        WITCH = which;
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_comment:
                getBottomData(COMMENT);
                break;
            case R.id.btn_special:
                getBottomData(SPECIAL);
                break;
            case R.id.btn_collect:
                if (null != authorInfo) {
                    final int isCollect = authorInfo.getIsCollect() == 0 ? 1 : 0;
                    ServerApi.getInstances().collect(isCollect, "1", authorId, new RequestCallBack<String>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void finish(NetResponse<String> result) {
                            if (result.netMsg.code == 1) {
                                authorInfo.setIsCollect(isCollect);
                                updateStatus();
                            }
                        }

                        @Override
                        public void onProgress(float percent) {

                        }
                    });
                }
                break;
            case R.id.iv_icon:
                if (null != authorInfo) {
                    ArrayList<String> urls = new ArrayList<>();
                    urls.add(authorInfo.getImg());
                    popupWindowUtil.showBigImage(findViewById(R.id.iv_icon), true, urls, 0);
                }
                break;
        }
    }
}

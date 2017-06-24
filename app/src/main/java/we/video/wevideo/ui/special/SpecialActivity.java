package we.video.wevideo.ui.special;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;

import we.video.wevideo.BaseActivity;
import we.video.wevideo.R;
import we.video.wevideo.adapter.SpecialDetailAdapter;
import we.video.wevideo.adapter.VideoAdapter;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.bean.Special;
import we.video.wevideo.bean.Video;
import we.video.wevideo.cons.Constants;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshLayout;
import we.video.wevideo.ui.support.refresh.Mater.MaterialRefreshListener;
import we.video.wevideo.util.AnimUtil;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.LogUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.ToastUtil;

/**
 * Created by Berfy on 2016/4/5.
 * 专辑详情(播放)
 */
public class SpecialActivity extends BaseActivity implements SurfaceHolder.Callback {

    private MaterialRefreshLayout refreshLayout;
    private ListView listView;
    private SpecialDetailAdapter adapter;
    private int PAGE = 1;

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private AnimUtil animUtil;

    private Button btn_play, btn_full;
    private SeekBar seekBar;

    private boolean canPlay;
    private boolean isTouchSeekBar;
    private boolean isShowControler = false;

    private int videoWidth, videoHeight;
    private int width, height;

    private int progree;
    private int during;

    private PopupWindowUtil popupWindowUtil;

    private Special special;
    private String specialId;

    private boolean isSurfaceCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_special);
        findViewById(R.id.header).setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshView);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getComment(true);
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                commentGone();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        findViewById(R.id.layout_bg).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentGone();
                return false;
            }
        });
        adapter = new SpecialDetailAdapter(mContext, new SpecialDetailAdapter.OnCommentListener() {
            @Override
            public void commentClick() {
                commentVisible();
            }
        }, new VideoAdapter.OnVideoSelectListener() {
            @Override
            public void select(Video video) {
                playVideo(video.getId());
            }
        });
        listView.setAdapter(adapter);
        specialId = getIntent().getStringExtra("id");
        LogUtil.e("专辑Id", specialId);
        popupWindowUtil = new PopupWindowUtil(mContext);
        surfaceView = (SurfaceView) findViewById(R.id.video);
//        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(DeviceUtil.getScreenWidth(mContext), DeviceUtil.getScreenWidth(mContext) * 9 / 16));
        surfaceHolder = surfaceView.getHolder();//SurfaceHolder是SurfaceView的控制接口
        surfaceHolder.addCallback(this);//因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
//        surfaceHolder.setFixedSize(320, 220);//显示的分辨率,不设置为视频默认
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型

        animUtil = new AnimUtil(mContext);

        width = DeviceUtil.getScreenWidth(mContext);
        height = DeviceUtil.getScreenHeight(mContext);
        findViewById(R.id.layout_video).setOnClickListener(this);
        findViewById(R.id.layout_top_left).setOnClickListener(this);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        btn_full = (Button) findViewById(R.id.btn_full);
        btn_full.setOnClickListener(this);
        findViewById(R.id.layout_video).setLayoutParams(new LinearLayout.LayoutParams(width, width * 9 / 16));
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setEnabled(false);
        findViewById(R.id.layout_video).setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouchSeekBar = true;
                handler.removeMessages(0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouchSeekBar = false;
                showControler();
                if (null != mediaPlayer) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
        updateInfo();
    }

    private void commentVisible() {
        findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);
        findViewById(R.id.edit_content).setFocusable(true);
        findViewById(R.id.edit_content).requestFocus();
        DeviceUtil.openKeyboard(mContext);
    }

    private void commentGone() {
        DeviceUtil.closeKeyboard(mContext, findViewById(R.id.edit_content).getWindowToken());
        findViewById(R.id.layout_comment).setVisibility(View.GONE);
    }

    private void updateInfo() {
        if (null != special) {
            setTitle(special.getVideo().getTitle());
            ((TextView) findViewById(R.id.tv_video_title_left)).setText(getLeftTitle());
            ((TextView) findViewById(R.id.tv_special_name)).setText(special.getTitle());
            ((TextView) findViewById(R.id.tv_video_title)).setText(special.getVideo().getTitle());
        }
    }

    private void getSpecial() {
        ServerApi.getInstances().getSpecialDetail(specialId, new RequestCallBack<Special>() {
            @Override
            public void start() {
            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<Special> result) {
                if (result.netMsg.code == 1) {
                    special = result.content;
                    adapter.setData(special);
                    adapter.notifyDataSetChanged();
                    refreshLayout.autoRefresh();
                    try {
                        if (null != special && null != special.getVideo()) {
                            playVideo(special.getVideo().getId());
                        } else {
                            ToastUtil.getInstance().showToast(R.string.special_tip_null);
                            SpecialActivity.this.finish();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void playVideo(String videoId) {
        stop();
        ServerApi.getInstances().getVideoDetail(videoId, new RequestCallBack<Video>() {
            @Override
            public void start() {
            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<Video> result) {
                if (result.netMsg.code == 1) {
                    if (null != result.content) {
                        if (null != special) {
                            special.setVideo(result.content);
                        }
                    }
                    getComment(false);
                    adapter.setData(special);
                    adapter.notifyDataSetChanged();
                    refreshLayout.autoRefresh();
                    updateInfo();
                    if (isSurfaceCreate)
                        play();
                }
            }
        });
    }

    private void getComment(final boolean isLoadMore) {
        if (!isLoadMore) {
            PAGE = 1;
        }
        ServerApi.getInstances().getComment(special.getVideo().getId(), PAGE, 10, new RequestCallBack<List<Comment>>() {
            @Override
            public void start() {

            }

            @Override
            public void onProgress(float percent) {

            }

            @Override
            public void finish(NetResponse<List<Comment>> result) {
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                if (!isLoadMore) {
                    adapter.getData().clear();
                }
                if (result.netMsg.code == 1) {
                    if (result.content.size() > 0) {
                        adapter.getData().addAll(result.content);
                        PAGE++;
                    } else {
                        if (isLoadMore) {
                            showToast("没有更多评论了");
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                play();
                break;
            case R.id.btn_full:
                if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.layout_video:
                showControler();
                break;
            case R.id.layout_top_left:
                exit();
                break;
            case R.id.btn_comment:
                if (null != special)
                    if (UserTemp.getInstances(mContext).isLogin()) {
                        String content = ((EditText) findViewById(R.id.edit_content)).getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            showToast(mContext.getString(R.string.tip_comment_null));
                        } else {
                            ServerApi.getInstances().comment(special.getVideo().getId(), content, new RequestCallBack<String>() {

                                @Override
                                public void start() {
                                    showLoading();
                                }

                                @Override
                                public void onProgress(float percent) {

                                }

                                @Override
                                public void finish(NetResponse<String> result) {
                                    dismissLoading();
                                    if (result.netMsg.code == 1) {
                                        getComment(false);
                                        ((EditText) findViewById(R.id.edit_content)).getText().clear();
                                        commentGone();
                                    }
                                }
                            });
                        }
                    } else {
                        DeviceUtil.toLogin(mContext, true);
                    }
                break;
        }
    }

    /**
     * 全屏切换
     */
    public void fullScreenChange(boolean isFull) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        System.out.println("fullScreen的值:" + isFull);
        if (!isFull) {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            //取消全屏设置
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("info", "landscape");// 横屏
            fullScreenChange(true);
            findViewById(R.id.btn_full).setBackgroundResource(R.mipmap.ic_video_zoomin);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("info", "portrait"); // 竖屏
            fullScreenChange(false);
            findViewById(R.id.btn_full).setBackgroundResource(R.mipmap.ic_video_zoomout);
        }
        fixScreenSize();
    }

    private void showControler() {
        if (!isShowControler) {
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessageDelayed(0, 5000);
        } else {
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://隐藏
                    isShowControler = false;
                    LogUtil.e("隐藏", "");
                    animUtil.bottomDismiss(findViewById(R.id.layout_controller));
                    animUtil.topDismiss(findViewById(R.id.layout_title));
                    break;
                case 1://显示
                    LogUtil.e("显示", "");
                    isShowControler = true;
                    animUtil.bottomShow(findViewById(R.id.layout_controller));
                    animUtil.topShow(findViewById(R.id.layout_title));
                    break;
            }
        }
    };

    private void play() {
        if (null != special) {
            //设置显示视频显示在SurfaceView上
            if (null == mediaPlayer) {
                findViewById(R.id.layout_state).setVisibility(View.VISIBLE);
                LogUtil.e("重新播放", "");
                //必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        LogUtil.d("缓冲进度", percent + "");
                        ((SeekBar) findViewById(R.id.seekbar)).setSecondaryProgress(during * percent / 100);
                    }
                });
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        LogUtil.d("播放信息", what + " " + extra);
                        return false;
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        btn_play.setEnabled(true);
                        findViewById(R.id.layout_video).setEnabled(true);
                        findViewById(R.id.layout_state).setVisibility(View.GONE);
                        seekBar.setEnabled(true);
                        canPlay = true;
                        fixScreenSize();
                        during = mediaPlayer.getDuration();
                        seekBar.setMax(during);
                        mediaPlayer.start();
                        mediaPlayer.seekTo(progree > 3000 ? progree - 3000 : progree);
                        btn_play.setBackgroundResource(R.mipmap.ic_video_pause);
                        checkSeekBar();
                    }
                });
                btn_play.setEnabled(false);
                Constants.EXECUTOR_PLAY.execute(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("播放地址", special.getVideo().getUrl());
                        try {
                            mediaPlayer.setDataSource(special.getVideo().getUrl());
                            mediaPlayer.prepare();
                            MediaMetadataRetriever retr = new MediaMetadataRetriever();
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                            retr.setDataSource(special.getVideo().getUrl(), headers);
                            videoWidth = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)); // 视频宽度
                            videoHeight = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)); // 视频高度
                            LogUtil.e("视频原始宽高", videoWidth + "  " + videoHeight);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fixScreenSize();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e("播放错误", "");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stop();
                                    new PopupWindowUtil(mContext).showTipChoosePop(getWindow().getDecorView(), "提示", "播放地址有问题", "重试", "取消", new PopupWindowUtil.OnPopListener() {
                                        @Override
                                        public void ok() {
                                            play();
                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            } else {
                if (mediaPlayer.isPlaying()) {
                    pause();
                    btn_play.setBackgroundResource(R.mipmap.ic_video_play);
                } else {
                    LogUtil.e("继续播放", "");
                    btn_play.setBackgroundResource(R.mipmap.ic_video_pause);
                    mediaPlayer.start();
                }
            }
        }
    }

    private void pause() {
        LogUtil.e("暂停", "");
        if (null != mediaPlayer) {
            mediaPlayer.pause();
        }
    }

    private void stop() {
        btn_play.setBackgroundResource(R.mipmap.ic_video_play);
        if (null != mediaPlayer) {
            LogUtil.e("停止播放", "");
            mediaPlayer.release();
            progree = 0;
            mediaPlayer = null;
            canPlay = false;
        }
    }

    private void fixScreenSize() {
        width = DeviceUtil.getScreenWidth(mContext);
        height = DeviceUtil.getScreenHeight(mContext);
        if (videoWidth > 0 && videoHeight > 0) {
            double bili = (videoWidth * 0.1) / (videoHeight * 0.1);
            LogUtil.e("视频宽高", videoWidth + " " + videoHeight);
            LogUtil.e("屏幕宽高", width + " " + height);
            int screenWidth;
            int screenHeight;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
                width += DeviceUtil.getBottoMenuHeight(mContext);
                screenWidth = width;
                screenHeight = height;
                LogUtil.e("外围宽高", width + " " + height);
                findViewById(R.id.layout_video).setLayoutParams(new LinearLayout.LayoutParams(width, height));
            } else {//竖屏
                screenWidth = width;
                screenHeight = screenWidth * 9 / 16;
                LogUtil.e("外围宽高", screenWidth + " " + screenHeight);
                findViewById(R.id.layout_video).setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenHeight));
            }
            int videoWitdh;
            int videoHeight;
            if ((videoWidth - videoWidth) > (screenWidth - screenHeight)) {//宽过屏幕
                videoWitdh = screenWidth;
                videoHeight = videoWitdh * this.videoHeight / this.videoWidth;
            } else {//比屏幕窄
                videoHeight = screenHeight;
                videoWitdh = (int) (videoHeight * bili);
            }
            LogUtil.e("最大宽高", screenWidth + " " + screenHeight);
            LogUtil.e("适配宽高", videoWitdh + " " + videoHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoWitdh, videoHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            surfaceView.setLayoutParams(layoutParams);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
                width += DeviceUtil.getBottoMenuHeight(mContext);
                findViewById(R.id.layout_video).setLayoutParams(new LinearLayout.LayoutParams(width, height));
            } else {//竖屏
                findViewById(R.id.layout_video).setLayoutParams(new LinearLayout.LayoutParams(width, width * 9 / 16));
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            surfaceView.setLayoutParams(layoutParams);
        }
    }

    private void checkSeekBar() {
        Constants.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                while (canPlay) {
                    if (!isTouchSeekBar)
                        try {
                            Thread.sleep(300);
                            if (canPlay && null != mediaPlayer) {
                                LogUtil.d("扫描时间", mediaPlayer.getCurrentPosition() + "    " + getFormatTime("mm:ss", mediaPlayer.getCurrentPosition()));
                                seekBar.setProgress((int) mediaPlayer.getCurrentPosition());
                                progree = mediaPlayer.getCurrentPosition();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView) findViewById(R.id.tv_cur_time)).setText(getFormatTime("mm:ss", progree));
                                        ((TextView) findViewById(R.id.tv_total_time)).setText(getFormatTime("mm:ss", during));
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }

    /**
     * 根据时间格式转换为字符串
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String getFormatTime(String pattern, long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        Date date2 = new Date();
        date2.setTime(date);
        return sdf.format(date2);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.e("surfaceView创建", "");
        isSurfaceCreate = true;
        getSpecial();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e("surfaceView改变", "");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e("surfaceView销毁", "");
        isSurfaceCreate = false;
        canPlay = false;
        stop();
        Constants.EXECUTOR_PLAY.shutdownNow();
        Constants.EXECUTOR_PLAY = null;
        Constants.EXECUTOR_PLAY = Executors.newFixedThreadPool(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void exit() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

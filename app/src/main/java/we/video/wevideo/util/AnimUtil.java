package we.video.wevideo.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import we.video.wevideo.R;

/**
 * Created by Berfy on 2016/4/7.
 * 动画工具类
 */
public class AnimUtil implements Animation.AnimationListener {

    private AnimationSet topShow;
    private AnimationSet topDismiss;
    private AnimationSet bottomShow;
    private AnimationSet bottomDismiss;
    private boolean isRunning;

    private static AnimUtil animUtil;
    private View topView, bottomView;
    private Context context;

    public AnimUtil(Context context) {
        this.context = context;
        bottomShow = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.bottom_show);
        bottomDismiss = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.bottom_dismiss);
        topShow = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.top_show);
        topDismiss = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.top_dismiss);

        bottomShow.setAnimationListener(this);
        bottomDismiss.setAnimationListener(this);
        topShow.setAnimationListener(this);
        topDismiss.setAnimationListener(this);
    }

    public void bottomShow(View view) {
        bottomView = view;
        if (!isRunning) {
            LogUtil.e("底部显示动画", "");
            view.startAnimation(bottomShow);
        }
    }

    public void bottomDismiss(View view) {
        bottomView = view;
        if (!isRunning) {
            LogUtil.e("底部隐藏动画", "");
            view.startAnimation(bottomDismiss);
        }
    }

    public void topShow(View view) {
        topView = view;
        if (!isRunning) {
            LogUtil.e("顶部显示动画", "");
            view.startAnimation(topShow);
        }
    }

    public void topDismiss(View view) {
        topView = view;
        if (!isRunning) {
            LogUtil.e("顶部隐藏动画", "");
            view.startAnimation(topDismiss);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        isRunning = true;
        if (animation == bottomShow) {
            LogUtil.e("开始下部", "");
            bottomView.setVisibility(View.VISIBLE);
        } else if (animation == topShow) {
            LogUtil.e("开始上部", "");
            topView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isRunning = false;
        if (animation == bottomDismiss) {
            LogUtil.e("隐藏下部", "");
            bottomView.setVisibility(View.GONE);
        }
        if (animation == topDismiss) {
            LogUtil.e("隐藏上部", "");
            topView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

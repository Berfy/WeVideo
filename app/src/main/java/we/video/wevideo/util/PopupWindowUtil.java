package we.video.wevideo.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import java.util.ArrayList;

import we.video.wevideo.R;
import we.video.wevideo.ui.support.touchGallery.adater.BasePagerAdapter;
import we.video.wevideo.ui.support.touchGallery.adater.UrlPagerAdapter;
import we.video.wevideo.ui.support.touchGallery.view.GalleryViewPager;

public class PopupWindowUtil {

    private boolean mIsRunning;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private TranslateAnimation mAnim_open = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
    private TranslateAnimation mAnim_close = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);

    public PopupWindowUtil(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(mContext);
        mAnim_open.setDuration(300);
        mAnim_close.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                mIsRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        mAnim_close.setDuration(300);
    }

    public void showPhotoPop(final View view, final OnPopPhotoListener onPopListener) {
        view.setEnabled(false);
        final View sportView = LayoutInflater.from(mContext).inflate(R.layout.pop_photo, null);
        mPopupWindow.setContentView(sportView);
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                view.setEnabled(true);
            }
        });
        sportView.findViewById(R.id.layout).startAnimation(mAnim_open);
        show(sportView, Gravity.CENTER);

        sportView.findViewById(R.id.bg).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!mIsRunning)
                            sportView.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        sportView.findViewById(R.id.btn_photo).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.photo();
                        if (!mIsRunning)
                            sportView.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        sportView.findViewById(R.id.btn_camera).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.camera();
                        if (!mIsRunning)
                            sportView.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
    }

    /**
     * 二选一对话框
     */
    public void showChoosePop(final View parent, String tip, String ok, String cancel, final OnPopListener onPopListener) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pop_choose, null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                view.setEnabled(true);
            }
        });
        view.findViewById(R.id.layout).startAnimation(mAnim_open);
        show(parent, Gravity.CENTER);

        ((TextView) view.findViewById(R.id.tv_tip)).setText(tip);
        ((Button) view.findViewById(R.id.btn_logout)).setText(ok);
        ((Button) view.findViewById(R.id.btn_cancel)).setText(cancel);

        view.findViewById(R.id.bg).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn_logout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.ok();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.cancel();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
    }

    /**
     * 二选一对话框
     */
    public void showSharePop(final View parent, String tip, String btn1, String btn2, String btn3, final OnShareListener onShareListener) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                view.setEnabled(true);
            }
        });
        view.findViewById(R.id.layout).startAnimation(mAnim_open);
        show(parent, Gravity.CENTER);

        ((TextView) view.findViewById(R.id.tv_tip)).setText(tip);
        ((Button) view.findViewById(R.id.btn1)).setText(btn1);
        ((Button) view.findViewById(R.id.btn2)).setText(btn2);
        ((Button) view.findViewById(R.id.btn3)).setText(btn3);

        view.findViewById(R.id.bg).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onShareListener.btn1();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onShareListener.btn2();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn3).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onShareListener.btn3();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
    }

    /**
     * 二选一提示对话框
     */
    public void showTipChoosePop(final View parent, String tip, String content, String ok, String cancel, final OnPopListener onPopListener) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pop_tip, null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                view.setEnabled(true);
            }
        });
        view.findViewById(R.id.layout).startAnimation(mAnim_open);
        show(parent, Gravity.CENTER);

        ((TextView) view.findViewById(R.id.tv_tip)).setText(tip);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ((Button) view.findViewById(R.id.btn_logout)).setText(ok);
        ((Button) view.findViewById(R.id.btn_cancel)).setText(cancel);

        view.findViewById(R.id.bg).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn_logout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.ok();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
        view.findViewById(R.id.btn_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPopListener.cancel();
                        if (!mIsRunning)
                            view.findViewById(R.id.layout).startAnimation(mAnim_close);
                    }
                });
    }

    /**
     * 加载提示
     */
    public void showLoadingPop(final View parent) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pop_loading, null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });
        show(parent, Gravity.CENTER);
    }

    public void showBigImage(View view, final boolean hasAnim, ArrayList<String> urls, int position) {
        final View popView = View.inflate(mContext, R.layout.activity_touch_gallery_layout, null);
        mPopupWindow.setContentView(popView);
        // 设置WheelViewPopWindow弹出窗体的高
        mPopupWindow.setWidth(LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        final GalleryViewPager viewPager = (GalleryViewPager) popView.findViewById(R.id.viewer);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(mContext);
        viewPager.setAdapter(pagerAdapter);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        LogUtil.e("位置", location[0] + "  " + location[1]);
        AnimationSet animationOpen = new AnimationSet(true);
        final AnimationSet animationClose = new AnimationSet(true);
        if (hasAnim) {
            animationOpen.addAnimation(new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
            animationOpen.addAnimation(new TranslateAnimation(location[0]
                    - DeviceUtil.getScreenWidth(mContext) / 2
                    + DeviceUtil.dip2px(mContext, 30), 0, location[1]
                    - DeviceUtil.getScreenHeight(mContext) / 2
                    + DeviceUtil.dip2px(mContext, 30), 0));
            animationOpen.setDuration(300);
            viewPager.startAnimation(animationOpen);
        }

        if (hasAnim) {
            LogUtil.e("位置", location[0] + "  " + location[1]);
            animationClose.addAnimation(new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
            animationClose.addAnimation(new TranslateAnimation(0, location[0]
                    - DeviceUtil.getScreenWidth(mContext) / 2
                    + DeviceUtil.dip2px(mContext, 30), 0, location[1]
                    - DeviceUtil.getScreenHeight(mContext) / 2
                    + DeviceUtil.dip2px(mContext, 30)));
            animationClose.setDuration(300);
            animationClose.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
        }
        if (null != urls) {
            viewPager.setOffscreenPageLimit(3);
            viewPager.setCurrentItem(position);
            pagerAdapter.getData().addAll(urls);
            pagerAdapter.notifyDataSetChanged();
            pagerAdapter.setOnItemClickListener(new BasePagerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int currentPosition) {
                    if (hasAnim) {
                        viewPager.startAnimation(animationClose);
                    } else {
                        dismiss();
                    }
                }
            });
            pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
                @Override
                public void onItemChange(int currentPosition) {
                }
            });
        }
        show(view, Gravity.CENTER);
    }

    public void show(View view, int gravity) {
        if (!mIsRunning) {
            LogUtil.e("判断显示", "===");
            dismiss();
            try {
                if (null != mPopupWindow && !mPopupWindow.isShowing()) {
                    LogUtil.e("显示", "===");
                    mPopupWindow.showAtLocation(view, gravity, 0, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        try {
            if (null != mPopupWindow && mPopupWindow.isShowing()) {//!((Activity) mContext).isFinishing() &&
                mPopupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPopupWindow.dismiss();
        }
    }

    public void dismiss(PopupWindow popupWindow) {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public interface OnPopPhotoListener {
        void photo();

        void camera();
    }

    public interface OnPopListener {
        void ok();

        void cancel();
    }

    public interface OnListPopListener {
        void select(int position, String content);
    }

    public interface OnShareListener {
        void btn1();

        void btn2();

        void btn3();
    }
}

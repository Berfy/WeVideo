package we.video.wevideo.ui.support;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import we.video.wevideo.R;
import we.video.wevideo.util.LogUtil;

/**
 * 1. 如何得知用户点击了哪个元素 2. 如何取得被点击元素的信息 3. 如何通过layout进行重绘绘制水波纹 4. 如果延迟up事件的分发
 */
public class WaveButton extends Button {

    private View mTargetTouchView;
    private Paint mHalfTransPaint;
    private Paint mTransPaint;
    private float[] mDownPositon;// 手指点击的坐标，也就是圆环的中心点
    private float mRawRadius;// 原始的圆环半径
    private float mDrawedRadius;// 正在慢慢绘制的圆环半径
    private final float mDefault_drawingDegrees = 100;//慢慢绘制圆环的时候，半径的递增百分比
    private float mDrawingRadiusDegrees = 0;//慢慢绘制圆环的时候，半径的递增百分比
    private static final long INVALID_DURATION = 5;
    private postUpEventDelayed mDelayedRunnable;
    private float mRawX, mRawY;
    private boolean mIsDraw;

    public void init() {
        mHalfTransPaint = new Paint();
        mHalfTransPaint.setColor(getResources().getColor(R.color.translate_black1));
        mHalfTransPaint.setAntiAlias(true);
        mTransPaint = new Paint();
        mTransPaint.setColor(Color.parseColor("#00ffffff"));
        mTransPaint.setAntiAlias(true);
        mDownPositon = new float[2];
        mDelayedRunnable = new postUpEventDelayed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDraw = true;
                mDelayedRunnable.event = null;
                mDrawingRadiusDegrees = mDefault_drawingDegrees;
                mTargetTouchView = null;
                mDrawedRadius = 0;
                mRawX = ev.getRawX();
                mRawY = ev.getRawY();
                mTargetTouchView = findTargetView(mRawX, mRawY, this);
                if (mTargetTouchView != null) {
                    RectF targetTouchRectF = getViewRectF(mTargetTouchView);
                    mDownPositon = getCircleCenterPostion(ev.getX(), ev.getY());
                    // 所要绘制的圆环的中心点
                    float circleCenterX = mDownPositon[0];
                    float circleCenterY = mDownPositon[1];
                    /**
                     * 圆环的半径： 圆环的中心点圆心当然是点击的那个点，但是半径是变化的
                     * 圆心可能落在mTargetTouchView区域的任意个方位之内，所以要想圆环绘制覆盖整个mTargetTouchView
                     * 则radius的取值为圆心的横坐标到mTargetTouchView的四个点的距离中的最大值
                     */
                    float left = circleCenterX - targetTouchRectF.left;
                    float right = targetTouchRectF.right - circleCenterX;
                    float top = circleCenterY - targetTouchRectF.top;
                    float bottom = targetTouchRectF.bottom - circleCenterY;
                    // 计算出最大的值则为半径
                    mRawRadius = Math.max(bottom, Math.max(Math.max(left, right), top));
                    LogUtil.e("中心", circleCenterX + " " + circleCenterY + "   最大半径" + mRawRadius);
                    postInvalidate();
                }
                LogUtil.e("onTouch", "按下" + ev.getRawX() + "," + ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("onTouch", "移动" + ev.getRawX() + "," + ev.getRawY());
                if (Math.abs(mRawX - ev.getRawX()) > 50 || Math.abs(mRawY - ev.getRawY()) > 50) {
                    mIsDraw = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
//                mIsCanDraw = false;
                LogUtil.e("onTouch", "取消" + ev.getX() + "," + ev.getY());
                mIsDraw = false;
                break;
            case MotionEvent.ACTION_UP:
                // 需要让波纹绘制完毕后再执行在up中执行的方法
                if (mDrawedRadius == 0) {
                    return false;
                }
                mDrawingRadiusDegrees /= 5;
                LogUtil.e("onTouch", "抬起" + ev.getX() + "," + ev.getY());
                mDelayedRunnable.event = ev;
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private OnClickListener onClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        onClickListener = l;
    }

    class postUpEventDelayed implements Runnable {
        private MotionEvent event;

        @Override
        public void run() {
            if (mTargetTouchView != null && mTargetTouchView.isClickable()
                    && event != null) {
                LogUtil.e("onTouch", "点击" + event.getX() + "," + event.getY());
                mTargetTouchView.dispatchTouchEvent(event);
                mIsDraw = false;
                onClickListener.onClick(mTargetTouchView);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        /**
         * 绘制完子元素后开始绘制波纹
         */
        if (mTargetTouchView != null) {
            RectF clipRectF = clipRectF(mTargetTouchView);
            canvas.save();
            // 为了不让绘制的圆环超出所要绘制的范围
            canvas.clipRect(clipRectF);
            if (mIsDraw) {
                if (mDrawedRadius < mRawRadius) {
                    LogUtil.e("画ing", "");
                    mDrawedRadius += mRawRadius / mDrawingRadiusDegrees;
                    canvas.drawCircle(mDownPositon[0], mDownPositon[1], mDrawedRadius, mHalfTransPaint);
                    postInvalidateDelayed(INVALID_DURATION);
                } else {
                    LogUtil.e("画完", "");
                    canvas.drawCircle(mDownPositon[0], mDownPositon[1], mDrawedRadius, mTransPaint);
                    post(mDelayedRunnable);
                }
            } else {
                canvas.drawCircle(mDownPositon[0], mDownPositon[1], mDrawedRadius, mTransPaint);
            }
            canvas.restore();
        }
    }

    /**
     * 获取圆环的中心坐标
     */
    public float[] getCircleCenterPostion(float x, float y) {
        int[] location = new int[2];
        float[] mDownPositon = new float[2];
        getLocationOnScreen(location);
        mDownPositon[0] = x;
        mDownPositon[1] = y;
        return mDownPositon;
    }

    /**
     * 获取要剪切的区域
     *
     * @param mTargetTouchView
     * @return
     */

    public RectF clipRectF(View mTargetTouchView) {
        RectF clipRectF = getViewRectF(mTargetTouchView);
        int[] location = new int[2];
        getLocationOnScreen(location);
        clipRectF.top -= location[1];
        clipRectF.bottom -= location[1];
        return clipRectF;
    }

    /**
     * 寻找目标view
     *
     * @param x
     * @param y
     * @param anchorView 从哪个view开始往下寻找
     * @return
     */
    public View findTargetView(float x, float y, View anchorView) {
        ArrayList<View> touchablesView = anchorView.getTouchables();
        View targetView = null;
        for (View child : touchablesView) {
            RectF rectF = getViewRectF(child);
            if (rectF.contains(x, y) && child.isClickable()) {
                LogUtil.e("点击View找到", "");
                // 这说明被点击的view找到了
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    public RectF getViewRectF(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int childLeft = 0;
        int childTop = location[1];
        int childRight = view.getMeasuredWidth();
        int childBottom = childTop + view.getMeasuredHeight();
        return new RectF(childLeft, childTop, childRight, childBottom);
    }

    public WaveButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public WaveButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveButton(Context context) {
        this(context, null, 0);
    }

}

package we.video.wevideo.ui.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Process;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import we.video.wevideo.cons.Constants;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/7/21.
 */
public class NewView1 extends LinearLayout {

    private Context mContext;
    private int mDegree1 = 180, mDegree2 = 0, mDegree3 = 360;
    private int mRadio = 30;
    private int mStokeWidth = 15;
    private int mCenterX1, mCenterY1, mCenterX2, mCenterY2;//两个圆中心坐标
    private int mCircleType1 = 1;//1 左上  2右下  3右上  4左下
    private int mCircleType2 = 4;//1 左上  2右下  3右上  4左下
    private int mCircleType3 = 3;//1 左上  2右下  3右上  4左下
    private boolean mStopStatus1, mStopStatus2, mStopStatus3;//是否暂停
    private int mSpeed = 4, mSpeed1 = 2;
    private boolean mIsRunning;
    private Paint mRectPaint, mPaint1, mPaint2, mPaint3;
    private int mTime180, mTime1, mTime2, mTime3;
    private int[] mStartTimes, mEndTimes;

    public NewView1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public NewView1(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
    }

    public void startAnim() {
        config();
        mIsRunning = true;
        mDegree1 = 180;
        mDegree2 = 0;
        mDegree3 = 360;
        mCircleType1 = 1;//1 左上  2右下  3右上  4左下
        mCircleType2 = 4;//1 左上  2右下  3右上  4左下
        mCircleType3 = 3;//1 左上  2右下  3右上  4左下
        Constants.EXECUTOR1.execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                while (!((Activity) mContext).isFinishing() && mIsRunning) {
                    if (mCircleType1 == 1) {
                        if (!mStopStatus1)
                            mDegree1 += mSpeed1;
                        if (mDegree1 >= 360) {
                            mDegree1 = 180;
                            mCircleType1 = 2;
                        }
                    } else if (mCircleType1 == 2) {
                        if (!mStopStatus1)
                            mDegree1 -= mSpeed1;
                        if (mDegree1 <= 0) {
                            mDegree1 = 360;
                            mCircleType1 = 3;
                        }
                    } else if (mCircleType1 == 3) {
                        if (!mStopStatus1)
                            mDegree1 -= mSpeed1;
                        if (mDegree1 <= 180) {
                            mDegree1 = 0;
                            mCircleType1 = 4;
                        }
                    } else if (mCircleType1 == 4) {
                        if (!mStopStatus1)
                            mDegree1 += mSpeed1;
                        if (mDegree1 >= 180) {
                            mCircleType1 = 1;
                        }
                    }
                    if (mCircleType2 == 1) {
                        if (!mStopStatus2)
                            mDegree2 += mSpeed1;
                        if (mDegree2 >= 360) {
                            mDegree2 = 180;
                            mCircleType2 = 2;
                        }
                    } else if (mCircleType2 == 2) {
                        if (!mStopStatus2)
                            mDegree2 -= mSpeed1;
                        if (mDegree2 <= 0) {
                            mDegree2 = 360;
                            mCircleType2 = 3;
                        }
                    } else if (mCircleType2 == 3) {
                        if (!mStopStatus2)
                            mDegree2 -= mSpeed1;
                        if (mDegree2 <= 180) {
                            mDegree2 = 0;
                            mCircleType2 = 4;
                        }
                    } else if (mCircleType2 == 4) {
                        if (!mStopStatus2)
                            mDegree2 += mSpeed1;
                        if (mDegree2 >= 180) {
                            mCircleType2 = 1;
                        }
                    }
                    if (mCircleType3 == 1) {
                        if (!mStopStatus3)
                            mDegree3 += mSpeed1;
                        if (mDegree3 >= 360) {
                            mDegree3 = 180;
                            mCircleType3 = 2;
                        }
                    } else if (mCircleType3 == 2) {
                        if (!mStopStatus3)
                            mDegree3 -= mSpeed1;
                        if (mDegree3 <= 0) {
                            mDegree3 = 360;
                            mCircleType3 = 3;
                        }
                    } else if (mCircleType3 == 3) {
                        if (!mStopStatus3)
                            mDegree3 -= mSpeed1;
                        if (mDegree3 <= 180) {
                            mDegree3 = 0;
                            mCircleType3 = 4;
                        }
                    } else if (mCircleType3 == 4) {
                        if (!mStopStatus3)
                            mDegree3 += mSpeed1;
                        if (mDegree3 >= 180) {
                            mCircleType3 = 1;
                        }
                    }
                    for (int s : mStartTimes) {
                        if (s == mTime1) {
                            LogUtil.e("继续", mTime1 + "");
                            mStopStatus1 = false;//继续S
                        }
                        if (s == mTime2) {
                            LogUtil.e("继续", mTime2 + "");
                            mStopStatus2 = false;//继续S
                        }
                        if (s == mTime3) {
                            LogUtil.e("继续", mTime3 + "");
                            mStopStatus3 = false;//继续S
                        }
                    }
                    for (int s : mEndTimes) {
                        if (s == mTime1) {
                            LogUtil.e("暂停", mTime1 + "");
                            mStopStatus1 = true;//暂停
                        }
                        if (s == mTime2) {
                            LogUtil.e("暂停", mTime2 + "");
                            mStopStatus2 = true;//暂停
                        }
                        if (s == mTime3) {
                            LogUtil.e("暂停", mTime3 + "");
                            mStopStatus3 = true;//暂停
                        }
                    }

                    if (mTime1 == mTime180 * 9) {
                        mTime1 = 0;
                    }
                    if (mTime2 == mTime180 * 10) {
                        mTime2 = mTime180 * 1;
                    }
                    if (mTime3 == mTime180 * 8) {
                        mTime3 = mTime180 * 2;
                    }
                    mTime1 += mSpeed;
                    mTime2 += mSpeed;
                    mTime3 += mSpeed;

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postInvalidate();
                        }
                    });
                    try {
                        Thread.sleep(mSpeed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void stopAnim() {
        mIsRunning = false;
    }

    private void config() {
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.RED);
        mRectPaint.setStrokeWidth(1);

        mPaint1 = new Paint();
        mPaint1.setColor(Color.WHITE);
        mPaint1.setAntiAlias(true);
        mPaint1.setStrokeWidth(2);
        mPaint1.setStyle(Paint.Style.FILL);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeWidth(2);
        mPaint2.setStyle(Paint.Style.FILL);

        mPaint3 = new Paint();
        mPaint3.setColor(Color.WHITE);
        mPaint3.setAntiAlias(true);
        mPaint3.setStrokeWidth(2);
        mPaint3.setStyle(Paint.Style.FILL);
        mTime180 = mSpeed * 180 / mSpeed1;
        LogUtil.e("180度时间", mTime180 + "");
        mTime1 = 0;
        mTime2 = mTime180 * 1;
        mTime3 = mTime180 * 2;
        mStartTimes = new int[]{mTime180 * 3, mTime180 * 6, mTime180 * 9};
        mEndTimes = new int[]{mTime180 * 2, mTime180 * 5, mTime180 * 8};
        post(new Runnable() {
            @Override
            public void run() {
                mRadio = getWidth() / 5;
                mStokeWidth = mRadio / 2;
                mCenterX1 = mRadio + mRadio / 2;
                mCenterY1 = getHeight() / 2;
                mCenterX2 = mCenterX1 + mRadio * 2;
                mCenterY2 = mCenterY1;
                LogUtil.e("当前视图宽高", getWidth() + " " + getHeight() + " mCenterX1" + mCenterX1 + ",mCenterY1" + mCenterY1 + "  mCenterX2" + mCenterX2 + ",mCenterY2" + mCenterY2);
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        LogUtil.e("刷新", mDegree1 + "");
//        Rect rect = new Rect();
//        rect.left = 0;
//        rect.top = 0;
//        rect.right = getWidth();
//        rect.bottom = getHeight();
//        canvas.drawRect(rect, mRectPaint);
        int centermCenterX1, centermCenterY1, centermCenterX2, centermCenterY2, centerX3, centerY3;
        if (mCircleType1 == 1 || mCircleType1 == 4) {
            centermCenterX1 = mCenterX1;
            centermCenterY1 = mCenterY1;
        } else {
            centermCenterX1 = mCenterX2;
            centermCenterY1 = mCenterY2;
        }
        if (mCircleType2 == 1 || mCircleType2 == 4) {
            centermCenterX2 = mCenterX1;
            centermCenterY2 = mCenterY1;
        } else {
            centermCenterX2 = mCenterX2;
            centermCenterY2 = mCenterY2;
        }
        if (mCircleType3 == 1 || mCircleType3 == 4) {
            centerX3 = mCenterX1;
            centerY3 = mCenterY1;
        } else {
            centerX3 = mCenterX2;
            centerY3 = mCenterY2;
        }

        canvas.drawCircle((float) (centermCenterX1 + mRadio
                * Math.cos(2 * Math.PI / 360 * mDegree1)), (float) (centermCenterY1 + mRadio
                * Math.sin(2 * Math.PI / 360 * mDegree1)), mStokeWidth, mPaint1);

        canvas.drawCircle((float) (centermCenterX2 + mRadio
                * Math.cos(2 * Math.PI / 360 * mDegree2)), (float) (centermCenterY2 + mRadio
                * Math.sin(2 * Math.PI / 360 * mDegree2)), mStokeWidth, mPaint2);

        canvas.drawCircle((float) (centerX3 + mRadio
                * Math.cos(2 * Math.PI / 360 * mDegree3)), (float) (centerY3 + mRadio
                * Math.sin(2 * Math.PI / 360 * mDegree3)), mStokeWidth, mPaint3);

        canvas.restore();
//        postInvalidateDelayed(drawDuring);
    }
}

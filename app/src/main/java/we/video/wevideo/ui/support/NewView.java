package we.video.wevideo.ui.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import we.video.wevideo.cons.Constants;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Administrator on 2016/7/21.
 */
public class NewView extends LinearLayout {

    private Context context;
    private int degree1 = 180, degree2 = 0, degree3 = 0;
    private int radio = 50;
    private int stoke = 30;
    private int x1, y1, x2, y2;//两个圆中心坐标
    private int circleType1 = 1;//1 顺时针1  2顺时针2  3逆时针1  4逆时针2
    private int circleType2 = 3;//1 顺时针1  2顺时针2  3逆时针1  4逆时针2
    private int circleType3 = 4;//1 顺时针1  2顺时针2  3逆时针1  4逆时针2
    private boolean status1, status2, status3;//是否暂停
    private int speed = 6;
    private int add = 2;
    private Paint circlePaint1, circlePaint2, circlePaint3;
    private int totalTime1, totalTime2, totalTime3;

    public NewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public NewView(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        x1 = radio * 2;
        y1 = radio * 2;
        x2 = radio * 2 * 2;
        y2 = radio * 2;
        final int time = speed * 180 / 2;
        totalTime1 = 0;
        totalTime2 = time * 1;
        totalTime3 = time * 2;
        final int[] gos = new int[]{time * 3, time * 6, time * 9};
        final int[] stops = new int[]{time * 2, time * 5, time * 8};
        LogUtil.e("180度时间", time + "");
//        postInvalidateDelayed(drawDuring);
        Constants.EXECUTOR1.execute(new Runnable() {
            @Override
            public void run() {
                while (!((Activity) context).isFinishing()) {
//                    LogUtil.e("时间", "" + totalTime);
//                    LogUtil.e("角度", degree1 + "");
                    if (circleType1 == 1) {
                        if (!status1)
                            degree1 += add;
                        if (degree1 == 360) {
                            degree1 = 180;
                            circleType1 = 2;
                        }
                    } else if (circleType1 == 2) {
                        if (!status1)
                            degree1 += add;
                        if (degree1 == 360) {
                            degree1 = 0;
                            circleType1 = 4;
                        }
                    } else if (circleType1 == 3) {
                        if (!status1)
                            degree1 += add;
                        if (degree1 == 180) {
                            circleType1 = 1;
                        }
                    } else if (circleType1 == 4) {
                        if (!status1)
                            degree1 += add;
                        if (degree1 == 180) {
                            degree1 = 0;
                            circleType1 = 3;
                        }
                    }

                    if (circleType2 == 1) {
                        if (!status2)
                            degree2 += add;
                        if (degree2 == 360) {
                            degree2 = 180;
                            circleType2 = 2;
                        }
                    } else if (circleType2 == 2) {
                        if (!status2)
                            degree2 += add;
                        if (degree2 == 360) {
                            degree2 = 0;
                            circleType2 = 4;
                        }
                    } else if (circleType2 == 3) {
                        if (!status2)
                            degree2 += add;
                        if (degree2 == 180) {
                            circleType2 = 1;
                        }
                    } else if (circleType2 == 4) {
                        if (!status2)
                            degree2 += add;
                        if (degree2 == 180) {
                            degree2 = 0;
                            circleType2 = 3;
                        }
                    }

                    if (circleType3 == 1) {
                        if (!status3)
                            degree3 += add;
                        if (degree3 == 360) {
                            degree3 = 180;
                            circleType3 = 2;
                        }
                    } else if (circleType3 == 2) {
                        if (!status3)
                            degree3 += add;
                        if (degree3 == 360) {
                            degree3 = 0;
                            circleType3 = 4;
                        }
                    } else if (circleType3 == 3) {
                        if (!status3)
                            degree3 += add;
                        if (degree3 == 180) {
                            circleType3 = 1;
                        }
                    } else if (circleType3 == 4) {
                        if (!status3)
                            degree3 += add;
                        if (degree3 == 180) {
                            degree3 = 0;
                            circleType3 = 3;
                        }
                    }

                    for (int s : gos) {
                        if (s == totalTime1) {
                            LogUtil.e("继续", totalTime1 + "");
                            status1 = false;//继续S
                        }
                        if (s == totalTime2) {
                            LogUtil.e("继续", totalTime2 + "");
                            status2 = false;//继续S
                        }
                        if (s == totalTime3) {
                            LogUtil.e("继续", totalTime3 + "");
                            status3 = false;//继续S
                        }
                    }
                    for (int s : stops) {
                        if (s == totalTime1) {
                            LogUtil.e("暂停", totalTime1 + "");
                            status1 = true;//暂停
                        }
                        if (s == totalTime2) {
                            LogUtil.e("暂停", totalTime2 + "");
                            status2 = true;//暂停
                        }
                        if (s == totalTime3) {
                            LogUtil.e("暂停", totalTime3 + "");
                            status3 = true;//暂停
                        }
                    }

                    if (totalTime1 == time * 9) {
                        totalTime1 = 0;
                    }
                    if (totalTime2 == time * 10) {
                        totalTime2 = time * 1;
                    }
                    if (totalTime3 == time * 8) {
                        totalTime3 = time * 2;
                    }
                    totalTime1 += speed;
                    totalTime2 += speed;
                    totalTime3 += speed;

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postInvalidate();
                        }
                    });
                    try {
                        Thread.sleep(speed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        circlePaint1 = new Paint();
        circlePaint1.setColor(Color.BLUE);
        circlePaint1.setAntiAlias(true);
        circlePaint1.setStrokeWidth(2);
        circlePaint1.setStyle(Paint.Style.FILL);

        circlePaint2 = new Paint();
        circlePaint2.setColor(Color.GREEN);
        circlePaint2.setAntiAlias(true);
        circlePaint2.setStrokeWidth(2);
        circlePaint2.setStyle(Paint.Style.FILL);

        circlePaint3 = new Paint();
        circlePaint3.setColor(Color.YELLOW);
        circlePaint3.setAntiAlias(true);
        circlePaint3.setStrokeWidth(2);
        circlePaint3.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
//        LogUtil.e("刷新", degree1 + "");
        int centerX1, centerY1, centerX2, centerY2, centerX3, centerY3;
        if (circleType1 == 1 || circleType1 == 3) {
            centerX1 = x1;
            centerY1 = y1;
        } else {
            centerX1 = x2;
            centerY1 = y2;
        }
        if (circleType2 == 1 || circleType2 == 3) {
            centerX2 = x1;
            centerY2 = y1;
        } else {
            centerX2 = x2;
            centerY2 = y2;
        }
        if (circleType3 == 1 || circleType3 == 3) {
            centerX3 = x1;
            centerY3 = y1;
        } else {
            centerX3 = x2;
            centerY3 = y2;
        }

        canvas.drawCircle((float) (centerX1 + radio
                * Math.cos(2 * Math.PI / 360 * degree1)), (float) (centerY1 + radio
                * Math.sin(2 * Math.PI / 360 * degree1)), stoke, circlePaint1);

        canvas.drawCircle((float) (centerX2 + radio
                * Math.cos(2 * Math.PI / 360 * degree2)), (float) (centerY2 + radio
                * Math.sin(2 * Math.PI / 360 * degree2)), stoke, circlePaint2);

        canvas.drawCircle((float) (centerX3 + radio
                * Math.cos(2 * Math.PI / 360 * degree3)), (float) (centerY3 + radio
                * Math.sin(2 * Math.PI / 360 * degree3)), stoke, circlePaint3);

        canvas.restore();
//        postInvalidateDelayed(drawDuring);
    }
}

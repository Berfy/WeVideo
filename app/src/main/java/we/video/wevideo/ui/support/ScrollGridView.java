package we.video.wevideo.ui.support;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class ScrollGridView extends GridView {

    private int oldY;

    private OnScrollListener onScrollListener;

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != onScrollListener) {
            int y = getTrueScrollY();
            onScrollListener.onScroll(y, oldY);
            if (y != oldY) {
                oldY = y;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public int getTrueScrollY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    public interface OnScrollListener {
        void onScroll(int y, int oldY);
    }
}

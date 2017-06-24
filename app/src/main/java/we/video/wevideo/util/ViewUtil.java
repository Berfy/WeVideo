package we.video.wevideo.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuepengfei
 * @category View简单堆栈管理
 */
public class ViewUtil implements ViewUtilInterface {

    private List<View> mViews;

    @Override
    public void addView(View view, ViewGroup layout) {
        layout.addView(view);
    }

    @Override
    public void updateView(Context context, View view, ViewGroup layout) {
        boolean isHas = false;
        if (null == mViews) {
            mViews = new ArrayList<View>();
        }

        if (mViews.size() > 0) {
            for (int i = 0; i < mViews.size(); i++) {
                if (view == mViews.get(i)) {
                    isHas = true;
                    LogUtil.e("显示", "");
                    mViews.get(i).setVisibility(View.VISIBLE);
                } else {
                    LogUtil.e("隐藏", "");
                    mViews.get(i).setVisibility(View.GONE);
                }
            }
        }

        if (!isHas) {
            LogUtil.e("没有就增加", "");
            layout.addView(view);
            mViews.add(view);
        }
    }

    public int getViewNum() {
        return null == mViews ? 0 : mViews.size();
    }

    public void removeTopView(ViewGroup layout) {
        if (null != mViews) {
            if (mViews.size() > 1) {
                layout.removeView(mViews.get(mViews.size() - 1));
                mViews.remove(mViews.size() - 1);
                mViews.get(mViews.size() - 1).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void removeView(View view, ViewGroup layout) {
        layout.removeView(view);
        mViews.remove(view);
    }

    @Override
    public void removeAllviews(ViewGroup layout) {
        layout.removeAllViews();
        mViews.clear();

    }
}

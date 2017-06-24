package we.video.wevideo.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @category View基类
 * @author yuepengfei
 * */
public interface ViewUtilInterface {
	public void addView(View view, ViewGroup layout);

	public void updateView(Context context, View view, ViewGroup layout);

	public void removeView(View view, ViewGroup layout);

	public void removeAllviews(ViewGroup layout);
}

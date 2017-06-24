package we.video.wevideo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.bean.Video;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑视频列表
 */
public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<Video> data = new ArrayList<>();
    private OnVideoSelectListener onVideoSelectListener;
    private int selectPosition = -1;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    public void setOnVideoSelectListener(OnVideoSelectListener onVideoSelectListener) {
        this.onVideoSelectListener = onVideoSelectListener;
        notifyDataSetChanged();
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public List<Video> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.adapter_video, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Video video = data.get(position);
        if (position == selectPosition) {
            holder.layout_play.setVisibility(View.VISIBLE);
        } else {
            holder.layout_play.setVisibility(View.GONE);
        }
        holder.tv_title.setText(video.getTitle());
        ImageUtil.getInstances().displayDefault(holder.iv_icon, video.getThumb());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("点击", "点击1");
                if (null != onVideoSelectListener) {
                    LogUtil.e("点击", "点击2");
                    selectPosition = position;
                    ((AbsListView) parent).setSmoothScrollbarEnabled(true);
                    ((AbsListView) parent).smoothScrollToPosition(position);
                    onVideoSelectListener.select(video);
                }
            }
        });
        return convertView;
    }

    class Holder {
        LinearLayout layout_bg, layout_play;
        TextView tv_title;
        ImageView iv_icon;

        Holder(View v) {
            layout_bg = (LinearLayout) v.findViewById(R.id.layout_bg);
            layout_play = (LinearLayout) v.findViewById(R.id.layout_play);
            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
        }
    }

    public interface OnVideoSelectListener {
        void select(Video video);
    }
}

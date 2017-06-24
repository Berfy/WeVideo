package we.video.wevideo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.bean.Special;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑列表
 */
public class SpecialAdapter extends BaseAdapter {

    private Context context;
    private OnSpeicalSelectListener onSpeicalSelectListener;
    private List<Special> data = new ArrayList<>();
    private float x, y;
    private long clickTime;
    private int width;

    public SpecialAdapter(Context context, OnSpeicalSelectListener onSpeicalSelectListener) {
        this.context = context;
        this.onSpeicalSelectListener = onSpeicalSelectListener;
        width = DeviceUtil.getScreenWidth(context);
    }

    public List<Special> getData() {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.adapter_index, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Special special = data.get(position);
        holder.iv_bg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width / 2.4)));
        ImageUtil.getInstances().displayDefault(holder.iv_bg, special.getThumb());
        if (null == special.getVideo()) {
            holder.tv_special_name.setVisibility(View.GONE);
        } else {
            holder.tv_special_name.setVisibility(View.VISIBLE);
            holder.tv_special_name.setText(special.getVideo().getTitle());
        }
        holder.tv_name.setText(special.getTitle());
        holder.tv_name.getPaint().setFakeBoldText(true);
        holder.tv_time.setText(special.getCreateTime());
        holder.tv_time.getPaint().setFakeBoldText(true);
        holder.layout_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width / 2.4)));
        holder.layout_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("点击", "");
                if (null != onSpeicalSelectListener) {
                    onSpeicalSelectListener.onSelect(special);
                }
            }
        });
        return convertView;
    }

    class Holder {
        LinearLayout layout_bg, layout_center;
        ImageView iv_bg;
        TextView tv_special_name, tv_name, tv_time;

        Holder(View v) {
            layout_center = (LinearLayout) v.findViewById(R.id.layout_center);
            layout_bg = (LinearLayout) v.findViewById(R.id.layout_bg);
            iv_bg = (ImageView) v.findViewById(R.id.iv_bg);
            tv_special_name = (TextView) v.findViewById(R.id.tv_special_name);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
        }
    }

    public interface OnSpeicalSelectListener {
        void onSelect(Special special);
    }
}

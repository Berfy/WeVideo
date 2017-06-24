package we.video.wevideo.adapter;

import android.content.Context;
import android.content.Intent;
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
import we.video.wevideo.bean.Collect;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.special.AuthorActivity;
import we.video.wevideo.ui.special.SpecialActivity;
import we.video.wevideo.ui.support.swipe.SwipeListView;
import we.video.wevideo.util.ImageUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 收藏列表
 */
public class CollectAdapter extends BaseAdapter {

    private Context context;
    private List<Collect> data = new ArrayList<>();
    private int rightWidth;

    public CollectAdapter(Context context, int rightWidth) {
        this.context = context;
        this.rightWidth = rightWidth;
    }

    public List<Collect> getData() {
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
            convertView = View.inflate(context, R.layout.adapter_collect, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Collect collect = data.get(position);
        ImageUtil.getInstances().displayDefault(holder.iv_icon, collect.getThumb());
        holder.tv_title.setText(context.getString(R.string.collect) + collect.getTypeName());
        holder.tv_content.setText(collect.getTitle());
        holder.tv_time.setText(collect.getCreateTime());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.layout_detele.setLayoutParams(layoutParams);
        holder.layout_detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerApi.getInstances().collect(0, collect.getType(), collect.getTypeId(), new RequestCallBack<String>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void finish(NetResponse<String> result) {
                        if (result.netMsg.code == 1) {
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onProgress(float percent) {

                    }
                });
                ((SwipeListView) parent).closeRight();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if ("0".equals(collect.getType())) {
                    intent = new Intent(context, SpecialActivity.class);
                    intent.putExtra("id", collect.getTypeId());
                } else {
                    intent = new Intent(context, AuthorActivity.class);
                    intent.putExtra("id", collect.getTypeId());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class Holder {
        TextView tv_title, tv_content, tv_time, tv_delete;
        ImageView iv_icon;
        RelativeLayout layout_detele;

        Holder(View v) {
            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_delete = (TextView) v.findViewById(R.id.tv_delete);
            layout_detele = (RelativeLayout) v.findViewById(R.id.layout_detele);
        }
    }
}

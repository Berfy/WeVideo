package we.video.wevideo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.util.ImageUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 评论列表
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> data = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public List<Comment> getData() {
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
            convertView = View.inflate(context, R.layout.adapter_comment, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Comment comment = data.get(position);
        ImageUtil.getInstances().displayDefault(holder.iv_icon, comment.getImg());
        holder.tv_name.setText(comment.getName());
        holder.tv_content.setText(comment.getContent());
        holder.tv_time.setText(comment.getCreateTime());
        return convertView;
    }

    class Holder {
        TextView tv_name, tv_content, tv_time;
        ImageView iv_icon;

        Holder(View v) {
            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
        }
    }
}

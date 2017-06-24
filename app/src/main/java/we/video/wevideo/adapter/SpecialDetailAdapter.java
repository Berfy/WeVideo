package we.video.wevideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.bean.Comment;
import we.video.wevideo.bean.Special;
import we.video.wevideo.cons.UserTemp;
import we.video.wevideo.dao.ServerApi;
import we.video.wevideo.net.NetResponse;
import we.video.wevideo.net.RequestCallBack;
import we.video.wevideo.ui.support.round.RoundImageView;
import we.video.wevideo.ui.special.AuthorActivity;
import we.video.wevideo.ui.special.ProductGridView;
import we.video.wevideo.ui.special.VideoGridView;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.ShareUtil;
import we.video.wevideo.util.ViewUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 专辑列表
 */
public class SpecialDetailAdapter extends BaseAdapter {

    private Context context;
    private Special special;
    private List<Comment> comments = new ArrayList<>();
    private boolean isSpecial = true;
    private PopupWindowUtil popupWindowUtil;
    private ViewUtil viewUtil;
    private OnCommentListener onCommentListener;
    private VideoAdapter.OnVideoSelectListener onVideoSelectListener;

    public SpecialDetailAdapter(Context context, OnCommentListener onCommentListener, VideoAdapter.OnVideoSelectListener onVideoSelectListener) {
        this.context = context;
        this.onCommentListener = onCommentListener;
        this.onVideoSelectListener = onVideoSelectListener;
        popupWindowUtil = new PopupWindowUtil(context);
        viewUtil = new ViewUtil();
    }

    public void setData(Special special) {
        this.special = special;
    }

    public List<Comment> getData() {
        return comments;
    }

    @Override
    public int getCount() {
        return comments.size() + 1;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                final Holder1 holder1;
                if (null == convertView) {
                    convertView = View.inflate(context, R.layout.adapter_special_info, null);
                    holder1 = new Holder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (Holder1) convertView.getTag();
                }
                if (null != special) {
                    holder1.tv_special_name.setText(special.getTitle());
                    holder1.tv_author_name.setText(special.getAuthor().getName());
                    if(null!= special.getVideo()){
                        holder1.tv_read_num.setText(special.getVideo().getReadNum() + "");
                        holder1.tv_video_title.setText(special.getVideo().getTitle());
                    }
                    if (isSpecial) {
                        holder1.btn_special.setBackgroundResource(R.drawable.bg_shape_rect_green_btn_normal);
                        holder1.btn_product.setBackgroundResource(R.drawable.bg_shape_rect_green_btn_press);
                        holder1.videoGridView.setSpecialId(special.getId());
                        holder1.videoGridView.setOnVideoSelectListener(onVideoSelectListener);
                        viewUtil.updateView(context, holder1.videoGridView.getView(), holder1.layout_content);
                    } else {
                        holder1.btn_special.setBackgroundResource(R.drawable.bg_shape_rect_green_btn_press);
                        holder1.btn_product.setBackgroundResource(R.drawable.bg_shape_rect_green_btn_normal);
                        if(null!= special.getVideo()){
                            holder1.productGridView.setData(special.getVideo().getId());
                        }
                        viewUtil.updateView(context, holder1.productGridView.getView(), holder1.layout_content);
                    }

                    holder1.btn_collect.setText(special.getIsCollect() == 1 ? context.getString(R.string.hascollect) : context.getString(R.string.collect));
                    holder1.btn_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UserTemp.getInstances(context).isLogin()) {
                                if (null != special) {
                                    final int isCollect = special.getIsCollect() == 0 ? 1 : 0;
                                    ServerApi.getInstances().collect(isCollect, "0", special.getId(), new RequestCallBack<String>() {
                                        @Override
                                        public void start() {

                                        }

                                        @Override
                                        public void finish(NetResponse<String> result) {
                                            if (result.netMsg.code == 1) {
                                                special.setIsCollect(isCollect);
                                                notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onProgress(float percent) {

                                        }
                                    });
                                }
                            } else {
                                DeviceUtil.toLogin(context, true);
                            }
                        }
                    });
                    holder1.btn_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindowUtil.showSharePop(holder1.btn_share, "分享到哪里", "微信好友", "微信朋友圈", "新浪微博", new PopupWindowUtil.OnShareListener() {
                                @Override
                                public void btn1() {
                                    ShareUtil.getInstances(context).wx("送给亲爱的", "老婆打开看", "http://120.25.197.142:8080/WeVideo/");
                                }

                                @Override
                                public void btn2() {
                                    ShareUtil.getInstances(context).wxCirCle("送给亲爱的", "老婆打开看", "http://120.25.197.142:8080/WeVideo/");
                                }

                                @Override
                                public void btn3() {
                                    ShareUtil.getInstances(context).sina("送给亲爱的", "老婆打开看", "http://120.25.197.142:8080/WeVideo/");
                                }
                            });
                        }
                    });
                    holder1.btn_special.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isSpecial) {
                                isSpecial = true;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    holder1.btn_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isSpecial) {
                                isSpecial = false;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    holder1.btn_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != onCommentListener) {
                                onCommentListener.commentClick();
                            }
                        }
                    });
                    holder1.iv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AuthorActivity.class);
                            intent.putExtra("id", special.getAuthor().getUserId());
                            context.startActivity(intent);
                        }
                    });
                    ImageUtil.getInstances().displayDefault(holder1.iv_icon, special.getAuthor().getImg());
                }
                break;
            case 1:
                Holder2 holder2;
                if (null == convertView) {
                    convertView = View.inflate(context, R.layout.adapter_comment, null);
                    holder2 = new Holder2(convertView);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (Holder2) convertView.getTag();
                }
                Comment comment = comments.get(position - 1);
                ImageUtil.getInstances().displayDefault(holder2.iv_icon, comment.getImg());
                holder2.tv_name.setText(comment.getName());
                holder2.tv_content.setText(comment.getContent());
                holder2.tv_time.setText(comment.getCreateTime());
                break;
        }
        return convertView;
    }

    class Holder1 {
        TextView tv_special_name, tv_read_num, tv_video_title, tv_author_name;
        RoundImageView iv_icon;
        Button btn_share, btn_collect, btn_special, btn_product, btn_comment;
        LinearLayout layout_content;
        VideoGridView videoGridView;
        ProductGridView productGridView;

        Holder1(View v) {
            iv_icon = (RoundImageView) v.findViewById(R.id.iv_icon);
            tv_video_title = (TextView) v.findViewById(R.id.tv_video_title);
            tv_special_name = (TextView) v.findViewById(R.id.tv_special_name);
            tv_read_num = (TextView) v.findViewById(R.id.tv_read_num);
            tv_author_name = (TextView) v.findViewById(R.id.tv_author_name);
            btn_share = (Button) v.findViewById(R.id.btn_share);
            btn_collect = (Button) v.findViewById(R.id.btn_collect);
            btn_special = (Button) v.findViewById(R.id.btn_special);
            btn_product = (Button) v.findViewById(R.id.btn_product);
            btn_comment = (Button) v.findViewById(R.id.btn_comment);
            layout_content = (LinearLayout) v.findViewById(R.id.layout_content);
            videoGridView = new VideoGridView(context);
            videoGridView.setSelectPostion(0);
            productGridView = new ProductGridView(context);
        }
    }

    class Holder2 {
        TextView tv_name, tv_content, tv_time;
        ImageView iv_icon;

        Holder2(View v) {
            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
        }
    }

    public interface OnCommentListener {
        void commentClick();
    }
}

package we.video.wevideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import we.video.wevideo.AdWebActivity;
import we.video.wevideo.MainActivity;
import we.video.wevideo.R;
import we.video.wevideo.bean.Ad;
import we.video.wevideo.bean.Special;
import we.video.wevideo.ui.special.AuthorActivity;
import we.video.wevideo.ui.special.SpecialActivity;
import we.video.wevideo.ui.support.NumberAnimTextView;
import we.video.wevideo.ui.support.WaveView;
import we.video.wevideo.ui.support.banner.BannerLayout;
import we.video.wevideo.ui.support.lib.SlidingMenu;
import we.video.wevideo.util.DeviceUtil;
import we.video.wevideo.util.ImageUtil;
import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/3/31.
 * 首页列表
 */
public class IndexAdapter extends BaseAdapter {

    private Context context;
    private List<Special> mData = new ArrayList<Special>();
    private List<Ad> mBanners;
    private boolean isRefreshBanner;
    private SlidingMenu slidingMenu;

    private int y, oldY;

    public IndexAdapter(Context context) {
        this.context = context;
    }

    public void setSlidingMenu(SlidingMenu slidingMenu) {
        this.slidingMenu = slidingMenu;
    }

    public void addAll(List<Special> data) {
        mData.addAll(data);
    }

    public void clear() {
        mData.clear();
    }

    public void setBannerData(List<Ad> ads) {
        isRefreshBanner = true;
        mBanners = ads;
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        switch (type) {
            case 0:
                BannerHolder bannerHolder;
                if (null == view) {
                    view = View.inflate(context, R.layout.view_banner, null);
                    bannerHolder = new BannerHolder(view);
                    view.setTag(bannerHolder);
                } else {
                    bannerHolder = (BannerHolder) view.getTag();
                }
                bannerHolder.banner.setSlidingMenu(slidingMenu);
                if (null != mBanners && mBanners.size() > 0) {
                    if (isRefreshBanner) {
                        isRefreshBanner = false;
                        bannerHolder.banner.setVisibility(View.VISIBLE);
                        bannerHolder.iv_default.setVisibility(View.GONE);
                        bannerHolder.banner.setViewUrls(mBanners);
                    }
                } else {
                    //显示占位图
                    bannerHolder.banner.setVisibility(View.GONE);
                    bannerHolder.iv_default.setVisibility(View.VISIBLE);
                    bannerHolder.iv_default.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getScreenWidth(context) / 2));
                }

                bannerHolder.banner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getScreenWidth(context) / 2));
                //添加监听事件
                bannerHolder.banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Ad ad = mBanners.get(position);
                        Intent intent;
                        if (ad.getType().equals("0")) {//专辑
                            intent = new Intent(context, SpecialActivity.class);
                            intent.putExtra("id", ad.getType_id());
                            ((MainActivity) context).startActivity(intent);
                        } else if (ad.getType().equals("1")) {//作者
                            intent = new Intent(context, AuthorActivity.class);
                            intent.putExtra("id", ad.getType_id());
                            ((MainActivity) context).startActivity(intent);
                        } else if (ad.getType().equals("2")) {//广告
                            intent = new Intent(context, AdWebActivity.class);
                            intent.putExtra("url", ad.getType_id());
                            intent.putExtra("title", ad.getTitle());
                            ((MainActivity) context).startActivity(intent);
                        }
                    }
                });
                break;
            case 1:
                Holder holder;
                if (null == view) {
                    view = View.inflate(context, R.layout.adapter_index, null);
                    holder = new Holder(view);
                    view.setTag(holder);
                } else {
                    holder = (Holder) view.getTag();
                }
                final Special special = mData.get(i - 1);
                holder.iv_bg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DeviceUtil.getScreenWidth(context) / 2.4)));
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
                holder.layout_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ((int) (DeviceUtil.getScreenWidth(context) / 2.4))));
                holder.layout_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.e("点击", "");
                        Intent intent = new Intent(context, SpecialActivity.class);
                        intent.putExtra("id", special.getId());
                        ((Activity) context).startActivity(intent);
                    }
                });
                break;
        }
        return view;
    }

    class BannerHolder {
        BannerLayout banner;
        ImageView iv_default;

        BannerHolder(final View v) {
            banner = (BannerLayout) v.findViewById(R.id.banner);
            iv_default = (ImageView) v.findViewById(R.id.iv_default);
            v.findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    ((TextView) v.findViewById(R.id.tv_title)).setText(mBanners.get(position).getTitle());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    class Holder {
        WaveView layout_bg;
        RelativeLayout layout_center;
        ImageView iv_bg;
        NumberAnimTextView tv_special_name, tv_name, tv_time;

        Holder(View v) {
            layout_center = (RelativeLayout) v.findViewById(R.id.layout_center);
            layout_bg = (WaveView) v.findViewById(R.id.layout_bg);
            iv_bg = (ImageView) v.findViewById(R.id.iv_bg);
            tv_special_name = (NumberAnimTextView) v.findViewById(R.id.tv_special_name);
            tv_name = (NumberAnimTextView) v.findViewById(R.id.tv_name);
            tv_time = (NumberAnimTextView) v.findViewById(R.id.tv_time);
        }
    }
}

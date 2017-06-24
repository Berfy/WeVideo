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
import we.video.wevideo.bean.Product;
import we.video.wevideo.util.ImageUtil;

/**
 * Created by Berfy on 2016/4/6.
 * 商品列表
 */
public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> data = new ArrayList<>();

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public List<Product> getData() {
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
            convertView = View.inflate(context, R.layout.adapter_product, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Product product = data.get(position);
        ImageUtil.getInstances().displayDefault(holder.iv_icon, product.getThumb());
        holder.tv_product_name.setText(product.getpName());
        holder.tv_product_price.setText(product.getPrice());
        holder.tv_product_price.getPaint().setFakeBoldText(true);
        return convertView;
    }

    class Holder {
        TextView tv_product_name, tv_product_price;
        ImageView iv_icon;

        Holder(View v) {
            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
            tv_product_name = (TextView) v.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) v.findViewById(R.id.tv_product_price);
        }
    }
}

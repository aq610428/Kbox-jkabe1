package com.jkabe.app.android.adapter;

import android.content.Context;

import com.jkabe.app.android.R;
import com.jkabe.app.android.bean.OrderBean;
import com.jkabe.app.android.glide.GlideUtils;

import java.util.List;


/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class OrderListAdapter extends AutoRVAdapter {
    private List<OrderBean.ItemsBean> list;

    public OrderListAdapter(Context context, List<OrderBean.ItemsBean> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_order_list;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        OrderBean.ItemsBean bean=list.get(position);
        GlideUtils.setImageUrl(bean.getSmallImg(),vh.getImageView(R.id.iv_logo));
        vh.getTextView(R.id.text_name).setText(bean.getTitle());
        vh.getTextView(R.id.text_price).setText("￥"+bean.getSellPrice());
        vh.getTextView(R.id.text_num).setText("x"+bean.getGoodNumber());

    }


}

package com.jkabe.app.android.adapter;


import android.content.Context;

import com.jkabe.app.android.R;
import com.jkabe.app.android.bean.CartBean;
import com.jkabe.app.android.glide.GlideUtils;

import java.util.List;


/**
 * @author: zt
 * @date: 2020/9/18
 * @name:ConfimAdapter
 */
public class ConfimAdapter extends AutoRVAdapter {
    List<CartBean> list;

    public ConfimAdapter(Context context, List<CartBean> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_config;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        CartBean bean = list.get(position);
        GlideUtils.CreateImageRound(bean.getSmallImg(), vh.getImageView(R.id.iv_logo), 5);
        vh.getTextView(R.id.text_name).setText(bean.getTitle());
        vh.getTextView(R.id.text_price).setText(bean.getSellPrice() + "");
        vh.getTextView(R.id.text_number).setText("x"+bean.getGoodNumber() + "");
    }
}

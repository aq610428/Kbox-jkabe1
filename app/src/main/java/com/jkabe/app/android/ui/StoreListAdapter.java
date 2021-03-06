package com.jkabe.app.android.ui;

import android.view.View;

import com.jkabe.app.android.R;
import com.jkabe.app.android.adapter.AutoRVAdapter;
import com.jkabe.app.android.adapter.ViewHolder;
import com.jkabe.app.android.bean.StoreInfo;
import com.jkabe.app.android.glide.GlideUtils;
import com.jkabe.app.android.util.BigDecimalUtils;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/28
 * @name:StoreAdapter
 */
public class StoreListAdapter extends AutoRVAdapter {
    private List<StoreInfo> infos;
    private StoreListActivity activity;

    public StoreListAdapter(StoreListActivity activity, List<StoreInfo> list) {
        super(activity, list);
        this.infos = list;
        this.activity = activity;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_store_store;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        StoreInfo info = infos.get(position);
        vh.getTextView(R.id.text_name).setText(infos.get(position).getName());
        GlideUtils.CreateImageRound(infos.get(position).getLogo(), vh.getImageView(R.id.iv_photo), 2);
        vh.getTextView(R.id.tv_address).setText(info.getAddress());
        if (info.getDistance()>=1000){
            vh.getTextView(R.id.text_distance).setText(BigDecimalUtils.div(new BigDecimal(info.getDistance()),new BigDecimal(1000),2) + "km");
        }else{
            vh.getTextView(R.id.text_distance).setText(info.getDistance() + "m");
        }

        vh.getTextView(R.id.text_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showDialog(info.getId(),info.getDistance()+"");
            }
        });

    }

    public void setData(List<StoreInfo> infos) {
        this.infos = infos;
    }
}

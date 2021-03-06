package com.jkabe.app.android.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkabe.app.android.R;
import com.jkabe.app.android.bean.OrderBean;
import com.jkabe.app.android.ui.OrderDetileActivity1;
import com.jkabe.app.android.ui.fragment.CompletedFragment;
import com.jkabe.app.android.util.Utility;

import java.util.List;


/**
 * @author: zt
 * @date: 2020/9/22
 * @name:TakeAdapter
 */
public class TakeAdapter2 extends AutoRVAdapter {
    public List<OrderBean> list;
    private CompletedFragment allFragment;

    public TakeAdapter2(CompletedFragment allFragment, List<OrderBean> list) {
        super(allFragment.getContext(), list);
        this.list = list;
        this.allFragment = allFragment;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_take_vh2;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        OrderBean orderBean = list.get(position);
        if (orderBean.getItems() != null && orderBean.getItems().size() > 0) {
            RecyclerView recyclerView = vh.getRecyclerView(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(allFragment.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            OrderListAdapter2 adapter = new OrderListAdapter2(allFragment, orderBean.getItems(), orderBean);
            recyclerView.setAdapter(adapter);
            vh.getTextView(R.id.text_num).setText("共" + orderBean.getItems().size() + "件");
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(allFragment.getContext(), OrderDetileActivity1.class);
                    intent.putExtra("id", orderBean.getId());
                    intent.putExtra("orderStatus", orderBean.getOrderStatus()+"");
                    allFragment.getContext().startActivity(intent);
                }
            });
        }
        vh.getTextView(R.id.text_count).setText("实付 ￥" + orderBean.getGoodMoney());

        if (!Utility.isEmpty(orderBean.getStringOrdertime())) {
            String stringOrdertime = orderBean.getStringOrdertime();
            vh.getTextView(R.id.text_date).setText(stringOrdertime.substring(0,10) + " " + stringOrdertime.substring(stringOrdertime.length()-8, stringOrdertime.length()));
        }
        vh.getTextView(R.id.text_stats).setText("待收货");
    }

    public void setData(List<OrderBean> beanList) {
        this.list = beanList;
    }
}

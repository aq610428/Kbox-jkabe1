package com.jkabe.app.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jkabe.app.android.R;
import com.jkabe.app.android.adapter.MeAdapter;
import com.jkabe.app.android.adapter.WareAdapter;
import com.jkabe.app.android.adapter.WareAdapter2;
import com.jkabe.app.android.banner.Banner2;
import com.jkabe.app.android.banner.BannerConfig;
import com.jkabe.app.android.banner.Transformer;
import com.jkabe.app.android.banner.listener.OnBannerListener;
import com.jkabe.app.android.base.BaseFragment;
import com.jkabe.app.android.bean.BannerVo;
import com.jkabe.app.android.bean.Block;
import com.jkabe.app.android.bean.CommonalityModel;
import com.jkabe.app.android.bean.GoodBean;
import com.jkabe.app.android.config.Api;
import com.jkabe.app.android.config.NetWorkListener;
import com.jkabe.app.android.config.okHttpModel;
import com.jkabe.app.android.ui.PreviewActivity;
import com.jkabe.app.android.ui.WareDeilActivity;
import com.jkabe.app.android.util.Constants;
import com.jkabe.app.android.util.JsonParse;
import com.jkabe.app.android.util.Md5Util;
import com.jkabe.app.android.util.StatusBarUtil;
import com.jkabe.app.android.util.ToastUtil;
import com.jkabe.app.android.util.Utility;
import com.jkabe.app.android.weight.MyLoader;
import com.jkabe.app.android.weight.SpaceItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/11/24
 * @name:AspFragment
 */
public class AspFragment extends BaseFragment implements OnBannerListener, NetWorkListener, OnRefreshListener, OnLoadMoreListener {
    private View rootView;
    private Banner2 banner;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView, recyclerView1;
    private List<BannerVo> banners = new ArrayList<>();
    private List<Block> data = new ArrayList<>();
    private WareAdapter wareAdapter;
    private List<GoodBean> list = new ArrayList<>();
    private int page = 1;
    private int limit = 10000;
    private boolean isRefresh;
    private MeAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_asp, container, false);
            initView();
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setTranslucentStatus(getActivity());
    }


    private void initView() {
        recyclerView1 = getView(rootView, R.id.recyclerView1);
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        recyclerView = getView(rootView, R.id.recyclerView);
        banner = getView(rootView, R.id.banner);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);

        data = Constants.getblocks();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MeAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView1.addItemDecoration(new SpaceItemDecoration(35));
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setNestedScrollingEnabled(false);

        query();
        queryGoodList();
    }


    @Override
    public void OnBannerClick(int position) {
        String url = banners.get(position).getUrl();
        if (!Utility.isEmpty(url) && !url.equals("www.jkabe.com")) {
            Intent intent = new Intent(getContext(), PreviewActivity.class);
            intent.putExtra("name", banners.get(position).getAdvertName());
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }


    /******广告*****/
    public void query() {
        String sign = "advertType=" + Constants.ADVER + "&pagecount=4" + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("advertType", Constants.ADVER);
        params.put("pagecount", "4");
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_ADVERT, params, Api.GET_ADVERT_ID, this);
    }


    /******商品列表*****/
    public void queryGoodList() {
        showProgressDialog(getActivity(), false);
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("apptype", Constants.TYPE);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GOODDATA, params, Api.GOODDATA_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_ADVERT_ID:
                        banners = JsonParse.getBannerListJson(object);
                        if (banner != null && banners.size() > 0) {
                            updateView();
                        }
                        break;
                    case Api.GOODDATA_ID:
                        List<GoodBean> beans = JsonParse.getGoodBeanJson(object);
                        if (beans != null && beans.size() > 0) {
                            setAdapter(beans);
                        } else {
                            if (isRefresh && page > 1) {
                                ToastUtil.showToast("无更多商品");
                            }
                            stopProgressDialog();
                        }
                        break;

                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
                stopProgressDialog();
            }

        } else {
            stopProgressDialog();
        }

        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


    private void setAdapter(List<GoodBean> beans) {
        if (!isRefresh) {
            list.clear();
            list.addAll(beans);
            wareAdapter = new WareAdapter(getContext(), list);
            recyclerView1.setHasFixedSize(true);
            recyclerView1.setAdapter(wareAdapter);
        } else {
            list.addAll(beans);
            wareAdapter.setData(list);
        }

        wareAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WareDeilActivity.class);
                intent.putExtra("goodBean", list.get(position));
                startActivity(intent);
            }
        });
        stopProgressDialog();
    }


    @Override
    public void onRefresh() {
        isRefresh = false;
        page = 1;
        queryGoodList();

    }


    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        queryGoodList();
    }


    public void updateView() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        banner.setImages(banners);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        banner.setTitleView(true);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.RIGHT)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }


    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


}

package com.jkabe.app.android.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jkabe.app.android.R;
import com.jkabe.app.android.adapter.LeftAdapter;
import com.jkabe.app.android.banner.Banner;
import com.jkabe.app.android.banner.Banner2;
import com.jkabe.app.android.banner.BannerConfig;
import com.jkabe.app.android.banner.Transformer;
import com.jkabe.app.android.banner.listener.OnBannerListener;
import com.jkabe.app.android.base.BaseFragment;
import com.jkabe.app.android.bean.BannerVo;
import com.jkabe.app.android.bean.CommonalityModel;
import com.jkabe.app.android.bean.LeftVo;
import com.jkabe.app.android.bean.UserInfo;
import com.jkabe.app.android.config.Api;
import com.jkabe.app.android.config.NetWorkListener;
import com.jkabe.app.android.config.okHttpModel;
import com.jkabe.app.android.ui.PreviewActivity;
import com.jkabe.app.android.util.Constants;
import com.jkabe.app.android.util.JsonParse;
import com.jkabe.app.android.util.Md5Util;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.Utility;
import com.jkabe.app.android.weight.MyLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crossoverone.statuslib.StatusUtil;


/**
 * @author: zt
 * @date: 2020/7/2
 * @name:车生活
 */
public class CarLeftFragment extends BaseFragment implements View.OnClickListener, OnBannerListener, NetWorkListener, OnRefreshListener {
    private View rootView;
    private Banner2 banner;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private List<BannerVo> banners = new ArrayList<>();
    private List<LeftVo> voList = new ArrayList<>();
    private LeftAdapter leftAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_car_left, container, false);
            initView();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        StatusUtil.setSystemStatus(getActivity(), false, true);
        queryUser();
    }


    private void initView() {
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        recyclerView = getView(rootView, R.id.recyclerView);
        banner = getView(rootView, R.id.banner);
        swipeToLoadLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        query();
        queryList();
    }


    public void updateView() {
        //设置图片网址或地址的集合
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
    public void onClick(View v) {

    }


    /******广告*****/
    public void query() {
        showProgressDialog(getActivity(), false);
        String sign = "advertType=1" + "&pagecount=4" + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("advertType", Constants.TYPE);
        params.put("pagecount", "4");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_ADVERT, params, Api.GET_ADVERT_ID, this);
    }

    /******车生活三方服务列表信息*****/
    public void queryList() {
        showProgressDialog(getActivity(), false);
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_ADVERT_TAG, params, Api.GET_ADVERT_TAG_ID, this);
    }


    /******查询个人资料*****/
    public void queryUser() {
        showProgressDialog(getActivity(), false);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MEID_USER, params, Api.GET_MEID_USER_ID, this);
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
                    case Api.GET_ADVERT_TAG_ID:
                        voList = JsonParse.getTagJson(object);
                        if (voList != null && voList.size() > 0) {
                            setAdapter();
                        }
                        break;
                    case Api.GET_MEID_USER_ID:
                        UserInfo info = JsonParse.getUserInfo(object);
                        SaveUtils.saveInfo(info);
                        break;

                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    private void setAdapter() {
        leftAdapter = new LeftAdapter(this, voList);
        recyclerView.setAdapter(leftAdapter);
    }

    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        queryList();
    }
}

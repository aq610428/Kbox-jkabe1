package com.jkabe.app.android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseActivity;
import com.jkabe.app.android.bean.CommonalityModel;
import com.jkabe.app.android.config.Api;
import com.jkabe.app.android.config.NetWorkListener;
import com.jkabe.app.android.config.okHttpModel;
import com.jkabe.app.android.util.Constants;
import com.jkabe.app.android.util.Md5Util;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.ToastUtil;
import com.jkabe.app.android.util.Utility;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/11/26
 * @name:搜索
 */
public class SearchActivity extends BaseActivity implements NetWorkListener {
    private RecyclerView recyclerView;
    private TextView text_delete, text_search;
    private EditText et_search;
    private Map<String, String> map = new LinkedHashMap<>();
    private List<String> list = new ArrayList<>();

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        recyclerView = getView(R.id.recyclerView);
        text_delete = getView(R.id.text_delete);
        text_search = getView(R.id.text_search);
        et_search = getView(R.id.et_search);
        text_delete.setOnClickListener(this);
        text_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new FlowLayoutManager());
    }


    public void query() {
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_INTERGRAL_VERSION, params, Api.GET_INTERGRAL_VERSION_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_INTERGRAL_VERSION_ID:

                        break;
                }
            }
        }
        stopProgressDialog();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.text_delete:

                break;
            case R.id.text_search:
                saveSearch();
                break;
        }
    }

    private void saveSearch() {
        String key = et_search.getText().toString();
        if (Utility.isEmpty(key)){
            ToastUtil.showToast("搜索内容不能为空");
            return;
        }
        list=SaveUtils.getKey();
        if (list!=null&&list.size()>0){
            for (int i = 0; i <list.size() ; i++) {
                 map.put(list.get(i),list.get(i));
            }
        }
        map.put(key,key);
        list.clear();
        for(Map.Entry<String, String> entry : map.entrySet()){
            list.add(entry.getValue());
        }





    }


    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }
}

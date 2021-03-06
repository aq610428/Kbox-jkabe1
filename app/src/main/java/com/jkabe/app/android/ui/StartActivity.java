package com.jkabe.app.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseActivity;
import com.jkabe.app.android.base.BaseApplication;
import com.jkabe.app.android.bean.UserInfo;
import com.jkabe.app.android.util.SaveUtils;


/***
 *
 * 启动页面
 *
 *
 */
public class StartActivity extends BaseActivity {
    private UserInfo info;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        BaseApplication.activityTaskManager.putActivity("StartActivity",this);
        info= SaveUtils.getSaveInfo();
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (info!=null){
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }

    @Override
    protected void initData() {

    }
}

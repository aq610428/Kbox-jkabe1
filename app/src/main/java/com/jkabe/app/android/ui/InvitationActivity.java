package com.jkabe.app.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseActivity;
import com.jkabe.app.android.base.BaseApplication;
import com.jkabe.app.android.util.FileManager;
import com.jkabe.app.android.util.ImageFactory;
import com.jkabe.app.android.util.QRCodeUtil;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.ShotShareUtil;
import com.jkabe.app.android.util.StatusBarUtil;

import java.io.File;

/**
 * @author: zt
 * @date: 2020/7/6
 * @name:邀请好友
 */
public class InvitationActivity extends BaseActivity {
    private TextView text_head, text_invitation,text_code;
    private ImageView icon_code;
    private RelativeLayout rl_share;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invitation);
        BaseApplication.activityTaskManager.putActivity("InvitationActivity", this);
    }

    @Override
    protected void initView() {
        text_code= getView(R.id.text_code);
        rl_share= getView(R.id.rl_share);
        text_head = getView(R.id.text_head);
        icon_code = getView(R.id.icon_code);
        text_invitation = getView(R.id.text_invitation);
        text_head.setOnClickListener(this);
        rl_share.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        text_code.setText("我的邀请码:" + SaveUtils.getSaveInfo().getRmcode());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap mBitmap = QRCodeUtil.createQRCodeWithLogo(SaveUtils.getSaveInfo().getTgurl(), 700, bitmap);
        icon_code.setImageBitmap(mBitmap);
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setTranslucentStatus(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.text_head:
                finish();
                break;
            case R.id.rl_share:
                shareImg();
                break;
        }
    }


    public void shareImg() {
        Bitmap bitmap = ImageFactory.DrawableToBitmap(icon_code.getDrawable());
        File file = FileManager.screenShot(bitmap);
        if (file != null) {
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            Uri uri = FileProvider.getUriForFile(this, "com.jkabe.app.android.provider", file);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/*");
            Intent chooser = Intent.createChooser(intent, "邀请好友");
            startActivity(chooser);
        }
    }
}

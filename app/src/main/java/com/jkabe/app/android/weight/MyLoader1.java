package com.jkabe.app.android.weight;

import android.content.Context;
import android.widget.ImageView;

import com.jkabe.app.android.R;
import com.jkabe.app.android.banner.loader.ImageLoader;
import com.jkabe.app.android.glide.GlideUtils;
import com.jkabe.app.android.util.Utility;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:MyLoader
 */
public class MyLoader1 extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (Utility.isEmpty(path)){
            imageView.setImageResource(R.mipmap.mp_chat_goods_card_default_img);
        }else{
            GlideUtils.setImageUrl(path.toString(),imageView);
        }

    }
}

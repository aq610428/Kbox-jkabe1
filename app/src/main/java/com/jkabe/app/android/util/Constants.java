package com.jkabe.app.android.util;


import com.jkabe.app.android.R;
import com.jkabe.app.android.bean.Block;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String APP_ID = "wxdebf49e4963895e1";
    public static String SUCESSCODE = "EXECUTE_SUCCESS";
    public static final String PARTNERID = "761ea3b34cc74aa58ac74d5e60048997";//区分动画
    public static final String SECREKEY = "f6af9cb871dd4237a4b89b8f5bbf8456";//区分动画
    public static String TYPE = "1";//区分权限
    public static final String oilUrl = "http://oil.card.jkabe.com/";//车油惠
    public static final String CITY = "city";//车油惠
    public static final String LAT = "lat";//车油惠
    public static final String LON = "lon";//车油惠
    public static final String OIL = "oil";//车油惠
    public static final String TOKEN = "token";//车油惠
    public static String ADVER = "1";//区分权限
    public static final String CATEGORYA = "3736cc5daf354098ab3ef3e33e581376";//车油惠


    public static List<Block> getblocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block("数码电器", R.mipmap.ic_television_3));
        blocks.add(new Block("食品粮油", R.mipmap.ic_television_2));
        blocks.add(new Block("服饰潮流", R.mipmap.ic_television_1));
        blocks.add(new Block("钟表珠宝", R.mipmap.ic_television_4));
        blocks.add(new Block("日用百货", R.mipmap.ic_television_5));
        return blocks;
    }
}

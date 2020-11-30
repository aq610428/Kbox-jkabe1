package com.jkabe.app.android.util;

import com.jkabe.app.android.bean.CarInfo;
import com.jkabe.app.android.bean.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:SaveUtils
 */
public class SaveUtils {
    /******用户基本信息*****/
    public static void saveInfo(UserInfo info) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("userInfo", (Serializable) info);
    }


    /******用户基本信息*****/
    public static UserInfo getSaveInfo() {
        UserInfo userInfo = (UserInfo) CacheDiskUtils.getInstance().getSerializable("userInfo");
        return userInfo;
    }

    /******搜索关键字保存*****/
    public static List<String> getKey() {
        List<String> list = (List<String>) CacheDiskUtils.getInstance().getSerializable("key");
        return list;
    }

    public static void saveKey(List<String> list) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("key", (Serializable) list);
    }


    /******用户基本信息*****/
    public static void clealCacheDisk() {
        CacheDiskUtils.getInstance().clear();
    }

    /******用户车辆信息信息*****/
    public static void saveCar(CarInfo carInfo) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("carInfo", carInfo);
    }

    /******用户车辆信息信息*****/
    public static CarInfo getCar() {
        CarInfo carInfo = (CarInfo) CacheDiskUtils.getInstance().getSerializable("carInfo");
        return carInfo;
    }
}

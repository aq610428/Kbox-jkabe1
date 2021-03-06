package com.jkabe.app.android.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.jkabe.app.android.bean.PayBean;
import com.jkabe.app.android.ui.ConfirmActivity;
import com.jkabe.app.android.ui.OrderDetileActivity;
import com.jkabe.app.android.ui.fragment.AllFragment;
import com.jkabe.app.android.ui.fragment.PayFragment;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/10/10
 * @name:支付工具类
 */
public class PayUtils {


    public static void wechatPay(Activity activity, PayBean payBean) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, Constants.APP_ID);
        if (!isWeixinAvilible(activity)) {
            ToastUtil.showToast("您未安装微信，请安装后再试~");
            return;
        }
        if (payBean != null) {
            PayReq req = new PayReq();
            req.appId = payBean.getPayMap().getAppid();
            req.partnerId = payBean.getPayMap().getPartnerid();
            req.prepayId = payBean.getPayMap().getPrepayid();
            req.nonceStr = payBean.getPayMap().getNoncestr();
            req.timeStamp = payBean.getPayMap().getTimestamp() + "";
            req.packageValue = payBean.getPayMap().getPackageX();
            req.sign = payBean.getPayMap().getSign();
            req.extData = "app data"; // optional
            wxapi.sendReq(req);
        }
    }

    public static void AliPay(PayFragment fragment, Handler mHandler, String orderInfo) {
        if (!isAliPayInstalled(fragment.getActivity())) {
            ToastUtil.showToast("您未安装支付宝，请安装后再试~");
            return;
        }
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(fragment.getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.i("支付结果result=" + result.toString());
                Message msg = new Message();
                msg.what = fragment.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void AliPay(AllFragment fragment, Handler mHandler, String orderInfo) {
        if (!isAliPayInstalled(fragment.getActivity())) {
            ToastUtil.showToast("您未安装支付宝，请安装后再试~");
            return;
        }
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(fragment.getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.i("支付结果result=" + result.toString());
                Message msg = new Message();
                msg.what = fragment.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public static void AliPay(ConfirmActivity activity, Handler mHandler, String orderInfo) {
        if (!isAliPayInstalled(activity)) {
            ToastUtil.showToast("您未安装支付宝，请安装后再试~");
            return;
        }
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.i("支付结果result=" + result.toString());
                Message msg = new Message();
                msg.what = activity.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public static void AliPay(OrderDetileActivity activity, Handler mHandler, String orderInfo) {
        if (!isAliPayInstalled(activity)) {
            ToastUtil.showToast("您未安装支付宝，请安装后再试~");
            return;
        }
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.i("支付结果result=" + result.toString());
                Message msg = new Message();
                msg.what = activity.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 检测是否安装支付宝
     *
     * @param activity
     * @return
     */
    public static boolean isAliPayInstalled(Activity activity) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        return componentName != null;
    }

    /**
     * 检测是否安装微信
     *
     * @param activity
     * @return
     */
    public static boolean isWeixinAvilible(Activity activity) {
        final PackageManager packageManager = activity.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}

package com.jkabe.app.android.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseApplication;
import com.jkabe.app.android.ui.LicenseActivity;
import com.jkabe.app.android.ui.LoginActivity;
import com.jkabe.app.android.ui.OrderDetileActivity;
import com.jkabe.app.android.ui.OrderDetileActivity1;
import com.jkabe.app.android.ui.fragment.AllFragment;
import com.jkabe.app.android.ui.fragment.CompletedFragment;
import com.jkabe.app.android.ui.fragment.PayFragment;


public class DialogUtils {

    public static void showDialog(final String phoneNum, Activity activity) {
        final Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout1, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        ((TextView) view.findViewById(R.id.title)).setText("温馨提示");
        ((TextView) view.findViewById(R.id.message)).setText("确定退出惠保养?");
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public static void showLogin(Context context,String msg) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg+"");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setVisibility(View.GONE);
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.activityTaskManager.closeAllActivityExceptOne("LoginActivity");
                context.startActivity(new Intent(context, LoginActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    /******取消订单****/
    public static void showDelete(OrderDetileActivity activity, String msg, String id) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.delete(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /******取消订单****/
    public static void showConfirm(CompletedFragment fragment, String msg, String id) {
        Dialog dialog = new Dialog(fragment.getContext());
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.payConfirm(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /******去人订单****/
    public static void showConfirm(OrderDetileActivity1 activity, String msg, String id) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.payConfirm(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /******取消订单****/
    public static void showConfirm(OrderDetileActivity activity, String msg) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.orderBean!=null&&activity.orderBean.getOrderinfo()!=null){
                    activity.payConfirm(activity.orderBean.getOrderinfo().getId());
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /******取消订单****/
    public static void showOrder(OrderDetileActivity activity, String msg) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.orderBean!=null&&activity.orderBean.getOrderinfo()!=null){
                    activity.cancelOrder(activity.orderBean.getOrderinfo().getId());
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    /******取消订单****/
    public static void showOrder(PayFragment fragment, String msg, String orderId) {
        Dialog dialog = new Dialog(fragment.getContext());
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.cancelOrder(orderId);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    /******取消订单****/
    public static void showOrder(AllFragment allFragment, String msg,String orderId) {
        Dialog dialog = new Dialog(allFragment.getContext());
        View view = LayoutInflater.from(allFragment.getContext()).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFragment.cancelOrder(orderId);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}

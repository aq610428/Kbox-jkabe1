package com.jkabe.app.android.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseFragment;
import com.jkabe.app.android.bean.CarInfo;
import com.jkabe.app.android.bean.CarVo;
import com.jkabe.app.android.bean.CommonalityModel;
import com.jkabe.app.android.config.Api;
import com.jkabe.app.android.config.NetWorkListener;
import com.jkabe.app.android.config.okHttpModel;
import com.jkabe.app.android.ui.BatteryActivity;
import com.jkabe.app.android.ui.BindActivity;
import com.jkabe.app.android.ui.DrivingLicenseActivity;
import com.jkabe.app.android.ui.EarlyActivity;
import com.jkabe.app.android.ui.ElectronicActivity;
import com.jkabe.app.android.ui.LocationIndexActivity;
import com.jkabe.app.android.ui.MedicalActivity;
import com.jkabe.app.android.ui.OilActivity;
import com.jkabe.app.android.ui.ParameterActivity;
import com.jkabe.app.android.ui.TravelActivity;
import com.jkabe.app.android.ui.VehicleActivity;
import com.jkabe.app.android.util.BigDecimalUtils;
import com.jkabe.app.android.util.Constants;
import com.jkabe.app.android.util.JsonParse;
import com.jkabe.app.android.util.LogUtils;
import com.jkabe.app.android.util.Md5Util;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.SystemTools;
import com.jkabe.app.android.util.ToastUtil;
import com.jkabe.app.android.util.Utility;
import com.jkabe.app.android.weight.PreferenceUtils;
import com.jkabe.app.android.weight.RuntimeRationale;
import com.jkabe.app.android.weight.SensorEventHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONObject;

import java.util.Map;

import crossoverone.statuslib.StatusUtil;

/**
 * @author: zt
 * @date: 2020/7/2
 * @name:我的车
 */
public class CarFragment extends BaseFragment implements View.OnClickListener, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, NetWorkListener {
    private View rootView;
    private TextureMapView mMapView;
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean mFirstFix = false;
    private SensorEventHelper mSensorHelper;
    private CarInfo carInfo;
    private TextView text_num, text_attery, text_electronic;
    private CarVo carVo;
    private RelativeLayout rl_edition;
    private TextView text_address, text_date, text_oll, text_early;
    private LinearLayout ll_write, ll_detection, ll_travel;
    final Handler mHandler = new Handler();
    private ImageView iv_set;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_car, container, false);
            mMapView = getView(rootView, R.id.map);
            mMapView.onCreate(savedInstanceState);
            initView();
            request();
        }
        return rootView;
    }


    private void request() {
        AndPermission.with(this).runtime().permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                .rationale(new RuntimeRationale())
                .onGranted(permissions -> setUpMap())
                .onDenied(permissions -> {
                    if (AndPermission.hasAlwaysDeniedPermission(getContext(), permissions)) {
                        showSettingDialog(getContext(), permissions);
                    }
                })
                .start();
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        StatusUtil.setSystemStatus(getActivity(), false, true);
        polling();
    }

    private void initView() {
        iv_set = getView(rootView, R.id.iv_set);
        ll_travel = getView(rootView, R.id.ll_travel);
        ll_detection = getView(rootView, R.id.ll_detection);
        ll_write = getView(rootView, R.id.ll_write);
        text_early = getView(rootView, R.id.text_early);
        text_oll = getView(rootView, R.id.text_oll);
        text_date = getView(rootView, R.id.text_date);
        text_address = getView(rootView, R.id.text_address);
        rl_edition = getView(rootView, R.id.rl_edition);
        text_electronic = getView(rootView, R.id.text_electronic);
        text_attery = getView(rootView, R.id.text_attery);
        text_num = getView(rootView, R.id.text_num);
        text_attery.setOnClickListener(this);
        text_electronic.setOnClickListener(this);
        rl_edition.setOnClickListener(this);
        text_early.setOnClickListener(this);
        text_oll.setOnClickListener(this);
        ll_write.setOnClickListener(this);
        ll_detection.setOnClickListener(this);
        ll_travel.setOnClickListener(this);
        text_num.setOnClickListener(this);
        iv_set.setOnClickListener(this);
        aMap = mMapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void setUpMap() {
        mSensorHelper = new SensorEventHelper(getContext());
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (carInfo != null && !Utility.isEmpty(carInfo.getImeicode())) {
            switch (v.getId()) {
                case R.id.text_num:
                    startActivity(new Intent(getContext(), VehicleActivity.class));
                    break;
                case R.id.text_attery:
                    startActivity(new Intent(getContext(), BatteryActivity.class));
                    break;
                case R.id.text_electronic:
                    startActivity(new Intent(getContext(), ElectronicActivity.class));
                    break;
                case R.id.ll_detection:
                    startActivity(new Intent(getContext(), DrivingLicenseActivity.class));
                    break;
                case R.id.text_oll:
                    String oil = PreferenceUtils.getPrefString(getContext(), Constants.OIL, "");
                    if (Utility.isEmpty(oil)) {
                        showDialog();
                    } else {
                        startActivity(new Intent(getContext(), OilActivity.class));
                    }
                    break;
                case R.id.text_early:
                    startActivity(new Intent(getContext(), EarlyActivity.class));
                    break;
                case R.id.ll_write:
                    if (Utility.isEmpty(PreferenceUtils.getPrefString(getContext(), Constants.OIL, ""))) {
                        showDialog();
                    } else {
                        startActivity(new Intent(getContext(), TravelActivity.class));
                    }
                    break;
                case R.id.ll_travel:
                    startActivity(new Intent(getContext(), MedicalActivity.class));
                    break;
                case R.id.iv_set:
                    startActivity(new Intent(getContext(), ParameterActivity.class));
                    break;
                case R.id.rl_edition:
                    startActivity(new Intent(getContext(), LocationIndexActivity.class));
                    break;
            }
        } else {
            startActivity(new Intent(getContext(), BindActivity.class));
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (!mFirstFix) {
                mFirstFix = true;
                PreferenceUtils.setPrefString(getContext(), Constants.CITY, amapLocation.getCity());
            }
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            LogUtils.e("errText=" + errText);
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }


    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    private void polling() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_DECICE_VERSION, params, Api.GET_DECICE_VERSION_ID, this);
    }

    private void qurycar() {
        if (SaveUtils.getCar() == null && Utility.isEmpty(SaveUtils.getCar().getImeicode())) {
            return;
        }
        String sign = "imeicode=" + SaveUtils.getCar().getImeicode() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("imeicode", SaveUtils.getCar().getImeicode() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_CAR_DEVICE, params, Api.GET_CAR_DEVICE_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_DECICE_VERSION_ID:
                        carInfo = JsonParse.getCarInfoJson(object);
                        updateView();
                        break;
                    case Api.GET_CAR_DEVICE_ID:
                        carVo = JsonParse.getCarVoJson(object);
                        if (carVo != null) {
                            updateMap();
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
    }

    private void updateMap() {
        String address = carVo.getLocationInfo().getAddress();
        CoordinateConverter converter = new CoordinateConverter(getContext());
        converter.from(CoordinateConverter.CoordType.GPS);
        if (!Utility.isEmpty(carVo.getLocationInfo().getLat())) {
            try {
                aMap.clear();
                rl_edition.setVisibility(View.VISIBLE);
                LatLng latLng = SystemTools.getLatLng(Double.parseDouble(carVo.getLocationInfo().getLat()), Double.parseDouble(carVo.getLocationInfo().getLng()));
                PreferenceUtils.setPrefString(getContext(), Constants.LAT, BigDecimalUtils.subLastBit(latLng.latitude, 6).doubleValue() + "");
                PreferenceUtils.setPrefString(getContext(), Constants.LON, BigDecimalUtils.subLastBit(latLng.longitude, 6).doubleValue() + "");
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.im_device_loc)));
                markerOption.position(latLng);
                String gps = carVo.getLocationInfo().getGpstime();
                if (!Utility.isEmpty(gps)) {
                    String begin = gps.substring(0, gps.length() - 8);
                    String end = gps.substring(gps.length() - 8, gps.length());
                    text_address.setText(address);
                    text_date.setText("时间:" + begin + " " + end);
                }
                markerOption.infoWindowEnable(true);
                aMap.addMarker(markerOption);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateView() {
        if (carInfo != null && !Utility.isEmpty(carInfo.getCarcard())) {
            SaveUtils.saveCar(carInfo);
            text_num.setText(carInfo.getCarcard());
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 100);
        } else {
            text_num.setText("绑定");
        }

    }

    /*******1分钟定位一次*****/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            qurycar();
            mHandler.postDelayed(this, 20 * 1000);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onError(Exception e) {
    }


    public void showDialog() {
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_user, null);
        EditText et_name = view.findViewById(R.id.et_name);
        et_name.setHint("请输入油价");
        et_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
                String oil = et_name.getText().toString();
                if (Utility.isEmpty(oil)) {
                    ToastUtil.showToast("油价不能为空");
                    return;
                }
                PreferenceUtils.setPrefString(getContext(), Constants.OIL, oil);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

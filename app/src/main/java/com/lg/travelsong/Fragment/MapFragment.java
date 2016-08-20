package com.lg.travelsong.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.lg.travelsong.R;
import com.lg.travelsong.bean.ResIdAndName;
import com.lg.travelsong.widget.CustomPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoYi on 2016/8/10
 */
public class MapFragment extends Fragment {
    private ActionBar ab;
    private CustomPopupWindow popupWindow;
    //    private String host;
    private View view_for_pop;
    private MapView mv_map;
    private BaiduMap mBaiduMap;
    private int trafficClick;
    private int heatClick;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        host = getArguments().getString("host");
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                popupWindow.showPopupWindow(view_for_pop);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        ab = getActivity().getActionBar();
        view_for_pop = view.findViewById(R.id.view_for_pop);
        mv_map = (MapView) view.findViewById(R.id.mv_map);

        //【定位】1.初始化LocationClient类
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        //隐藏百度logo
        View logo = mv_map.getChildAt(1);
        if (logo != null && (logo instanceof ImageView || logo instanceof ZoomControls)) {
            logo.setVisibility(View.GONE);
        }
        //获取百度地图控制器
        mBaiduMap = mv_map.getMap();
    }

    private void initData() {
        //初始化actionbar add按钮弹出框
        List<ResIdAndName> list = new ArrayList<>();
        ResIdAndName rn1 = new ResIdAndName(R.drawable.actionbar_add, getText(R.string.map_type_nomal).toString());
        ResIdAndName rn2 = new ResIdAndName(R.drawable.actionbar_back, getText(R.string.map_type_sallite).toString());
        ResIdAndName rn3 = new ResIdAndName(R.drawable.actionbar_more, getText(R.string.map_type_none).toString());
        ResIdAndName rn4 = new ResIdAndName(R.drawable.actionbar_search, getText(R.string.map_layer_traffic).toString());
        ResIdAndName rn5 = new ResIdAndName(R.drawable.antionbar_cancel, getText(R.string.map_layer_heat).toString());
        list.add(rn1);
        list.add(rn2);
        list.add(rn3);
        list.add(rn4);
        list.add(rn5);
        popupWindow = new CustomPopupWindow(getActivity(), list, new CustomPopupWindow.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                        break;
                    case 3:
                        if (++trafficClick % 2 == 1) {
                            mBaiduMap.setTrafficEnabled(true);
                        } else {
                            mBaiduMap.setTrafficEnabled(false);
                        }
                        break;
                    case 4:
                        if (++heatClick % 2 == 1) {
                            mBaiduMap.setBaiduHeatMapEnabled(true);
                        } else {
                            mBaiduMap.setBaiduHeatMapEnabled(false);
                        }
                        break;
                }
            }
        });
        ab.setTitle(R.string.map_title);
        //2.配置定位SDK参数
        initLocation();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //显示信息
        MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.FOLLOWING;
        boolean enableDirection = true;//设置允许显示方向
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.location);
        MyLocationConfiguration configuration = new MyLocationConfiguration(mode, true, customMarker);
        mBaiduMap.setMyLocationConfigeration(configuration);
        //比例尺设置为200m
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.setMapStatus(update);
        //4.开始定位
        mLocationClient.start();
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 3.实现BDLocationListener接口
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            if (location != null) {
                MyLocationData.Builder builder = new MyLocationData.Builder();
                builder.accuracy(location.getRadius());//设置精度
                builder.direction(location.getDirection());//设置方向
                builder.latitude(location.getLatitude());//设置纬度
                builder.longitude(location.getLongitude());//设置精度
                MyLocationData locationData = builder.build();
                mBaiduMap.setMyLocationData(locationData);//显示定位数据到地图上
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在Fragment执行onDestroyView时执行mMapView.onDestroy()，实现地图生命周期管理
        mv_map.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mv_map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在Fragment执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mv_map.onPause();
    }
}

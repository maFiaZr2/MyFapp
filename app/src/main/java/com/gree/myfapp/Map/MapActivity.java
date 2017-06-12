package com.gree.myfapp.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.gree.myfapp.R;

/**
 * Created by asus on 2016/9/22.
 */

public class MapActivity extends AppCompatActivity {
    private MapView mView;
    private BaiduMap baiduMap;
    private double longitude, latitude;
    private float radius, direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        mView = (MapView) findViewById(R.id.mView);
        longitude = getIntent().getDoubleExtra("longitude", 112.89);
        latitude = getIntent().getDoubleExtra("latitude", 98.55);
        radius = getIntent().getFloatExtra("radius", 50);
        direction = getIntent().getFloatExtra("direction", 100);
        baiduMap = mView.getMap();
        baiduMap.setMyLocationEnabled(true);
        Locationlistenner2 myListener = new Locationlistenner2();
        myListener.onReceiveLocation();
    }

    public class Locationlistenner2 {
        private void onReceiveLocation() {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(radius)
                    .direction(direction)
                    .longitude(longitude)
                    .latitude(latitude).build();
            baiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(latitude,
                    longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    protected void onPause() {
        mView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mView.onDestroy();
        mView = null;
        super.onDestroy();
    }

}

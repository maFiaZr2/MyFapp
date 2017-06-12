package com.gree.myfapp.Map;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by asus on 2016/11/1.
 */

public class ShowMap {
    private static BaiduMap baiduMap;
    private double longitude, latitude;
    private float radius, direction;
    private static ShowMap showMap;

    private static ShowMap getInstance() {
        if (showMap == null) {
            showMap = new ShowMap();
        }
        return showMap;
    }

    public static ShowMap with(MapView mv) {
        baiduMap = mv.getMap();
        baiduMap.setMyLocationEnabled(true);
        return getInstance();
    }

    public LocationShow show(BDLocation lc) {
        return new LocationShow(lc);
    }

    public class LocationShow {

        private LocationShow(BDLocation lc) {
            longitude = lc.getLongitude();
            latitude = lc.getLatitude();
            radius = lc.getRadius();
            direction = lc.getDirection();
        }

        public void get() {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(radius)
                    .direction(direction)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
            baiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(latitude, longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }
}

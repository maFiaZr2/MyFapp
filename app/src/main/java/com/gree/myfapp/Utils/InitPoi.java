package com.gree.myfapp.Utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

/**
 * Created by asus on 2016/11/6.
 */

public class InitPoi {
    private PoiSearch poiSearch = PoiSearch.newInstance();
    private static InitPoi initPoi;

    public static InitPoi getInstance() {
        if (initPoi == null) {
            initPoi = new InitPoi();
        }
        return initPoi;
    }

    public void NearBySearch(LatLng ll, String shopNameString, int Cap,int Page, OnGetPoiSearchResultListener listener) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.radius(2000);
        nearbySearchOption.location(ll);
        nearbySearchOption.keyword(shopNameString);
        nearbySearchOption.pageCapacity(Cap);
        nearbySearchOption.pageNum(Page);
        nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        poiSearch.searchNearby(nearbySearchOption);
        poiSearch.setOnGetPoiSearchResultListener(listener);
    }

    public void CitySearch(String city,String keyword, int page,OnGetPoiSearchResultListener listener) {
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(city);
        citySearchOption.keyword(keyword);
        citySearchOption.pageCapacity(10);
        citySearchOption.pageNum(page);
        poiSearch.searchInCity(citySearchOption);
        poiSearch.setOnGetPoiSearchResultListener(listener);

    }

    public void BoundSearch(LatLngBounds bound,String keyword, int page,OnGetPoiSearchResultListener listener) {
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
        boundSearchOption.bound(bound);
        boundSearchOption.keyword(keyword);
        boundSearchOption.pageCapacity(10);
        boundSearchOption.pageNum(page);
        poiSearch.searchInBound(boundSearchOption);
        poiSearch.setOnGetPoiSearchResultListener(listener);
    }

    public void DetailSearch(String uid,OnGetPoiSearchResultListener listener){
        PoiDetailSearchOption detailSearchOption = new PoiDetailSearchOption();
        detailSearchOption.poiUid(uid);
        poiSearch.searchPoiDetail(detailSearchOption);
        poiSearch.setOnGetPoiSearchResultListener(listener);
    }
}

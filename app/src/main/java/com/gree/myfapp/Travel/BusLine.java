package com.gree.myfapp.Travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.gree.myfapp.R;
import com.gree.myfapp.Utils.DividerItemDecoration;
import com.gree.myfapp.Utils.InitPoi;
import com.gree.myfapp.Utils.MyDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/7.
 */

public class BusLine extends AppCompatActivity implements
        OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener, BusAdapter.OnItemClickListener {
    private List<String> station = new ArrayList<>();
    private BusLineSearch mBusLineSearch;
    private BusAdapter adapter;
    private List<Bus> buses = new ArrayList<>();
    private LatLng ll;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.test);

        Intent intent = getIntent();
        ll = new LatLng(intent.getDoubleExtra("latitude", 11.22),
                intent.getDoubleExtra("longitude", 11.22));
        InitPoi.getInstance().NearBySearch(ll, "80", 10, 0, this);

        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);

        Button btn = (Button) findViewById(R.id.busBtn);
        Button btn1 = (Button) findViewById(R.id.change);
        textView = (TextView) findViewById(R.id.to);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.busRecycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(BusLine.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(BusLine.this, LinearLayoutManager.VERTICAL));
        adapter = new BusAdapter(buses);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (station.get(0) != null) {
                    mBusLineSearch.searchBusLine((new BusLineSearchOption()
                            .city("珠海").uid(station.get(0))));
                } else {
                    MyDialog.with(BusLine.this).toast("没有结果");
                }

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (station.get(1) != null) {
                    mBusLineSearch.searchBusLine((new BusLineSearchOption()
                            .city("珠海").uid(station.get(1))));
                } else {
                    MyDialog.with(BusLine.this).toast("没有结果");
                }
            }
        });
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        List<PoiInfo> infos = poiResult.getAllPoi();
        PoiInfo info = new PoiInfo();
        station.clear();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).type == PoiInfo.POITYPE.BUS_LINE) {
                station.add(infos.get(i).uid);
            }
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        buses.clear();
        int size = result.getStations().size();
        for (int i = 0; i < size; i++) {
            Bus bus = new Bus();
            bus.setName(i + "  " + result.getStations().get(i).getTitle());
            bus.setLocation(result.getStations().get(i).getLocation());
            if (DistanceUtil.getDistance(ll, result.getStations().get(i).getLocation()) < 500) {
                bus.setyLocation("you in here");
            }
            buses.add(bus);
        }
        textView.setText(result.getStations().get(0).getTitle() + "-->" +
                result.getStations().get(size - 1).getTitle());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemClick(View view, int poisition) {
        MyDialog.with(BusLine.this).toast("you click the " + poisition);
    }
}
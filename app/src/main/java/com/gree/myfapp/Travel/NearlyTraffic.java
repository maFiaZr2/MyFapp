package com.gree.myfapp.Travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.gree.myfapp.R;
import com.gree.myfapp.Utils.DividerItemDecoration;
import com.gree.myfapp.Utils.InitPoi;
import com.gree.myfapp.Utils.MyDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by asus on 2016/11/13.
 */

public class NearlyTraffic extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private List<List<PoiInfo>> BusStation = new ArrayList<>();
    private List<BusInfo> busInfo = new ArrayList<>();
    private BusStationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        Intent intent = getIntent();
        LatLng ll = new LatLng(intent.getDoubleExtra("latitude", 11.22),
                intent.getDoubleExtra("longitude", 11.22));
        InitView();
        InitPoi.getInstance().NearBySearch(ll, "公交", 30, 0, NearlyTraffic.this);
    }

    private void InitView() {
        RecyclerView cycler = (RecyclerView) findViewById(R.id.recycle);
        cycler.setLayoutManager(new LinearLayoutManager(this));
        cycler.setItemAnimator(new DefaultItemAnimator());
        cycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new BusStationAdapter(busInfo);
        cycler.setAdapter(adapter);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            MyDialog.with(NearlyTraffic.this).toast("未找到结果");
            return;
        }
        //获取所有的Poi
        List<PoiInfo> infos = poiResult.getAllPoi();
        //获取所有的公交站Poi
        List<PoiInfo> infos2 = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).type == PoiInfo.POITYPE.BUS_STATION) {
                infos2.add(infos.get(i));
            }
        }
        //设置公交站List的List的第一个值
        List<PoiInfo> ssss = new ArrayList<>();
        ssss.add(infos2.get(0));
        BusStation.add(ssss);
        //往BusStation中增加公交站信息
        for (int i = 1; i < infos2.size(); i++) {
            busstation(infos2.get(i));
        }
        //相同公交站不同线路放在一起，并且去除重复值
        for (int i = 0; i < BusStation.size(); i++) {
            BusInfo businfo = new BusInfo();
            businfo.setName(BusStation.get(i).get(0).name);
            List<String> lines = new ArrayList<>();
            for (int b = 0; b < BusStation.get(i).size(); b++) {
                String[] line = new String[30];
                line = BusStation.get(i).get(b).address.split(";");
                for (int j = 0; j < line.length; j++) {
                    lines.add(line[j]);
                }
            }
            businfo.setLine(quchong(lines));
            busInfo.add(businfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void busstation(PoiInfo pi) {
        for (int i = 0; i < BusStation.size(); i++) {
            if ((pi.name).equals(BusStation.get(i).get(0).name)) {
                BusStation.get(i).add(pi);
                break;
            } else {
                if (i == BusStation.size() - 1) {
                    List<PoiInfo> s = new ArrayList<>();
                    s.add(pi);
                    BusStation.add(s);
                    break;
                }
            }
        }
    }

    private String quchong(List<String> info) {
        List<String> s = new ArrayList<>(new HashSet<>(info));
        String out = "";
        for (int i = 0; i < s.size(); i++) {
            out = out + s.get(i) + ",";
        }
        return out;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
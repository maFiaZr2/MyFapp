package com.gree.myfapp.Eat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.gree.myfapp.MyApplication;
import com.gree.myfapp.R;
import com.gree.myfapp.Utils.InitPoi;
import com.gree.myfapp.Utils.DividerItemDecoration;
import com.gree.myfapp.Utils.MyDialog;
import com.gree.myfapp.Utils.PhoneDisplay;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asus on 2016/10/16.
 */

public class EatActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private RecyclerView mRecyclerView;
    private List<Eat> eatList = new ArrayList<>();
    private EatAdapter eatAdapter;
    private boolean isLoading;
    private int index = 1;
    private int totalPage;
    private Handler handler = new Handler();
    private LatLng ll;
    private String number;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.recyclerview);
        initRecyclerView();
        initEat(0);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration
                (this, LinearLayoutManager.VERTICAL));
        eatAdapter = new EatAdapter(eatList);
        mRecyclerView.setAdapter(eatAdapter);
        setHeader(mRecyclerView);
        eatAdapter.setOnItemClickListener(new EatAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position, Eat eat) {
//                eatAdapter.removeItem(eat);
                ReadDetail(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                eatAdapter.removeItem(position);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisiableItemPosition = manager.findLastVisibleItemPosition();
                if (lastVisiableItemPosition + 1 == eatAdapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                requestLoadMoreData();
//                                mRecyclerView.scrollToPosition(11);
                                isLoading = false;
                                Toast.makeText(EatActivity.this, "newState=="+newState, Toast.LENGTH_SHORT).show();
//                                eatAdapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                }
            }
        });
//下方注释的代码用来解决headerview和footerview加载到头一个或者最后一个item  而不是占据一行的bug
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(gridLayoutManager);
//        // gridLayoutManager  布局管理器
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                //如果是第一个(添加HeaderView)   还有就是最后一个(FooterView)
//                return position == eatList.size() + 1 || position == 0 ? gridLayoutManager.getSpanCount() : 1;
//            }
//        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
//                        mRecyclerView.removeAllViews();
                        eatList.clear();
                        initEat(0);
                        Toast.makeText(EatActivity.this, "swipeRefresh==ok", Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });
    }

    private void initEat(int Page) {
        Toast.makeText(this, "Page==" + Page, Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        ll = new LatLng(intent.getDoubleExtra("latitude", 11.22),
                intent.getDoubleExtra("longitude", 11.22));
        String shopNameString = intent.getStringExtra("shopName");
        InitPoi.getInstance().NearBySearch(ll, shopNameString, 10, Page, this);
        eatAdapter.notifyDataSetChanged();

    }

    private void requestLoadMoreData() {
//        mRecyclerView.scrollToPosition(11);
        index++;
        Toast.makeText(this, "xxxxx" + index, Toast.LENGTH_SHORT).show();
        if (index < totalPage) {
            initEat(index);
        } else {
            MyDialog.with(EatActivity.this).toast("已经没有了");
        }

        eatAdapter.notifyItemRemoved(eatAdapter.getItemCount());
    }

    public void setHeader(RecyclerView view) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_header, view, false);
        TextView header = (TextView) view.findViewById(R.id.headerText);
        eatAdapter.setHeaderView(headerView);
    }

    private void ReadDetail(int position) {
        InitPoi.getInstance().DetailSearch(eatList.get(position).getUid(), this);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            MyDialog.with(EatActivity.this).toast("未找到结果");
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> list = poiResult.getAllPoi();
            totalPage = poiResult.getTotalPageNum();
            for (int i = 0; i < list.size(); i++) {
                Eat eat = new Eat();
                LatLng ll1 = list.get(i).location;
                double distance = DistanceUtil.getDistance(ll, ll1);
                eat.setName(list.get(i).name);
                eat.setAddress(list.get(i).address);
                eat.setDistance(distance);
                eat.setUid(list.get(i).uid);
                eatList.add(eat);
            }
        }
        eatAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            MyDialog.with(EatActivity.this).toast("未找到结果");
        } else {
            showPopWindow(poiDetailResult);
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    private void showPopWindow(PoiDetailResult poiDetailResult) {
        View view = LayoutInflater.from(MyApplication.getContext())
                .inflate(R.layout.popwindow, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView tag = (TextView) view.findViewById(R.id.tag);
        TextView overallrating = (TextView) view.findViewById(R.id.OverallRating);
        TextView tel = (TextView) view.findViewById(R.id.tel);
        Button btn = (Button) view.findViewById(R.id.btn);
        if (TextUtils.isEmpty(poiDetailResult.getTelephone())) {
            btn.setVisibility(View.INVISIBLE);
        }
        number = poiDetailResult.getTelephone();
        name.setText(poiDetailResult.getName());
        tag.setText(poiDetailResult.getTag());
        if (TextUtils.isEmpty(poiDetailResult.getTelephone())) {
            tel.setText("暂无电话");
        } else {
            tel.setText("电话：" + poiDetailResult.getTelephone());
        }

        if (poiDetailResult.getOverallRating() == 0.0) {
            overallrating.setText("暂无评价");
        } else {
            overallrating.setText("综合评价：" + String.valueOf(poiDetailResult.getOverallRating()));
        }
        if (poiDetailResult.getPrice() == 0.0) {
            price.setText("暂无价格参考");
        } else {
            price.setText("人均价格：" + String.valueOf(poiDetailResult.getPrice()));
        }
        PhoneDisplay pd = new PhoneDisplay(getWindowManager().getDefaultDisplay());
        PopupWindow pop = new PopupWindow(view, pd.getWidth() * 2 / 3, pd.getHeight() / 2);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.showAtLocation(findViewById(R.id.recycle), Gravity.CENTER, 0, 0);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:" + number);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

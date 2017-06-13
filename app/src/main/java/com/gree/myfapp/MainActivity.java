package com.gree.myfapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapView;
import com.gree.myfapp.Eat.EatActivity;
import com.gree.myfapp.Map.ShowMap;
import com.gree.myfapp.Travel.BusLine;
import com.gree.myfapp.Travel.NearlyTraffic;
import com.gree.myfapp.Utils.DividerItemDecoration;
import com.gree.myfapp.Utils.LocationService;
import com.gree.myfapp.Utils.MyDialog;
import com.gree.myfapp.Weather.GetWeather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LocationService ls = new LocationService(this);
    private BDLocationListener myListener = new MyLocationListener();
    //    private TextView[] weatherText = new TextView[3];
//    private ImageView[] image = new ImageView[3];
    private Button[] buttons = new Button[5];
    private double longitude, latitude;
    private String locationInfo;
    private TextView textView;
    private MapView mapView;
    private ArrayList<String> weatherData;
    private ArrayList<String> index;
    private ArrayList<String> picUrl;
    private Handler handler = new Handler();
    private RecyclerView weatherList;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        initComponents();
        initLocation();
    }

    private void initComponents() {
        textView = (TextView) findViewById(R.id.show_position);
        buttons[0] = (Button) findViewById(R.id.eat_button);
        buttons[1] = (Button) findViewById(R.id.play_button);
        buttons[2] = (Button) findViewById(R.id.live_button);
        buttons[3] = (Button) findViewById(R.id.walk_button);
        buttons[4] = (Button) findViewById(R.id.control_button);
        mapView = (MapView) findViewById(R.id.mView);
        weatherList = (RecyclerView) findViewById(R.id.weatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        for (Button b : buttons) {
            b.setOnClickListener(this);
        }
    }

    public void initLocation() {
        MyDialog.with(MainActivity.this).progressDialog("更新中...");
        ls.registerListener(myListener);
        ls.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.control_button:
                initLocation();
                break;
            case R.id.eat_button:
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.selecteat, null);
                TextView select = (TextView) layout.findViewById(R.id.select);
                TextView nearly = (TextView) layout.findViewById(R.id.nearly);
                final EditText edit = (EditText) layout.findViewById(R.id.shopEdit);
                Button btn = (Button) layout.findViewById(R.id.search);
                new AlertDialog.Builder(this).setView(layout).show();
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setItems(getResources().getStringArray(R.array.selectItem),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                switch (i) {
                                                    case 0:
                                                        MyDialog.with(MainActivity.this).toast("0");
                                                        break;
                                                    case 1:
                                                        MyDialog.with(MainActivity.this).toast("1");
                                                        break;
                                                    case 2:
                                                        MyDialog.with(MainActivity.this).toast("2");
                                                        break;
                                                    case 3:
                                                        MyDialog.with(MainActivity.this).toast("3");
                                                        break;
                                                }
                                            }

                                        }).show();
                    }
                });
                nearly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setItems(getResources().getStringArray(R.array.nearyItem),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, EatActivity.class);
                        String shopName = edit.getText().toString();
                        if (!TextUtils.isEmpty(shopName)) {
                            intent.putExtra("shopName", shopName);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("latitude", latitude);
                            startActivity(intent);
                        } else {
                            MyDialog.with(MainActivity.this).toast("请输入");
                        }
                    }
                });
                break;
            case R.id.play_button:
                Intent intent3 = new Intent(this, NearlyTraffic.class);
                intent3.putExtra("longitude", longitude);
                intent3.putExtra("latitude", latitude);
                startActivity(intent3);
                break;
            case R.id.live_button:
                Intent intent2 = new Intent(this, BusLine.class);
                intent2.putExtra("longitude", longitude);
                intent2.putExtra("latitude", latitude);
                startActivity(intent2);
                break;
            case R.id.walk_button:
            default:
                break;
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            MyDialog.with(MainActivity.this).dismiss();
            if (location.getLocType() == BDLocation.TypeNetWorkLocation ||
                    location.getLocType() == BDLocation.TypeGpsLocation) {// 网络定位结果
                ShowMap.with(mapView).show(location).get();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.i("location", "onReceiveLocation: " + longitude + latitude);
                locationInfo = "时间：" + location.getTime() +
                        "\n位置：" + location.getAddrStr() +
                        "\n" + location.getLocationDescribe() + "\n";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        weatherData = GetWeather.withUrl(longitude, latitude).WeatherData();
                        index = GetWeather.withUrl(longitude, latitude).Index();
                        picUrl = GetWeather.withUrl(longitude, latitude).PicUrl();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuffer sb = new StringBuffer();
                                sb.append(locationInfo);
                                for (String s : index) {
                                    sb.append(s);
                                }
                                textView.setText(sb);
                                weatherListAdapter weatherAdaper = new weatherListAdapter(weatherData, picUrl);
                                weatherList.setAdapter(weatherAdaper);
                                weatherAdaper.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            } else {
                closeDialog2();
            }
            ls.stop();
        }
    }

    private void closeDialog2() {
        MyDialog.with(MainActivity.this).alertDialog()
                .btn(-1, "打开网络", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }).btn(-2, "打开GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }).btn(-3, "重新定位", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initLocation();
            }
        }).set("更新失败", false);
        ls.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ls.stop();
        mapView.onDestroy();
        mapView = null;
    }

    private class weatherListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final ArrayList<String> weatherData;
        private final ArrayList<String> picUrl;

        public weatherListAdapter(ArrayList<String> weatherData, ArrayList<String> picUrl) {
            this.weatherData = weatherData;
            this.picUrl = picUrl;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weatherlist, parent, false);
            return new ViewHolder(view);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.weatherImage);
                textView = (TextView) itemView.findViewById(R.id.weatherText);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder) {
                ViewHolder myHolder = (ViewHolder) holder;
                Picasso.with(MyApplication.getContext()).load(picUrl.get(position)).fit().into(myHolder.imageView);
                myHolder.textView.setText(weatherData.get(position));
            }


        }

        @Override
        public int getItemCount() {
            return picUrl.size();
        }
    }
}
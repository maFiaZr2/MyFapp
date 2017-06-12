package com.gree.myfapp.Weather;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.Gson;
import com.gree.myfapp.MyApplication;
import com.gree.myfapp.R;
import com.gree.myfapp.Utils.GetJsonDatas;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/3.
 */

public class GetWeather {
    private static GetWeather weather;
    private static List<WeatherBean.ResultsBean.IndexBean> index;
    private static List<WeatherBean.ResultsBean.WeatherDataBean> weatherData;

    private static GetWeather getInstance() {
        if (weather == null) {
            weather = new GetWeather();
        }
        return weather;
    }

    public static GetWeather withUrl(double lg, double lt) {
        try {
            Gson gson = new Gson();
            URL url = new URL(String.format(MyApplication.getContext()
                            .getResources().getString(R.string.weatherurl),
                    Double.toString(lg), Double.toString(lt)));
            WeatherBean bean = gson.fromJson(GetJsonDatas.get(url), WeatherBean.class);
            index = bean.getResults().get(0).getIndex();
            weatherData = bean.getResults().get(0).getWeather_data();
            Log.i("gw", "withUrl: " + bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getInstance();
    }

    public ArrayList<String> WeatherData() {
        ArrayList<String> weatherdata = new ArrayList<>();
        for (int i = 0; i < weatherData.size(); i++) {
            if (i != 0) {
                weatherdata.add(weatherData.get(i).getDate() + "\n\n" +
                        weatherData.get(i).getWeather() + "\n" +
                        weatherData.get(i).getTemperature());
            } else {
                weatherdata.add(weatherData.get(i).getDate() + "\n" +
                        weatherData.get(i).getWeather() + "\n" +
                        weatherData.get(i).getTemperature());
            }
        }
        return weatherdata;
    }

    public ArrayList<String> Index() {
        ArrayList<String> indexInfo = new ArrayList<>();
        for (int i = 0; i < index.size(); i++) {
            indexInfo.add(index.get(i).getTipt() + ":\n" + index.get(i).getDes() + "\n");
        }
        return indexInfo;
    }

    public ArrayList<String> PicUrl() {
        ArrayList<String> picUrl = new ArrayList<>();
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        for (int i = 0; i < weatherData.size(); i++) {
            if ((hour >= 6) && (hour <= 18)) {
                picUrl.add(weatherData.get(i).getDayPictureUrl());
            } else {
                picUrl.add(weatherData.get(i).getNightPictureUrl());
            }
        }
        return picUrl;
    }
}

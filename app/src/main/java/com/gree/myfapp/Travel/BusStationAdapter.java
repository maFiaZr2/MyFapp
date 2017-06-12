package com.gree.myfapp.Travel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gree.myfapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/17.
 */

public class BusStationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BusInfo> busInfos = new ArrayList<>();

    public BusStationAdapter(List<BusInfo> datas) {
        this.busInfos = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.busstation.setText(busInfos.get(position).getName());
        viewHolder.busline.setText(busInfos.get(position).getLine());
    }

    @Override
    public int getItemCount() {
        return busInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView busstation;
        TextView busline;

        public ViewHolder(View itemView) {
            super(itemView);
            busstation = (TextView) itemView.findViewById(R.id.title);
            busline = (TextView) itemView.findViewById(R.id.stationLocation);
            busline.setVisibility(View.VISIBLE);
        }
    }
}

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
 * Created by asus on 2016/11/8.
 */

public class BusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Bus> busList = new ArrayList<>();
    private OnItemClickListener mClick;

    public BusAdapter(List<Bus> list) {
        busList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (busList == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.title.setText(busList.get(position).getName());
        viewHolder.location.setText(busList.get(position).getLocation().toString());
        viewHolder.yLocation.setText(busList.get(position).getyLocation());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    mClick.OnItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView location;
        TextView yLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            location = (TextView) itemView.findViewById(R.id.stationLocation);
            yLocation = (TextView) itemView.findViewById(R.id.yourLocation);
        }
    }

    interface OnItemClickListener{
        void OnItemClick(View view,int poisition);
    }

    public void setOnClickListener(OnItemClickListener onClickListener){
        this.mClick = onClickListener;
    }
}

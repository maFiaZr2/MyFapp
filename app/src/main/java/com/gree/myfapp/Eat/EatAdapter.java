package com.gree.myfapp.Eat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gree.myfapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/10/16.
 */

public class EatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Eat> eatlist = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;
    private View mHeaderView;
    private OnItemClickListener mOnItemClickListener;

    public EatAdapter(List<Eat> eatList) {
        this.eatlist = eatList;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null)
            return TYPE_NORMAL;
        if (position == 0)
            return TYPE_HEADER;
        if (mHeaderView != null && position + 1 == getItemCount())
            return TYPE_FOOTER;
        if (mHeaderView == null && position == getItemCount())
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ViewHolder(mHeaderView);
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            return new FooterViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        if (holder instanceof ViewHolder) {
            ViewHolder holder1 = (ViewHolder) holder;
            if (pos == eatlist.size()) {
                return;
            }
            holder1.name.setText(eatlist.get(pos).getName());
            holder1.address.setText(eatlist.get(pos).getAddress());
            holder1.distance.setText(String.valueOf((int) eatlist.get(pos).getDistance()) + "米");
            holder1.price.setText(String.valueOf(eatlist.get(pos).getPrice()));
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.OnItemClick(view, pos, eatlist.get(pos));
                    }
                }
            });
            holder1.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemLongClick(view, pos);
                    }
                    return true;
                }
            });
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? eatlist.size() + 1 : eatlist.size() + 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView address;
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.shopName);
            price = (TextView) itemView.findViewById(R.id.money);
            address = (TextView) itemView.findViewById(R.id.address);
            distance = (TextView) itemView.findViewById(R.id.distance);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void OnItemClick(View view, int position, Eat eat);

        void onItemLongClick(View view, int poisition);
    }

//    public void addItem(Eat eat, int position) {
//        eatlist.add(position, eat);
//        notifyItemInserted(position); //Attention!
//    }
//
//    public void removeItem(int position) {
//        eatlist.remove(position);
//        notifyItemRemoved(position);//Attention!
//    }
}

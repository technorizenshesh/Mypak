package com.mypakuser.newmodule.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mypakuser.R;
import com.mypakuser.databinding.AdapterOrderHistoryBinding;
import com.mypakuser.models.ModelOrderHistory;
import com.mypakuser.newmodule.activities.NearByDriverAct;

import java.util.ArrayList;

public class AdapterOrderHistory extends RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder> {

    Context mContext;
    ArrayList<ModelOrderHistory.Result> orderList;

    public AdapterOrderHistory(Context mContext, ArrayList<ModelOrderHistory.Result> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterOrderHistoryBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.adapter_order_history,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelOrderHistory.Result data = orderList.get(position);
        holder.binding.setData(data);
        holder.binding.btRequestDriver.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, NearByDriverAct.class)
             .putExtra("id",data.getId())
            );
        });
    }

    @Override
    public int getItemCount() {
        return orderList==null?0:orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterOrderHistoryBinding  binding;
        public MyViewHolder(@NonNull AdapterOrderHistoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}

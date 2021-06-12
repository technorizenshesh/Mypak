package com.mypakuser.newmodule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mypakuser.R;
import com.mypakuser.databinding.AdapterNearbydriverBinding;
import com.mypakuser.databinding.AdapterOrderHistoryBinding;
import com.mypakuser.models.ModelNearByDrivers;
import com.mypakuser.models.ModelOrderHistory;
import com.mypakuser.utils.ProjectUtil;

import java.util.ArrayList;

public class AdapterNearByDriver extends RecyclerView.Adapter<AdapterNearByDriver.MyViewHolder> {

    Context mContext;
    ArrayList<ModelNearByDrivers.Result> orderList;
    double lat1,lon1;

    public AdapterNearByDriver(Context mContext, ArrayList<ModelNearByDrivers.Result> orderList
            ,double lat1,double lon1) {
        this.mContext = mContext;
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdapterNearByDriver.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterNearbydriverBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.adapter_nearbydriver,parent,false);
        return new AdapterNearByDriver.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNearByDriver.MyViewHolder holder, int position) {
        ModelNearByDrivers.Result data = orderList.get(position);
        holder.binding.setData(data);
        holder.binding.tvDistance.setText(ProjectUtil.CalculationByDistance(lat1,lon1,
                Double.parseDouble(data.getLat()),Double.parseDouble(data.getLon()))+"");

        holder.binding.ivArrow.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return orderList==null?0:orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNearbydriverBinding  binding;
        public MyViewHolder(@NonNull AdapterNearbydriverBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}


package com.mypakuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mypakuser.R;
import com.mypakuser.activities.ParcelDetailAct;
import com.mypakuser.databinding.AdapterShipingParcelsBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelShipRequest;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.SharedPref;

import java.util.ArrayList;

public class AdapterShipRequest extends RecyclerView.Adapter<AdapterShipRequest.StoreHolder> {

    Context mContext;
    ArrayList<ModelShipRequest.Result> storeList;
    SharedPref sharedPref;
    ModelLogin modelLogin;

    public AdapterShipRequest(Context mContext, ArrayList<ModelShipRequest.Result> storeList) {
        this.mContext = mContext;
        this.storeList = storeList;
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
    }

    @NonNull
    @Override
    public AdapterShipRequest.StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterShipingParcelsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.adapter_shiping_parcels,parent,false);
        return new AdapterShipRequest.StoreHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShipRequest.StoreHolder holder, int position) {
        ModelShipRequest.Result data = storeList.get(position);

        holder.binding.setParcel(data);

        if("Accept".equals(data.getBid_status())) {
            // holder.binding.btChat.setVisibility(View.VISIBLE);
        } else if("Pickup".equals(data.getBid_status())) {
            // holder.binding.btChat.setVisibility(View.VISIBLE);
        } else if("Delivered".equals(data.getBid_status())) {
            // holder.binding.btChat.setVisibility(View.VISIBLE);
        } else {
            // holder.binding.btChat.setVisibility(View.GONE);
        }

        holder.binding.btParcel.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, ParcelDetailAct.class)
                    .putExtra("parcelid",data.getId())
            );
        });

    }

    @Override
    public int getItemCount() {
        return storeList == null?0:storeList.size();
    }

    public class StoreHolder extends RecyclerView.ViewHolder{

        AdapterShipingParcelsBinding binding;

        public StoreHolder(AdapterShipingParcelsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


}




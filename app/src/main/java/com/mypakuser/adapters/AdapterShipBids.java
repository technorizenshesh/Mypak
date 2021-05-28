package com.mypakuser.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mypakuser.R;
import com.mypakuser.activities.HomeAct;
import com.mypakuser.activities.ParcelDetailAct;
import com.mypakuser.databinding.AdapterShipBidsBinding;
import com.mypakuser.databinding.BidDetailDialogBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelShipBid;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterShipBids extends RecyclerView.Adapter<AdapterShipBids.MyBidsViewHolder> {

    Context mContext;
    ArrayList<ModelShipBid.Result> bidList;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    String ShipUserId;
    UpdateBidInterf updateBidInterf;

    public AdapterShipBids(Context mContext, ArrayList<ModelShipBid.Result> bidList,String ShipUserId,UpdateBidInterf updateBidInterf) {
        this.mContext = mContext;
        this.bidList = bidList;
        this.ShipUserId = ShipUserId;
        this.updateBidInterf = updateBidInterf;
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
    }

    @NonNull
    @Override
    public MyBidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterShipBidsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext)
                ,R.layout.adapter_ship_bids,parent,false);
        return new MyBidsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBidsViewHolder holder, int position) {
        ModelShipBid.Result data = bidList.get(position);
        holder.binding.setBids(data);

        holder.binding.llRoot.setOnClickListener(v -> {
            openBidDetailDialog(data);
        });

    }

    private void openBidDetailDialog(ModelShipBid.Result data) {
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        BidDetailDialogBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext)
                        , R.layout.bid_detail_dialog,
                        null,
                        false);
        dialog.setContentView(dialogBinding.getRoot());

        Picasso.get().load(data.getImage())
                .placeholder(R.drawable.default_profile_icon)
                .error(R.drawable.default_profile_icon)
                .into(dialogBinding.bidUserProfile);

        dialogBinding.tvUsername.setText(data.getName());
        dialogBinding.pickDate.setText(data.getPick_date());
        dialogBinding.dropDate.setText(data.getDrop_date());
        dialogBinding.comment.setText(data.getComment());

        dialogBinding.ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogBinding.btAccept.setOnClickListener(v -> {
            acceptRejectBids(data,dialog,"Accept");
        });

        dialogBinding.btReject.setOnClickListener(v -> {
            acceptRejectBids(data,dialog,"Cancel");
        });

        dialog.show();

    }

    private void acceptRejectBids(ModelShipBid.Result data,Dialog dialog,String status) {
        ProjectUtil.showProgressDialog(mContext,false,mContext.getString(R.string.please_wait));
        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);

        HashMap<String,String> param = new HashMap<>();
        param.put("status",status);
        param.put("bid_id",data.getId());

        Call<ResponseBody> call = api.acceptCancelUserApiCall(param);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String stringRes = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringRes);
                    if(jsonObject.getString("status").equals("1")) {
                        dialog.dismiss();
                        Log.e("stringRes","stringRes = " + stringRes);
                        ((ParcelDetailAct)mContext).finish();
                        mContext.startActivity(new Intent(mContext, HomeAct.class));
                        // updateBidInterf.updateBid(status);
                    } else {}
                } catch (Exception e) {}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProjectUtil.pauseProgressDialog();
            }
        });
    }

    public interface UpdateBidInterf {
        void updateBid(String str);
    }

    @Override
    public int getItemCount() {
        return bidList == null?0:bidList.size();
    }

    public class MyBidsViewHolder extends RecyclerView.ViewHolder {

        AdapterShipBidsBinding binding;
        public MyBidsViewHolder(@NonNull AdapterShipBidsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


}

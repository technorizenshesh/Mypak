package com.mypakuser.newmodule.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.activities.ParcelDetailAct;
import com.mypakuser.databinding.ActivityParcelStatusBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelOrderHistory;
import com.mypakuser.models.ModelVehicles;
import com.mypakuser.newmodule.adapters.AdapterOrderHistory;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelStatusAct extends AppCompatActivity {

    Context mContext = ParcelStatusAct.this;
    ActivityParcelStatusBinding binding;
    SharedPref sharedPref;
    ModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_parcel_status);
        ProjectUtil.changeStatusBarColor(ParcelStatusAct.this);
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
        init();

    }

    private void init() {

        binding.swipLayout.setRefreshing(true);
        getOrderHistory();

        binding.swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderHistory();
            }
        });

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

    }

    private void getOrderHistory() {
        // ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));

        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",modelLogin.getResult().getId());

        Call<ResponseBody> call = api.getOrderHistoryCall(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                binding.swipLayout.setRefreshing(false);
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {
                        ModelOrderHistory modelOrderHistory = new Gson().fromJson(responseString,ModelOrderHistory.class);
                        AdapterOrderHistory adapterOrderHistory = new AdapterOrderHistory(mContext,modelOrderHistory.getResult());
                        binding.rvOrderHistory.setAdapter(adapterOrderHistory);
                    } else {}

                } catch (Exception e) {
                    Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Exception","Exception = " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProjectUtil.pauseProgressDialog();
                binding.swipLayout.setRefreshing(false);
            }

        });
    }

}
package com.mypakuser.newmodule.parcelsfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.activities.HomeAct;
import com.mypakuser.databinding.FragmentParcelPayBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelVehicles;
import com.mypakuser.newmodule.activities.ParcelStatusAct;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.ProjectUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ParcelPayFragment extends Fragment {

    Context mContext;
    FragmentParcelPayBinding binding;
    HashMap<String,String> params;
    HashMap<String,File> fileParam;
    String vehicleId="";
    double totalRouteInKm = 0.0,picLat=0.0,pickLon=0.0,dropLat=0.0,dropLon=0.0;

    public ParcelPayFragment(HashMap<String, String> params, HashMap<String, File> fileParam) {
        this.params = params;
        this.fileParam = fileParam;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parcel_pay, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {

        params.put("parcel_category","");
        params.put("item_detail","");

        Log.e("dadfasdfasd","params " + params);
        Log.e("dadfasdfasd","fileParam " + fileParam);

        picLat = Double.parseDouble(params.get("pickup_lat"));
        pickLon = Double.parseDouble(params.get("pickup_lon"));
        dropLat = Double.parseDouble(params.get("drop_lat"));
        dropLon = Double.parseDouble(params.get("drop_lon"));

        totalRouteInKm = ProjectUtil.CalculationByDistance(picLat,pickLon,dropLat,dropLon);
        binding.distance.setText(String.format("%.2f",totalRouteInKm) + "Km");
        Log.e("dadfasdfasd","totalRouteInKm =" + totalRouteInKm);

        getVehicleList();

        binding.vehicleName.setText(params.get("vehicle_id"));

        binding.rbCashCheck.setOnClickListener(v -> {
            binding.rbOnline.setChecked(false);
        });

        binding.rbOnline.setOnClickListener(v -> {
            binding.rbCashCheck.setChecked(false);
        });

        binding.btNext.setOnClickListener(v -> {
            if(binding.rbCashCheck.isChecked()) {
                params.put("payment_type","Cash");
                submitRequest();
            } else if(binding.rbOnline.isChecked()) {
                params.put("payment_type","Online");
                submitRequest();
            } else {
                Toast.makeText(mContext,getString(R.string.please_select_pay_menthod), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getVehicleList() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));

        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        Call<ResponseBody> call = api.getAllCars();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {
                        ModelVehicles modelVehicles = new Gson().fromJson(responseString,ModelVehicles.class);

                        for(int i=0;i<modelVehicles.getResult().size();i++){
                            if(modelVehicles.getResult().get(i).getCar_name().trim().toLowerCase()
                                    .equals(params.get("vehicle_id").trim().toLowerCase())) {
                                vehicleId = modelVehicles.getResult().get(i).getId();
                                double chargePerKm = Double.parseDouble(modelVehicles.getResult().get(i).getCharge());
                                binding.vehiclePrice.setText("$"+chargePerKm);

                                double totalPayment = chargePerKm * totalRouteInKm;
                                params.put("total_price",String.valueOf(totalPayment));

                                binding.totalPay.setText("$"+totalPayment);

                                Log.e("dadfasdfasd","chargePerKm = "+ chargePerKm);
                                Log.e("dadfasdfasd","totalPayment = "+ totalPayment);

                            }
                        }

                    } else {}

                } catch (Exception e) {
                    Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Exception","Exception = " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProjectUtil.pauseProgressDialog();
            }

        });
    }

    private void submitRequest() {

        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        AndroidNetworking.upload(AppConstant.BASE_URL + "shipping_request")
                .addMultipartFile(fileParam)
                .addMultipartParameter(params)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("1")) {
                                Log.e("response","response = "+ response);
                                getActivity().finish();
                                startActivity(new Intent(mContext,ParcelStatusAct.class));
                            } else {}

                        } catch (Exception e) {
                            Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Exception","Exception = " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

}
package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.adapters.AdapterShipBids;
import com.mypakuser.databinding.ActivityParcelDetailBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelShipBid;
import com.mypakuser.models.ModelShipDetail;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.DrawPollyLine;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelDetailAct extends AppCompatActivity implements OnMapReadyCallback {

    Context mContext = ParcelDetailAct.this;
    ActivityParcelDetailBinding binding;
    String parcelId = "";
    GoogleMap gMap;
    SupportMapFragment mapFragment;
    MarkerOptions originOption,dropOffOption;
    LatLng originLatLng,dropLatLng;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    long pickUpDateMilli = 0;
    AdapterShipBids adapterShipBids;
    ModelShipBid modelShipBid;
    boolean orderType;
    private PolylineOptions lineOptions;
    private String shippmentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_parcel_detail);
        ProjectUtil.changeStatusBarColor(ParcelDetailAct.this);
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
        parcelId = getIntent().getStringExtra("parcelid");

        init();

        shipDetailApi();
        getAllBidsNotLoader();
    }

    private void init() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ParcelDetailAct.this);

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

    }

    private void shipDetailApi() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        HashMap<String,String> params = new HashMap<>();
        params.put("shipping_id",parcelId);

        Call<ResponseBody> call = api.getShipDetailApiCall(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {

                        ModelShipDetail modelShipDetail = new Gson().fromJson(responseString,ModelShipDetail.class);
                        binding.setShip(modelShipDetail.getResult());
                        shippmentUserId = modelShipDetail.getResult().getUser_id();

                        originLatLng = new LatLng(Double.parseDouble(modelShipDetail.getResult().getPickup_lat())
                                ,Double.parseDouble(modelShipDetail.getResult().getPickup_lon()));

                        dropLatLng = new LatLng(Double.parseDouble(modelShipDetail.getResult().getDrop_lat())
                                ,Double.parseDouble(modelShipDetail.getResult().getDrop_lon()));

                        originOption = new MarkerOptions().position(originLatLng).title(modelShipDetail.getResult().getPickup_location());
                        dropOffOption = new MarkerOptions().position(dropLatLng).title(modelShipDetail.getResult().getDrop_location());

                        drawRoute();

                        Log.e("responseString","responseString = " + responseString);

                    } else {
                        // Toast.makeText(mContext, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

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

    private void drawRoute() {
        gMap.addMarker(originOption);
        gMap.addMarker(dropOffOption);

        ArrayList<LatLng> listLatLon = new ArrayList<>();
        listLatLon.add(originOption.getPosition());
        listLatLon.add(dropOffOption.getPosition());

        zoomRoute(gMap,listLatLon);
        DrawPolyLine();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(getIntent() != null) {
                parcelId = getIntent().getStringExtra("parcelid");
                getAllBidsNotLoader();
            }
        }
    };

    private void DrawPolyLine() {
        DrawPollyLine.get(this).setOrigin(originOption.getPosition())
                .setDestination(dropOffOption.getPosition()).execute(new DrawPollyLine.onPolyLineResponse() {
            @Override
            public void Success(ArrayList<LatLng> latLngs) {
                lineOptions = new PolylineOptions();
                lineOptions.addAll(latLngs);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
                ArrayList<LatLng> latlonList = new ArrayList<>();
                latlonList.add(originOption.getPosition());
                latlonList.add(dropLatLng);
                zoomRoute(gMap,latlonList);
                gMap.addPolyline(lineOptions);
            }
        });
    }

    private void getAllBidsNotLoader() {
        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);

        HashMap<String,String> param = new HashMap<>();
        param.put("shipping_id",parcelId);

        Call<ResponseBody> call = api.getBidApiCall(param);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String stringRes = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringRes);
                    if(jsonObject.getString("status").equals("1")) {
                        modelShipBid = new Gson().fromJson(stringRes, ModelShipBid.class);
                        adapterShipBids = new AdapterShipBids(mContext,modelShipBid.getResult(),shippmentUserId,ParcelDetailAct.this::updateBid);
                        binding.rvBids.setHasFixedSize(true);
                        binding.rvBids.setAdapter(adapterShipBids);
                    } else {

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProjectUtil.pauseProgressDialog();
            }
        });
    }

    public void updateBid(String status) {
        getAllBidsNotLoader();
        if("Accept".equals(status)) {
            binding.tvStatus.setText(status);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter("shipdetail"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

}
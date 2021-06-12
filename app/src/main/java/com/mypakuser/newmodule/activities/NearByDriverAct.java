package com.mypakuser.newmodule.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.activities.HomeAct;
import com.mypakuser.databinding.ActivityNearByDriverBinding;
import com.mypakuser.models.ModelNearByDrivers;
import com.mypakuser.models.ModelOrderHistory;
import com.mypakuser.newmodule.adapters.AdapterNearByDriver;
import com.mypakuser.newmodule.adapters.AdapterOrderHistory;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class NearByDriverAct extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 1234;
    Context mContext = NearByDriverAct.this;
    ActivityNearByDriverBinding binding;
    GoogleMap mMap;
    String id="";
    LatLng pickUpLatLng;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 3000;
    private long FASTEST_INTERVAL = 2000;
    boolean isApiCalled;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_near_by_driver);
        init();
        id = getIntent().getStringExtra("id");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isApiCalled = false;
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission (
                mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NearByDriverAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // isCurrentLocation = true;
                    currentLocation = location;
                    Log.e("ivCurrentLocation", "location = " + location);

                    String address = ProjectUtil
                            .getCompleteAddressString(mContext, currentLocation.getLatitude()
                                    , currentLocation.getLongitude());

                    if (!isApiCalled) {
                        pickUpLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        getAllNearByDrivers(String.valueOf(pickUpLatLng.latitude),
                                String.valueOf(pickUpLatLng.longitude));
                    }

                } else {
                    startLocationUpdates();
                    Log.e("ivCurrentLocation", "location = " + location);
                }
            }
        });

    }

    private void init() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(NearByDriverAct.this);

        binding.swipLayout.setOnRefreshListener(() -> {
            isApiCalled = false;
            fetchLocation();
        });

        binding.tvList.setOnClickListener(v -> {
            binding.rvNearDrivers.setVisibility(View.VISIBLE);
            binding.tvList.setBackgroundResource(R.drawable.orange_back_10);
            binding.tvList.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            binding.tvMap.setTextColor(ContextCompat.getColor(mContext,R.color.black));
            binding.tvMap.setBackground(null);
        });

        binding.tvMap.setOnClickListener(v -> {
            binding.rvNearDrivers.setVisibility(View.GONE);
            binding.tvMap.setBackgroundResource(R.drawable.orange_back_10);
            binding.tvMap.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            binding.tvList.setTextColor(ContextCompat.getColor(mContext,R.color.black));
            binding.tvList.setBackground(null);
        });

    }

    private void setCurrentLocationMap(Location currentLocation) {
        if (mMap != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin))
                    .title(ProjectUtil.getCompleteAddressString(mContext, currentLocation.getLatitude(), currentLocation.getLongitude()));
            mMap.addMarker(markerOptions);
            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        Log.e("hdasfkjhksdf", "Location = ");

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Log.e("hdasfkjhksdf", "Location = " + locationResult.getLastLocation());
                            currentLocation = locationResult.getLastLocation();
                            String address = ProjectUtil.getCompleteAddressString(mContext, currentLocation.getLatitude(), currentLocation.getLongitude());

//                            if (TextUtils.isEmpty(binding.tvAddress.getText().toString().trim())) {
//                                binding.tvAddress.setText(address);
//                                setCurrentLocationMap(currentLocation);
//                            }
                        } else {
                            fetchLocation();
                        }
                    }
                },
                Looper.myLooper());
    }

    private void getAllNearByDrivers(String lat,String lon) {
        ProjectUtil.showProgressDialog(mContext,true,getString(R.string.please_wait));

        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        HashMap<String,String> params = new HashMap<>();
        params.put("lat",lat);
        params.put("lon",lon);

        Call<ResponseBody> call = api.getNearByDriversCall(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                binding.swipLayout.setRefreshing(false);
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {
                        ModelNearByDrivers modelNearByDrivers = new Gson().fromJson(responseString,ModelNearByDrivers.class);
                        AdapterNearByDriver adapterNearByDriver = new AdapterNearByDriver(mContext,modelNearByDrivers.getResult(),
                                Double.parseDouble(lat),Double.parseDouble(lon));
                        binding.rvNearDrivers.setAdapter(adapterNearByDriver);

                        isApiCalled = true;

                        if(mMap != null) {
                            mMap.clear();
                        }

                        for(int i=0;i<modelNearByDrivers.getResult().size();i++) {
                            Location location = new Location("");
                            location.setLatitude(Double.parseDouble(modelNearByDrivers.getResult().get(i).getLat()));
                            location.setLongitude(Double.parseDouble(modelNearByDrivers.getResult().get(i).getLon()));
                            setCurrentLocationMap(location);
                        }

                        binding.rvNearDrivers.setVisibility(View.VISIBLE);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
package com.mypakuser.newmodule.parcelsfragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mypakuser.R;
import com.mypakuser.databinding.FragmentParcelDeliveryBinding;
import com.mypakuser.utils.ProjectUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class ParcelDeliveryFragment extends Fragment implements OnMapReadyCallback {

    private static final int AUTOCOMPLETE_REQUEST_CODE_PICK_UP = 101;
    private static final int REQUEST_CODE = 1234;
    Context mContext;
    LatLng pickUpLatLng;
    GoogleMap mMap;
    FragmentParcelDeliveryBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    HashMap<String,String> params;
    private Location currentLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 3000;
    private long FASTEST_INTERVAL = 2000;

    public ParcelDeliveryFragment(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getString(R.string.places_api_key));
        }
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parcel_delivery, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        init();
        return binding.getRoot();
    }

    private void init() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ParcelDeliveryFragment.this);

        binding.ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.btNext.setOnClickListener(v -> {
            if(pickUpLatLng != null) {
                params.put("drop_location",binding.tvAddress.getText().toString().trim());
                params.put("drop_lat", String.valueOf(pickUpLatLng.latitude));
                params.put("drop_lon", String.valueOf(pickUpLatLng.longitude));
                ((ParcelUploadAct)getActivity()).loadFragment(new ParcelPhotoFragment(params));
            } else {
                Toast.makeText(mContext, getString(R.string.please_dev_loc), Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE_PICK_UP);
        });

    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission (
                mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (
                mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

                    String address = ProjectUtil.getCompleteAddressString(mContext
                            ,currentLocation.getLatitude()
                            ,currentLocation.getLongitude());

                    if (TextUtils.isEmpty(binding.tvAddress.getText().toString().trim())) {
                        binding.tvAddress.setText(address.trim());
                        pickUpLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        setCurrentLocationMap(currentLocation);
                    }

                } else {
                    startLocationUpdates();
                    Log.e("ivCurrentLocation", "location = " + location);
                }
            }
        });

    }

    private void setCurrentLocationMap(Location currentLocation) {
        if (mMap != null) {
            mMap.clear();
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
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Log.e("hdasfkjhksdf", "Location = " + locationResult.getLastLocation());
                            currentLocation = locationResult.getLastLocation();
                            String address = ProjectUtil.getCompleteAddressString(mContext, currentLocation.getLatitude(), currentLocation.getLongitude());

                            if (TextUtils.isEmpty(binding.tvAddress.getText().toString().trim())) {
                                binding.tvAddress.setText(address);
                                setCurrentLocationMap(currentLocation);
                            }
                        } else {
                            fetchLocation();
                        }
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_PICK_UP) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                pickUpLatLng = place.getLatLng();
                try {
                    String address = ProjectUtil.getCompleteAddressString(mContext,
                            place.getLatLng().latitude,
                            place.getLatLng().longitude);
                    binding.tvAddress.setText(address.trim());
                    Location location = new Location("");
                    location.setLatitude(pickUpLatLng.latitude);
                    location.setLongitude(pickUpLatLng.longitude);
                    setCurrentLocationMap(location);
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

}


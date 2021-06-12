package com.mypakuser.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.adapters.AdapterShipRequest;
import com.mypakuser.databinding.ActivityHomeBinding;
import com.mypakuser.databinding.AddShippDialogBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.models.ModelShipRequest;
import com.mypakuser.newmodule.activities.ParcelStatusAct;
import com.mypakuser.newmodule.parcelsfragments.ParcelUploadAct;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HomeAct extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 1234;
    private static final int AUTOCOMPLETE_REQUEST_CODE_PICK_UP = 101;
    Context mContext = HomeAct.this;
    ActivityHomeBinding binding;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    GoogleMap mMap;
    LatLng pickUpLatLng, dropOffLatLng;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    PolylineOptions polylineOptions;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 3000;
    private long FASTEST_INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        ProjectUtil.changeStatusBarColor(HomeAct.this);
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);

        init();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        fetchLocation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLocation();
        sharedPref = SharedPref.getInstance(mContext);
        binding.navItems.tvName.setText(modelLogin.getResult().getUser_name());
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission (
                mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

    private void init() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(HomeAct.this);

        binding.btInstant.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ParcelUploadAct.class));
             //.putExtra(AppConstant.DEV_TYPE,AppConstant.INSTANT)
        });

//        binding.btSchedule.setOnClickListener(v -> {
//            startActivity(new Intent(mContext, ParcelUploadAct.class)
//              .putExtra(AppConstant.DEV_TYPE,AppConstant.SCHEDULE)
//            );
//        });

//      binding.tvAddress.setOnClickListener(v -> {
//          List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
//          Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                  .build(this);
//          startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE_PICK_UP);
//        });

        binding.navItems.tvMySending.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ParcelStatusAct.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

//        binding.navItems.tvMessages.setOnClickListener(v -> {
//            startActivity(new Intent(mContext,ChatListAct.class));
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//
//        binding.navItems.tvMyProfile.setOnClickListener(v -> {
//            startActivity(new Intent(mContext,MyProfileAct.class));
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        });
//
        binding.navItems.tvLogout.setOnClickListener(v -> {
            logoutAppDialog();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        binding.ivMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

    }

//    private void openAddParcelDialog() {
//        dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
//        dialogBinding = DataBindingUtil
//                .inflate(LayoutInflater.from(mContext),R.layout.add_shipp_dialog,null,false);
//        dialog.setContentView(dialogBinding.getRoot());
//
//        dialogBinding.ivBack.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        dialogBinding.btUpload.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//      dialogBinding.etDropAdd.setOnClickListener(v -> {
//            // Set the fields to specify which types of place data to
//            // return after the user has made a selection.
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
//
//            // Start the autocomplete intent.
//            Intent intent = new Autocomplete.IntentBuilder(
//                    AutocompleteActivityMode.FULLSCREEN, fields)
//                    .build(mContext);
//
//            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
//        });
//
//        dialogBinding.etPickAdd.setOnClickListener(v -> {
//            // Set the fields to specify which types of place data to
//            // return after the user has made a selection.
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
//
//            // Start the autocomplete intent.
//            Intent intent = new Autocomplete.IntentBuilder(
//                    AutocompleteActivityMode.FULLSCREEN, fields)
//                    .build(mContext);
//
//            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_PICK_UP);
//        });
//
//        dialogBinding.etPickDate.setOnClickListener(v -> {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                    date.set(year, monthOfYear, dayOfMonth);
//                    String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());
//                    dialogBinding.etPickDate.setText(dateString);
//                    pickUpMilliseconds = ProjectUtil.convertDateIntoMillisecond(dateString);
//                }
//            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
//            datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
//            datePickerDialog.show();
//        });
//
//        dialogBinding.etDropDate.setOnClickListener(v -> {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                    date.set(year, monthOfYear, dayOfMonth);
//                    String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());
//                    dialogBinding.etDropDate.setText(dateString);
//                    pickUpMilliseconds = ProjectUtil.convertDateIntoMillisecond(dateString);
//                }
//            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
//            datePickerDialog.getDatePicker().setMinDate(pickUpMilliseconds);
//            datePickerDialog.show();
//        });
//
//        dialogBinding.btUpload.setOnClickListener(v -> {
//            if(TextUtils.isEmpty(dialogBinding.etPickAdd.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etDropAdd.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etPickDate.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etDropDate.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etRecipName.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etMobile.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else if(dialogBinding.spQuantity.getSelectedItemPosition() == 0) {
//                Toast.makeText(mContext,getString(R.string.please_select_item_quantity), Toast.LENGTH_SHORT).show();
//            } else if(dialogBinding.spItemCat.getSelectedItemPosition() == 0) {
//                Toast.makeText(mContext,getString(R.string.please_select_item_category), Toast.LENGTH_SHORT).show();
//            } else if(TextUtils.isEmpty(dialogBinding.etItemDetail.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            }  else if(TextUtils.isEmpty(dialogBinding.etDevInstruction.getText().toString().trim())) {
//                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
//            } else {
//                paramHash = new HashMap<>();
//                paramHash.put("user_id",modelLogin.getResult().getId());
//                paramHash.put("pickup_location", dialogBinding.etPickAdd.getText().toString().trim());
//                paramHash.put("pickup_lat",String.valueOf(pickUpLatLon.latitude));
//                paramHash.put("pickup_lon",String.valueOf(pickUpLatLon.longitude));
//                paramHash.put("drop_location",dialogBinding.etDropAdd.getText().toString().trim());
//                paramHash.put("drop_lat",String.valueOf(dropOffLatLon.latitude));
//                paramHash.put("drop_lon",String.valueOf(pickUpLatLon.longitude));
//                paramHash.put("pickup_date",dialogBinding.etPickDate.getText().toString().trim());
//                paramHash.put("dropoff_date",dialogBinding.etDropDate.getText().toString().trim());
//                paramHash.put("recipient_name",dialogBinding.etRecipName.getText().toString().trim());
//                paramHash.put("mobile_no",dialogBinding.etMobile.getText().toString().trim());
//                paramHash.put("parcel_quantity",dialogBinding.spQuantity.getSelectedItem().toString());
//                paramHash.put("parcel_category",dialogBinding.spItemCat.getSelectedItem().toString());
//                paramHash.put("item_detail",dialogBinding.etItemDetail.getText().toString().trim());
//                paramHash.put("dev_instruction",dialogBinding.etDevInstruction.getText().toString().trim());
//                paramHash.put("direction_json","");
//
//                addShippingRequest(paramHash);
//
//            }
//
//        });
//
//        dialog.show();
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_PICK_UP) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                pickUpLatLng = place.getLatLng();
                try {
                    String address = ProjectUtil.getCompleteAddressString(mContext,
                            place.getLatLng().latitude,
                            place.getLatLng().longitude);
                    binding.tvAddress.setText(address);
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void logoutAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(getString(R.string.logout_text))
                .setCancelable(false)
                .setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // sharedPref.clearAllPreferences();
                        Intent loginscreen = new Intent(mContext,GetStartedAct.class);
                        loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        NotificationManager nManager = ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE));
                        nManager.cancelAll();
                        startActivity(loginscreen);
                        finishAffinity();
                    }
                }).setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
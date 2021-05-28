package com.mypakuser.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
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

public class HomeAct extends AppCompatActivity {

    Context mContext = HomeAct.this;
    ActivityHomeBinding binding;
    AddShippDialogBinding dialogBinding;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    LatLng pickUpLatLon,dropOffLatLon;
    int AUTOCOMPLETE_REQUEST_CODE_PICK_UP = 1,
            AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 2;
    Calendar date = Calendar.getInstance();
    Calendar currentDate = Calendar.getInstance();
    ModelShipRequest modelShipRequest = null;
    long pickUpMilliseconds;
    Dialog dialog;
    HashMap<String,String> paramHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        ProjectUtil.changeStatusBarColor(HomeAct.this);
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
        if (!Places.isInitialized()) {
            Places.initialize(mContext, getString(R.string.places_api_key));
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = SharedPref.getInstance(mContext);
        binding.navItems.tvName.setText(modelLogin.getResult().getUser_name());
    }


    private void init() {

        binding.swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllShipRequest();
            }
        });

        getAllShipRequest();

//        binding.btParcel.setOnClickListener(v -> {
//            startActivity(new Intent(mContext,ParcelDetailAct.class));
//        });

        binding.navItems.tvMySending.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ParcelStatusAct.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        binding.navItems.tvMessages.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ChatListAct.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        binding.navItems.tvMyProfile.setOnClickListener(v -> {
            startActivity(new Intent(mContext,MyProfileAct.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        binding.navItems.tvLogout.setOnClickListener(v -> {
            logoutAppDialog();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        binding.ivMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.ivAddAdress.setOnClickListener(v -> {
           openAddParcelDialog();
        });

    }

    private void getAllShipRequest() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",modelLogin.getResult().getId());

        Call<ResponseBody> call = api.getAllShippingRequestApiCall(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                binding.swipLayout.setRefreshing(false);
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {

                        modelShipRequest = new Gson().fromJson(responseString,ModelShipRequest.class);

                        AdapterShipRequest adapterShipRequest = new AdapterShipRequest(mContext,modelShipRequest.getResult());
                        binding.rvRequest.setAdapter(adapterShipRequest);

                        Log.e("responseStringdfsdfsd","responseString = " + responseString);

                    } else {
                        Toast.makeText(mContext, getString(R.string.no_request_found), Toast.LENGTH_SHORT).show();
                        AdapterShipRequest adapterShipRequest = new AdapterShipRequest(mContext,null);
                        binding.rvRequest.setAdapter(adapterShipRequest);
                        // Toast.makeText(mContext, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    // Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void openAddParcelDialog() {
        dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext),R.layout.add_shipp_dialog,null,false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogBinding.btUpload.setOnClickListener(v -> {
            dialog.dismiss();
        });

      dialogBinding.etDropAdd.setOnClickListener(v -> {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(mContext);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
        });

        dialogBinding.etPickAdd.setOnClickListener(v -> {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(mContext);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_PICK_UP);
        });

        dialogBinding.etPickDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    date.set(year, monthOfYear, dayOfMonth);
                    String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());
                    dialogBinding.etPickDate.setText(dateString);
                    pickUpMilliseconds = ProjectUtil.convertDateIntoMillisecond(dateString);
                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
            datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            datePickerDialog.show();
        });

        dialogBinding.etDropDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    date.set(year, monthOfYear, dayOfMonth);
                    String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());
                    dialogBinding.etDropDate.setText(dateString);
                    pickUpMilliseconds = ProjectUtil.convertDateIntoMillisecond(dateString);
                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
            datePickerDialog.getDatePicker().setMinDate(pickUpMilliseconds);
            datePickerDialog.show();
        });

        dialogBinding.btUpload.setOnClickListener(v -> {
            if(TextUtils.isEmpty(dialogBinding.etPickAdd.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etDropAdd.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etPickDate.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etDropDate.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etRecipName.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etMobile.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else if(dialogBinding.spQuantity.getSelectedItemPosition() == 0) {
                Toast.makeText(mContext,getString(R.string.please_select_item_quantity), Toast.LENGTH_SHORT).show();
            } else if(dialogBinding.spItemCat.getSelectedItemPosition() == 0) {
                Toast.makeText(mContext,getString(R.string.please_select_item_category), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(dialogBinding.etItemDetail.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            }  else if(TextUtils.isEmpty(dialogBinding.etDevInstruction.getText().toString().trim())) {
                Toast.makeText(mContext,getString(R.string.all_fields_are_mandatory), Toast.LENGTH_SHORT).show();
            } else {
                paramHash = new HashMap<>();
                paramHash.put("user_id",modelLogin.getResult().getId());
                paramHash.put("pickup_location", dialogBinding.etPickAdd.getText().toString().trim());
                paramHash.put("pickup_lat",String.valueOf(pickUpLatLon.latitude));
                paramHash.put("pickup_lon",String.valueOf(pickUpLatLon.longitude));
                paramHash.put("drop_location",dialogBinding.etDropAdd.getText().toString().trim());
                paramHash.put("drop_lat",String.valueOf(dropOffLatLon.latitude));
                paramHash.put("drop_lon",String.valueOf(pickUpLatLon.longitude));
                paramHash.put("pickup_date",dialogBinding.etPickDate.getText().toString().trim());
                paramHash.put("dropoff_date",dialogBinding.etDropDate.getText().toString().trim());
                paramHash.put("recipient_name",dialogBinding.etRecipName.getText().toString().trim());
                paramHash.put("mobile_no",dialogBinding.etMobile.getText().toString().trim());
                paramHash.put("parcel_quantity",dialogBinding.spQuantity.getSelectedItem().toString());
                paramHash.put("parcel_category",dialogBinding.spItemCat.getSelectedItem().toString());
                paramHash.put("item_detail",dialogBinding.etItemDetail.getText().toString().trim());
                paramHash.put("dev_instruction",dialogBinding.etDevInstruction.getText().toString().trim());
                paramHash.put("direction_json","");

                addShippingRequest(paramHash);

            }

        });

        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_PICK_UP) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                pickUpLatLon = place.getLatLng();
                try {
                    String address = ProjectUtil.getCompleteAddressString(mContext,
                            place.getLatLng().latitude,
                            place.getLatLng().longitude);
                    dialogBinding.etPickAdd.setText(address);
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
        } else {
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                dropOffLatLon = place.getLatLng();

                try {
                    String address = ProjectUtil.getCompleteAddressString(mContext,
                            place.getLatLng().latitude,
                            place.getLatLng().longitude);
                    dialogBinding.etDropAdd.setText(address);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }

    }


    private void addShippingRequest(HashMap<String, String> paramHash) {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        Call<ResponseBody> call = api.addShippingRequestApiCall(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {

                        dialog.dismiss();
                        pickUpMilliseconds = 0;

                        if(modelShipRequest != null) {
                            ModelShipRequest.Result data = new ModelShipRequest().new Result();
                            data.setId(jsonObject.getJSONObject("result").getString("id"));
                            data.setStatus(jsonObject.getJSONObject("result").getString("status"));
                            data.setPickup_location(jsonObject.getJSONObject("result").getString("pickup_location"));
                            data.setDrop_location(jsonObject.getJSONObject("result").getString("drop_location"));

                            modelShipRequest.getResult().add(data);

                            AdapterShipRequest adapterShipRequest = new AdapterShipRequest(mContext,modelShipRequest.getResult());
                            binding.rvRequest.setAdapter(adapterShipRequest);

                        } else {
                            getAllShipRequest();
                        }

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

}
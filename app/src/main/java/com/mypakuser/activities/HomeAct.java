package com.mypakuser.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityHomeBinding;
import com.mypakuser.databinding.AddShippDialogBinding;
import com.mypakuser.utils.ProjectUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeAct extends AppCompatActivity {

    Context mContext = HomeAct.this;
    ActivityHomeBinding binding;
    AddShippDialogBinding dialogBinding;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        ProjectUtil.changeStatusBarColor(HomeAct.this);
        init();

    }

    private void init() {

        binding.btParcel.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ParcelDetailAct.class));
        });

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

        //        dialogBinding.etDropAdd.setOnClickListener(v -> {
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
//                // getRoute();
//
//            }
//
//        });

        dialog.show();

    }

}
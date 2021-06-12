package com.mypakuser.newmodule.parcelsfragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mypakuser.R;
import com.mypakuser.databinding.FragmentParcelPhotoBinding;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.Compress;
import com.mypakuser.utils.ProjectUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ParcelPhotoFragment extends Fragment {

    private static final int PERMISSION_ID = 101;
    Context mContext;
    FragmentParcelPhotoBinding binding;
    HashMap<String,String> params;
    Calendar currentDate = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    HashMap<String,File> fileParam = new HashMap<>();
    File mfile = null;
    private int GALLERY = 1, CAMERA = 2;
    String dateString,timeString;

    public ParcelPhotoFragment(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parcel_photo,container,false);
        init();
        return binding.getRoot();
    }

    private void init() {

        binding.etDateTime.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    date.set(year, monthOfYear, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    date.set(Calendar.MINUTE, minute);
                                    String dateTimeString = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa").format(date.getTime());
                                    String date = dateTimeString;
                                    String[] parts = date.split(" ");
                                    dateString = parts[0];
                                    timeString = parts[1] + " " + parts[2].toUpperCase();
                                    binding.etDateTime.setText(dateString + " " + parts[1] + " " + parts[2].toUpperCase().replace(".",""));
                                }
                            },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                    timePickerDialog.show();
                }
            },currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DATE));
            datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            datePickerDialog.show();
        });

        binding.btNext.setOnClickListener(v -> {
            if(mfile == null) {
                Toast.makeText(mContext, getString(R.string.please_upload_parcel_photo), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(binding.etTitle.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_title), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(binding.etDsecp.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_desp), Toast.LENGTH_SHORT).show();
            } else {
                if(TextUtils.isEmpty(binding.etDateTime.getText().toString().trim())) {
                    String date = ProjectUtil.getCurrentDate();
                    String time = ProjectUtil.getCurrentTime();
                    params.put("dev_type",AppConstant.INSTANT);
                    params.put("parcel_date",date);
                    params.put("parcel_time",time);
                    params.put("title",binding.etTitle.getText().toString().trim());
                    params.put("description",binding.etDsecp.getText().toString().trim());
                } else {
                    params.put("dev_type",AppConstant.SCHEDULE);
                    params.put("parcel_date",dateString);
                    params.put("parcel_time",timeString);
                    params.put("title",binding.etTitle.getText().toString().trim());
                    params.put("description",binding.etDsecp.getText().toString().trim());
                }

                fileParam.put("parcel_image",mfile);
                ((ParcelUploadAct)getActivity()).loadFragment(new ParcelVehicleFragment(params,fileParam));

            }

        });

        binding.ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.ivUploadParcelImg.setOnClickListener(v -> {
            if(checkPermissions()) {
                showPictureDialog();
            } else {
                requestPermissions();
            }
        });

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions (
                getActivity(),
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPictureDialog();
            }
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mContext);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }

                });

        pictureDialog.show();

    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.need_permission));
        builder.setMessage(getString(R.string.this_app_needs));
        builder.setPositiveButton(getString(R.string.goto_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    String path = getRealPathFromURI(contentURI);
                    Compress.get(mContext).setQuality(20).execute(new Compress.onSuccessListener() {
                        @Override
                        public void response(boolean status, String message, File file) {
                            mfile = file;//ProjectUtil.saveBitmapToFile(new File(path));
                            binding.ivUploadParcelImg.setImageURI(Uri.parse(mfile.getPath()));
                        }
                    }).CompressedImage(path);

                } catch (Exception e) {
                    Log.e("hjagksads","image = " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            String path = getRealPathFromURI(getImageUri(mContext, thumbnail));

            Compress.get(mContext).setQuality(20)
                    .execute(new Compress.onSuccessListener() {
                        @Override
                        public void response(boolean status, String message, File file) {
                            mfile = file;
                            binding.ivUploadParcelImg.setImageBitmap(thumbnail);
                            // binding.profileImage.setImageURI(Uri.parse(file.getPath()));
                        }
                    }).CompressedImage(path);

        }

    }

}
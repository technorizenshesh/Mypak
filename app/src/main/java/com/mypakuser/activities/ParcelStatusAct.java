package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityParcelStatusBinding;
import com.mypakuser.utils.ProjectUtil;

public class ParcelStatusAct extends AppCompatActivity {

    Context mContext = ParcelStatusAct.this;
    ActivityParcelStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_parcel_status);
        ProjectUtil.changeStatusBarColor(ParcelStatusAct.this);
        init();

    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

        binding.btParcel.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ParcelDetailAct.class));
        });

    }

}
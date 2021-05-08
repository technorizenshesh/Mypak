package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityParcelDetailBinding;
import com.mypakuser.utils.ProjectUtil;

public class ParcelDetailAct extends AppCompatActivity {

    Context mContext = ParcelDetailAct.this;
    ActivityParcelDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_parcel_detail);
        ProjectUtil.changeStatusBarColor(ParcelDetailAct.this);
        init();

    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

    }

}
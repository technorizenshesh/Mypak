package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.os.Bundle;
import com.mypakuser.R;
import com.mypakuser.databinding.ActivityCarSelectBinding;

public class CarSelectAct extends AppCompatActivity {

    Context mContext = CarSelectAct.this;
    ActivityCarSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_car_select);

        init();

    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

    }


}
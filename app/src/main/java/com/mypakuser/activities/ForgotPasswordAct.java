package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordAct extends AppCompatActivity {

    Context mContext = ForgotPasswordAct.this;
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);

        init();

    }

    private void init() {

        binding.btSubmit.setOnClickListener(v -> {
            finish();
        });

    }


}
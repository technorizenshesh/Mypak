package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivitySignUpBinding;

public class SignUpAct extends AppCompatActivity {

    Context mContext = SignUpAct.this;
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);

        init();

    }

    private void init() {

        binding.btSignUp.setOnClickListener(v -> {
            startActivity(new Intent(mContext,VerifyAct.class));
        });

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(mContext,LoginAct.class));
            finish();
        });

    }

}
package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityLoginBinding;
import com.mypakuser.utils.ProjectUtil;

public class LoginAct extends AppCompatActivity {

    Context mContext = LoginAct.this;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        ProjectUtil.changeStatusBarColor(LoginAct.this);
        init();

    }

    private void init() {
        binding.btLogin.setOnClickListener(v -> {
            startActivity(new Intent(mContext,HomeAct.class));
        });

        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(mContext,SignUpAct.class));
        });

        binding.tvForogtPassword.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ForgotPasswordAct.class));
        });
    }

}
package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityGetStartedBinding;
import com.mypakuser.utils.ProjectUtil;

public class GetStartedAct extends AppCompatActivity {

    Context mContext = GetStartedAct.this;
    ActivityGetStartedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_get_started);
        ProjectUtil.changeStatusBarColor(GetStartedAct.this);
        init();

    }

    private void init() {

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(mContext,LoginAct.class));
        });

        binding.cvGmail.setOnClickListener(v -> {
            startActivity(new Intent(mContext,LoginAct.class));
        });
    }

}

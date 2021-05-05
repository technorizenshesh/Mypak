package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityWelcomeBinding;

public class WelcomeAct extends AppCompatActivity {

    Context mContext = WelcomeAct.this;
    ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome);

        init();

    }

    private void init() {

        binding.btStart.setOnClickListener(v -> {
            startActivity(new Intent(mContext,GetStartedAct.class));
        });

    }

}
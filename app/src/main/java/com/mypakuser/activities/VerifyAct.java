package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityVerifyBinding;
import com.mypakuser.utils.ProjectUtil;

public class VerifyAct extends AppCompatActivity {

    Context mContext = VerifyAct.this;
    ActivityVerifyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verify);
        ProjectUtil.changeStatusBarColor(VerifyAct.this);
        init();

    }

    private void init() {

        binding.btVerify.setOnClickListener(v -> {
            startActivity(new Intent(mContext,HomeAct.class));
        });

    }

}
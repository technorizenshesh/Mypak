package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityChangePasBinding;
import com.mypakuser.utils.ProjectUtil;

public class ChangePasAct extends AppCompatActivity {

    Context mContext = ChangePasAct.this;
    ActivityChangePasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_pas);
        ProjectUtil.changeStatusBarColor(ChangePasAct.this);
        init();

    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

        binding.btSubmit.setOnClickListener(v -> {
            finish();
        });

    }

}
package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mypakuser.R;
import com.mypakuser.utils.ProjectUtil;

public class SplashAct extends AppCompatActivity {

    Context mContext = SplashAct.this;
    final static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ProjectUtil.changeStatusBarColor(SplashAct.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext,WelcomeAct.class));
            }
        },SPLASH_TIME_OUT);
    }

}
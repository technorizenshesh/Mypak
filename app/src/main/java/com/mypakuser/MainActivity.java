package com.mypakuser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    String merchantId = "MER_7e3e2c7df34f42819b3edee31022ee3f";
    String secret = "Po8lRRT67a";
    String appKey = "0ChuRg11PUF1laa3";
    String appId = "hUjRc2yh6OqZId0aaKCmzWGhvADwoSvs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void showResult(String paymentResult) {
        Log.d("ffhjfadhjfha",paymentResult);
        new AlertDialog.Builder(this)
                .setTitle("Payment result")
                .setMessage(paymentResult)
                .setPositiveButton("Close",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}
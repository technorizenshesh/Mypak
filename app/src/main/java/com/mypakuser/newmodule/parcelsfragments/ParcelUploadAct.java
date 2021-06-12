package com.mypakuser.newmodule.parcelsfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mypakuser.R;
import com.mypakuser.databinding.ActivityParcelUploadBinding;

import java.util.HashMap;

public class ParcelUploadAct extends AppCompatActivity {

    Context mContext = ParcelUploadAct.this;
    ActivityParcelUploadBinding binding;
    HashMap<String,String> hashMap = new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_parcel_upload);
        
        init();
        
    }

    private void init() {
        loadFragment(new ParcelPickupFragment());
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("Fragment");
        transaction.replace(R.id.frameLayout,fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.e("gfdsfdsfdasfsf","" + getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

}
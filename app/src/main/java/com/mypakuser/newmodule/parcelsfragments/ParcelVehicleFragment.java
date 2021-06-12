package com.mypakuser.newmodule.parcelsfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mypakuser.R;
import com.mypakuser.databinding.FragmentParcelVehicleBinding;

import java.io.File;
import java.util.HashMap;

public class ParcelVehicleFragment extends Fragment {

    Context mContext;
    HashMap<String,String> params;
    HashMap<String,File> fileParam;
    FragmentParcelVehicleBinding binding;

    public ParcelVehicleFragment(HashMap<String, String> params, HashMap<String, File> fileParam) {
        this.params = params;
        this.fileParam = fileParam;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_parcel_vehicle, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.btNext.setOnClickListener(v -> {
            params.put("vehicle_id",binding.spVehicleType.getSelectedItem().toString());
            params.put("parcel_quantity",binding.spPackSize.getSelectedItem().toString());

            ((ParcelUploadAct)getActivity()).loadFragment(new ParcelPayFragment(params,fileParam));
        });

    }

}
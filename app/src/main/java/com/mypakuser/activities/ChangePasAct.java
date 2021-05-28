package com.mypakuser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.databinding.ActivityChangePasBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.App;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasAct extends AppCompatActivity {

    Context mContext = ChangePasAct.this;
    ActivityChangePasBinding binding;
    SharedPref sharedPref;
    ModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_pas);
        ProjectUtil.changeStatusBarColor(ChangePasAct.this);
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS);
        init();
    }

    private void init() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

        binding.btSubmit.setOnClickListener(v -> {
            if(TextUtils.isEmpty(binding.etNewPass.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_new_pass), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(binding.etOldPass.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_old_pass), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(binding.etConfirmPass.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_conf_pass), Toast.LENGTH_SHORT).show();
            } else if(!(binding.etNewPass.getText().toString().trim().length() > 4 )) {
                Toast.makeText(mContext, getString(R.string.password_validation_text), Toast.LENGTH_SHORT).show();
            } else if(!(binding.etNewPass.getText().toString().trim().equals(binding.etConfirmPass.getText().toString().trim()))){
                Toast.makeText(mContext, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
            } else {
                changePassword();
            }
        });

    }

    private void changePassword() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));

        HashMap<String,String> paramHash = new HashMap<>();
        paramHash.put("user_id",modelLogin.getResult().getId());
        paramHash.put("old_password",binding.etOldPass.getText().toString().trim());
        paramHash.put("new_password",binding.etNewPass.getText().toString().trim());

        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        Call<ResponseBody> call = api.changePassCall(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {
                        Toast.makeText(ChangePasAct.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Exception","Exception = " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProjectUtil.pauseProgressDialog();
            }

        });

    }

}
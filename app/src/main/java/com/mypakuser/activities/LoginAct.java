package com.mypakuser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mypakuser.R;
import com.mypakuser.databinding.ActivityLoginBinding;
import com.mypakuser.models.ModelLogin;
import com.mypakuser.utils.Api;
import com.mypakuser.utils.ApiFactory;
import com.mypakuser.utils.App;
import com.mypakuser.utils.AppConstant;
import com.mypakuser.utils.InternetConnection;
import com.mypakuser.utils.ProjectUtil;
import com.mypakuser.utils.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAct extends AppCompatActivity {

    Context mContext = LoginAct.this;
    ActivityLoginBinding binding;
    String registerId;
    SharedPref sharedPref;
    ModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        ProjectUtil.changeStatusBarColor(LoginAct.this);
        sharedPref = SharedPref.getInstance(mContext);

        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult().getToken();
                        registerId = token;
                        Log.e("registerIdregisterId","registerId = " + registerId);

                    }

                });

        init();

    }

    private void init() {

        binding.btLogin.setOnClickListener(v -> {
            if(TextUtils.isEmpty(binding.etEmail.getText().toString().trim())){
                Toast.makeText(mContext, getString(R.string.please_enter_email_add), Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(binding.etPass.getText().toString().trim())){
                Toast.makeText(mContext, getString(R.string.please_enter_pass), Toast.LENGTH_SHORT).show();
            } else {
                if(InternetConnection.checkConnection(mContext)){
                    loginApiCall();
                } else {
                    App.showConnectionDialog(mContext);
                }
            }
        });

        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(mContext,SignUpAct.class));
        });

        binding.tvForogtPassword.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ForgotPasswordAct.class));
        });
    }

    private void loginApiCall() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));

        HashMap<String,String> paramHash = new HashMap<>();
        paramHash.put("email",binding.etEmail.getText().toString().trim());
        paramHash.put("password",binding.etPass.getText().toString().trim());
        paramHash.put("lat","");
        paramHash.put("lon","");
        paramHash.put("type",AppConstant.USER);
        paramHash.put("register_id",registerId);

        Api api = ApiFactory.getClientWithoutHeader(mContext).create(Api.class);
        Call<ResponseBody> call = api.loginApiCall(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);

                    if(jsonObject.getString("status").equals("1")) {

                        modelLogin = new Gson().fromJson(responseString, ModelLogin.class);

                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER,true);
                        sharedPref.setUserDetails(AppConstant.USER_DETAILS,modelLogin);

                        if(modelLogin.getResult().getType().equals(AppConstant.USER)) {
                            startActivity(new Intent(mContext,HomeAct.class));
                            finish();
                        } else {
                            Toast.makeText(mContext, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
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
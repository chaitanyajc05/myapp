package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.myapp.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setUpUi();
        listener();
    }

    private void setUpUi() {
    }

    private void listener() {
        mBinder.btnLogin.setOnClickListener(this);
        mBinder.tvSignUpHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                checkValidation();
                break;
            case R.id.tvSignUpHere:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
        }
    }

    private void checkValidation() {
        if (TextUtils.isEmpty(mBinder.etMobileNumber.getText().toString()))
            showToastShort(getString(R.string.strPleaseEnterYourMobileNumber));
        else if (TextUtils.isEmpty(mBinder.etPassword.getText().toString()))
            showToastShort(getString(R.string.strPleaseEnterYourPassword));
        else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
//            apiSignIn();
    }

    private void apiSignIn() {
        AndroidNetworking.post("https://www.recyclebb.com/login")
                .addBodyParameter("password", mBinder.etPassword.getText().toString())
                .addBodyParameter("mobileNo", mBinder.etMobileNumber.getText().toString())
                .addBodyParameter("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .addBodyParameter("deviceType", "A")
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mBinder.linLoader.setVisibility(View.GONE);
                        mBinder.aviLoader.hide();
                        // do anything with response
                        if (response.optString("Success").equals("1")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void showToastShort(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}

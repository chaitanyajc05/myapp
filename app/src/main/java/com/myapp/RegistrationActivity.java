package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.myapp.databinding.ActivityRegisterBinding;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegisterBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setUpUi();
        listener();
    }

    private void setUpUi() {

    }

    private void listener() {
        mBinder.ivBack.setOnClickListener(this);
        mBinder.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                checkValidation();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void checkValidation() {
        if (TextUtils.isEmpty(mBinder.etFirstName.getText().toString()))
            showToastShort(getResources().getString(R.string.strEnterFirstName));
        else if (TextUtils.isEmpty(mBinder.etLastName.getText().toString()))
            showToastShort(getResources().getString(R.string.strEnterLastName));
        else if (TextUtils.isEmpty(mBinder.etMobileNumber.getText().toString()))
            showToastShort(getResources().getString(R.string.strEnterMobileNumber));
        else if (TextUtils.isEmpty(mBinder.etPassword.getText().toString()))
            showToastShort(getResources().getString(R.string.strEnterPassword));
        else if (TextUtils.isEmpty(mBinder.etEmailAddress.getText().toString()))
            showToastShort(getResources().getString(R.string.strEnterEmailAddress));
        else if (!isValidEmail(mBinder.etEmailAddress.getText().toString()))
            showToastShort(getString(R.string.strValidEmail));
        else {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
//            apiSignUp();
    }

    private void apiSignUp() {
        AndroidNetworking.post("https://www.recyclebb.com/register")
                .addBodyParameter("email", mBinder.etEmailAddress.getText().toString())
                .addBodyParameter("password", mBinder.etPassword.getText().toString())
                .addBodyParameter("firstName", mBinder.etFirstName.getText().toString())
                .addBodyParameter("lastName", mBinder.etLastName.getText().toString())
                .addBodyParameter("mobileNo", mBinder.etMobileNumber.getText().toString())
                .addBodyParameter("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .addBodyParameter("deviceType","A")
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
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

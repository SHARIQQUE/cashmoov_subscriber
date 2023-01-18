package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.register.RegisterStepTwo;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePassword extends AppCompatActivity {
    public static CreatePassword setpinC;
    EditText etPin,etRePin;
    TextView tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        setpinC = this;
        getIds();
    }

    private void getIds() {
        etPin = findViewById(R.id.etPin);
        etRePin = findViewById(R.id.etRePin);
        tvContinue = findViewById(R.id.tvContinue);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(setpinC,"please enter password");
                    return;
                }
                if (etPin.getText().toString().trim().length()<8) {
                    MyApplication.showErrorToast(setpinC, "password must be 8 character long with special character");
                    return;
                }
                if(etRePin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(setpinC,"please enter confirm password");
                    return;
                }

                if(!(etPin.getText().toString().trim()).equalsIgnoreCase(etRePin.getText().toString().trim())) {
                    MyApplication.showErrorToast(setpinC,"Password and Confirm password must be same");
                    return;
                }

                callPUT();

            }
        });
    }

    public void callPUT(){
        //{"userName":"99108591851"}

        JSONObject logiJson=new JSONObject();
        try {


            logiJson.put("password",AESEncryption.getAESEncryption(etRePin.getText().toString().trim()));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.showloader(CreatePassword.this,getString(R.string.please_wait));
        API.PUT_SET_PASS("ewallet/api/v1/walletOwnerUser/setPassword",logiJson,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            Intent intent = new Intent(setpinC, RegisterStepTwo.class);
                            startActivity(intent);
                            MyApplication.showToast(CreatePassword.this,jsonObject.optString("resultDescription"));
                        }else{
                            MyApplication.showToast(CreatePassword.this,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();

                    }
                });
    }



}

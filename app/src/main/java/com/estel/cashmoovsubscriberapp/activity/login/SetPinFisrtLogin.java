package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.HiddenPassTransformationMethod;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.register.RegisterStepTwo;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class SetPinFisrtLogin extends AppCompatActivity {
    public static SetPinFisrtLogin setpinC;
    EditText etPin,etRePin;
    TextView tvContinue;
    ImageView icPin,icRepin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);
        setpinC = this;
        getIds();
    }
    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    private void getIds() {
        etPin = findViewById(R.id.etPin);
        etPin.setTransformationMethod(hiddenPassTransformationMethod);
        etRePin = findViewById(R.id.etRePin);
        etRePin.setTransformationMethod(hiddenPassTransformationMethod);
        icPin = findViewById(R.id.icPin);
        icRepin = findViewById(R.id.icRePin);
        tvContinue = findViewById(R.id.tvContinue);

        etRePin.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 4)
                    MyApplication.hideKeyboard(setpinC);            }
        });


        icPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){
                    icPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    icPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                etPin.setSelection(etPin.getText().length());
            }
        });

        icRepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etRePin.getTransformationMethod().equals(hiddenPassTransformationMethod)){
                    icRepin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etRePin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    icRepin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etRePin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                etRePin.setSelection(etRePin.getText().length());
            }
        });


        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(setpinC,getString(R.string.val_pin));
                    return;
                }
                if (etPin.getText().toString().trim().length()<4) {
                    MyApplication.showErrorToast(setpinC, getString(R.string.val_valid_pin));
                    return;
                }
                if(etRePin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(setpinC,getString(R.string.val_re_pin));
                    return;
                }
               /* if(etRePin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(setpinC,getString(R.string.val_valid_re_pin));
                    return;
                }*/
                if(!(etPin.getText().toString().trim()).equalsIgnoreCase(etRePin.getText().toString().trim())) {
                    MyApplication.showErrorToast(setpinC,getString(R.string.val_pin_re_pin));
                    return;
                }

                callApiSetPin();
            }
        });
    }


    private void callApiSetPin() {
        try{

            String encryptionDatanew = AESEncryption.getAESEncryption(etRePin.getText().toString().trim());

            JSONObject setPinJson=new JSONObject();
            setPinJson.put("pin",encryptionDatanew);
            String requestNo=AESEncryption.getAESEncryption(setPinJson.toString());
            JSONObject jsonObject=null;
            try{
                jsonObject=new JSONObject();
                jsonObject.put("request",requestNo);
            }catch (Exception e){

            }
            MyApplication.showloader(setpinC,setpinC.getString(R.string.please_wait));
            API.PUT("ewallet/api/v1/walletOwnerUser/setPin", jsonObject, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            MyApplication.showToast(setpinC,getString(R.string.pin_generate_success));
                                Intent intent = new Intent(setpinC, RegisterStepTwo.class);
                                startActivity(intent);
                                finish();


                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(setpinC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(setpinC,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();

                }
            });

        }catch (Exception e){

        }


    }



}

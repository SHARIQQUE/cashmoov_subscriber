package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.HiddenPassTransformationMethod;
import com.estel.cashmoovsubscriberapp.activity.login.AESEncryption;
import com.estel.cashmoovsubscriberapp.activity.login.PhoneNumberRegistrationScreen;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class ChangePin extends AppCompatActivity implements View.OnClickListener {
    public static ChangePin changepinC;
    Button btnCancel, btnConfirm;
    EditText etOldPin,etNewPin,etReNewPin;
    ImageView icOldPin,icNewPin,icReNewPin;
    // ImageView imgBack;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        changepinC=this;
        //setBackMenu();
        getIds();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }

    private void getIds() {
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        etOldPin = findViewById(R.id.etOldPin);
        etNewPin = findViewById(R.id.etNewPin);
        etReNewPin = findViewById(R.id.etReNewPin);

        etOldPin.setTransformationMethod(hiddenPassTransformationMethod);
        etNewPin.setTransformationMethod(hiddenPassTransformationMethod);
        etReNewPin.setTransformationMethod(hiddenPassTransformationMethod);
        icOldPin = findViewById(R.id.icOldPin);
        icNewPin = findViewById(R.id.icNewPin);
        icReNewPin = findViewById(R.id.icReNewPin);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        icOldPin.setOnClickListener(changepinC);
        icNewPin.setOnClickListener(changepinC);
        icReNewPin.setOnClickListener(changepinC);
        btnCancel.setOnClickListener(changepinC);
        btnConfirm.setOnClickListener(changepinC);
    }
    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.icOldPin:
                if(etOldPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){

                    icOldPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etOldPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{

                    icOldPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etOldPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                etOldPin.setSelection(etOldPin.getText().length());
                break;
            case R.id.icNewPin:
                if(etNewPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){

                    icNewPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etNewPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{

                    icNewPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etNewPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                etNewPin.setSelection(etNewPin.getText().length());
                break;
            case R.id.icReNewPin:
                if(etReNewPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){

                    icReNewPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etReNewPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
                else{

                    icReNewPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etReNewPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                etReNewPin.setSelection(etReNewPin.getText().length());
                break;
            case R.id.btnCancel:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(etOldPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_old_pin));
                    return;
                }
                if (etOldPin.getText().toString().trim().length()<3) {
                    MyApplication.showErrorToast(changepinC, getString(R.string.val_valid_old_pin));
                    return;
                }
                if(etNewPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_new_pin));
                    return;
                }
                if(etNewPin.getText().toString().trim().length()<3) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_valid_new_pin));
                    return;
                }
                if(etReNewPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_re_pin));
                    return;
                }
                if(etReNewPin.getText().toString().trim().length()<3) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_valid_re_pin));
                    return;
                }
                if(!(etNewPin.getText().toString().trim()).equalsIgnoreCase(etReNewPin.getText().toString().trim())) {
                    MyApplication.showErrorToast(changepinC,getString(R.string.val_new_pin_re_pin));
                    return;
                }



                callApiChangePin();

                break;

        }
    }

    private void callApiChangePin() {
        try{

            String encryptionDataOld = AESEncryption.getAESEncryption(etOldPin.getText().toString().trim());
            String encryptionDataNew = AESEncryption.getAESEncryption(etReNewPin.getText().toString().trim());

            JSONObject setPinJson=new JSONObject();
            setPinJson.put("walletOwnerUserCode",MyApplication.getSaveString("userCode", changepinC));
            setPinJson.put("oldPin",encryptionDataOld);
            setPinJson.put("pin",encryptionDataNew);

            MyApplication.showloader(changepinC,changepinC.getString(R.string.please_wait));
            API.PUT("ewallet/api/v1/walletOwnerUser/changePin", setPinJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            //MyApplication.showToast(changepinC,getString(R.string.pin_changed_msg));
                            MyApplication.showToast(changepinC,jsonObject.optString("resultDescription", "N/A"));
                            Intent intent = new Intent(changepinC, PhoneNumberRegistrationScreen.class);
                            startActivity(intent);
                            finish();


                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(changepinC,getString(R.string.technical_failure));
                            MyApplication.hideLoader();

                        } else {
                            MyApplication.hideLoader();

                            MyApplication.showToast(changepinC,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();

                }
            });

        }catch (Exception e){
            MyApplication.hideLoader();

        }


    }

}
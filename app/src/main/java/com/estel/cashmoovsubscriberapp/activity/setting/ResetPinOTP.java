package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class ResetPinOTP extends AppCompatActivity implements View.OnClickListener {
    public static ResetPinOTP verifyaccountscreenC;
    EditText etOne,etTwo,etThree,etFour,etSix,etFive;
    TextView tvPhoneNoMsg,tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_register_otp);
        verifyaccountscreenC = this;
        getIds();
    }

    private void getIds() {
        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);
        etThree = findViewById(R.id.etThree);
        etFour = findViewById(R.id.etFour);
        etFive = findViewById(R.id.etFive);
        etSix = findViewById(R.id.etSix);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);
        tvContinue = findViewById(R.id.tvContinue);
        tvPhoneNoMsg.setText("Kindly contact your customer care for generate OTP or please enter the OTP to verify your account.");

        TextView[] otpTextViews = {etOne, etTwo, etThree, etFour,etFive,etSix};

        for (TextView currTextView : otpTextViews) {
            currTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nextTextView().requestFocus();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                public TextView nextTextView() {

                    int i;
                    for (i = 0; i < otpTextViews.length - 1; i++) {
                        if (otpTextViews[i] == currTextView)
                            return otpTextViews[i + 1];
                    }
                    return otpTextViews[i];
                }
            });
        }

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvContinue.setOnClickListener(verifyaccountscreenC);
    }

    public String getEditTextString(EditText editText){
        return editText.getText().toString().trim();
    }
    String pass;
    @Override
    public void onClick(View view) {
         pass=getEditTextString(etOne)+getEditTextString(etTwo)+getEditTextString(etThree)+
                 getEditTextString(etFour)+getEditTextString(etFive)+getEditTextString(etSix);
         if(pass.length()==6){
             callApiLoginPass();
         }else{

         }

    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();
           // {transTypeCode: "101813", otp: "11111"}
            loginJson.put("transTypeCode","101813");
            loginJson.put("otp",pass);


            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(ResetPinOTP.this,getString(R.string.getting_user_info));
            API.PUT("ewallet/api/v1/otp/verify", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {



                    if (jsonObject != null) {
                        if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                            // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                            Intent i = new Intent(ResetPinOTP.this, ReSetPin.class);
                            startActivity(i);
                            finish();

                        } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                            MyApplication.showToast(ResetPinOTP.this,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(ResetPinOTP.this,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }

                   // Toast.makeText(VerifyRegisterOTP.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.showToast(ResetPinOTP.this,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}


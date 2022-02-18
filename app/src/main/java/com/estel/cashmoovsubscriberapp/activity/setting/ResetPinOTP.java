package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import org.json.JSONObject;

public class ResetPinOTP extends AppCompatActivity implements View.OnClickListener, OnOtpCompletionListener {
    public static ResetPinOTP resetpinotpC;
    OtpView otp_view;
    TextView tvPhoneNoMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_register_otp);
        resetpinotpC = this;
        getIds();
    }

    private void getIds() {
        otp_view = findViewById(R.id.otp_view);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);
        tvPhoneNoMsg.setText(getString(R.string.verification_register_otp_ip));

//        etOne.addTextChangedListener(new GenericTextWatcher(etTwo, etOne));
//        etTwo.addTextChangedListener(new GenericTextWatcher(etThree, etOne));
//        etThree.addTextChangedListener(new GenericTextWatcher(etFour, etTwo));
//        etFour.addTextChangedListener(new GenericTextWatcher(etFive, etThree));
//        etFive.addTextChangedListener(new GenericTextWatcher(etSix, etFour));
//        etSix.addTextChangedListener(new GenericTextWatcher(etSix, etFive));
//
//
//
//        etSix.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if(s.length() >= 1)
//                    MyApplication.hideKeyboard(resetpinotpC);            }
//        });
//
//        TextView[] otpTextViews = {etOne, etTwo, etThree, etFour,etFive,etSix};
//
//        for (TextView currTextView : otpTextViews) {
//            currTextView.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    nextTextView().requestFocus();
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//
//                public TextView nextTextView() {
//
//                    int i;
//                    for (i = 0; i < otpTextViews.length - 1; i++) {
//                        if (otpTextViews[i] == currTextView)
//                            return otpTextViews[i + 1];
//                    }
//                    return otpTextViews[i];
//                }
//            });
//        }

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        otp_view.setOtpCompletionListener(resetpinotpC);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(resetpinotpC,getString(R.string.val_otp));
        }
    }

//    public class GenericTextWatcher implements TextWatcher {
//        private EditText etPrev;
//        private EditText etNext;
//
//        public GenericTextWatcher(EditText etNext, EditText etPrev) {
//            this.etPrev = etPrev;
//            this.etNext = etNext;
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            String text = editable.toString();
//            if (text.length() == 1)
//                etNext.requestFocus();
//            else if (text.length() == 0)
//                etPrev.requestFocus();
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//        }
//    }
//
//    public String getEditTextString(EditText editText){
//        return editText.getText().toString().trim();
//    }

    @Override
    public void onClick(View view) {
         String pass=otp_view.getText().toString().trim();
         if(pass.length()==6){
             callApiLoginPass(pass);
         }else{
             MyApplication.showToast(resetpinotpC,"Please enter otp");
         }

    }


    private void callApiLoginPass(String otp) {
        try{

            JSONObject loginJson=new JSONObject();
           // {transTypeCode: "101813", otp: "11111"}
            loginJson.put("transTypeCode","101813");
            loginJson.put("otp",otp);


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


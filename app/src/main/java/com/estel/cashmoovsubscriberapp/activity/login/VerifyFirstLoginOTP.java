package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class VerifyFirstLoginOTP extends AppCompatActivity implements View.OnClickListener {
    public static VerifyFirstLoginOTP verifyaccountscreenC;
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

            loginJson.put("username",MyApplication.getSaveString("USERMOBILE",verifyaccountscreenC));
            loginJson.put("password",pass);
            loginJson.put("grant_type","password");
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyFirstLoginOTP.this,getString(R.string.getting_user_info));
            API.POST_REQEST_LoginOTP("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("token",jsonObject.optString("access_token"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("email",jsonObject.optString("email"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("username",jsonObject.optString("username"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"), VerifyFirstLoginOTP.this);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), VerifyFirstLoginOTP.this);

                    // MyApplication.saveString("locale", jsonObject.optString("locale"), LoginActivity.this);
//
//                    try {
//                        JSONArray serviceListResponceArray=jsonObject.optJSONArray("serviceList");
//                        if(serviceListResponceArray!=null&&serviceListResponceArray.length()>0){
//
//                            dataM.clear();
//                            for (int i=0;i<serviceListResponceArray.length();i++){
//
//                                JSONObject jsonObjectServiceList=serviceListResponceArray.optJSONObject(i);
//                                JSONArray serviceCategoryListResArray=jsonObjectServiceList.optJSONArray("serviceCategoryList");
//                                if(serviceCategoryListResArray!=null&&serviceCategoryListResArray.length()>0){
//                                    ArrayList<ServiceList.serviceCategoryList> data=new ArrayList<>();
//                                    data.clear();
//                                    for (int j=0;j<serviceCategoryListResArray.length();j++){
//                                        JSONObject jsonObjectServiceListResponceArray=serviceCategoryListResArray.optJSONObject(j);
//
//                                        data.add(new ServiceList.serviceCategoryList(
//                                                jsonObjectServiceListResponceArray.optInt("id"),
//                                                jsonObjectServiceListResponceArray.optString("code"),
//                                                jsonObjectServiceListResponceArray.optString("serviceCode"),
//                                                jsonObjectServiceListResponceArray.optString("serviceName"),
//                                                jsonObjectServiceListResponceArray.optString("name"),
//                                                jsonObjectServiceListResponceArray.optString("nameFr"),
//                                                jsonObjectServiceListResponceArray.optString("status"),
//                                                jsonObjectServiceListResponceArray.optString("creationDate")
//                                        ));
//
//                                    }
//
//                                    dataM.add(new ServiceList.serviceListMain(
//                                            jsonObjectServiceList.optInt("id"),
//                                            jsonObjectServiceList.optString("code"),
//                                            jsonObjectServiceList.optString("nameFr"),
//                                            jsonObjectServiceList.optString("name"),
//                                            jsonObjectServiceList.optString("status"),
//                                            jsonObjectServiceList.optString("creationDate"),
//                                            data
//                                    ));
//
//                                }
//                            }
//
//                            ServiceList serviceList=new ServiceList(dataM);
//                            MyApplication.tinyDB.putObject("ServiceList",serviceList);
//                        }
//                    }catch (Exception e){
//
//                    }

//                    Intent i = new Intent(VerifyFirstLoginOTP.this, SetPinFisrtLogin.class);
//                    startActivity(i);
//                    finish();
                    Intent i = new Intent(VerifyFirstLoginOTP.this, MainActivity.class);
                    startActivity(i);
                    finish();
                   // Toast.makeText(VerifyRegisterOTP.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.showToast(VerifyFirstLoginOTP.this,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}


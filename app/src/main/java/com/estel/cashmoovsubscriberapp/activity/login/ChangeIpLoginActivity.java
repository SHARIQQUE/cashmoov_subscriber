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

public class ChangeIpLoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static ChangeIpLoginActivity verifyaccountscreenC;
    EditText etOne,etTwo,etThree,etFour,etFive,etSix;
    TextView tvPhoneNoMsg,tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account_screen);
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

        tvPhoneNoMsg.setText("Kindly contact your customer care for generate OTP or please enter the OTP to verify your account.");

        setOnCLickListener();

    }

    public String getEditTextString(EditText editText){
        return editText.getText().toString().trim();
    }
    String pass;
    private void setOnCLickListener() {
        tvContinue.setOnClickListener(verifyaccountscreenC);
    }

    @Override
    public void onClick(View view) {

        pass=getEditTextString(etOne)+getEditTextString(etTwo)+getEditTextString(etThree)+
                getEditTextString(etFour)+getEditTextString(etFive)+getEditTextString(etSix);
        if(pass.length()==6){
            callApiLoginPass();
        }else{
            MyApplication.showToast(verifyaccountscreenC,"Please enter otp");
        }

    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();

            loginJson.put("username",MyApplication.UserMobile);
            loginJson.put("password",pass);
            loginJson.put("grant_type","password");
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(verifyaccountscreenC,getString(R.string.getting_user_info));
            API.POST_REQEST_LoginOTP("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("token",jsonObject.optString("access_token"),verifyaccountscreenC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),verifyaccountscreenC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),verifyaccountscreenC);
                    MyApplication.saveString("email",jsonObject.optString("email"),verifyaccountscreenC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),verifyaccountscreenC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),verifyaccountscreenC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),verifyaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyaccountscreenC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),verifyaccountscreenC);
                    MyApplication.saveString("username",jsonObject.optString("username"),verifyaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyaccountscreenC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), verifyaccountscreenC);

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

                    Intent i = new Intent(verifyaccountscreenC, MainActivity.class);
                    startActivity(i);
                    finish();
                    // Toast.makeText(verifyaccountscreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();
                    MyApplication.showToast(verifyaccountscreenC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}


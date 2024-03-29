package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.ServiceList;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerifyRegisterOTP extends AppCompatActivity implements OnOtpCompletionListener {
    public static VerifyRegisterOTP verifyregisterotpC;
    OtpView otp_view;
    TextView tvPhoneNoMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_register_otp);
        verifyregisterotpC = this;
        getIds();
    }

    private void getIds() {
        otp_view = findViewById(R.id.otp_view);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        otp_view.setOtpCompletionListener(verifyregisterotpC);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(verifyregisterotpC,getString(R.string.val_otp));
        }
    }

    private void callApiLoginPass(String otp) {
        try{

            JSONObject loginJson=new JSONObject();

            loginJson.put("username",MyApplication.UserMobile);
            loginJson.put("password",otp);
            loginJson.put("grant_type","password");
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyRegisterOTP.this,getString(R.string.getting_user_info));
            API.POST_REQEST_LoginOTP("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("token",jsonObject.optString("access_token"),VerifyRegisterOTP.this);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),VerifyRegisterOTP.this);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),VerifyRegisterOTP.this);
                    MyApplication.saveString("email",jsonObject.optString("email"),VerifyRegisterOTP.this);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),VerifyRegisterOTP.this);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),VerifyRegisterOTP.this);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),VerifyRegisterOTP.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),VerifyRegisterOTP.this);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),VerifyRegisterOTP.this);
                    MyApplication.saveString("username",jsonObject.optString("username"),VerifyRegisterOTP.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),VerifyRegisterOTP.this);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), VerifyRegisterOTP.this);


                    // MyApplication.saveString("locale", jsonObject.optString("locale"), LoginActivity.this);
//
                    try {
                        JSONArray serviceListResponceArray=jsonObject.optJSONArray("serviceList");
                        if(serviceListResponceArray!=null&&serviceListResponceArray.length()>0){

                            dataM.clear();
                            for (int i=0;i<serviceListResponceArray.length();i++){

                                JSONObject jsonObjectServiceList=serviceListResponceArray.optJSONObject(i);
                                JSONArray serviceCategoryListResArray=jsonObjectServiceList.optJSONArray("serviceCategoryList");
                                if(serviceCategoryListResArray!=null&&serviceCategoryListResArray.length()>0){
                                    ArrayList<ServiceList.serviceCategoryList> data=new ArrayList<>();
                                    data.clear();
                                    for (int j=0;j<serviceCategoryListResArray.length();j++){
                                        JSONObject jsonObjectServiceListResponceArray=serviceCategoryListResArray.optJSONObject(j);

                                        data.add(new ServiceList.serviceCategoryList(
                                                jsonObjectServiceListResponceArray.optInt("id"),
                                                jsonObjectServiceListResponceArray.optString("code"),
                                                jsonObjectServiceListResponceArray.optString("serviceCode"),
                                                jsonObjectServiceListResponceArray.optString("serviceName"),
                                                jsonObjectServiceListResponceArray.optString("name"),
                                                jsonObjectServiceListResponceArray.optString("status"),
                                                jsonObjectServiceListResponceArray.optString("creationDate"),
                                                jsonObjectServiceListResponceArray.optBoolean("productAllowed")
                                        ));

                                    }

                                    dataM.add(new ServiceList.serviceListMain(
                                            jsonObjectServiceList.optInt("id"),
                                            jsonObjectServiceList.optString("code"),
                                            jsonObjectServiceList.optString("name"),
                                            jsonObjectServiceList.optString("status"),
                                            jsonObjectServiceList.optString("creationDate"),
                                            jsonObjectServiceList.optBoolean("isOverdraftTrans"),
                                            data
                                    ));

                                }
                            }

                            ServiceList serviceList=new ServiceList(dataM);
                            MyApplication.tinyDB.putObject("ServiceList",serviceList);
                        }
                    }catch (Exception e){

                    }

                    Intent i = new Intent(VerifyRegisterOTP.this, SetPinFisrtLogin.class);
                    startActivity(i);
                    finish();
                   // Toast.makeText(VerifyRegisterOTP.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.showToast(VerifyRegisterOTP.this,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}


package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.ServiceList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerifyRESETPINScreen extends AppCompatActivity implements OnOtpCompletionListener {
    public static VerifyRESETPINScreen verifyloginaccountscreenC;
    OtpView otp_view;
    TextView tvPhoneNoMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_reset_screen);
        verifyloginaccountscreenC = this;
        getIds();
    }

    private void getIds() {
        otp_view = findViewById(R.id.otp_view);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        otp_view.setOtpCompletionListener(verifyloginaccountscreenC);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(verifyloginaccountscreenC,getString(R.string.val_otp));
        }
    }

    String FCM_TOKEN;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    FCM_TOKEN = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {
                    FCM_TOKEN = task.getResult().getToken();
                    Log.i("FCM TOKEN", FCM_TOKEN);
                }
            }
        });

    }

    private void callApiLoginPass(String otp) {
        try{

            JSONObject loginJson=new JSONObject();

            loginJson.put("username",MyApplication.UserMobile);
            loginJson.put("password",MyApplication.getEncript(otp));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            MyApplication.saveBool("FirstLoginCounter",true,MyApplication.getInstance());
            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyRESETPINScreen.this,"Verify OTP");
            API.POST_RESETPIN("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    /*MyApplication.showloader(verifyloginaccountscreenC,"Verify OTP");
                    MyApplication.hideLoader();*/

                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("token",jsonObject.optString("access_token"),verifyloginaccountscreenC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),verifyloginaccountscreenC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),verifyloginaccountscreenC);
                    MyApplication.saveString("email",jsonObject.optString("email"),verifyloginaccountscreenC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),verifyloginaccountscreenC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),verifyloginaccountscreenC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),verifyloginaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyloginaccountscreenC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),verifyloginaccountscreenC);
                    MyApplication.saveString("username",jsonObject.optString("username"),verifyloginaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyloginaccountscreenC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), verifyloginaccountscreenC);

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
                                                ,
                                                jsonObjectServiceListResponceArray.optInt("minTransValue"),
                                                jsonObjectServiceListResponceArray.optInt("maxTransValue")
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

                    Intent i = new Intent(verifyloginaccountscreenC, SetPin.class);
                    startActivity(i);
                    finish();
                    // Toast.makeText(verifyloginaccountscreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(verifyloginaccountscreenC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}


package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

public class VerifyFirstLoginOTP extends AppCompatActivity implements View.OnClickListener {
    public static VerifyFirstLoginOTP verifyaccountscreenC;
    EditText etOne,etTwo,etThree,etFour,etSix,etFive;
    TextView tvPhoneNoMsg,tvContinue;
    String FCM_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_register_otp);
        verifyaccountscreenC = this;
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
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyFirstLoginOTP.this,getString(R.string.getting_user_info));
            API.POST_REQEST_GENERATEOTP("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
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
                    callAPIWalletOwnerDetails();
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

    public void callAPIWalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode", verifyaccountscreenC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                    ///profileImageName


                    try {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("walletOwner");
                        if (jsonObject1.has("profileImageName")){
                            MyApplication.saveString("ImageName", API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                                    MyApplication.getSaveString("walletOwnerCode", verifyaccountscreenC)+"/"+
                                    jsonObject1.optString("profileImageName"),verifyaccountscreenC);
                        }else{
                            MyApplication.saveString("ImageName", "",verifyaccountscreenC);
                        }
                    }catch (Exception e){

                    }
                    MyApplication.isFirstTime=true;
                    apiCallSecurity();

                }else{
                    MyApplication.showToast(verifyaccountscreenC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(verifyaccountscreenC,aFalse);
            }
        });
    }



    private void apiCallSecurity() {


        API.GET("ewallet/api/v1/loginSecurity", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {
                        Toast.makeText(verifyaccountscreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                        MyApplication.saveBool("FirstLogin",true,verifyaccountscreenC);
                        Intent i = new Intent(verifyaccountscreenC, MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(verifyaccountscreenC, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(verifyaccountscreenC, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(verifyaccountscreenC, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

}


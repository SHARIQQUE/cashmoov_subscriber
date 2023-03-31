package com.estel.cashmoovsubscriberapp.activity.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.HiddenPassTransformationMethod;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.register.RegisterStepOne;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import com.estel.cashmoovsubscriberapp.model.ServiceList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginPin extends AppCompatActivity {
    public static LoginPin loginpinC;
    EditText etPin,etmobile;
    TextView tvContinue,tvFinger,msgText,tvregister,tvregister1,nameText;
    String mName,mLastName,mMobile;
    boolean  isPasswordVisible;
    CardView pin_linear_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);
        loginpinC = this;
        getIds();


    }




    String pin;

    String FCM_TOKEN;


    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.saveString("qrcode","", LoginPin.this);
        test();
        //callfee();
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



    private void getIds() {
        etPin = findViewById(R.id.etPin);
        tvContinue = findViewById(R.id.tvContinue);
        tvFinger = findViewById(R.id.tvFinger);
        msgText = findViewById(R.id.msgText);
        tvregister1 = findViewById(R.id.tvregister1);
        nameText=findViewById(R.id.nameText);
        etmobile=findViewById(R.id.etmobile);

        mName=MyApplication.getSaveString("firstName", LoginPin.this);
        mLastName=MyApplication.getSaveString("lastName", LoginPin.this);

        mMobile=MyApplication.getSaveString("mobile", LoginPin.this);

        if(mLastName.equalsIgnoreCase("null")){
            nameText.setText("Hi "+ mName);

        }else{
            nameText.setText("Hi "+ mName+" "+mLastName);

        }

        etmobile.setText(mMobile);
        etmobile.setEnabled(false);
        etPin.addTextChangedListener(new TextWatcher() {

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
                    MyApplication.hideKeyboard(loginpinC);            }
        });


        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        etPin.setTransformationMethod(hiddenPassTransformationMethod);
        etPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPin.getRight() - etPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = etPin.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            etPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            etPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            etPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        etPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });









        tvregister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
               callMssidn(MyApplication.getSaveString("mobile",loginpinC));
            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(loginpinC,getString(R.string.val_pin));
                    return;
                }
                if(etPin.getText().toString().trim().length()<4) {
                    MyApplication.showErrorToast(loginpinC, getString(R.string.val_valid_pin));
                    return;
                } 
                
                if(!MyApplication.isConnectingToInternet(LoginPin.this)){
                    Toast.makeText(LoginPin.this, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                }else{
                    callApiLoginPass();

                }

                

            }
        });


        pin =MyApplication.getSaveString("pin",loginpinC);
        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", loginpinC);

       /* if(pin!=null && pin.length()==4) {
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()){
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
               // setOnClickListener();

            }else{
                tvFinger.setVisibility(View.GONE);
                msgText.setVisibility(View.GONE);
            }
        }else{
               // setOnClickListener();

            }
        }else{
            tvFinger.setVisibility(View.GONE);
            msgText.setVisibility(View.GONE);
        }
       */
        pin_linear_lay=findViewById(R.id.pin_linear_lay);
        pin_linear_lay.setVisibility(View.GONE);
        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", LoginPin.this);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                setOnClickListener();
                pin_linear_lay.setVisibility(View.GONE);
            }else {
                pin_linear_lay.setVisibility(View.VISIBLE);
            }
        }else{
            pin_linear_lay.setVisibility(View.VISIBLE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickListener();
            }
        });

    }

    private void callMssidn(String mobile) {

        MyApplication.showloader(loginpinC,loginpinC.getString(R.string.please_wait));
        API.GET_PUBLIC("ewallet/public/walletOwner/msisdn/"+mobile, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();
                System.out.println("PhoneNoRegister response======="+jsonObject.toString());

                if (jsonObject != null) {
                    if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                        // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));

                       if(jsonObject.optJSONObject("walletOwnerUser").optBoolean("reSetPinCredRequest")) {
                           MyApplication.UserMobile=mobile;
                            // MyApplication.showToast(PhoneNumberRegistrationScreen.this,"Reset PIN Called");
                            Intent i = new Intent(loginpinC, VerifyRESETPINScreen.class);
                            startActivity(i);

                        }else {

                           MyApplication.showToast(loginpinC,getString(R.string.contactadmin));
                        }

                    }
                }
            }

            @Override
            public void failure(String aFalse) {
                //MyApplication.showToast(phnoregistrationccreenC,aFalse);
                MyApplication.hideLoader();
            }
        });


}

     BiometricPrompt biometricPrompt=null;
    private void setOnClickListener() {

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(loginpinC);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
              //  msgText.setText(getString(R.string.no_fingerprint_senser));
                tvFinger.setVisibility(View.GONE);
                pin_linear_lay.setVisibility(View.VISIBLE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
               // msgText.setText(getString(R.string.no_biometric_senser));
                pin_linear_lay.setVisibility(View.VISIBLE);
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
              //  msgText.setText(getString(R.string.device_not_contain_fingerprint));
                pin_linear_lay.setVisibility(View.VISIBLE);
                tvFinger.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        biometricPrompt = new BiometricPrompt(loginpinC, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                pin_linear_lay.setVisibility(View.VISIBLE);
                biometricPrompt.cancelAuthentication();
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());
                callApiLoginPassF();
               /* Intent intent = new Intent(loginpinC, MainActivity.class);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationFailed() {
                pin_linear_lay.setVisibility(View.VISIBLE);
                biometricPrompt.cancelAuthentication();
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(getString(R.string.use_finger_to_login)).setNegativeButtonText(getString(R.string.cancel)).setConfirmationRequired(false).build();
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biometricPrompt.authenticate(promptInfo);
            }
        });

        biometricPrompt.authenticate(promptInfo);

    }

    private void callApiLoginPass() {
        try{

            pin_linear_lay.setVisibility(View.VISIBLE);
            JSONObject loginJson=new JSONObject();

           
            loginJson.put("username",MyApplication.getSaveString("mobile",loginpinC));
            loginJson.put("password",MyApplication.getEncript(etPin.getText().toString().trim()));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_Login("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("pin",etPin.getText().toString().trim(),loginpinC);

                    MyApplication.saveString("token",jsonObject.optString("access_token"),loginpinC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),loginpinC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),loginpinC);
                    MyApplication.saveString("email",jsonObject.optString("email"),loginpinC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),loginpinC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),loginpinC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),loginpinC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),loginpinC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),loginpinC);
                    MyApplication.saveString("username",jsonObject.optString("username"),loginpinC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),loginpinC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), loginpinC);


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
                        MyApplication.hideLoader();

                    }


                    if(jsonObject.optBoolean("reSetPinCredRequest")){
                        // MyApplication.showloader(LoginActivity.this,"Change Password Screen");
                        MyApplication.hideLoader();
                        Intent i = new Intent(loginpinC, VerifyRESETPINScreen.class);
                        startActivity(i);
                        //callLogin();
                        // callPostGetLoginOTP();
                        //Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }else{

                       callAPIWalletOwnerDetails();
                      //  callLogin();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(loginpinC, VerifyFirstLoginOTP.class);
                        startActivity(i);
                        // callPostGetLoginOTP();
                       /* Intent i = new Intent(loginpinC, ChangeIpLoginActivity.class);
                        startActivity(i);*/
                    }

                    MyApplication.showToast(loginpinC,aFalse);

                }
            });

        }catch (Exception e){
            MyApplication.hideLoader();

        }

    }

    private void callApiLoginPassF() {
        try{
            pin_linear_lay.setVisibility(View.VISIBLE);
            JSONObject loginJson=new JSONObject();


            loginJson.put("username",MyApplication.getSaveString("mobile",loginpinC));
            loginJson.put("password",MyApplication.getEncript(pin));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_Login("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("pin",pin,loginpinC);

                    MyApplication.saveString("token",jsonObject.optString("access_token"),loginpinC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),loginpinC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),loginpinC);
                    MyApplication.saveString("email",jsonObject.optString("email"),loginpinC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),loginpinC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),loginpinC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),loginpinC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),loginpinC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),loginpinC);
                    MyApplication.saveString("username",jsonObject.optString("username"),loginpinC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),loginpinC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), loginpinC);

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


                    if(jsonObject.optBoolean("reSetPinCredRequest")){
                        // MyApplication.showloader(LoginActivity.this,"Change Password Screen");

                        Intent i = new Intent(loginpinC, VerifyRESETPINScreen.class);
                        startActivity(i);
                        //callLogin();
                        // callPostGetLoginOTP();
                        //Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }else{
                       // callLogin();
                       callAPIWalletOwnerDetails();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(loginpinC, VerifyFirstLoginOTP.class);
                        startActivity(i);
                        // callPostGetLoginOTP();
                       /* Intent i = new Intent(loginpinC, ChangeIpLoginActivity.class);
                        startActivity(i);*/
                    }

                    MyApplication.showToast(loginpinC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }


    public void callAPIWalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode", loginpinC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                    ///profileImageName


                    try {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("walletOwner");
                        MyApplication.saveString("profileTypeCodeNew",jsonObject1.optString("profileTypeCode"),LoginPin.this);
                        if (jsonObject1.has("profileImageName")){
                            MyApplication.saveString("ImageName", API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                                    MyApplication.getSaveString("walletOwnerCode", loginpinC)+"/"+
                                    jsonObject1.optString("profileImageName"),loginpinC);
                        }else{
                            MyApplication.saveString("ImageName", "",loginpinC);
                        }
                        apiCallSecurity();
                        //callLogin();
                    }catch (Exception e){

                    }

                }else{
                    MyApplication.showToast(loginpinC,jsonObject.optString("resultDescription"));
                    MyApplication.hideLoader();
                    etmobile.setEnabled(true);


                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                etmobile.setEnabled(true);

                //MyApplication.showToast(loginpinC,aFalse);
            }
        });
    }

    private void apiCallSecurity() {

        API.GET("ewallet/api/v1/loginSecurity", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {


                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {
                        MyApplication.isFirstTime=true;
                        MyApplication.IsPromoCalled = true;
                        MyApplication.isNotification=true;
                        MyApplication.saveBool("FirstLoginCounter",true,LoginPin.this);

                        Intent i = new Intent(loginpinC, MainActivity.class);
                        startActivity(i);
                        finish();

                        Toast.makeText(loginpinC,LoginPin.this.getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                        //MyApplication.hideLoader();

                    } else {

                        Toast.makeText(loginpinC, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(loginpinC, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(loginpinC, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
//    public void callLogin(){
//        MyApplication.isFirstTime=true;
//        MyApplication.IsPromoCalled = true;
//        Intent i = new Intent(loginpinC, MainActivity.class);
//        startActivity(i);
//        finish();
//        Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
//    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(getString(R.string.really_exit))
                .setMessage(getString(R.string.are_you_sure_exit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finishAffinity();
                    }
                }).create().show();
    }


    public void callfee(){
        try {
            JSONObject jsonObject = new JSONObject("{\n" +
                    "    \"transactionId\": \"2193742\",\n" +
                    "    \"requestTime\": \"Tue Dec 28 04:26:43 UTC 2021\",\n" +
                    "    \"responseTime\": \"Tue Dec 28 04:26:44 UTC 2021\",\n" +
                    "    \"resultCode\": \"0\",\n" +
                    "    \"resultDescription\": \"Transaction Successful\",\n" +
                    "    \"walletOwnerTemplateList\": [\n" +
                    "        {\n" +
                    "            \"id\": 6897,\n" +
                    "            \"code\": \"106897\",\n" +
                    "            \"walletOwnerCode\": \"1000002943\",\n" +
                    "            \"walletOwnerName\": \"test\",\n" +
                    "            \"templateCode\": \"101277\",\n" +
                    "            \"templateName\": \"DefaultSubFee\",\n" +
                    "            \"templateCategoryCode\": \"100002\",\n" +
                    "            \"templateCategoryName\": \"Fee\",\n" +
                    "            \"status\": \"Active\",\n" +
                    "            \"state\": \"Approved\",\n" +
                    "            \"creationDate\": \"2021-11-23T03:31:27.476+0530\",\n" +
                    "            \"modificationDate\": \"2021-12-25T01:35:14.443+0530\",\n" +
                    "            \"modifiedBy\": \"100322\",\n" +
                    "            \"feeTemplateList\": [\n" +
                    "                {\n" +
                    "                    \"id\": 645,\n" +
                    "                    \"code\": \"100776\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100009\",\n" +
                    "                    \"serviceName\": \"Airtime Purchase\",\n" +
                    "                    \"serviceCategoryCode\": \"100021\",\n" +
                    "                    \"serviceCategoryName\": \"Mobile Prepaid\",\n" +
                    "                    \"serviceProviderCode\": \"100136\",\n" +
                    "                    \"serviceProviderName\": \"Noble\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 100000.0,\n" +
                    "                    \"maxValue\": 1.0E7,\n" +
                    "                    \"fixedFeeValue\": 200.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100308\",\n" +
                    "                    \"creationDate\": \"2021-12-25T02:51:13.159+0530\",\n" +
                    "                    \"modificationDate\": \"2021-12-25T02:51:46.959+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"productCode\": \"100031\",\n" +
                    "                    \"productName\": \"Recharge CELLCOM\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 644,\n" +
                    "                    \"code\": \"100775\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100009\",\n" +
                    "                    \"serviceName\": \"Airtime Purchase\",\n" +
                    "                    \"serviceCategoryCode\": \"100021\",\n" +
                    "                    \"serviceCategoryName\": \"Mobile Prepaid\",\n" +
                    "                    \"serviceProviderCode\": \"100136\",\n" +
                    "                    \"serviceProviderName\": \"Noble\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 1.0,\n" +
                    "                    \"maxValue\": 100000.0,\n" +
                    "                    \"fixedFeeValue\": 77.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-12-23T23:50:48.917+0530\",\n" +
                    "                    \"modificationDate\": \"2021-12-23T23:51:12.428+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"productCode\": \"100029\",\n" +
                    "                    \"productName\": \"Recharge Orange\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 643,\n" +
                    "                    \"code\": \"100774\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100001\",\n" +
                    "                    \"serviceName\": \"Recharge & Payment\",\n" +
                    "                    \"serviceCategoryCode\": \"100028\",\n" +
                    "                    \"serviceCategoryName\": \"TV\",\n" +
                    "                    \"serviceProviderCode\": \"100140\",\n" +
                    "                    \"serviceProviderName\": \"Noble\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 1.0,\n" +
                    "                    \"maxValue\": 100000.0,\n" +
                    "                    \"fixedFeeValue\": 150.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-12-23T23:48:08.645+0530\",\n" +
                    "                    \"modificationDate\": \"2021-12-23T23:48:53.594+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"productCode\": \"100028\",\n" +
                    "                    \"productName\": \"STARTIMES RECHARGE\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 629,\n" +
                    "                    \"code\": \"100760\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100000\",\n" +
                    "                    \"serviceName\": \"Money Transfer\",\n" +
                    "                    \"serviceCategoryCode\": \"INTREM\",\n" +
                    "                    \"serviceCategoryName\": \"International Remittance\",\n" +
                    "                    \"serviceProviderCode\": \"100113\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100002\",\n" +
                    "                    \"calculationTypeName\": \"Percentage\",\n" +
                    "                    \"minValue\": 20001.0,\n" +
                    "                    \"maxValue\": 30000.0,\n" +
                    "                    \"fixedFeeValue\": 0.0,\n" +
                    "                    \"percentFeeValue\": 1.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-24T19:00:49.777+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T19:02:22.845+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"sendCountryCode\": \"100092\",\n" +
                    "                    \"sendCountryName\": \"Guinea\",\n" +
                    "                    \"sendCurrencyCode\": \"100062\",\n" +
                    "                    \"sendCurrencyName\": \"GNF\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 628,\n" +
                    "                    \"code\": \"100759\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100000\",\n" +
                    "                    \"serviceName\": \"Money Transfer\",\n" +
                    "                    \"serviceCategoryCode\": \"INTREM\",\n" +
                    "                    \"serviceCategoryName\": \"International Remittance\",\n" +
                    "                    \"serviceProviderCode\": \"100113\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 100001.0,\n" +
                    "                    \"maxValue\": 20000.0,\n" +
                    "                    \"fixedFeeValue\": 215.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-24T19:00:04.343+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T19:02:22.817+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"sendCountryCode\": \"100092\",\n" +
                    "                    \"sendCountryName\": \"Guinea\",\n" +
                    "                    \"sendCurrencyCode\": \"100062\",\n" +
                    "                    \"sendCurrencyName\": \"GNF\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 626,\n" +
                    "                    \"code\": \"100757\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100009\",\n" +
                    "                    \"serviceName\": \"Airtime Purchase\",\n" +
                    "                    \"serviceCategoryCode\": \"100021\",\n" +
                    "                    \"serviceCategoryName\": \"Mobile Prepaid\",\n" +
                    "                    \"serviceProviderCode\": \"100136\",\n" +
                    "                    \"serviceProviderName\": \"Noble\",\n" +
                    "                    \"calculationTypeCode\": \"100002\",\n" +
                    "                    \"calculationTypeName\": \"Percentage\",\n" +
                    "                    \"minValue\": 10.0,\n" +
                    "                    \"maxValue\": 1000000.0,\n" +
                    "                    \"fixedFeeValue\": 0.0,\n" +
                    "                    \"percentFeeValue\": 0.5,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-24T04:52:26.740+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T04:53:06.570+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"productCode\": \"100031\",\n" +
                    "                    \"productName\": \"Recharge CELLCOM\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 625,\n" +
                    "                    \"code\": \"100756\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100009\",\n" +
                    "                    \"serviceName\": \"Airtime Purchase\",\n" +
                    "                    \"serviceCategoryCode\": \"100021\",\n" +
                    "                    \"serviceCategoryName\": \"Mobile Prepaid\",\n" +
                    "                    \"serviceProviderCode\": \"100136\",\n" +
                    "                    \"serviceProviderName\": \"Noble\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 10.0,\n" +
                    "                    \"maxValue\": 1000000.0,\n" +
                    "                    \"fixedFeeValue\": 75.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-24T03:50:49.301+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T04:26:42.347+0530\",\n" +
                    "                    \"bearer\": \"100020\",\n" +
                    "                    \"bearerName\": \"Receiver\",\n" +
                    "                    \"productCode\": \"100030\",\n" +
                    "                    \"productName\": \"Recharge MTN\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 622,\n" +
                    "                    \"code\": \"100753\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100012\",\n" +
                    "                    \"serviceName\": \"Pay\",\n" +
                    "                    \"serviceCategoryCode\": \"100057\",\n" +
                    "                    \"serviceCategoryName\": \"Pay\",\n" +
                    "                    \"serviceProviderCode\": \"100134\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100002\",\n" +
                    "                    \"calculationTypeName\": \"Percentage\",\n" +
                    "                    \"minValue\": 10.0,\n" +
                    "                    \"maxValue\": 1000000.0,\n" +
                    "                    \"fixedFeeValue\": 0.0,\n" +
                    "                    \"percentFeeValue\": 0.3,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-20T00:05:53.994+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T06:59:32.194+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 621,\n" +
                    "                    \"code\": \"100752\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100000\",\n" +
                    "                    \"serviceName\": \"Money Transfer\",\n" +
                    "                    \"serviceCategoryCode\": \"100024\",\n" +
                    "                    \"serviceCategoryName\": \"To Subscriber\",\n" +
                    "                    \"serviceProviderCode\": \"100112\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 10.0,\n" +
                    "                    \"maxValue\": 1.0E8,\n" +
                    "                    \"fixedFeeValue\": 50.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-18T23:48:28.083+0530\",\n" +
                    "                    \"modificationDate\": \"2021-12-23T23:12:01.431+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"sendCountryCode\": \"100092\",\n" +
                    "                    \"sendCountryName\": \"Guinea\",\n" +
                    "                    \"sendCurrencyCode\": \"100062\",\n" +
                    "                    \"sendCurrencyName\": \"GNF\",\n" +
                    "                    \"receiveCountryCode\": \"100092\",\n" +
                    "                    \"receiveCountryName\": \"Guinea\",\n" +
                    "                    \"receiveCurrencyCode\": \"100062\",\n" +
                    "                    \"receiveCurrencyName\": \"GNF\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 602,\n" +
                    "                    \"code\": \"100733\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100000\",\n" +
                    "                    \"serviceName\": \"Money Transfer\",\n" +
                    "                    \"serviceCategoryCode\": \"NONSUB\",\n" +
                    "                    \"serviceCategoryName\": \"To Non Subscriber\",\n" +
                    "                    \"serviceProviderCode\": \"100122\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 10.0,\n" +
                    "                    \"maxValue\": 10000.0,\n" +
                    "                    \"fixedFeeValue\": 100.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100403\",\n" +
                    "                    \"modifiedBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-10T21:35:41.631+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T18:04:35.259+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"sendCountryCode\": \"100092\",\n" +
                    "                    \"sendCountryName\": \"Guinea\",\n" +
                    "                    \"sendCurrencyCode\": \"100062\",\n" +
                    "                    \"sendCurrencyName\": \"GNF\",\n" +
                    "                    \"receiveCountryCode\": \"100092\",\n" +
                    "                    \"receiveCountryName\": \"Guinea\",\n" +
                    "                    \"receiveCurrencyCode\": \"100062\",\n" +
                    "                    \"receiveCurrencyName\": \"GNF\",\n" +
                    "                    \"taxConfigurationList\": [\n" +
                    "                        {\n" +
                    "                            \"id\": 179,\n" +
                    "                            \"code\": \"100178\",\n" +
                    "                            \"taxTypeCode\": \"100133\",\n" +
                    "                            \"taxTypeName\": \"Financial Tax\",\n" +
                    "                            \"calculationTypeCode\": \"100002\",\n" +
                    "                            \"calculationTypeName\": \"Percentage\",\n" +
                    "                            \"value\": 0.0,\n" +
                    "                            \"minValue\": 0.0,\n" +
                    "                            \"maxValue\": 1.0E8,\n" +
                    "                            \"taxAvailBy\": \"Fee Amount\",\n" +
                    "                            \"status\": \"Active\",\n" +
                    "                            \"state\": \"Updated\",\n" +
                    "                            \"createdBy\": \"100377\",\n" +
                    "                            \"modifiedBy\": \"100250\",\n" +
                    "                            \"creationDate\": \"2021-07-14T21:22:09.764+0530\",\n" +
                    "                            \"modificationDate\": \"2021-11-29T12:51:56.546+0530\",\n" +
                    "                            \"percentValue\": 13.0\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 597,\n" +
                    "                    \"code\": \"100728\",\n" +
                    "                    \"templateCode\": \"101277\",\n" +
                    "                    \"templateName\": \"DefaultSubFee\",\n" +
                    "                    \"serviceCode\": \"100000\",\n" +
                    "                    \"serviceName\": \"Money Transfer\",\n" +
                    "                    \"serviceCategoryCode\": \"INTREM\",\n" +
                    "                    \"serviceCategoryName\": \"International Remittance\",\n" +
                    "                    \"serviceProviderCode\": \"100113\",\n" +
                    "                    \"serviceProviderName\": \"Cashmoov\",\n" +
                    "                    \"calculationTypeCode\": \"100001\",\n" +
                    "                    \"calculationTypeName\": \"Fixed\",\n" +
                    "                    \"minValue\": 1.0,\n" +
                    "                    \"maxValue\": 10000.0,\n" +
                    "                    \"fixedFeeValue\": 175.0,\n" +
                    "                    \"percentFeeValue\": 0.0,\n" +
                    "                    \"taxTypeCode\": \"100133,100132\",\n" +
                    "                    \"taxAccount\": \"1000002056\",\n" +
                    "                    \"taxAccountName\": \"TAX Account\",\n" +
                    "                    \"status\": \"Active\",\n" +
                    "                    \"state\": \"Approved\",\n" +
                    "                    \"createdBy\": \"100375\",\n" +
                    "                    \"creationDate\": \"2021-11-03T18:54:16.906+0530\",\n" +
                    "                    \"modificationDate\": \"2021-11-24T19:02:22.787+0530\",\n" +
                    "                    \"bearer\": \"100021\",\n" +
                    "                    \"bearerName\": \"Sender\",\n" +
                    "                    \"sendCountryCode\": \"100092\",\n" +
                    "                    \"sendCountryName\": \"Guinea\",\n" +
                    "                    \"sendCurrencyCode\": \"100062\",\n" +
                    "                    \"sendCurrencyName\": \"GNF\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}");

            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")) {

                JSONObject mainObject=new JSONObject();
                JSONArray mainArr=new JSONArray();

                JSONArray walletOwnerTemplateList = jsonObject.optJSONArray("walletOwnerTemplateList");
                JSONObject data = walletOwnerTemplateList.optJSONObject(0);
                if(data.has("feeTemplateList")) {
                    JSONArray feeTemplateList = data.optJSONArray("feeTemplateList");
                    for (int i = 0; i < feeTemplateList.length(); i++) {
                        JSONObject fee = feeTemplateList.optJSONObject(i);
                        if(i==0){
                            JSONObject jo1=new JSONObject();
                            jo1.put("serviceCode",fee.optString("serviceCode"));
                            jo1.put("serviceName",fee.optString("serviceName"));
                            jo1.put("serviceCategoryCode",fee.optString("serviceCategoryCode"));
                            jo1.put("serviceCategoryName",fee.optString("serviceCategoryName"));
                            if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                jo1.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                            }else{
                                jo1.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                            }
                            jo1.put("calculationTypeName", fee.optString("calculationTypeName"));
                            jo1.put("minValue", fee.optString("minValue"));
                            jo1.put("maxValue", fee.optString("maxValue"));

                            if(fee.has("productCode")){
                                jo1.put("productCode", fee.optString("productCode"));
                               // dataObject.put("productCode", fee.optString("productCode"));
                            }
                            if(fee.has("productName")){
                                jo1.put("productName", fee.optString("productName"));
                               // dataObject.put("productName", fee.optString("productName"));
                            }
                            mainArr.put(jo1);

                        }

                        if(i>0){

                            JSONObject jo1=new JSONObject();
                            jo1.put("serviceCode",fee.optString("serviceCode"));
                            jo1.put("serviceName",fee.optString("serviceName"));
                            jo1.put("serviceCategoryCode",fee.optString("serviceCategoryCode"));
                            jo1.put("serviceCategoryName",fee.optString("serviceCategoryName"));
                            if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                jo1.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                            }else{
                                jo1.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                            }
                            jo1.put("calculationTypeName", fee.optString("calculationTypeName"));
                            jo1.put("minValue", fee.optString("minValue"));
                            jo1.put("maxValue", fee.optString("maxValue"));
                            if(fee.has("productCode")){
                                jo1.put("productCode", fee.optString("productCode"));
                                // dataObject.put("productCode", fee.optString("productCode"));
                            }
                            if(fee.has("productName")){
                                jo1.put("productName", fee.optString("productName"));
                                // dataObject.put("productName", fee.optString("productName"));
                            }

                            for(int j=0;j<mainArr.length();j++){

                                if(mainArr.toString().contains(fee.optString("serviceCategoryCode"))){
                                    mainArr.put(j,jo1);
                                    isTrue=true;


                                }

                            }
                            if(isTrue){

                            }else {
                                mainArr.put(jo1);
                            }

                        }

                    }
                }

                mainObject.put("data",mainArr);
                System.out.println("Fee"+ mainObject.toString());
            }


            }catch (Exception e){

        }
    }

public boolean isTrue=false;


    public void test(){
        try{

            JSONArray jsonArray=new JSONArray("[{id:7, vendorcode:'LIBYANA', productcode:'LMP3', denomination:'3.0', recordcount:'2', pinNo_encript:'4596653520316', serialNumber:'225001150023346', key:'5C441CDF5D0C4A71', status_check:'Y'},\n" +
                    "{id:9, vendorcode:'LIBYANA', productcode:'LMP5', denomination:'5.0', recordcount:'2', pinNo_encript:'5844757500024', serialNumber:'201621161299710', key:'EBE4FEB2432E0E54', status_check:'Y'},\n" +
                    "{id:9, vendorcode:'LIBYANA', productcode:'LMP3', denomination:'3.0', recordcount:'2', pinNo_encript:'5844757500024', serialNumber:'201621161299747', key:'EBE4FEB2432E0E54', status_check:'Y'},\n" +
                    "{id:9, vendorcode:'ABCD', productcode:'aBC', denomination:'3.0', recordcount:'2', pinNo_encript:'5844757500024', serialNumber:'201621161299747', key:'EBE4FEB2432E0E54', status_check:'Y'},\n" +
                    "{id:10, vendorcode:'LIBYANA', productcode:'LMP5', denomination:'5.0', recordcount:'2', pinNo_encript:'7622364700044', serialNumber:'201621161299711', key:'FED478DF3F23EF0A', status_check:'Y'}]");
            JSONArray records=new JSONArray();

            for(int i=0;i<jsonArray.length();i++){
                if(i==0) {

                    JSONObject jsonObject = new JSONObject();
                    JSONArray stock=new JSONArray();
                    JSONObject st_Obj=new JSONObject();
                    jsonObject.put("productcode", jsonArray.optJSONObject(i).optString("productcode"));
                    jsonObject.put("denomination", jsonArray.optJSONObject(i).optString("denomination"));
                    jsonObject.put("stockcount", "1");

                    st_Obj.put("s",jsonArray.optJSONObject(i).optString("serialNumber"));
                    st_Obj.put("d","31/12/21 20:08:33");

                    stock.put(st_Obj);
                    jsonObject.put("stock",stock);
                    records.put(jsonObject);
                }else{

                    int value=hasValue(records,"productcode",jsonArray.optJSONObject(i).optString("productcode"));
                    if(value!= -1){
                        JSONObject jsonObject = new JSONObject();
                        JSONObject st_Obj=new JSONObject();
                        st_Obj.put("s",jsonArray.optJSONObject(i).optString("serialNumber"));
                        st_Obj.put("d","31/12/21 20:08:33");

                       records.optJSONObject(value).put("stockcount", (Integer.parseInt(records.optJSONObject(value).optString("stockcount"))+1)+"");
                        records.optJSONObject(value).optJSONArray("stock").put(st_Obj);
                    }else {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray stock=new JSONArray();
                        JSONObject st_Obj=new JSONObject();
                        jsonObject.put("productcode", jsonArray.optJSONObject(i).optString("productcode"));
                        jsonObject.put("denomination", jsonArray.optJSONObject(i).optString("denomination"));
                        jsonObject.put("stockcount", "1");

                        st_Obj.put("s",jsonArray.optJSONObject(i).optString("serialNumber"));
                        st_Obj.put("d","31/12/21 20:08:33");

                        stock.put(st_Obj);
                        jsonObject.put("stock",stock);
                        records.put(jsonObject);
                    }
                }

            }


            System.out.println("=================="+records.toString());




        }catch (Exception e){

        }
    }

    public int hasValue(JSONArray json, String key, String value) {
        for(int i = 0; i < json.length(); i++) {  // iterate through the JsonArray
            // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
            if(json.optJSONObject(i).optString(key).equals(value)) return i;
        }
        return -1;
    }

}

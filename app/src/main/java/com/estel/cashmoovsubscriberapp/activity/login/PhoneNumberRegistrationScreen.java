package com.estel.cashmoovsubscriberapp.activity.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.register.RegisterStepOne;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.CountryInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class PhoneNumberRegistrationScreen extends AppCompatActivity {
    public static PhoneNumberRegistrationScreen phnoregistrationccreenC;
    TextView spCountry,tvContinue,tvPin,tvOr,tvFinger,msgText;
    SpinnerDialog spinnerDialogCountry;
    EditText etPhoneNo,etPass;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> countryModelList=new ArrayList<>();
    String FCM_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_number_registration_screen);

        phnoregistrationccreenC = this;

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
        spCountry = findViewById(R.id.spCountry);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        etPass = findViewById(R.id.etPass);
        tvPin = findViewById(R.id.tvPin);
        tvOr = findViewById(R.id.tvOr);
        tvFinger = findViewById(R.id.tvFinger);
        msgText = findViewById(R.id.msgText);
        tvContinue = findViewById(R.id.tvContinue);


//        spCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (spinnerDialogCountry!=null)
//                    spinnerDialogCountry.showSpinerDialog();
//            }
//        });

        setOnCLickListener();

        callApiCountry();

    }

    private void setOnCLickListener() {
        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(phnoregistrationccreenC);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgText.setText(getString(R.string.no_fingerprint_senser));
                msgText.setVisibility(View.GONE);
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgText.setText(getString(R.string.no_biometric_senser));
                msgText.setVisibility(View.GONE);
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgText.setText(getString(R.string.device_not_contain_fingerprint));
                msgText.setVisibility(View.GONE);
                tvFinger.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(phnoregistrationccreenC, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());
               /* Intent intent = new Intent(phnoregistrationccreenC, MainActivity.class);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(getString(R.string.use_finger_to_login)).setNegativeButtonText(getString(R.string.cancel)).build();
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

        tvPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(phnoregistrationccreenC, LoginPin.class);
                startActivity(intent);

            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhoneNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(phnoregistrationccreenC,getString(R.string.val_phone),etPhoneNo);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                }
                if(etPhoneNo.getText().toString().trim().length()<9) {
                    MyApplication.showTipError(phnoregistrationccreenC,getString(R.string.enter_phone_no_val),etPhoneNo);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                }
                if(etPass.isShown()&&etPass.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(phnoregistrationccreenC, getString(R.string.val_pin),etPass);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                }if(etPass.isShown()&&etPass.getText().toString().trim().length()!=4) {
                    MyApplication.showTipError(phnoregistrationccreenC, getString(R.string.val_valid_pin),etPass);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                } else {
                    if(etPass.isShown()){
                        callApiLoginPass();
                    }else{
                        callApiCheckMobile();
                    }

                }

            }
        });
    }

    private void callApiCountry() {
        try {
            API.GET_PUBLIC("ewallet/public/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                countryList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                         if(data.optBoolean("subscriberAllowed")) {
                                        if (data.optString("code").equalsIgnoreCase("100092")) {
                                            countryModelList.add(new CountryInfoModel.Country(
                                                    data.optInt("id"),
                                                    data.optInt("mobileLength"),
                                                    data.optString("code"),
                                                    data.optString("isoCode"),
                                                    data.optString("name"),
                                                    data.optString("countryCode"),
                                                    data.optString("status"),
                                                    data.optString("dialCode"),
                                                    data.optString("currencyCode"),
                                                    data.optString("currencySymbol"),
                                                    data.optString("creationDate"),
                                                    data.optBoolean("subscriberAllowed")
                                            ));

                                            countryList.add(data.optString("name").trim());
                                            spCountry.setText(countryList.get(0));

                                            }
                                        }

                                    }

                                    spinnerDialogCountry = new SpinnerDialog(phnoregistrationccreenC, countryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogCountry.setCancellable(true); // for cancellable
                                    spinnerDialogCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCountry.setText(item);
                                            spCountry.setTag(position);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(phnoregistrationccreenC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });


        } catch (Exception e) {

        }

    }

    private void callApiLoginOtp() {
        try{

            JSONObject loginOtpJson=new JSONObject();
            loginOtpJson.put("mobileNumber",etPhoneNo.getText().toString());
            loginOtpJson.put("walletOwnerCategoryCode","100010");

            MyApplication.showloader(phnoregistrationccreenC,"Please wait!");
            API.POST_REQEST_CHECK("ewallet/public/login-otp", loginOtpJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    System.out.println("PhoneNoRegister response======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                                    etPass.setVisibility(View.VISIBLE);
                                    tvPin.setVisibility(View.VISIBLE);
                                    tvOr.setVisibility(View.VISIBLE);
                                    tvFinger.setVisibility(View.VISIBLE);
                            MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("1000")){
                            MyApplication.showToast(phnoregistrationccreenC,getString(R.string.technical_failure));
                        } else {
                            //MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));

                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    //MyApplication.showToast(phnoregistrationccreenC,aFalse);
                    MyApplication.showToast(phnoregistrationccreenC,getString(R.string.register_first_time_message));
                    Intent intent = new Intent(phnoregistrationccreenC, RegisterStepOne.class);
                    startActivity(intent);
                }
            });

        }catch (Exception e){

        }

    }

    private void callApiCheckMobile() {
        try{

            JSONObject loginOtpJson=new JSONObject();
            loginOtpJson.put("mobileNumber",etPhoneNo.getText().toString());
            loginOtpJson.put("walletOwnerCategoryCode","100010");

            MyApplication.showloader(phnoregistrationccreenC,"Please wait!");
            API.GET_PUBLIC("ewallet/public/walletOwner/msisdn/"+etPhoneNo.getText().toString(), new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    System.out.println("PhoneNoRegister response======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                            etPass.setVisibility(View.VISIBLE);
                            tvPin.setVisibility(View.GONE);
                            tvOr.setVisibility(View.GONE);
                            tvFinger.setVisibility(View.GONE);
                            MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                        } else {
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("1074")){
                                MyApplication.showToast(phnoregistrationccreenC,getString(R.string.register_first_time_message));
                                Intent intent = new Intent(phnoregistrationccreenC, RegisterStepOne.class);
                                startActivity(intent);
                            }else {
                                MyApplication.showToast(phnoregistrationccreenC, jsonObject.optString("resultDescription", "N/A"));
                            }
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    //MyApplication.showToast(phnoregistrationccreenC,aFalse);
                    MyApplication.showToast(phnoregistrationccreenC,getString(R.string.register_first_time_message));
                    Intent intent = new Intent(phnoregistrationccreenC, RegisterStepOne.class);
                    startActivity(intent);
                }
            });

        }catch (Exception e){

        }

    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();

            MyApplication.UserMobile=etPhoneNo.getText().toString().trim();
            loginJson.put("username",etPhoneNo.getText().toString().trim());
            loginJson.put("password",etPass.getText().toString().trim());
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
           // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(phnoregistrationccreenC,getString(R.string.getting_user_info));
            API.POST_REQEST_Login("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                    System.out.println("Login response======="+jsonObject.toString());
                    MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),phnoregistrationccreenC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),phnoregistrationccreenC);
                    MyApplication.saveString("email",jsonObject.optString("email"),phnoregistrationccreenC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),phnoregistrationccreenC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),phnoregistrationccreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),phnoregistrationccreenC);
                    MyApplication.saveString("username",jsonObject.optString("username"),phnoregistrationccreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), phnoregistrationccreenC);

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
                    if(jsonObject.optString("firstLoginStatus").equalsIgnoreCase("Y")){
                        // MyApplication.showloader(LoginActivity.this,"Change Password Screen");
                        MyApplication.saveBool("FirstLogin",true,phnoregistrationccreenC);
                        MyApplication.saveString("loginUsername", etPhoneNo.getText().toString().trim(), phnoregistrationccreenC);
                        MyApplication.saveString("loginPassword",etPass.getText().toString().trim(), phnoregistrationccreenC);
                        //MyApplication.saveString("token","b1b80862-17b3-48f0-83a3-b4d27ddd09e2",phnoregistrationccreenC);

                        Intent i = new Intent(phnoregistrationccreenC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(phnoregistrationccreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                      // callPostGetLoginOTP();
                        //Toast.makeText(phnoregistrationccreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }else{
                        MyApplication.saveBool("FirstLogin",true,phnoregistrationccreenC);
                        MyApplication.saveString("loginUsername", etPhoneNo.getText().toString().trim(), phnoregistrationccreenC);
                        MyApplication.saveString("loginPassword", etPass.getText().toString().trim(), phnoregistrationccreenC);

                        Intent i = new Intent(phnoregistrationccreenC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(phnoregistrationccreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(phnoregistrationccreenC, VerifyLoginAccountScreen.class);
                        startActivity(i);
                       // callPostGetLoginOTP();
                       /* Intent i = new Intent(phnoregistrationccreenC, ChangeIpLoginActivity.class);
                        startActivity(i);*/
                    }

                    MyApplication.showToast(phnoregistrationccreenC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }


    public void callPostGetLoginOTP(){
        //{"userName":"99108591851"}
        JSONObject jsonObject1=new JSONObject();
        try {
           // {"walletownerusercode":"101992","notificationtypecode":"100000","transtypecode":"101813","email":"sahil.thakur08@gmail.com"}
          /*  jsonObject1.put("walletownerusercode","101992");
            jsonObject1.put("notificationtypecode","100000");
            jsonObject1.put("transtypecode","101813");
            jsonObject1.put("email","sahil,thakur08@gmail.com");
*/

            jsonObject1.put("walletownerusercode",MyApplication.getSaveString("userCode",phnoregistrationccreenC));
            jsonObject1.put("notificationtypecode","100000");
            jsonObject1.put("transtypecode","101813");
            jsonObject1.put("email",MyApplication.getSaveString("email",phnoregistrationccreenC));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.showloader(phnoregistrationccreenC,"Please Wait...");
        API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject1,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            Intent i = new Intent(phnoregistrationccreenC, VerifyLoginAccountScreen.class);
                            startActivity(i);

                            MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                            //  MyApplication.showToast(jsonObject.optString("resultDescription"));
                        }else{
                            MyApplication.showToast(phnoregistrationccreenC,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();

                    }
                });
    }

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

}

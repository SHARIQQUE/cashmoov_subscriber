package com.estel.cashmoovsubscriberapp.activity.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.Executor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginPin extends AppCompatActivity {
    public static LoginPin loginpinC;
    EditText etPin;
    TextView tvContinue,tvFinger,msgText,tvregister;
    ImageView icPin;


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
        icPin = findViewById(R.id.icPin);
        tvContinue = findViewById(R.id.tvContinue);
        tvFinger = findViewById(R.id.tvFinger);
        msgText = findViewById(R.id.msgText);
        tvregister = findViewById(R.id.tvregister);


        icPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPin.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    icPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    icPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etPin.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });


        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(loginpinC, RegisterStepOne.class);
                startActivity(intent);
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
                    callApiLoginPass();
                    
                

            }
        });

        pin =MyApplication.getSaveString("pin",loginpinC);
        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", loginpinC);

        if(pin!=null && pin.length()==4) {
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()){
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                setOnClickListener();

            }else{
                tvFinger.setVisibility(View.GONE);
                msgText.setVisibility(View.GONE);
            }
        }else{
                setOnClickListener();

            }
        }else{
            tvFinger.setVisibility(View.GONE);
            msgText.setVisibility(View.GONE);
        }

        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickListener();
            }
        });

    }

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
                msgText.setText(getString(R.string.no_fingerprint_senser));
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgText.setText(getString(R.string.device_not_contain_fingerprint));
                tvFinger.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(loginpinC, executor, new BiometricPrompt.AuthenticationCallback() {
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
                callApiLoginPassF();
               /* Intent intent = new Intent(loginpinC, MainActivity.class);
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

        biometricPrompt.authenticate(promptInfo);

    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();

           
            loginJson.put("username",MyApplication.getSaveString("mobile",loginpinC));
            loginJson.put("password",etPin.getText().toString().trim());
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_Login("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
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

                        Intent i = new Intent(loginpinC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                        // callPostGetLoginOTP();
                        //Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }else{

                        Intent i = new Intent(loginpinC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(loginpinC, VerifyLoginAccountScreen.class);
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

    private void callApiLoginPassF() {
        try{

            JSONObject loginJson=new JSONObject();


            loginJson.put("username",MyApplication.getSaveString("mobile",loginpinC));
            loginJson.put("password",pin);
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_Login("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
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

                        Intent i = new Intent(loginpinC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                        // callPostGetLoginOTP();
                        //Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }else{

                        Intent i = new Intent(loginpinC, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(loginpinC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(loginpinC, VerifyLoginAccountScreen.class);
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

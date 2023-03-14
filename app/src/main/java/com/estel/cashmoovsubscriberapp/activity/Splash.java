package com.estel.cashmoovsubscriberapp.activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.LoginPin;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Splash extends LogoutAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        MyApplication.setLang(Splash.this);
//        createJson();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        Context context = Splash.this;
                        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                        System.out.println("IPPPP  "+ip);

                        if(MyApplication.getSaveBool("FirstLogin",Splash.this)) {
                            Intent i = new Intent(Splash.this, LoginPin.class);
                            startActivity(i);
                            finish();
                            /*Intent i = new Intent(Splash.this, Login.class);
                            startActivity(i);
                            finish();*/
                        }else{
                            Intent i = new Intent(Splash.this, OnboardingOne.class);
                            startActivity(i);
                            finish();

                        }

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(Splash.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                                .show();
                    }


                };

                TedPermission.with(Splash.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission, you can not use this service.\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setGotoSettingButtonText("Go to settings")
                        .setPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,READ_CONTACTS,ACCESS_FINE_LOCATION)
                        .check();

                // This method will be executed once the timer is over

            }
        }, 2000);

//test

    }







   /* public void createJson(){
        JSONObject mainObj=new JSONObject();
        JSONArray mainArray=new JSONArray();
        JSONObject dataObj=new JSONObject();
        try{
            dataObj.put("actionType","");
            dataObj.put("assignTo","");
            dataObj.put("comments","");
            dataObj.put("entityCode","");
            dataObj.put("entityName","");
            dataObj.put("entityCode","");
            dataObj.put("featureCode","");
            dataObj.put("status","");

            JSONObject oneObj=new JSONObject();
            JSONObject twoObj=new JSONObject();
            twoObj.put("code","");
            twoObj.put("featureCode","");
            twoObj.put("desWalletCode","");
            twoObj.put("srcWalletCode","");
            twoObj.put("srcWalletOwnerCode","");
            twoObj.put("srcWalletOwnerName","");
            twoObj.put("srcCurrencyCode","");
            twoObj.put("srcCurrencyName","");
            twoObj.put("srcWalletTypeCode","");
            twoObj.put("srcWalletTypeName","");
            twoObj.put("desWalletTypeCode","");
            twoObj.put("desWalletTypeName","");
            twoObj.put("desWalletOwnerCode","");
            twoObj.put("desWalletOwnerName","");
            twoObj.put("desWalletOwnerNumber","");
            twoObj.put("amount","");
            twoObj.put("channelTypeCode","");
            twoObj.put("desCurrencyCode","");
            twoObj.put("desCurrencyName","");
            twoObj.put("status","");
            twoObj.put("createdBy","");
            twoObj.put("creationDate","");

            twoObj.put("tax","");
            twoObj.put("fee","");
            twoObj.put("finalAmount","");
            twoObj.put("srcCurrencySymbol","");
            twoObj.put("desCurrencySymbol","");
            twoObj.put("transactionType","");
            twoObj.put("serviceCode","");
            twoObj.put("serviceCategoryCode","");
            twoObj.put("serviceProviderCode","");

            dataObj.put("entity",twoObj);
            dataObj.put("updatedInformation",oneObj);
            mainArray.put(dataObj);

            mainObj.put("dataApprovalList",mainArray);

            System.out.println("jsososm============"+mainObj.toString());



        }catch (Exception e){

        }


    }*/




}

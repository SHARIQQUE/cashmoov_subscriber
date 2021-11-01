package com.estel.cashmoovsubscriberapp.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.PhoneNumberRegistrationScreen;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class Reset extends AppCompatActivity implements View.OnClickListener {
    public static Reset resetC;
    Button btnCancel, btnConfirm;
    // ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        resetC = this;
        //setBackMenu();
        getIds();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }


    private void getIds() {
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCancel.setOnClickListener(resetC);
        btnConfirm.setOnClickListener(resetC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnCancel:
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.logout))
                        .setIcon(R.drawable.ic_logout)
                        .setMessage(getString(R.string.logout_conf_message))
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                API.POST_REQEST_WH_NEW("ewallet/oauth/logout", null, new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {
                            if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                // MyApplication.saveString("Locale","",myprofileC);
                                MyApplication.saveBool("FirstLogin",false,resetC);
                                MyApplication.saveString("ImageName", "1", resetC);
                                Intent i = new Intent(resetC, PhoneNumberRegistrationScreen.class);
                                startActivity(i);
                                finish();

                            } else {
                                MyApplication.showToast(resetC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                    }
                });

                              /*  Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain);
                                finishAffinity();*/
                            }
                        }).create().show();
                break;
        }
    }



}
package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONObject;

public class MyQrCode extends AppCompatActivity {
    public static MyQrCode myqrcodeC;
    ImageView imgBack,imgHome,imgQR;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        myqrcodeC=this;
        setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(myqrcodeC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(myqrcodeC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        tvName = findViewById(R.id.tvName);
        imgQR = findViewById(R.id.imgQR);

        String fName = MyApplication.getSaveString("firstName", getApplicationContext());
        String lName = MyApplication.getSaveString("lastName", getApplicationContext());
      //  String mobile = MyApplication.getSaveString("mobile", getApplicationContext());
        //String email = MyApplication.getSaveString("email", getApplicationContext());

        String name = null;
        if (fName != null && !fName.isEmpty() && !fName.equals("null")) {
            name = fName;
        }
        if (lName != null && !lName.isEmpty() && !lName.equals("null")) {
            name=fName+" "+lName;
        }
        tvName.setText(getString(R.string.hello)+" "+name);

//        if (mobile != null && !mobile.isEmpty() && !mobile.equals("null")) {
//            txt_mobile.setText(mobile);
//        }

        callApiGenerateQR();

    }

    private void callApiGenerateQR() {
        try{

            JSONObject qrJson=new JSONObject();
            qrJson.put("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", myqrcodeC));

            System.out.println("QR request"+qrJson.toString());

            MyApplication.showloader(myqrcodeC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/qrCode", qrJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    System.out.println("qrJson response======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            displayQRCode(jsonObject.optString("qrCode"));
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(myqrcodeC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(myqrcodeC,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {

                }
            });

        }catch (Exception e){

        }

    }

    private void displayQRCode(String qrCode){
        byte[] qrByteArray = Base64.decode(qrCode, Base64.DEFAULT);
        Glide.with(this)
                .asBitmap()
                .load(qrByteArray)
                .into(imgQR);

    }

}
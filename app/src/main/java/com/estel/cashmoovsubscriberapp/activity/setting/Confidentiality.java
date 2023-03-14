package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.suke.widget.SwitchButton;

public class Confidentiality extends LogoutAppCompactActivity implements View.OnClickListener {
    public static Confidentiality confidentialityC;
    Button btnCancel;
    SwitchButton btnSwich;
    //ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confidentiality);
        confidentialityC=this;
       // setBackMenu();
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
        btnSwich = findViewById(R.id.btnSwich);

        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                btnSwich.setChecked(true);
            }else {
                btnSwich.setChecked(false);
            }
        }else{
            btnSwich.setChecked(true);
        }


        btnSwich.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    MyApplication.saveString("ACTIVATEPROTECTION", "Activate", confidentialityC);
                    MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);
                } else {
                    MyApplication.saveString("ACTIVATEPROTECTION", "Deactivate", confidentialityC);
                    MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);

                }
            }
        });

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCancel.setOnClickListener(confidentialityC);
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

        }
    }

}
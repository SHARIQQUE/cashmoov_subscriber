package com.estel.cashmoovsubscriberapp.activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.LoginPin;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.util.List;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        MyApplication.setLang(Splash.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
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
                        .setPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,READ_CONTACTS)
                        .check();

                // This method will be executed once the timer is over

            }
        }, 2000);



    }




}

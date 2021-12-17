package com.estel.cashmoovsubscriberapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {
    public static Login loginC;
    CardView cardPass;
    EditText etPhone,etPass;
    TextView tvContinue,tvPin,tvOr,tvFinger,msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginC = this;
        getIds();
    }

    private void getIds() {
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        cardPass = findViewById(R.id.cardPass);
        tvContinue = findViewById(R.id.tvContinue);
        tvPin = findViewById(R.id.tvPin);
        tvOr = findViewById(R.id.tvOr);
        tvFinger = findViewById(R.id.tvFinger);
        msgText = findViewById(R.id.msgText);

//        cardPass.setVisibility(View.VISIBLE);
//        tvPin.setVisibility(View.VISIBLE);
//        tvOr.setVisibility(View.VISIBLE);


        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(loginC);
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
        final BiometricPrompt biometricPrompt = new BiometricPrompt(loginC, executor, new BiometricPrompt.AuthenticationCallback() {
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
                System.out.println("Fingerprint Result"+result.toString());
                MyApplication.isFirstTime=true;
                Intent intent = new Intent(loginC, MainActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(loginC, LoginPin.class);
                startActivity(intent);

            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhone.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(loginC,getString(R.string.val_phone));
                    return;
                }
                if(etPhone.getText().toString().trim().length()<9) {
                    MyApplication.showErrorToast(loginC,getString(R.string.enter_phone_no_val));
                    return;
                }
                if(etPass.isShown()&&etPass.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(loginC, getString(R.string.val_pass));
                    return;
                } else {
                    MyApplication.isFirstTime=true;
                    Intent intent = new Intent(loginC, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }




}

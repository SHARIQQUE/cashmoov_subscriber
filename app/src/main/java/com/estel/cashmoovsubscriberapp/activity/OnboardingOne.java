package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;

public class OnboardingOne extends LogoutAppCompactActivity implements View.OnClickListener {
    public static OnboardingOne onboardingoneC;
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_one);
        onboardingoneC = this;
        getIds();
    }

    private void getIds() {
        MyApplication.saveBool("SkipIntro",true,onboardingoneC);
        tvNext = findViewById(R.id.tvNext);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(onboardingoneC);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(onboardingoneC, OnboardingTwo.class);
        startActivity(i);
        finish();
    }
}

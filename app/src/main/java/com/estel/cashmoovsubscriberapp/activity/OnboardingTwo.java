package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;

public class OnboardingTwo extends LogoutAppCompactActivity implements View.OnClickListener {
    public static OnboardingTwo onboardingtwoC;
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_two);
        onboardingtwoC = this;
        getIds();
    }

    private void getIds() {
        tvNext = findViewById(R.id.tvNext);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(onboardingtwoC);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(onboardingtwoC, OnboardingThree.class);
        startActivity(i);
        finish();
    }
}
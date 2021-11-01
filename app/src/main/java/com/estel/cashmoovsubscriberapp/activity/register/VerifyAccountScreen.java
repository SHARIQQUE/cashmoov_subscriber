package com.estel.cashmoovsubscriberapp.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;

public class VerifyAccountScreen extends AppCompatActivity implements View.OnClickListener {
    public static VerifyAccountScreen verifyaccountscreenC;
    EditText etOne,etTwo,etThree,etFour;
    TextView tvPhoneNoMsg,tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account_screen);
        verifyaccountscreenC = this;
        getIds();
    }

    private void getIds() {
        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);
        etThree = findViewById(R.id.etThree);
        etFour = findViewById(R.id.etFour);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);
        tvContinue = findViewById(R.id.tvContinue);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvContinue.setOnClickListener(verifyaccountscreenC);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(verifyaccountscreenC, AccountCreatedScreen.class);
        startActivity(i);
        finish();
    }
}


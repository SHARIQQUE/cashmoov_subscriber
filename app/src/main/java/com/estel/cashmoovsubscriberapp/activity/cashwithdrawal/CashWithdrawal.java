package com.estel.cashmoovsubscriberapp.activity.cashwithdrawal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.ToSubscriberConfirmScreen;

public class CashWithdrawal extends AppCompatActivity implements View.OnClickListener {
    public static CashWithdrawal cashwithdrawalC;
    ImageView imgBack,imgHome;
    TextView tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashwithdrawal);
        cashwithdrawalC=this;
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
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        tvSend = findViewById(R.id.tvSend);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(cashwithdrawalC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvSend:
                intent = new Intent(cashwithdrawalC, ToSubscriberConfirmScreen.class);
                startActivity(intent);
                break;

        }
    }

}
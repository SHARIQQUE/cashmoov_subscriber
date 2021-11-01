package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.BeneficiaryAirtimeReceipt;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.SelfAirtimeReceipt;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayReceipt;

public class TransactionSuccessScreen extends AppCompatActivity implements View.OnClickListener {
    public static TransactionSuccessScreen transSuccessscreenC;
    // ImageView imgBack;
    TextView tvContinue;
    String checkIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_success);
        transSuccessscreenC=this;
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
        tvContinue = findViewById(R.id.tvContinue);

        if (getIntent().getExtras() != null) {
            checkIntent = (getIntent().getStringExtra("SENDINTENT"));

        }
        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvContinue.setOnClickListener(transSuccessscreenC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvContinue:
                if(checkIntent.equalsIgnoreCase("TOSUB")){
                    intent = new Intent(transSuccessscreenC, ToSubscriberReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("TONONSUB")) {
                    intent = new Intent(transSuccessscreenC, ToNonSubscriberReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("INTERNATIONAL")) {
                    intent = new Intent(transSuccessscreenC, InternationalReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("BILLPAY")) {
                    intent = new Intent(transSuccessscreenC, BillPayReceipt.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("SELFAIRTIME")) {
                    intent = new Intent(transSuccessscreenC, SelfAirtimeReceipt.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("BENEFICIARYAIRTIME")) {
                    intent = new Intent(transSuccessscreenC, BeneficiaryAirtimeReceipt.class);
                    startActivity(intent);
                    return;
                }
                break;

        }
    }

}
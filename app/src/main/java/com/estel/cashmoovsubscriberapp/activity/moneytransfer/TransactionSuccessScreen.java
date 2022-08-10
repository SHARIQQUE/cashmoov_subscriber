package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.OnboardingOne;
import com.estel.cashmoovsubscriberapp.activity.Splash;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.BeneficiaryAirtimeReceipt;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.SelfAirtimeReceipt;
import com.estel.cashmoovsubscriberapp.activity.cashout.CashOutReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.cashwithdrawal.CashWithdrawalReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.InFormReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.InFormRecptNew;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.OutFormReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.OutFormRecptNew;
import com.estel.cashmoovsubscriberapp.activity.login.LoginPin;
import com.estel.cashmoovsubscriberapp.activity.partner.PartnerBillPayReceipt;
import com.estel.cashmoovsubscriberapp.activity.pay.PayReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.receiveremittance.ReceiveRemittancelReceiptScreen;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayReceipt;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

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
        setTransaction();
        //setBackMenu();
        //getIds();
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

                if(checkIntent.equalsIgnoreCase("OTOSUB")){
                    intent = new Intent(transSuccessscreenC, OutFormRecptNew.class);
                    startActivity(intent);
                    return;
                }
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
                if(checkIntent.equalsIgnoreCase("PARTNERBILLPAY")) {
                    intent = new Intent(transSuccessscreenC, PartnerBillPayReceipt.class);
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
                if(checkIntent.equalsIgnoreCase("PAY")) {
                    intent = new Intent(transSuccessscreenC, PayReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("CASHWITHDRAWAL")) {
                    intent = new Intent(transSuccessscreenC, CashWithdrawalReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("CASHOUT")) {
                    intent = new Intent(transSuccessscreenC, CashOutReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("RECEIVEREMITTANCE")) {
                    intent = new Intent(transSuccessscreenC, ReceiveRemittancelReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                break;

        }
    }

    private void setTransaction(){
        if (getIntent().getExtras() != null) {
            checkIntent = (getIntent().getStringExtra("SENDINTENT"));

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if(checkIntent.equalsIgnoreCase("INTOSUB")){
                    intent = new Intent(transSuccessscreenC, InFormRecptNew.class);
                    startActivity(intent);
                    return;
                }

                if(checkIntent.equalsIgnoreCase("OTOSUB")){
                    intent = new Intent(transSuccessscreenC, OutFormRecptNew.class);
                    startActivity(intent);
                    return;
                }

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
                if(checkIntent.equalsIgnoreCase("PARTNERBILLPAY")) {
                    intent = new Intent(transSuccessscreenC, PartnerBillPayReceipt.class);
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
                if(checkIntent.equalsIgnoreCase("PAY")) {
                    intent = new Intent(transSuccessscreenC, PayReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("CASHWITHDRAWAL")) {
                    intent = new Intent(transSuccessscreenC, CashWithdrawalReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("CASHOUT")) {
                    intent = new Intent(transSuccessscreenC, CashOutReceiptScreen.class);
                    startActivity(intent);
                    return;
                }
                if(checkIntent.equalsIgnoreCase("RECEIVEREMITTANCE")) {
                    intent = new Intent(transSuccessscreenC, ReceiveRemittancelReceiptScreen.class);
                    startActivity(intent);
                    return;
                }

            }
        }, 2000);
    }

}
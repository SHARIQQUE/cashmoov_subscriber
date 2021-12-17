package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;

public class MoneyTransfer extends AppCompatActivity implements View.OnClickListener {
    public static MoneyTransfer moneytransferC;
    ImageView imgBack,imgHome;
    CardView cardToSubscriber,cardToNonSubscriber,cardInternational,cardBankToWallet,cardWalletToBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);
        moneytransferC=this;
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
                MyApplication.isFirstTime=false;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        cardToSubscriber = findViewById(R.id.cardToSubscriber);
        cardToNonSubscriber = findViewById(R.id.cardToNonSubscriber);
        cardInternational = findViewById(R.id.cardInternational);
        cardBankToWallet = findViewById(R.id.cardBankToWallet);
        cardWalletToBank = findViewById(R.id.cardWalletToBank);

        cardBankToWallet.setVisibility(View.GONE);
        cardWalletToBank.setVisibility(View.GONE);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        cardToSubscriber.setOnClickListener(moneytransferC);
        cardToNonSubscriber.setOnClickListener(moneytransferC);
        cardInternational.setOnClickListener(moneytransferC);
        cardBankToWallet.setOnClickListener(moneytransferC);
        cardWalletToBank.setOnClickListener(moneytransferC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.cardToSubscriber:
                intent = new Intent(moneytransferC, ToSubscriber.class);
                startActivity(intent);
                break;
            case R.id.cardToNonSubscriber:
                intent = new Intent(moneytransferC, ToNonSubscriber.class);
                startActivity(intent);
                break;
            case R.id.cardInternational:
                intent = new Intent(moneytransferC, International.class);
                startActivity(intent);
                break;
            case R.id.cardBankToWallet:
                MyApplication.showToast(moneytransferC,"Coming soon...");
//                intent = new Intent(moneytransferC, BankToWallet.class);
//                startActivity(intent);
                break;
            case R.id.cardWalletToBank:
                MyApplication.showToast(moneytransferC,"Coming soon...");
//                intent = new Intent(moneytransferC, WalletToBank.class);
//                startActivity(intent);
                break;
        }
    }
}
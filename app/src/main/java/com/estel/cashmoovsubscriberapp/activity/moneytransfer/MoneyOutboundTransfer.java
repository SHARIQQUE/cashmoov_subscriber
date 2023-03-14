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
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.InTransfer;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.OutTransfer;

public class MoneyOutboundTransfer extends LogoutAppCompactActivity implements View.OnClickListener {
    public static MoneyOutboundTransfer moneytransferC;
    ImageView imgBack,imgHome;
    CardView mCardCashmoov,mCardToreceiveinternational,mCaretToInternational,mCardToPartners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_outbound);
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
                MyApplication.hideKeyboard(moneytransferC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(moneytransferC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        mCardCashmoov = findViewById(R.id.cardCashmoov);
        mCardToPartners=findViewById(R.id.cardToPartners);
        setOnCLickListener();

    }

    private void setOnCLickListener() {
        mCardCashmoov.setOnClickListener(moneytransferC);
        mCardToPartners.setOnClickListener(moneytransferC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.cardCashmoov:
                if(!MyApplication.showToSubscriber){
                    // cardToSubscriber.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else{
                    intent = new Intent(moneytransferC, International.class);
                    startActivity(intent);
                }
                break;


            case R.id.cardToPartners:
                if(!MyApplication.showInternationalRemit){
                    //cardInternational.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else {
                    intent = new Intent(moneytransferC, OutTransfer.class);
                    startActivity(intent);
                }
                break;

        }
    }
}
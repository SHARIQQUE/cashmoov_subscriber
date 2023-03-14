package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.InTransfer;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.Inform;
import com.estel.cashmoovsubscriberapp.activity.internationaltransfer.OutTransfer;

public class MoneyTransfer extends LogoutAppCompactActivity implements View.OnClickListener {
    public static MoneyTransfer moneytransferC;
    ImageView imgBack,imgHome;
    CardView cardToreceiveinternational,cardToSubscriber,cardToNonSubscriber,cardInternational,cardBankToWallet,cardWalletToBank,cardInternationalIn;
    private long mLastClickTime = 0;

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
        cardToSubscriber = findViewById(R.id.cardToSubscriber);
        cardToNonSubscriber = findViewById(R.id.cardToNonSubscriber);
        cardInternationalIn = findViewById(R.id.cardInternationalIn);
        cardBankToWallet = findViewById(R.id.cardBankToWallet);
        cardWalletToBank = findViewById(R.id.cardWalletToBank);
        cardToreceiveinternational=findViewById(R.id.cardToreceiveinternational);

        cardBankToWallet.setVisibility(View.GONE);
        cardWalletToBank.setVisibility(View.GONE);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        cardToSubscriber.setOnClickListener(moneytransferC);
        cardToNonSubscriber.setOnClickListener(moneytransferC);
        cardInternationalIn.setOnClickListener(moneytransferC);
        cardBankToWallet.setOnClickListener(moneytransferC);
        cardWalletToBank.setOnClickListener(moneytransferC);
        cardToreceiveinternational.setOnClickListener(moneytransferC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.cardToSubscriber:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(!MyApplication.showToSubscriber){
                    // cardToSubscriber.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else{
                    if(!MyApplication.isConnectingToInternet(MoneyTransfer.this)){
                        Toast.makeText(moneytransferC, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                    }else{
                        intent = new Intent(moneytransferC, ToSubscriber.class);
                        startActivity(intent);
                    }

                }
                break;

            case R.id.cardToreceiveinternational:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(!MyApplication.showToSubscriber){
                    // cardToSubscriber.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else{
                    intent = new Intent(moneytransferC, InTransfer.class);
                    startActivity(intent);
                }
                break;
            case R.id.cardToNonSubscriber:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(!MyApplication.showToNonSubscriber){
                    //cardToNonSubscriber.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else{
                    intent = new Intent(moneytransferC, ToNonSubscriber.class);
                    startActivity(intent);
                }
                break;
            case R.id.cardInternationalIn:


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
               if(!MyApplication.showInternationalRemit){
                    //cardInternational.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else {
                   showDialoginternational();
                }
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

    private void showDialoginternational(){
        final Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_money_transfer_international);


      CardView  mCardCashmoov = dialog.findViewById(R.id.cardCashmoov);
        CardView mCardToPartners=dialog.findViewById(R.id.cardToPartners);
        mCardCashmoov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(!MyApplication.showToSubscriber){
                    // cardToSubscriber.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else{
                    dialog.dismiss();

                    Intent intent = new Intent(moneytransferC, International.class);
                    startActivity(intent);
                }
            }
        });

        mCardToPartners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(!MyApplication.showInternationalRemit){
                    //cardInternational.setVisibility(View.VISIBLE);
                    MyApplication.showToast(moneytransferC,getString(R.string.service_not_available));
                }else {
                    dialog.dismiss();
                    Intent intent = new Intent(moneytransferC, OutTransfer.class);
                    startActivity(intent);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

}
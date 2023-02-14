package com.estel.cashmoovsubscriberapp.activity.fee;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;

public class MoneyTransferFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static MoneyTransferFeeActivity moneytransferfeeC;
    ImageView imgBack,imgHome;
    CardView cardSubscriber,cardNonSubscriber,cardInternational;
    TextView tvServiceName;
    Button btnClose;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_money_transfer_fee);
        moneytransferfeeC=this;
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
//        imgHome = findViewById(R.id.imgHome);
//
//
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }

    private void getIds() {
        cardSubscriber = findViewById(R.id.cardSubscriber);
        cardNonSubscriber = findViewById(R.id.cardNonSubscriber);
        cardInternational = findViewById(R.id.cardInternational);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.money_transfer));
        btnClose = findViewById(R.id.btnClose);

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardSubscriber.setOnClickListener(moneytransferfeeC);
        cardNonSubscriber.setOnClickListener(moneytransferfeeC);
        cardInternational.setOnClickListener(moneytransferfeeC);
        btnClose.setOnClickListener(moneytransferfeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardSubscriber:


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.subscriber));
                startActivity(intent);
                break;
            case R.id.cardNonSubscriber:


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.non_subscriber));
                startActivity(intent);
                break;
            case R.id.cardInternational:


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                    intent = new Intent(moneytransferfeeC, InternationalTransferFeeActivity.class);
                    startActivity(intent);
                    // showFeePopup(getString(R.string.money_transfer));

              /*  intent = new Intent(moneytransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.international));
                startActivity(intent);*/
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }



}

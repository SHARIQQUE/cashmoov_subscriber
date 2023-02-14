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

import com.estel.cashmoovsubscriberapp.R;

public class InternationalTransferFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static InternationalTransferFeeActivity intertransferfeeC;
    ImageView imgBack,imgHome;
    CardView cardinternationalremitance,careinternationalremitanceinn,caredinternationalout;
    TextView tvServiceName;
    Button btnClose;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_international_transfer_in);
        intertransferfeeC=this;
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
        cardinternationalremitance = findViewById(R.id.cardinternationalremitance);
        careinternationalremitanceinn = findViewById(R.id.careinternationalremitanceinn);
        caredinternationalout = findViewById(R.id.caredinternationalout);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.money_transfer));
        btnClose = findViewById(R.id.btnClose);

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardinternationalremitance.setOnClickListener(intertransferfeeC);
        careinternationalremitanceinn.setOnClickListener(intertransferfeeC);
        caredinternationalout.setOnClickListener(intertransferfeeC);
        btnClose.setOnClickListener(intertransferfeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardinternationalremitance:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(intertransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.international_remmitance_setting));
                startActivity(intent);
                break;
            case R.id.careinternationalremitanceinn:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(intertransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.international_transfer_in));
                startActivity(intent);
                break;
            case R.id.caredinternationalout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                    intent = new Intent(intertransferfeeC, FeeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.international_transfer_out));

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

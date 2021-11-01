package com.estel.cashmoovsubscriberapp.activity.fee;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.R;

public class Fee extends AppCompatActivity implements View.OnClickListener {
    public static Fee feeC;
    ImageView imgBack,imgHome;
    LinearLayout linMoneyTransfer,linAirtimePurhase,linBillPay,linPay,
            linCashWithdrawal,linRecRemittance;
   // TextView tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        feeC=this;
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
        linMoneyTransfer = findViewById(R.id.linMoneyTransfer);
        linAirtimePurhase = findViewById(R.id.linAirtimePurhase);
        linBillPay = findViewById(R.id.linBillPay);
        linPay = findViewById(R.id.linPay);
        linCashWithdrawal = findViewById(R.id.linCashWithdrawal);
        linRecRemittance = findViewById(R.id.linRecRemittance);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        linMoneyTransfer.setOnClickListener(feeC);
        linAirtimePurhase.setOnClickListener(feeC);
        linBillPay.setOnClickListener(feeC);
        linPay.setOnClickListener(feeC);
        linCashWithdrawal.setOnClickListener(feeC);
        linRecRemittance.setOnClickListener(feeC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linMoneyTransfer:
                showFeePopup(getString(R.string.money_transfer));
                break;
            case R.id.linAirtimePurhase:
                showFeePopup(getString(R.string.airtime_purchase));
                break;
            case R.id.linBillPay:
                showFeePopup(getString(R.string.bill_payment));
                break;
            case R.id.linPay:
                showFeePopup(getString(R.string.pay));
                break;
            case R.id.linCashWithdrawal:
                showFeePopup(getString(R.string.cash_withdrawal));
                break;
            case R.id.linRecRemittance:
                showFeePopup(getString(R.string.receive_remittance));
                break;

        }
    }

    public void showFeePopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_money_transfer);

        Button btnClose;
        TextView tvServiceName;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

}
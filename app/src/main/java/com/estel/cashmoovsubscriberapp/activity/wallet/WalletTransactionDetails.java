package com.estel.cashmoovsubscriberapp.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.model.Taxmodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WalletTransactionDetails extends AppCompatActivity {
    public static WalletTransactionDetails wallettransdetailsC;
    ImageView imgBack,imgHome;
    TextView taxText,postbalance_value,txt_trans_type_name,txt_from_owner_name,tax_value,fee_value,txt_from_amount,txt_trans_id,txt_creation_date,txt_status,txt_success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transaction_details);
        wallettransdetailsC=this;
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
                MyApplication.hideKeyboard(wallettransdetailsC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(wallettransdetailsC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        txt_trans_type_name = findViewById(R.id.txt_trans_type_name);
        txt_from_owner_name = findViewById(R.id.txt_from_owner_name);
        txt_from_amount = findViewById(R.id.txt_from_amount);
        txt_trans_id = findViewById(R.id.txt_trans_id);
        txt_creation_date = findViewById(R.id.txt_creation_date);
        txt_status = findViewById(R.id.txt_status);
        txt_success = findViewById(R.id.txt_success);
        tax_value=findViewById(R.id.tax_value);
        fee_value=findViewById(R.id.fee_value);
        postbalance_value=findViewById(R.id.postbalance_value);



        Bundle b = getIntent().getExtras();

        if (getIntent().getExtras() != null) {
            String transType = (getIntent().getStringExtra("TRANSTYPE"));
            String fromOwnerName = (getIntent().getStringExtra("FROMWALLETOWNERNAME"));
           // String fromAmount = (getIntent().getStringExtra("FROMAMOUNT"));
            String fromCurr = (getIntent().getStringExtra("FROMCURR"));
            String transId = (getIntent().getStringExtra("TRANSID"));
            String creationDate = (getIntent().getStringExtra("CREATIONDATE"));
            String status = (getIntent().getStringExtra("STATUS"));
            String tax = (getIntent().getStringExtra("taxvalue"));
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
            DecimalFormat df = new DecimalFormat("0.00",symbols);
            double fromAmount =  b.getDouble("FROMAMOUNT");
            double result = b.getDouble("newamount");
            double postbalanmce = b.getDouble("postbalance");
            double fee = b.getDouble("fee");

            txt_trans_type_name.setText(getString(R.string.transaction_type)+" - "+transType);
            txt_from_owner_name.setText(fromOwnerName);
            txt_from_amount.setText(getString(R.string.trans_amount_colon)+" "+fromCurr+" "+MyApplication.addDecimal(""+fromAmount));
            txt_trans_id.setText(getString(R.string.transaction_id_colon)+" "+transId);
            txt_status.setText(getString(R.string.status)+" : "+status);
            txt_success.setText(getString(R.string.transaction_successful));
            fee_value.setText(getString(R.string.fee_colon)+"" +MyApplication.addDecimal(fee+""));
            postbalance_value.setText(getString(R.string.post_balance_colon) +MyApplication.addDecimal( ""+postbalanmce));

            try {

                if (tax.equalsIgnoreCase("")){
                    tax_value.setText(MyApplication.getTaxString("TAX") + " :" + " " + MyApplication.addDecimal("0.00"));
                }else {
                    Gson gson = new Gson();

                    Type userListType = new TypeToken<ArrayList<Taxmodel>>() {
                    }.getType();

                    ArrayList<Taxmodel> userArray = gson.fromJson(tax, userListType);
                    for (Taxmodel user : userArray) {
                        tax_value.setText(MyApplication.getTaxString(user.getTaxTypeName()) + " :" + " " + MyApplication.addDecimal(user.getValue()));

                        System.out.println("get user" + user.getTaxTypeName());
                    }
                }

            } catch (Exception e) {

            }




            // taxText.setText(tax);
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                Date date = null;
                date = inputFormat.parse(creationDate);
                String formattedDate = outputFormat.format(date);
                txt_creation_date.setText(getString(R.string.date)+" : "+formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }


}
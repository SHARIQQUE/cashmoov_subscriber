package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;

public class AddBeneficiary extends AppCompatActivity implements View.OnClickListener {

    Cursor cursor;
    public static AddBeneficiary addbeneficiaryC;
    ImageView imgBackBeneficiary;
    LinearLayout linNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beneficiary);
        addbeneficiaryC=this;
        setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBackBeneficiary = findViewById(R.id.imgBackBeneficiary);
        imgBackBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }


    private void getIds() {
        linNext = findViewById(R.id.linNext);

        linNext.setOnClickListener(addbeneficiaryC);


    }



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linNext:
                intent = new Intent(addbeneficiaryC, BeneficiaryAirtime.class);
                startActivity(intent);
                break;

        }
    }


}
package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import java.util.StringTokenizer;


public class AirtimePurchase extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    public static AirtimePurchase airtimepurchaseC;
    ImageView imgBack,imgHome;
    LinearLayout tvMyNo,tvOtherNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_purchase);
        airtimepurchaseC=this;
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
                MyApplication.hideKeyboard(airtimepurchaseC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(airtimepurchaseC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void getIds() {
        tvMyNo = findViewById(R.id.tvMyNo);
        tvOtherNo = findViewById(R.id.tvOtherNo);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvMyNo.setOnClickListener(airtimepurchaseC);
        tvOtherNo.setOnClickListener(airtimepurchaseC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.tvMyNo:
                intent = new Intent(airtimepurchaseC, SelfAirtime.class);
                startActivity(intent);
                break;

            case R.id.tvOtherNo:

                //getContactList();
//                Uri uri = Uri.parse("content://contacts");
//                intent = new Intent(Intent.ACTION_PICK, uri);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//                startActivityForResult(intent, REQUEST_CODE);

                intent = new Intent(airtimepurchaseC, AddBeneficiary.class);
                startActivity(intent);
                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                Intent i = new Intent(airtimepurchaseC, BeneficiaryAirtime.class);
                i.putExtra("PHONE",number);
                startActivity(i);

            }
        }
    }


}
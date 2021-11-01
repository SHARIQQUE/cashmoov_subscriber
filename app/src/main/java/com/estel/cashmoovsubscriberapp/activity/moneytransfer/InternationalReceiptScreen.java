package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import java.io.File;
import java.io.FileOutputStream;

public class InternationalReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static InternationalReceiptScreen internationalreceiptscreenC;
    Button btnShareReceipt;
    TextView tvSubscriberMobile,tvProvider,tvTransType,tvMobile,tvName,tvTransId,tvCurrency,tvFee,tvTransAmount,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout tax1_layout,tax2_layout;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_screen);
        internationalreceiptscreenC=this;
        rootView = getWindow().getDecorView().findViewById(R.id.lin);

        getIds();
    }
    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void store(Bitmap bm, String fileName){
        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shareImage(file);
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


    private void getIds() {
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        tvSubscriberMobile = findViewById(R.id.tvSubscriberMobile);
        tvProvider = findViewById(R.id.tvProvider);
        tvTransType = findViewById(R.id.tvTransType);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
        tvTransId = findViewById(R.id.tvTransId);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvFee = findViewById(R.id.tvFee);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);

        tax1_layout = findViewById(R.id.tax1_layout);
        tax2_layout = findViewById(R.id.tax2_layout);
        tax1_lable = findViewById(R.id.tax1_lable);
        tax1_value = findViewById(R.id.tax1_value);
        tax2_lable = findViewById(R.id.tax2_lable);
        tax2_value = findViewById(R.id.tax2_value);

        tvSubscriberMobile.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("mobileNumber"));
        tvProvider.setText(International.serviceProvider);
        tvTransType.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionType"));
        tvMobile.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("mobileNumber"));
        tvName.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("firstName")+" "+
                InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("lastName"));
        tvTransId.setText(InternationalConfirmScreen.receiptJson.optString("transactionId"));
        tvCurrency.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencyName"));
        tvFee.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "
                + MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optInt("fee"))));

        tvTransAmount.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("desCurrencySymbol")+" "+ MyApplication.addDecimal(InternationalConfirmScreen.tvTransAmount.getText().toString()));
        tvAmountPaid.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("desCurrencySymbol")+" "+MyApplication.addDecimal(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("amountToPaid")));
        tvAmountCharged.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("amount")));


        if(InternationalConfirmScreen.taxConfigList!=null){
            if(InternationalConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"));
                tax1_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(InternationalConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"));
                tax1_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(InternationalConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName"));
                tax2_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(InternationalConfirmScreen.taxConfigList.optJSONObject(1).optString("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnShareReceipt.setOnClickListener(internationalreceiptscreenC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                Bitmap bitmap=getScreenShot(rootView);
                store(bitmap,"test.jpg");
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}

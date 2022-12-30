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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class InternationalReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static InternationalReceiptScreen internationalreceiptscreenC;
    Button btnClose,btnShareReceipt;
    TextView amountpaidinternational,tvSubscriberMobile,tvrate,tvConfCode,tvProvider,tvTransType,tvMobile,tvName,tvTransId,tvCurrency,tvFee,tvTransAmt,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout amounttobepadLinear,linConfCode,tax1_layout,tax2_layout;
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

    public  void createImageFile(Bitmap bm)  {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = File.createTempFile(
                    timeStamp,
                    ".jpeg",
                    storageDir
            );

            System.out.println(image.getAbsolutePath());
            if (image.exists()) image.delete();
            //   Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(image);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

        }
    }

//    public  void store(Bitmap bm, String fileName){
//        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        shareImage(file);
//    }

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
        btnClose = findViewById(R.id.btnClose);
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        tvSubscriberMobile = findViewById(R.id.tvSubscriberMobile);
        linConfCode = findViewById(R.id.linConfCode);
        amounttobepadLinear=findViewById(R.id.amounttobepadLinear);
        amounttobepadLinear.setVisibility(View.VISIBLE);
        tvConfCode = findViewById(R.id.tvConfCode);
        tvProvider = findViewById(R.id.tvProvider);
        tvTransType = findViewById(R.id.tvTransType);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
        tvTransId = findViewById(R.id.tvTransId);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvFee = findViewById(R.id.tvFee);
        tvTransAmt = findViewById(R.id.tvTransAmt);
        tvrate=findViewById(R.id.tvrate);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        amountpaidinternational=findViewById(R.id.amountpaidinternational);
        tax1_layout = findViewById(R.id.tax1_layout);
        tax2_layout = findViewById(R.id.tax2_layout);
        tax1_lable = findViewById(R.id.tax1_lable);
        tax1_value = findViewById(R.id.tax1_value);
        tax2_lable = findViewById(R.id.tax2_lable);
        tax2_value = findViewById(R.id.tax2_value);

        linConfCode.setVisibility(View.VISIBLE);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        tvSubscriberMobile.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("mobileNumber"));
        tvConfCode.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("confirmationCode"));
        tvProvider.setText(International.serviceProvider);
        tvTransType.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionType"));
        tvMobile.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("mobileNumber"));
        tvName.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("firstName")+" "+
                InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("lastName"));
        tvTransId.setText(InternationalConfirmScreen.receiptJson.optString("transactionId"));
        tvCurrency.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencyName"));
        tvFee.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "
                + MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("fee"))));

        tvTransAmt.setText((InternationalConfirmScreen.tvTransAmounts.getText().toString()));

        amountpaidinternational.setVisibility(View.GONE);


        tvAmountPaid.setText(getString(R.string.amount_to_paid)+ " : " +"  "+ InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencySymbol")+" "+MyApplication.addDecimal(""+InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amountToPaid")));
        tvAmountCharged.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amount"))));
        tvrate.setText(MyApplication.addDecimalfive(International.rate));

        if(InternationalConfirmScreen.taxConfigList!=null){
            if(InternationalConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax1_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value"))));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(InternationalConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax1_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value"))));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(MyApplication.getTaxString(InternationalConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName"))+" ");
                tax2_value.setText(InternationalConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+MyApplication.addDecimal(String.valueOf(InternationalConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value"))));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(internationalreceiptscreenC);
        btnShareReceipt.setOnClickListener(internationalreceiptscreenC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                btnShareReceipt.setVisibility(View.VISIBLE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
               // store(bitmap,"test.jpg");
                break;
            case R.id.btnClose:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        MyApplication.isFirstTime=false;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}

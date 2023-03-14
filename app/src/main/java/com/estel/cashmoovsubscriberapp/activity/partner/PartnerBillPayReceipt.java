package com.estel.cashmoovsubscriberapp.activity.partner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPay;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayConfirmScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PartnerBillPayReceipt extends LogoutAppCompactActivity implements View.OnClickListener {
    public static PartnerBillPayReceipt billpayreceiptC;
    Button btnClose,btnShareReceipt;
    TextView transId,tvSubscriberMobile,tvProvider,tvTransType,tvMobile,tvName,tvOperatorName,tvTransIdnew,tvTransId,tvCurrency,tvFee,tvTransAmount,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout tax1_layout,tax2_layout;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay_receipt);
        billpayreceiptC=this;
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

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


    private void getIds() {
        transId = findViewById(R.id.transId);
        btnClose = findViewById(R.id.btnClose);
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        tvSubscriberMobile = findViewById(R.id.tvSubscriberMobile);
        tvTransType = findViewById(R.id.tvTransType);
        tvMobile = findViewById(R.id.tvMobile);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        tvTransId = findViewById(R.id.tvTransId);
        tvTransIdnew=findViewById(R.id.tvTransIdnew);
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

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);

        tvSubscriberMobile.setText(Partner.mobile);
        tvTransType.setText(getString(R.string.paymentnew));
        transId.setText(getString(R.string.vendor_trans_id_colon));
        tvMobile.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("accountNumber"));

        tvOperatorName.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("operator"));
        tvTransIdnew.setText(BillPayConfirmScreen.receiptJson.optString("transactionId"));

        tvTransId.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("vendorTransId"));
        tvFee.setText(Partner.currencySymbol+" "
                + MyApplication.addDecimal(""+PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("fee")));

//        tvTransAmount.setText(Partner.currencySymbol+" "+ MyApplication.addDecimal(PartnerBillPayConfirmScreen.tvTransAmount.getText().toString()));
//        tvAmountPaid.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("remittance").optString("amountToPaid")));
//        tvAmountCharged.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("remittance").optString("amount")));

        tvTransAmount.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(""+PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("amount")));
        tvAmountCharged.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(""+PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("totalAmount")));


        if(PartnerBillPayConfirmScreen.taxConfigList!=null){
            if(PartnerBillPayConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")));
                tax1_value.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value"))));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(PartnerBillPayConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")));
                tax1_value.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value"))));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(MyApplication.getTaxString(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName")));
                tax2_value.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value"))));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(billpayreceiptC);
        btnShareReceipt.setOnClickListener(billpayreceiptC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                btnShareReceipt.setVisibility(View.VISIBLE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
                break;
            case R.id.btnClose:
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
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

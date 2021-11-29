package com.estel.cashmoovsubscriberapp.activity.partner;

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
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPay;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayConfirmScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class PartnerBillPayReceipt extends AppCompatActivity implements View.OnClickListener {
    public static PartnerBillPayReceipt billpayreceiptC;
    Button btnShareReceipt;
    TextView transId,tvSubscriberMobile,tvProvider,tvTransType,tvMobile,tvName,tvOperatorName,tvTransId,tvCurrency,tvFee,tvTransAmount,tvAmountPaid,tvAmountCharged,
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
            btnShareReceipt.setVisibility(View.GONE);
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


    private void getIds() {
        transId = findViewById(R.id.transId);
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        tvSubscriberMobile = findViewById(R.id.tvSubscriberMobile);
        tvTransType = findViewById(R.id.tvTransType);
        tvMobile = findViewById(R.id.tvMobile);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        tvTransId = findViewById(R.id.tvTransId);
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

        DecimalFormat df = new DecimalFormat("0.000");
        tvSubscriberMobile.setText(Partner.mobile);
        tvTransType.setText(getString(R.string.airtime_purchase));
        transId.setText(getString(R.string.vendor_trans_id_colom));
        tvMobile.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("accountNumber"));

        tvOperatorName.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("operator"));
        tvTransId.setText(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optString("vendorTransId"));
        tvFee.setText(Partner.currencySymbol+" "
                + df.format(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("fee")));

//        tvTransAmount.setText(Partner.currencySymbol+" "+ MyApplication.addDecimal(PartnerBillPayConfirmScreen.tvTransAmount.getText().toString()));
//        tvAmountPaid.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("remittance").optString("amountToPaid")));
//        tvAmountCharged.setText(Partner.currencySymbol+" "+MyApplication.addDecimal(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("remittance").optString("amount")));

        tvTransAmount.setText(Partner.currencySymbol+" "+df.format(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("amount")));
        tvAmountCharged.setText(Partner.currencySymbol+" "+df.format(PartnerBillPayConfirmScreen.receiptJson.optJSONObject("recharge").optDouble("totalAmount")));


        if(PartnerBillPayConfirmScreen.taxConfigList!=null){
            if(PartnerBillPayConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(Partner.currencySymbol+" "+df.format(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(PartnerBillPayConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(Partner.currencySymbol+" "+df.format(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName")+" :");
                tax2_value.setText(Partner.currencySymbol+" "+df.format(PartnerBillPayConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnShareReceipt.setOnClickListener(billpayreceiptC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
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

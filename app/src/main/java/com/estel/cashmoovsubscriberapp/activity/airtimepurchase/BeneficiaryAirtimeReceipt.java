package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

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
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPay;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayConfirmScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class BeneficiaryAirtimeReceipt extends AppCompatActivity implements View.OnClickListener {
    public static BeneficiaryAirtimeReceipt benefiairtimereceiptC;
    Button btnClose,btnShareReceipt;
    TextView accNo,transId,tvSubscriberMobile,tvProvider,tvTransType,tvMobile,tvName,tvOperatorName,tvTransId,tvCurrency,tvFee,tvTransAmount,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout tax1_layout,tax2_layout;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay_receipt);
        benefiairtimereceiptC=this;
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
        accNo = findViewById(R.id.accNo);
        transId = findViewById(R.id.transId);
        btnClose = findViewById(R.id.btnClose);
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
        tvSubscriberMobile.setText(BeneficiaryAirtime.mobile);
        tvTransType.setText(getString(R.string.airtime_purchase));
        accNo.setText(getString(R.string.mobile_number_colom));
        transId.setText(getString(R.string.vendor_trans_id_colon));
        tvMobile.setText(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optString("accountNumber"));
        tvOperatorName.setText(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optString("operator"));
        tvTransId.setText(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optString("vendorTransId"));
        tvFee.setText(BeneficiaryAirtime.currencySymbol+" "
                + df.format(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optDouble("fee")));

//        tvTransAmount.setText(BeneficiaryAirtime.currencySymbol+" "+ MyApplication.addDecimal(BeneficiaryAirtimeConfirm.tvTransAmount.getText().toString()));
//        tvAmountPaid.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("remittance").optString("amountToPaid")));
//        tvAmountCharged.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("remittance").optString("amount")));

        tvTransAmount.setText(BeneficiaryAirtime.currencySymbol+" "+df.format(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optDouble("amount")));
        tvAmountCharged.setText(BeneficiaryAirtime.currencySymbol+" "+df.format(BeneficiaryAirtimeConfirm.receiptJson.optJSONObject("recharge").optDouble("totalAmount")));

        if(BeneficiaryAirtimeConfirm.taxConfigList!=null){
            if(BeneficiaryAirtimeConfirm.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(BeneficiaryAirtime.currencySymbol+" "+df.format(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(0).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(BeneficiaryAirtimeConfirm.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(BeneficiaryAirtime.currencySymbol+" "+df.format(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(0).optDouble("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(1).optString("taxTypeName")+" :");
                tax2_value.setText(BeneficiaryAirtime.currencySymbol+" "+df.format(BeneficiaryAirtimeConfirm.taxConfigList.optJSONObject(1).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(benefiairtimereceiptC);
        btnShareReceipt.setOnClickListener(benefiairtimereceiptC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                btnShareReceipt.setVisibility(View.GONE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
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

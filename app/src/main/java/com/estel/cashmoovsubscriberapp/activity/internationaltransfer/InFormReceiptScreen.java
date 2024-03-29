package com.estel.cashmoovsubscriberapp.activity.internationaltransfer;

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
import java.text.SimpleDateFormat;

public class InFormReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static InFormReceiptScreen tosubscriberreceiptscreenC;
    Button btnClose,btnShareReceipt;
    TextView tvSubscriberMobile,tvProvider,tvTransType,tvMobile,tvName,tvTransId,tvCurrency,tvFee,tvTransAmt,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout tax1_layout,tax2_layout;
    double finalamount;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_screen);
        tosubscriberreceiptscreenC=this;
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
//          String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
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
        btnClose = findViewById(R.id.btnClose);
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        tvSubscriberMobile = findViewById(R.id.tvSubscriberMobile);
        tvProvider = findViewById(R.id.tvProvider);
        tvTransType = findViewById(R.id.tvTransType);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
        tvTransId = findViewById(R.id.tvTransId);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvFee = findViewById(R.id.tvFee);
        tvTransAmt = findViewById(R.id.tvTransAmt);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);

        tax1_layout = findViewById(R.id.tax1_layout);
        tax2_layout = findViewById(R.id.tax2_layout);
        tax1_lable = findViewById(R.id.tax1_lable);
        tax1_value = findViewById(R.id.tax1_value);
        tax2_lable = findViewById(R.id.tax2_lable);
        tax2_value = findViewById(R.id.tax2_value);

        DecimalFormat df = new DecimalFormat("0.000");
        tvSubscriberMobile.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("mobileNumber"));
        tvProvider.setText(InTransfer.operatorNname);
        tvTransType.setText("In Transfer");
        //tvTransType.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("transactionType"));
        tvMobile.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("mobileNumber"));
        tvName.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("firstName")+" "+
                InFormConfirmation.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("lastName"));
        tvTransId.setText(InFormConfirmation.receiptJson.optString("transactionId"));
        tvCurrency.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("toCurrencyName"));
        tvFee.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "
                + df.format(InFormConfirmation.receiptJson.optJSONObject("remittance").optDouble("fee")));

        tvTransAmt.setText(MyApplication.addDecimal(InFormConfirmation.tvTransAmount.getText().toString()));
        tvAmountPaid.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("toCurrencySymbol")+" "
                +df.format(InFormConfirmation.receiptJson.optJSONObject("remittance").optDouble("amountToPaid")));
        tvAmountCharged.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")
                +" "+df.format(InFormConfirmation.receiptJson.optJSONObject("remittance").optDouble("amount")));

        if(InFormConfirmation.taxConfigList!=null){
            if(InFormConfirmation.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InFormConfirmation.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "
                        +df.format(InFormConfirmation.taxConfigList.optJSONObject(0).optDouble("value")));
               // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(InFormConfirmation.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InFormConfirmation.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InFormConfirmation.taxConfigList.optJSONObject(0).optDouble("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(InFormConfirmation.taxConfigList.optJSONObject(1).optString("taxTypeName")+" :");
                tax2_value.setText(InFormConfirmation.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InFormConfirmation.taxConfigList.optJSONObject(1).optDouble("value")));
               // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }

        //trans_amt_r.setText(ToSubscriber.currencySymbol+" "+MyApplication.addDecimal(Double.toString(finalamount)));

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(tosubscriberreceiptscreenC);
        btnShareReceipt.setOnClickListener(tosubscriberreceiptscreenC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareReceipt:
                btnShareReceipt.setVisibility(View.GONE);
                Bitmap bitmap=getScreenShot(rootView);
                //int id= Random(System.currentTimeMillis()).nextInt(1000);
                createImageFile(bitmap);
                //store(bitmap,id+"test.jpg");
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

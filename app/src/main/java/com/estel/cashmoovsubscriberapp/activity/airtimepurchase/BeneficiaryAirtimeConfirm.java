package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.HiddenPassTransformationMethod;
import com.estel.cashmoovsubscriberapp.activity.cashwithdrawal.CashWithdrawal;
import com.estel.cashmoovsubscriberapp.activity.login.AESEncryption;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.TransactionSuccessScreen;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayConfirmScreen;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayDetails;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.apiCalls.BioMetric_Responce_Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BeneficiaryAirtimeConfirm extends AppCompatActivity implements View.OnClickListener {
    public static BeneficiaryAirtimeConfirm benefiairtimeconfirmC;
    // ImageView imgBack;
    Button btnConfirm,btnCancel;
    public static TextView tvProvider,tvMobileTxt,tvAccNo,tvOperatorName,tvCurrency,tvTransAmount,tvAmountPaid,tvAmountCharged,tvFee,tax_label,tax_r,vat_label,vat_r;
    EditText etPin;
    double finalamount;
    LinearLayout tax_label_layout,vat_label_layout,pinLinear;
    CardView cardBearFee;
    ImageView icPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay_confirm);
        benefiairtimeconfirmC=this;
        //setBackMenu();
        getIds();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }

    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    private void getIds() {
        tvProvider = findViewById(R.id.tvProvider);
        tvMobileTxt = findViewById(R.id.tvMobileTxt);
        tvAccNo = findViewById(R.id.tvAccNo);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvFee = findViewById(R.id.tvFee);
        etPin = findViewById(R.id.etPin);
        pinLinear=findViewById(R.id.pinLinear);
        etPin.setTransformationMethod(hiddenPassTransformationMethod);
        icPin = findViewById(R.id.icPin);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        tax_r=findViewById(R.id.tax_r);
        vat_r=findViewById(R.id.vat_r);
        tax_label=findViewById(R.id.tax_label);
        vat_label=findViewById(R.id.vat_label);
        tax_label_layout=findViewById(R.id.tax_label_layout);
        vat_label_layout=findViewById(R.id.vat_label_layout);

        // tvProvider.setText(BeneficiaryAirtime.serviceProvider);
        tvMobileTxt.setText(getString(R.string.mobile_number_colom));
        tvAccNo.setText(BeneficiaryAirtime.etPhone.getText().toString());
        tvOperatorName.setText(BeneficiaryAirtime.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("name"));
        // tvCurrency.setText(BeneficiaryAirtime.currency);

        tvTransAmount.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(BeneficiaryAirtime.etAmount.getText().toString().replace(",","")));
        tvAmountPaid.setText(BeneficiaryAirtime.currencySymbol+" "+ MyApplication.addDecimal(BeneficiaryAirtime.currencyValue));
        tvFee.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(BeneficiaryAirtime.fee));

        finalamount=Double.parseDouble(BeneficiaryAirtime.fee)+Double.parseDouble(BeneficiaryAirtime.etAmount.getText().toString().replace(",",""));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        if(BeneficiaryAirtime.taxConfigurationList!=null){
            if(BeneficiaryAirtime.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax_r.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optDouble("value"))));
                finalamount=Double.parseDouble(BeneficiaryAirtime.fee)+Double.parseDouble(BeneficiaryAirtime.etAmount.getText().toString().replace(",",""))+Double.parseDouble(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(BeneficiaryAirtime.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax_r.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optDouble("value"))));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxString(BeneficiaryAirtime.taxConfigurationList.optJSONObject(1).optString("taxTypeName"))+" ");
                vat_r.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(BeneficiaryAirtime.taxConfigurationList.optJSONObject(1).optDouble("value"))));
                finalamount=Double.parseDouble(BeneficiaryAirtime.fee)+Double.parseDouble(BeneficiaryAirtime.etAmount.getText().toString().replace(",",""))+Double.parseDouble(BeneficiaryAirtime.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(BeneficiaryAirtime.taxConfigurationList.optJSONObject(1).optString("value"));
            }
        }

        tvAmountCharged.setText(BeneficiaryAirtime.currencySymbol+" "+MyApplication.addDecimal(String.valueOf(finalamount)));

        etPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 4)
                    MyApplication.hideKeyboard(benefiairtimeconfirmC);            }
        });

        TextView tvFinger =findViewById(R.id.tvFinger);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                // tvFinger.setVisibility(View.VISIBLE);
            } else {
                //  tvFinger.setVisibility(View.GONE);
            }
        }else{
            // tvFinger.setVisibility(View.VISIBLE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.biometricAuth(benefiairtimeconfirmC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            BeneficiaryAirtime.dataToSend.put( "pin",encryptionDatanew);

                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(benefiairtimeconfirmC,failure);
                    }
                });
            }
        });


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        icPin.setOnClickListener(benefiairtimeconfirmC);
        btnConfirm.setOnClickListener(benefiairtimeconfirmC);
        btnCancel.setOnClickListener(benefiairtimeconfirmC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.icPin:
                if(etPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){
                    icPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    icPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                break;
            case R.id.btnConfirm: {


                if (pinLinear.getVisibility() == View.VISIBLE) {
                    if (etPin.getText().toString().trim().isEmpty()) {
                        MyApplication.showErrorToast(BeneficiaryAirtimeConfirm.this, getString(R.string.val_pin));
                        return;
                    }
                    if (etPin.getText().toString().trim().length() < 4) {
                        MyApplication.showErrorToast(BeneficiaryAirtimeConfirm.this, getString(R.string.val_valid_pin));
                        return;
                    }
                    try {
                        pinLinear.setVisibility(View.VISIBLE);
                        etPin.setClickable(false);
                        btnConfirm.setVisibility(View.GONE);

                        String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                        BeneficiaryAirtime.dataToSend.put("pin", encryptionDatanew);

                        callPostAPI();
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                } else {
                    MyApplication.biometricAuth(BeneficiaryAirtimeConfirm.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {
                                etPin.setClickable(false);
                                btnConfirm.setVisibility(View.GONE);
                                String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin", MyApplication.appInstance).toString().trim());
                                BeneficiaryAirtime.dataToSend.put("pin", encryptionDatanew);

                                callPostAPI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(BeneficiaryAirtimeConfirm.this, failure);
                            pinLinear.setVisibility(View.VISIBLE);

                        }
                    });


                }

              /*  MyApplication.biometricAuth(benefiairtimeconfirmC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            BeneficiaryAirtime.dataToSend.put( "pin",encryptionDatanew);

                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(benefiairtimeconfirmC,failure);
                        pinLinear.setVisibility(View.VISIBLE);
                    }
                });
            }
               /* if(etPin.getText().toString().trim().isEmpty()){
                    MyApplication.showErrorToast(benefiairtimeconfirmC,getString(R.string.val_pin));
                    return;
                }
                if(etPin.getText().toString().trim().length()<4){
                    MyApplication.showErrorToast(benefiairtimeconfirmC,getString(R.string.val_valid_pin));
                    return;
                }
                try {
                    etPin.setClickable(false);
                    btnConfirm.setVisibility(View.GONE);
                    String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                    BeneficiaryAirtime.dataToSend.put( "pin",encryptionDatanew);
                    callPostAPI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("dataToSend---"+BeneficiaryAirtime.dataToSend.toString());*/
            }
            break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){
        MyApplication.showloader(benefiairtimeconfirmC,"Please Wait...");

        System.out.println("Another Number Final Request  "+BeneficiaryAirtime.dataToSend);
        String requestNo=AESEncryption.getAESEncryption(BeneficiaryAirtime.dataToSend.toString());
        JSONObject jsonObjectA=null;
        try{
            jsonObjectA=new JSONObject();
            jsonObjectA.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/recharge/mobile-prepaid", jsonObjectA,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();


                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(benefiairtimeconfirmC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("recharge");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(benefiairtimeconfirmC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","BENEFICIARYAIRTIME");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(benefiairtimeconfirmC,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        etPin.setClickable(true);
                        btnConfirm.setVisibility(View.VISIBLE);
                    }
                });
    }


}

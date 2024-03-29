package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.HiddenPassTransformationMethod;
import com.estel.cashmoovsubscriberapp.activity.cashout.CashOut;
import com.estel.cashmoovsubscriberapp.activity.login.AESEncryption;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.apiCalls.BioMetric_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.AmountDetailsInfoModel;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ToSubscriberConfirmScreen extends AppCompatActivity implements View.OnClickListener {
    public static ToSubscriberConfirmScreen tosubscriberconfirmscreenC;
    // ImageView imgBack;
    Button btnConfirm,btnCancel;
    LinearLayout tax_label_layout,vat_label_layout;
    public static TextView tvProvider,tvMobile,tvName,tvConfCode,tvCurrency,tvTransAmount,tvAmountPaid,tvAmountCharged,tvFee,tax_label,tax_r,vat_label,vat_r;
    EditText etPin;
    double finalamount;
    ImageView icPin;
    SwitchButton switch_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_screen);
        tosubscriberconfirmscreenC=this;
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

    private void getIds() {

        tvProvider = findViewById(R.id.tvProvider);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
       // tvConfCode = findViewById(R.id.tvConfCode);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvFee = findViewById(R.id.tvFee);
        etPin = findViewById(R.id.etPin);
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

        tvProvider.setText(ToSubscriber.serviceProvider);
        tvMobile.setText(ToSubscriber.mobileNo);
        tvName.setText(ToSubscriber.ownerName+" "+ToSubscriber.lastName);
      //  tvConfCode.setText(ToSubscriber.mobileNo);
        tvCurrency.setText(ToSubscriber.currency);
        tvTransAmount.setText(ToSubscriber.currencySymbol+" "+MyApplication.addDecimal(ToSubscriber.etAmount.getText().toString().replace(",","")));
        tvAmountPaid.setText(ToSubscriber.currencySymbol+" "+ToSubscriber.currencyValue);

        tvFee.setText(ToSubscriber.currencySymbol+" "+ToSubscriber.fee);

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
                    MyApplication.hideKeyboard(tosubscriberconfirmscreenC);            }
        });


        TextView tvFinger =findViewById(R.id.tvFinger);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                tvFinger.setVisibility(View.VISIBLE);
            } else {
                tvFinger.setVisibility(View.GONE);
            }
        }else{
            tvFinger.setVisibility(View.VISIBLE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.biometricAuth(tosubscriberconfirmscreenC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            ToSubscriber.dataToSend.put( "pin",encryptionDatanew);
                            if(switch_button.isChecked()) {
                                dataToSendBear.put("pin", encryptionDatanew);
                            }
                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(tosubscriberconfirmscreenC,failure);
                    }
                });
            }
        });


        switch_button=findViewById(R.id.switch_button);
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if(isChecked){
                    callApiAmountDetailsIbear();
                }else{
                    getIds();
                }
            }
        });

        finalamount=Double.parseDouble(ToSubscriber.fee)+Double.parseDouble(ToSubscriber.etAmount.getText().toString().replace(",",""));
        DecimalFormat df = new DecimalFormat("0.000");
        if(ToSubscriber.taxConfigurationList!=null){


            if(ToSubscriber.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("taxTypeName")+" :");
                tax_r.setText(ToSubscriber.currencySymbol+" "+df.format(ToSubscriber.taxConfigurationList.optJSONObject(0).optDouble("value")));
                finalamount=(Double.parseDouble(ToSubscriber.fee)+Double.parseDouble(ToSubscriber.etAmount.getText().toString().replace(",",""))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value")));
            }
            if(ToSubscriber.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("taxTypeName")+" :");
                tax_r.setText(ToSubscriber.currencySymbol+" "+df.format(ToSubscriber.taxConfigurationList.optJSONObject(0).optDouble("value")));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(ToSubscriber.taxConfigurationList.optJSONObject(1).optString("taxTypeName")+" :");
                vat_r.setText(ToSubscriber.currencySymbol+" "+df.format(ToSubscriber.taxConfigurationList.optJSONObject(1).optDouble("value")));
                finalamount=(Double.parseDouble(ToSubscriber.fee)+Double.parseDouble(ToSubscriber.etAmount.getText().toString().replace(",",""))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(1).optString("value")));
            }
        }



        tvAmountCharged.setText(ToSubscriber.currencySymbol+" "+df.format(finalamount));




        setOnCLickListener();

    }

    private void setOnCLickListener() {
        icPin.setOnClickListener(tosubscriberconfirmscreenC);
        btnConfirm.setOnClickListener(tosubscriberconfirmscreenC);
        btnCancel.setOnClickListener(tosubscriberconfirmscreenC);

    }
    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    @Override
    public void onClick(View view) {
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
            case R.id.btnConfirm:
                if(etPin.getText().toString().trim().isEmpty()){
                    MyApplication.showErrorToast(tosubscriberconfirmscreenC,getString(R.string.val_pin));
                    return;
                }
                if(etPin.getText().toString().trim().length()<4){
                    MyApplication.showErrorToast(tosubscriberconfirmscreenC,getString(R.string.val_valid_pin));
                    return;
                }
                try {
                    etPin.setClickable(false);
                    btnConfirm.setVisibility(View.GONE);
                    String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                    ToSubscriber.dataToSend.put( "pin",encryptionDatanew);
                    if(switch_button.isChecked()) {
                        dataToSendBear.put("pin", encryptionDatanew);
                    }
                    callPostAPI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("dataToSend---"+ToSubscriber.dataToSend.toString());

                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){
        MyApplication.showloader(tosubscriberconfirmscreenC,"Please Wait...");
        JSONObject clone=null;
        try {
            if (switch_button.isChecked()) {
                clone = new JSONObject(dataToSendBear.toString());
            }else{
                clone = new JSONObject(ToSubscriber.dataToSend.toString());
            }
        }catch (Exception e){

        }
        System.out.println("Data Send "+clone);

        String requestNo=AESEncryption.getAESEncryption(clone.toString());
        JSONObject jsonObject=null;
        try{
            jsonObject=new JSONObject();
            jsonObject.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/walletTransfer/subscriber/cashOut", jsonObject,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("walletTransfer");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(tosubscriberconfirmscreenC,TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","TOSUB");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription"));
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

    DecimalFormat df = new DecimalFormat("0.000");
    JSONObject dataToSendBear;
    private void callApiAmountDetailsIbear() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");

            // dataToSend.put("transactionType","104591");
            //            dataToSend.put("desWalletOwnerCode",walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"));
            //            dataToSend.put("srcWalletOwnerCode",MyApplication.getSaveString("walletOwnerCode",tosubscriberC));
            //            dataToSend.put("srcCurrencyCode","100062");
            //            dataToSend.put("desCurrencyCode","100062");
            //            dataToSend.put("value",etAmount.getText().toString());
            //            dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
            //            dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
            //            dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
            //            dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
            //http://192.168.1.171:8081/ewallet/api/v1/exchangeRate/getBearerFee?
            // value=100&channelTypeCode=100000&serviceCode=100000&
            // serviceCategoryCode=100024&serviceProviderCode=100112&walletOwnerCode=1000002724
            API.GET("ewallet/api/v1/exchangeRate/getBearerFee?"+
                            "&value="+ToSubscriber.dataToSend.optString("value")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+ToSubscriber.dataToSend.optString("serviceCode")
                            +"&serviceCategoryCode="+ToSubscriber.dataToSend.optString("serviceCategoryCode")+
                            "&serviceProviderCode="+ToSubscriber.dataToSend.optString("serviceProviderCode")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberconfirmscreenC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("ToSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(jsonObject.has("exchangeRate")) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");
                                        String fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        tvFee.setText(ToSubscriber.currencySymbol + " " + ToSubscriber.fee + "+ Bear Fee " + fee);
                                        Double transAmount = jsonObjectAmountDetails.optDouble("fee") + Double.parseDouble(ToSubscriber.etAmount.getText().toString().replace(",",""));
                                        Double paidAmount = jsonObjectAmountDetails.optDouble("fee") + Double.parseDouble(ToSubscriber.currencyValue);
                                        Double chargeAmount = jsonObjectAmountDetails.optDouble("fee") + finalamount;
                                        tvTransAmount.setText(ToSubscriber.currencySymbol + " " + df.format(transAmount));
                                        tvAmountPaid.setText(ToSubscriber.currencySymbol + " " + df.format(paidAmount));

                                        tvAmountCharged.setText(ToSubscriber.currencySymbol + " " + df.format(chargeAmount));


                                        try {
                                            dataToSendBear = new JSONObject();
                                            dataToSendBear.put("transactionType", "104591");
                                            dataToSendBear.put("desWalletOwnerCode", ToSubscriber.dataToSend.optString("desWalletOwnerCode"));
                                            dataToSendBear.put("srcWalletOwnerCode", MyApplication.getSaveString("walletOwnerCode", tosubscriberconfirmscreenC));
                                            dataToSendBear.put("srcCurrencyCode", "100062");
                                            dataToSendBear.put("desCurrencyCode", "100062");
                                            dataToSendBear.put("value", transAmount);
                                            dataToSendBear.put("channelTypeCode", MyApplication.channelTypeCode);
                                            dataToSendBear.put("serviceCode", ToSubscriber.dataToSend.optString("serviceCode"));
                                            dataToSendBear.put("serviceCategoryCode", ToSubscriber.dataToSend.optString("serviceCategoryCode"));
                                            dataToSendBear.put("serviceProviderCode", ToSubscriber.dataToSend.optString("serviceProviderCode"));
                                            dataToSendBear.put("bearerAllow", true);
                                            dataToSendBear.put("bearerFee", fee);

                                            dataToSendBear.put("transactionCoordinate", MainActivity.transactionCoordinate);
                                            dataToSendBear.put("transactionArea", MainActivity.transactionArea);
                                            dataToSendBear.put("isGpsOn", true);

                                        } catch (Exception e) {

                                        }
                                    }else{
                                        MyApplication.showToast(tosubscriberconfirmscreenC,"Template not found");
                                        switch_button.setChecked(false);
                                    }
                                   /* AmountDetailsInfoModel.AmountDetails amountDetails = new AmountDetailsInfoModel.AmountDetails(
                                            jsonObjectAmountDetails.optInt("fee"),
                                            jsonObjectAmountDetails.optInt("receiverFee"),
                                            jsonObjectAmountDetails.optInt("receiverTax"),
                                            jsonObjectAmountDetails.optString("value", "N/A"),
                                            jsonObjectAmountDetails.optString("currencyValue", "N/A")

                                    );

                                    AmountDetailsInfoModel amountDetailsInfoModel = new AmountDetailsInfoModel(
                                            jsonObject.optString("transactionId", "N/A"),
                                            jsonObject.optString("requestTime", "N/A"),
                                            jsonObject.optString("responseTime", "N/A"),
                                            jsonObject.optString("resultCode", "N/A"),
                                            jsonObject.optString("resultDescription", "N/A"),
                                            amountDetails
                                    );

                                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");*/
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }




                                } else {
                                    MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }



}

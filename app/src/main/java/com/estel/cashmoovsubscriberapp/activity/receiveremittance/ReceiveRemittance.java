package com.estel.cashmoovsubscriberapp.activity.receiveremittance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.AESEncryption;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.TransactionSuccessScreen;
import com.estel.cashmoovsubscriberapp.activity.partner.PartnerBillPayConfirmScreen;
import com.estel.cashmoovsubscriberapp.activity.partner.PartnerBillPayDetails;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.apiCalls.BioMetric_Responce_Handler;

import org.json.JSONArray;
import org.json.JSONObject;


public class ReceiveRemittance extends AppCompatActivity implements View.OnClickListener {
    public static ReceiveRemittance receiveremittanceC;
    ImageView imgBack,imgHome;
    TextView spBenefiCurrency,tvSend;
    public static EditText etPhone,etName,etLname,etConfCode,etAmount,etPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_remittance);
        receiveremittanceC=this;
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
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void getIds() {
        spBenefiCurrency = findViewById(R.id.spBenefiCurrency);
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etLname = findViewById(R.id.etLname);
        etConfCode = findViewById(R.id.etConfCode);
        etAmount = findViewById(R.id.etAmount);
        etPin = findViewById(R.id.etPin);
        tvSend = findViewById(R.id.tvSend);

        etPhone.setEnabled(false);
        etName.setEnabled(false);

        callwalletOwnerDetails();



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
                MyApplication.biometricAuth(receiveremittanceC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            etPin.setClickable(false);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            if(etPhone.getText().toString().isEmpty()){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_phone));
                                return;
                            }
                            if(etPhone.getText().toString().trim().length()<9){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.enter_phone_no_val));
                                return;
                            }
                            if(etName.getText().toString().isEmpty()){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_beneficiairy_name));
                                return;
                            }
                            if(etConfCode.getText().toString().isEmpty()){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_confirmation_code));
                                return;
                            }
                            if(spBenefiCurrency.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_select_benifi_curr));
                                return;
                            }
                            if(etAmount.getText().toString().isEmpty()){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_amount));
                                return;
                            }
                            if(etAmount.getText().toString().trim().equals("0")||etAmount.getText().toString().trim().equals(".")||etAmount.getText().toString().trim().equals(".0")||
                                    etAmount.getText().toString().trim().equals("0.")||etAmount.getText().toString().trim().equals("0.0")||etAmount.getText().toString().trim().equals("0.00")){
                                MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_valid_amount));
                                return;
                            }

                            try{
                                dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",getApplicationContext()));
                                dataToSend.put("toCurrencyCode",fromCurrencyCode);
                                dataToSend.put("amount",etAmount.getText().toString());
                                dataToSend.put("confirmationCode",etConfCode.getText().toString());
                                dataToSend.put("firstName",etName.getText().toString());
                                dataToSend.put("lastName",etLname.getText().toString());
                                dataToSend.put("phoneNumber",etPhone.getText().toString());
                                dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                                dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                                dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                               // String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                                dataToSend.put("pin", encryptionDatanew);
                                dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                                dataToSend.put("transactionArea",MainActivity.transactionArea);
                                dataToSend.put("isGpsOn",true);

                                System.out.println("Data Send "+dataToSend.toString());

                                etPin.setClickable(false);
                                tvSend.setVisibility(View.GONE);

                                callPostAPI();

                            }catch (Exception e){

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(receiveremittanceC,failure);
                    }
                });
            }
        });

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(receiveremittanceC);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSend:
                if(etPhone.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_phone));
                    return;
                }
                if(etPhone.getText().toString().trim().length()<9){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.enter_phone_no_val));
                    return;
                }
                if(etName.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_beneficiairy_name));
                    return;
                }
                if(etConfCode.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_confirmation_code));
                    return;
                }
                if(spBenefiCurrency.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_select_benifi_curr));
                    return;
                }
                if(etAmount.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_amount));
                    return;
                }
                if(etAmount.getText().toString().trim().equals("0")||etAmount.getText().toString().trim().equals(".")||etAmount.getText().toString().trim().equals(".0")||
                        etAmount.getText().toString().trim().equals("0.")||etAmount.getText().toString().trim().equals("0.0")||etAmount.getText().toString().trim().equals("0.00")){
                    MyApplication.showErrorToast(receiveremittanceC,getString(R.string.val_valid_amount));
                    return;
                }
                if (etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(receiveremittanceC, getString(R.string.val_pin));
                    return;
                }
                if (etPin.getText().toString().trim().length() < 4) {
                    MyApplication.showErrorToast(receiveremittanceC, getString(R.string.val_valid_pin));
                    return;
                }
                try{
                    dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",getApplicationContext()));
                    dataToSend.put("toCurrencyCode",fromCurrencyCode);
                    dataToSend.put("amount",etAmount.getText().toString());
                    dataToSend.put("confirmationCode",etConfCode.getText().toString());
                    dataToSend.put("firstName",etName.getText().toString());
                    dataToSend.put("lastName",etLname.getText().toString());
                    dataToSend.put("phoneNumber",etPhone.getText().toString());
                    dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                    dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                    dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                    String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                    dataToSend.put("pin", encryptionDatanew);
                    dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                    dataToSend.put("transactionArea",MainActivity.transactionArea);
                    dataToSend.put("isGpsOn",true);

                    System.out.println("Data Send "+dataToSend.toString());

                    etPin.setClickable(false);
                    tvSend.setVisibility(View.GONE);

                    callPostAPI();

                }catch (Exception e){

                }

                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        callserviceCategory();
    }


    public static  JSONObject dataToSend=new JSONObject();
    public static String serviceProvider,mobileNo,ownerName,lastName,currencyValue,rate,exRateCode,confCode,
            currency,fromCurrency,fromCurrencySymbol,currencySymbol,fromCurrencyCode,toCurrencyCode,
            receiveCountryCode,payAgentCode;
    public static int fee,receiverFee,receiverTax;
    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    public void callwalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+ MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    walletOwner=jsonObject;
                    JSONObject data=walletOwner.optJSONObject("walletOwner");
                    etPhone.setText(data.optString("mobileNumber","N/A"));
                    etName.setText(data.optString("ownerName","N/A"));
                    etLname.setText(data.optString("lastName","N/A"));
                    callApiFromCurrency(data.optString("code"));
                }else{
                    MyApplication.showToast(receiveremittanceC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(receiveremittanceC,aFalse);
            }
        });

    }

    private void callApiFromCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    fromCurrencyCode = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencyCode");
                                    fromCurrency = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencyName");
                                    fromCurrencySymbol = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencySymbol");
                                    spBenefiCurrency.setText(fromCurrency);

                                } else {
                                    MyApplication.showToast(receiveremittanceC,jsonObject.optString("resultDescription", "  "));
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

    public void callserviceCategory(){
        API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100019&serviceCategoryCode=CSHPIC&status=Y", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    serviceCategory=jsonObject;
                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                }else{
                    MyApplication.showToast(receiveremittanceC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(receiveremittanceC,aFalse);
            }
        });
    }


    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){
        MyApplication.showloader(receiveremittanceC,"Please Wait...");
        API.POST_REQEST_WH_NEW("ewallet/api/v1/remittance/cashPickUp", dataToSend,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(receiveremittanceC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("remittance");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            tvSend.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(receiveremittanceC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","RECEIVEREMITTANCE");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            tvSend.setVisibility(View.VISIBLE);
                            MyApplication.showToast(receiveremittanceC,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        etPin.setClickable(true);
                        tvSend.setVisibility(View.VISIBLE);
                    }
                });

    }



}
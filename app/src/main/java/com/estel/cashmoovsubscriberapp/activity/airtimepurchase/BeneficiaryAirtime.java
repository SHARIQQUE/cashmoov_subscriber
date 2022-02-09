package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class BeneficiaryAirtime extends AppCompatActivity implements View.OnClickListener {
    public static BeneficiaryAirtime beneficiaryairtimeC;
    ImageView imgBack,imgHome;
    TextView tvAmtCurr,tvSend;
    String phone;
    public static EditText etPhone,etAmount;
    CardView cardOneThousand,cardTwoThousand,cardFiveThousand,cardTenThousand,cardFifteenThousand,cardTwentyThousand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_airtime);
        beneficiaryairtimeC=this;
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
        etPhone = findViewById(R.id.etPhone);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        etAmount = findViewById(R.id.etAmount);
        cardOneThousand = findViewById(R.id.cardOneThousand);
        cardTwoThousand = findViewById(R.id.cardTwoThousand);
        cardFiveThousand = findViewById(R.id.cardFiveThousand);
        cardTenThousand = findViewById(R.id.cardTenThousand);
        cardFifteenThousand = findViewById(R.id.cardFifteenThousand);
        cardTwentyThousand = findViewById(R.id.cardTwentyThousand);
        tvSend = findViewById(R.id.tvSend);

        if (getIntent().getExtras() != null) {
            phone = (getIntent().getStringExtra("PHONE"));
            phone = phone.replaceAll("[^0-9]","");
            etPhone.setText(phone);

        }


        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>=9) {
                    callApiMsisdnPrefix();
                }


            }

        });


        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>=1) {
                    callApiAmountDetails();
                }


            }

        });

        callwalletOwner();
        setOnCLickListener();

    }

    private void setOnCLickListener() {
        cardOneThousand.setOnClickListener(beneficiaryairtimeC);
        cardTwoThousand.setOnClickListener(beneficiaryairtimeC);
        cardFiveThousand.setOnClickListener(beneficiaryairtimeC);
        cardTenThousand.setOnClickListener(beneficiaryairtimeC);
        cardFifteenThousand.setOnClickListener(beneficiaryairtimeC);
        cardTwentyThousand.setOnClickListener(beneficiaryairtimeC);
        tvSend.setOnClickListener(beneficiaryairtimeC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvSend:
                if(etPhone.getText().toString().trim().isEmpty()) {
                        MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                        return;
                }
                if(etPhone.getText().toString().trim().length()<9) {
                    MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                    return;
                }
                if (etAmount.getText().toString().trim().isEmpty()) {
                        MyApplication.showErrorToast(beneficiaryairtimeC, getString(R.string.val_amount));
                        return;
                }
                if (etAmount.getText().toString().trim().equals("0") || etAmount.getText().toString().trim().equals(".") || etAmount.getText().toString().trim().equals(".0") ||
                        etAmount.getText().toString().trim().equals("0.") || etAmount.getText().toString().trim().equals("0.0") || etAmount.getText().toString().trim().equals("0.00")) {
                        MyApplication.showErrorToast(beneficiaryairtimeC, getString(R.string.val_valid_amount));
                        return;
                }
                try {
                        dataToSend.put("accountNumber", etPhone.getText().toString());
                        dataToSend.put("amount", etAmount.getText().toString());
                        dataToSend.put("channel", "SELFCARE");
                        dataToSend.put("fromCurrencyCode", "100062");
                        dataToSend.put("operator", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("code"));
                        dataToSend.put("productCode", productCategory.optJSONArray("productList").optJSONObject(0).optString("code"));
                        dataToSend.put("requestType", "recharge");
                        dataToSend.put("serviceCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode"));
                        dataToSend.put("serviceCategoryCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode"));
                        dataToSend.put("serviceProviderCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode"));

                        dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                        dataToSend.put("transactionArea",MainActivity.transactionArea);
                        dataToSend.put("isGpsOn",true);
                        System.out.println("Data Send " + dataToSend.toString());
                        Intent i = new Intent(beneficiaryairtimeC, BeneficiaryAirtimeConfirm.class);
                        startActivity(i);
                      } catch (Exception e) {

                }
                    break;
                    case R.id.cardOneThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("1000");
                        break;
                    case R.id.cardTwoThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("2000");
                        break;
                    case R.id.cardFiveThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("5000");
                        break;
                    case R.id.cardTenThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("10000");
                        break;
                    case R.id.cardFifteenThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("15000");
                        break;
                    case R.id.cardTwentyThousand:
                        if(etPhone.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                            return;
                        }
                        if(etPhone.getText().toString().trim().length()<9) {
                            MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                            return;
                        }
                        callApiAmountDetails("20000");
                        break;


        }
    }

    public static String serviceProvider,mobile,currency,currencySymbol;
    public static JSONObject serviceCategory = new JSONObject();
    public static JSONObject productCategory = new JSONObject();

    public void callwalletOwner(){
        MyApplication.showloader(beneficiaryairtimeC,"Please Wait...");
        API.GET("ewallet/api/v1/wallet/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if (jsonObject != null) {

                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                        if (jsonObject.has("walletList") && jsonObject.optJSONArray("walletList") != null) {
                            JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                            for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                JSONObject data = walletOwnerListArr.optJSONObject(i);
                                if (data.optString("walletTypeCode").equalsIgnoreCase("100008")) {
                                    mobile = data.optString("walletOwnerMsisdn");
                                    currency = data.optString("currencyName");
                                    currencySymbol = data.optString("currencySymbol");
                                    tvAmtCurr.setText(currencySymbol);
                                    //etPhone.setText(mobile);
                                    callApiMsisdnPrefix();
                                }

                            }


                        }
                    }

                }else{
                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(beneficiaryairtimeC,aFalse);
            }
        });


    }


    private void callApiMsisdnPrefix() {
        try {
            String number = etPhone.getText().toString();
            //String number = "655595950";
            String firstTwodigits = number.substring(0,2);
            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/operator/allByCriteria?msisdnPrefix=224"+firstTwodigits+"&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    tvSend.setVisibility(View.VISIBLE);
                                    serviceCategory = jsonObject;
                                    callApiProductProvider();
                                } else {
                                    tvSend.setVisibility(View.GONE);
                                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiProductProvider() {
        try {

            API.GET("ewallet/api/v1/product/allByCriteria?serviceCategoryCode="+
                            serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode")
                            +"&operatorCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("code"),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    productCategory = jsonObject;
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");


                                } else {
                                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription", "N/A"));
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

    DecimalFormat df = new DecimalFormat("0.000");
    public static  JSONObject dataToSend=new JSONObject();
    public static String currencyValue,fee;
    public static int receiverFee,receiverTax;
    public static JSONArray taxConfigurationList;

    private void callApiAmountDetails() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100062"+
                            "&sendCountryCode="+"100092"
                            +"&receiveCountryCode="+"100092"+
                            "&currencyValue="+etAmount.getText().toString()+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", beneficiaryairtimeC)+
                            "&productCode=+"+productCategory.optJSONArray("productList").optJSONObject(0).optString("code"),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("BenefiAirtime response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee= df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    tvSend.setVisibility(View.VISIBLE);
                                    //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    //  receiverTax = jsonObjectAmountDetails.optInt("receiverTax");

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }


                                } else {
                                    tvSend.setVisibility(View.GONE);
                                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiAmountDetails(String value) {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100062"+
                            "&sendCountryCode="+"100092"
                            +"&receiveCountryCode="+"100092"+
                            "&currencyValue="+value+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", beneficiaryairtimeC)+
                            "&productCode="+productCategory.optJSONArray("productList").optJSONObject(0).optString("code"),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("SelfAirtime response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee= df.format(jsonObjectAmountDetails.optDouble("fee"));

                                    //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    //  receiverTax = jsonObjectAmountDetails.optInt("receiverTax");

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }
                                    etAmount.setText(value);
                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }
                                    try {
                                        dataToSend.put("accountNumber", etPhone.getText().toString());
                                        dataToSend.put("amount", value);
                                        dataToSend.put("channel", "SELFCARE");
                                        dataToSend.put("fromCurrencyCode", "100062");
                                        dataToSend.put("operator", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("code"));
                                        dataToSend.put("productCode", productCategory.optJSONArray("productList").optJSONObject(0).optString("code"));
                                        dataToSend.put("requestType", "recharge");
                                        dataToSend.put("serviceCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode"));
                                        dataToSend.put("serviceCategoryCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode"));
                                        dataToSend.put("serviceProviderCode", serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode"));

                                        System.out.println("Data Send " + dataToSend.toString());
                                        Intent i = new Intent(beneficiaryairtimeC, BeneficiaryAirtimeConfirm.class);
                                        startActivity(i);
                                    } catch (Exception e) {

                                    }


                                } else {
                                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription", "N/A"));
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
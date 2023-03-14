package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.StringTokenizer;

public class BeneficiaryAirtime extends LogoutAppCompactActivity implements View.OnClickListener {
    public static BeneficiaryAirtime beneficiaryairtimeC;
    ImageView imgBack,imgHome;
    TextView tvAmtCurr,tvSend;
    String phone;
    public static EditText etPhone,etAmount;
    CardView cardOneThousand,cardTwoThousand,cardFiveThousand,cardTenThousand,cardFifteenThousand,cardTwentyThousand;

    private long mLastClickTime = 0;

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
                MyApplication.hideKeyboard(beneficiaryairtimeC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(beneficiaryairtimeC);
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
           //zx phone = phone.replaceAll("[^0-9]","");
            MyApplication.contactValidation(phone,etPhone);

        //    etPhone.setText(phone);

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
                //insertCommaIntoNumber(etAmount,s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isFormatting) {
                    return;
                }

                if(s.length()>0) {

                    formatInput(etAmount,s, s.length(), s.length());

                    callApiAmountDetails();
                }
                isFormatting = false;


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

    private void insertCommaIntoNumber(EditText etText,CharSequence s)
    {
        try {
            if (s.toString().length() > 0)
            {
                String convertedStr = s.toString();
                if (s.toString().contains("."))
                {
                    if(chkConvert(s.toString()))
                        convertedStr = customFormat("###,###.##",Double.parseDouble(s.toString().replace(",","")));
                }
                else
                {
                    convertedStr = customFormat("###,###.##", Double.parseDouble(s.toString().replace(",","")));
                }

                if (!etText.getText().toString().equals(convertedStr) && convertedStr.length() > 0) {
                    etText.setText(convertedStr);
                    etText.setSelection(etText.getText().length());
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String customFormat(String pattern, double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat myFormatter = new DecimalFormat(pattern,symbols);
        String output = myFormatter.format(value);
        return output;
    }

    public boolean chkConvert(String s)
    {
        String tempArray[] = s.toString().split("\\.");
        if (tempArray.length > 1)
        {
            if (Integer.parseInt(tempArray[1]) > 0) {
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvSend:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(etPhone.getText().toString().trim().isEmpty()) {
                        MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_phone));
                        return;
                }
                if(etPhone.getText().toString().trim().length()<9) {
                    MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.enter_phone_no_val));
                    return;
                }
                if (etAmount.getText().toString().trim().replace(",","").isEmpty()) {
                        MyApplication.showErrorToast(beneficiaryairtimeC, getString(R.string.val_amount));
                        return;
                }
                if (etAmount.getText().toString().trim().replace(",","").equals("0") || etAmount.getText().toString().trim().replace(",","").equals(".") || etAmount.getText().toString().trim().replace(",","").equals(".0") ||
                        etAmount.getText().toString().trim().replace(",","").equals("0.") || etAmount.getText().toString().trim().replace(",","").equals("0.0") || etAmount.getText().toString().trim().replace(",","").equals("0.00")) {
                        MyApplication.showErrorToast(beneficiaryairtimeC, getString(R.string.val_valid_amount));
                        return;
                }

                if(Double.parseDouble(etAmount.getText().toString().trim().replace(",",""))<MyApplication.AirtimePurchaseMinAmount) {
                    MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_amount_min)+" "+MyApplication.AirtimePurchaseMinAmount);
                    return;
                }

                if(Double.parseDouble(etAmount.getText().toString().trim().replace(",",""))>MyApplication.AirtimePurchaseMaxAmount) {
                    MyApplication.showErrorToast(beneficiaryairtimeC,getString(R.string.val_amount_max)+" "+MyApplication.AirtimePurchaseMaxAmount);
                    return;
                }
                try {
                        dataToSend.put("accountNumber", etPhone.getText().toString());
                        dataToSend.put("amount", etAmount.getText().toString().trim().replace(",",""));
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
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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
                    MyApplication.hideLoader();
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
                                    MyApplication.hideLoader();
                                    MyApplication.showToast(beneficiaryairtimeC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();
                            tvSend.setVisibility(View.GONE);

                        }
                    });

        } catch (Exception e) {
            MyApplication.hideLoader();
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
                                    MyApplication.hideLoader();
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
            MyApplication.hideLoader();
        }



    }
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
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
                            "&currencyValue="+etAmount.getText().toString().trim().replace(",","")+
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

                                    if(jsonObjectAmountDetails.has("receiverTax")) {
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
                                    if(jsonObjectAmountDetails.has("receiverTax")) {
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
                                       /* Intent i = new Intent(beneficiaryairtimeC, BeneficiaryAirtimeConfirm.class);
                                        startActivity(i);*/
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


    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText, CharSequence s, int start, int count) {

        if(MyApplication.checkMinMax(beneficiaryairtimeC,s,etAmount
                ,MyApplication.AirtimePurchaseMinAmount,MyApplication.AirtimePurchaseMaxAmount)){
            return;
        }
        if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
            return;
        }


        isFormatting = true;

        StringBuilder sbResult = new StringBuilder();
        String result;
        int newStart = start;

        try {
            // Extract value without its comma
            String digitAndDotText = s.toString().replace(",", "");
            int commaAmount = 0;

            // if user press . turn it into 0.
            if (s.toString().startsWith(".") && s.length() == 1) {
                editText.setText("0.");
                editText.setSelection(editText.getText().toString().length());
                return;
            }

            // if user press . when number already exist turns it into comma
            if (s.toString().startsWith(".") && s.length() > 1) {
                StringTokenizer st = new StringTokenizer(s.toString());
                String afterDot = st.nextToken(".");
                editText.setText("0." + AutoFormatUtil.extractDigits(afterDot));
                editText.setSelection(2);
                return;
            }

            if (digitAndDotText.contains(".")) {
                // escape sequence for .
                String[] wholeText = digitAndDotText.split("\\.");

                if (wholeText.length == 0) {
                    return;
                }

                // in 150,000.45 non decimal is 150,000 and decimal is 45
                String nonDecimal = wholeText[0];
                if (nonDecimal.length() == 0) {
                    return;
                }

                // only format the non-decimal value
                result = AutoFormatUtil.formatToStringWithoutDecimal(nonDecimal);

                sbResult
                        .append(result)
                        .append(".");

                if (wholeText.length > 1) {
                    sbResult.append(wholeText[1]);
                }

            } else {
                result = AutoFormatUtil.formatWithDecimal(digitAndDotText);
                sbResult.append(result);
            }

            // count == 0 indicates users is deleting a text
            // count == 1 indicates users is entering a text
            newStart += ((count == 0) ? 0 : 1);

            // calculate comma amount in edit text
            commaAmount += AutoFormatUtil.getCharOccurance(result, ',');

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += ((count == 0) ? -1 : 1);
                prevCommaAmount = commaAmount;
            }

            // case when deleting without comma
            if (commaAmount == 0 && count == 0 && prevCommaAmount != commaAmount) {
                newStart -= 1;
                prevCommaAmount = commaAmount;
            }

            // case when deleting without dots
            if (count == 0 && !sbResult.toString()
                    .contains(".") && prevCommaAmount != commaAmount) {
                newStart = start;
                prevCommaAmount = commaAmount;
            }

            editText.setText(sbResult.toString());

            // ensure newStart is within result length
            if (newStart > sbResult.toString().length()) {
                newStart = sbResult.toString().length();
            } else if (newStart < 0) {
                newStart = 0;
            }

            editText.setSelection(newStart);

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
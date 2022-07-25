package com.estel.cashmoovsubscriberapp.activity.airtimepurchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.ToSubscriberConfirmScreen;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPay;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayConfirmScreen;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.OperatorModel;
import com.estel.cashmoovsubscriberapp.model.SubscriberInfoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class SelfAirtime extends AppCompatActivity implements View.OnClickListener {
    public static SelfAirtime selfairtimeC;
    ImageView imgBack,imgHome;
    TextView tvAmtCurr,spOperator,tvSend;
    public static EditText etPhone,etAmount;
    CardView cardOneThousand,cardTwoThousand,cardFiveThousand,cardTenThousand,cardFifteenThousand,cardTwentyThousand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_airtime);
        selfairtimeC=this;
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
                MyApplication.hideKeyboard(selfairtimeC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(selfairtimeC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        spOperator = findViewById(R.id.spOperator);
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


        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (isFormatting) {
                    return;
                }*/

                if(s.length()>=1) {
                    //formatInput(etAmount,s, s.length(), s.length());

                    callApiAmountDetails();
                }
                //isFormatting = false;

            }

        });


        callwalletOwner();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        cardOneThousand.setOnClickListener(selfairtimeC);
        cardTwoThousand.setOnClickListener(selfairtimeC);
        cardFiveThousand.setOnClickListener(selfairtimeC);
        cardTenThousand.setOnClickListener(selfairtimeC);
        cardFifteenThousand.setOnClickListener(selfairtimeC);
        cardTwentyThousand.setOnClickListener(selfairtimeC);
        tvSend.setOnClickListener(selfairtimeC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSend:
//        if(spOperator.getText().toString().equals(getString(R.string.valid_select_operator))) {
//            MyApplication.showErrorToast(selfairtimeC,getString(R.string.val_select_operator));
//            return;
//        }
//        if(etPhone.getText().toString().trim().isEmpty()) {
//            MyApplication.showErrorToast(selfairtimeC,getString(R.string.val_phone));
//            return;
//        }
                if (etAmount.getText().toString().trim().replace(",","").isEmpty()) {
                    MyApplication.showErrorToast(selfairtimeC, getString(R.string.val_amount));
                    return;
                }
                if (etAmount.getText().toString().trim().replace(",","").equals("0") || etAmount.getText().toString().trim().replace(",","").equals(".") || etAmount.getText().toString().trim().replace(",","").equals(".0") ||
                        etAmount.getText().toString().trim().replace(",","").equals("0.") || etAmount.getText().toString().trim().replace(",","").equals("0.0") || etAmount.getText().toString().trim().replace(",","").equals("0.00")) {
                    MyApplication.showErrorToast(selfairtimeC, getString(R.string.val_valid_amount));
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
                    Intent i = new Intent(selfairtimeC, SelfAirtimeConfirm.class);
                    startActivity(i);
                } catch (Exception e) {

                }
                break;
            case R.id.cardOneThousand:
                callApiAmountDetails("1000");
                break;
            case R.id.cardTwoThousand:
                callApiAmountDetails("2000");
                break;
            case R.id.cardFiveThousand:
                callApiAmountDetails("5000");
                break;
            case R.id.cardTenThousand:
                callApiAmountDetails("10000");
                break;
            case R.id.cardFifteenThousand:
                callApiAmountDetails("15000");
                break;
            case R.id.cardTwentyThousand:
                callApiAmountDetails("20000");
                break;

        }
    }

    public static String serviceProvider,mobile,currency,currencySymbol;
    public static JSONObject serviceCategory = new JSONObject();
    public static JSONObject productCategory = new JSONObject();

    public void callwalletOwner(){
        MyApplication.showloader(selfairtimeC,"Please Wait...");
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
                                    etPhone.setText(mobile);
                                    callApiMsisdnPrefix();
                                }

                            }


                        }
                    }

                }else{
                    MyApplication.showToast(selfairtimeC,jsonObject.optString("resultDescription"));
                    MyApplication.hideLoader();
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(selfairtimeC,aFalse);
                MyApplication.hideLoader();
            }
        });


    }


    private void callApiMsisdnPrefix() {
        try {
            String number = etPhone.getText().toString();
           // String number = "655595950";
            String firstTwodigits = number.substring(0,2);
            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/operator/allByCriteria?msisdnPrefix=224"+firstTwodigits+"&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                           // MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    tvSend.setVisibility(View.VISIBLE);
                                    serviceCategory = jsonObject;
                                    callApiProductProvider();
                                } else {
                                    tvSend.setVisibility(View.GONE);
                                    MyApplication.hideLoader();
                                    MyApplication.showToast(selfairtimeC,jsonObject.optString("resultDescription", "N/A"));
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
                                    MyApplication.showToast(selfairtimeC,jsonObject.optString("resultDescription", "N/A"));
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
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", selfairtimeC)+
                            "&productCode=+"+productCategory.optJSONArray("productList").optJSONObject(0).optString("code"),
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

                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }


                                } else {
                                    MyApplication.showToast(selfairtimeC,jsonObject.optString("resultDescription", "N/A"));
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
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", selfairtimeC)+
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

                                        dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                                        dataToSend.put("transactionArea",MainActivity.transactionArea);
                                        dataToSend.put("isGpsOn",true);
                                        System.out.println("Data Send " + dataToSend.toString());
                                        Intent i = new Intent(selfairtimeC, SelfAirtimeConfirm.class);
                                        startActivity(i);
                                    } catch (Exception e) {

                                    }


                                } else {
                                    MyApplication.showToast(selfairtimeC,jsonObject.optString("resultDescription", "N/A"));
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
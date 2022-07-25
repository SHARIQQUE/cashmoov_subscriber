package com.estel.cashmoovsubscriberapp.activity.rechargeandpayments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.partner.PartnerBillPayPlanList;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class BillPayDetails extends AppCompatActivity implements View.OnClickListener {
    public static BillPayDetails billpaydetailsC;
    ImageView imgBack,imgHome;
    TextView tvOperatorName,tvAmtCurr,tvSend;
    public static EditText etAccountNo,etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billpay_details);
        billpaydetailsC=this;
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
                MyApplication.hideKeyboard(billpaydetailsC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(billpaydetailsC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        tvOperatorName = findViewById(R.id.tvOperatorName);
        etAccountNo = findViewById(R.id.etAccountNo);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        etAmount = findViewById(R.id.etAmount);
        tvSend = findViewById(R.id.tvSend);

        tvOperatorName.setText(BillPay.operatorNname);
        tvAmtCurr.setText(BillPay.currencySymbol);
        if(BillPayPlanList.productTypeCode.equalsIgnoreCase("100001")){
            etAmount.setEnabled(true);
        } else{
            etAmount.setEnabled(false);
            etAmount.setText(String.valueOf(BillPayPlanList.productValue));
            callApiAmountDetails();
        }

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

                if (s.length() > 1) {
                   // formatInput(etAmount,s, s.length(), s.length());

                    callApiAmountDetails();
                }

                //isFormatting = false;


            }

        });

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(billpaydetailsC);
    }


    @Override
    public void onClick(View view) {
                if(etAccountNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(billpaydetailsC,getString(R.string.val_acc_no));
                    return;
                }
                if(etAccountNo.getText().toString().trim().length()<4){
                    MyApplication.showErrorToast(billpaydetailsC,getString(R.string.val_valid_acc_no));
                    return;
                }
                if(etAmount.getText().toString().trim().replace(",","").isEmpty()) {
                    MyApplication.showErrorToast(billpaydetailsC,getString(R.string.val_amount));
                    return;
                }
                if(etAmount.getText().toString().trim().replace(",","").equals("0")||etAmount.getText().toString().trim().replace(",","").equals(".")||etAmount.getText().toString().trim().replace(",","").equals(".0")||
                        etAmount.getText().toString().trim().replace(",","").equals("0.")||etAmount.getText().toString().trim().replace(",","").equals("0.0")||etAmount.getText().toString().trim().replace(",","").equals("0.00")){
                    MyApplication.showErrorToast(billpaydetailsC,getString(R.string.val_valid_amount));
                    return;
                }
                try{
                    dataToSend.put("accountNumber",etAccountNo.getText().toString());
                    dataToSend.put("amount",etAmount.getText().toString().trim().replace(",",""));
                    dataToSend.put("channel","SELFCARE");
                    dataToSend.put("fromCurrencyCode","100062");
                    dataToSend.put("operator",BillPayProduct.operatorCode);
                    dataToSend.put("productCode",BillPayPlanList.productCode);
                    dataToSend.put("requestType","recharge");
                    dataToSend.put("serviceCode",BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode"));
                    dataToSend.put("serviceCategoryCode",BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode"));
                    dataToSend.put("serviceProviderCode",BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode"));
                    dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                    dataToSend.put("transactionArea",MainActivity.transactionArea);
                    dataToSend.put("isGpsOn",true);
                    System.out.println("Data Send "+dataToSend.toString());
                    Intent i=new Intent(billpaydetailsC, BillPayConfirmScreen.class);
                    startActivity(i);
                }catch (Exception e){

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
                            "&currencyValue="+etAmount.getText().toString().trim().replace(",","")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+BillPay.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceProviderCode")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", billpaydetailsC)+
                            "&productCode="+BillPayPlanList.productCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("BillPayDetails response======="+jsonObject.toString());
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
                                    MyApplication.showToast(billpaydetailsC,jsonObject.optString("resultDescription", "N/A"));
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
    private void formatInput(EditText editText,CharSequence s, int start, int count) {
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
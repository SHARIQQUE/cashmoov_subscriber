package com.estel.cashmoovsubscriberapp.activity.cashwithdrawal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.AddBeneficiaryToSubscriber;
import com.estel.cashmoovsubscriberapp.activity.pay.Pay;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.AmountDetailsInfoModel;
import com.estel.cashmoovsubscriberapp.model.CountryCurrencyInfoModel;
import com.estel.cashmoovsubscriberapp.model.SubscriberInfoModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CashWithdrawal extends AppCompatActivity implements View.OnClickListener {
    public static CashWithdrawal cashwithdrawalC;
    ImageView imgBack,imgHome;
    TextView tvAmtCurr,tvName,tvPhone,spBenifiCurr,tvSend;
    AutoCompleteTextView etRecipientNo;
    EditText etAmount;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private boolean isQR;
    private boolean isSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashwithdrawal);
        cashwithdrawalC=this;
        setBackMenu();
        getIds();
    }
    public static final int REQUEST_CODE = 1;

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
                MyApplication.hideKeyboard(cashwithdrawalC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(cashwithdrawalC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void qrScan(View v){
        Intent i = new Intent(cashwithdrawalC, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }

    public boolean isSet=false;
    public static  JSONObject dataToSend=new JSONObject();
    public static String serviceProvider,mobileNo,ownerName,lastName,fee,currencyValue,rate,exRateCode,confCode,
            currency,fromCurrency,fromCurrencySymbol,currencySymbol,fromCurrencyCode,toCurrencyCode,
            receiveCountryCode,payAgentCode;
    public static int receiverFee,receiverTax;
    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    private void getIds() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        spBenifiCurr = findViewById(R.id.spBenifiCurr);
        etRecipientNo = findViewById(R.id.etRecipientNo);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        etAmount = findViewById(R.id.etAmount);
        tvSend = findViewById(R.id.tvSend);

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        // agent_mob_no.setText("991085918540");//dev
        //agent_mob_no.setText("9015050968");//qa

        etRecipientNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher m = p.matcher(s);
                if(s.length()>=9 && m.matches()){
                    if(isSet) {
                        isSet=false;
                    }else{
                        callApiSubsriberList();
                    }
                }
            }
        });

        etRecipientNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etRecipientNo.getRight() - etRecipientNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        MyApplication.isContact=true;
                        Intent intent = new Intent(CashWithdrawal.this,
                                AddBeneficiaryToSubscriber.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        etRecipientNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    etRecipientNo.setText(list[0]);
                    etAmount.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etAmount, InputMethodManager.SHOW_IMPLICIT);
//                    etFname.setText(list[1]);
//                    etLname.setText(list[2]);
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

                if (spBenifiCurr.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
                   // MyApplication.showErrorToast(cashwithdrawalC, getString(R.string.val_select_curr));
                    return;
                }
                else {
                    if(s.length()>=1) {
                        callApiAmountDetails();
                    }else{
//                        tvFee.setText("");
//                        tvAmtPaid.setText("");
//                        tvRate.setText("");
                    }
                }



            }

        });

        if( MyApplication.isScan){
            Intent i = new Intent(cashwithdrawalC, QrCodeActivity.class);
            startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            MyApplication.isScan=false;
        }
        callwalletOwnerDetails();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(cashwithdrawalC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            MyApplication.isContact = false;
            String requiredValue = data.getStringExtra("PHONE");
            etRecipientNo.setText(requiredValue);
            etAmount.requestFocus();

        }
        if (resultCode != Activity.RESULT_OK) {
            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(cashwithdrawalC).create();
                alertDialog.setTitle(getString(R.string.scan_error));
                alertDialog.setMessage(getString(R.string.val_scan_error_content));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("LOGTAG", "Have scan result in your app activity :" + result);
           /* AlertDialog alertDialog = new AlertDialog.Builder(PayActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] date=result.split(":");
                            isQR=true;
                            callwalletOwnerDetailsQR(date[0]);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
*/
            String[] date=result.split(":");
            isQR=true;
            callwalletOwnerDetailsQR(date[0]);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvSend:
                if(etRecipientNo.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(cashwithdrawalC,getString(R.string.val_inst_agent_branch_no));
                    return;
                }
                if(etRecipientNo.getText().toString().trim().length()<9){
                    MyApplication.showErrorToast(cashwithdrawalC,getString(R.string.val_inst_agemt_branch_no_len));
                    return;
                }
                if(etAmount.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(cashwithdrawalC,getString(R.string.val_amount));
                    return;
                }
                if(etAmount.getText().toString().trim().equals("0")||etAmount.getText().toString().trim().equals(".")||etAmount.getText().toString().trim().equals(".0")||
                        etAmount.getText().toString().trim().equals("0.")||etAmount.getText().toString().trim().equals("0.0")||etAmount.getText().toString().trim().equals("0.00")){
                    MyApplication.showErrorToast(cashwithdrawalC,getString(R.string.val_valid_amount));
                    return;
                }
                if(isSuccess){
                try{
                    dataToSend.put("transactionType","101441");
                    dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",getApplicationContext()));
                    dataToSend.put("walletOwnerCategoryCode",MyApplication.getSaveString("walletOwnerCategoryCode",getApplicationContext()));
                    dataToSend.put("receiverCode",payAgentCode);
                    dataToSend.put("senderCode",MyApplication.getSaveString("walletOwnerCode",getApplicationContext()));
                    dataToSend.put("fromCurrencyCode",fromCurrencyCode);
                    dataToSend.put("toCurrencyCode",toCurrencyCode);
                    dataToSend.put("amount",etAmount.getText().toString());
                    dataToSend.put("conversionRate","0.00");
                    dataToSend.put("comments","");
                    dataToSend.put("receiveCountryCode","100092");
                    dataToSend.put("sendCountryCode","100092");
                    dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
                    dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                    dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                    dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));

                    dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                    dataToSend.put("transactionArea",MainActivity.transactionArea);
                    dataToSend.put("isGpsOn",true);
                    //dataToSend.put("exchangeRateCode",exchangeRateCode);
                    MyApplication.saveString("AMOUNTCASHWITHDRAWAL",etAmount.getText().toString(),cashwithdrawalC);
                    System.out.println("Data Send "+dataToSend.toString());
                    Intent i=new Intent(cashwithdrawalC, CashWithdrawalConfirmScreen.class);
                    startActivity(i);
                }catch (Exception e){

                }
                }else{
                    etAmount.setText("");
                    MyApplication.showErrorToast(cashwithdrawalC,getString(R.string.val_valid_inst_agent_branch_no));
                }

                break;

        }
    }

    public void callwalletOwnerDetailsQR(String Code){
        API.GET("ewallet/api/v1/walletOwner/"+Code, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    etRecipientNo.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));
                    //  callwalletOwnerCountryCurrency();
                }else{
                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(cashwithdrawalC,aFalse);
            }
        });

    }

    public void callwalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    walletOwner=jsonObject;
                    JSONObject data=walletOwner.optJSONObject("walletOwner");
                    tvName.setText(data.optString("ownerName","N/A")+" "+data.optString("lastName","N/A"));
                    tvPhone.setText(data.optString("mobileNumber","N/A"));
                    callApiFromCurrency(data.optString("code"));
                }else{
                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(cashwithdrawalC,aFalse);
            }
        });

        callserviceCategory();
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
                                    tvAmtCurr.setText(fromCurrencySymbol);

                                } else {
                                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription", "  "));
                                }
                            }

                            // callApiBenefiCurrency();
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

    private void callApiCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    //JSONObject countryCurrObj = jsonObject.optJSONObject("country");
                                    JSONArray countryCurrencyListArr = jsonObject.optJSONArray("walletOwnerCountryCurrencyList");
                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                        if(data.optString("currencyCode").equalsIgnoreCase("100062")){
                                            toCurrencyCode = data.optString("currencyCode");
                                            currency = data.optString("currencyName");
                                            currencySymbol = data.optString("currencySymbol");
                                            spBenifiCurr.setText(currency);

                                        }


                                    }

                                } else {
                                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription", "  "));
                                }
                            }

                            // callApiBenefiCurrency();
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
        API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100011&serviceCategoryCode=100059&status=Y", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    serviceCategory=jsonObject;
                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                }else{
                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(cashwithdrawalC,aFalse);
            }
        });
    }

    private void callApiSubsriberList() {
        try {
            //walletOwnerCategoryCode
            // MyApplication.showloader(PayActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/walletOwner/all?"+"walletOwnerCategoryCode=100002,100001,100000"+
                            "&mobileNumber="+(etRecipientNo.getText().toString())+"&offset=0&limit=100",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    subscriberList.clear();


                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletOwnerList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        SubscriberInfoModel.Subscriber subscriber = new SubscriberInfoModel.Subscriber(
                                                jsonObjectSubscriber.optInt("id"),
                                                jsonObjectSubscriber.optBoolean("walletExists"),
                                                jsonObjectSubscriber.optString("code", "N/A"),
                                                jsonObjectSubscriber.optString("walletOwnerCategoryCode", "N/A"),
                                                jsonObjectSubscriber.optString("ownerName"),
                                                jsonObjectSubscriber.optString("mobileNumber", "N/A"),
                                                jsonObjectSubscriber.optString("idProofNumber", "N/A"),
                                                jsonObjectSubscriber.optString("email", "N/A"),
                                                jsonObjectSubscriber.optString("status", "N/A"),
                                                jsonObjectSubscriber.optString("state", "N/A"),
                                                jsonObjectSubscriber.optString("stage", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeName", "N/A"),
                                                jsonObjectSubscriber.optString("notificationLanguage", "N/A"),
                                                jsonObjectSubscriber.optString("notificationTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("notificationName", "N/A"),
                                                jsonObjectSubscriber.optString("gender", "N/A"),
                                                jsonObjectSubscriber.optString("dateOfBirth", "N/A"),
                                                jsonObjectSubscriber.optString("lastName"),
                                                jsonObjectSubscriber.optString("registerCountryCode", "N/A"),
                                                jsonObjectSubscriber.optString("registerCountryName", "N/A"),
                                                jsonObjectSubscriber.optString("creationDate", "N/A"),
                                                jsonObjectSubscriber.optString("profileTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("profileTypeName", "N/A"),
                                                jsonObjectSubscriber.optString("walletOwnerCatName", "N/A"),
                                                jsonObjectSubscriber.optString("occupationTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("occupationTypeName", "N/A"),
                                                jsonObjectSubscriber.optString("requestedSource", "N/A"),
                                                jsonObjectSubscriber.optString("regesterCountryDialCode", "N/A"),
                                                jsonObjectSubscriber.optString("walletOwnerCode", "N/A")
                                        );


                                        SubscriberInfoModel subscriberInfoModel = new SubscriberInfoModel(
                                                jsonObject.optString("transactionId", "N/A"),
                                                jsonObject.optString("requestTime", "N/A"),
                                                jsonObject.optString("responseTime", "N/A"),
                                                jsonObject.optString("resultCode", "N/A"),
                                                jsonObject.optString("resultDescription", "N/A"),
                                                subscriber
                                        );

                                        setSubscriberdata(subscriberInfoModel);

                                    }


                                } else{
                                    setSubscriberdataf("No Data");
                                  /*  MyApplication.hideLoader();
                                    MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                          */      }

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

    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;

    private void setSubscriberdataf(String subscriberInfoModel) {
        isSuccess = false;
        subscriberList.clear();
        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(cashwithdrawalC,R.layout.item_select, subscriberList);
        etRecipientNo.setAdapter(adapter);
        etRecipientNo.setThreshold(9);
        etRecipientNo.showDropDown();

    }

    String receiverCode;
    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Subscriber data = subscriberInfoModel.getSubscriber();
        isSuccess = true;
        mobileNo = data.getMobileNumber();
        ownerName = data.getOwnerName();
        lastName = data.getLastName();
        receiveCountryCode = data.getRegisterCountryCode();
        payAgentCode = data.getCode();

        if(isQR){
            etRecipientNo.setText(data.getMobileNumber());
//            et_fname.setText(data.getOwnerName());
//            et_lname.setText(data.getLastName());
            isQR=false;

        }else{
            subscriberList.add(data.getMobileNumber()+","+data.getOwnerName()+","+data.getLastName());
            adapter = new ArrayAdapter<String>(cashwithdrawalC,R.layout.item_select, subscriberList);
            etRecipientNo.setAdapter(adapter);
            etRecipientNo.setThreshold(9);
            etRecipientNo.showDropDown();
        }

        receiverCode=data.getCode();
        if(receiverCode.equalsIgnoreCase("")){
//            etFname.setText("");
//            etLname.setText("");
            etAmount.setText("");
        }else{
            callApiCurrency(receiverCode);
        }
    }

    DecimalFormat df = new DecimalFormat("0.000");
    public static JSONArray taxConfigurationList;
    private void callApiAmountDetails() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+fromCurrencyCode+
                            "&receiveCurrencyCode="+toCurrencyCode+
                            "&sendCountryCode="+walletOwner.optJSONObject("walletOwner").optString("registerCountryCode")
                            +"&receiveCountryCode="+receiveCountryCode+
                            "&currencyValue="+etAmount.getText().toString()+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", cashwithdrawalC)+
                            "&remitAgentCode="+MyApplication.getSaveString("walletOwnerCode", cashwithdrawalC)+
                            "&payAgentCode="+payAgentCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("ToSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");
                                    AmountDetailsInfoModel.AmountDetails amountDetails = new AmountDetailsInfoModel.AmountDetails(
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
                                    fee= df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    rate = jsonObjectAmountDetails.optString("value");
                                    exRateCode = jsonObjectAmountDetails.optString("code");
                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }
                                    tvSend.setVisibility(View.VISIBLE);
                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }


                                } else {
                                    tvSend.setVisibility(View.GONE);
                                    MyApplication.showToast(cashwithdrawalC,jsonObject.optString("resultDescription", "N/A"));
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
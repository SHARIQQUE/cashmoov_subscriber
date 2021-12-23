package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.AddBeneficiary;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.AmountDetailsInfoModel;
import com.estel.cashmoovsubscriberapp.model.SubscriberInfoModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToSubscriber extends AppCompatActivity implements View.OnClickListener {
    public static ToSubscriber tosubscriberC;
    ImageView imgBack,imgHome;
    TextView tvSend;
    AutoCompleteTextView etSubscriberNo;
    public static EditText etFname,etLname,etAmount;
    TextView etName,etPhone;
    private boolean isQR;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    public static final int REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_subscriber);
        tosubscriberC=this;
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

    public void qrScan(View v){
        Intent i = new Intent(tosubscriberC, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

            MyApplication.isContact=false;
            String requiredValue = data.getStringExtra("PHONE");
            etSubscriberNo.setText(requiredValue);

        }
        if (resultCode != Activity.RESULT_OK) {
            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(tosubscriberC).create();
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
            String[] date=result.split(":");
            isQR=true;
            callwalletOwnerDetailsQR(date[0]);

        }
    }

    public boolean isSet=false;
    public static  JSONObject dataToSend=new JSONObject();
    public static String currencyValue,fee,serviceProvider,mobileNo,ownerName,lastName,confCode,currency,currencySymbol;
    public static int receiverFee,receiverTax;


    private void getIds() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etSubscriberNo = findViewById(R.id.etSubscriberNo);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etAmount = findViewById(R.id.etAmount);
        tvSend = findViewById(R.id.tvSend);
        etFname.setEnabled(false);
        etLname.setEnabled(false);

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        //  agent_mob_no.setText("9078678111");
        //agent_mob_no.setText("");

        etSubscriberNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSubscriberNo.getRight() - etSubscriberNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        MyApplication.isContact=true;
                        Intent intent = new Intent(ToSubscriber.this,
                                AddBeneficiaryToSubscriber.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        etSubscriberNo.addTextChangedListener(new TextWatcher() {
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
                        etFname.setText("");
                        etLname.setText("");
                        callApiSubsriberList();
                    }
                }
            }
        });

        etSubscriberNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    etSubscriberNo.setText(list[0]);
                    etFname.setText(list[1]);
                    etLname.setText(list[2]);
                    etAmount.setText("");
                    etAmount.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etAmount, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    etFname.setText("");
                    etLname.setText("");
                }

            }
        });
        callwalletOwner();

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

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(tosubscriberC);

    }





    @Override
    public void onClick(View view) {
        if(etSubscriberNo.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_subscriber_no));
            return;
        }
        if(etSubscriberNo.getText().toString().trim().length()<9) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.enter_subscriber_no_val));
            return;
        }
        if(etAmount.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_amount));
            return;
        }
        if(etAmount.getText().toString().trim().equals("0")||etAmount.getText().toString().trim().equals(".")||etAmount.getText().toString().trim().equals(".0")||
                etAmount.getText().toString().trim().equals("0.")||etAmount.getText().toString().trim().equals("0.0")||etAmount.getText().toString().trim().equals("0.00")){
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_valid_amount));
            return;
        }

        try{
            dataToSend.put("transactionType","104591");
            dataToSend.put("desWalletOwnerCode",walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"));
            dataToSend.put("srcWalletOwnerCode",MyApplication.getSaveString("walletOwnerCode",tosubscriberC));
            dataToSend.put("srcCurrencyCode","100062");
            dataToSend.put("desCurrencyCode","100062");
            dataToSend.put("value",etAmount.getText().toString());
            dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
            dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
            dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
            dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
            dataToSend.put("bearerAllow", false);
            dataToSend.put("bearerFee", 0.0);
            dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
            dataToSend.put("transactionArea",MainActivity.transactionArea);
            dataToSend.put("isGpsOn",true);

            Intent i=new Intent(tosubscriberC, ToSubscriberConfirmScreen.class);
            startActivity(i);
        }catch (Exception e){

        }

    }

    public void callwalletOwner(){

        MyApplication.showloader(tosubscriberC,"Please Wait...");
        API.GET("ewallet/api/v1/wallet/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                   etName.setText(jsonObject.optJSONArray("walletList").optJSONObject(0).optString("walletOwnerName"));
                   etPhone.setText(jsonObject.optJSONArray("walletList").optJSONObject(0).optString("walletOwnerMsisdn"));
                }else{
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });

        callserviceCategory();

    }

    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    private void callApiSubsriberList() {
        try {
            //walletOwnerCategoryCode
            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/walletOwner/all?" + "walletOwnerCategoryCode="
                            + "100010" +
                            "&mobileNumber=" + (etSubscriberNo.getText().toString()) + "&offset=" + "0" + "&limit=" + "500",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
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


                                } else {
                                    setSubscriberdataf("No Data");
                                    // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
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


    public void callwalletOwnerDetailsQR(String Code){
        API.GET("ewallet/api/v1/walletOwner/"+Code, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    etSubscriberNo.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));
                    //  callwalletOwnerCountryCurrency();
                }else{
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                    etFname.setText("");
                    etLname.setText("");
                    etAmount.setText("");
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });
    }


    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {

        subscriberList.clear();

        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(tosubscriberC,R.layout.item_select, subscriberList);
        etSubscriberNo.setAdapter(adapter);
        etSubscriberNo.setThreshold(9);
        etSubscriberNo.showDropDown();



    }

    String receiverCode="";

    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Subscriber data = subscriberInfoModel.getSubscriber();
        mobileNo = data.getMobileNumber();
        ownerName = data.getOwnerName();
        lastName = data.getLastName();
        if (isQR) {
            etSubscriberNo.setText(data.getMobileNumber());
            etFname.setText(data.getOwnerName());
            etLname.setText(data.getLastName());
            isQR = false;

        } else {
            subscriberList.add(data.getMobileNumber() + "," + data.getOwnerName() + "," + data.getLastName());
            adapter = new ArrayAdapter<String>(tosubscriberC, R.layout.item_select, subscriberList);
            etSubscriberNo.setAdapter(adapter);
            etSubscriberNo.setThreshold(9);
            etSubscriberNo.showDropDown();

            etFname.setText(data.getOwnerName());
            etLname.setText(data.getLastName());
        }

        receiverCode=data.getCode();
        if(receiverCode.equalsIgnoreCase("")){
            etFname.setText("");
            etLname.setText("");
            etAmount.setText("");
        }else{
            callApiWalletCountryCurrency(receiverCode);
        }

    }


    private void callApiWalletCountryCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                                    currency = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencyName");
                                    currencySymbol = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencySymbol");
                                } else {
                                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
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

        public void callserviceCategory() {
        API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100000&serviceCategoryCode=100024&status=Y", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    serviceCategory = jsonObject;
                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                } else {
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });
    }



    DecimalFormat df = new DecimalFormat("0.000");
    public static JSONArray taxConfigurationList;
    private void callApiAmountDetails() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100062"+
                            "&sendCountryCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("registerCountryCode")
                            +"&receiveCountryCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("registerCountryCode")+
                            "&currencyValue="+etAmount.getText().toString()+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC)+
                            "&remitAgentCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC)+
                            "&payAgentCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"),
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
                                    fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }


                                } else {
                                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription", "N/A"));
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
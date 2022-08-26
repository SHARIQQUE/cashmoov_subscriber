package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.GenderInfoModel;
import com.estel.cashmoovsubscriberapp.model.SubscriberInfoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ToNonSubscriber extends AppCompatActivity implements View.OnClickListener{
    public static ToNonSubscriber tononsubscriberC;
    ImageView imgBack,imgHome;
    TextView tvAmtCurr,tvSend,tvFee,tvAmtPaid,spGender;
    public static AutoCompleteTextView etPhone;
    public static EditText etAmount,etFname,etLname,etComment,etAmountNew;
    private ArrayList<String> benefiGenderList = new ArrayList<>();
    private ArrayList<GenderInfoModel.Gender> benefiGenderModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogBenefiGender;

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            MyApplication.isContact = false;
            String requiredValue = data.getStringExtra("PHONE");
            MyApplication.contactValidation(requiredValue,etPhone);

           // etPhone.setText(requiredValue);
            etFname.requestFocus();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_non_subscriber);
        tononsubscriberC=this;
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
                MyApplication.hideKeyboard(tononsubscriberC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(tononsubscriberC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    public boolean isSet=false;
    public static  JSONObject dataToSend=new JSONObject();
    public static String currencyValue,fee,serviceProvider,mobileNo,ownerName,lastName,confCode,fromCurrency,fromCurrencySymbol;
    public static int receiverFee,receiverTax;
    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    private void getIds() {
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        etAmount = findViewById(R.id.etAmount);
        tvFee = findViewById(R.id.tvFee);
        tvAmtPaid = findViewById(R.id.tvAmtPaid);
        spGender = findViewById(R.id.spGender);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etComment = findViewById(R.id.etComment);
        tvSend = findViewById(R.id.tvSend);
        etAmountNew = findViewById(R.id.etAmountNew);

        if (getIntent().getExtras() != null) {
            String msisdn  = (getIntent().getStringExtra("TOSUBMSISDN"));
            String amount  = (getIntent().getStringExtra("TOSUBAMOUNT"));

            if(amount!=null){
                etAmount.setText(amount);
                callApiAmountDetails();
            }
            if(etPhone!=null){
                etPhone.setText(msisdn);
                callApiSubsriberList();
            }
        }

        spGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiGender!=null)
                    spinnerDialogBenefiGender.showSpinerDialog();
            }
        });

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        //  agent_mob_no.setText("9078678111");
        //agent_mob_no.setText("");
        etPhone.addTextChangedListener(new TextWatcher() {
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
                        etFname.getText().clear();
                        etLname.getText().clear();
                       // etAmount.getText().clear();
                        etComment.getText().clear();
                        spGender.setText(getString(R.string.valid_select_gender));
                        callApiSubsriberList();
                    }
                }else{
                    etFname.getText().clear();
                    etLname.getText().clear();
                    // etAmount.getText().clear();
                    etComment.getText().clear();
                    spGender.setText(getString(R.string.valid_select_gender));
                }
            }
        });

        etPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etPhone.getRight() - etPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        MyApplication.isContact=true;
                        Intent intent = new Intent(ToNonSubscriber.this,
                                AddBeneficiaryToSubscriber.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });


        etPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    if(list.length==3) {
                        etPhone.setText(list[0]);
                        etFname.setText(list[1]);
                        etLname.setText(list[2]);
                        etComment.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);
                    }else{
                        etPhone.setText(list[0]);
                        etFname.setText(list[1]);
                        etLname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etLname, InputMethodManager.SHOW_IMPLICIT);
                    }

                }else{
                    etFname.setText("");
                    etLname.setText("");
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

                if (isFormatting) {
                    return;
                }

                if(s.length()>0) {

                    formatInput(etAmount,s, s.length(), s.length());

                    callApiAmountDetails();
                }else{
                    //etAmountNew.setText("");
                    tvFee.setText("");
                    tvAmtPaid.setText("");
                }
                isFormatting = false;


            }

        });

        etAmountNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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


                    formatInput(etAmountNew,s, s.length(), s.length());

                    callApiAmountDetailsNew();
                }else{
                    etAmount.setText("");
                    tvFee.setText("");
                    tvAmtPaid.setText("");
                }

                isFormatting = false;
            }

        });

        callserviceCategory();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(tononsubscriberC);

    }

    @Override
    public void onClick(View view) {


        if(etAmount.getText().toString().trim().replace(",","").isEmpty()) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_amount));
            return;
        }
        if(etAmount.getText().toString().trim().replace(",","").equals("0")||etAmount.getText().toString().trim().replace(",","").equals(".")||etAmount.getText().toString().trim().replace(",","").equals(".0")||
                etAmount.getText().toString().trim().replace(",","").equals("0.")||etAmount.getText().toString().trim().replace(",","").equals("0.0")||etAmount.getText().toString().trim().replace(",","").equals("0.00")){
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_valid_amount));
            return;
        }


        if(Double.parseDouble(etAmount.getText().toString().trim().replace(",",""))<MyApplication.ToNonSubscriberMinAmount) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_amount_min)+" "+MyApplication.ToNonSubscriberMinAmount);
            return;
        }

        if(Double.parseDouble(etAmount.getText().toString().trim().replace(",",""))>MyApplication.ToNonSubscriberMaxAmount) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_amount_max)+" "+MyApplication.ToNonSubscriberMaxAmount);
            return;
        }

        if(etPhone.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_phone));
            return;
        }
        if(etPhone.getText().toString().trim().length()<9) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.enter_phone_no_val));
            return;
        }
        if(etFname.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_fname));
            return;
        }
        if(etLname.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_lname));
            return;
        }
        if(spGender.getText().toString().equals(getString(R.string.valid_select_gender))) {
            MyApplication.showErrorToast(tononsubscriberC,getString(R.string.val_select_gender));
            return;
        }

        callApiBeneficiary();
    }

    public void callserviceCategory() {
        API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100000&serviceCategoryCode=NONSUB&status=Y", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    serviceCategory = jsonObject;
                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                } else {
                    MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tononsubscriberC,aFalse);
            }
        });

        callApigenderType();
    }

    private void callApigenderType() {
        try {

            API.GET("ewallet/api/v1/master/GENDERTYPE",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                benefiGenderList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("genderTypeList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            benefiGenderModelList.add(new GenderInfoModel.Gender(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("status"),
                                                    data.optString("type")

                                            ));

                                            benefiGenderList.add(data.optString("type").trim());

                                        }

                                        spinnerDialogBenefiGender = new SpinnerDialog(tononsubscriberC, benefiGenderList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                        spinnerDialogBenefiGender.setCancellable(true); // for cancellable
                                        spinnerDialogBenefiGender.setShowKeyboard(false);// for open keyboard by default
                                        spinnerDialogBenefiGender.bindOnSpinerListener(new OnSpinerItemClick() {
                                            @Override
                                            public void onClick(String item, int position) {
                                                //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                                spGender.setText(item);
                                                spGender.setTag(position);
                                            }
                                        });
                                    }

                                } else {
                                    MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription", "N/A"));
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

        callApiFromCurrency();

    }

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
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
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tononsubscriberC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("NonSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(etAmount.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue = df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        receiverFee = jsonObjectAmountDetails.optInt("receiverFee");
                                        receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        tvFee.setText(fee);
                                        tvAmtPaid.setText(currencyValue);

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                        } else {
                                            taxConfigurationList = null;
                                        }
                                    }

                                } else {
                                    MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiAmountDetailsNew() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100062"+
                            "&sendCountryCode="+"100092"
                            +"&receiveCountryCode="+"100092"+
                            "&currencyValue="+etAmountNew.getText().toString().trim().replace(",","")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tononsubscriberC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("NonSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(etAmountNew.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        try {
                                            double currValue = Double.parseDouble(etAmountNew.getText().toString().trim().replace(",",""));
                                            double value = jsonObjectAmountDetails.optDouble("value");
                                            if (value == 0 || value == .0 || value == 0.0 || value == 0.00 || value == 0.000) {
                                                etAmount.setText(String.valueOf(currValue));
                                            } else {
                                                String finalValue = df.format(currValue / value);
                                                etAmount.setText(finalValue);
                                            }

                                        } catch (Exception e) {
                                        }
//                                    fee=  df.format(jsonObjectAmountDetails.optDouble("fee"));
//                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
//                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
//                                    tvFee.setText(fee);

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

//                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
//                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
//                                    }else{
//                                        taxConfigurationList=null;
//                                    }
                                    }

                                } else {
                                    MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiSubsriberList() {
        try {

            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+etPhone.getText().toString()+"&countryCode=100092",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("customerList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        SubscriberInfoModel.Customer customer = new SubscriberInfoModel.Customer(
                                                jsonObjectSubscriber.optInt("id"),
                                                jsonObjectSubscriber.optString("code", "N/A"),
                                                jsonObjectSubscriber.optString("firstName"),
                                                jsonObjectSubscriber.optString("lastName"),
                                                jsonObjectSubscriber.optString("mobileNumber", "N/A"),
                                                jsonObjectSubscriber.optString("gender", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeName", "N/A"),
                                                jsonObjectSubscriber.optString("idProofNumber", "N/A"),
                                                jsonObjectSubscriber.optString("idExpiryDate", "N/A"),
                                                jsonObjectSubscriber.optString("dateOfBirth", "N/A"),
                                                jsonObjectSubscriber.optString("countryCode", "N/A"),
                                                jsonObjectSubscriber.optString("countryName", "N/A"),
                                                jsonObjectSubscriber.optString("regionCode", "N/A"),
                                                jsonObjectSubscriber.optString("regionName", "N/A"),
                                                jsonObjectSubscriber.optString("city", "N/A"),
                                                jsonObjectSubscriber.optString("address", "N/A"),
                                                jsonObjectSubscriber.optString("issuingCountryCode", "N/A"),
                                                jsonObjectSubscriber.optString("issuingCountryName", "N/A"),
                                                jsonObjectSubscriber.optString("idProofUrl", "N/A"),
                                                jsonObjectSubscriber.optString("status", "N/A"),
                                                jsonObjectSubscriber.optString("creationDate", "N/A"),
                                                jsonObjectSubscriber.optString("createdBy", "N/A"),
                                                jsonObjectSubscriber.optString("modificationDate", "N/A"),
                                                jsonObjectSubscriber.optString("modifiedBy", "N/A")
                                        );


                                        SubscriberInfoModel subscriberInfoModel = new SubscriberInfoModel(
                                                jsonObject.optString("transactionId", "N/A"),
                                                jsonObject.optString("requestTime", "N/A"),
                                                jsonObject.optString("responseTime", "N/A"),
                                                jsonObject.optString("resultCode", "N/A"),
                                                jsonObject.optString("resultDescription", "N/A"),
                                                customer
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

    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {

        subscriberList.clear();

        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(tononsubscriberC,R.layout.item_select, subscriberList);
        etPhone.setAdapter(adapter);
        etPhone.setThreshold(9);
        etPhone.showDropDown();
    }


    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Customer data = subscriberInfoModel.getCustomer();
        mobileNo = data.getMobileNumber();
        ownerName = data.getFirstName();
        lastName = data.getLastName();

            subscriberList.add(data.getMobileNumber() + "," + data.getFirstName() + "," + data.getLastName());
            adapter = new ArrayAdapter<String>(tononsubscriberC, R.layout.item_select, subscriberList);
            etPhone.setAdapter(adapter);
            etPhone.setThreshold(9);
            etPhone.showDropDown();

            etFname.setText(data.getFirstName());
            etLname.setText(data.getLastName());


    }


    private void callApiFromCurrency() {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+"100092",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    fromCurrency = jsonObject.optJSONObject("country").optString("currencyCode");
                                    fromCurrencySymbol = jsonObject.optJSONObject("country").optString("currencySymbol");

                                    tvAmtCurr.setText(fromCurrencySymbol);
                                } else {
                                    MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription", "  "));
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


    private void callApiBeneficiary() {
        try{

            JSONObject benefiJson=new JSONObject();
            benefiJson.put("countryCode","100092");
            benefiJson.put("email","");
            benefiJson.put("firstName",etFname.getText().toString().trim());
            benefiJson.put("lastName",etLname.getText().toString().trim());
            benefiJson.put("gender",benefiGenderModelList.get((Integer) spGender.getTag()).getCode());
            benefiJson.put("mobileNumber",etPhone.getText().toString().trim());

            System.out.println("BenefiLocal request"+benefiJson.toString());

            MyApplication.showloader(tononsubscriberC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/receiver", benefiJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    System.out.println("BenefiLocal response======="+jsonObject.toString());
                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            MyApplication.showToast(tononsubscriberC,getString(R.string.kyc_detail_response_msg));

                            String transStatus = jsonObject.optString("resultDescription", "N/A");
                            String transId = jsonObject.optString("transactionId", "N/A");
                            JSONObject jsonObjectSender = jsonObject.optJSONObject("customer");


                            try{
                                dataToSend.put("amount",etAmount.getText().toString().trim().replace(",",""));
                                dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
                                dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                                dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                                dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                                dataToSend.put("comments",etComment.getText().toString());
                                dataToSend.put("conversionRate","0.00");
                                dataToSend.put("fromCurrencyCode","100062");
                                if(jsonObjectSender.optString("issuingCountryCode").isEmpty()){
                                    dataToSend.put("receiveCountryCode",jsonObjectSender.optString("countryCode"));
                                }else{
                                    dataToSend.put("receiveCountryCode",jsonObjectSender.optString("issuingCountryCode"));
                                }

                                dataToSend.put("receiverCode",jsonObjectSender.optString("code"));
                                dataToSend.put("sendCountryCode",jsonObjectSender.optString("countryCode"));
                                dataToSend.put("senderCode",MyApplication.getSaveString("walletOwnerCode",tononsubscriberC));
                                dataToSend.put("toCurrencyCode","100062");
                                dataToSend.put("transactionType","SENDREMITTANCE");
                                dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",tononsubscriberC));
                                dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                                dataToSend.put("transactionArea",MainActivity.transactionArea);
                                dataToSend.put("isGpsOn",true);

                                dataToSend.put("remitType","Wallet to Cash Local");


                                System.out.println("Data Send "+dataToSend.toString());
                                Intent i=new Intent(tononsubscriberC, ToNonSubscriberConfirmScreen.class);
                                startActivity(i);
                            }catch (Exception e){

                            }

                        } else {
                            MyApplication.showToast(tononsubscriberC,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {

                }
            });

        }catch (Exception e){

        }

    }

    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {
        if(MyApplication.checkMinMax(tononsubscriberC,s,editText
                ,MyApplication.ToNonSubscriberMinAmount,MyApplication.ToNonSubscriberMaxAmount)){
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
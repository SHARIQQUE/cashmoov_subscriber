package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.CountryCurrencyInfoModel;
import com.estel.cashmoovsubscriberapp.model.CountryInfoModel;
import com.estel.cashmoovsubscriberapp.model.GenderInfoModel;
import com.estel.cashmoovsubscriberapp.model.ServiceProviderModel;
import com.estel.cashmoovsubscriberapp.model.SubscriberInfoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class International extends AppCompatActivity implements View.OnClickListener {
    public static International internationalC;
    ImageView imgBack,imgHome;
    TextView spServiceProvider,spRecCountry,spBenifiCurr,spGender,tvFee,tvAmtPaid,tvRate;
    AutoCompleteTextView etPhone;
    public static EditText etAmount,etFname,etLname,etComment;
    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<String> benefiCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> benefiCountryModelList = new ArrayList<>();

    private ArrayList<String> benefiCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> benefiCurrencyModelList = new ArrayList<>();

    private ArrayList<String> benefiGenderList = new ArrayList<>();
    private ArrayList<GenderInfoModel.Gender> benefiGenderModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogSerProvider,spinnerDialogBenefiCountry,spinnerDialogBenefiCurrency,spinnerDialogBenefiGender;
    TextView tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international);
        internationalC=this;
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public boolean isSet=false;
    public static  JSONObject dataToSend=new JSONObject();
    public static String serviceProvider,mobileNo,ownerName,lastName,currencyValue,fee,rate,exRateCode,confCode,currency,fromCurrency,fromCurrencySymbol,toCurrencySymbol;
    public static int receiverFee,receiverTax;
    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    private void getIds() {
        spServiceProvider = findViewById(R.id.spServiceProvider);
        spRecCountry = findViewById(R.id.spRecCountry);
        spBenifiCurr = findViewById(R.id.spBenifiCurr);
        etAmount = findViewById(R.id.etAmount);
        tvFee = findViewById(R.id.tvFee);
        tvAmtPaid = findViewById(R.id.tvAmtPaid);
        tvRate = findViewById(R.id.tvRate);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        spGender = findViewById(R.id.spGender);
        etComment = findViewById(R.id.etComment);
        tvSend = findViewById(R.id.tvSend);

        spServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSerProvider!=null)
                    spinnerDialogSerProvider.showSpinerDialog();
            }
        });

        spRecCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiCountry!=null)
                    spinnerDialogBenefiCountry.showSpinerDialog();
            }
        });

        spBenifiCurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiCurrency!=null)
                    spinnerDialogBenefiCurrency.showSpinerDialog();
            }
        });


        spGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiGender!=null)
                    spinnerDialogBenefiGender.showSpinerDialog();
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
                    MyApplication.showErrorToast(internationalC, getString(R.string.val_select_curr));
                    return;
                }
                else {
                    if(s.length()>=1) {
                        callApiAmountDetails();
                    }else{
                        tvFee.setText("");
                        tvAmtPaid.setText("");
                        tvRate.setText("");
                    }
                }



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
                        etFname.setText("");
                        etLname.setText("");
                        callApiSubsriberList();
                    }
                }
            }
        });

        etPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    etPhone.setText(list[0]);
                    etFname.setText(list[1]);
                    etLname.setText(list[2]);

                }else{
                    etFname.setText("");
                    etLname.setText("");
                }

            }
        });


        callApiserviceProvider();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(internationalC);
    }


    @Override
    public void onClick(View view) {
        if(spServiceProvider.getText().toString().equals(getString(R.string.valid_select_service_provider))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_select_service_provider));
            return;
        }
        if(spRecCountry.getText().toString().equals(getString(R.string.valid_select_rec_country))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_select_country));
            return;
        }
        if(spBenifiCurr.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_select_curr));
            return;
        }
        if(etAmount.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_amount));
            return;
        }
        if(etAmount.getText().toString().trim().equals("0")||etAmount.getText().toString().trim().equals(".")||etAmount.getText().toString().trim().equals(".0")||
                etAmount.getText().toString().trim().equals("0.")||etAmount.getText().toString().trim().equals("0.0")||etAmount.getText().toString().trim().equals("0.00")){
            MyApplication.showErrorToast(internationalC,getString(R.string.val_valid_amount));
            return;
        }
        if(etPhone.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_phone));
            return;
        }
        if(etPhone.getText().toString().trim().length()<9) {
            MyApplication.showErrorToast(internationalC,getString(R.string.enter_phone_no_val));
            return;
        }
        if(etFname.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_fname));
            return;
        }
        if(etLname.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_lname));
            return;
        }
        if(spGender.getText().toString().equals(getString(R.string.valid_select_gender))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.val_select_gender));
            return;
        }
        MyApplication.saveString("AMOUNTINTERNATIONAL",etAmount.getText().toString(),internationalC);
        callApiBeneficiary();

    }

    private void callApiserviceProvider() {
        try {

            API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100000&serviceCategoryCode=INTREM&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                serviceProviderList.clear();
                                serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    serviceCategory = jsonObject;
                                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("serviceProviderList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        serviceProviderModelList.add(new ServiceProviderModel.ServiceProvider(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("name"),
                                                data.optString("serviceCategoryCode"),
                                                data.optString("serviceCategoryName"),
                                                data.optString("serviceCode"),
                                                data.optString("serviceName"),
                                                data.optString("serviceProviderMasterCode"),
                                                data.optString("status")

                                        ));
                                        serviceProviderList.add(data.optString("name").trim());
                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogSerProvider = new SpinnerDialog(internationalC, serviceProviderList, "Select Service Provider", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                    spinnerDialogSerProvider.setCancellable(true); // for cancellable
                                    spinnerDialogSerProvider.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSerProvider.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spServiceProvider.setText(item);
                                            spServiceProvider.setTag(position);


                                        }
                                    });

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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

        callApiCountry();

    }

    private void callApiCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                benefiCountryList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        benefiCountryModelList.add(new CountryInfoModel.Country(
                                                data.optInt("id"),
                                                data.optInt("mobileLength"),
                                                data.optString("code"),
                                                data.optString("isoCode"),
                                                data.optString("name"),
                                                data.optString("countryCode"),
                                                data.optString("status"),
                                                data.optString("dialCode"),
                                                data.optString("currencyCode"),
                                                data.optString("currencySymbol"),
                                                data.optString("creationDate"),
                                                data.optBoolean("subscriberAllowed")
                                        ));

                                        benefiCountryList.add(data.optString("name").trim());

                                    }

                                    spinnerDialogBenefiCountry= new SpinnerDialog(internationalC, benefiCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogBenefiCountry.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spRecCountry.setText(item);
                                            spRecCountry.setTag(position);
                                            spBenifiCurr.setText(getString(R.string.valid_select_benifi_curr));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            callApiCurrency(benefiCountryModelList.get(position).getCode());
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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

                                    spinnerDialogBenefiGender = new SpinnerDialog(internationalC, benefiGenderList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

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

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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

    DecimalFormat df = new DecimalFormat("0.000");
    public static JSONArray taxConfigurationList;
    private void callApiAmountDetails() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"receiveCountryCode="
                    +benefiCountryModelList.get((Integer) spRecCountry.getTag()).getCode()+
                            "&sendCountryCode="+"100092"
                            +"&sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+benefiCurrencyModelList.get((Integer) spBenifiCurr.getTag()).getCurrencyCode()+
                            "&currencyValue="+etAmount.getText().toString()+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", internationalC),

                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee= df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    rate = jsonObjectAmountDetails.optString("value");
                                    exRateCode = jsonObjectAmountDetails.optString("code");
                                    //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                    tvRate.setText(MyApplication.addDecimal(rate));
                                    tvFee.setText(fee);
                                    tvAmtPaid.setText(MyApplication.addDecimal(currencyValue));

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
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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


                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "  "));
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

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                benefiCurrencyList.clear();
                                benefiCurrencyModelList.clear();
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                        benefiCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("createdBy"),
                                                data.optString("creationDate"),
                                                data.optString("currCode"),
                                                data.optString("currencyCode"),
                                                data.optString("currencyName"),
                                                data.optString("currencySymbol"),
                                                data.optString("dialCode"),
                                                data.optInt("mobileLength"),
                                                data.optString("modificationDate"),
                                                data.optString("modifiedBy"),
                                                data.optString("state"),
                                                data.optString("status"),
                                                data.optBoolean("inBound"),
                                                data.optBoolean("outBound")

                                        ));

                                        benefiCurrencyList.add(data.optString("currCode").trim());

                                    }

                                    spinnerDialogBenefiCurrency = new SpinnerDialog(internationalC, benefiCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogBenefiCurrency.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiCurrency.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiCurrency.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spBenifiCurr.setText(item);
                                            spBenifiCurr.setTag(position);
                                            etAmount.setText("");
                                            currency = benefiCurrencyModelList.get(position).getCurrCode();
                                            toCurrencySymbol = benefiCurrencyModelList.get(position).getCurrencySymbol();
                                           // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "  "));
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

    private void callApiSubsriberList() {
        try {

            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+etPhone.getText().toString()+"&countryCode="+
                            benefiCountryModelList.get((Integer) spRecCountry.getTag()).getCode(),
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
        adapter = new ArrayAdapter<String>(internationalC,R.layout.item_select, subscriberList);
        etPhone.setAdapter(adapter);
        etPhone.setThreshold(9);
        etPhone.showDropDown();
    }


    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Customer data = subscriberInfoModel.getCustomer();

        subscriberList.add(data.getMobileNumber() + "," + data.getFirstName() + "," + data.getLastName());
        adapter = new ArrayAdapter<String>(internationalC, R.layout.item_select, subscriberList);
        etPhone.setAdapter(adapter);
        etPhone.setThreshold(9);
        etPhone.showDropDown();

        etFname.setText(data.getFirstName());
        etLname.setText(data.getLastName());


    }

    private void callApiBeneficiary() {
        try{

            JSONObject benefiJson=new JSONObject();
            benefiJson.put("countryCode",benefiCountryModelList.get((Integer) spRecCountry.getTag()).getCode());
            benefiJson.put("email","");
            benefiJson.put("firstName",etFname.getText().toString().trim());
            benefiJson.put("gender",benefiGenderModelList.get((Integer) spGender.getTag()).getCode());
            benefiJson.put("lastName",etLname.getText().toString().trim());
            benefiJson.put("mobileNumber",etPhone.getText().toString().trim());

            System.out.println("BenefiLocal request"+benefiJson.toString());

            MyApplication.showloader(internationalC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/receiver", benefiJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    System.out.println("BenefiLocal response======="+jsonObject.toString());
                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            MyApplication.showToast(internationalC,getString(R.string.kyc_detail_response_msg));

                            String transStatus = jsonObject.optString("resultDescription", "N/A");
                            String transId = jsonObject.optString("transactionId", "N/A");
                            JSONObject jsonObjectSender = jsonObject.optJSONObject("customer");


                            mobileNo = jsonObjectSender.optString("mobileNumber");
                            ownerName = jsonObjectSender.optString("firstName");
                            lastName = jsonObjectSender.optString("lastName");

                            try{
                                dataToSend.put("amount",etAmount.getText().toString());
                                dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
                                dataToSend.put("comments",etComment.getText().toString());
                                dataToSend.put("conversionRate",rate);
                                dataToSend.put("exchangeRateCode",exRateCode);
                                dataToSend.put("fromCurrencyCode","100062");
                                dataToSend.put("receiveCountryCode",jsonObjectSender.optString("countryCode"));
                                dataToSend.put("receiverCode",jsonObjectSender.optString("code"));
                                dataToSend.put("sendCountryCode","100092");
                                dataToSend.put("senderCode",MyApplication.getSaveString("walletOwnerCode",internationalC));
                                dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                                dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                                dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                                dataToSend.put("toCurrencyCode",benefiCurrencyModelList.get((Integer) spBenifiCurr.getTag()).getCurrencyCode());
                                dataToSend.put("transactionType","SENDREMITTANCE");
                                dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",internationalC));

                                System.out.println("Data Send "+dataToSend.toString());
                                Intent i=new Intent(internationalC, InternationalConfirmScreen.class);
                                startActivity(i);
                            }catch (Exception e){

                            }

                        } else {
                            MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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


}
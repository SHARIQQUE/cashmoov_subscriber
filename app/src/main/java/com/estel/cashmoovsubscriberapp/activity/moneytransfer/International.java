package com.estel.cashmoovsubscriberapp.activity.moneytransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.estel.cashmoovsubscriberapp.model.ServiceProviderModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class International extends AppCompatActivity implements View.OnClickListener {
    public static International internationalC;
    ImageView imgBack,imgHome;
    public static TextView spServiceProvider,spRecCountry,spBenifiCurr,tvFee,tvAmtPaid,tvRate,tvAmtCurr,tvAmtPaidCurr;
    public static EditText etAmount,etAmountNew;
    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<String> benefiCountryList = new ArrayList<>();
    public static ArrayList<CountryInfoModel.Country> benefiCountryModelList = new ArrayList<>();

    private ArrayList<String> benefiCurrencyList = new ArrayList<>();
    public static ArrayList<CountryCurrencyInfoModel.CountryCurrency> benefiCurrencyModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogSerProvider,spinnerDialogBenefiCountry,spinnerDialogBenefiCurrency;
    TextView tvNext;
    private boolean isAmt=true;
    private boolean isAmtPaid=true;


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
                MyApplication.isFirstTime=false;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public static String serviceProvider,currencyValue,fee,rate,exRateCode,currency,fromCurrency,fromCurrencySymbol,toCurrencySymbol;
    public static int receiverFee,receiverTax;
    public static JSONObject serviceCategory = new JSONObject();

    private void getIds() {
        spServiceProvider = findViewById(R.id.spServiceProvider);
        spRecCountry = findViewById(R.id.spRecCountry);
        spBenifiCurr = findViewById(R.id.spBenifiCurr);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        tvAmtPaidCurr = findViewById(R.id.tvAmtPaidCurr);
        etAmount = findViewById(R.id.etAmount);
        tvFee = findViewById(R.id.tvFee);
        tvAmtPaid = findViewById(R.id.tvAmtPaid);
        tvRate = findViewById(R.id.tvRate);
        tvNext = findViewById(R.id.tvNext);
        etAmountNew=findViewById(R.id.etAmountNew);
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
                    if(s.length()>=1) {
                        if(isAmt){
                            etAmountNew.setEnabled(false);
                            isAmtPaid=false;
                            callApiAmountDetails();
                        }else{
                            etAmountNew.setEnabled(true);
                            isAmtPaid=true;
                        }
                    }else{
                        isAmtPaid=true;
                        etAmountNew.setEnabled(true);
                        etAmountNew.getText().clear();
                        tvFee.setText("");
                        tvAmtPaid.setText("");
                        tvRate.setText("");
                }

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
                if (spBenifiCurr.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
                    MyApplication.showErrorToast(internationalC, getString(R.string.val_select_curr));
                    return;
                }
                    if(s.length()>=1) {
                        if(isAmtPaid){
                            etAmount.setEnabled(false);
                            isAmt=false;
                            callApiAmountDetailsNew();
                        }else{
                           isAmt=true;
                           etAmount.setEnabled(true);
                        }

                    }else{
                        isAmt=true;
                        etAmount.setEnabled(true);
                        etAmount.getText().clear();
                        tvFee.setText("");
                        tvAmtPaid.setText("");
                        tvRate.setText("");
                    }
                }


        });


        callApiserviceProvider();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(internationalC);
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
        MyApplication.saveString("AMOUNTINTERNATIONAL",etAmount.getText().toString(),internationalC);
        Intent i=new Intent(internationalC, InternationalRecipientDetails.class);
        startActivity(i);

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
                                            spServiceProvider.setText(data.optString("name"));


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

                                    callApiCountry();

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
                                        if (!MyApplication.getSaveString("userCountryCode", internationalC).equalsIgnoreCase(data.optString("code"))) {
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
                                    //etAmountNew.setText(currencyValue);
                                    tvRate.setText(rate);
                                    tvFee.setText(fee);
                                    etAmountNew.setText(currencyValue);
                                    tvAmtPaid.setText(currencyValue);


//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvNext.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvNext.setVisibility(View.VISIBLE);
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


    private void callApiAmountDetailsNew() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");receiveCountryCode
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"receiveCountryCode="
                            +benefiCountryModelList.get((Integer) spRecCountry.getTag()).getCode()+
                            "&sendCountryCode="+"100092"
                            +"&sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+benefiCurrencyModelList.get((Integer) spBenifiCurr.getTag()).getCurrencyCode()+
                            "&currencyValue="+etAmountNew.getText().toString()+
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

                                    try {
                                        double currValue = Double.parseDouble(etAmountNew.getText().toString());
                                        double value = jsonObjectAmountDetails.optDouble("value");
                                        if(value==0||value==.0||value==0.0||value==0.00||value==0.000){
                                            tvAmtPaid.setText(String.valueOf(currValue));
                                        }else{
                                            String finalValue = df.format(currValue / value);
                                           // tvAmtPaid.setText(finalValue);
                                            etAmount.setText(finalValue);
                                        }

                                    }catch (Exception e){}

                                    currencyValue = etAmountNew.getText().toString();
                                    //currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee= df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    rate = jsonObjectAmountDetails.optString("value");
                                    exRateCode = jsonObjectAmountDetails.optString("code");
                                    //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                    //etAmountNew.setText(currencyValue);
                                    tvRate.setText(rate);
                                    tvFee.setText(fee);
                                   // etAmountNew.setText(currencyValue);
                                    tvAmtPaid.setText(currencyValue);

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvNext.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvNext.setVisibility(View.VISIBLE);
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
                                    tvAmtCurr.setText(fromCurrencySymbol);

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
                                            currency = benefiCurrencyModelList.get(position).getCurrCode();
                                            toCurrencySymbol = benefiCurrencyModelList.get(position).getCurrencySymbol();
                                            tvAmtPaidCurr.setText(toCurrencySymbol);
                                           // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            etAmount.getText().clear();
                                            etAmountNew.getText().clear();
                                        }
                                    });

                                    callApiFromCurrency();

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "  "));
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
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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class InternationalRecipientDetails extends AppCompatActivity implements View.OnClickListener {
    public static InternationalRecipientDetails internationalC;
    ImageView imgBack,imgHome;
    TextView spGender;
    AutoCompleteTextView etPhone;
    public static EditText etFname,etLname,etComment;


    private ArrayList<String> benefiGenderList = new ArrayList<>();
    private ArrayList<GenderInfoModel.Gender> benefiGenderModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogBenefiGender;
    TextView tvSend;

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            MyApplication.isContact = false;
            String requiredValue = data.getStringExtra("PHONE");
            etPhone.setText(requiredValue);
            etFname.requestFocus();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_recipient_details);
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
    public static String mobileNo,ownerName,lastName,fromCurrency,fromCurrencySymbol,toCurrencySymbol;
    public static int receiverFee,receiverTax;
    public static JSONObject walletOwner = new JSONObject();

    private void getIds() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        spGender = findViewById(R.id.spGender);
        etComment = findViewById(R.id.etComment);
        tvSend = findViewById(R.id.tvSend);


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
                        etFname.setText("");
                        etLname.setText("");
                        spGender.setText(getString(R.string.valid_select_gender));
                        callApiSubsriberList();
                    }
                }else{
                    etFname.setText("");
                    etLname.setText("");
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
                        Intent intent = new Intent(InternationalRecipientDetails.this,
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
                    if (list.length == 3) {
                        etPhone.setText(list[0]);
                        etFname.setText(list[1]);
                        etLname.setText(list[2]);
                        etComment.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        etPhone.setText(list[0]);
                        etFname.setText(list[1]);
                        etLname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etLname, InputMethodManager.SHOW_IMPLICIT);
                    }
                }else{
                    etFname.setText("");
                    etLname.setText("");
                    spGender.setText(getString(R.string.valid_select_gender));
                }

            }
        });


        callApigenderType();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvSend.setOnClickListener(internationalC);
    }


    @Override
    public void onClick(View view) {
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
        callApiBeneficiary();

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

    }

    private void callApiSubsriberList() {
        try {

            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+etPhone.getText().toString()+"&countryCode="+
                            International.benefiCountryModelList.get((Integer)International.spRecCountry.getTag()).getCode(),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("customerList");
                                    int pcount=0;
                                    int index=0;
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        Iterator<?> keys = jsonObjectSubscriber.keys();
                                        JSONObject countObj=new JSONObject();
                                        int count=0;
                                        while( keys.hasNext() ) {
                                            count++;
                                            String key = (String) keys.next();

                                        }
                                        if(count>pcount){
                                            index=i;
                                            pcount=count;
                                        }

                                    }


                                    JSONObject jsonObjectSubscriber =  walletOwnerListArr.optJSONObject(index);
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

//        subscriberList.add(""+""+subscriberInfoModel+""+"");
//        adapter = new ArrayAdapter<String>(internationalC,R.layout.item_select, subscriberList);
//        etPhone.setAdapter(adapter);
//        etPhone.setThreshold(9);
//        etPhone.showDropDown();
        etFname.setText("");
        etLname.setText("");
        spGender.setText(getString(R.string.valid_select_gender));
    }

    String gendercode="";
    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Customer data = subscriberInfoModel.getCustomer();

//        subscriberList.add(data.getMobileNumber() + "," + data.getFirstName() + "," + data.getLastName());
//        adapter = new ArrayAdapter<String>(internationalC, R.layout.item_select, subscriberList);
//        etPhone.setAdapter(adapter);
//        etPhone.setThreshold(9);
//        etPhone.showDropDown();

        etFname.setText(data.getFirstName());
        etLname.setText(data.getLastName());
        if(data.getGender().equalsIgnoreCase("M"))
        {
            spGender.setText("Male");
            gendercode = "M";
        } else if(data.getGender().equalsIgnoreCase("F")) {
            spGender.setText("Female");
            gendercode = "F";
        } else{
            spGender.setText("Other");
            gendercode = "O";
        }
        etComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);



    }

    private void callApiBeneficiary() {
        try{

            JSONObject benefiJson=new JSONObject();
            benefiJson.put("countryCode",International.benefiCountryModelList.get((Integer)International.spRecCountry.getTag()).getCode());
            benefiJson.put("email","");
            benefiJson.put("firstName",etFname.getText().toString().trim());
            if(spGender.getTag()!=null){
                benefiJson.put("gender",benefiGenderModelList.get((Integer) spGender.getTag()).getCode());
            }else{
                benefiJson.put("gender",gendercode);
            }

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
                                dataToSend.put("amount",International.etAmount.getText().toString());
                                dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
                                dataToSend.put("comments",etComment.getText().toString());
                                dataToSend.put("conversionRate",International.rate);
                                dataToSend.put("exchangeRateCode",International.exRateCode);
                                dataToSend.put("fromCurrencyCode","100062");
                                dataToSend.put("receiveCountryCode",jsonObjectSender.optString("countryCode"));
                                dataToSend.put("receiverCode",jsonObjectSender.optString("code"));
                                dataToSend.put("sendCountryCode","100092");
                                dataToSend.put("senderCode",MyApplication.getSaveString("walletOwnerCode",internationalC));
                                dataToSend.put("serviceCode",International.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                                dataToSend.put("serviceCategoryCode",International.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                                dataToSend.put("serviceProviderCode",International.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                                dataToSend.put("toCurrencyCode",International.benefiCurrencyModelList.get((Integer)International.spBenifiCurr.getTag()).getCurrencyCode());
                                dataToSend.put("transactionType","SENDREMITTANCE");
                                dataToSend.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",internationalC));


                                dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
                                dataToSend.put("transactionArea",MainActivity.transactionArea);
                                dataToSend.put("isGpsOn",true);

                                dataToSend.put("remitType","Wallet to Cash International");

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
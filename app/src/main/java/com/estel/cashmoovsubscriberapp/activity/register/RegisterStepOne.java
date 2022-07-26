package com.estel.cashmoovsubscriberapp.activity.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.VerifyRegisterOTP;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.CityInfoModel;
import com.estel.cashmoovsubscriberapp.model.GenderModel;
import com.estel.cashmoovsubscriberapp.model.OccupationTypeModel;
import com.estel.cashmoovsubscriberapp.model.RegionInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegisterStepOne extends AppCompatActivity implements View.OnClickListener {

    public static RegisterStepOne registersteponeC;
    public static String subscriberWalletOwnerCode;
    DatePickerDialog picker;
    public static EditText etFname,etLname,etPhone,etEmail,etAddress,etDob;
    TextView tvNext,spRegion,spCity,spGender,spOccupation;
    private ImageView mCalenderIcon_Image;
    SpinnerDialog spinnerDialogGender,spinnerDialogOccupation,spinnerDialogRegion,spinnerDialogCity;

    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();

    private ArrayList<String> genderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> genderModelList=new ArrayList<>();

    private ArrayList<String> occupationTypeList = new ArrayList<>();
    private ArrayList<OccupationTypeModel.OccupationType> occupationTypeModelList=new ArrayList<>();

    private static String dob = "";
    public static Dialog dialog;
    private TextView mDobText;
    String calmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_stepone);
        registersteponeC=this;
        getIds();

    }

    private void getIds() {

        mDobText=findViewById(R.id.dobText);

        etFname = findViewById(R.id.etFname);
        etFname.setNextFocusDownId(R.id.etLname);

        etLname = findViewById(R.id.etLname);
        etLname.setNextFocusDownId(R.id.etPhone);

        etPhone = findViewById(R.id.etPhone);
        etPhone.setNextFocusDownId(R.id.etEmail);

        etEmail = findViewById(R.id.etEmail);
        etEmail.setNextFocusDownId(R.id.etAddress);

        spRegion = findViewById(R.id.spRegion);
        spCity = findViewById(R.id.spCity);
        etAddress = findViewById(R.id.etAddress);
        etAddress.setNextFocusDownId(R.id.etDob);

        etDob = findViewById(R.id.etDob);
        spGender = findViewById(R.id.spGender);
        spOccupation = findViewById(R.id.spOccupation);
        tvNext = findViewById(R.id.tvNext);
        mCalenderIcon_Image=findViewById(R.id.calenderIcon_Image);
        // etDob.setInputType(InputType.TYPE_NULL);
        mCalenderIcon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme5();

                dialogfragment.show(getSupportFragmentManager(), "Theme 3");

               // ffffff

            }
        });

        // date picker dialog


        spRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogRegion!=null){
                    spinnerDialogRegion.showSpinerDialog();
                }

            }
        });
        spCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogCity!=null){
                    spinnerDialogCity.showSpinerDialog();
                }

            }
        });
        spGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogGender!=null)
                    spinnerDialogGender.showSpinerDialog();
            }
        });
        spOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogOccupation!=null)
                    spinnerDialogOccupation.showSpinerDialog();
            }
        });


        setOnCLickListener();




    }



    @Override
    protected void onStart() {
        super.onStart();

        callApiRegions();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(registersteponeC);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.tvNext:
                if(etFname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_fname));
                    MyApplication.showTipError(this,getString(R.string.val_fname),etFname);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etFname.getText().toString().trim().length()<3) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_fname));
                    MyApplication.showTipError(this,getString(R.string.val_fname_len),etFname);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }

                if(etLname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_lname));
                    MyApplication.showTipError(this,getString(R.string.val_lname),etLname);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etLname.getText().toString().trim().length()<3) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_lname));
                    MyApplication.showTipError(this,getString(R.string.val_lname_len),etLname);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etPhone.getText().toString().trim().isEmpty()) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_phone));
                    MyApplication.showTipError(this,getString(R.string.enter_phone_no),etPhone);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etPhone.getText().toString().trim().length()<9) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_phone));
                    MyApplication.showTipError(this,getString(R.string.enter_phone_no_val),etPhone);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etEmail.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_email));
                    MyApplication.showTipError(this,getString(R.string.val_email_valid),etEmail);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(!MyApplication.isEmail(etEmail.getText().toString())){
                    MyApplication.showTipError(this,getString(R.string.val_email_valid),etEmail);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(spRegion.getText().toString().equals(getString(R.string.valid_select_region))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_region),spRegion);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(spCity.getText().toString().equals(getString(R.string.valid_select_city))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_city),spCity);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etAddress.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_address));
                    MyApplication.showTipError(this,getString(R.string.val_address),etAddress);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(spGender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_gender),spGender);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(spOccupation.getText().toString().equals(getString(R.string.valid_select_occupation))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_occupation));
                    MyApplication.showTipError(this,getString(R.string.val_select_occupation),spOccupation);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                if(etDob.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_dob));
                    MyApplication.showTipError(this,getString(R.string.val_dob),etDob);
                    MyApplication.hideKeyboard(registersteponeC);
                    return;
                }
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("ownerName",etFname.getText().toString().trim());
                    jsonObject.put("lastName",etLname.getText().toString().trim());
                    jsonObject.put("dateOfBirth",etDob.getText().toString().trim());
                    jsonObject.put("idExpiryDate","2021-11-09");
                    jsonObject.put("email",etEmail.getText().toString().trim());
                    jsonObject.put("gender",genderModelList.get((Integer) spGender.getTag()).getCode());
                    jsonObject.put("mobileNumber",etPhone.getText().toString().trim());
                    // jsonObject.put("idProofNumber","");
                    // jsonObject.put("idProofTypeCode","");

                    jsonObject.put("issuingCountryCode","100092");
                    jsonObject.put("registerCountryCode","100092");
                    jsonObject.put("notificationLanguage",MyApplication.getSaveString("Locale", registersteponeC));
                    jsonObject.put("notificationTypeCode","100002");
                    jsonObject.put("occupationTypeCode",occupationTypeModelList.get((Integer) spOccupation.getTag()).getCode());
                    jsonObject.put("regionCode",regionModelList.get((Integer) spRegion.getTag()).getCode());
                    jsonObject.put("city",cityModelList.get((Integer) spCity.getTag()).getCode());
                    jsonObject.put("addressLine1",etAddress.getText().toString().trim());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("RegisterOne request======="+jsonObject.toString());

                callRegisterApi(jsonObject);

        }

    }
    //http://202.131.144.130:8081/ewallet/api/v1/region/country/100092
    private void callApiRegions() {
        try {
//http://202.131.144.130:8081/ewallet/public/region/country/100092
            API.GET_PUBLIC("ewallet/public/region/country/100092",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {
                                regionList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectRegions = jsonObject.optJSONObject("country");
                                    JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("regionList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        regionModelList.add(new RegionInfoModel.Region(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("creationDate"),
                                                data.optString("name"),
                                                data.optString("state"),
                                                data.optString("status")

                                        ));

                                        regionList.add(data.optString("name").trim());

                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogRegion = new SpinnerDialog(registersteponeC, regionList, "Select Region", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogRegion.setCancellable(true); // for cancellable
                                    spinnerDialogRegion.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRegion.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spRegion.setText(item);
                                            spRegion.setTag(position);
                                            spCity.setText(getString(R.string.valid_select_city));

                                            callApiCity(regionModelList.get(position).getCode());
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiCity(String code) {
        try {
//http://202.131.144.130:8081/ewallet/public/city/region/100068
            API.GET_PUBLIC("ewallet/public/city/region/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {

                                cityList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectRegions = jsonObject.optJSONObject("region");
                                    JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("cityList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        cityModelList.add(new CityInfoModel.City(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("modificationDate"),
                                                data.optString("name"),
                                                data.optString("regionCode"),
                                                data.optString("regionName"),
                                                data.optString("state"),
                                                data.optString("status")

                                        ));

                                        cityList.add(data.optString("name").trim());

                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogCity = new SpinnerDialog(registersteponeC, cityList, "Select City", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogCity.setCancellable(true); // for cancellable
                                    spinnerDialogCity.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCity.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCity.setText(item);
                                            spCity.setTag(position);
                                        }
                                    });

                                    callApiGenderType();

                                } else {
                                    MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiGenderType() {
        try {
            API.GET_PUBLIC("ewallet/public/gender/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                genderList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("genderTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        genderModelList.add(new GenderModel.Gender(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        genderList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogGender = new SpinnerDialog(registersteponeC, genderList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogGender.setCancellable(true); // for cancellable
                                    spinnerDialogGender.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogGender.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spGender.setText(item);
                                            spGender.setTag(position);
                                        }
                                    });

                                    callApiOccupationType();

                                } else {
                                    MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiOccupationType() {
        try {
            API.GET_PUBLIC("ewallet/public/occupationType/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                occupationTypeList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("occupationTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        occupationTypeModelList.add(new OccupationTypeModel.OccupationType(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        occupationTypeList.add(data.optString("type").trim());

                                    }
                                    spinnerDialogOccupation = new SpinnerDialog(registersteponeC, occupationTypeList, "Select Occupation", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogOccupation.setCancellable(true); // for cancellable
                                    spinnerDialogOccupation.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogOccupation.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spOccupation.setText(item);
                                            spOccupation.setTag(position);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription", "N/A"));
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

    public void callRegisterApi(JSONObject jsonObject){

        MyApplication.showloader(registersteponeC,"Please wait...");
        API.POST_REQEST_REGISTER("ewallet/public/subscribersignup", jsonObject, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                System.out.println("RegisterOne response======="+jsonObject.toString());
                if(jsonObject!=null){
                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                        MyApplication.saveBool("FirstLogin",false,registersteponeC);
                        MyApplication.UserMobile=etPhone.getText().toString().trim();
                        subscriberWalletOwnerCode = jsonObject.optJSONObject("walletOwner").optString("walletOwnerCode");
                        etFname.setText(jsonObject.optJSONObject("walletOwner").optString("ownerName"));
                        etLname.setText(jsonObject.optJSONObject("walletOwner").optString("lastName"));
                        etPhone.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber"));
                        etEmail.setText(jsonObject.optJSONObject("walletOwner").optString("email"));
                        etDob.setText(jsonObject.optJSONObject("walletOwner").optString("dateOfBirth"));
                        if(jsonObject.optJSONObject("walletOwner").optString("gender").equalsIgnoreCase("M")){
                            spGender.setText("Male");
                        } if(jsonObject.optJSONObject("walletOwner").optString("gender").equalsIgnoreCase("F")){
                            spGender.setText("Female");
                        }
                        Intent i = new Intent(registersteponeC, VerifyRegisterOTP.class);
                        startActivity(i);
                        // callApiAddSubscriberAddress(subscriberWalletOwnerCode);
                    }else{
                        MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription"));
                    }

                }
            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(registersteponeC,aFalse);
            }

        });

    }

    private void callApiAddSubscriberAddress(String subscriberWalletOwnerCode) {
        try{
            JSONObject jsonObjectadd=new JSONObject();
            JSONObject addSubscriberJson=new JSONObject();
            try {
                addSubscriberJson.put("walletOwnerCode",subscriberWalletOwnerCode);

                jsonObjectadd.put("addTypeCode","");
                jsonObjectadd.put("addressLine1",etAddress.getText().toString().trim());
                jsonObjectadd.put("addressLine2","");
                jsonObjectadd.put("countryCode","100092");
                jsonObjectadd.put("city",cityModelList.get((Integer) spCity.getTag()).getCode());
                jsonObjectadd.put("regionCode",regionModelList.get((Integer) spRegion.getTag()).getCode());
                jsonObjectadd.put("location","");

                JSONArray jsonArray=new JSONArray();

                jsonArray.put(jsonObjectadd);
                addSubscriberJson.put("addressList",jsonArray);

            }catch (Exception e){

            }

            MyApplication.showloader(registersteponeC,"Please wait!");
            API.POST_REQEST_REGISTER("ewallet/api/v1/address", addSubscriberJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            //MyApplication.showToast(getString(R.string.address_add_msg));
                            Intent i = new Intent(registersteponeC, VerifyRegisterOTP.class);
                            startActivity(i);
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(registersteponeC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(registersteponeC,jsonObject.optString("resultDescription", "N/A"));
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


    ////////// For address Post Api

//{
//        "walletOwnerCode": "1000002708",
//        "addressList": [
//        {
//        "addTypeCode": "",
//        "addressLine1": "105",
//        "addressLine2": "",
//        "countryCode": "100102",
//        "city": "100025",
//        "regionCode": "100010",
//        "location": ""
//        }
//        ]
//        }
//
//
//
//        http://202.131.144.130:8081/ewallet/api/v1/address

    public static class DatePickerDialogTheme5 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.YEAR, -18);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL, this, year, month, day);

            datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());


            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            etDob.setText(year + "-" + (month+1) + "-" + day);

            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }
}
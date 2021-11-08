package com.estel.cashmoovsubscriberapp.activity.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.VerifyRegisterOTP;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.GenderModel;
import com.estel.cashmoovsubscriberapp.model.OccupationTypeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegisterStepOne extends AppCompatActivity implements View.OnClickListener {

    public static RegisterStepOne registersteponeC;
    DatePickerDialog picker;
    EditText etFname,etLname,etPhone,etEmail,etCity,etAddress,etDob;
    TextView tvNext,spGender,spOccupation;
    SpinnerDialog spinnerDialogGender,spinnerDialogOccupation;
    private ArrayList<String> genderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> genderModelList=new ArrayList<>();

    private ArrayList<String> occupationTypeList = new ArrayList<>();
    private ArrayList<OccupationTypeModel.OccupationType> occupationTypeModelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_stepone);
        registersteponeC=this;
        getIds();

    }

    private void getIds() {

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        etDob = findViewById(R.id.etDob);
        spGender = findViewById(R.id.spGender);
        spOccupation = findViewById(R.id.spOccupation);
        tvNext = findViewById(R.id.tvNext);

        etDob.setInputType(InputType.TYPE_NULL);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterStepOne.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, 1960, 01, 00);
                picker.show();
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

        callApiOccupationType();
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
                if(etCity.getText().toString().trim().isEmpty()) {
                   // MyApplication.showErrorToast(registersteponeC,getString(R.string.val_city));
                    MyApplication.showTipError(this,getString(R.string.val_city),etCity);
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
                    jsonObject.put("notificationTypeCode","100000");
                    jsonObject.put("occupationTypeCode",occupationTypeModelList.get((Integer) spOccupation.getTag()).getCode());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callRegisterApi(jsonObject);

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
                        Intent i = new Intent(registersteponeC, VerifyRegisterOTP.class);
                        startActivity(i);
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

}
package com.estel.cashmoovsubscriberapp.activity.fee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.AirtimeFeeOperatorAdapter;
import com.estel.cashmoovsubscriberapp.adapter.BillPayFeeOperatorAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.OperatorAirtimeFeeListeners;
import com.estel.cashmoovsubscriberapp.listners.OperatorBillPayFeeListeners;
import com.estel.cashmoovsubscriberapp.model.OperatorModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BillPayFeeActivity extends AppCompatActivity implements View.OnClickListener, OperatorBillPayFeeListeners {
    public static BillPayFeeActivity billpayfeeC;
    ImageView imgBack,imgHome;
    RecyclerView rvOperator;
    TextView tvServiceName;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_airtime_fee);
        billpayfeeC=this;
        //setBackMenu();
        getIds();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgHome = findViewById(R.id.imgHome);
//
//
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }

    private void getIds() {
        rvOperator = findViewById(R.id.rvOperator);
        btnClose = findViewById(R.id.btnClose);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.bill_payment));

        setOnCLickListener();

        callBillPayOperatorProvider();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(billpayfeeC);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onOperatorBillPayFeeListItemClick(String code, String name) {
        callBillPayFee(code);
    }


    private ArrayList<OperatorModel> operatorBillPayList = new ArrayList<>();
    private void callBillPayOperatorProvider() {
        try {
            MyApplication.showloader(billpayfeeC,"Please Wait...");
            API.GET("ewallet/api/v1/operator/allByCriteria?serviceCode=100001&serviceCategoryCode=100028&status=Y&offset=0&limit=200",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                operatorBillPayList.clear();
                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("operatorList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            operatorBillPayList.add(new OperatorModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("modificationDate"),
                                                    data.optString("name"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceCategoryName"),
                                                    data.optString("serviceCode"),
                                                    data.optString("serviceName"),
                                                    data.optString("serviceProviderCode"),
                                                    data.optString("serviceProviderName"),
                                                    data.optString("state"),
                                                    data.optString("status")

                                            ));

                                        }
                                        BillPayFeeOperatorAdapter billPayFeeOperatorAdapter = new BillPayFeeOperatorAdapter(billpayfeeC,operatorBillPayList);
                                        rvOperator.setHasFixedSize(true);
                                        rvOperator.setLayoutManager(new LinearLayoutManager(billpayfeeC, LinearLayoutManager.VERTICAL,false));
                                        rvOperator.setAdapter(billPayFeeOperatorAdapter);
                                    }

                                } else {
                                    MyApplication.showToast(billpayfeeC,jsonObject.optString("resultDescription", "N/A"));
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

    public static JSONObject mainJsonObject=null;

    private void callBillPayFee(String operatorCode) {
        try {

            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+MyApplication.getSaveString("walletOwnerCode", billpayfeeC)+"/"+operatorCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    mainJsonObject = new JSONObject();
                                    mainJsonObject = jsonObject;
                                    JSONArray walletOwnerTemplateList=mainJsonObject.optJSONArray("walletOwnerTemplateList");
                                    if(walletOwnerTemplateList!=null&& walletOwnerTemplateList.length()>0) {
                                        JSONObject data=walletOwnerTemplateList.optJSONObject(0);
                                        if(data.has("feeTemplateList")){
                                            JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                            for (int i=0;i<feeTemplateList.length();i++){
                                                JSONObject fee=feeTemplateList.optJSONObject(i);


                                            }

                                            Intent i = new Intent(billpayfeeC,FeeDetails.class);
                                            i.putExtra("FEEINTENT","Bill Payment");
                                            //i.putExtra("OPERATORCODE",operatorCode);
                                            startActivity(i);
                                        }
                                    }


                                } else {
                                    MyApplication.showToast(billpayfeeC,jsonObject.optString("resultDescription", "N/A"));
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



}

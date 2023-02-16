package com.estel.cashmoovsubscriberapp.activity.rechargeandpayments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.PlanListAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.PlanListeners;
import com.estel.cashmoovsubscriberapp.model.ProductModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BillPayPlanList extends AppCompatActivity implements PlanListeners {
    public static BillPayPlanList billplanlistC;
    ImageView imgBack, imgHome;
    RecyclerView rvPlanList;
    private ArrayList<ProductModel> productList = new ArrayList<>();
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay_plan_list);
        billplanlistC = this;
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
                MyApplication.hideKeyboard(billplanlistC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime = false;
                MyApplication.hideKeyboard(billplanlistC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rvPlanList = findViewById(R.id.rvPlanList);
        callApiPlanList();

    }

//    {
//        "transactionId": "2650692",
//            "requestTime": "Mon Feb 28 13:15:19 UTC 2022",
//            "responseTime": "Mon Feb 28 13:15:19 UTC 2022",
//            "resultCode": "0",
//            "resultDescription": "Transaction Successful",
//            "pageable": {
//        "totalRecords": 2
//    },
//        "productList": [
//        {
//            "id": 37,
//                "code": "100038",
//                "serviceCategoryCode": "100028",
//                "serviceCategoryName": "TV",
//                "operatorCode": "100046",
//                "operatorName": "STARTIMES RECHARGE",
//                "productTypeCode": "100001",
//                "productTypeName": "Flexi",
//                "name": "STARTIMES BASIQUE",
//                "productMasterCode": "100002",
//                "value": 0,
//                "description": "OTHERS",
//                "minValue": 1,
//                "maxValue": 5000,
//                "status": "Active",
//                "state": "Approved",
//                "creationDate": "2022-02-28T11:14:39.318+0530",
//                "modificationDate": "2022-02-28T11:17:55.773+0530",
//                "vendorProductCode": "P_2AK0VR0"
//        },
//        {
//            "id": 36,
//                "code": "100037",
//                "serviceCategoryCode": "100028",
//                "serviceCategoryName": "TV",
//                "operatorCode": "100046",
//                "operatorName": "STARTIMES RECHARGE",
//                "productTypeCode": "100000",
//                "productTypeName": "Fixed",
//                "name": "STARTIMES BASIQUE",
//                "productMasterCode": "100002",
//                "value": 7000,
//                "description": "STARTIMES BASIQUE JOUR",
//                "minValue": 0,
//                "maxValue": 0,
//                "status": "Active",
//                "state": "Approved",
//                "creationDate": "2022-02-28T11:11:44.675+0530",
//                "modificationDate": "2022-02-28T11:17:35.802+0530",
//                "vendorProductCode": "P_2AK0VR0"
//        }
//  ]
//    }


    public static JSONObject productCategory = new JSONObject();

    private void callApiPlanList() {
        try {
            MyApplication.showloader(billplanlistC,getString(R.string.please_wait));
            API.GET("ewallet/api/v1/product/allByCriteria?operatorCode="+BillPayProduct.operatorCode+"&serviceCategoryCode=100028&productMasterCode="+
                    BillPayProduct.productCode+"&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                           MyApplication.hideLoader();

                            if (jsonObject != null) {
                                productList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    productCategory = jsonObject;
                                    JSONArray walletOwnerListArr = productCategory.optJSONArray("productList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            productList.add(new ProductModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("description"),
                                                    data.optInt("maxValue"),
                                                    data.optInt("minValue"),
                                                    data.optInt("value"),
                                                    data.optString("modificationDate"),
                                                    data.optString("name"),
                                                    data.optString("operatorCode"),
                                                    data.optString("operatorName"),
                                                    data.optString("productMasterCode"),
                                                    data.optString("productTypeCode"),
                                                    data.optString("productTypeName"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceCategoryName"),
                                                    data.optString("state"),
                                                    data.optString("status"),
                                                    data.optString("vendorProductCode")

                                            ));

                                        }

                                        setData(productList);
                                    }

                                } else {
                                    MyApplication.showToast(billplanlistC,jsonObject.optString("resultDescription", "N/A"));
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

    private void setData(List<ProductModel> productList){
        PlanListAdapter planListAdapter = new PlanListAdapter(billplanlistC,productList);
        rvPlanList.setHasFixedSize(true);
        rvPlanList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvPlanList.setAdapter(planListAdapter);

    }

    public static String productCode,productTypeCode;
    public static int productValue;

    @Override
    public void onPlanListItemClick(String code, String typeCode, int value) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        productCode = code;
        productTypeCode = typeCode;
        productValue = value;
        Intent intent = new Intent(billplanlistC, BillPayDetails.class);
        startActivity(intent);
    }


}

package com.estel.cashmoovsubscriberapp.activity.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.MyQrCode;
import com.estel.cashmoovsubscriberapp.activity.NotificationList;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPayDetails;
import com.estel.cashmoovsubscriberapp.activity.setting.Profile;
import com.estel.cashmoovsubscriberapp.adapter.OperatorAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.OperatorListeners;
import com.estel.cashmoovsubscriberapp.model.OperatorModel;
import com.estel.cashmoovsubscriberapp.model.ServiceProviderModel;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Partner extends AppCompatActivity implements OperatorListeners {
    public static Partner partnerC;
    ImageView imgNotification,imgQR;
    RecyclerView rvOperator;
    RelativeLayout relMain;
    LinearLayout linProgress;
    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        partnerC=this;
        MyApplication.hideKeyboard(partnerC);
        setMenu();
        getIds();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.hideKeyboard(partnerC);
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
    
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.isFirstTime=false;
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
    private void setMenu() {
        imgNotification = findViewById(R.id.imgNotification);
        imgQR = findViewById(R.id.imgQR);

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(partnerC, NotificationList.class);
                startActivity(intent);
            }
        });
        imgQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(partnerC, MyQrCode.class);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        bottomBar = findViewById(R.id.bottomBar);
        rvOperator = findViewById(R.id.rvOperator);
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        relMain = findViewById(R.id.relMain);
        linProgress = findViewById(R.id.linProgress);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    MyApplication.isFirstTime=false;

//                    if(isTaskRoot()){
                        Intent i = new Intent(partnerC, MainActivity.class);
                        startActivity(i);
//                    }else{
//                       onBackPressed();
//                    }

                    //  finish();
                }
                if (bottomId == 1) {
//                    Intent i = new Intent(walletscreenC, WalletScreen.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 2) {
                        Intent i = new Intent(partnerC, Profile.class);
                        startActivity(i);

                    //  finish();
                }
                return true;
            }
        });

        callwalletOwner();
    }

    public static String serviceProvider,mobile,currency,currencySymbol;
    public static JSONObject serviceCategory = new JSONObject();

    public void callwalletOwner(){

        MyApplication.showloader(partnerC,"Please Wait...");
        API.GET("ewallet/api/v1/wallet/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if (jsonObject != null) {

                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                        if (jsonObject.has("walletList") && jsonObject.optJSONArray("walletList") != null) {
                            JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                            for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                JSONObject data = walletOwnerListArr.optJSONObject(i);
                                if (data.optString("walletTypeCode").equalsIgnoreCase("100008")) {
                                    mobile = data.optString("walletOwnerMsisdn");
                                    currency = data.optString("currencyName");
                                    currencySymbol = data.optString("currencySymbol");
                                }

                            }


                        }
                    }

                    }else{
                      MyApplication.showToast(partnerC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(partnerC,aFalse);
            }
        });

        callApioperatorProvider();

    }




    private void callApioperatorProvider() {
        try {

            API.GET("ewallet/api/v1/operator/allByCriteria?serviceCode=100001&serviceCategoryCode=100028&status=Y&offset=0&limit=200",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                operatorList.clear();
                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                  //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    serviceCategory = jsonObject;
                                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("operatorList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            operatorList.add(new OperatorModel(
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

                                        setData(operatorList);
                                    }

                                } else {
                                    MyApplication.showToast(partnerC,jsonObject.optString("resultDescription", "N/A"));
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

    private void setData(List<OperatorModel> operatorList){
        OperatorAdapter operatorAdapter = new OperatorAdapter(partnerC,operatorList);
        rvOperator.setHasFixedSize(true);
        rvOperator.setLayoutManager(new GridLayoutManager(this,3));
        rvOperator.setAdapter(operatorAdapter);
    }



    public static String operatorCode,operatorName;

    @Override
    public void onOperatorListItemClick(String code, String name) {
        operatorCode = code;
        operatorName = name;
        Intent intent = new Intent(partnerC, PartnerProduct.class);
        startActivity(intent);

    }
}
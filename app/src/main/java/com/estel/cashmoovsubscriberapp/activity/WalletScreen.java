package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.setting.Profile;
import com.estel.cashmoovsubscriberapp.adapter.MiniStatementTransAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.MiniStatemetListners;
import com.estel.cashmoovsubscriberapp.model.MiniStatement;
import com.estel.cashmoovsubscriberapp.model.MiniStatementTrans;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class WalletScreen extends AppCompatActivity implements View.OnClickListener, MiniStatemetListners {

    public static WalletScreen walletscreenC;
    private RecyclerView rv_mini_statement_trans;
    private List<MiniStatementTrans> miniStatementTransList = new ArrayList<>();
    private List<MiniStatement> miniStatementList = new ArrayList<>();
    private TextView tvCurrency,tvClick,tvBalance;
    SmoothBottomBar bottomBar;
    ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);
        walletscreenC = this;
        getIds();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
    }

    private void getIds() {
        imgQR = findViewById(R.id.imgQR);
        bottomBar = findViewById(R.id.bottomBar);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvClick = findViewById(R.id.tvClick);
        tvBalance = findViewById(R.id.tvBalance);
        rv_mini_statement_trans = findViewById(R.id.rv_mini_statement_trans);

        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    Intent i = new Intent(walletscreenC, MainActivity.class);
                    startActivity(i);
                  //  finish();
                }
                if (bottomId == 1) {
//                    Intent i = new Intent(walletscreenC, WalletScreen.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 2) {
                    Intent i = new Intent(walletscreenC, Profile.class);
                    startActivity(i);
                  //  finish();
                }
                return true;
            }
        });

        setOnCLickListener();
        callApiWalletList();

    }

    private void setOnCLickListener() {
        imgQR.setOnClickListener(walletscreenC);
        tvClick.setOnClickListener(walletscreenC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.imgQR:
                intent = new Intent(walletscreenC, MyQrCode.class);
                startActivity(intent);
                break;
            case R.id.tvClick:
                tvClick.setVisibility(View.GONE);
                tvBalance.setVisibility(View.VISIBLE);
        }
    }

    private void callApiWalletList() {
        try {
            MyApplication.showloader(walletscreenC,"Please wait!");
            API.GET("ewallet/api/v1/wallet/walletOwner/"+ MyApplication.getSaveString("walletOwnerCode", walletscreenC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("MiniStatement response======="+jsonObject.toString());
                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    miniStatementList.clear();
                                    if(jsonObject.has("walletList") &&jsonObject.optJSONArray("walletList")!=null){
                                        JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            if(data.optString("walletTypeCode").equalsIgnoreCase("100008")){
                                                tvCurrency.setText(getString(R.string.your_currency)+" : "+data.optString("currencyName"));
                                                tvBalance.setText(data.optString("value")+" "+data.optString("currencySymbol"));
                                                callApiMiniStatementTrans(data.optString("code"));
                                            }

                                        }


                                    }


                                } else {
                                    MyApplication.showToast(walletscreenC,jsonObject.optString("resultDescription"));
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

    private void callApiMiniStatementTrans(String walletCode) {
        try {
            MyApplication.showloader(walletscreenC,"Please wait!");
            API.GET("ewallet/api/v1/miniStatement/allByCriteria?"+"walletCode="+walletCode+"&offset=0"+"&limit=20",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                MyApplication.hideLoader();

                                miniStatementTransList.clear();
                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectMiniStatementTrans = jsonObject.optJSONObject("miniStatement");
                                    JSONArray miniStatementTransListArr = jsonObjectMiniStatementTrans.optJSONArray("walletTransactionList");
                                    if(miniStatementTransListArr!=null&& miniStatementTransListArr.length()>0){
                                        for (int i = 0; i < miniStatementTransListArr.length(); i++) {
                                            JSONObject data = miniStatementTransListArr.optJSONObject(i);
                                            miniStatementTransList.add(new MiniStatementTrans(data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("transactionId"),
                                                    data.optString("fromWalletOwnerCode").trim(),
                                                    data.optString("toWalletOwnerCode").trim(),
                                                    data.optString("fromWalletOwnerName").trim(),
                                                    data.optString("fromWalletOwnerMsisdn").trim(),
                                                    data.optString("fromWalletCode").trim(),
                                                    data.optString("fromWalletName").trim(),
                                                    data.optString("fromCurrencyCode").trim(),
                                                    data.optString("toCurrencyCode").trim(),
                                                    data.optString("fromCurrencyName").trim(),
                                                    data.optString("toCurrencyName").trim(),
                                                    data.optString("fromCurrencySymbol").trim(),
                                                    data.optString("toCurrencySymbol").trim(),
                                                    data.optString("transactionTypeCode").trim(),
                                                    data.optString("transactionTypeName").trim(),
                                                    data.optString("creationDate").trim(),
                                                    data.optString("comReceiveWalletCode").trim(),
                                                    data.optString("taxAsJson").trim(),
                                                    data.optString("holdingAccountCode").trim(),
                                                    data.optString("status").trim(),
                                                    data.optDouble("fromAmount"),
                                                    data.optDouble("toAmount"),
                                                    data.optDouble("comReceiveAmount"),
                                                    data.optDouble("srcPostBalance"),
                                                    data.optDouble("srcPreviousBalance"),
                                                    data.optDouble("destPreviousBalance"),
                                                    data.optDouble("destPostBalance"),
                                                    data.optDouble("commissionAmountForInstitute"),
                                                    data.optDouble("commissionAmountForAgent"),
                                                    data.optDouble("commissionAmountForBranch"),
                                                    data.optDouble("commissionAmountForMerchant"),
                                                    data.optDouble("commissionAmountForOutlet"),
                                                    data.optDouble("transactionAmount"),
                                                    data.optDouble("principalAmount"),
                                                    data.optString("fromWalletOwnerSurname").trim(),
                                                    data.optString("fromWalletTypeCode").trim(),
                                                    data.optBoolean("isReverse")));
                                        }

                                        setData(miniStatementTransList);

                                    }

                                } else {
                                    MyApplication.showToast(walletscreenC,jsonObject.optString("resultDescription"));
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

    private void setData(List<MiniStatementTrans> miniStatementTransList){
        MiniStatementTransAdapter miniStatementTransAdapter = new MiniStatementTransAdapter(walletscreenC,miniStatementTransList);
        rv_mini_statement_trans.setHasFixedSize(true);
        rv_mini_statement_trans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv_mini_statement_trans.setAdapter(miniStatementTransAdapter);
    }


    @Override
    public void onMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName, String currencySymbol, double fromAmount, String transactionId, String creationDate, String status) {
        Intent intent = new Intent(walletscreenC, WalletTransactionDetails.class);
        intent.putExtra("TRANSTYPE",transactionTypeName);
        intent.putExtra("FROMWALLETOWNERNAME",fromWalletOwnerName);
        intent.putExtra("FROMAMOUNT",currencySymbol+" "+fromAmount);
        intent.putExtra("TRANSID",transactionId);
        intent.putExtra("CREATIONDATE",creationDate);
        intent.putExtra("STATUS",status);
        startActivity(intent);
    }
}
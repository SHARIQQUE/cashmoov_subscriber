package com.estel.cashmoovsubscriberapp.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.MyQrCode;
import com.estel.cashmoovsubscriberapp.activity.NotificationList;
import com.estel.cashmoovsubscriberapp.activity.setting.Profile;
import com.estel.cashmoovsubscriberapp.adapter.MiniStatementTransAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.MiniStatemetListners;
import com.estel.cashmoovsubscriberapp.model.MiniStatement;
import com.estel.cashmoovsubscriberapp.model.MiniStatementTrans;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class WalletScreen extends AppCompatActivity implements View.OnClickListener, MiniStatemetListners {

    public static WalletScreen walletscreenC;
    private RecyclerView rv_mini_statement_trans;
    private List<MiniStatementTrans> miniStatementTransList = new ArrayList<>();
    private List<MiniStatement> miniStatementList = new ArrayList<>();
    LinearLayout linClick;
    private TextView tvCurrency,tvAccStatement,tvClick,tvBalance,tvView,tvViewHide;
    SmoothBottomBar bottomBar;
    ImageView imgNotification,imgQR;
    ImageView imgBack,imgHome;
    TextView tvRefresh;
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    int page = 0, limit = 20;
    String walletCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);
        setBackMenu();
        walletscreenC = this;
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
                MyApplication.hideKeyboard(walletscreenC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(walletscreenC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
//        tvView.setVisibility(View.VISIBLE);
//        tvViewHide.setVisibility(View.GONE);
//        rv_mini_statement_trans.setVisibility(View.GONE);
//        loadingPB.setVisibility(View.GONE);
//        tvRefresh.setVisibility(View.GONE);
    }

    private void getIds() {
//        imgNotification = findViewById(R.id.imgNotification);
//        imgQR = findViewById(R.id.imgQR);
        bottomBar = findViewById(R.id.bottomBar);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvAccStatement = findViewById(R.id.tvAccStatement);
        linClick = findViewById(R.id.linClick);
        tvClick = findViewById(R.id.tvClick);
        tvBalance = findViewById(R.id.tvBalance);
        tvRefresh = findViewById(R.id.tvRefresh);
        tvView = findViewById(R.id.tvView);
        tvViewHide = findViewById(R.id.tvViewHide);
        loadingPB = findViewById(R.id.loadingPB);
        nestedSV = findViewById(R.id.nestedSV);
        rv_mini_statement_trans = findViewById(R.id.rv_mini_statement_trans);
        tvBalance.setVisibility(View.VISIBLE);
        tvClick.setVisibility(View.GONE);

        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    MyApplication.isFirstTime=false;
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

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                   // page+=20;
                    limit+=20;
                    loadingPB.setVisibility(View.VISIBLE);
                    callApiMiniStatementTrans(walletCode,page,limit);
                }
            }
        });

        setOnCLickListener();

        callApiWalletList();

    }

    private void setOnCLickListener() {
//        imgNotification.setOnClickListener(walletscreenC);
//        imgQR.setOnClickListener(walletscreenC);
        tvAccStatement.setOnClickListener(walletscreenC);
        linClick.setOnClickListener(walletscreenC);
        tvRefresh.setOnClickListener(walletscreenC);
        tvView.setOnClickListener(walletscreenC);
        tvViewHide.setOnClickListener(walletscreenC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvAccStatement:
              /*  intent = new Intent(walletscreenC, TransactionDetailsActivity.class);
                startActivity(intent);*/
                break;
//            case R.id.imgNotification:
//                intent = new Intent(walletscreenC, NotificationList.class);
//                startActivity(intent);
//                break;
//            case R.id.imgQR:
//                intent = new Intent(walletscreenC, MyQrCode.class);
//                startActivity(intent);
//                break;
            case R.id.linClick:
                if(tvClick.isShown()) {
                    tvClick.setVisibility(View.GONE);
                    tvBalance.setVisibility(View.VISIBLE);
                }
                else{
                    tvClick.setVisibility(View.VISIBLE);
                    tvBalance.setVisibility(View.GONE);
                }
                break;
            case R.id.tvRefresh:
                page = 0;
                limit = 20;
                loadingPB.setVisibility(View.VISIBLE);
                callApiMiniStatementTrans(walletCode,page,limit);
                break;
            case R.id.tvView:
                 tvViewHide.setVisibility(View.VISIBLE);
                 tvView.setVisibility(View.GONE);
                 rv_mini_statement_trans.setVisibility(View.VISIBLE);
                 tvRefresh.setVisibility(View.VISIBLE);
                 loadingPB.setVisibility(View.VISIBLE);
                 callApiMiniStatementTrans(walletCode,page,limit);
                break;
            case R.id.tvViewHide:
                page=0;
                limit=20;
                tvView.setVisibility(View.VISIBLE);
                tvViewHide.setVisibility(View.GONE);
                rv_mini_statement_trans.setVisibility(View.GONE);
                tvRefresh.setVisibility(View.GONE);
                loadingPB.setVisibility(View.GONE);
                break;

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
                                                tvCurrency.setText(getString(R.string.your_currency)+" :  "+data.optString("currencyName"));
                                                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

                                                DecimalFormat df = new DecimalFormat("0.00",symbols);

                                                tvBalance.setText(df.format(data.optInt("value"))+" "+data.optString("currencySymbol"));
                                                walletCode = data.optString("code");

                                                System.out.println("get value"+data.optString("value"));
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

   // http://202.131.144.129:8081/ewallet/api/v1/miniStatement/allByCriteria?walletCode=1000030010&offset=0&limit=200

    private void callApiMiniStatementTrans(String walletCode, int page, int limit) {
        try {
           // MyApplication.showloader(walletscreenC,"Please wait!");
            API.GET("ewallet/api/v1/miniStatement/allByCriteria?"+"walletCode="+walletCode+"&offset="+page+"&limit="+limit,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                           // MyApplication.hideLoader();
                            if (jsonObject != null) {

                                miniStatementTransList.clear();
                                String msisdn,name;
                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectMiniStatementTrans = jsonObject.optJSONObject("miniStatement");
                                    JSONArray miniStatementTransListArr = jsonObjectMiniStatementTrans.optJSONArray("walletTransactionList");
                                    if(miniStatementTransListArr!=null&& miniStatementTransListArr.length()>0){
                                        for (int i = 0; i < miniStatementTransListArr.length(); i++) {
                                            JSONObject data = miniStatementTransListArr.optJSONObject(i);
                                            //if(data.optString("transactionTypeCode").equalsIgnoreCase("101441")){
                                            if(data.has("receiverCustomer")){
                                                msisdn = data.optJSONObject("receiverCustomer").optString("mobileNumber");
                                                name = data.optJSONObject("receiverCustomer").optString("firstName")+" "+data.optJSONObject("receiverCustomer").optString("lastName");
                                            }else{
                                                msisdn = data.optString("toWalletOwnerMsisdn").trim();
                                                name =  data.optString("toWalletOwnerName").trim();

                                            }

                                            miniStatementTransList.add(new MiniStatementTrans(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("transactionId"),
                                                    data.optString("fromWalletOwnerCode").trim(),
                                                    data.optString("toWalletOwnerCode").trim(),
                                                    data.optString("fromWalletOwnerName").trim(),
                                                    name,
                                                    data.optString("fromWalletOwnerMsisdn").trim(),
                                                    msisdn,
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
                                                    data.optString("taxAsJson"),
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
                                        loadingPB.setVisibility(View.GONE);
                                    }

                                } else {
                                    loadingPB.setVisibility(View.GONE);
                                    MyApplication.showToast(walletscreenC,jsonObject.optString("resultDescription"));
                                }

                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                          //  MyApplication.hideLoader();
                            loadingPB.setVisibility(View.GONE);

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
    public void onMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName, String walletOwnerMsisdn, String currencySymbol, double fromAmount, String transactionId, String creationDate, String status,double comReceiveAmount,String tax,double srcpostbalance) {
        String name="";
        if(fromWalletOwnerName.isEmpty()||fromWalletOwnerName==null){
            name = walletOwnerMsisdn;
        }else{
            name = fromWalletOwnerName+" ("+walletOwnerMsisdn+")";
        }
        Intent intent = new Intent(walletscreenC, WalletTransactionDetails.class);
        intent.putExtra("TRANSTYPE",transactionTypeName);
        intent.putExtra("FROMWALLETOWNERNAME",name);
        intent.putExtra("FROMAMOUNT",currencySymbol+" "+fromAmount);
        intent.putExtra("TRANSID",transactionId);
        intent.putExtra("CREATIONDATE",creationDate);
        intent.putExtra("STATUS",status);

        intent.putExtra("taxvalue",tax);
        intent.putExtra("newamount",comReceiveAmount);
        intent.putExtra("postbalance",srcpostbalance);

        startActivity(intent);
    }


}
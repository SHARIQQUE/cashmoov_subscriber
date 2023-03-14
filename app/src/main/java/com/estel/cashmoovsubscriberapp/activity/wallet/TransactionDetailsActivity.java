package com.estel.cashmoovsubscriberapp.activity.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.adapter.TransactionListAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.TransactionListLisners;
import com.estel.cashmoovsubscriberapp.model.TransactionModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransactionDetailsActivity extends LogoutAppCompactActivity implements TransactionListLisners {
    ImageView imgBack,imgHome;
    LinearLayout linPdf,linCsv,linExcel;
    private RecyclerView rvTransaction;
    private List<TransactionModel> transactionList = new ArrayList<>();
    public static TransactionDetailsActivity transactiondetailC;
    SearchView searchView;
    public String searchText = "";
    TransactionListAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        transactiondetailC=this;
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
                MyApplication.hideKeyboard(transactiondetailC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(transactiondetailC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        linPdf = findViewById(R.id.linPdf);
        linCsv = findViewById(R.id.linCsv);
        linExcel = findViewById(R.id.linExcel);
        searchView = findViewById(R.id.searchView);
        rvTransaction = findViewById(R.id.rvTransaction);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = searchView.getQuery().toString();
                setSearchResult(searchText);
                return false;
            }

        });

        searchView.setOnCloseListener(() -> {
            getTransactionList();
            return false;
        });

        linPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        linCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        linExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        getTransactionList();
    }

    private void getTransactionList() {

        MyApplication.showloader(transactiondetailC,getString(R.string.please_wait));
        API.GET("ewallet/api/v1/transaction/all?srcWalletOwnerCode="+MyApplication.getSaveString("walletOwnerCode",transactiondetailC)+"&offset=0&limit=5000", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                transactionList.clear();
                String taxName;

                if(jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    JSONArray dataArray = jsonObject.optJSONArray("transactionsList");
                    if(dataArray!=null&&dataArray.length()>0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            if(data.has("taxConfigurationList")){
                                JSONArray taxArray = data.optJSONArray("taxConfigurationList");
                                taxName = taxArray.optJSONObject(0).optString("taxTypeName");
                            }else{
                                taxName = "N/A";
                            }
                                transactionList.add(new TransactionModel(
                                        data.optInt("id"),
                                        data.optString("code"),
                                        data.optString("transactionId"),
                                        data.optString("transTypeCode"),
                                        data.optString("transTypeName"),
                                        data.optString("srcWalletOwnerCode"),
                                        data.optString("srcWalletOwnerName"),
                                        data.optString("desWalletOwnerCode"),
                                        data.optString("desWalletOwnerName"),
                                        data.optString("srcWalletCode"),
                                        data.optString("desWalletCode"),
                                        data.optString("srcCurrencyCode"),
                                        data.optString("srcCurrencyName"),
                                        data.optString("srcCurrencySymbol"),
                                        data.optString("desCurrencyCode"),
                                        data.optString("desCurrencyName"),
                                        data.optString("desCurrencySymbol"),
                                        data.optInt("transactionAmount"),
                                        data.optString("tax"),
                                        data.optString("resultCode"),
                                        data.optString("resultDescription"),
                                        data.optString("creationDate"),
                                        data.optString("createdBy"),
                                        data.optString("status"),
                                        data.optBoolean("transactionReversed"),
                                        data.optInt("srcMobileNumber"),
                                        data.optInt("destMobileNumber"),
                                        data.optBoolean("receiverBearer"),
                                        data.optString("rechargeNumber"),
                                        data.optInt("fee"),
                                        taxName,
                                        data.optInt("value"),
                                        data.optInt("srcPreBalance"),
                                        data.optInt("destPreBalance"),
                                        data.optInt("srcPostBalance"),
                                        data.optInt("destPostBalance")));

                        }

                        setData(transactionList);



                    }
                }else{
                    MyApplication.showToast(transactiondetailC,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(transactiondetailC,aFalse);
            }
        });
    }

    private void setData(List<TransactionModel> transactionList) {
        transactionListAdapter = new TransactionListAdapter(transactiondetailC,transactionList);
        rvTransaction.setHasFixedSize(true);
        rvTransaction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvTransaction.setAdapter(transactionListAdapter);

    }

    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    Date date = null;
    public void setSearchResult(String searchText) {
        ArrayList<TransactionModel> searchModels = new ArrayList<>();
        for (TransactionModel item : transactionList) {
            try {
                date = inputFormat.parse(item.getCreationDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(date);
            if (item.getTransactionId().contains(searchText.toLowerCase()) ||
                    item.getSrcWalletOwnerName().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getDesWalletOwnerName().toLowerCase().contains(searchText.toLowerCase()) ||
                    formattedDate.contains(searchText.toLowerCase()) ||
                    Integer.toString(item.getSrcMobileNumber()).contains(searchText.toLowerCase()) ||
                    Integer.toString(item.getDestMobileNumber()).contains(searchText.toLowerCase()) ||
                    item.getTransTypeName().toLowerCase().contains(searchText.toLowerCase()) ||
                    Integer.toString(item.getTransactionAmount()).contains(searchText.toLowerCase()) ||
                    item.getCreationDate().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getResultDescription().toLowerCase().contains(searchText.toLowerCase())) {
                searchModels.add(item);
            }
        }

        transactionListAdapter.onDataChange(searchModels);
    }

    @Override
    public void onTransactionViewItemClick(String transId, String transType, String transDate, String source, String destination, int sourceMsisdn, int destMsisdn, String symbol, int amount, int fee, String taxType, String tax, int postBalance, String status) {
        Dialog dialog = new Dialog(transactiondetailC, R.style.AppTheme);  //android.R.style.Theme_Translucent_NoTitleBar
        dialog.setContentView(R.layout.dialog_view_trans_details);

        //get ids
        TextView etTransId = dialog.findViewById(R.id.etTransId);
        TextView etTransType = dialog.findViewById(R.id.etTransType);
        TextView etTransDate = dialog.findViewById(R.id.etTransDate);
        TextView etSource = dialog.findViewById(R.id.etSource);
        TextView etDestination = dialog.findViewById(R.id.etDestination);
        TextView etSourcMSISDN = dialog.findViewById(R.id.etSourcMSISDN);
        TextView etDestMSISDN = dialog.findViewById(R.id.etDestMSISDN);
        TextView etAmount = dialog.findViewById(R.id.etAmount);
        TextView etFee = dialog.findViewById(R.id.etFee);
        TextView etTaxType = dialog.findViewById(R.id.etTaxType);
        TextView etTax = dialog.findViewById(R.id.etTax);
        TextView etPostBalance = dialog.findViewById(R.id.etPostBalance);
        TextView etStatus = dialog.findViewById(R.id.etStatus);

        //set values
        etTransId.setText(transId);
        etTransType.setText(transType);
        etSource.setText(source);
        etDestination.setText(destination);
        etSourcMSISDN.setText(String.valueOf(sourceMsisdn));
        etDestMSISDN.setText(String.valueOf(destMsisdn));
        etAmount.setText(symbol+" "+MyApplication.addDecimal(String.valueOf((amount))));
        etFee.setText(MyApplication.addDecimal(String.valueOf((fee))));
        etTaxType.setText(taxType);
        etTax.setText(MyApplication.addDecimal(String.valueOf((tax))));
        etPostBalance.setText(symbol+" "+MyApplication.addDecimal(String.valueOf((postBalance))));
        etStatus.setText(status);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = null;
            date = inputFormat.parse(transDate);
            String formattedDate = outputFormat.format(date);
            etTransDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dialog.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}


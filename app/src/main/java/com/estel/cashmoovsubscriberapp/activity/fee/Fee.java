package com.estel.cashmoovsubscriberapp.activity.fee;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fee extends AppCompatActivity implements View.OnClickListener {
    public static Fee feeC;
    ImageView imgBack,imgHome;
    JSONObject feeData;
    LinearLayout linMoneyTransfer,linAirtimePurhase,linBillPay,linPay,
            linCashWithdrawal,linRecRemittance;
    TextView rm_value,cash_value,pay_value,bp_value,map_value,m_value;
   // TextView tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        feeC=this;
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

    private void getIds() {
        linMoneyTransfer = findViewById(R.id.linMoneyTransfer);
        linAirtimePurhase = findViewById(R.id.linAirtimePurhase);
        linBillPay = findViewById(R.id.linBillPay);
        linPay = findViewById(R.id.linPay);
        linCashWithdrawal = findViewById(R.id.linCashWithdrawal);
        linRecRemittance = findViewById(R.id.linRecRemittance);

        //rm_value,cash_value,pay_value,bp_value,map_value,m_value;
        rm_value = findViewById(R.id.rm_value);
        cash_value = findViewById(R.id.cash_value);
        pay_value = findViewById(R.id.pay_value);
        bp_value = findViewById(R.id.bp_value);
        map_value = findViewById(R.id.map_value);
        m_value = findViewById(R.id.m_value);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        linMoneyTransfer.setOnClickListener(feeC);
        linAirtimePurhase.setOnClickListener(feeC);
        linBillPay.setOnClickListener(feeC);
        linPay.setOnClickListener(feeC);
        linCashWithdrawal.setOnClickListener(feeC);
        linRecRemittance.setOnClickListener(feeC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callApiFeeList();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linMoneyTransfer:
                showFeePopup(getString(R.string.money_transfer));
                break;
            case R.id.linAirtimePurhase:
                showOtherPopUp(getString(R.string.airtime_purchase),getString(R.string.airtime_purchase));
                break;
            case R.id.linBillPay:
                showOtherPopUp(getString(R.string.bill_payment),getString(R.string.bill_payment));
                break;
            case R.id.linPay:
                showOtherPopUp(getString(R.string.pay),getString(R.string.pay));
                break;
            case R.id.linCashWithdrawal:
                showOtherPopUp(getString(R.string.cash_withdrawal),getString(R.string.cash_withdrawal));
                break;
            case R.id.linRecRemittance:
                showOtherPopUp(getString(R.string.receive_remittance),getString(R.string.receive_remittance));
                break;

        }
    }

    public void showFeePopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_money_transfer);

        Button btnClose;
        TextView tvServiceName,txt1,txt2,txt3,txt1_value,txt2_value,txt3_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt3 = feeDialog.findViewById(R.id.txt3);
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);
        txt3_value = feeDialog.findViewById(R.id.txt3_value);

        if(feeData.optString("mode").equalsIgnoreCase("percent")){
            //feeData.put("value"
                    txt1_value.setText(feeData.optString("value")+" %");
            txt2_value.setText(feeData.optString("value")+" %");
            txt3_value.setText(feeData.optString("value")+" %");
        }else{
            txt1_value.setText(feeData.optString("value"));
            txt2_value.setText(feeData.optString("value"));
            txt3_value.setText(feeData.optString("value"));
        }
        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }


    public void showOtherPopUp(String serviceName,String service) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName,txt1,txt2,txt3,txt1_value,txt2_value,txt3_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(service);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt3 = feeDialog.findViewById(R.id.txt3);
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);
        txt3_value = feeDialog.findViewById(R.id.txt3_value);

        if(feeData.optString("mode").equalsIgnoreCase("percent")){
            //feeData.put("value"
            txt1_value.setText(feeData.optString("value")+" %");
            txt2_value.setText(feeData.optString("value")+" %");
            txt3_value.setText(feeData.optString("value")+" %");
        }else{
            txt1_value.setText(feeData.optString("value"));
            txt2_value.setText(feeData.optString("value"));
            txt3_value.setText(feeData.optString("value"));
        }
        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }


    JSONObject payFee;
    JSONObject receiveRemmitanceFee;
    JSONObject cashOutFee;
    JSONObject INTREMFee;
    private void callApiFeeList() {
        try {
            feeData=new JSONObject();
            payFee=new JSONObject();
            receiveRemmitanceFee=new JSONObject();
            cashOutFee=new JSONObject();
            INTREMFee=new JSONObject();
             MyApplication.showloader(feeC,"Please wait!");
            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+ MyApplication.getSaveString("walletOwnerCode", feeC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("Fee response======="+jsonObject.toString());
                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                                    JSONArray walletOwnerTemplateList=jsonObject.optJSONArray("walletOwnerTemplateList");
                                    JSONObject data=walletOwnerTemplateList.optJSONObject(0);

                                    JSONObject jsonObjectTestMain=new JSONObject();
                                    JSONArray jsonArrayMain=new JSONArray();
                                    int pos=0;
                                    if(data.has("feeTemplateList")){
                                        JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                        for (int i=0;i<feeTemplateList.length();i++){
                                            JSONObject fee=feeTemplateList.optJSONObject(i);

                                            try {
                                                JSONObject t = new JSONObject();
                                                if(jsonArrayMain.toString().contains(fee.optString("serviceName"))){

                                                    for(int j=0;j<jsonArrayMain.length();j++){
                                                        if(jsonArrayMain.optJSONObject(j).optString("ServiceName").equalsIgnoreCase(fee.optString("serviceName"))){
                                                            pos=j;
                                                        }
                                                    }
                                                    JSONArray dataArray=new JSONArray();
                                                    JSONObject dataObject=new JSONObject();
                                                    dataObject.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    dataObject.put("serviceCategoryName", fee.optString("serviceCategoryName"));
                                                    if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                        dataObject.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                                                    }else{
                                                        dataObject.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                                                    }
                                                    dataObject.put("calculationTypeName", fee.optString("calculationTypeName"));
                                                    dataObject.put("minValue", fee.optString("minValue"));
                                                    dataObject.put("maxValue", fee.optString("maxValue"));
                                                    dataArray.put(dataObject);
                                                    t.put("child",dataArray);
                                                    //jsonArrayMain.put(t);
                                                    jsonArrayMain.optJSONObject(pos).optJSONArray("child").put(dataObject);

                                                }else{
                                                    t.put("ServiceName", fee.optString("serviceName"));
                                                    JSONArray dataArray=new JSONArray();
                                                    JSONObject dataObject=new JSONObject();
                                                    dataObject.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    dataObject.put("serviceCategoryName", fee.optString("serviceCategoryName"));
                                                    if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                        dataObject.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                                                    }else{
                                                        dataObject.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                                                    }
                                                    dataObject.put("calculationTypeName", fee.optString("calculationTypeName"));
                                                    dataObject.put("minValue", fee.optString("minValue"));
                                                    dataObject.put("maxValue", fee.optString("maxValue"));
                                                    dataArray.put(dataObject);
                                                    t.put("child",dataArray);
                                                    jsonArrayMain.put(t);

                                                }




                                            }catch (Exception e){

                                            }

                                        }

                                        try {
                                            jsonObjectTestMain.put("data",jsonArrayMain);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        System.out.println("data  = = = "+jsonObjectTestMain.toString());


                                    }else{
                                         MyApplication.showToast(feeC,"Fee not assign");
                                    }



                                }
                                else {
                                     MyApplication.showToast(feeC,jsonObject.optString("resultDescription"));
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
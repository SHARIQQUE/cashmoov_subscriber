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
import com.estel.cashmoovsubscriberapp.model.GenderInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fee extends AppCompatActivity implements View.OnClickListener {
    public static Fee feeC;
    ImageView imgBack,imgHome;
    JSONObject feeData;
    public static JSONObject jsonObjectTestMain=null;
    LinearLayout linMoneyTransfer,linAirtimePurhase,linBillPay,linPay,
            linCashWithdrawal,linRecRemittance;
    TextView tvMoneyTransfer,tvFeeMoneyTransfer,tvAirtimePurchase,tvFeeAirtimePurchase,tvBillPayment,tvFeeBillPayment,
            tvPay,tvFeePay,tvCashWithdrawal,tvFeeCashWithdrawal,tvReceiveRemit,tvFeeReceiveRemit;

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
                MyApplication.isFirstTime=false;
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

        tvMoneyTransfer = findViewById(R.id.tvMoneyTransfer);
        tvFeeMoneyTransfer = findViewById(R.id.tvFeeMoneyTransfer);
        tvAirtimePurchase = findViewById(R.id.tvAirtimePurchase);
        tvFeeAirtimePurchase = findViewById(R.id.tvFeeAirtimePurchase);
        tvBillPayment = findViewById(R.id.tvBillPayment);
        tvFeeBillPayment = findViewById(R.id.tvFeeBillPayment);
        tvPay = findViewById(R.id.tvPay);
        tvFeePay = findViewById(R.id.tvFeePay);
        tvCashWithdrawal = findViewById(R.id.tvCashWithdrawal);
        tvFeeCashWithdrawal = findViewById(R.id.tvFeeCashWithdrawal);
        tvReceiveRemit = findViewById(R.id.tvReceiveRemit);
        tvFeeReceiveRemit = findViewById(R.id.tvFeeReceiveRemit);


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
                if(tvFeeMoneyTransfer.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showFeePopup(getString(R.string.money_transfer));
                }
                break;
            case R.id.linAirtimePurhase:
                if(tvFeeAirtimePurchase.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showAirtimePurchasePopup(getString(R.string.airtime_purchase));
                }
                break;
            case R.id.linBillPay:
                if(tvFeeBillPayment.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showBillPayPopup(getString(R.string.bill_payment));
                }
                break;
            case R.id.linPay:
                if(tvFeePay.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showPayPopup(getString(R.string.pay));
                }
                break;
            case R.id.linCashWithdrawal:
                if(tvFeeCashWithdrawal.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showCashWithdrawalPopup(getString(R.string.cash_withdrawal));
                }
                break;
            case R.id.linRecRemittance:
                if(tvFeeReceiveRemit.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showRecRemitPopup(getString(R.string.receive_remittance));
                }
                break;

        }
    }

    public void showFeePopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_money_transfer);

        Button btnClose;
        TextView tvServiceName, txt1, txt2, txt3, txt1_value, txt2_value, txt3_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt3 = feeDialog.findViewById(R.id.txt3);
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);
        txt3_value = feeDialog.findViewById(R.id.txt3_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Money Transfer")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Pay")) {
//                                txt1_value.setText(childData.optString("percentFeeValue"));
//                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("To Subscriber")) {

                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("To Non Subscriber")) {
                                txt2_value.setText(childData.optString("percentFeeValue"));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("International Remittance")) {
                                txt3_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Pay")) {
//                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
//                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("To Subscriber")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("To Non Subscriber")) {
                                txt2_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("International Remittance")) {
                                txt3_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Subscriber");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Non Subscriber");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","International");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


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


    public void showAirtimePurchasePopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.recharge_mobile));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Airtime Purchase")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Airtime Purchase");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

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

    public void showBillPayPopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.recharge_tv));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Bill Payment");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


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

    public void showPayPopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.pay));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Pay")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Pay")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Pay")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Pay");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

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

    public void showCashWithdrawalPopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.cash_withdrawal));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Cash PickUp")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash PickUp")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash PickUp")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Cash Withdrawal");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

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

    public void showRecRemitPopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.receive_remittance));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Receive")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Receive Money")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Receive Money")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feeC,FeeDetails.class);
                i.putExtra("FEEINTENT","Receive Remittance");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


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

                                    jsonObjectTestMain=new JSONObject();
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
                                            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
                                            for (int i = 0; i < FeeListArr.length(); i++) {
                                                JSONObject feeData = FeeListArr.optJSONObject(i);
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Money Transfer")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeMoneyTransfer.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeMoneyTransfer.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
//                                                    if(feeData.optJSONArray("child").optJSONObject(i).optString("serviceCategoryCode").equalsIgnoreCase("100057")){
//                                                        if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
//                                                            tvFeePay.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
//                                                        }
//                                                        if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
//                                                            tvFeePay.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
//                                                        }
//                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Airtime Purchase")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeAirtimePurchase.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeAirtimePurchase.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeBillPayment.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeBillPayment.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Pay")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeePay.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeePay.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Cash PickUp")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeCashWithdrawal.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeCashWithdrawal.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Receive")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeReceiveRemit.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeReceiveRemit.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }



                                            }



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
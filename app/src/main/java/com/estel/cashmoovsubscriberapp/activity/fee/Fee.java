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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.AirtimeFeeOperatorAdapter;
import com.estel.cashmoovsubscriberapp.adapter.BillPayFeeOperatorAdapter;
import com.estel.cashmoovsubscriberapp.adapter.OperatorAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.listners.OperatorAirtimeFeeListeners;
import com.estel.cashmoovsubscriberapp.listners.OperatorBillPayFeeListeners;
import com.estel.cashmoovsubscriberapp.model.FeeDetailModel;
import com.estel.cashmoovsubscriberapp.model.OperatorModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Fee extends AppCompatActivity implements View.OnClickListener, OperatorAirtimeFeeListeners, OperatorBillPayFeeListeners {
    public static Fee feeC;
    ImageView imgBack,imgHome;
    JSONObject feeData;
    public static JSONObject jsonObjectTestMain=null;
    public static JSONObject mainJsonObject=null;
    LinearLayout linMoneyTransfer,linAirtimePurhase,linBillPay,linPay,linCashOut,
            linCashWithdrawal,linRecRemittance;
    TextView tvMoneyTransfer,tvFeeMoneyTransfer,tvAirtimePurchase,tvFeeAirtimePurchase,tvBillPayment,tvFeeBillPayment,
            tvPay,tvFeePay,tvCashOut,tvFeeCashOut,tvCashWithdrawal,tvFeeCashWithdrawal,tvReceiveRemit,tvFeeReceiveRemit;

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
        linCashOut= findViewById(R.id.linCashOut);
        //linCashWithdrawal = findViewById(R.id.linCashWithdrawal);
        linRecRemittance = findViewById(R.id.linRecRemittance);

        tvMoneyTransfer = findViewById(R.id.tvMoneyTransfer);
        tvFeeMoneyTransfer = findViewById(R.id.tvFeeMoneyTransfer);
        tvAirtimePurchase = findViewById(R.id.tvAirtimePurchase);
        tvFeeAirtimePurchase = findViewById(R.id.tvFeeAirtimePurchase);
        tvBillPayment = findViewById(R.id.tvBillPayment);
        tvFeeBillPayment = findViewById(R.id.tvFeeBillPayment);
        tvPay = findViewById(R.id.tvPay);
        tvFeePay = findViewById(R.id.tvFeePay);
        tvCashOut = findViewById(R.id.tvCashOut);
        tvFeeCashOut = findViewById(R.id.tvFeeCashOut);
        //tvCashWithdrawal = findViewById(R.id.tvCashWithdrawal);
        //tvFeeCashWithdrawal = findViewById(R.id.tvFeeCashWithdrawal);
        tvReceiveRemit = findViewById(R.id.tvReceiveRemit);
        tvFeeReceiveRemit = findViewById(R.id.tvFeeReceiveRemit);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        linMoneyTransfer.setOnClickListener(feeC);
        linAirtimePurhase.setOnClickListener(feeC);
        linBillPay.setOnClickListener(feeC);
        linPay.setOnClickListener(feeC);
        linCashOut.setOnClickListener(feeC);
        //linCashWithdrawal.setOnClickListener(feeC);
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
            case R.id.linCashOut:
                if(tvFeeCashOut.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                }else{
                    showCashOutPopup(getString(R.string.cashout));
                }
                break;
//            case R.id.linCashWithdrawal:
//                if(tvFeeCashWithdrawal.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
//                    MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
//                }else{
//                    showCashWithdrawalPopup(getString(R.string.cash_withdrawal));
//                }
//                break;
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

    Dialog feeAirtimeDialog;
    public void showAirtimePurchasePopup(String serviceName) {
        feeAirtimeDialog = new Dialog(feeC);
        feeAirtimeDialog.setContentView(R.layout.popup_airtime_fee);

        Button btnClose;
        TextView tvServiceName;
        RecyclerView rvOperator;
        tvServiceName = feeAirtimeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        rvOperator = feeAirtimeDialog.findViewById(R.id.rvOperator);

        callApiAirtimeOperatorProvider(rvOperator);

        btnClose = feeAirtimeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeAirtimeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeAirtimeDialog.show();
    }

//    Dialog feeDialog;
//    public void showAirtimePurchasePopup(String serviceName) {
//        feeDialog = new Dialog(feeC);
//        feeDialog.setContentView(R.layout.popup_airtime_purchase);
//
//        Button btnClose;
//        TextView tvServiceName, txt1, txt2, txt3, txt4, txt1_value, txt2_value, txt3_value, txt4_value;
//        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
//        tvServiceName.setText(serviceName);
//        txt1 = feeDialog.findViewById(R.id.txt1);
//        txt2 = feeDialog.findViewById(R.id.txt2);
//        txt3 = feeDialog.findViewById(R.id.txt3);
//        txt4 = feeDialog.findViewById(R.id.txt4);
////        txt1.setText(getString(R.string.recharge_mobile));
////        txt1.setVisibility(View.VISIBLE);
//        txt1_value = feeDialog.findViewById(R.id.txt1_value);
//        txt2_value = feeDialog.findViewById(R.id.txt2_value);
//        txt3_value = feeDialog.findViewById(R.id.txt3_value);
//        txt4_value = feeDialog.findViewById(R.id.txt4_value);
//
//        LinearLayout lin1=feeDialog.findViewById(R.id.lin1);
//        LinearLayout lin2=feeDialog.findViewById(R.id.lin2);
//        LinearLayout lin3=feeDialog.findViewById(R.id.lin3);
//        LinearLayout lin4=feeDialog.findViewById(R.id.lin4);
//        lin1.setVisibility(View.GONE);
//
//
//
//        if (jsonObjectTestMain != null) {
//            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
//            for (int i = 0; i < FeeListArr.length(); i++) {
//                JSONObject feeData = FeeListArr.optJSONObject(i);
//
//                JSONArray ChildListArr = feeData.optJSONArray("child");
//                for (int j = 0; j < ChildListArr.length(); j++) {
//                    JSONObject childData = ChildListArr.optJSONObject(j);
//
//                    if (feeData.optString("ServiceName").equalsIgnoreCase("Airtime Purchase")) {
//                       /* if (childData.optString("serviceCategoryCode").equalsIgnoreCase("100021")) {
//                            System.out.println("productName==="+childData.optString("productName"));
//                            TextView textView1 = new TextView(this);
//                            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT));
//                            textView1.setText(childData.optString("productName"));
//                            textView1.setPadding(5,5,5,5);
//                            textView1.setTextColor(Color.parseColor("#000000"));
//                            textView1.setCompoundDrawablePadding(20);
//                            textView1.setTextSize(16);
//                            textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_outline_24,0,0,0);
//                            textView1.setTag("" + j);
//                            textView1.setClickable(true);//make your TextView Clickable
//                            textView1.setOnClickListener(btnClickListener);
//
//                            linearLayout.addView(textView1);
//                        }*/
//
//                        if (childData.optString("productCode").equalsIgnoreCase("100029")) {
//                            lin1.setVisibility(View.VISIBLE);
//                            txt1.setText(childData.optString("productName"));
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt1_value.setText(childData.optString("percentFeeValue"));
//                                }
//
//                            }
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt1_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
//                                }
//
//                            }
//                        }
//
//                        if (childData.optString("productCode").equalsIgnoreCase("100030")) {
//                            lin2.setVisibility(View.VISIBLE);
//                            txt2.setText(childData.optString("productName"));
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt2_value.setText(childData.optString("percentFeeValue"));
//                                }
//
//                            }
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt2_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
//                                }
//
//                            }
//                        }
//
//                        if (childData.optString("productCode").equalsIgnoreCase("100031")) {
//                            lin3.setVisibility(View.VISIBLE);
//                            txt3.setText(childData.optString("productName"));
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt3_value.setText(childData.optString("percentFeeValue"));
//                                }
//
//                            }
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt3_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
//                                }
//
//                            }
//                        }
//
//                        if (childData.optString("productCode").equalsIgnoreCase("ALLPRO")) {
//                            lin4.setVisibility(View.VISIBLE);
//                            txt4.setText(childData.optString("productName"));
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt4_value.setText(childData.optString("percentFeeValue"));
//                                }
//
//                            }
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
//                                    txt4_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
//                                }
//
//                            }
//                        }
//
//
//
//
//
//                    }
//                }
//            }
//        }
//
//
//        txt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Airtime Purchase");
//                i.putExtra("PRODUCTCODE","100029");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//        txt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Airtime Purchase");
//                i.putExtra("PRODUCTCODE","100030");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//        txt3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Airtime Purchase");
//                i.putExtra("PRODUCTCODE","100031");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//        txt4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Airtime Purchase");
//                i.putExtra("PRODUCTCODE","ALLPRO");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//
//        btnClose = feeDialog.findViewById(R.id.btnClose);
//        btnClose.setText(getString(R.string.close));
//        tvServiceName.setText(serviceName);
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                feeDialog.dismiss();
//            }
//        });
//        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        feeDialog.show();
//    }

//    View.OnClickListener btnClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            Log.d("btnClickListener",v.getTag()+"");
//            Toast.makeText(Fee.this, "TextView Clicked : "+v.getTag(),
//                    Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(feeC,FeeDetails.class);
//            i.putExtra("FEEINTENT","Airtime Purchase");
//            i.putExtra("FEEINTENTPOS",Integer.parseInt(v.getTag()+""));
//            startActivity(i);
//            feeDialog.dismiss();
//        }
//
//    };

    Dialog feeBillPayDialog;
    public void showBillPayPopup(String serviceName) {
        feeBillPayDialog = new Dialog(feeC);
        feeBillPayDialog.setContentView(R.layout.popup_airtime_fee);

        Button btnClose;
        TextView tvServiceName;
        RecyclerView rvOperator;
        tvServiceName = feeBillPayDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        rvOperator = feeBillPayDialog.findViewById(R.id.rvOperator);

        callBillPayOperatorProvider(rvOperator);

        btnClose = feeBillPayDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeBillPayDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeBillPayDialog.show();
    }

//    public void showBillPayPopup(String serviceName) {
//        Dialog feeDialog = new Dialog(feeC);
//        feeDialog.setContentView(R.layout.popup_airtime_purchase);
//
//        Button btnClose;
//        TextView tvServiceName, txt1, txt1_value;
//        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
//        tvServiceName.setText(serviceName);
//        txt1 = feeDialog.findViewById(R.id.txt1);
//        txt1.setText(getString(R.string.recharge_tv));
//        txt1_value = feeDialog.findViewById(R.id.txt1_value);
//
//        if (jsonObjectTestMain != null) {
//            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
//            for (int i = 0; i < FeeListArr.length(); i++) {
//                JSONObject feeData = FeeListArr.optJSONObject(i);
//
//                JSONArray ChildListArr = feeData.optJSONArray("child");
//                for (int j = 0; j < ChildListArr.length(); j++) {
//                    JSONObject childData = ChildListArr.optJSONObject(j);
//
//                    if (feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")) {
//                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//
//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
//                                txt1_value.setText(childData.optString("percentFeeValue"));
//                            }
//
//                        }
//                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
//                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//
//        txt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Bill Payment");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//
//
//        btnClose = feeDialog.findViewById(R.id.btnClose);
//        btnClose.setText(getString(R.string.close));
//        tvServiceName.setText(serviceName);
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                feeDialog.dismiss();
//            }
//        });
//        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        feeDialog.show();
//    }

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

    public void showCashOutPopup(String serviceName) {
        Dialog feeDialog = new Dialog(feeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.cashout));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Cash")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash Out")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash Out")) {
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
                i.putExtra("FEEINTENT","Cash Out");
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

//    public void showCashWithdrawalPopup(String serviceName) {
//        Dialog feeDialog = new Dialog(feeC);
//        feeDialog.setContentView(R.layout.popup_airtime_purchase);
//
//        Button btnClose;
//        TextView tvServiceName, txt1, txt1_value;
//        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
//        tvServiceName.setText(serviceName);
//        txt1 = feeDialog.findViewById(R.id.txt1);
//        txt1.setText(getString(R.string.cash_withdrawal));
//        txt1_value = feeDialog.findViewById(R.id.txt1_value);
//
//        if (jsonObjectTestMain != null) {
//            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
//            for (int i = 0; i < FeeListArr.length(); i++) {
//                JSONObject feeData = FeeListArr.optJSONObject(i);
//
//                JSONArray ChildListArr = feeData.optJSONArray("child");
//                for (int j = 0; j < ChildListArr.length(); j++) {
//                    JSONObject childData = ChildListArr.optJSONObject(j);
//
//                    if (feeData.optString("ServiceName").equalsIgnoreCase("Cash PickUp")) {
//                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//
//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash PickUp")) {
//                                txt1_value.setText(childData.optString("percentFeeValue"));
//                            }
//
//                        }
//                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
//                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash PickUp")) {
//                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//
//        txt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(feeC,FeeDetails.class);
//                i.putExtra("FEEINTENT","Cash Withdrawal");
//                startActivity(i);
//                feeDialog.dismiss();
//            }
//        });
//
//        btnClose = feeDialog.findViewById(R.id.btnClose);
//        btnClose.setText(getString(R.string.close));
//        tvServiceName.setText(serviceName);
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                feeDialog.dismiss();
//            }
//        });
//        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        feeDialog.show();
//    }

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
                                    int prdPos=0;
                                    if(data.has("feeTemplateList")){
                                        JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                        for (int i=0;i<feeTemplateList.length();i++){
                                            JSONObject fee=feeTemplateList.optJSONObject(i);

                                            try {
                                                JSONObject t = new JSONObject();
                                                if(jsonArrayMain.toString().contains(fee.optString("serviceCategoryCode"))){

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


                                                    JSONObject prodObj=new JSONObject();
                                                    JSONArray prodArr=new JSONArray();
                                                    if(fee.has("productCode")){
                                                        prodObj.put("productCode", fee.optString("productCode"));
                                                        dataObject.put("productCode", fee.optString("productCode"));
                                                    }
                                                    if(fee.has("productName")){
                                                        prodObj.put("productName", fee.optString("productName"));
                                                        dataObject.put("productName", fee.optString("productName"));
                                                    }
                                                    for(int j=0;j<jsonArrayMain.length();j++){
                                                        if(jsonArrayMain.optJSONObject(j).optString("serviceCategoryCode").equalsIgnoreCase(fee.optString("serviceCategoryCode"))){
                                                            pos=j;

                                                        }
                                                       /* for(int k=0;k<jsonArrayMain.optJSONObject(j).optJSONArray("child").length();k++){
                                                            JSONObject tt=jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k);
                                                            System.out.println("ttt"+tt.optString("productCode"));
                                                            System.out.println("ttt    fe"+fee.optString("productCode"));
                                                            if(tt.optString("productCode").isEmpty()){

                                                            }else {
                                                                if (tt.optString("productCode").equalsIgnoreCase
                                                                        (fee.optString("productCode"))) {
                                                                    prdPos = k;
                                                                    jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k)
                                                                            .optJSONArray("productArr").put(k + 1, prodObj);
                                                                }
                                                            }

                                                        }*/


                                                    }

                                                    prodArr.put(prodObj);

                                                    dataObject.put("productArr",prodArr);

                                                    dataArray.put(dataObject);
                                                    t.put("child",dataArray);
                                                    //jsonArrayMain.put(t);


                                                    jsonArrayMain.optJSONObject(pos).optJSONArray("child").put(dataObject);

                                                }else{
                                                    t.put("ServiceName", fee.optString("serviceName"));
                                                    t.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    t.put("serviceCategoryName", fee.optString("serviceCategoryName"));
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
                                                    JSONObject prodObj=new JSONObject();
                                                    JSONArray prodArr=new JSONArray();
                                                    if(fee.has("productCode")){
                                                        prodObj.put("productCode", fee.optString("productCode"));
                                                        dataObject.put("productCode", fee.optString("productCode"));
                                                    }
                                                    if(fee.has("productName")){
                                                        prodObj.put("productName", fee.optString("productName"));
                                                        dataObject.put("productName", fee.optString("productName"));
                                                    }

                                                    /*for(int j=0;j<jsonArrayMain.length();j++){
                                                        if(jsonArrayMain.optJSONObject(j).optString("ServiceName").equalsIgnoreCase(fee.optString("serviceName"))){
                                                            for(int k=0;k<jsonArrayMain.optJSONObject(j).optJSONArray("child").length();k++){
                                                                JSONObject jjj=jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k);
                                                                JSONArray pdArr=jjj.optJSONArray("productArr");
                                                                if(pdArr.length()>0){
                                                                    for(int a=0;a<pdArr.length();a++) {
                                                                        if(pdArr.optJSONObject(a).optString("productCode").equalsIgnoreCase(fee.optString("productCode"))){
                                                                            prdPos=a;
                                                                            jsonArrayMain.optJSONObject(pos).optJSONArray("child").optJSONObject(k)
                                                                                    .optJSONArray("productArr").put(a,prodObj);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }*/

                                                    prodArr.put(prodObj);
                                                    dataObject.put("productArr",prodArr);
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

                                           /* for(int i=0;i<FeeListArr.length();i++){
                                                JSONArray feeData = FeeListArr.optJSONObject(i).optJSONArray("child");

                                                for(int j=0;j<feeData.length();j++){
                                                    String prdcode=feeData.optJSONObject(j).optString("productCode");
                                                    for(int k=0;k<feeData.length();k++){
                                                      JSONObject kkk=feeData.optJSONObject(k);
                                                      if(kkk.optString("productCode").equalsIgnoreCase(prdcode)){
                                                          System.out.println("iuoui"+k);
                                                      }

                                                    }
                                                }

                                            }
*/                                            for (int i = 0; i < FeeListArr.length(); i++) {
                                                JSONObject feeData = FeeListArr.optJSONObject(i);
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Money Transfer")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeMoneyTransfer.setText("Paid Service");
                                                       // tvFeeMoneyTransfer.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeMoneyTransfer.setText("Paid Service");
                                                        //tvFeeMoneyTransfer.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
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
                                                        tvFeeAirtimePurchase.setText("Paid Service");
                                                       // tvFeeAirtimePurchase.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeAirtimePurchase.setText("Paid Service");
                                                       // tvFeeAirtimePurchase.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeBillPayment.setText("Paid Service");
                                                       // tvFeeBillPayment.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeBillPayment.setText("Paid Service");
                                                        //tvFeeBillPayment.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Pay")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeePay.setText("Paid Service");
                                                        //tvFeePay.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeePay.setText("Paid Service");
                                                        //tvFeePay.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Cash")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeCashOut.setText("Paid Service");
                                                        //tvFeePay.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeCashOut.setText("Paid Service");
                                                        //tvFeePay.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
//                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Cash PickUp")){
//                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
//                                                        tvFeeCashWithdrawal.setText("Paid Service");
//                                                        //tvFeeCashWithdrawal.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
//                                                    }
//                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
//                                                        tvFeeCashWithdrawal.setText("Paid Service");
//                                                        //tvFeeCashWithdrawal.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
//                                                    }
//                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Receive")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeReceiveRemit.setText("Paid Service");
                                                        //tvFeeReceiveRemit.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeReceiveRemit.setText("Paid Service");
                                                       // tvFeeReceiveRemit.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
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

    private ArrayList<OperatorModel> operatorAirtimeList = new ArrayList<>();
    private void callApiAirtimeOperatorProvider(RecyclerView rvOperator) {
        try {

            API.GET("ewallet/api/v1/operator/allByCriteria?serviceCode=100009&serviceCategoryCode=100021&status=Y&offset=0&limit=200",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                operatorAirtimeList.clear();
                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("operatorList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            operatorAirtimeList.add(new OperatorModel(
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
                                        AirtimeFeeOperatorAdapter airtimeFeeOperatorAdapter = new AirtimeFeeOperatorAdapter(feeC,operatorAirtimeList);
                                        rvOperator.setHasFixedSize(true);
                                        rvOperator.setLayoutManager(new LinearLayoutManager(feeC, LinearLayoutManager.VERTICAL,false));
                                        rvOperator.setAdapter(airtimeFeeOperatorAdapter);
                                    }

                                } else {
                                    MyApplication.showToast(feeC,jsonObject.optString("resultDescription", "N/A"));
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

    private ArrayList<OperatorModel> operatorBillPayList = new ArrayList<>();
    private void callBillPayOperatorProvider(RecyclerView rvOperator) {
        try {

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
                                        BillPayFeeOperatorAdapter billPayFeeOperatorAdapter = new BillPayFeeOperatorAdapter(feeC,operatorBillPayList);
                                        rvOperator.setHasFixedSize(true);
                                        rvOperator.setLayoutManager(new LinearLayoutManager(feeC, LinearLayoutManager.VERTICAL,false));
                                        rvOperator.setAdapter(billPayFeeOperatorAdapter);
                                    }

                                } else {
                                    MyApplication.showToast(feeC,jsonObject.optString("resultDescription", "N/A"));
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



    @Override
    public void onOperatorAirtimeFeeListItemClick(String code, String name) {
        callAirtimeFee(code);
        feeAirtimeDialog.dismiss();
    }

    @Override
    public void onOperatorBillPayFeeListItemClick(String code, String name) {
        callBillPayFee(code);
        feeBillPayDialog.dismiss();
    }

    private void callAirtimeFee(String operatorCode) {
        try {

            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+MyApplication.getSaveString("walletOwnerCode", feeC)+"/"+operatorCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    mainJsonObject = new JSONObject();
                                    mainJsonObject = jsonObject;
                                    JSONArray walletOwnerTemplateList=jsonObject.optJSONArray("walletOwnerTemplateList");
                                    if(walletOwnerTemplateList!=null&& walletOwnerTemplateList.length()>0) {
                                        JSONObject data=walletOwnerTemplateList.optJSONObject(0);

                                        if(data.has("feeTemplateList")){
                                            JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                            for (int i=0;i<feeTemplateList.length();i++){
                                                JSONObject feeObj=feeTemplateList.optJSONObject(i);
                                                    if(feeObj.has("calculationTypeName")) {
                                                        Intent in = new Intent(feeC,FeeDetails.class);
                                                        in.putExtra("FEEINTENT","Airtime Purchase");
                                                        //in.putExtra("OPERATORCODE",operatorCode);
                                                        startActivity(in);
                                                    }else{
                                                        MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                                                    }

                                            }


                                        }else{
                                            MyApplication.showToast(feeC,getString(R.string.range_value_not_available));
                                        }
                                    }


                                } else {
                                    MyApplication.showToast(feeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callBillPayFee(String operatorCode) {
        try {

            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+MyApplication.getSaveString("walletOwnerCode", feeC)+"/"+operatorCode,
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

                                            Intent i = new Intent(feeC,FeeDetails.class);
                                            i.putExtra("FEEINTENT","Bill Payment");
                                            //i.putExtra("OPERATORCODE",operatorCode);
                                            startActivity(i);
                                        }
                                    }


                                } else {
                                    MyApplication.showToast(feeC,jsonObject.optString("resultDescription", "N/A"));
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
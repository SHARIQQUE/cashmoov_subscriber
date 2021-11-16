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
                                    if(data.has("feeTemplateList")){
                                        JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                        for (int i=0;i<feeTemplateList.length();i++){
                                            JSONObject fee=feeTemplateList.optJSONObject(i);
                                          /*  //PAY
                                            if(fee.optString("serviceCategoryCode").equalsIgnoreCase("100057")){
                                                if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    try {
                                                        payFee.optString("mode","percent");
                                                        payFee.optString("value",fee.optString("percentFeeValue"));
                                                        pay_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                    }catch (Exception e){

                                                    }
                                                }else{
                                                    try {
                                                       // pay_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                        pay_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        payFee.optString("mode","fixed");
                                                        payFee.optString("value",fee.optString("fixedFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }
                                            }else{
                                                try {

                                                    pay_value.setText("N/A");
                                                    payFee.optString("mode","fixed");
                                                    payFee.optString("value","N/A");
                                                }catch (Exception e){

                                                }
                                            }

                                            //Cash PickUp
                                            if(fee.optString("serviceCategoryCode").equalsIgnoreCase("CSHPIC")){
                                                if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    try {
                                                        rm_value.setText(fee.optString("percentFeeValue")+"% on the transaction");

                                                        receiveRemmitanceFee.optString("mode","percent");
                                                        receiveRemmitanceFee.optString("value",fee.optString("percentFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }else{
                                                    try {
                                                       // rm_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                        rm_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        //rm_value.setText("N/A");
                                                        receiveRemmitanceFee.optString("mode","fixed");
                                                        receiveRemmitanceFee.optString("value",fee.optString("fixedFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }
                                            }else{
                                                try {
                                                    //rm_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                    //rm_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                    rm_value.setText("N/A");
                                                    receiveRemmitanceFee.optString("mode","fixed");
                                                    receiveRemmitanceFee.optString("value","N/A");
                                                }catch (Exception e){

                                                }
                                            }

                                            //Cash Out
                                            if(fee.optString("serviceCategoryCode").equalsIgnoreCase("100012")){
                                                if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    try {
                                                        cash_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                       // cash_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        //cash_value.setText("N/A");
                                                        cashOutFee.optString("mode","percent");
                                                        cashOutFee.optString("value",fee.optString("percentFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }else{
                                                    try {
                                                       // cash_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                        cash_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        //cash_value.setText("N/A");
                                                        cashOutFee.optString("mode","fixed");
                                                        cashOutFee.optString("value",fee.optString("fixedFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }
                                            }else{
                                                try {
                                                    //cash_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                    //cash_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                    cash_value.setText("N/A");
                                                    cashOutFee.optString("mode","fixed");
                                                    cashOutFee.optString("value","N/A");
                                                }catch (Exception e){

                                                }
                                            }


                                            //INTREM
                                            if(fee.optString("serviceCategoryCode").equalsIgnoreCase("INTREM")){
                                                if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    try {
                                                        m_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                        //m_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        //m_value.setText("N/A");
                                                        INTREMFee.optString("mode","percent");
                                                        INTREMFee.optString("value",fee.optString("percentFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }else{
                                                    try {
                                                        //m_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                        m_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                        //m_value.setText("N/A");
                                                        INTREMFee.optString("mode","fixed");
                                                        INTREMFee.optString("value",fee.optString("fixedFeeValue"));
                                                    }catch (Exception e){

                                                    }
                                                }
                                            }else{
                                                try {
                                                    //m_value.setText(fee.optString("percentFeeValue")+"% on the transaction");
                                                    //m_value.setText(fee.optString("fixedFeeValue")+" fixed on the transaction");
                                                    m_value.setText("N/A");
                                                    INTREMFee.optString("mode","fixed");
                                                    INTREMFee.optString("value","N/A");
                                                }catch (Exception e){

                                                }
                                            }
*/

                                            if(fee.optString("serviceCode").equalsIgnoreCase("100020")){
                                                //assign all
                                                if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    //percentage code
                                                    try {
                                                        //rm_value,cash_value,pay_value,bp_value,map_value,m_value;
                                                        feeData.put("mode","percent");
                                                        feeData.put("value",fee.optString("percentFeeValue"));
                                                        rm_value.setText(feeData.optString("value")+"% on the transaction");
                                                        cash_value.setText(feeData.optString("value")+"% on the transaction");
                                                        pay_value.setText(feeData.optString("value")+"% on the transaction");
                                                        bp_value.setText(feeData.optString("value")+"% on the transaction");
                                                        map_value.setText(feeData.optString("value")+"% on the transaction");
                                                        m_value.setText(feeData.optString("value")+"% on the transaction");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }else{
                                                    //fixed
                                                    try {
                                                        feeData.put("mode","fixed");
                                                        feeData.put("value",fee.optString("fixedFeeValue"));
                                                        rm_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        cash_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        pay_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        bp_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        map_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        m_value.setText(feeData.optString("value")+" fixed on the transaction");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                return;

                                            }
                                            else{
                                                try {
                                                    feeData.put("mode","fixed");
                                                    feeData.put("value","N/A");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                rm_value.setText("N/A");
                                                cash_value.setText("N/A");
                                                pay_value.setText("N/A");
                                                bp_value.setText("N/A");
                                                map_value.setText("N/A");
                                                m_value.setText("N/A");
                                               if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                    //percentage code
                                                    try {
                                                        //rm_value,cash_value,pay_value,bp_value,map_value,m_value;
                                                        feeData.put("mode","percent");
                                                        feeData.put("value",fee.optString("percentFeeValue"));
                                                        rm_value.setText(feeData.optString("value")+"% on the transaction");
                                                        cash_value.setText(feeData.optString("value")+"% on the transaction");
                                                        pay_value.setText(feeData.optString("value")+"% on the transaction");
                                                        bp_value.setText(feeData.optString("value")+"% on the transaction");
                                                        map_value.setText(feeData.optString("value")+"% on the transaction");
                                                        m_value.setText(feeData.optString("value")+"% on the transaction");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }else{
                                                    //fixed
                                                    try {
                                                        feeData.put("mode","fixed");
                                                        feeData.put("value",fee.optString("fixedFeeValue"));
                                                        rm_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        cash_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        pay_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        bp_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        map_value.setText(feeData.optString("value")+" fixed on the transaction");
                                                        m_value.setText(feeData.optString("value")+" fixed on the transaction");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                        }

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

    public void assignall(){

    }

}
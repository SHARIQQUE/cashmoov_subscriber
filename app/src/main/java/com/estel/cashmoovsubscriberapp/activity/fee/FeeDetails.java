package com.estel.cashmoovsubscriberapp.activity.fee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.FeeDetailsAdapter;
import com.estel.cashmoovsubscriberapp.model.FeeDetailModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FeeDetails extends AppCompatActivity implements View.OnClickListener {
    public static FeeDetails feedetailsC;
    // ImageView imgBack;
    Button btnClose;
    public static TextView tvName;
    String checkIntent,checkOperatorCodeIntent;
    int pos;
    ArrayList<FeeDetailModel>feeDetailModelArrayList= new ArrayList<>();
    RecyclerView rvFeeDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_details);
        feedetailsC=this;
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
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }

    private void getIds() {
        rvFeeDetail = findViewById(R.id.rvFeeDetail);
        tvName = findViewById(R.id.tvName);
        btnClose = findViewById(R.id.btnClose);

        if (getIntent().getExtras() != null) {
            checkIntent = (getIntent().getStringExtra("FEEINTENT"));
           // checkOperatorCodeIntent = (getIntent().getStringExtra("OPERATORCODE"));
            /*if((getIntent().getIntExtra("FEEINTENTPOS",-1)!=-1)){
                pos=(getIntent().getIntExtra("FEEINTENTPOS",-1));
            }*/
            tvName.setText(checkIntent);
            System.out.println("get val"+checkIntent);
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.subscriber))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100024")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));


                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                     }

                    }

                    setData(feeDetailModelArrayList);
                        //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

                }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.non_subscriber))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("NONSUB")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.international))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("INTREM")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.airtime_purchase))){
            feeDetailModelArrayList.clear();
            if (AirtimeFeeActivity.mainJsonObject != null) {
                JSONArray FeeListArr = AirtimeFeeActivity.mainJsonObject.optJSONArray("walletOwnerTemplateList");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                        JSONArray ChildListArr = feeData.optJSONArray("feeTemplateList");
                        for (int j = 0; j < ChildListArr.length(); j++) {
                            JSONObject childData = ChildListArr.optJSONObject(j);

                            if (childData.optString("calculationTypeCode").equalsIgnoreCase("100002")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                                "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                        childData.optString("percentFeeValue")+"%"
                                ));
                            } else {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                                "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        if(checkIntent.equalsIgnoreCase(getString(R.string.bill_payment))){
            feeDetailModelArrayList.clear();
            if (BillPayFeeActivity.mainJsonObject != null) {
                JSONArray FeeListArr = BillPayFeeActivity.mainJsonObject.optJSONArray("walletOwnerTemplateList");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("feeTemplateList");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("percentFeeValue")+"%"
                            ));
                        } else {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("fixedFeeValue")
                            ));
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        if(checkIntent.equalsIgnoreCase(getString(R.string.pay))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100057")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.cashout))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100012")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

//        if(checkIntent.equalsIgnoreCase("Cash Withdrawal")){
//            feeDetailModelArrayList.clear();
//            if (Fee.jsonObjectTestMain != null) {
//                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
//                for (int i = 0; i < FeeListArr.length(); i++) {
//                    JSONObject feeData = FeeListArr.optJSONObject(i);
//
//                    JSONArray ChildListArr = feeData.optJSONArray("child");
//                    for (int j = 0; j < ChildListArr.length(); j++) {
//                        JSONObject childData = ChildListArr.optJSONObject(j);
//
//                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("CSHPIC")){
//                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
//                                feeDetailModelArrayList.add(new FeeDetailModel(
//                                        String.format("%.2f",childData.optDouble("minValue"))+"  -  "+
//                                                String.format("%.2f",childData.optDouble("maxValue")),
//                                        childData.optString("percentFeeValue")
//                                ));
//                            }else{
//                                feeDetailModelArrayList.add(new FeeDetailModel(
//                                        String.format("%.2f",childData.optDouble("minValue"))+"  -  "+
//                                                String.format("%.2f",childData.optDouble("maxValue")),
//                                        childData.optString("fixedFeeValue")
//                                ));
//                            }
//                        }
//                    }
//
//                }
//
//                setData(feeDetailModelArrayList);
//                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());
//
//            }
//        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.receive_remittance))){
            feeDetailModelArrayList.clear();
            if (Fee.jsonObjectTestMain != null) {
                JSONArray FeeListArr = Fee.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                    //if(childData.optString("serviceCategoryCode").equalsIgnoreCase("REMON")){
                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("CSHPIC")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                               MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }

                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(feedetailsC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }

    private void setData(List<FeeDetailModel> feeDetailModelArrayList){
        FeeDetailsAdapter feeDetailsAdapter = new FeeDetailsAdapter(feedetailsC,feeDetailModelArrayList);
        rvFeeDetail.setHasFixedSize(true);
        rvFeeDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvFeeDetail.setAdapter(feeDetailsAdapter);
    }


}



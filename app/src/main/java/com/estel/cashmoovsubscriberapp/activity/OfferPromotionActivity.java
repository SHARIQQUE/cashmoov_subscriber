package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.OfferPromotionAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.NotificationModel;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OfferPromotionActivity extends AppCompatActivity{
    public static OfferPromotionActivity offerpromotionC;
    ImageView imgBack, imgHome;
    RecyclerView rv_offer_promotion;
    private List<NotificationModel> notificationModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_promotion);
        offerpromotionC = this;
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
                MyApplication.isFirstTime = false;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rv_offer_promotion = findViewById(R.id.rv_offer_promotion);

        gettempalteList();

    }


    private void gettempalteList() {

         MyApplication.showloader(offerpromotionC,"Please Wait...");
        API.GET_WF("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/" + MyApplication.getSaveString("walletOwnerCode", offerpromotionC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
               // MyApplication.hideLoader();


                if (jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    JSONArray dataArray = jsonObject.optJSONArray("walletOwnerTemplateList");

                    Set<Integer> set = new HashSet<>();

                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            if (data.optString("templateCategoryCode").equalsIgnoreCase("100013")) {
                                set.add(0);
                                getPromotionList(data.optString("templateCode"));
                            }
                        }
                        if (!set.contains(0)) {
                            MyApplication.hideLoader();
                            MyApplication.showToast(offerpromotionC,getString(R.string.no_promo_found));

                        }

                    }
                } else {
                    MyApplication.hideLoader();
                    MyApplication.showToast(offerpromotionC, jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(offerpromotionC, aFalse);
            }
        });
    }

    ArrayList<OfferPromotionModel> offerPromotionModelArrayList;

    private void getPromotionList(String code) {
//http://202.131.144.130:8081/ewallet/api/v1/promOfferTemplate/101235
        //Bhawesh, 15:02
        //http://192.168.1.171:8081/ewallet/api/v1/promOfferTemplate/allByCriteria?templateCode=101235&status=Y&state=A
        // MyApplication.showloader(mainC,"Please Wait...");
        API.GET_WF("ewallet/api/v1/promOfferTemplate/allByCriteria?templateCode=" + code + "&status=Y&state=A", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                offerPromotionModelArrayList = new ArrayList<>();

                offerPromotionModelArrayList.clear();

                if (jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    JSONArray dataArray = jsonObject.optJSONArray("promOfferTemplateList");
                  /*  if(offerPromotionModelArrayList.size()==dataArray.length()){
                        return;
                    }*/
                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            offerPromotionModelArrayList.add(new OfferPromotionModel(
                                    data.optInt("id"),
                                    data.optString("code"),
                                    data.optString("templateCode"),
                                    data.optString("templateName"),
                                    data.optString("serviceCode"),
                                    data.optString("serviceName"),
                                    data.optString("serviceCategoryCode"),
                                    data.optString("serviceCategoryName"),
                                    data.optString("serviceProviderCode"),
                                    data.optString("serviceProviderName"),
                                    data.optString("fromDate"),
                                    data.optString("toDate"),
                                    data.optString("promOfferTypeCode"),
                                    data.optString("promOfferTypeName"),
                                    data.optString("profileTypeCode"),
                                    data.optString("profileTypeName"),
                                    data.optString("description"),
                                    data.optString("heading"),
                                    data.optString("subHeading"),
                                    data.optString("fileName"),
                                    data.optString("subHeading"),
                                    data.optString("state"),
                                    data.optString("createdBy"),
                                    data.optString("creationDate"),
                                    data.optString("modificationDate")));

                        }

                        rv_offer_promotion.setHasFixedSize(true);
                        rv_offer_promotion.setLayoutManager(new LinearLayoutManager(offerpromotionC, LinearLayoutManager.VERTICAL, false));
                        OfferPromotionAdapter offerPromotionAdapter = new OfferPromotionAdapter(offerpromotionC, offerPromotionModelArrayList);
                        rv_offer_promotion.setAdapter(offerPromotionAdapter);

                       // offerPromotionAdapter.notifyDataSetChanged();


                    }
                } else {
                    MyApplication.hideLoader();
                     MyApplication.showToast(offerpromotionC,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(offerpromotionC, aFalse);
            }
        });
    }
}
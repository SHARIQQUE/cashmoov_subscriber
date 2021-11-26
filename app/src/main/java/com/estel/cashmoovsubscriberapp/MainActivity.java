package com.estel.cashmoovsubscriberapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.activity.MyQrCode;
import com.estel.cashmoovsubscriberapp.activity.NotificationList;
import com.estel.cashmoovsubscriberapp.activity.partner.Partner;
import com.estel.cashmoovsubscriberapp.activity.setting.Profile;
import com.estel.cashmoovsubscriberapp.activity.wallet.WalletScreen;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.AirtimePurchase;
import com.estel.cashmoovsubscriberapp.activity.cashwithdrawal.CashWithdrawal;
import com.estel.cashmoovsubscriberapp.activity.fee.Fee;
import com.estel.cashmoovsubscriberapp.activity.moneytransfer.MoneyTransfer;
import com.estel.cashmoovsubscriberapp.activity.pay.Pay;
import com.estel.cashmoovsubscriberapp.activity.receiveremittance.ReceiveRemittance;
import com.estel.cashmoovsubscriberapp.activity.rechargeandpayments.BillPay;
import com.estel.cashmoovsubscriberapp.activity.servicepoint.ServicePoint;
import com.estel.cashmoovsubscriberapp.adapter.OfferPromotionAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static MainActivity mainC;
    SmoothBottomBar bottomBar;
    ImageView imgNotification,imgQR;
    CircleImageView imgProfile;
    LinearLayout linClick;
    TextView tvClick,tvBalance;
    CardView cardMoneyTransfer,cardAirtimePurchase,cardRechargePayment,cardPay,
    cardCashWithdrawal,cardRecRemittance,cardFee,cardServicePoints;
    RecyclerView rv_offer_promotion;
    ArrayList<OfferPromotionModel> offerPromotionModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainC = this;
        MyApplication.setLang(mainC);
        getIds();
    }

    @Override
    protected void onStart() {
        super.onStart();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.souarecashmoov)
                .error(R.drawable.souarecashmoov);


        String ImageName=MyApplication.getSaveString("ImageName", mainC);
        if(ImageName!=null&&ImageName.length()>1) {
            String image_url = MyApplication.ImageURL + ImageName;
            Glide.with(this).load(image_url).apply(options).into(imgProfile);
        }

        callApiWalletList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
    }

    private void getIds() {
        imgNotification = findViewById(R.id.imgNotification);
        imgQR = findViewById(R.id.imgQR);
        imgProfile = findViewById(R.id.imgProfile);
        linClick = findViewById(R.id.linClick);
        tvClick = findViewById(R.id.tvClick);
        tvBalance = findViewById(R.id.tvBalance);
        bottomBar = findViewById(R.id.bottomBar);
        cardMoneyTransfer = findViewById(R.id.cardMoneyTransfer);
        cardAirtimePurchase = findViewById(R.id.cardAirtimePurchase);
        cardRechargePayment = findViewById(R.id.cardRechargePayment);
        cardPay = findViewById(R.id.cardPay);
        cardCashWithdrawal = findViewById(R.id.cardCashWithdrawal);
        cardRecRemittance = findViewById(R.id.cardRecRemittance);
        cardFee = findViewById(R.id.cardFee);
        cardServicePoints = findViewById(R.id.cardServicePoints);
        rv_offer_promotion = findViewById(R.id.rv_offer_promotion);

        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));


        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                Log.e("PositionMain--",""+bottomId);

                if (bottomId == 0) {
//                    Intent i = new Intent(mainC, MainActivity.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 1) {
                    Intent i = new Intent(mainC, Partner.class);
                    startActivity(i);
                   // finish();
                }
                if (bottomId == 2) {
                    Intent i = new Intent(mainC, Profile.class);
                    startActivity(i);
                   // finish();
                }
                return false;
            }
        });

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        imgNotification.setOnClickListener(mainC);
        imgQR.setOnClickListener(mainC);
        imgProfile.setOnClickListener(mainC);
        linClick.setOnClickListener(mainC);
        cardMoneyTransfer.setOnClickListener(mainC);
        cardAirtimePurchase.setOnClickListener(mainC);
        cardRechargePayment.setOnClickListener(mainC);
        cardPay.setOnClickListener(mainC);
        cardCashWithdrawal.setOnClickListener(mainC);
        cardRecRemittance.setOnClickListener(mainC);
        cardFee.setOnClickListener(mainC);
        cardServicePoints.setOnClickListener(mainC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.imgNotification:
                intent = new Intent(mainC, NotificationList.class);
                startActivity(intent);
                break;
            case R.id.imgQR:
                intent = new Intent(mainC, MyQrCode.class);
                startActivity(intent);
                break;
            case R.id.imgProfile:
                intent = new Intent(mainC, Profile.class);
                startActivity(intent);
                break;
            case R.id.linClick:
                intent = new Intent(mainC, WalletScreen.class);
                startActivity(intent);
//                if(tvClick.isShown()) {
//                    tvClick.setVisibility(View.GONE);
//                    tvBalance.setVisibility(View.VISIBLE);
//                }
//                else{
//                    tvClick.setVisibility(View.VISIBLE);
//                    tvBalance.setVisibility(View.GONE);
//                }
                break;
            case R.id.cardMoneyTransfer:
                intent = new Intent(mainC, MoneyTransfer.class);
                startActivity(intent);
                break;
            case R.id.cardAirtimePurchase:
                intent = new Intent(mainC, AirtimePurchase.class);
                startActivity(intent);
                break;
            case R.id.cardRechargePayment:
                intent = new Intent(mainC, BillPay.class);
                startActivity(intent);
                break;
            case R.id.cardPay:
                intent = new Intent(mainC, Pay.class);
                startActivity(intent);
                //Toast.makeText(mainC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardCashWithdrawal:
                intent = new Intent(mainC, CashWithdrawal.class);
                startActivity(intent);
                //Toast.makeText(mainC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardRecRemittance:
                intent = new Intent(mainC, ReceiveRemittance.class);
                startActivity(intent);
                //Toast.makeText(mainC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardFee:
                intent = new Intent(mainC, Fee.class);
                startActivity(intent);
                //Toast.makeText(mainC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardServicePoints:
                intent = new Intent(mainC, ServicePoint.class);
                startActivity(intent);
                //Toast.makeText(mainC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void callApiWalletList() {
        try {
           // MyApplication.showloader(mainC,"Please wait!");
            API.GET("ewallet/api/v1/wallet/walletOwner/"+ MyApplication.getSaveString("walletOwnerCode", mainC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("MiniStatement response======="+jsonObject.toString());
                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    if(jsonObject.has("walletList") &&jsonObject.optJSONArray("walletList")!=null){
                                        JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            if(data.optString("walletTypeCode").equalsIgnoreCase("100008")){
                                                tvBalance.setText(data.optString("value")+" "+data.optString("currencySymbol"));
                                            }

                                        }


                                    }

                                    getPromotionList();

                                } else {
                                    MyApplication.showToast(mainC,jsonObject.optString("resultDescription"));
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

    //http://202.131.144.130:8081/ewallet/api/v1/promOfferTemplate/all

    private void getPromotionList() {

        MyApplication.showloader(mainC,"Please Wait...");
        API.GET_WF("ewallet/api/v1/promOfferTemplate/all", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                offerPromotionModelArrayList=new ArrayList<>();


                if(jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    JSONArray dataArray = jsonObject.optJSONArray("promOfferTemplateList");
                    if(dataArray!=null&&dataArray.length()>0) {
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
                                    data.optString("fileName"),
                                    data.optString("status"),
                                    data.optString("state"),
                                    data.optString("createdBy"),
                                    data.optString("creationDate"),
                                    data.optString("modificationDate")));

                        }


                        rv_offer_promotion.setHasFixedSize(true);
                        // rv_offer_promotion.setLayoutManager(new GridLayoutManager(this,2));
                        rv_offer_promotion.setLayoutManager(new LinearLayoutManager(mainC,LinearLayoutManager.HORIZONTAL,false));
                        rv_offer_promotion.setItemAnimator(new DefaultItemAnimator());
                        OfferPromotionAdapter offerPromotionAdapter = new OfferPromotionAdapter(mainC,offerPromotionModelArrayList);
                        rv_offer_promotion.setAdapter(offerPromotionAdapter);
                        offerPromotionAdapter.notifyDataSetChanged();


                    }
                }else{
                    MyApplication.showToast(mainC,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(mainC,aFalse);
            }
        });
    }

    int doubleBackToExitPressed = 1;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

}
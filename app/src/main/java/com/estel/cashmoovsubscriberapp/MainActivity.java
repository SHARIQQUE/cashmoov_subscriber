package com.estel.cashmoovsubscriberapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
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
import com.estel.cashmoovsubscriberapp.activity.location.Constants;
import com.estel.cashmoovsubscriberapp.activity.location.FetchAddressIntentServices;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static MainActivity mainC;
    SmoothBottomBar bottomBar;
    ImageView imgNotification,imgQR,imgLogo;
    CircleImageView imgProfile;
    LinearLayout linMain,linClick,linPromotion;
    TextView tvClick,tvBalance,pro_text;
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

        resultReceiver = new AddressResultReceiver(new Handler());
        getIds();
    }



    @Override
    protected void onStart() {
        super.onStart();


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);


        String ImageName=MyApplication.getSaveString("ImageName", mainC);
        if(ImageName!=null&&ImageName.length()>1) {
          /*  String image_url = API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                    MyApplication.getSaveString("walletOwnerCode", mainC)+"/" + ImageName;
*/
           // System.out.println("Image Url:  "+image_url);
            System.out.println("Image Name:  "+ImageName);
            Glide.with(this).load(ImageName).apply(options).into(imgProfile);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MyApplication.isFirstTime=false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
        MyApplication.isFirstTime=false;
    }

    private void getIds() {
        pro_text = findViewById(R.id.pro_text);
        imgNotification = findViewById(R.id.imgNotification);
        imgQR = findViewById(R.id.imgQR);
        imgLogo = findViewById(R.id.imgLogo);
        imgProfile = findViewById(R.id.imgProfile);
        linMain = findViewById(R.id.linMain);
        linClick = findViewById(R.id.linClickn);
        linPromotion =findViewById(R.id.linPromotion);
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

        callApiWalletList();


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
            case R.id.linClickn:
                MyApplication.saveBool("TRANSHISTORYCLICK",true,mainC);
                intent = new Intent(mainC, WalletScreen.class);
                startActivity(intent);

               /* if(isTaskRoot()){

                }else{
                   // super.onBackPressed();
                }*/


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


                                    gettempalteList();
//                                    if(MyApplication.isFirstTime){
//                                        linPromotion.setVisibility(View.VISIBLE);
//                                        linMain.setVisibility(View.GONE);
//                                        imgLogo.setVisibility(View.VISIBLE);
//                                        gettempalteList();
//                                    }else{
//                                        linPromotion.setVisibility(View.VISIBLE);
//                                        linMain.setVisibility(View.GONE);
//                                        imgLogo.setVisibility(View.VISIBLE);
//                                        gettempalteList();
//                                    }



                                } else {
                                    linPromotion.setVisibility(View.GONE);
                                    linMain.setVisibility(View.VISIBLE);
                                    imgLogo.setVisibility(View.INVISIBLE);
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
    //http://202.131.144.130:8081/ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/1000002843

    private void gettempalteList() {

       // MyApplication.showloader(mainC,"Please Wait...");
        API.GET_WF("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/"+MyApplication.getSaveString("walletOwnerCode", mainC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                offerPromotionModelArrayList=new ArrayList<>();


                if(jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    JSONArray dataArray = jsonObject.optJSONArray("walletOwnerTemplateList");

                    //walletOwnerTemplateList
                    if(dataArray!=null&&dataArray.length()>0) {

                        Set<Integer> set = new HashSet<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            if(data.optString("templateCategoryCode").equalsIgnoreCase("100013")){
                                    set.add(0);
                                linPromotion.setVisibility(View.VISIBLE);
                                linMain.setVisibility(View.GONE);
                                imgLogo.setVisibility(View.VISIBLE);
                                getPromotionList(data.optString("templateCode"));
                            }
//                            else{
//                                    set.add(1);
//
//                               // MyApplication.showToast(mainC,jsonObject.optString("resultDescription"));
//                            }
                        }
                            Log.e("Set--",set.toString());
                        if(!set.contains(0)){
                            linPromotion.setVisibility(View.GONE);
                            linMain.setVisibility(View.VISIBLE);
                            imgLogo.setVisibility(View.INVISIBLE);

                        }



                    }
                }else{
                    linPromotion.setVisibility(View.GONE);
                    linMain.setVisibility(View.VISIBLE);
                    imgLogo.setVisibility(View.INVISIBLE);
                    MyApplication.showToast(mainC,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(mainC,aFalse);
            }
        });
    }


    OfferPromotionAdapter offerPromotionAdapter;

    private void getPromotionList(String code) {
//http://202.131.144.130:8081/ewallet/api/v1/promOfferTemplate/101235
        //Bhawesh, 15:02
        //http://192.168.1.171:8081/ewallet/api/v1/promOfferTemplate/allByCriteria?templateCode=101235&status=Y&state=A
       // MyApplication.showloader(mainC,"Please Wait...");
        API.GET_WF("ewallet/api/v1/promOfferTemplate/allByCriteria?templateCode="+code+"&status=Y&state=A", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                offerPromotionModelArrayList=new ArrayList<>();

                offerPromotionModelArrayList.clear();
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
                                    data.optString("subHeading"),
                                    data.optString("state"),
                                    data.optString("createdBy"),
                                    data.optString("creationDate"),
                                    data.optString("modificationDate")));

                        }


                        rv_offer_promotion.setHasFixedSize(true);
                        // rv_offer_promotion.setLayoutManager(new GridLayoutManager(this,2));
                        rv_offer_promotion.setLayoutManager(new LinearLayoutManager(mainC,LinearLayoutManager.HORIZONTAL,false));
                        //rv_offer_promotion.setItemAnimator(new DefaultItemAnimator());
                       // Collections.sort(offerPromotionModelArrayList, Collections.reverseOrder());
                        offerPromotionAdapter = new OfferPromotionAdapter(mainC,offerPromotionModelArrayList);
                        rv_offer_promotion.setAdapter(offerPromotionAdapter);

                        offerPromotionAdapter.notifyDataSetChanged();
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new AutoScrollTask(), 3000, 5000);



                    }
                }else{
                    linPromotion.setVisibility(View.GONE);
                    linMain.setVisibility(View.VISIBLE);
                    imgLogo.setVisibility(View.INVISIBLE);
                   // MyApplication.showToast(mainC,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(mainC,aFalse);
            }
        });
    }


    int position ;
    boolean end;
    private class AutoScrollTask extends TimerTask{
        @Override
        public void run() {
            if(position == offerPromotionModelArrayList.size() -1){
                end = true;
            } else if (position == 0) {
                end = false;
            }
            if(!end){
                position++;
            } else {
                position--;
            }
            rv_offer_promotion.smoothScrollToPosition(position);
        }
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


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ResultReceiver resultReceiver;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String transactionCoordinate;
    public static String transactionArea;
    private void getCurrentLocation() {
       // progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            System.out.println("loc========="+ lati+""+ longi);

                            transactionCoordinate=lati+","+longi;
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);

                        } else {
                            //progressBar.setVisibility(View.GONE);

                        }
                    }
                }, Looper.getMainLooper());

    }
    String textLatLong, address, postcode, locaity, state, district, country;
    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                address=(resultData.getString(Constants.ADDRESS));
                locaity=(resultData.getString(Constants.LOCAITY));
                state=(resultData.getString(Constants.STATE));
                district=(resultData.getString(Constants.DISTRICT));
                country=(resultData.getString(Constants.COUNTRY));
                postcode=(resultData.getString(Constants.POST_CODE));

                System.out.println("loc========="+address);
                System.out.println("loc=========locaity"+locaity);
                System.out.println("loc=========state"+state);
                System.out.println("loc=========district"+district);
                System.out.println("loc=========country"+country);
                System.out.println("loc=========postcode"+postcode);

                transactionArea=locaity+","+district+","+state+","+country+","+postcode;

            } else {
                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
          //  progressBar.setVisibility(View.GONE);
        }


    }

    private void fetchaddressfromlocation(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEVIER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);


    }



}
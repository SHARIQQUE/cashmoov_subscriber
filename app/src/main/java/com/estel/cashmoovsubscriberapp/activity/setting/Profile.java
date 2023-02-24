package com.estel.cashmoovsubscriberapp.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.BuildConfig;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.MyQrCode;
import com.estel.cashmoovsubscriberapp.activity.NotificationList;
import com.estel.cashmoovsubscriberapp.activity.fee.Fee;
import com.estel.cashmoovsubscriberapp.activity.partner.Partner;
import com.estel.cashmoovsubscriberapp.activity.servicepoint.ServicePoint;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    public static Profile profileC;
    ImageView imgBack,imgHome;
    ImageView imgNotification,imgQR;
    TextView tvBadge,spinner_currency,paymonthcountText,paymonthlimitAccountText;
    SmoothBottomBar bottomBar;
    LinearLayout linGloballimitcount,linRateUs,linFee,linServicePoint,linBeneficiary,linChangeLang,linConfidentiality,linShareApp,
            linTermCondition,linAbout,linChangePin,linEditProfile,linReset;
    String currencyName_from_currency="",profiletypecode="",currencycode="",walletownercode="",walletOwnerCode_destination="",walletOwnerCode_source="";
    String countryCurrencyCode_from_currency="";

    TextView currency,number,etAddress,name;
    CircleImageView profile_img;
    private ReviewManager reviewManager;
    private String mNumber;
    private long mLastClickTime = 0;

    String languageToUse = "";
    MyApplication applicationComponentClass;

    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    ArrayList<String> arrayList_currencySymbol = new ArrayList<String>();
    ArrayList<String> arrayList_desWalletOwnerCode = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     /*   applicationComponentClass = (MyApplication) getApplicationContext();

        try {

            languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

            if (languageToUse.trim().length() == 0) {
                languageToUse = "en";
            }


            Locale locale = new Locale(languageToUse);

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileC = this;
        reviewManager = ReviewManagerFactory.create(this);
        MyApplication.hideKeyboard(profileC);
       // MyApplication.setLang(profileC);



        //  setBackMenu();
        getIds();

    }

    @Override
    protected void onStart() {
        super.onStart();
        number.setText("N/A");
        MyApplication.hideKeyboard(profileC);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);
        String ImageName=MyApplication.getSaveString("ImageName", profileC);
        if(ImageName!=null&&ImageName.length()>1) {
           // String image_url = MyApplication.ImageURL + ImageName;
            Glide.with(this).load(ImageName).apply(options).into(profile_img);
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgHome = findViewById(R.id.imgHome);
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        number.setText(mNumber);
        MyApplication.hideLoader();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(2);
        number.setText(mNumber);

        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        if(MyApplication.isNotification&&MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)!=0){
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)));
        }else{
            tvBadge.setVisibility(View.GONE);
        }
    }


    private void getIds() {
        imgNotification = findViewById(R.id.imgNotification);
        tvBadge = findViewById(R.id.tvBadge);
        imgQR = findViewById(R.id.imgQR);
        bottomBar = findViewById(R.id.bottomBar);
        linRateUs = findViewById(R.id.linRateUs);
        linFee = findViewById(R.id.linFee);
        linServicePoint = findViewById(R.id.linServicePoint);
        linBeneficiary = findViewById(R.id.linBeneficiary);
        linChangeLang = findViewById(R.id.linChangeLang);
        linConfidentiality = findViewById(R.id.linConfidentiality);
        linShareApp = findViewById(R.id.linShareApp);
        linTermCondition = findViewById(R.id.linTermCondition);
        linAbout = findViewById(R.id.linAbout);
        linChangePin = findViewById(R.id.linChangePin);
        linEditProfile = findViewById(R.id.linEditProfile);
        linReset = findViewById(R.id.linReset);

        currency = findViewById(R.id.currency);
        number = findViewById(R.id.number);
        etAddress = findViewById(R.id.etAddress);
        name = findViewById(R.id.name);

        profile_img = findViewById(R.id.profile_img);
        linGloballimitcount = findViewById(R.id.linGloballimitcount);
        linGloballimitcount.setOnClickListener(this);

        if(MyApplication.isNotification&&MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)!=0){
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)));
        }else{
            tvBadge.setVisibility(View.GONE);
        }




        String naam= MyApplication.getSaveString("firstName",profileC)+" "+
                MyApplication.getSaveString("lastName",profileC);
        String add= MyApplication.getSaveString("issuingCountryName",profileC);
        String num= MyApplication.getSaveString("mobile",profileC);
        String currency= MyApplication.getSaveString("firstName",profileC);
        String img= MyApplication.getSaveString("firstName",profileC);


        //number.setText(num);
        //name.setText(naam);
       // etAddress.setText(add);


        bottomBar.setItemActiveIndex(2);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                /*ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

                int sizeStack =  am.getRunningTasks(2).size();

                for(int i = 0;i < sizeStack;i++){

                    ComponentName cn = am.getRunningTasks(2).get(i).topActivity;
                    MyApplication.showToast(profileC,cn.getClassName());
                    Log.d("Class Name======", cn.getClassName());
                }*/

                if (bottomId == 0) {
                    MyApplication.isFirstTime=false;

                    Intent i = new Intent(profileC, MainActivity.class);
                    startActivity(i);

                    //  finish();
                }
                if (bottomId == 1) {
                    //Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(profileC, Partner.class);
                    startActivity(i);


                    //finish();
                }
                if (bottomId == 2) {
//                    Intent i = new Intent(profileC, Profile.class);
//                    startActivity(i);
                    //  finish();
                }
                return true;
            }
        });

        callAPIWalletOwnerDetails();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        imgNotification.setOnClickListener(profileC);
        imgQR.setOnClickListener(profileC);
        linRateUs.setOnClickListener(profileC);
        linFee.setOnClickListener(profileC);
        linServicePoint.setOnClickListener(profileC);
        linBeneficiary.setOnClickListener(profileC);
        linChangeLang.setOnClickListener(profileC);
        linConfidentiality.setOnClickListener(profileC);
        linShareApp.setOnClickListener(profileC);
        linTermCondition.setOnClickListener(profileC);
        linAbout.setOnClickListener(profileC);
        linChangePin.setOnClickListener(profileC);
        linEditProfile.setOnClickListener(profileC);
        linReset.setOnClickListener(profileC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.imgNotification:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                intent = new Intent(profileC, NotificationList.class);
                startActivity(intent);
                MyApplication.isNotification = false;
                break;
            case R.id.imgQR:
                intent = new Intent(profileC, MyQrCode.class);
                startActivity(intent);
                break;
            case R.id.linRateUs:
                showRateApp();
                break;
            case R.id.linGloballimitcount:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                alertdialougeGlobalLimit();
                break;
            case R.id.linFee:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, Fee.class);
                startActivity(intent);
                break;
            case R.id.linServicePoint:
                MyApplication.showloader(Profile.this,getString(R.string.please_wait));

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, ServicePoint.class);
                startActivity(intent);

                break;
            case R.id.linBeneficiary:
//                intent = new Intent(profileC, AddBeneficiary.class);
//                startActivity(intent);
                Toast.makeText(profileC,getString(R.string.ComingSoon), Toast.LENGTH_SHORT).show();

                break;
            case R.id.linChangeLang:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, ChangeLanguage.class);
                startActivity(intent);
                break;
            case R.id.linConfidentiality:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, Confidentiality.class);
                startActivity(intent);
                break;
            case R.id.linShareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.linTermCondition:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Toast.makeText(profileC,getString(R.string.ComingSoon), Toast.LENGTH_SHORT).show();

                break;
            case R.id.linAbout:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, About.class);
                startActivity(intent);
                break;
            case R.id.linChangePin:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, ChangePin.class);
                startActivity(intent);
                break;
            case R.id.linEditProfile:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, EditProfile.class);
                startActivity(intent);
                break;
            case R.id.linReset:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(profileC, Reset.class);
                startActivity(intent);
                break;

        }
    }

    public void callAPIWalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode", profileC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    name.setText(jsonObject.optJSONObject("walletOwner").optString("ownerName")+" "+
                            jsonObject.optJSONObject("walletOwner").optString("lastName"));
                    etAddress.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryName","N/A"));
                    number.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));
                    mNumber=jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A");

                    walletownercode = jsonObject.optJSONObject("walletOwner").optString("walletOwnerCategoryCode");
                    profiletypecode = jsonObject.optJSONObject("walletOwner").optString("profileTypeCode");


                    System.out.println("get walletcode"+jsonObject.toString());
                    callApiFromCurrency(jsonObject.optJSONObject("walletOwner").optString("registerCountryCode"));
                }else{
                    MyApplication.showToast(profileC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(profileC,aFalse);
            }
        });
    }

    private void callApiFromCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    currency.setText(jsonObject.optJSONObject("country").optString("currencyCode"));
                                    //fromCurrencySymbol = jsonObject.optJSONObject("country").optString("currencySymbol");


                                } else {
                                    MyApplication.showToast(profileC,jsonObject.optString("resultDescription", "  "));
                                }
                            }

                            // callApiBenefiCurrency();
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

//    private void init() {
//        reviewManager = ReviewManagerFactory.create(this);
//        // Referencing the button
//        findViewById(R.id.linRateUs).setOnClickListener(view -> showRateApp());
//    }


    /**
     * Shows rate app bottom sheet using In-App review API
     * The bottom sheet might or might not shown depending on the Quotas and limitations
     * https://developer.android.com/guide/playcore/in-app-review#quotas
     * We show fallback dialog if there is any error
     */
    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
                showRateAppFallbackDialog();
            }
        });
    }

    /**
     * Showing native dialog with three buttons to review the app
     * Redirect user to playstore to review the app
     */
    private void showRateAppFallbackDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.rate_app)
                .setMessage(R.string.rate_app_message)
                .setPositiveButton(R.string.rate_btn_pos, (dialog, which) -> {

                })
                .setNegativeButton(R.string.rate_btn_neg,
                        (dialog, which) -> {
                        })
                .setNeutralButton(R.string.rate_btn_nut,
                        (dialog, which) -> {
                        })
                .setOnDismissListener(dialog -> {
                })
                .show();
      }

    public void alertdialougeGlobalLimit() {

        try {


            Dialog operationDialog = new Dialog(Profile.this);
            operationDialog.setContentView(R.layout.dialog_global_limit);

            Button closeButton;
            closeButton = operationDialog.findViewById(R.id.closeButton);
            spinner_currency= operationDialog.findViewById(R.id.spinner_Currency);
            paymonthcountText=operationDialog.findViewById(R.id.paymonthcountText);
            paymonthlimitAccountText=operationDialog.findViewById(R.id.paymonthlimitAccountText);






            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    operationDialog.dismiss();
                }
            });
            //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            operationDialog.show();

            apicurrency();

        } catch (Exception e) {

            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    private void apicurrency() {

        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("userCode", Profile.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+"1000006042",languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {


                      /*  arrayList_currecnyName.add(0,getString(R.string.select_currency_star));
                        arrayList_currecnyCode.add(0,getString(R.string.select_currency_star));
                       arrayList_currencySymbol.add(0,getString(R.string.select_currency_star));
                       arrayList_desWalletOwnerCode.add(0,getString(R.string.select_currency_star));

*/

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            currencyName_from_currency = jsonObject3.getString("currencyName");
                            countryCurrencyCode_from_currency = jsonObject3.getString("currencyCode");
                            walletOwnerCode_destination = jsonObject3.getString("walletOwnerCode");

                            String currencySymbol = jsonObject3.getString("currencySymbol");




                            if(jsonObject3.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject3.getString("currencyName");

                            }

                            arrayList_currecnyName.add(currencyName_from_currency);
                            arrayList_currecnyCode.add(countryCurrencyCode_from_currency);
                            arrayList_currencySymbol.add(currencySymbol);
                            arrayList_desWalletOwnerCode.add(walletOwnerCode_destination);


                        }
                        callAPIGloballimitCount();

                      /*  CommonBaseAdapterSecond arraadapter2 = new CommonBaseAdapterSecond(TransferFloats.this, arrayList_currecnyName);
                        spinner_currency.setAdapter(arraadapter2);
*/

//                        spinner_currency.setText(arrayList_currecnyName.get(0));
//
//                        currencycode = arrayList_currecnyCode.get(0);

                       // callAPIGloballimitCount();
                    }

                    else {
                        Toast.makeText(Profile.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(Profile.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(Profile.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void callAPIGloballimitCount() {
        try {

            API.GET("ewallet/api/v1/globallimitconfiguration/getProfileAndWltOwrCat?profileTypeCode="+profiletypecode+"&wltOwrCatCode="+walletownercode+"&currencyCode="+"100062",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            try {

                                if (jsonObject != null) {

                                    System.out.println("get json"+jsonObject);


                                    String resultCode =  jsonObject.getString("resultCode");

                                    System.out.println("get code"+resultCode);

                                    if (jsonObject.has("globalLimitConfiguration")) {
                                        paymonthcountText.setText(jsonObject.optJSONObject("globalLimitConfiguration").optString("perMonthLimitCount"));

                                        paymonthlimitAccountText.setText(MyApplication.addDecimal(jsonObject.optJSONObject("globalLimitConfiguration").optString("perMonthLimitAmount")));

                                    }
                                    else
                                    {
                                        paymonthcountText.setText("0");
                                        paymonthlimitAccountText.setText("0.00");

                                    }








                                    // callApiRecCountry();

                                } else {
                                    MyApplication.showToast(Profile.this,jsonObject.optString("resultDescription", "  "));
                                }

                            } catch (Exception e) {

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
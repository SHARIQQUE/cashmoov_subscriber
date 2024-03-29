package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    public static Profile profileC;
    ImageView imgBack,imgHome;
    ImageView imgNotification,imgQR;
    TextView tvBadge;
    SmoothBottomBar bottomBar;
    LinearLayout linRateUs,linFee,linServicePoint,linBeneficiary,linChangeLang,linConfidentiality,linShareApp,
            linTermCondition,linAbout,linChangePin,linEditProfile,linReset;

    TextView currency,number,etAddress,name;
    CircleImageView profile_img;
    private ReviewManager reviewManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileC=this;
        reviewManager = ReviewManagerFactory.create(this);
        MyApplication.hideKeyboard(profileC);
      //  setBackMenu();
        getIds();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(2);
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
            case R.id.linFee:
                intent = new Intent(profileC, Fee.class);
                startActivity(intent);
                break;
            case R.id.linServicePoint:
                intent = new Intent(profileC, ServicePoint.class);
                startActivity(intent);
                break;
            case R.id.linBeneficiary:
//                intent = new Intent(profileC, AddBeneficiary.class);
//                startActivity(intent);
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();

                break;
            case R.id.linChangeLang:
                intent = new Intent(profileC, ChangeLanguage.class);
                startActivity(intent);
                break;
            case R.id.linConfidentiality:
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
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();

                break;
            case R.id.linAbout:
                intent = new Intent(profileC, About.class);
                startActivity(intent);
                break;
            case R.id.linChangePin:
                intent = new Intent(profileC, ChangePin.class);
                startActivity(intent);
                break;
            case R.id.linEditProfile:
                intent = new Intent(profileC, EditProfile.class);
                startActivity(intent);
                break;
            case R.id.linReset:
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


}
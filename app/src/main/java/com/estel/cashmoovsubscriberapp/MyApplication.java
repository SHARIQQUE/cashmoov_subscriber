package com.estel.cashmoovsubscriberapp;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.media.MediaMetadataEditor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;

import com.balsikandar.crashreporter.CrashReporter;
import com.estel.cashmoovsubscriberapp.activity.login.AESEncryption;
import com.estel.cashmoovsubscriberapp.activity.login.PhoneNumberRegistrationScreen;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.BioMetric_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class MyApplication extends Application {

    public static boolean PasswordEncription = true;

    private static final int PERMISSION_REQUEST_CODE = 101;
    public static final String directoryName = "CustomLoggerSubs";
    private static final String TAG = MyApplication.class.getSimpleName();
    public static boolean isScan;
    public static boolean isScanNew=false;
    public static ArrayList<OfferPromotionModel> offerPromotionModelArrayList=new ArrayList<>();
    public static int offerPromtionPos=0;
    public static boolean isNotification=false;
    public static TinyDB tinyDB;
    public static boolean showToSubscriber=false;
    public static boolean showToNonSubscriber=false;
    public static boolean showInternationalRemit=false;
    public static boolean showMoneyTransfer=false;
    public static boolean showAirtimePurchase=false;
    public static boolean showBillPayment=false;
    public static boolean showPay=false;
    public static boolean showCashOut=false;
    public static boolean showCashPickup=false;
    public static int ToSubscriberMinAmount;
    public static int ToSubscriberMaxAmount;
        public static int ToNonSubscriberMinAmount;
        public static int ToNonSubscriberMaxAmount;
    public static int InternationalMinAmount;
    public static int InternationalMaxAmount;
    public static int AirtimePurchaseMinAmount;
    public static int AirtimePurchaseMaxAmount;
    public static int BillPaymentMinAmount;
    public static int BillPaymentMaxAmount;
    public static int PayMinAmount;
    public static int PayMaxAmount;
    public static int CashOutMinAmount;
    public static int CashOutMaxAmount;
    public static int CashPickupMinAmount;
    public static int CashPickupMaxAmount;
    private static KProgressHUD hud;
    public static MyApplication appInstance;
    public static String lang;
    public static String UserMobile;
    public static String ImageURL;
    public static String setProtection;
    public static String channelTypeCode = "100000";
    public static boolean isFirstTime=false;
    public static boolean isContact=false;
    public static boolean IsMainOpen=false;
    public static boolean IsPromoCalled=false;
    public static int mobileLength=9;
    public static int amountLength=13;
    public static int amountLengthcashpickup=10;

    public static int mobileLengthinternational=14;

    public static MyApplication getInstance() {
        return appInstance;
    }


    private SharedPreferences mSharedPreferences;
    private String PREF_NAME = "cashmoove_sh";
    public static void initialisedLogger(Application message) {
        /*
         * First initialised SaveLogsInStorage in variable
         * to access all save logs instance method
         */




        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(System.currentTimeMillis());
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Cashmoov/");
        if (!storageDir.exists())
            storageDir.mkdirs();
       /* File image = File.createTempFile(
                timeStamp,
                ".jpeg",
                storageDir
        );*/






        /*File Directory = new File(directoryName);
        Directory.mkdirs();*/

        /*
         *Explain how to use all save logs instance method
         */
        //printLogsInStorage();
    }

    public  void callLogin() {
        saveBool("isLogin",false,getInstance());
        Intent intent = new Intent(getInstance(), PhoneNumberRegistrationScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        tinyDB=new TinyDB(appInstance);

        ImageURL= API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                MyApplication.getSaveString("walletOwnerCode", this)+"/";
        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", appInstance);
        System.out.println("get value"+MyApplication.setProtection);
        if (MyApplication.setProtection.equalsIgnoreCase("Activate")|| MyApplication.setProtection.equalsIgnoreCase("Deactivate")) {

        } else {
            MyApplication.saveString("ACTIVATEPROTECTION", "Activate", appInstance);
            MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", appInstance);

        }

        AndroidNetworking.initialize(getApplicationContext());
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        AndroidNetworking.setBitmapDecodeOptions(options);*/
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);

        initialisedLogger(this);


        CrashReporter.initialize(this);
        AESEncryption.getAESEncryption("1234");

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        AndroidNetworking.setConnectionQualityChangeListener(new ConnectionQualityChangeListener() {
            @Override
            public void onChange(ConnectionQuality currentConnectionQuality, int currentBandwidth) {
                Log.d(TAG, "onChange: currentConnectionQuality : " + currentConnectionQuality + " currentBandwidth : " + currentBandwidth);
            }
        });
    }


    public static OkHttpClient okClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })

            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source","SUBSCRIBER")
                            .header("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static OkHttpClient okClientfileUpload = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BASIC))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source","SUBSCRIBER")
                            .header("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static String doubleRound(double value){
        return String.format("%.2f", value);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showTipError(Activity activity,String msg,View v){
        ViewTooltip
                .on(activity, v)
                .autoHide(true, 2000)
                .clickToHide(true)
                .corner(30)
                .color(Color.RED)
                .position(ViewTooltip.Position.TOP)
                .text(msg)
                .show();
    }

    public static void showAPIToast(String message){
        Toast toast= Toast.makeText(getInstance(),
                "   "+message+"  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastNew(Context activity, String message) {
        Toast toast = Toast.makeText(activity,
                "   " + message + "  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
//        View view = toast.getView();
//        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        view.setBackgroundResource(R.drawable.success_toast);
//        TextView text = view.findViewById(android.R.id.message);
//        text.setPadding(20,10,20,10);
//        text.setTextSize(13);
//        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        toast.show();
    }

    public static void showToast(Activity activity, String message){
        Toast toast= Toast.makeText(activity,
                "   "+message+"  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
//        View view = toast.getView();
//        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        view.setBackgroundResource(R.drawable.success_toast);
//        TextView text = view.findViewById(android.R.id.message);
//        text.setPadding(20,10,20,10);
//        text.setTextSize(13);
//        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        toast.show();
    }
    public SharedPreferences getmSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        }
        return mSharedPreferences;
    }



    public static void showErrorToast(Activity activity, String message){
        Toast toast= Toast.makeText(activity, message, Toast.LENGTH_SHORT);
      /*  toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
        View view = toast.getView();
        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        view.setBackgroundResource(R.drawable.error_toast);
        TextView text = view.findViewById(android.R.id.message);
        text.setPadding(20,10,20,10);
        text.setTextSize(13);
        text.setTextColor(ContextCompat.getColor(activity, R.color.white));*/
        toast.show();
    }


    public static void showloader(Activity activity, String message){
//        ImageView imageView = new ImageView(activity);
//        imageView.setBackgroundResource(R.drawable.spin_animation);
//        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
//        drawable.start();
        hud = KProgressHUD.create(activity)
                //.setCustomView(imageView)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setMaxProgress(100)
                // .setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        hud.setProgress(90);
    }

    public static void hideLoader(){
        if(hud!=null){
            hud.dismiss();
        }
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    public boolean isPassword(String pass) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{6,}$").matcher(pass).matches();
    }

    public static void saveString(String key, String value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void deleteString(String key, String value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        preferences.edit().clear().commit();

    }

    public static void saveInt(String key, int value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Integer getSaveInt(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        int json = preferences.getInt(key,0);
        return json;
    }
    public static void saveBool(String key, Boolean value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getSaveBool(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        Boolean json = preferences.getBoolean(key,false);
        return json;
    }
    public static String getSaveString(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        String json = preferences.getString(key,"");
        return json;
    }

    public  static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                        /*if(getSpeed()){
                            return true;
                        }else{
                            return false;
                        }*/

                    }
                }
            }
        }
        return false;
    }



    public static String addDecimalfive(String number) {
        String data="00.000";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));

        }else{
            DecimalFormat df = new DecimalFormat("00.00000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }

    public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    public static String addDecimal(String number) {
        String data="0.00";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")) {
            DecimalFormat df = new DecimalFormat("0.00", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));
        }
        return data;
    }


    public static String addDecimalfrench(String number) {
        String data="0.00";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")) {
            DecimalFormat df = new DecimalFormat("00.000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##00.000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));
        }
        return data;
    }

    public static int prevCommaAmount;
    public static String formatInput(CharSequence s, int start, int count) {


        StringBuilder sbResult = new StringBuilder();
        String result;
        int newStart = start;

        try {
            // Extract value without its comma
            String digitAndDotText = s.toString().replace(",", "");
            int commaAmount = 0;




            if (digitAndDotText.contains(".")) {
                // escape sequence for .
                String[] wholeText = digitAndDotText.split("\\.");



                // in 150,000.45 non decimal is 150,000 and decimal is 45
                String nonDecimal = wholeText[0];


                // only format the non-decimal value
                result = AutoFormatUtil.formatToStringWithoutDecimal(nonDecimal);

                sbResult
                        .append(result)
                        .append(".");

                if (wholeText.length > 1) {
                    sbResult.append(wholeText[1]);
                }

            } else {
                result = AutoFormatUtil.formatWithDecimal(digitAndDotText);
                sbResult.append(result);
            }

            // count == 0 indicates users is deleting a text
            // count == 1 indicates users is entering a text
            newStart += ((count == 0) ? 0 : 1);

            // calculate comma amount in edit text
            commaAmount += AutoFormatUtil.getCharOccurance(result, ',');

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += ((count == 0) ? -1 : 1);
                prevCommaAmount = commaAmount;
            }

            // case when deleting without comma
            if (commaAmount == 0 && count == 0 && prevCommaAmount != commaAmount) {
                newStart -= 1;
                prevCommaAmount = commaAmount;
            }

            // case when deleting without dots
            if (count == 0 && !sbResult.toString()
                    .contains(".") && prevCommaAmount != commaAmount) {
                newStart = start;
                prevCommaAmount = commaAmount;
            }

            //editText.setText(sbResult.toString());

            // ensure newStart is within result length
            if (newStart > sbResult.toString().length()) {
                newStart = sbResult.toString().length();
            } else if (newStart < 0) {
                newStart = 0;
            }

            // editText.setSelection(newStart);
            return sbResult.toString();

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return sbResult.toString();
    }

    public static String addDecimalthreetwo(String number) {
        String data="0.00";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")) {
            DecimalFormat df = new DecimalFormat("0.000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }

    public static String addDecimaltwo(String number) {
        String data="0.00";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")) {
            DecimalFormat df = new DecimalFormat("0.00", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }


    public static String addDecimalthreenew(String number) {
        String data="0.00";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")) {
            DecimalFormat df = new DecimalFormat("0.000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }




    public  static void setrequired(TextView textView,String str){
        TextView fname_label=textView;
        String t=str+appInstance.getString(R.string.required_asterisk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fname_label.setText( Html.fromHtml(t,Html.FROM_HTML_MODE_LEGACY));
        } else {
            fname_label.setText(Html.fromHtml(t), TextView.BufferType.SPANNABLE);
        }

    }

    public static String getUniqueId(){
        String android_id = Settings.Secure.getString(MyApplication.appInstance.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android==","Android ID : "+android_id);
        //android_id="963E768F-B803-4F46-B8DB-0D2B3AA53155";
        // return  "SA2132425277828";
        return  android_id;

    }

    public static void setLang(Context context){
        lang =  getSaveString("Locale", context);
        if(lang!=null && !MyApplication.lang.isEmpty()){
            if(lang.equalsIgnoreCase("en")){
                changeLocale(context,lang);
                MyApplication.saveString("Locale", lang, context);
            }else{
                changeLocale(context,lang);
                MyApplication.saveString("Locale", lang, context);
            }

            //change to fr
        }else{
            changeLocale(context,"fr");
            MyApplication.saveString("Locale", "fr", context);
        }
    }

    public static void changeLocale(Context context, String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());//Update the config
    }
    public static String convertUTCToLocalTime(String Date){
        String dateStr = Date;
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        //
        //df.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        java.util.Date date1 = null;
        try {
            date1 = df1.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        android.text.format.DateFormat df2 = new android.text.format.DateFormat();

        if(isToday(date1)){
            String df_medium_us_str= (String) df2.format("HH:mm", date1);
            return "Today, "+df_medium_us_str;
        }

        if(isYesterday(date1)){
            String df_medium_us_str= (String) df2.format("HH:mm", date1);
            return "Yesterday, "+df_medium_us_str;
        }
        String df_medium_us_str= (String) df2.format("dd-MMM-yyyy", date1);
       /* if(isToday(date1)){
            String df_medium_us_str= (String) df2.format("hh:mm:ss a", date1);
            return "Today, "+df_medium_us_str;
        }

        if(isYesterday(date1)){
            String df_medium_us_str= (String) df2.format("hh:mm:ss a", date1);
            return "Yesterday, "+df_medium_us_str;
        }
        String df_medium_us_str= (String) df2.format("dd-MMM-yyyy hh:mm:ss a", date1);*/
        return  df_medium_us_str;
    }
    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }



    public static boolean isToday(Date d) {
        return DateUtils.isToday(d.getTime());
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }


    public static int bioMetricCounter=0;
    public static BiometricPrompt biometricPrompt=null;
    public static Activity activityNew;
    public static void biometricAuth(Activity activity, BioMetric_Responce_Handler bioMetric_responce_handler){
        activityNew=activity;
        FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(activity);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                //bioMetric_responce_handler.failure(activity.getString(R.string.no_fingerprint_senser));
                //msgText.setText(getString(R.string.no_fingerprint_senser));
                //tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // bioMetric_responce_handler.failure(activity.getString(R.string.no_biometric_senser));
              /*  msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);*/
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                //  bioMetric_responce_handler.failure(activity.getString(R.string.device_not_contain_fingerprint));

                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(activity);
        // this will give us result of AUTHENTICATION
        biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);


                if(!fingerprintManager.hasEnrolledFingerprints()) {
                    bioMetric_responce_handler.failure(activity.getString(R.string.no_fingerprint_senser));

                    // User hasn't enrolled any fingerprints to authenticate with
                } else {
                    // Everything is ready for fingerprint authentication
                }

                //   checkCounter(bioMetric_responce_handler,errString+"");



                //   checkCounter(bioMetric_responce_handler,errString+"");

            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());
                bioMetric_responce_handler.success("Call API");

               /* Intent intent = new Intent(loginpinC, MainActivity.class);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                checkCounter(bioMetric_responce_handler,activity.getString(R.string.please_enter_pin_bio));

                biometricPrompt.cancelAuthentication();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(activity.getString(R.string.use_finger_to_transaction)).setNegativeButtonText(activity.getString(R.string.cancel)).setConfirmationRequired(false).build();

        biometricPrompt.authenticate(promptInfo);


    }

    public static void checkCounter(BioMetric_Responce_Handler bioMetric_responce_handler,String message){
        if(bioMetricCounter==3){
            bioMetricCounter=0;

            bioMetric_responce_handler.failure(message);
        }else{
            bioMetricCounter=bioMetricCounter+1;
            showToast(activityNew,appInstance.getString(R.string.tryagain));

        }

    }

public static int  attmptCount=0;
    public static int  maxattmptCount=2;

    public static  void contactValidation(String Phoneno, EditText editText){
        if(Phoneno.length()>11)
        {
            if(Phoneno.contains("+91") || Phoneno.length()==11){
                int startidx=Phoneno.length()-10;
                String getnumber=Phoneno.substring(startidx,Phoneno.length());
                editText.setText(getnumber);
            }else{
                int startidx=Phoneno.length()-9;
                String getnumber=Phoneno.substring(startidx,Phoneno.length());
                editText.setText(getnumber);

            }


        }
        else
        {
            editText.setText(Phoneno);
        }
    }





    public static boolean checkMinMax(Activity activity,CharSequence s,EditText editText,int minAmount,int maxAmount){
       /* if(s.length()==1 && s.toString().equalsIgnoreCase(".")){
            return true;
        }
        if (Double.parseDouble(s.toString().trim().replace(",","")) < minAmount) {
            MyApplication.showTipError(activity, activity.getString(R.string.min_amount) + minAmount, editText);
            return true;
        }

        if (Double.parseDouble(s.toString().trim().replace(",","")) > maxAmount) {
            MyApplication.showTipError(activity, activity.getString(R.string.max_amount) + maxAmount, editText);
            return true;
        }*/
        return false;
    }

    public static String getTaxString(String test){
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")
                ||MyApplication.getSaveString("Locale", MyApplication.getInstance()).isEmpty()||
                MyApplication.getSaveString("Locale", MyApplication.getInstance())==null){

            return test +" :";
        }else {
            if (test.equalsIgnoreCase("VAT")) {
                return "T.V.A :";
            }
            if (test.equalsIgnoreCase("Financial Tax")) {
                return "Taxe financière :";
            }
        }

        return test+" :";
    }

    public static String getTaxStringnew(String test){
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")
                ||MyApplication.getSaveString("Locale", MyApplication.getInstance()).isEmpty()||
                MyApplication.getSaveString("Locale", MyApplication.getInstance())==null){

            return test;
        }else {
            if (test.equalsIgnoreCase("VAT")) {
                return "T.V.A :";
            }
            if (test.equalsIgnoreCase("Financial Tax")) {
                return "Taxe financière :";
            }
        }

        return test;
    }

    public static String getEncript(String dataencript) {
        if (PasswordEncription) {
            String encryptionDatanew = AESEncryption.getAESEncryption(dataencript);
            return encryptionDatanew;

        }
        return dataencript;
    }



}

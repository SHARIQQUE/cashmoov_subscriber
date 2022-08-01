package com.estel.cashmoovsubscriberapp.apiCalls;

import static com.estel.cashmoovsubscriberapp.MyApplication.okClient;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.estel.cashmoovsubscriberapp.MyApplication;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;

public class API {
    private static final String TAG = "API CALLS";

    //public static String BASEURL="http://202.131.144.130:8081/";         //QA
   // public static String BASEURL="http://202.131.144.129:8081/";           //UAT
    public static String BASEURL="https://cashmoovmm.com:8081/";

    public static String BASEURL_AMOUNT="http://192.168.1.170:8081/";


    //Production
    //http://202.140.50.120:8081/
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public static OkHttpClient clientBASIC = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
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

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .build();
                }
            })
            .build();

    public static OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("author","Cashmoov1")
                            .build();
                }
            })
            .build();




    public static void POST_REQEST_Login(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("fcmToken",jsonObject.optString("fcmToken"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                // .addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  MyApplication.hideLoader();


                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject error1=new JSONObject(error.getErrorBody());
                            if(error1.optString("error").equalsIgnoreCase("1251")){
                                //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure("1251");
                            }else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }

                        if (error.getErrorCode() != 0) {
//                            if(error.getErrorCode()==400){
//                                MyApplication.showAPIToast("Server not responding,please try again after some time..");
//
//                            }
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void POST_REQEST_GENERATEOTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("type","GENERATEOTP")
                .addHeaders("source","SUBSCRIBER")
                // .addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject error1=new JSONObject(error.getErrorBody());
                            if(error1.optString("error").equalsIgnoreCase("1251")){
                                //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure("1251");
                            }else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void POST_RESETPIN(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("type","RESETPIN")
                .addHeaders("source","SUBSCRIBER")
                // .addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject error1=new JSONObject(error.getErrorBody());
                            if(error1.optString("error").equalsIgnoreCase("1251")){
                                //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure("1251");
                            }else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void POST_REQEST_LoginOTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("type","LOGINOTP")
                .addHeaders("source","SUBSCRIBER")
                // .addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject error1=new JSONObject(error.getErrorBody());
                            if(error1.optString("error").equalsIgnoreCase("1251")){
                                //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure("1251");
                            }else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void POST_REQEST_CHECK(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .setOkHttpClient(okHttpClient)
                .addJSONObjectBody(jsonObject) // posting json
                //.addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))
                //   .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(response.optString("resultCode").equalsIgnoreCase("1059")) {
                            MyApplication.hideLoader();
                            responce_handler.failure(response.optString("resultDescription"));
                        }else{
                            responce_handler.success(response);
                        }
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void POST_REQEST_REGISTER(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setOkHttpClient(client)
                // .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        responce_handler.failure(error.getErrorDetail());
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void POST_REQEST_TransferAMount(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL_AMOUNT+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setOkHttpClient(client)
                // .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);

                        Log.d(TAG, "onResponse sonu object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        responce_handler.failure(error.getErrorDetail());
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    // String idProofTypeCode, String customerCode,
    public static void Upload_REQUEST_WH(String URL, File file,String idProofTypeCode,String customerCode, final Api_Responce_Handler responce_handler){


        AndroidNetworking.upload(BASEURL+URL)
                .addHeaders("channel","APP")
                .addMultipartFile("file",file)
                .addMultipartParameter("idProofTypeCode",idProofTypeCode)
                .addMultipartParameter("customerCode",customerCode)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okClientfileUpload)
                .setContentType("multipart/form-data")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .build()
                // setting an executor to get response or completion on that executor thread
                /* .setUploadProgressListener(new UploadProgressListener() {
                     @Override
                     public void onProgress(long bytesUploaded, long totalBytes) {
                         // do anything with progress
                     }
                 })*/
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);
                        // below code will be executed in the executor provided
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    public static void Upload_REQEST(String URL, File file,String docTypeCode, final Api_Responce_Handler responce_handler){


        AndroidNetworking.upload(BASEURL+URL)
                .addHeaders("channel","APP")
                .addMultipartFile("file",file)
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addMultipartParameter("docTypeCode",docTypeCode)
                .addMultipartParameter("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",MyApplication.getInstance()))
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okClientfileUpload)
                .setContentType("multipart/form-data")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .build()
                // setting an executor to get response or completion on that executor thread
                /* .setUploadProgressListener(new UploadProgressListener() {
                     @Override
                     public void onProgress(long bytesUploaded, long totalBytes) {
                         // do anything with progress
                     }
                 })*/
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);
                        // below code will be executed in the executor provided
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==400){
                                MyApplication.showAPIToast("Server not responding,please try again after some time..");

                            }
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }


    public static void Upload_REQESTID(String URL, String idProofTypeCode,String idProofNumber,File Frontfile,File BackFile,
                                       final Api_Responce_Handler responce_handler){


        AndroidNetworking.upload(BASEURL+URL)
                .addHeaders("channel","APP")
                .addMultipartFile("filefront",Frontfile)
                .addMultipartFile("fileback",BackFile)
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addMultipartParameter("docTypeCodeFront","100012")
                .addMultipartParameter("docTypeCodeback","100013")
                .addMultipartParameter("idProofTypeCode",idProofTypeCode)
                .addMultipartParameter("idProofNumber",idProofNumber)
                .addMultipartParameter("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",MyApplication.getInstance()))
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(clientBASIC)
                .setContentType("multipart/form-data")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .build()
                // setting an executor to get response or completion on that executor thread
                /* .setUploadProgressListener(new UploadProgressListener() {
                     @Override
                     public void onProgress(long bytesUploaded, long totalBytes) {
                         // do anything with progress
                     }
                 })*/
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);
                        // below code will be executed in the executor provided
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==400){
                                MyApplication.showAPIToast("Server not responding,please try again after some time..");

                            }
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }


    public static void Upload_REQEST_WH_NEW(String URL, File file,String filename, final Api_Responce_Handler responce_handler){


        AndroidNetworking.upload(BASEURL+URL)
                .addMultipartFile("file",file)
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addMultipartParameter("idProofTypeCode","100000")
                .addMultipartParameter("customerCode","1000000350")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okClientfileUpload)
                .setContentType("multipart/form-data")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .build()
                // setting an executor to get response or completion on that executor thread
                /* .setUploadProgressListener(new UploadProgressListener() {
                     @Override
                     public void onProgress(long bytesUploaded, long totalBytes) {
                         // do anything with progress
                     }
                 })*/
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);
                        // below code will be executed in the executor provided
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }


    public static void POST_REQEST_WH_NEW(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setOkHttpClient(client)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {

                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void PUT(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setOkHttpClient(client)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            if(errorJ.has("resultDescription")){
                                responce_handler.failure(errorJ.optString("resultDescription"));
                            }else{
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void PUT_Forgot_Pass(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }
    public static void GET_WF(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.get(BASEURL+URL)
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }
    public static void GET(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.get(BASEURL+URL)
                .setOkHttpClient(client)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))

                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void GET_PUBLIC(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.get(BASEURL+URL)
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("source","SUBSCRIBER")
                //   .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))

                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void GET_PUBLICN(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.get(URL)
                .setOkHttpClient(okClient)

                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void PUT_REQUEST_NEW(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void PUT_REQEST_WH(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(response.optBoolean("success")) {
                            responce_handler.success(response);
                        }else{
                            MyApplication.hideLoader();
                            responce_handler.failure(response.optString("errorMessage"));
                        }
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void POST_LOGIN_OTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("channel","APP")

                .addHeaders("type","LOGINOTP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)

                .setPriority(Priority.MEDIUM)
                .setTag("LOGINOTP")
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        responce_handler.failure(error.getErrorDetail());
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    public static void POST_GET_OTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("channel","APP")
                //.addHeaders("Authorization","Bearer b1b80862-17b3-48f0-83a3-b4d27ddd09e2")

                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))

                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)

                .setPriority(Priority.MEDIUM)
                .setTag("LOGINOTP")
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {

                        responce_handler.failure(error.getErrorDetail());
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void POST_REQEST_GenerateLogin(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type",jsonObject.optString("grant_type"))
                .addBodyParameter("scope",jsonObject.optString("scope"))

                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                // .addHeaders("Authorization", "Basic Y2FzaG1vb3Y6MTIzNDU2")
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("type", "LOGINOTP")
                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject jsonObject1=new JSONObject(error.getErrorBody());
                            responce_handler.failure(jsonObject1.optString("error_message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public static void PUT_SET_PASS(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","SUBSCRIBER")
                .addHeaders("channel","APP")
                .addHeaders("type", "LOGINOTP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        try {
                            JSONObject jsonObject1=new JSONObject(error.getErrorBody());
                            responce_handler.failure(jsonObject1.optString("error_message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }



}

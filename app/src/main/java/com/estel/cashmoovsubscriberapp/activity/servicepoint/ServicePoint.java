package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.LatLongModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ServicePoint extends FragmentActivity  implements View.OnClickListener,OnMapReadyCallback {
    public static ServicePoint servicepointC;
    ImageView imgBack,imgHome;
    LinearLayout linNext;
    private ArrayList<LatLongModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_point);
        servicepointC=this;
        setBackMenu();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getIds();
    }

   /* @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(servicepointC);
                onBackPressed();
               // onSupportNavigateUp();
            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(servicepointC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    /*OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            callApiLocations(googleMap);

//                locationList.clear();
//
//                locationList.add(new LatLongModel(1,"Boke","","","10.9407",
//                        "14.2803",""));
//                locationList.add(new LatLongModel(2,"conakry","","","9.9456",
//                        "9.6966",""));
//                locationList.add(new LatLongModel(3,"Faranah","","","10.0451",
//                        "10.7492",""));
//
//                for (int i = 0; i < locationList.size(); i++) {
//                    LatLng latLong = new LatLng(Double.parseDouble(locationList.get(i).getLatitude()),Double.parseDouble(locationList.get(i).getLognitude()));
//
//                    googleMap.addMarker(new MarkerOptions().position(latLong).title(locationList.get(i).getName()));
//                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationList.get(i)));
//                }

        }
    };*/
    private void getIds() {
        //linNext = findViewById(R.id.linNext);





      //  setOnCLickListener();

    }
//
//    private void setOnCLickListener() {
//        linNext.setOnClickListener(servicepointC);
//    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.linNext:
//                intent = new Intent(servicepointC, SearchResult.class);
//                startActivity(intent);
//                break;

        }
    }

    //http://202.131.144.130:8081/ewallet/api/v1/config/all

    private void callApiLocations(GoogleMap googleMap) {
        try {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            API.GET_PUBLIC("ewallet/api/v1/config/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {
                                locationList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray locationListArr = jsonObject.optJSONArray("locationDataList");
                                    for (int i = 0; i < locationListArr.length(); i++) {
                                        JSONObject data = locationListArr.optJSONObject(i);
                                        if(i==0){
                                            locationList.add(new LatLongModel(
                                                    data.optInt("id"),
                                                    "Cashmoov",
                                                    "CASHMOOV, Conakry, Guinea",
                                                   "CASHMOOV, Conakry, Guinea",
                                                    "9.50916",
                                                    "-13.71185",
                                                    data.optString("info")

                                            ));
                                        }
                                        locationList.add(new LatLongModel(
                                                data.optInt("id"),
                                                data.optString("name"),
                                                data.optString("address"),
                                                data.optString("outlateName"),
                                                data.optString("latitude"),
                                                data.optString("lognitude"),
                                                data.optString("info")

                                        ));

                                        LatLng latLong = new LatLng(Double.parseDouble(locationList.get(i).getLatitude()),Double.parseDouble(locationList.get(i).getLognitude()));
                                        googleMap.addMarker(new MarkerOptions().position(latLong).title(locationList.get(i).getName()));


                                    }
/*
                                    locationList.clear();

                                    for (int i = 0; i <= 2; i++) {


                                        if(i==0) {
                                            locationList.add(new LatLongModel(
                                                    i,
                                                    "Block D, Delta I, Greater Noida, Uttar Pradesh 201310",
                                                    "Block D, Delta I, Greater Noida, Uttar Pradesh 201310",
                                                    "Block D, Delta I, Greater Noida, Uttar Pradesh 201310",
                                                    "28.48941",
                                                    "77.52661",
                                                    "Block D, Delta I, Greater Noida, Uttar Pradesh 201310"
                                            ));
                                        }

                                        if(i==1) {
                                            locationList.add(new LatLongModel(
                                                    i,
                                                    "PLOT NO 27, near IFS SOCIETY, near NSG CHOWK, Amritpuram, Block E, Chandila, Gamma 1, Greater Noida, Uttar Pradesh 201308",
                                                    "PLOT NO 27, near IFS SOCIETY, near NSG CHOWK, Amritpuram, Block E, Chandila, Gamma 1, Greater Noida, Uttar Pradesh 201308",
                                                    "PLOT NO 27, near IFS SOCIETY, near NSG CHOWK, Amritpuram, Block E, Chandila, Gamma 1, Greater Noida, Uttar Pradesh 201308",
                                                    "28.48482",
                                                    "77.50103",
                                                    "PLOT NO 27, near IFS SOCIETY, near NSG CHOWK, Amritpuram, Block E, Chandila, Gamma 1, Greater Noida, Uttar Pradesh 201308"
                                            ));
                                        }

                                        if(i==2) {
                                            locationList.add(new LatLongModel(
                                                    i,
                                                    "HO-01, Noida Extension, Sector 1, Greater Noida, Uttar Pradesh 201306",
                                                    "HO-01, Noida Extension, Sector 1, Greater Noida, Uttar Pradesh 201306",
                                                    "HO-01, Noida Extension, Sector 1, Greater Noida, Uttar Pradesh 201306",
                                                    "28.58123",
                                                    "77.44543",
                                                    "HO-01, Noida Extension, Sector 1, Greater Noida, Uttar Pradesh 201306"
                                            ));
                                        }

                                            LatLng latLong = new LatLng(Double.parseDouble(locationList.get(i).getLatitude()),Double.parseDouble(locationList.get(i).getLognitude()));
                                            googleMap.addMarker(new MarkerOptions().position(latLong).title(locationList.get(i).getName())
                                            );

                                    }*/

                                    LatLng latLong = new LatLng(Double.parseDouble(locationList.get(0).getLatitude()),Double.parseDouble(locationList.get(0).getLognitude()));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong , 18.0f) );



                                } else {
                                    MyApplication.showToast(servicepointC,jsonObject.optString("resultDescription", "N/A"));
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        callApiLocations(googleMap);
    }
}
package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

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

public class ServicePoint extends AppCompatActivity implements View.OnClickListener {
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
        //linNext = findViewById(R.id.linNext);

        OnMapReadyCallback callback = new OnMapReadyCallback() {

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
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

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
            case R.id.linNext:
                intent = new Intent(servicepointC, SearchResult.class);
                startActivity(intent);
                break;

        }
    }

    //http://202.131.144.130:8081/ewallet/api/v1/config/all

    private void callApiLocations(GoogleMap googleMap) {
        try {
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

                                    LatLng latLong = new LatLng(Double.parseDouble(locationList.get(0).getLatitude()),Double.parseDouble(locationList.get(0).getLognitude()));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong , 5.0f) );



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

}
package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ServicePoint extends FragmentActivity  implements View.OnClickListener,OnMapReadyCallback {
    public static ServicePoint servicepointC;
    ImageView imgBack,imgHome;
    public static ArrayList<LatLongModel> locationList = new ArrayList<>();
    private GoogleMap googleMapNew;
    SearchView searchView;
    TextView textView;
    int resultPos = -1;
    Boolean isAllFabsVisible;
    FloatingActionButton fab,fabOne,fabTwo,fabThree,fabFour;
    TextView tvOne,tvTwo,tvThree,tvFour;


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
        textView = findViewById(R.id.textView);
        fab = findViewById(R.id.fab);
        fabOne = findViewById(R.id.fabOne);
        fabTwo = findViewById(R.id.fabTwo);
        fabThree = findViewById(R.id.fabThree);
        fabFour = findViewById(R.id.fabFour);
        tvOne = findViewById(R.id.tvOne);
        tvTwo = findViewById(R.id.tvTwo);
        tvThree = findViewById(R.id.tvThree);
        tvFour = findViewById(R.id.tvFour);

        fabOne.setVisibility(View.GONE);
        tvOne.setVisibility(View.GONE);
        fabTwo.setVisibility(View.GONE);
        tvTwo.setVisibility(View.GONE);
        fabThree.setVisibility(View.GONE);
        tvThree.setVisibility(View.GONE);
        fabFour.setVisibility(View.GONE);
        tvFour.setVisibility(View.GONE);

        isAllFabsVisible = false;


//        searchView=findViewById(R.id.searchView);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(servicepointC,
//                        SearchResult.class);
//                startActivityForResult(intent , 305);
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                int pos=-1;
//                for(int i=0;i<locationList.size();i++){
//                    if(locationList.get(i).getName().equalsIgnoreCase(query)){
//                        pos=i;
//                    }
//                }
//
//                if(pos!=-1){
//                    LatLng latLong = new LatLng(Double.parseDouble(locationList.get(pos).getLatitude()),Double.parseDouble(locationList.get(pos).getLognitude()));
//                    googleMapNew.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong , 18.0f) );
//                }else{
//                    Toast.makeText(ServicePoint.this, "No Match found",Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });


       setOnCLickListener();

    }

    private void setOnCLickListener() {
        textView.setOnClickListener(servicepointC);
        fab.setOnClickListener(servicepointC);
        fabOne.setOnClickListener(servicepointC);
        fabTwo.setOnClickListener(servicepointC);
        fabThree.setOnClickListener(servicepointC);
        fabFour.setOnClickListener(servicepointC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 305 && resultCode == RESULT_OK) {

            resultPos = data.getIntExtra("POSITION",-1);
//            searchView.setActivated(true);
//            searchView.onActionViewExpanded();
//            searchView.setIconified(false);
//            searchView.clearFocus();
//            searchView.setQuery(locationList.get(resultPos).getName());
            if(resultPos!=-1){
                LatLng latLong = new LatLng(Double.parseDouble(locationList.get(resultPos).getLatitude()),Double.parseDouble(locationList.get(resultPos).getLognitude()));
                googleMapNew.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong , 18.0f) );
                //Toast.makeText(ServicePoint.this, "Location is "+locationList.get(resultPos).getName(),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ServicePoint.this, "No Match found",Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.textView:
                intent = new Intent(servicepointC,
                        SearchResult.class);
                startActivityForResult(intent , 305);
                break;
            case R.id.fab:
                if (!isAllFabsVisible) {
                    fab.setImageResource(R.drawable.ic_baseline_close_24);
                    fabOne.show();
                    fabTwo.show();
                    fabThree.show();
                    fabFour.show();
                    tvOne.setVisibility(View.VISIBLE);
                    tvTwo.setVisibility(View.VISIBLE);
                    tvThree.setVisibility(View.VISIBLE);
                    tvFour.setVisibility(View.VISIBLE);
                    isAllFabsVisible = true;
                } else {
                    fab.setImageResource(R.drawable.ic_baseline_add_24);
                    fabOne.hide();
                    fabTwo.hide();
                    fabThree.hide();
                    fabFour.hide();
                    tvOne.setVisibility(View.GONE);
                    tvTwo.setVisibility(View.GONE);
                    tvThree.setVisibility(View.GONE);
                    tvFour.setVisibility(View.GONE);
                    isAllFabsVisible = false;
                }
                break;
            case R.id.fabOne:
                googleMapNew.setMapType(GoogleMap.MAP_TYPE_NONE);
                tvOne.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvTwo.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvThree.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvFour.setTextColor(getResources().getColor(R.color.textColorDBlack));
                break;
            case R.id.fabTwo:
                googleMapNew.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                tvOne.setTextColor(getResources().getColor(R.color.white));
                tvTwo.setTextColor(getResources().getColor(R.color.white));
                tvThree.setTextColor(getResources().getColor(R.color.white));
                tvFour.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.fabThree:
                googleMapNew.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                tvOne.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvTwo.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvThree.setTextColor(getResources().getColor(R.color.textColorDBlack));
                tvFour.setTextColor(getResources().getColor(R.color.textColorDBlack));
                break;
            case R.id.fabFour:
                googleMapNew.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                tvOne.setTextColor(getResources().getColor(R.color.white));
                tvTwo.setTextColor(getResources().getColor(R.color.white));
                tvThree.setTextColor(getResources().getColor(R.color.white));
                tvFour.setTextColor(getResources().getColor(R.color.white));
                break;


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

                                    //JSONArray locationListArr = loc();
                                    for (int i = 0; i < locationListArr.length(); i++) {
                                        JSONObject data = locationListArr.optJSONObject(i);
                                       /* if(i==0){
                                            locationList.add(new LatLongModel(
                                                    data.optInt("id"),
                                                    "Cashmoov",
                                                    "CASHMOOV, Conakry, Guinea",
                                                   "CASHMOOV, Conakry, Guinea",
                                                    "9.50916",
                                                    "-13.71185",
                                                    data.optString("info")

                                            ));
                                        }*/
                                        locationList.add(new LatLongModel(
                                                data.optInt("id"),
                                                data.optString("name"),
                                                data.optString("mssisdn"),
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
        if(resultPos==-1){

            callApiLocations(googleMap);
            googleMapNew=googleMap;
        }
    }


    public JSONArray loc() {
        try {
            return new JSONArray(" [ \n" +
                    "\n" +
                    " {\n" +
                    "      \"id\": 1,\n" +
                    "      \"name\": \"Cashmoov\",\n" +
                    "      \"address\": \"CASHMOOV, Conakry, Guinea\",\n" +
                    "      \"outlateName\": \"CASHMOOV outlate\",\n" +
                    "      \"latitude\": \"9.50916\",\n" +
                    "      \"lognitude\": \"-13.71185\",\n" +
                    "      \"info\": \"CASHMOOV Extra Info to show\"\n" +
                    "  },{\n" +
                    "      \"id\": 2,\n" +
                    "      \"name\": \"Kaporo\",\n" +
                    "      \"address\": \"Conakry, Guinea\",\n" +
                    "      \"outlateName\": \"Kaporo outlate\",\n" +
                    "      \"latitude\": \"9.6128514\",\n" +
                    "      \"lognitude\": \"-13.6443255\",\n" +
                    "      \"info\": \"Kaporo Extra Info to show\"\n" +
                    "  },\n" +
                    "      {\n" +
                    "          \"id\": 3,\n" +
                    "          \"name\": \"Lambanyi \",\n" +
                    "          \"address\": \"clinic.2espoir@yahoo.fr, Guinea\",\n" +
                    "          \"outlateName\": \"Lambanyi outlate\",\n" +
                    "          \"latitude\": \"9.6441644\",\n" +
                    "          \"lognitude\": \"-13.6107783\",\n" +
                    "          \"info\": \"Lambanyi Extra Info to show\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "          \"id\": 4,\n" +
                    "          \"name\": \"Sodefa\",\n" +
                    "          \"address\": \"Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Sodefa outlate\",\n" +
                    "          \"latitude\": \"9.7232112\",\n" +
                    "          \"lognitude\": \"-13.4628236\",\n" +
                    "          \"info\": \"Sodefa Extra Info to show\"\n" +
                    "      },\n" +
                    "       {\n" +
                    "          \"id\": 5,\n" +
                    "          \"name\": \"Kagbelen\",\n" +
                    "          \"address\": \"Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Kagbelen outlate\",\n" +
                    "          \"latitude\": \"9.7150686\",\n" +
                    "          \"lognitude\": \"-13.5027974\",\n" +
                    "          \"info\": \"Kagbelen Extra Info to show\"\n" +
                    "      },\n" +
                    "       {\n" +
                    "          \"id\": 6,\n" +
                    "          \"name\": \"Sonfonia\",\n" +
                    "          \"address\": \"MC8Q+QP2, Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Sonfonia outlate\",\n" +
                    "          \"latitude\": \"9.666807\",\n" +
                    "          \"lognitude\": \"-13.560616\",\n" +
                    "          \"info\": \"Sonfonia Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 7,\n" +
                    "          \"name\": \"Coyah\",\n" +
                    "          \"address\": \"N1, Coyah, Guinea\",\n" +
                    "          \"outlateName\": \"Coyah outlate\",\n" +
                    "          \"latitude\": \"9.7082253\",\n" +
                    "          \"lognitude\": \"-13.3816784\",\n" +
                    "          \"info\": \"Coyah Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 8,\n" +
                    "          \"name\": \"Madina\",\n" +
                    "          \"address\": \"Rte du Niger, Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Madina outlate\",\n" +
                    "          \"latitude\": \"9.5447134\",\n" +
                    "          \"lognitude\": \"-13.6662351\",\n" +
                    "          \"info\": \"Madina Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 9,\n" +
                    "          \"name\": \"Matam\",\n" +
                    "          \"address\": \"H942+4GC, Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Matam outlate\",\n" +
                    "          \"latitude\": \"9.5555759\",\n" +
                    "          \"lognitude\": \"-13.6487493\",\n" +
                    "          \"info\": \"Matam Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 10,\n" +
                    "          \"name\": \"Cosa\",\n" +
                    "          \"address\": \"Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Cosa outlate\",\n" +
                    "          \"latitude\": \"9.606482\",\n" +
                    "          \"lognitude\": \"-13.6129768\",\n" +
                    "          \"info\": \"Cosa Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 11,\n" +
                    "          \"name\": \"Cité enco 5\",\n" +
                    "          \"address\": \"Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Cité enco 5 outlate\",\n" +
                    "          \"latitude\": \"9.6292325\",\n" +
                    "          \"lognitude\": \"-13.5932317\",\n" +
                    "          \"info\": \"Cité enco 5 Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 12,\n" +
                    "          \"name\": \"Matoto\",\n" +
                    "          \"address\": \"JC64+VCM, Matoto, Guinea\",\n" +
                    "          \"outlateName\": \"Matoto outlate\",\n" +
                    "          \"latitude\": \"9.6120084\",\n" +
                    "          \"lognitude\": \"-13.5939988\",\n" +
                    "          \"info\": \"Matoto Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 13,\n" +
                    "          \"name\": \"Enta\",\n" +
                    "          \"address\": \"N1, Conakry, Guinea\",\n" +
                    "          \"outlateName\": \"Enta outlate\",\n" +
                    "          \"latitude\": \"9.6424487\",\n" +
                    "          \"lognitude\": \"-13.5631677\",\n" +
                    "          \"info\": \"Enta Extra Info to show\"\n" +
                    "      }, {\n" +
                    "          \"id\": 14,\n" +
                    "          \"name\": \"KM 36\",\n" +
                    "          \"address\": \"JC64+VCM, Matoto, Guinea\",\n" +
                    "          \"outlateName\": \"KM 36 outlate\",\n" +
                    "          \"latitude\": \"9.7057265\",\n" +
                    "          \"lognitude\": \"-13.495398\",\n" +
                    "          \"info\": \"KM 36 Extra Info to show\"\n" +
                    "      }\n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      \n" +
                    "      ]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
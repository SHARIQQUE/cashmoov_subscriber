package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.LogoutAppCompactActivity;
import com.estel.cashmoovsubscriberapp.activity.airtimepurchase.Contact;
import com.estel.cashmoovsubscriberapp.adapter.SearchResultListAdapter;
import com.estel.cashmoovsubscriberapp.listners.LocationListLisners;
import com.estel.cashmoovsubscriberapp.model.LatLongModel;

import java.util.ArrayList;

public class SearchResult extends LogoutAppCompactActivity implements LocationListLisners {
    public static SearchResult searchresultC;
    ImageView imgBack,imgHome;
    RecyclerView rvLocationList;
    SearchView searchView;
    public String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchresultC=this;
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
                MyApplication.hideKeyboard(searchresultC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(searchresultC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void getIds() {

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText.toString();
                // if (newText.length() >= 9) {
                setSearchResult(newText);
                // }
                return false;
            }

        });


        rvLocationList = findViewById(R.id.rvLocationList);

        searchResultListAdapter = new SearchResultListAdapter(searchresultC,ServicePoint.locationList);
        rvLocationList.setHasFixedSize(true);
        rvLocationList.setLayoutManager(new LinearLayoutManager(searchresultC, LinearLayoutManager.VERTICAL,false));
        rvLocationList.setAdapter(searchResultListAdapter);

    }
    SearchResultListAdapter searchResultListAdapter;


    public void setSearchResult(String searchText) {

        ArrayList<LatLongModel> searchModels = new ArrayList<>();
        for (LatLongModel item : ServicePoint.locationList) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getOutlateName().toLowerCase().contains(searchText.toLowerCase())) {
                searchModels.add(item);
            }
        }

        searchResultListAdapter.onDataChange(searchModels);
    }


    @Override
    public void onLocationListViewItemClick(int pos) {

        Intent intent = getIntent();
        intent.putExtra("POSITION",pos);
        setResult(RESULT_OK, intent);
        finish();

    }
}

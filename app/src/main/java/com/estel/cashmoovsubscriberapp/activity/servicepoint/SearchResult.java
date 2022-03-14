package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.SearchResultListAdapter;
import com.estel.cashmoovsubscriberapp.listners.LocationListLisners;

public class SearchResult extends AppCompatActivity implements LocationListLisners {
    public static SearchResult searchresultC;
    ImageView imgBack,imgHome;
    RecyclerView rvLocationList;

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
        rvLocationList = findViewById(R.id.rvLocationList);

        SearchResultListAdapter searchResultListAdapter = new SearchResultListAdapter(searchresultC,ServicePoint.locationList);
        rvLocationList.setHasFixedSize(true);
        rvLocationList.setLayoutManager(new LinearLayoutManager(searchresultC, LinearLayoutManager.VERTICAL,false));
        rvLocationList.setAdapter(searchResultListAdapter);

    }


    @Override
    public void onLocationListViewItemClick(int pos) {

        Intent intent = getIntent();
        intent.putExtra("POSITION",pos);
        setResult(RESULT_OK, intent);
        finish();

    }
}

package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;

public class SearchResult extends AppCompatActivity implements View.OnClickListener {
    public static SearchResult searchresultC;
    ImageButton bt_close;
    //TextView tvSend;

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
        bt_close = findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }


    private void getIds() {
       // tvSend = findViewById(R.id.tvSend);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
       // tvSend.setOnClickListener(searchresultC);
    }


    @Override
    public void onClick(View view) {

    }

}

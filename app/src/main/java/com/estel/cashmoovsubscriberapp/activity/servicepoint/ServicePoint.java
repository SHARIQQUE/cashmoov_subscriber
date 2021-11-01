package com.estel.cashmoovsubscriberapp.activity.servicepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;

public class ServicePoint extends AppCompatActivity implements View.OnClickListener {
    public static ServicePoint servicepointC;
  //  ImageView imgBack;
    LinearLayout linNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_point);
        servicepointC=this;
       // setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }


    private void getIds() {
        linNext = findViewById(R.id.linNext);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        linNext.setOnClickListener(servicepointC);
    }


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

}
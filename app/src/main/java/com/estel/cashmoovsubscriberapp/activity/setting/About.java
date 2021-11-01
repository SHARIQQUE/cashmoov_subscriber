package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;

public class About extends AppCompatActivity {
    public static About aboutC;
    Button btnVisitSite;
    TextView tvUserId;
    // ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aboutC=this;
        //setBackMenu();
        getIds();


        // sharique commnet Subscriber app
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
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
        tvUserId = findViewById(R.id.tvUserId);
        btnVisitSite = findViewById(R.id.btnVisitSite);

        tvUserId.setText(getString(R.string.user_id)+" : "+ MyApplication.getSaveString("userCode", aboutC));

        btnVisitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL();
            }
        });

    }
    public void openURL()  {
        String url="https://www.cashmoov.net";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.startActivity(intent);
    }


}
package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.adapter.TermsConditionsAdapter;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;
import com.estel.cashmoovsubscriberapp.model.TermsConditionsModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfferPromotionDetailActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;
    private ImageView ic_close;
    private CircleImageView img_offer_logo;
    private TextView sub_txt_content,txt_header,txt_content,txt_name,txt_title,txt_time,txt_view_more,txt_terms_one,txt_terms_two;
    private BottomSheetBehavior behavior;
    private RecyclerView rvbottom;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_promotion_detail);
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
                MyApplication.isFirstTime=false;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        img_offer_logo = findViewById(R.id.img_offer_logo);
        txt_header = findViewById(R.id.txt_header);
        txt_content = findViewById(R.id.txt_content);
        txt_time = findViewById(R.id.txt_date_time);
        sub_txt_content= findViewById(R.id.sub_txt_content);
        OfferPromotionModel data= MyApplication.offerPromotionModelArrayList.get(MyApplication.offerPromtionPos);
        txt_header.setText(data.getHeading());
        txt_content.setText(data.getDescription());
        sub_txt_content.setText(data.getStatus());
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//        Date date = null;
//        try {
//            date = inputFormat.parse(data.getToDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String formattedDate = outputFormat.format(date);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            Date date = null;
            date = inputFormat.parse(data.getToDate());
            String formattedDate = outputFormat.format(date);
            txt_time.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(data.getPromOfferTypeName().equalsIgnoreCase("Both")||
                data.getPromOfferTypeName().equalsIgnoreCase("Image")){
            img_offer_logo.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.logo200x70b)
                            .error(R.drawable.logo200x70b))
                    .load(API.BASEURL+"ewallet/api/v1/promOfferTemplate/download/"+data.getCode()+"/"+data.getFileName())
                    .into(img_offer_logo);
        }else{
            img_offer_logo.setVisibility(View.INVISIBLE);
        }



//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//
//
//        View bottomSheet = findViewById(R.id.bottom_sheet);
//        behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                // React to state change
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                // React to dragging events
//            }
//        });
//
//        rvbottom = findViewById(R.id.rvbottom);
//        TermsConditionsModel[] termsConditionsList = new TermsConditionsModel[] {
//                new TermsConditionsModel(1,"Offer Details","* Merchane will receive upto 100 points on accepting 6 payments in one day."),
//                new TermsConditionsModel(2,"Offer Eligibility","* The offer is only eligible on payments accepted through cashmoov All-in-One QR. \n"+
//                        "* Offer is valid for select merchants registered with Casgmoov."),
//                new TermsConditionsModel(3,"Offer Conditions","* The value of each point earned is equivalent to INR 0.10, i.e. 1 point = 1- paise. \n"+
//                        "* Minimum amount of each payment should be ₹20. \n"+
//                        "* Offer is valid on payments accepted using Cashmoov All-in-One QR Code of the merchant."),
//        };
//
//        rvbottom.setHasFixedSize(true);
//        // rv_offer_promotion.setLayoutManager(new GridLayoutManager(this,2));
//        rvbottom.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        rvbottom.setItemAnimator(new DefaultItemAnimator());
//        TermsConditionsAdapter termsConditionsAdapter = new TermsConditionsAdapter(this,termsConditionsList);
//        rvbottom.setAdapter(termsConditionsAdapter);
//        termsConditionsAdapter.notifyDataSetChanged();
//
//        txt_view_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//
//        ic_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        });

    }


}

package com.estel.cashmoovsubscriberapp.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.google.android.material.radiobutton.MaterialRadioButton;;

public class ChangeLanguage extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static ChangeLanguage changelanguageC;
    MaterialRadioButton sbFrench, sbEnglish;
    TextView tvChange;

    // ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        changelanguageC = this;
        //setBackMenu();
        getIds();
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
        sbFrench = findViewById(R.id.sbFrench);
        sbEnglish = findViewById(R.id.sbEnglish);
        tvChange = findViewById(R.id.tvChange);

        MyApplication.setLang(changelanguageC);
        MyApplication.lang = MyApplication.getSaveString("Locale", changelanguageC);
        if (MyApplication.lang.equalsIgnoreCase("en")) {
            //sbFrench.setChecked(false);
            sbEnglish.setChecked(true);
        } else {
            sbFrench.setChecked(true);
            //sbFrench.setChecked(false);
        }

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvChange.setOnClickListener(changelanguageC);
        sbFrench.setOnCheckedChangeListener(changelanguageC);
        sbEnglish.setOnCheckedChangeListener(changelanguageC);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            if (compoundButton.getId() == R.id.sbFrench) {
                sbEnglish.setChecked(false);
            }
            if (compoundButton.getId() == R.id.sbEnglish) {
                sbFrench.setChecked(false); }
            }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvChange:
                langDialog();
                break;
        }

    }

    public void langDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_language))
                .setIcon(R.drawable.ic_baseline_translate_blue)
                .setMessage(getString(R.string.change_lang_conf_msg))
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(sbFrench.isChecked()){
                            MyApplication.changeLocale(changelanguageC, "fr");
                            MyApplication.saveString("Locale", "fr", changelanguageC);

                        } else if(sbEnglish.isChecked()){
                            MyApplication.changeLocale(changelanguageC, "en");
                            MyApplication.saveString("Locale", "en", changelanguageC);

                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).create().show();
    }


}
package com.estel.cashmoovsubscriberapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.login.PhoneNumberRegistrationScreen;
import com.estel.cashmoovsubscriberapp.activity.register.RegisterStepOne;

public class TutorialScreen extends LogoutAppCompactActivity implements View.OnClickListener {
    public static TutorialScreen tutorialscreenC;
    TextView tvStart,login;
    VideoView videoView;
    RelativeLayout relPlay;
    String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        tutorialscreenC = this;
        getIds();
    }

    private void getIds() {
        login=  findViewById(R.id.login);
        tvStart = findViewById(R.id.tvStart);
        videoView = findViewById(R.id.videoView);
        relPlay = findViewById(R.id.relPlay);


        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(tutorialscreenC);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        // starts the video

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvStart.setOnClickListener(tutorialscreenC);
        relPlay.setOnClickListener(tutorialscreenC);
        login.setOnClickListener(tutorialscreenC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.tvStart:
                intent = new Intent(tutorialscreenC, RegisterStepOne.class);
                startActivity(intent);

                break;
            case R.id.relPlay:
                videoView.start();
                relPlay.setVisibility(View.GONE);
                break;
            case R.id.login:
                intent = new Intent(tutorialscreenC, PhoneNumberRegistrationScreen.class);
                startActivity(intent);
                break;
        }

    }
}
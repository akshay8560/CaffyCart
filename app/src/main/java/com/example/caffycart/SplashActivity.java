package com.example.caffycart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    VideoView coffeeSplash;
    public final int totalSplashScreenTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        // This will remove the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // this is for the main splash screen video
        coffeeSplash = (VideoView) findViewById(R.id.caffySplash);
        String splash_path = "android.resource://" + getPackageName() + "/" + R.raw.cafy_cart;
        Uri videoSplash = Uri.parse(splash_path);
        coffeeSplash.setVideoURI(videoSplash);
        coffeeSplash.start();
        coffeeSplash.setOnCompletionListener(mediaPlayer -> coffeeSplash.start());

        //calling new activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, totalSplashScreenTime);

    }
}
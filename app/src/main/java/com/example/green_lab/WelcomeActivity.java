package com.example.green_lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView plantImage;
    private Button btnIntro, btnAdventure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        plantImage = findViewById(R.id.iv_plant_main);
        btnIntro = findViewById(R.id.btn_watch_intro);
        btnAdventure = findViewById(R.id.btn_start_adventure);

        // Interaction 1: Meaningful Tap Animation + Pop Sound
        plantImage.setOnClickListener(v -> {
            // Trigger the pop sound safely!
            SoundManager.playPop(this);

            ScaleAnimation bounce = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            bounce.setDuration(300);
            bounce.setRepeatMode(Animation.REVERSE);
            bounce.setRepeatCount(1);
            plantImage.startAnimation(bounce);
        });

        // Intent Navigation + Click Sounds
        btnIntro.setOnClickListener(v -> {
            SoundManager.playClick(this);
            startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        });

        btnAdventure.setOnClickListener(v -> {
            SoundManager.playClick(this);
            startActivity(new Intent(WelcomeActivity.this, OpenCVScannerActivity.class));
        });
    }
}
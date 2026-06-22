package com.example.green_lab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        VideoView videoView = findViewById(R.id.video_intro);
        Button btnScanner = findViewById(R.id.btn_start_scanner);

        // Load and play video
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.plant_timelapse;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Ensure the video loops endlessly!
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
        });

        btnScanner.setOnClickListener(v -> {
            // Audio Feedback: Standard button click sound
            SoundManager.playClick(this);

            startActivity(new Intent(IntroActivity.this, OpenCVScannerActivity.class));
            finish(); // Close intro so user doesn't go back to it
        });
    }
}
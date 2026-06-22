package com.example.green_lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvScore = findViewById(R.id.tv_final_score);
        TextView tvFeedback = findViewById(R.id.tv_score_feedback);
        TextView tvTitle = findViewById(R.id.tv_result_title);
        ImageView ivBadge = findViewById(R.id.iv_badge);
        Button btnRestart = findViewById(R.id.btn_restart);

        // 1. Retrieve and Display Score (Out of 5)
        int score = getIntent().getIntExtra("FINAL_SCORE", 0);
        tvScore.setText(score + " / 5");

        // 2. Dynamic Feedback Logic & Audio
        if (score == 5) {
            SoundManager.playSuccess(this); // Audio: Big win!
            tvTitle.setText("Perfect!");
            tvFeedback.setText("Amazing job! You are a master plant scientist!");
        } else if (score >= 3) {
            SoundManager.playSuccess(this); // Audio: Good job!
            tvTitle.setText("Great Job!");
            tvFeedback.setText("You know a lot about plants! Keep exploring.");
        } else {
            SoundManager.playPop(this); // Audio: Gentle pop for a low score
            tvTitle.setText("Good Try!");
            tvFeedback.setText("Plant science can be tricky. Try again to learn more!");
            ivBadge.setAlpha(0.5f); // Dim the badge slightly if they got a low score
        }

        // 3. Pop-in Animation for the Badge
        ivBadge.setScaleX(0f);
        ivBadge.setScaleY(0f);
        ivBadge.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator())
                .start();

        // 4. Play Again Button Logic
        btnRestart.setOnClickListener(v -> returnToStart());

        // 5. Intercept the hardware Back Button!
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // If they press the back button, act exactly like the restart button
//                returnToStart();
//            }
//        });
    }

    // Helper method to safely clear the stack, play a sound, and restart
    private void returnToStart() {
        // Audio Feedback: Standard click when restarting
        SoundManager.playClick(this);

        Intent intent = new Intent(ResultActivity.this, WelcomeActivity.class);
        // This completely wipes the back stack, destroying the broken Simulation screen
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
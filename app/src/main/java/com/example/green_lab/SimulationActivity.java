package com.example.green_lab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SimulationActivity extends AppCompatActivity {

    // State tracker to prevent overlapping sounds
    // 0 = Dark, 1 = Perfect, 2 = Too Hot
    private int currentPlantState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        SeekBar seekBar = findViewById(R.id.seekbar_sun);
        ImageView plantImage = findViewById(R.id.iv_sim_plant);
        TextView tvFeedback = findViewById(R.id.tv_sim_feedback);
        Button btnQuiz = findViewById(R.id.btn_to_quiz);

        ProgressBar pbHeat = findViewById(R.id.pb_heat);
        ProgressBar pbWater = findViewById(R.id.pb_water);
        TextView tvTheory = findViewById(R.id.tv_theory_explanation);

        ImageView ivSun = findViewById(R.id.iv_dynamic_sun);
        ImageView ivMascot = findViewById(R.id.iv_mascot_quiz);

        // 1. Declare the listener as an explicit variable
        SeekBar.OnSeekBarChangeListener seekbarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                pbHeat.setProgress(progress);
                pbWater.setProgress(100 - progress);

                // Dynamic Sun Logic
                float scale = 0.2f + (progress / 100f) * 1.3f;
                ivSun.setScaleX(scale);
                ivSun.setScaleY(scale);
                ivSun.setAlpha((progress / 100f));

                // Determine the NEW state based on the slider
                int newState;
                if (progress < 30) {
                    newState = 0; // Dark
                } else if (progress <= 85) {
                    newState = 1; // Perfect
                } else {
                    newState = 2; // Too Hot
                }

                // Only update UI and play sound if state CHANGED
                if (newState != currentPlantState) {
                    currentPlantState = newState; // Update tracker

                    if (newState == 0) {
                        // Dark Zone
                        // 'if (fromUser)' stops sound glitches from playing when the screen first opens!
                        if (fromUser) SoundManager.playPop(SimulationActivity.this);
                        plantImage.setImageResource(R.drawable.ic_plant_small);
                        ivMascot.setImageResource(R.drawable.ic_mascot_idle);
                        tvFeedback.setText("Too little light!");
                        tvTheory.setText("Plants use sunlight as power to cook their food. In the dark, they have no energy to grow!");

                    } else if (newState == 1) {
                        // Perfect Zone
                        if (fromUser) SoundManager.playSuccess(SimulationActivity.this);
                        plantImage.setImageResource(R.drawable.ic_plant_perfect);
                        ivMascot.setImageResource(R.drawable.ic_mascot_happy);
                        tvFeedback.setText("Perfect Balance!");
                        tvTheory.setText("Just right! The bright sunlight gives the plant energy, and it has plenty of water to drink.");

                    } else if (newState == 2) {
                        // Danger Zone
                        if (fromUser) SoundManager.playWrong(SimulationActivity.this);
                        plantImage.setImageResource(R.drawable.ic_plant_wilted);
                        ivMascot.setImageResource(R.drawable.ic_mascot_idle);
                        tvFeedback.setText("Too Hot! Drying out!");
                        tvTheory.setText("Oh no! The hot sun is making the plant sweat out all its water (Transpiration). It's too thirsty!");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                SoundManager.playWhoosh(SimulationActivity.this);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        // 2. Attach the listener to the SeekBar
        seekBar.setOnSeekBarChangeListener(seekbarListener);

        // 3. BUG FIX: Manually trigger the listener once instantly upon Activity launch!
        // Passing 'false' for fromUser scales the sun down instantly without playing audio pop sounds.
        seekbarListener.onProgressChanged(seekBar, seekBar.getProgress(), false);

        // Quiz Button
        btnQuiz.setOnClickListener(v -> {
            SoundManager.playClick(this);
            startActivity(new Intent(SimulationActivity.this, QuizActivity.class));
        });
    }
}
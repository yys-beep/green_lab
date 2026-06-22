package com.example.green_lab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExploreActivity extends AppCompatActivity {

    // Track which parts have been found
    private boolean foundLeaf = false;
    private boolean foundRoots = false;
    private boolean foundStem = false;
    private boolean foundFlower = false;

    // Prevent the success sound from playing multiple times
    private boolean hasWon = false;

    private TextView tvProgressText;
    private ProgressBar pbExplore;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Map Hotspots
        Button hotspotLeaf = findViewById(R.id.hotspot_leaf);
        Button hotspotRoots = findViewById(R.id.hotspot_roots);
        Button hotspotStem = findViewById(R.id.hotspot_stem);
        Button hotspotFlower = findViewById(R.id.hotspot_flower);

        // Map UI Elements
        TextView tvInfoTitle = findViewById(R.id.tv_info_title);
        TextView tvInfoDesc = findViewById(R.id.tv_info_desc);
        tvProgressText = findViewById(R.id.tv_progress_text);
        pbExplore = findViewById(R.id.pb_explore);
        btnNext = findViewById(R.id.btn_go_simulation);

        // --- CLICK LISTENERS ---
        hotspotLeaf.setOnClickListener(v -> {
            SoundManager.playPop(this); // Audio Feedback
            foundLeaf = true;
            tvInfoTitle.setText("The Leaves 🍃");
            tvInfoDesc.setText("Leaves are the plant's kitchen! They use tiny doors called Stomata to breathe in air and catch sunlight to make food.");
            updateProgress();
        });

        hotspotRoots.setOnClickListener(v -> {
            SoundManager.playPop(this); // Audio Feedback
            foundRoots = true;
            tvInfoTitle.setText("The Roots 🪱");
            tvInfoDesc.setText("Roots act like strong anchors and straws! They hold the plant deep in the dirt and drink up water.");
            updateProgress();
        });

        hotspotStem.setOnClickListener(v -> {
            SoundManager.playPop(this); // Audio Feedback
            foundStem = true;
            tvInfoTitle.setText("The Stem 🎋");
            tvInfoDesc.setText("The stem is like an elevator! It carries water up from the roots and holds the plant up tall to reach the sun.");
            updateProgress();
        });

        hotspotFlower.setOnClickListener(v -> {
            SoundManager.playPop(this); // Audio Feedback
            foundFlower = true;
            tvInfoTitle.setText("The Flower 🌸");
            tvInfoDesc.setText("Flowers are the beautiful part of the plant that makes seeds, so brand new baby plants can grow!");
            updateProgress();
        });

        // Next Button Listener
        btnNext.setOnClickListener(v -> {
            SoundManager.playClick(this); // Audio Feedback
            startActivity(new Intent(ExploreActivity.this, SimulationActivity.class));
        });
    }

    // --- CHECK PROGRESS FUNCTION ---
    private void updateProgress() {
        int count = 0;
        if (foundLeaf) count++;
        if (foundRoots) count++;
        if (foundStem) count++;
        if (foundFlower) count++;

        // Update Tracker UI
        tvProgressText.setText("🔍 Parts Found: " + count + " / 4");
        pbExplore.setProgress(count);

        // Unlock Button if all 4 are found
        if (count == 4 && !hasWon) {
            hasWon = true;
            SoundManager.playSuccess(this); // Grand Finale Audio Feedback!

            btnNext.setEnabled(true);
            tvProgressText.setText("🌟 All parts found! You can go to the Lab!");
        }
    }
}
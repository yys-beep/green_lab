package com.example.green_lab;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FactoryActivity extends AppCompatActivity {
    private int itemsDropped = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        ImageView leafFactory = findViewById(R.id.iv_leaf_factory);
        ImageView dragSun = findViewById(R.id.drag_sun);
        ImageView dragWater = findViewById(R.id.drag_water);
        ImageView dragCo2 = findViewById(R.id.drag_co2);

        ImageView ivGlucose = findViewById(R.id.iv_glucose_output);
        ImageView ivOxygen = findViewById(R.id.iv_oxygen_output);
        ImageView ivMascot = findViewById(R.id.iv_mascot);

        Button btnNext = findViewById(R.id.btn_next_explore);
        TextView tvFeedback = findViewById(R.id.tv_factory_feedback);
        TextView tvEducationalDesc = findViewById(R.id.tv_educational_desc);
        LinearLayout llDraggables = findViewById(R.id.ll_draggables);

        // --- TOUCH LISTENER (When dragging starts) ---
        @SuppressLint("ClickableViewAccessibility")
        View.OnTouchListener touchListener = (view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Audio Feedback: Whoosh sound when picking up an item
                SoundManager.playWhoosh(this);

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            }
            return false;
        };

        dragSun.setOnTouchListener(touchListener);
        dragWater.setOnTouchListener(touchListener);
        dragCo2.setOnTouchListener(touchListener);

        // --- DRAG LISTENER (When dropping ends) ---
        leafFactory.setOnDragListener((v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View draggedView = (View) event.getLocalState();

                // DEFENSIVE PROGRAMMING: Prevent NullPointerException crash
                if (draggedView != null) {
                    // Audio Feedback: Pop sound when successfully dropped on the leaf
                    SoundManager.playPop(this);

                    draggedView.setVisibility(View.INVISIBLE);
                    itemsDropped++;

                    if (draggedView.getId() == R.id.drag_sun) {
                        tvFeedback.setText("☀️ Zap! The leaf caught sunlight for energy!");
                    } else if (draggedView.getId() == R.id.drag_water) {
                        tvFeedback.setText("💧 Slurp! The plant drank water using roots!");
                    } else if (draggedView.getId() == R.id.drag_co2) {
                        tvFeedback.setText("☁️ Whoosh! The leaf breathed in Carbon Dioxide!");
                    }

                    ivMascot.setImageResource(R.drawable.ic_mascot_happy);
                    ivMascot.animate().translationY(-50f).setDuration(200).withEndAction(() -> {
                        ivMascot.animate().translationY(0f).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
                    }).start();

                    if (itemsDropped < 3) {
                        handler.postDelayed(() -> {
                            ivMascot.setImageResource(R.drawable.ic_mascot_idle);
                        }, 2000);
                    }

                    // The Grand Finale
                    if (itemsDropped == 3) {
                        handler.postDelayed(() -> {
                            // Audio Feedback: Victory Chime!
                            SoundManager.playSuccess(this);

                            tvFeedback.setText("🎉 Photosynthesis Complete!");
                            tvEducationalDesc.setText("The leaf used Sunlight, Water, and Air to bake sweet Glucose (sugar) for energy. It also made fresh Oxygen for us to breathe!");

                            leafFactory.setImageResource(R.drawable.ic_leaf_factory_active);

                            llDraggables.setVisibility(View.INVISIBLE);
                            btnNext.setVisibility(View.VISIBLE);

                            ivGlucose.setVisibility(View.VISIBLE);
                            ivOxygen.setVisibility(View.VISIBLE);

                            ivGlucose.setScaleX(0f);
                            ivGlucose.setScaleY(0f);
                            ivGlucose.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new OvershootInterpolator()).start();

                            ivOxygen.setScaleX(0f);
                            ivOxygen.setScaleY(0f);
                            ivOxygen.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(new OvershootInterpolator()).start();

                            ivMascot.setImageResource(R.drawable.ic_mascot_happy);
                            ivMascot.animate().translationY(-100f).setDuration(500).withEndAction(() -> {
                                ivMascot.animate().translationY(0f).setDuration(500).setInterpolator(new OvershootInterpolator()).start();
                            }).start();

                        }, 2500);
                    }
                } else {
                    // Gracefully handle the error without crashing
                    return false;
                }
            }
            return true;
        });

        // --- BUTTON LISTENER ---
        btnNext.setOnClickListener(v -> {
            // Audio Feedback: Standard button click
            SoundManager.playClick(this);
            startActivity(new Intent(FactoryActivity.this, ExploreActivity.class));
        });
    }
}
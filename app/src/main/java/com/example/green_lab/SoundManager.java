package com.example.green_lab;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private static MediaPlayer mediaPlayer;

    // Core helper to clear old memory and play new sound safely
    private static void playSound(Context context, int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, soundResource);
        mediaPlayer.start();
    }

    // 1. Navigation Tap (Buttons)
    public static void playClick(Context context) {
        playSound(context, R.raw.click_sound);
    }

    // 2. Playful Interaction (Plant tap, Dragging)
    public static void playPop(Context context) {
        playSound(context, R.raw.pop_sound);
    }

    // 3. Reward Chime (Winning, Correct Quiz Answer)
    public static void playSuccess(Context context) {
        playSound(context, R.raw.success_sound);
    }

    // 4. Movement/Swipe (Sliders)
    public static void playWhoosh(Context context) {
        playSound(context, R.raw.whoosh_sound);
    }

    // 5. Try Again (Wrong Quiz Answer)
    public static void playWrong(Context context) {
        playSound(context, R.raw.wrong_sound);
    }
}
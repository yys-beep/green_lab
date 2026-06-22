package com.example.green_lab;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class OpenCVScannerActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase cameraView;
    private ProgressBar progressBar;
    private Button btnSwitchCamera;

    private boolean isUnlocked = false;
    private static final int CAMERA_PERMISSION_CODE = 100;

    // Track which camera is currently active (Default to BACK)
    private int currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv_scanner);

        progressBar = findViewById(R.id.pb_green_detect);
        cameraView = findViewById(R.id.camera_view);
        btnSwitchCamera = findViewById(R.id.btn_switch_camera);

        // Explicitly set the initial camera to the back lens
        cameraView.setCameraIndex(currentCameraId);

        // --- Camera Switch Button Logic ---
        btnSwitchCamera.setOnClickListener(v -> {
            // Audio Feedback: Standard click
            SoundManager.playClick(this);

            if (cameraView != null) {
                // 1. Turn off the current camera feed
                cameraView.disableView();

                // 2. Swap the ID
                if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_BACK) {
                    currentCameraId = CameraBridgeViewBase.CAMERA_ID_FRONT;
                } else {
                    currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK;
                }

                // 3. Apply the new ID and turn it back on
                cameraView.setCameraIndex(currentCameraId);
                cameraView.enableView();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initializeOpenCV();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    private void initializeOpenCV() {
        if (OpenCVLoader.initDebug()) {
            cameraView.setCameraPermissionGranted();
            cameraView.enableView();
            cameraView.setCvCameraViewListener(this);
        } else {
            Log.e("OpenCV", "Initialization Failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeOpenCV();
            } else {
                Toast.makeText(this, "Camera permission is required to play!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Mat hsv = new Mat();
        Mat mask = new Mat();

        Imgproc.cvtColor(rgba, hsv, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2HSV);

        Scalar lowerGreen = new Scalar(35, 50, 50);
        Scalar upperGreen = new Scalar(85, 255, 255);
        Core.inRange(hsv, lowerGreen, upperGreen, mask);

        int greenPixels = Core.countNonZero(mask);
        int totalPixels = rgba.rows() * rgba.cols();
        int percentage = (int) (((float) greenPixels / totalPixels) * 100);

        runOnUiThread(() -> {
            progressBar.setProgress(percentage * 2);
            if (progressBar.getProgress() >= 100 && !isUnlocked) {
                isUnlocked = true;
                showSuccessPopup();
            }
        });

        mask.release();
        hsv.release();

        // Important: When using the front camera, the image is mirrored.
        // OpenCV naturally flips it, so you don't need to write extra matrix flip code
        // unless the text/objects look backwards to the user!
        return rgba;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {}

    @Override
    public void onCameraViewStopped() {}

    private void showSuccessPopup() {
        // Audio Feedback: Victory Chime the moment they find enough green!
        SoundManager.playSuccess(this);

        new AlertDialog.Builder(this)
                .setTitle("Chlorophyll Found!")
                .setMessage("You found green chlorophyll! Let's enter the factory.")
                .setPositiveButton("Enter", (dialog, which) -> {
                    // Audio Feedback: Standard click to move to next screen
                    SoundManager.playClick(OpenCVScannerActivity.this);

                    startActivity(new Intent(OpenCVScannerActivity.this, FactoryActivity.class));
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
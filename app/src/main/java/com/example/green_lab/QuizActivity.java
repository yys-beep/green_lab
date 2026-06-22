package com.example.green_lab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private TextView tvProgress, tvQuestion;
    private ProgressBar pbProgress;
    private RadioGroup rgAnswers;
    private RadioButton rbAns1, rbAns2, rbAns3;
    private Button btnSubmit;

    private int currentQuestionIndex = 0;
    private int score = 0;

    // 1. The Quiz Data
    private String[] questions = {
            "What gas do plants breathe in through their leaves?",
            "What do plants use as 'power' to cook their food?",
            "Which part of the plant acts like a straw to drink water?",
            "What sweet food do plants make for themselves?",
            "What do plants release into the air that humans need to breathe?"
    };

    private String[][] options = {
            {"Oxygen", "Carbon Dioxide", "Helium"},
            {"Sunlight", "Electricity", "Moonlight"},
            {"Flowers", "Leaves", "Roots"},
            {"Pizza", "Glucose (Sugar)", "Salt"},
            {"Carbon Dioxide", "Water", "Oxygen"}
    };

    // Index of the correct answer (0 = Ans1, 1 = Ans2, 2 = Ans3)
    private int[] correctAnswers = {1, 0, 2, 1, 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvProgress = findViewById(R.id.tv_quiz_progress);
        pbProgress = findViewById(R.id.pb_quiz_progress);
        tvQuestion = findViewById(R.id.tv_question);
        rgAnswers = findViewById(R.id.rg_answers);
        rbAns1 = findViewById(R.id.rb_ans1);
        rbAns2 = findViewById(R.id.rb_ans2);
        rbAns3 = findViewById(R.id.rb_ans3);
        btnSubmit = findViewById(R.id.btn_submit_quiz);

        // Load the very first question
        loadQuestion();

        btnSubmit.setOnClickListener(v -> {
            int selectedId = rgAnswers.getCheckedRadioButtonId();

            if (selectedId == -1) {
                // Audio Feedback: Standard tap because they need to pick an option
                SoundManager.playClick(this);
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if they got it right
            int selectedAnswerIndex = -1;
            if (selectedId == R.id.rb_ans1) selectedAnswerIndex = 0;
            if (selectedId == R.id.rb_ans2) selectedAnswerIndex = 1;
            if (selectedId == R.id.rb_ans3) selectedAnswerIndex = 2;

            if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                score++;
                // Audio Feedback: Happy Success Chime!
                SoundManager.playSuccess(this);
                Toast.makeText(this, "Correct! 🌟", Toast.LENGTH_SHORT).show();
            } else {
                // Audio Feedback: Gentle "Try Again" bloop
                SoundManager.playWrong(this);
                Toast.makeText(this, "Oops! That's not right. 🌱", Toast.LENGTH_SHORT).show();
            }

            // Move to the next question or finish the quiz
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                loadQuestion();
            } else {
                // Quiz is over! Send score to ResultActivity
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("FINAL_SCORE", score);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion() {
        // Clear previous selection
        rgAnswers.clearCheck();

        // Update UI
        tvProgress.setText("Question " + (currentQuestionIndex + 1) + " of 5");
        pbProgress.setProgress(currentQuestionIndex + 1);

        tvQuestion.setText(questions[currentQuestionIndex]);
        rbAns1.setText(options[currentQuestionIndex][0]);
        rbAns2.setText(options[currentQuestionIndex][1]);
        rbAns3.setText(options[currentQuestionIndex][2]);

        // Change button text on the very last question
        if (currentQuestionIndex == questions.length - 1) {
            btnSubmit.setText("Finish Quiz!");
        } else {
            btnSubmit.setText("Submit Answer");
        }
    }
}
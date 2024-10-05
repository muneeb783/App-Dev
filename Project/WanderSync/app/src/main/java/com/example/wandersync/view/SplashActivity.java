package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersync.R;

public class SplashActivity extends AppCompatActivity {

    private static final String INITIAL_TEXT = "WS";
    private static final String FULL_TEXT = "WanderSync";
    private static final int ANIMATION_DELAY = 100; // Delay between each letter
    private static final int INITIAL_DELAY = 1000; // Delay before animation starts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView textView = findViewById(R.id.text_wander_sync);
        textView.setText(INITIAL_TEXT);

        // Delay before starting the animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateText(textView, FULL_TEXT);
        }, INITIAL_DELAY);

        // Start MainActivity after the full text animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, INITIAL_DELAY + (FULL_TEXT.length() * ANIMATION_DELAY));  // Adjusted duration
    }

    // Method to animate the text from "WS" to "WanderSync"
    private void animateText(TextView textView, String fullText) {
        Handler handler = new Handler(Looper.getMainLooper());

        // Initialize the text with "WS"
        StringBuilder currentText = new StringBuilder(INITIAL_TEXT);

// Animate the addition of "ander"
        for (int i = 1; i <= 5; i++) {  // Indices for "ander" (1 to 5)
            final int index = i; // Use final variable for the handler
            int delay = ANIMATION_DELAY * (index - 1);
            handler.postDelayed(() -> {
                // Insert characters after "W" (index 0)
                currentText.insert(index, fullText.charAt(index));
                textView.setText(currentText.toString());
            }, delay);
        }

// Animate the addition of "ync"
        for (int i = 7; i < fullText.length(); i++) {  // Indices for "ync" (6 to 8)
            final int index = i; // Use final variable for the handler
            int delay = ANIMATION_DELAY * (i - 5);
            handler.postDelayed(() -> {
                // Append characters after existing text
                currentText.append(fullText.charAt(index));
                textView.setText(currentText.toString());
            }, delay);
        }
    }
}

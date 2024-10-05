package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersync.R;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonQuit; // Declare buttonQuit as a class variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        buttonStart = findViewById(R.id.button_start);
        buttonQuit = findViewById(R.id.buttonQuit);

        // Set onClickListener for the Quit button
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // This will close the activity
            }
        });

        // Apply window insets to ensure proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set onClickListener for the Start button
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

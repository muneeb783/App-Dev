package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.wandersync.R;


public class CreateAccountActivity extends AppCompatActivity {


    private EditText createUsernameInput, createEmailInput, createPasswordInput;
    private Button createAccountButton;
    private ImageButton backButtonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        createUsernameInput = findViewById(R.id.create_username_input);
        createEmailInput = findViewById(R.id.create_email_input);
        createPasswordInput = findViewById(R.id.create_password_input);
        createAccountButton = findViewById(R.id.create_account_button);
        backButtonLogin = findViewById(R.id.button_back_login);




        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = createUsernameInput.getText().toString();
                String email = createEmailInput.getText().toString();
                String password = createPasswordInput.getText().toString();


                //make sure to change the logic
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        backButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

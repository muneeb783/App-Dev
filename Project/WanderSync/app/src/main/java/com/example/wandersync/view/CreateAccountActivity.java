package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wandersync.R;
import com.example.wandersync.viewmodel.CreateAccountViewModel;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText createUsernameInput;
    private EditText createEmailInput;
    private EditText createPasswordInput;
    private CreateAccountViewModel createAccountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button createAccountButton;
        ImageButton backButtonLogin;

        createUsernameInput = findViewById(R.id.create_username_input);
        createEmailInput = findViewById(R.id.create_email_input);
        createPasswordInput = findViewById(R.id.create_password_input);
        createAccountButton = findViewById(R.id.create_account_button);
        backButtonLogin = findViewById(R.id.button_back_login);

        createAccountViewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);

        createAccountViewModel.getCreateAccountSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess == true) {
                    Toast.makeText(CreateAccountActivity.this,
                            "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        createAccountViewModel.getCreateAccountError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(CreateAccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = createUsernameInput.getText().toString();
                String email = createEmailInput.getText().toString();
                String password = createPasswordInput.getText().toString();

                createAccountViewModel.createAccount(username, email, password);
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

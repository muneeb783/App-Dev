package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.wandersync.R;
import com.example.wandersync.view.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccountActivity extends AppCompatActivity {


    private EditText createUsernameInput, createEmailInput, createPasswordInput;
    private Button createAccountButton;
    private ImageButton backButtonLogin;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        createUsernameInput = findViewById(R.id.create_username_input);
        createEmailInput = findViewById(R.id.create_email_input);
        createPasswordInput = findViewById(R.id.create_password_input);
        createAccountButton = findViewById(R.id.create_account_button);
        backButtonLogin = findViewById(R.id.button_back_login);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");




        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = createUsernameInput.getText().toString();
                String email = createEmailInput.getText().toString();
                String password = createPasswordInput.getText().toString();


                //updated to work w/ firebase
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    User newUser = new User(username, email, password);

                    databaseReference.child(username).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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

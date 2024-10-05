package com.example.wandersync.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.view.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountViewModel extends ViewModel {

    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> createAccountSuccess = new MutableLiveData<>();
    private MutableLiveData<String> createAccountError = new MutableLiveData<>();

    public CreateAccountViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void createAccount(String username, String email, String password) {
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            User newUser = new User(username, email, password);
            databaseReference.child(username)
                    .setValue(newUser)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            createAccountSuccess.postValue(true);
                        } else {
                            createAccountError.postValue("Failed to create account.");
                        }
                    });
        } else {
            createAccountError.postValue("Please fill all fields");
        }
    }

    public LiveData<Boolean> getCreateAccountSuccess() {
        return createAccountSuccess;
    }

    public LiveData<String> getCreateAccountError() {
        return createAccountError;
    }
}

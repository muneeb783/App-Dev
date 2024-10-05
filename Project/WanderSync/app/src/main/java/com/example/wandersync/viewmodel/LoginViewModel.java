// ViewModel: LoginViewModel.java
package com.example.wandersync.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginViewModel extends ViewModel {

    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<String> loginError;
    private User user;

    public LoginViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        loginSuccess = new MutableLiveData<>();
        loginError = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public void login(String username, String password) {
        databaseReference.child(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user = snapshot.getValue(User.class);
                            if (user != null && user.getPassword().equals(password)) {
                                loginSuccess.postValue(true);
                            } else {
                                loginError.postValue("Invalid password");
                            }
                        } else {
                            loginError.postValue("Invalid username");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loginError.postValue(error.getMessage());
                    }
                });
    }

    public User getUser() {
        return user;
    }
}

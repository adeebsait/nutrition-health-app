package com.example.finalyear.ui.authentication.data;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class DietFireAuthRepository {

    private final FirebaseAuth auth;

    @Inject
    public DietFireAuthRepository() {
        this.auth = FirebaseAuth.getInstance();
    }

    public boolean getUserIsLoggedIn(){
        return auth.getCurrentUser()!=null;
    }

    public String getUserEmail(){
        return auth.getCurrentUser().getEmail();
    }


    public String getUid() {
        return auth.getCurrentUser().getUid();

    }
    public void loginWithEmail(String email, String password,
                               OnCompleteListener<AuthResult> listener){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void signUpWithEmail(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public boolean logOut() {
        auth.signOut();
        return true;
    }
}

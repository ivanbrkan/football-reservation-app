package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SplashActivity extends Activity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Korisnik je prijavljen, preusmjerite ga na glavnu aktivnost
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Korisnik nije prijavljen, preusmjerite ga na LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}

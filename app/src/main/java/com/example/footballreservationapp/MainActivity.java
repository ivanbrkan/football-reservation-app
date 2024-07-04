package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private EditText etDate, etTime;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewReservations;
    private ReservationsAdapter reservationsAdapter;
    private List<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        TextView logoutTextView = findViewById(R.id.logoutTextView);
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        reservationList = new ArrayList<>();
        reservationsAdapter = new ReservationsAdapter(reservationList, false);
        recyclerViewReservations.setAdapter(reservationsAdapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnReserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveSlot();
            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        loadUserReservations();
    }

    private void reserveSlot() {
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String userId = mAuth.getCurrentUser().getUid();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(this, "Sva polja su obavezna", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("time", time);
        reservation.put("userId", userId);
        reservation.put("status", "pending");

        db.collection("reservations").add(reservation).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Rezervacija uspješna", Toast.LENGTH_SHORT).show();
                    loadUserReservations(); // Refresh reservations list
                } else {
                    Toast.makeText(MainActivity.this, "Greška pri rezervaciji", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUserReservations() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("reservations").whereEqualTo("userId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reservationList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Reservation reservation = document.toObject(Reservation.class);
                                reservationList.add(reservation);
                            }
                            reservationsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Greška pri učitavanju rezervacija", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}

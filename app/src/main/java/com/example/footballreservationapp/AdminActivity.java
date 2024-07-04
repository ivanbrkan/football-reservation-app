package com.example.footballreservationapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends Activity {
    private RecyclerView recyclerView;
    private ReservationsAdapter adapter;
    private List<Reservation> reservationList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationList = new ArrayList<>();
        adapter = new ReservationsAdapter(reservationList, new ReservationsAdapter.OnReservationClickListener() {
            @Override
            public void onAcceptClick(int position) {
                handleReservation(position, "accepted");
            }

            @Override
            public void onRejectClick(int position) {
                handleReservation(position, "rejected");
            }
        }, true);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadPendingReservations();
        loadAcceptedReservations();
    }

    private void loadPendingReservations() {
        db.collection("reservations").whereEqualTo("status", "pending").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reservationList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Reservation reservation = document.toObject(Reservation.class);
                                reservation.setId(document.getId());
                                reservationList.add(reservation);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AdminActivity.this, "Greška pri učitavanju rezervacija", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadAcceptedReservations() {
        db.collection("reservations").whereEqualTo("status", "accepted").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Reservation reservation = document.toObject(Reservation.class);
                                reservation.setId(document.getId());
                                reservationList.add(reservation);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AdminActivity.this, "Greška pri učitavanju prihvaćenih rezervacija", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleReservation(int position, String status) {
        Reservation reservation = reservationList.get(position);
        db.collection("reservations").document(reservation.getId()).update("status", status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminActivity.this, "Rezervacija ažurirana", Toast.LENGTH_SHORT).show();
                            reservationList.remove(position);
                            adapter.notifyItemRemoved(position);
                            sendNotification(reservation.getUserId(), status);
                        } else {
                            Toast.makeText(AdminActivity.this, "Greška pri ažuriranju rezervacije", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendNotification(String userId, String status) {
        // Kod za slanje obavijesti korisniku o statusu rezervacije pomoću Firebase Cloud Messaging
    }
}

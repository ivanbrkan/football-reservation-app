package com.example.footballreservationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {
    private List<Reservation> reservationList;
    private OnReservationClickListener listener;
    private boolean isAdmin;

    public ReservationsAdapter(List<Reservation> reservationList, OnReservationClickListener listener, boolean isAdmin) {
        this.reservationList = (reservationList != null) ? reservationList : new ArrayList<>();
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    public ReservationsAdapter(List<Reservation> reservationList, boolean isAdmin) {
        this.reservationList = (reservationList != null) ? reservationList : new ArrayList<>();
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isAdmin ? R.layout.item_reservation_admin : R.layout.item_reservation_user;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.dateTextView.setText(reservation.getDate());
        holder.timeTextView.setText(reservation.getTime());
        holder.statusTextView.setText(reservation.getStatus());

        if (isAdmin && holder.acceptButton != null && holder.rejectButton != null) {
            holder.acceptButton.setOnClickListener(v -> listener.onAcceptClick(position));
            holder.rejectButton.setOnClickListener(v -> listener.onRejectClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return (reservationList != null) ? reservationList.size() : 0;
    }

    public interface OnReservationClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, statusTextView;
        Button acceptButton, rejectButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    // Metoda za ažuriranje podataka
    public void updateData(List<Reservation> newReservations) {
        this.reservationList = (newReservations != null) ? newReservations : new ArrayList<>();
        notifyDataSetChanged();
    }
}

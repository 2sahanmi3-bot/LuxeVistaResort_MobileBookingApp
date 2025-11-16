package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class BookingHistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        LinearLayout bookingsContainer = view.findViewById(R.id.bookings_container);
        String userId = "demoUser";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        bookingsContainer.removeAllViews();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                class BookingWithId {
                    Booking booking;
                    String documentId;
                    BookingWithId(Booking booking, String documentId) {
                        this.booking = booking;
                        this.documentId = documentId;
                    }
                }
                List<BookingWithId> bookings = new ArrayList<>();
                long latestTimestamp = 0;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Booking booking = doc.toObject(Booking.class);
                    String docId = doc.getId();
                    bookings.add(new BookingWithId(booking, docId));
                    if (booking.getTimestamp() > latestTimestamp) {
                        latestTimestamp = booking.getTimestamp();
                    }
                }
                for (BookingWithId bookingWithId : bookings) {
                    Booking booking = bookingWithId.booking;
                    String docId = bookingWithId.documentId;
                    View item = inflater.inflate(R.layout.item_booking, bookingsContainer, false);
                    TextView tvDetails = item.findViewById(R.id.tv_booking_details);
                    Button btnCancel = item.findViewById(R.id.btn_cancel_booking);
                    String details = booking.getRoomType() + "\n" +
                            "Check-in: " + sdf.format(booking.getCheckInDate()) + "\n" +
                            "Check-out: " + sdf.format(booking.getCheckOutDate());
                    tvDetails.setText(details);
                    if (booking.getTimestamp() == latestTimestamp) {
                        item.setBackgroundColor(0xFFE3F2FD); // Light blue highlight
                    }
                    btnCancel.setOnClickListener(v -> {
                        db.collection("bookings").document(docId).delete()
                            .addOnSuccessListener(aVoid -> {
                                bookingsContainer.removeView(item);
                                Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to cancel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    });
                    bookingsContainer.addView(item);
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to fetch bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        return view;
    }
} 
package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import androidx.core.util.Pair;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;

public class BookRoomFragment extends Fragment {
    // Demo: static booking list
    public static List<Booking> bookingList = new ArrayList<>();

    private Long checkInDate = null;
    private Long checkOutDate = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_room, container, false);
        TextView tvRoomDetails = view.findViewById(R.id.tv_room_details);
        TextView tvBookingDates = view.findViewById(R.id.tv_booking_dates);
        Button btnConfirmBooking = view.findViewById(R.id.btn_confirm_booking);
        Button btnSelectDates = view.findViewById(R.id.btn_select_dates);

        Bundle args = getArguments();
        if (args != null) {
            String type = args.getString("roomType", "");
            String description = args.getString("roomDescription", "");
            double price = args.getDouble("roomPrice", 0.0);
            String imageUrl = args.getString("roomImageUrl", "");
            StringBuilder details = new StringBuilder();
            details.append(type).append("\n").append(description).append("\n").append("Price: LKR").append(price);
            tvRoomDetails.setText(details.toString());
            // Show selected dates if passed
            if (args.containsKey("checkInDate") && args.containsKey("checkOutDate")) {
                long checkIn = args.getLong("checkInDate");
                long checkOut = args.getLong("checkOutDate");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
                String dateText = "Check-in: " + sdf.format(new java.util.Date(checkIn)) + "\nCheck-out: " + sdf.format(new java.util.Date(checkOut));
                tvBookingDates.setText(dateText);
            }
        }
        // TODO: Show selected dates if passed
        btnSelectDates.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Booking Dates");
            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
            picker.show(getParentFragmentManager(), "BOOK_DATE_RANGE_PICKER");
            picker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
                checkInDate = selection.first;
                checkOutDate = selection.second;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
                String dateText = "Check-in: " + sdf.format(new java.util.Date(checkInDate)) + "\nCheck-out: " + sdf.format(new java.util.Date(checkOutDate));
                tvBookingDates.setText(dateText);
            });
        });
        btnConfirmBooking.setOnClickListener(v -> {
            if (checkInDate == null || checkOutDate == null) {
                Toast.makeText(getContext(), "Please select dates before booking.", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = "demoUser";
            Booking booking = new Booking(
                userId,
                args != null ? args.getString("roomType", "") : "",
                args != null ? args.getString("roomDescription", "") : "",
                args != null ? args.getDouble("roomPrice", 0.0) : 0.0,
                args != null ? args.getString("roomImageUrl", "") : "",
                checkInDate,
                checkOutDate
            );
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Booking saved to Firestore!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        });
        return view;
    }
} 
package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import androidx.core.util.Pair;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.content.Context;

public class ServiceDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);
        ImageView ivService = view.findViewById(R.id.iv_service_detail_image);
        TextView tvName = view.findViewById(R.id.tv_service_detail_name);
        TextView tvDescription = view.findViewById(R.id.tv_service_detail_description);
        Button btnBook = view.findViewById(R.id.btn_book_service_detail);
        Bundle args = getArguments();
        if (args != null) {
            tvName.setText(args.getString("name", ""));
            tvDescription.setText(args.getString("description", ""));
            Picasso.get().load(args.getString("imageUrl", "")).into(ivService);
        }
        btnBook.setOnClickListener(v -> {
            // Show date picker
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .build();
            datePicker.show(getParentFragmentManager(), "SERVICE_DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                // For demo: static time slots
                String[] timeSlots = {"10:00 AM", "11:00 AM", "12:00 PM", "2:00 PM", "4:00 PM"};
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select a time slot");
                builder.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, timeSlots), (dialog, which) -> {
                    String selectedTime = timeSlots[which];
                    // Save booking to Firestore
                    String userId = "demoUser";
                    String serviceId = args != null ? args.getString("id", "") : "";
                    String serviceName = args != null ? args.getString("name", "") : "";
                    String serviceImage = args != null ? args.getString("imageUrl", "") : "";
                    String serviceDesc = args != null ? args.getString("description", "") : "";
                    long date = selection;
                    long timestamp = System.currentTimeMillis();
                    ServiceBooking booking = new ServiceBooking(userId, serviceId, serviceName, serviceDesc, serviceImage, date, selectedTime, timestamp);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("serviceBookings")
                        .add(booking)
                        .addOnSuccessListener(documentReference -> {
                            android.widget.Toast.makeText(getContext(), "Service booked!", android.widget.Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            android.widget.Toast.makeText(getContext(), "Failed: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                        });
                });
                builder.show();
            });
        });
        return view;
    }
} 
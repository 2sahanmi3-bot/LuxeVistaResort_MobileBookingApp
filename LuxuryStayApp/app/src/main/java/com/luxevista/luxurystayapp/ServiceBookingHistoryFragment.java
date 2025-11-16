package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ServiceBookingHistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_booking_history, container, false);
        LinearLayout bookingsContainer = view.findViewById(R.id.service_bookings_container);
        String userId = "demoUser";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        bookingsContainer.removeAllViews();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("serviceBookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                class BookingWithId {
                    ServiceBooking booking;
                    String documentId;
                    BookingWithId(ServiceBooking booking, String documentId) {
                        this.booking = booking;
                        this.documentId = documentId;
                    }
                }
                List<BookingWithId> bookings = new ArrayList<>();
                long latestTimestamp = 0;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ServiceBooking booking = doc.toObject(ServiceBooking.class);
                    String docId = doc.getId();
                    bookings.add(new BookingWithId(booking, docId));
                    if (booking.getTimestamp() > latestTimestamp) {
                        latestTimestamp = booking.getTimestamp();
                    }
                }
                for (BookingWithId bookingWithId : bookings) {
                    ServiceBooking booking = bookingWithId.booking;
                    String docId = bookingWithId.documentId;
                    View item = inflater.inflate(R.layout.item_service_booking, bookingsContainer, false);
                    
                    // Set up the views
                    ImageView ivServiceImage = item.findViewById(R.id.iv_service_booking_image);
                    TextView tvServiceName = item.findViewById(R.id.tv_service_booking_name);
                    TextView tvServiceDescription = item.findViewById(R.id.tv_service_booking_description);
                    TextView tvServiceDate = item.findViewById(R.id.tv_service_booking_date);
                    TextView tvServiceTime = item.findViewById(R.id.tv_service_booking_time);
                    TextView tvServiceStatus = item.findViewById(R.id.tv_service_booking_status);
                    Button btnCancel = item.findViewById(R.id.btn_cancel_service_booking);
                    
                    // Load service image
                    String imageUrl = booking.getServiceImageUrl();
                    if (imageUrl != null && !imageUrl.startsWith("http")) {
                        // It's a drawable resource name
                        int resourceId = getResources().getIdentifier(imageUrl, "drawable", requireContext().getPackageName());
                        if (resourceId != 0) {
                            ivServiceImage.setImageResource(resourceId);
                        }
                    } else {
                        // It's a URL, use Picasso
                        Picasso.get().load(imageUrl).into(ivServiceImage);
                    }
                    
                    // Set text content
                    tvServiceName.setText(booking.getServiceName());
                    tvServiceDescription.setText(booking.getServiceDescription());
                    tvServiceDate.setText("Date: " + sdf.format(booking.getDate()));
                    tvServiceTime.setText("Time: " + booking.getTimeSlot());
                    tvServiceStatus.setText("Status: " + booking.getStatus().toUpperCase());
                    
                    // Set status color
                    switch (booking.getStatus().toLowerCase()) {
                        case "confirmed":
                            tvServiceStatus.setTextColor(0xFF4CAF50); // Green
                            break;
                        case "pending":
                            tvServiceStatus.setTextColor(0xFFFF9800); // Orange
                            break;
                        case "cancelled":
                            tvServiceStatus.setTextColor(0xFFF44336); // Red
                            break;
                    }
                    
                    // Highlight latest booking
                    if (booking.getTimestamp() == latestTimestamp) {
                        item.setBackgroundColor(0xFFE3F2FD); // Light blue highlight
                    }
                    
                    // Only show cancel button for pending bookings
                    if ("pending".equals(booking.getStatus().toLowerCase())) {
                        btnCancel.setVisibility(View.VISIBLE);
                        btnCancel.setOnClickListener(v -> {
                            db.collection("serviceBookings").document(docId).delete()
                                .addOnSuccessListener(aVoid -> {
                                    bookingsContainer.removeView(item);
                                    Toast.makeText(getContext(), "Service booking cancelled", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to cancel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                        });
                    } else {
                        btnCancel.setVisibility(View.GONE);
                    }
                    
                    bookingsContainer.addView(item);
                }
                
                // Show message if no bookings
                if (bookings.isEmpty()) {
                    TextView noBookingsText = new TextView(getContext());
                    noBookingsText.setText("No service bookings found.\nBook a service to see your history here!");
                    noBookingsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    noBookingsText.setTextSize(16);
                    noBookingsText.setTextColor(0xFF666666);
                    noBookingsText.setPadding(32, 64, 32, 64);
                    bookingsContainer.addView(noBookingsText);
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to fetch service bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        return view;
    }
} 
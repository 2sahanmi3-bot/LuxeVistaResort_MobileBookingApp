package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminDashboardFragment extends Fragment {
    
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    
    // UI Elements
    private TextView totalUsersText, totalBookingsText, totalServicesText;
    private LinearLayout pendingBookingsContainer, pendingServicesContainer;
    private Button viewAllUsersButton, logoutButton;
    
    // Data lists
    private List<Booking> allBookings = new ArrayList<>();
    private List<ServiceBooking> allServiceBookings = new ArrayList<>();
    private List<String> allUsers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        
        // Initialize UI elements
        initializeViews(view);
        
        // Fetch data from Firebase
        fetchDataFromFirebase();
        
        // Set up button listeners
        setupButtonListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        totalUsersText = view.findViewById(R.id.total_users_text);
        totalBookingsText = view.findViewById(R.id.total_bookings_text);
        totalServicesText = view.findViewById(R.id.total_services_text);
        pendingBookingsContainer = view.findViewById(R.id.pending_bookings_container);
        pendingServicesContainer = view.findViewById(R.id.pending_services_container);
        viewAllUsersButton = view.findViewById(R.id.view_all_users_button);
        logoutButton = view.findViewById(R.id.logout_button);
    }
    
    private void fetchDataFromFirebase() {
        // Fetch all users
        fetchUsers();
        
        // Fetch all bookings
        fetchBookings();
        
        // Fetch all service bookings
        fetchServiceBookings();
    }
    
    private void fetchUsers() {
        db.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        allUsers.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userEmail = document.getString("email");
                            if (userEmail != null) {
                                allUsers.add(userEmail);
                            }
                        }
                        updateUsersCount();
                    } else {
                        Toast.makeText(getContext(), "Error fetching users: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    private void fetchBookings() {
        db.collection("bookings")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        allBookings.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Booking booking = document.toObject(Booking.class);
                            if (booking != null) {
                                allBookings.add(booking);
                            }
                        }
                        updateBookingsCount();
                        displayPendingBookings();
                    } else {
                        Toast.makeText(getContext(), "Error fetching bookings: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    private void fetchServiceBookings() {
        db.collection("serviceBookings")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        allServiceBookings.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ServiceBooking serviceBooking = document.toObject(ServiceBooking.class);
                            if (serviceBooking != null) {
                                allServiceBookings.add(serviceBooking);
                            }
                        }
                        updateServicesCount();
                        displayPendingServices();
                    } else {
                        Toast.makeText(getContext(), "Error fetching service bookings: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    private void updateUsersCount() {
        if (totalUsersText != null) {
            totalUsersText.setText(String.valueOf(allUsers.size()));
        }
    }
    
    private void updateBookingsCount() {
        if (totalBookingsText != null) {
            totalBookingsText.setText(String.valueOf(allBookings.size()));
        }
    }
    
    private void updateServicesCount() {
        if (totalServicesText != null) {
            totalServicesText.setText(String.valueOf(allServiceBookings.size()));
        }
    }
    
    private void displayPendingBookings() {
        if (pendingBookingsContainer == null) return;
        
        pendingBookingsContainer.removeAllViews();
        
        // Show only pending bookings
        List<Booking> pendingBookings = new ArrayList<>();
        
        for (Booking booking : allBookings) {
            if ("pending".equals(booking.getStatus())) {
                pendingBookings.add(booking);
            }
        }
        
        // Limit to 5 most recent pending bookings
        int displayCount = Math.min(pendingBookings.size(), 5);
        
        for (int i = 0; i < displayCount; i++) {
            Booking booking = pendingBookings.get(i);
            View bookingView = createBookingView(booking);
            pendingBookingsContainer.addView(bookingView);
        }
        
        // Update the header text
        TextView headerText = getView().findViewById(R.id.pending_bookings_header);
        if (headerText != null) {
            headerText.setText("📋 Pending Bookings (" + displayCount + ")");
        }
    }
    
    private void displayPendingServices() {
        if (pendingServicesContainer == null) return;
        
        pendingServicesContainer.removeAllViews();
        
        // Show only pending service bookings
        List<ServiceBooking> pendingServices = new ArrayList<>();
        
        for (ServiceBooking service : allServiceBookings) {
            if ("pending".equals(service.getStatus())) {
                pendingServices.add(service);
            }
        }
        
        // Limit to 5 most recent pending services
        int displayCount = Math.min(pendingServices.size(), 5);
        
        for (int i = 0; i < displayCount; i++) {
            ServiceBooking service = pendingServices.get(i);
            View serviceView = createServiceView(service);
            pendingServicesContainer.addView(serviceView);
        }
        
        // Update the header text
        TextView headerText = getView().findViewById(R.id.pending_services_header);
        if (headerText != null) {
            headerText.setText("🛎️ Pending Services (" + displayCount + ")");
        }
    }
    
    private View createBookingView(Booking booking) {
        LinearLayout bookingLayout = new LinearLayout(getContext());
        bookingLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        bookingLayout.setOrientation(LinearLayout.HORIZONTAL);
        bookingLayout.setBackgroundColor(0xFFFFF3E0);
        bookingLayout.setPadding(48, 48, 48, 48);
        
        // Get user email for display
        String userEmail = "Unknown User";
        if (allUsers.size() > 0) {
            // Simple mapping - in real app you'd have proper user IDs
            userEmail = allUsers.get(0);
        }
        
        // Create info layout
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        ));
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView titleText = new TextView(getContext());
        titleText.setText(userEmail + " - " + booking.getRoomType());
        titleText.setTextColor(0xFF333333);
        titleText.setTextSize(14);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String checkInDate = sdf.format(new Date(booking.getCheckInDate()));
        
        TextView dateText = new TextView(getContext());
        dateText.setText("Check-in: " + checkInDate);
        dateText.setTextColor(0xFF666666);
        dateText.setTextSize(12);
        
        infoLayout.addView(titleText);
        infoLayout.addView(dateText);
        
        // Create buttons layout
        LinearLayout buttonsLayout = new LinearLayout(getContext());
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        Button confirmButton = new Button(getContext());
        confirmButton.setText("✓");
        confirmButton.setTextColor(0xFFFFFFFF);
        confirmButton.setBackgroundColor(0xFF4CAF50);
        confirmButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            144
        ));
        confirmButton.setMinWidth(0);
        confirmButton.setPadding(32, 32, 32, 32);
        confirmButton.setOnClickListener(v -> confirmBooking(booking));
        
        Button cancelButton = new Button(getContext());
        cancelButton.setText("✗");
        cancelButton.setTextColor(0xFFFFFFFF);
        cancelButton.setBackgroundColor(0xFFF44336);
        cancelButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            144
        ));
        cancelButton.setMinWidth(0);
        cancelButton.setPadding(32, 32, 32, 32);
        cancelButton.setOnClickListener(v -> cancelBooking(booking));
        
        buttonsLayout.addView(confirmButton);
        buttonsLayout.addView(cancelButton);
        
        bookingLayout.addView(infoLayout);
        bookingLayout.addView(buttonsLayout);
        
        return bookingLayout;
    }
    
    private View createServiceView(ServiceBooking service) {
        LinearLayout serviceLayout = new LinearLayout(getContext());
        serviceLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        serviceLayout.setOrientation(LinearLayout.HORIZONTAL);
        serviceLayout.setBackgroundColor(0xFFE8F5E9);
        serviceLayout.setPadding(48, 48, 48, 48);
        
        // Get user email for display
        String userEmail = "Unknown User";
        if (allUsers.size() > 0) {
            // Simple mapping - in real app you'd have proper user IDs
            userEmail = allUsers.get(0);
        }
        
        // Create info layout
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        ));
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView titleText = new TextView(getContext());
        titleText.setText(userEmail + " - " + service.getServiceName());
        titleText.setTextColor(0xFF333333);
        titleText.setTextSize(14);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault());
        String serviceDate = sdf.format(new Date(service.getDate()));
        
        TextView dateText = new TextView(getContext());
        dateText.setText("Date: " + serviceDate);
        dateText.setTextColor(0xFF666666);
        dateText.setTextSize(12);
        
        infoLayout.addView(titleText);
        infoLayout.addView(dateText);
        
        // Create buttons layout
        LinearLayout buttonsLayout = new LinearLayout(getContext());
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        Button confirmButton = new Button(getContext());
        confirmButton.setText("✓");
        confirmButton.setTextColor(0xFFFFFFFF);
        confirmButton.setBackgroundColor(0xFF4CAF50);
        confirmButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            144
        ));
        confirmButton.setMinWidth(0);
        confirmButton.setPadding(32, 32, 32, 32);
        confirmButton.setOnClickListener(v -> confirmService(service));
        
        Button cancelButton = new Button(getContext());
        cancelButton.setText("✗");
        cancelButton.setTextColor(0xFFFFFFFF);
        cancelButton.setBackgroundColor(0xFFF44336);
        cancelButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            144
        ));
        cancelButton.setMinWidth(0);
        cancelButton.setPadding(32, 32, 32, 32);
        cancelButton.setOnClickListener(v -> cancelService(service));
        
        buttonsLayout.addView(confirmButton);
        buttonsLayout.addView(cancelButton);
        
        serviceLayout.addView(infoLayout);
        serviceLayout.addView(buttonsLayout);
        
        return serviceLayout;
    }
    
    private void confirmBooking(Booking booking) {
        // Update booking status in Firebase
        db.collection("bookings")
            .whereEqualTo("userId", booking.getUserId())
            .whereEqualTo("timestamp", booking.getTimestamp())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    db.collection("bookings").document(documentId)
                        .update("status", "confirmed")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Booking confirmed!", Toast.LENGTH_SHORT).show();
                            fetchBookings();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error confirming booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
            });
    }
    
    private void cancelBooking(Booking booking) {
        // Update booking status in Firebase
        db.collection("bookings")
            .whereEqualTo("userId", booking.getUserId())
            .whereEqualTo("timestamp", booking.getTimestamp())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    db.collection("bookings").document(documentId)
                        .update("status", "cancelled")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Booking cancelled!", Toast.LENGTH_SHORT).show();
                            fetchBookings();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error cancelling booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
            });
    }
    
    private void confirmService(ServiceBooking service) {
        // Update service booking status in Firebase
        db.collection("serviceBookings")
            .whereEqualTo("userId", service.getUserId())
            .whereEqualTo("timestamp", service.getTimestamp())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    db.collection("serviceBookings").document(documentId)
                        .update("status", "confirmed")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Service confirmed!", Toast.LENGTH_SHORT).show();
                            fetchServiceBookings();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error confirming service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
            });
    }
    
    private void cancelService(ServiceBooking service) {
        // Update service booking status in Firebase
        db.collection("serviceBookings")
            .whereEqualTo("userId", service.getUserId())
            .whereEqualTo("timestamp", service.getTimestamp())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    db.collection("serviceBookings").document(documentId)
                        .update("status", "cancelled")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Service cancelled!", Toast.LENGTH_SHORT).show();
                            fetchServiceBookings();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error cancelling service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
            });
    }
    
    private void setupButtonListeners() {
        if (viewAllUsersButton != null) {
            viewAllUsersButton.setOnClickListener(v -> {
                // Show all users in a dialog or navigate to users list
                showAllUsers();
            });
        }
        
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                auth.signOut();
                // Navigate to login fragment
                Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            });
        }
    }
    
    private void showAllUsers() {
        StringBuilder userList = new StringBuilder("All Users:\n\n");
        for (String user : allUsers) {
            userList.append("• ").append(user).append("\n");
        }
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("All Users")
               .setMessage(userList.toString())
               .setPositiveButton("OK", null)
               .show();
    }
} 
package com.luxevista.luxurystayapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private ImageView profileImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_IMAGE_URI = "profile_image_uri";
    private FirebaseFirestore db;
    private TextView emailView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        // Show logged-in email
        emailView = view.findViewById(R.id.profile_email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailView.setText(user.getEmail());
            // Ensure user data exists in Firestore
            ensureUserDataInFirestore(user);
        } else {
            emailView.setText("Not logged in");
        }

        // Nickname logic
        TextView nicknameView = view.findViewById(R.id.profile_nickname);
        String savedNickname = prefs.getString(KEY_NICKNAME, "Nickname");
        nicknameView.setText(savedNickname);
        Button btnSetNickname = view.findViewById(R.id.btn_set_nickname);
        btnSetNickname.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(nicknameView.getText());
            new AlertDialog.Builder(getContext())
                .setTitle("Set Nickname")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String nickname = input.getText().toString();
                    nicknameView.setText(nickname);
                    prefs.edit().putString(KEY_NICKNAME, nickname).apply();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        // Change profile image
        profileImage = view.findViewById(R.id.profile_image);
        // Load saved image URI if exists
        String savedImageUri = prefs.getString(KEY_IMAGE_URI, null);
        if (savedImageUri != null) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
        }
        Button btnChangeProfileImage = view.findViewById(R.id.btn_change_profile_image);
        btnChangeProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Booking history
        Button btnBookingHistory = view.findViewById(R.id.btn_booking_history);
        btnBookingHistory.setOnClickListener(v ->
            Navigation.findNavController(view).navigate(R.id.bookingHistoryFragment)
        );

        // Service history
        Button btnServiceHistory = view.findViewById(R.id.btn_service_history);
        btnServiceHistory.setOnClickListener(v ->
            Navigation.findNavController(view).navigate(R.id.serviceBookingHistoryFragment)
        );

        // Change password
        Button btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new AlertDialog.Builder(getContext())
                .setTitle("Change Password")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newPassword = input.getText().toString();
                    if (user != null && !newPassword.isEmpty()) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        // Change email
        Button btnChangeEmail = view.findViewById(R.id.btn_change_email);
        btnChangeEmail.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setText(user != null ? user.getEmail() : "");
            new AlertDialog.Builder(getContext())
                .setTitle("Change Email")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newEmail = input.getText().toString().trim();
                    if (user != null && !newEmail.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                        // Update email in Firebase Auth
                        user.updateEmail(newEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update email in Firestore
                                    updateEmailInFirestore(user.getUid(), newEmail);
                                    emailView.setText(newEmail);
                                    Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    } else {
                        Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        // Logout
        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            // Navigate to login screen
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        // Register image picker launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (selectedImage != null) {
                        profileImage.setImageURI(selectedImage);
                        prefs.edit().putString(KEY_IMAGE_URI, selectedImage.toString()).apply();
                    }
                }
            }
        );

        return view;
    }

    private void ensureUserDataInFirestore(FirebaseUser user) {
        // Check if user data exists in Firestore
        db.collection("users").document(user.getUid()).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    // Create user document in Firestore
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", user.getEmail());
                    userData.put("uid", user.getUid());
                    userData.put("createdAt", System.currentTimeMillis());
                    
                    db.collection("users").document(user.getUid()).set(userData)
                        .addOnSuccessListener(aVoid -> {
                            // User data created successfully
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });
                }
            });
    }

    private void updateEmailInFirestore(String userId, String newEmail) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", newEmail);
        updates.put("updatedAt", System.currentTimeMillis());
        
        db.collection("users").document(userId).update(updates)
            .addOnSuccessListener(aVoid -> {
                // Email updated in Firestore successfully
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to update email in database", Toast.LENGTH_SHORT).show();
            });
    }
} 
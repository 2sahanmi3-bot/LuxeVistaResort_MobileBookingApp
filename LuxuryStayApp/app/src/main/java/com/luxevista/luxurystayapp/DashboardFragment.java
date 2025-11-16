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
import androidx.navigation.Navigation;
import com.squareup.picasso.Picasso;

public class DashboardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ImageView dashboardImage = view.findViewById(R.id.dashboard_image);
        Picasso.get().load("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80").into(dashboardImage);
        TextView btnBookRoom = view.findViewById(R.id.btn_book_room);
        btnBookRoom.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_dashboard_to_bookRoom));
        TextView btnBookService = view.findViewById(R.id.btn_book_service);
        btnBookService.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_dashboard_to_serviceBooking));
        TextView btnInfo = view.findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_dashboard_to_info));
        TextView btnNotifications = view.findViewById(R.id.btn_notifications);
        btnNotifications.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_dashboard_to_notifications));
        return view;
    }
} 
package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// import androidx.navigation.Navigation;

public class SplashFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        // Removed navigation to login for a simple app
        // new Handler(Looper.getMainLooper()).postDelayed(() -> {
        //     Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
        // }, 1500); // 1.5 seconds
        return view;
    }
} 
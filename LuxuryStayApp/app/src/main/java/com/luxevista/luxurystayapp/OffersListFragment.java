package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
// import androidx.navigation.Navigation;

public class OffersListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_offers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer("Spa Summer Deal", "Get 30% off on all spa bookings this July!", "spa_summer_deal"));
        offers.add(new Offer("Suite Upgrade", "Upgrade to a luxury suite for just LKR50 extra per night.", "suit_upgrade"));
        offers.add(new Offer("Romantic Dinner", "25% off on candlelight dinner for two at our rooftop restaurant.", "romantic_dinner"));
        offers.add(new Offer("Family Fun Pack", "Kids stay and eat free! Enjoy family activities all weekend.", "family_fun_pack"));
        offers.add(new Offer("Early Bird Special", "Book 30 days in advance and get 20% off your stay.", "early_bird_special"));
        offers.add(new Offer("Spa & Wellness Retreat", "Book a 3-night stay and get a complimentary spa session.", "spa_wellness_retreat"));
        offers.add(new Offer("Weekend Escape", "Stay Friday to Sunday and get a free breakfast buffet.", "suite"));

        OfferAdapter adapter = new OfferAdapter(offers, offer -> {
            // Navigation to offer detail removed due to missing action
            // Bundle bundle = new Bundle();
            // bundle.putString("title", offer.getTitle());
            // bundle.putString("description", offer.getDescription());
            // bundle.putString("imageUrl", offer.getImageUrl());
            // Navigation.findNavController(view).navigate(R.id.action_offersListFragment_to_offerDetailFragment, bundle);
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
} 
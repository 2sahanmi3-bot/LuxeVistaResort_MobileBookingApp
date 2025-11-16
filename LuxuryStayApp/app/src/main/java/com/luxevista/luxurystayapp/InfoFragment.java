package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.cardview.widget.CardView;
import android.widget.LinearLayout;
import android.widget.FrameLayout;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_info);

        // Add a super paragraph about the web app
        FrameLayout flAbout = view.findViewById(R.id.fl_about);
        CardView aboutCard = new CardView(requireContext());
        aboutCard.setRadius(32f);
        aboutCard.setCardElevation(10f);
        aboutCard.setUseCompatPadding(true);
        aboutCard.setContentPadding(48, 48, 48, 48);
        aboutCard.setCardBackgroundColor(getResources().getColor(R.color.white));
        TextView aboutText = new TextView(requireContext());
        aboutText.setText("Welcome to LuxeVista Resort's Web App!\n\nExperience the future of luxury hospitality at your fingertips. Our web app is designed to make your stay seamless, from effortless room booking and exclusive offers to discovering local attractions and booking spa treatments. Enjoy a personalized dashboard, real-time notifications, and a curated selection of experiences—all in a beautiful, intuitive interface. Whether you're planning a romantic getaway, a family adventure, or a business trip, LuxeVista's web app ensures every moment is extraordinary.\n\nStart exploring and unlock a world of comfort, convenience, and unforgettable memories!");
        aboutText.setTextSize(20f);
        aboutText.setTextColor(getResources().getColor(R.color.blue_primary));
        aboutText.setPadding(0, 0, 0, 0);
        aboutText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        aboutCard.addView(aboutText);
        flAbout.addView(aboutCard);

        // Add the rest of the InfoFragment content (header)
        FrameLayout flHeader = view.findViewById(R.id.fl_header);
        CardView headerCard = new CardView(requireContext());
        headerCard.setRadius(24f);
        headerCard.setCardElevation(8f);
        headerCard.setUseCompatPadding(true);
        headerCard.setContentPadding(32, 32, 32, 32);
        headerCard.setCardBackgroundColor(getResources().getColor(R.color.white));
        TextView headerText = new TextView(requireContext());
        headerText.setText("Welcome to LuxeVista Resort!\n\nExperience luxury, comfort, and unforgettable moments.\n\nDid you know? Our infinity pool was voted the most Instagrammable spot in the city!\n\nExplore our exclusive offers and nearby attractions below.");
        headerText.setTextSize(18f);
        headerText.setTextColor(getResources().getColor(R.color.blue_primary));
        headerText.setPadding(0, 0, 0, 0);
        headerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headerCard.addView(headerText);
        flHeader.addView(headerCard);

        List<InfoItem> infoItems = new ArrayList<>();
        OffersRepository offersRepo = new OffersRepository();
        for (Offer offer : offersRepo.getHotelOffers()) {
            infoItems.add(new InfoItem(offer));
        }
        AttractionsRepository attractionsRepo = new AttractionsRepository();
        for (Attraction attraction : attractionsRepo.getNearbyAttractions()) {
            infoItems.add(new InfoItem(attraction));
        }
        InfoAdapter adapter = new InfoAdapter(infoItems);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

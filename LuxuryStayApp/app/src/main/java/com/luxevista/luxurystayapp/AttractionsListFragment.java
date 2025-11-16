package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.navigation.Navigation;

public class AttractionsListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attractions_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_attractions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction("Coral Bay Dive", "Explore marine life with scuba tours", "https://via.placeholder.com/150"));
        attractions.add(new Attraction("Sunset Grill", "Seaside dining with fresh seafood", "https://via.placeholder.com/150"));
        attractions.add(new Attraction("Old Town Museum", "Discover the region's history and culture", "https://via.placeholder.com/150"));

        AttractionAdapter adapter = new AttractionAdapter(attractions, attraction -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", attraction.getName());
            bundle.putString("description", attraction.getDescription());
            bundle.putString("imageUrl", attraction.getImageUrl());

        });
        recyclerView.setAdapter(adapter);
        return view;
    }
} 
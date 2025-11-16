package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

public class AttractionDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attraction_detail, container, false);
        ImageView ivImage = view.findViewById(R.id.iv_attraction_detail_image);
        TextView tvName = view.findViewById(R.id.tv_attraction_detail_name);
        TextView tvDescription = view.findViewById(R.id.tv_attraction_detail_description);
        TextView tvCategory = view.findViewById(R.id.tv_attraction_detail_category);
        TextView tvDistance = view.findViewById(R.id.tv_attraction_detail_distance);
        int attractionId = getArguments() != null ? getArguments().getInt("attractionId", -1) : -1;
        Attraction attraction = AttractionsRepository.getAttractionById(attractionId);
        if (attraction != null) {
            Picasso.get().load(attraction.getImageUrl()).into(ivImage);
            tvName.setText(attraction.getName());
            tvDescription.setText(attraction.getDescription());
            tvCategory.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);
        } else {
            tvName.setText("Attraction not found");
        }
        return view;
    }
} 
package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

public class OfferDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        ImageView ivImage = view.findViewById(R.id.iv_offer_detail_image);
        TextView tvTitle = view.findViewById(R.id.tv_offer_detail_title);
        TextView tvDescription = view.findViewById(R.id.tv_offer_detail_description);
        TextView tvValidity = view.findViewById(R.id.tv_offer_detail_validity);
        TextView tvTerms = view.findViewById(R.id.tv_offer_detail_terms);
        int offerId = getArguments() != null ? getArguments().getInt("offerId", -1) : -1;
        Offer offer = OffersRepository.getOfferById(offerId);
        if (offer != null) {
            Picasso.get().load(offer.getImageUrl()).into(ivImage);
            tvTitle.setText(offer.getTitle());
            tvDescription.setText(offer.getDescription());
            tvValidity.setVisibility(View.GONE);
            tvTerms.setText("Terms: Standard terms apply.");
        } else {
            tvTitle.setText("Offer not found");
        }
        return view;
    }
} 
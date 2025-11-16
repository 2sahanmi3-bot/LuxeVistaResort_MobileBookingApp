package com.luxevista.luxurystayapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<InfoItem> items;
    private static final int TYPE_OFFER = 0;
    private static final int TYPE_ATTRACTION = 1;

    public InfoAdapter(List<InfoItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isOffer() ? TYPE_OFFER : TYPE_ATTRACTION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_OFFER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
            return new OfferViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
            return new AttractionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InfoItem item = items.get(position);
        if (holder instanceof OfferViewHolder) {
            Offer offer = item.offer;
            OfferViewHolder vh = (OfferViewHolder) holder;
            if (offer != null) {
                vh.tvTitle.setText(offer.getTitle() != null ? offer.getTitle() : "");
                vh.tvDescription.setText(offer.getDescription() != null ? offer.getDescription() : "");
                vh.tvValidity.setVisibility(View.GONE);
                vh.tvBadge.setVisibility(View.GONE);
                Picasso.get()
                    .load(offer.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(vh.ivImage);
            }
        } else if (holder instanceof AttractionViewHolder) {
            Attraction attraction = item.attraction;
            AttractionViewHolder vh = (AttractionViewHolder) holder;
            if (attraction != null) {
                vh.tvName.setText(attraction.getName() != null ? attraction.getName() : "");
                vh.tvDescription.setText(attraction.getDescription() != null ? attraction.getDescription() : "");
                vh.tvCategory.setVisibility(View.GONE);
                vh.tvDistance.setVisibility(View.GONE);
                vh.tvBadge.setVisibility(View.GONE);
                Picasso.get()
                    .load(attraction.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(vh.ivImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDescription, tvValidity, tvBadge;
        OfferViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_offer_image);
            tvTitle = itemView.findViewById(R.id.tv_offer_title);
            tvDescription = itemView.findViewById(R.id.tv_offer_description);
            tvValidity = itemView.findViewById(R.id.tv_offer_validity);
            tvBadge = itemView.findViewById(R.id.tv_offer_badge);
        }
    }

    static class AttractionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDescription, tvCategory, tvDistance, tvBadge;
        AttractionViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_attraction_image);
            tvName = itemView.findViewById(R.id.tv_attraction_name);
            tvDescription = itemView.findViewById(R.id.tv_attraction_description);
            tvCategory = itemView.findViewById(R.id.tv_attraction_category);
            tvDistance = itemView.findViewById(R.id.tv_attraction_distance);
            tvBadge = itemView.findViewById(R.id.tv_attraction_badge);
        }
    }
} 
package com.luxevista.luxurystayapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private List<Offer> offerList;
    private OnOfferClickListener listener;

    public interface OnOfferClickListener {
        void onOfferClick(Offer offer);
    }

    public OfferAdapter(List<Offer> offerList, OnOfferClickListener listener) {
        this.offerList = offerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.tvTitle.setText(offer.getTitle());
        holder.tvDescription.setText(offer.getDescription());
        holder.tvValidity.setVisibility(View.GONE);
        holder.tvBadge.setVisibility(View.GONE);
        
        // Load image - check if it's a drawable resource or URL
        String imageUrl = offer.getImageUrl();
        if (imageUrl != null && !imageUrl.startsWith("http")) {
            // It's a drawable resource name
            Context context = holder.ivImage.getContext();
            int resourceId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
            if (resourceId != 0) {
                holder.ivImage.setImageResource(resourceId);
            }
        } else {
            // It's a URL, use Picasso
            Picasso.get().load(imageUrl).into(holder.ivImage);
        }
        
        holder.btnAction.setOnClickListener(v -> listener.onOfferClick(offer));
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDescription, tvValidity, tvBadge;
        Button btnAction;
        OfferViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_offer_image);
            tvTitle = itemView.findViewById(R.id.tv_offer_title);
            tvDescription = itemView.findViewById(R.id.tv_offer_description);
            tvValidity = itemView.findViewById(R.id.tv_offer_validity);
            tvBadge = itemView.findViewById(R.id.tv_offer_badge);
            btnAction = itemView.findViewById(R.id.btn_offer_action);
        }
    }
} 
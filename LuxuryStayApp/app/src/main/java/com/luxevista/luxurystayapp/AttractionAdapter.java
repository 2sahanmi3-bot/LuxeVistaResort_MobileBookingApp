package com.luxevista.luxurystayapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {
    private List<Attraction> attractionList;
    private OnAttractionClickListener listener;

    public interface OnAttractionClickListener {
        void onAttractionClick(Attraction attraction);
    }

    public AttractionAdapter(List<Attraction> attractionList, OnAttractionClickListener listener) {
        this.attractionList = attractionList;
        this.listener = listener;
    }

    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttractionViewHolder holder, int position) {
        Attraction attraction = attractionList.get(position);
        holder.tvName.setText(attraction.getName());
        holder.tvDescription.setText(attraction.getDescription());
        holder.tvCategory.setVisibility(View.GONE);
        holder.tvDistance.setVisibility(View.GONE);
        holder.tvBadge.setVisibility(View.GONE);
        Picasso.get().load(attraction.getImageUrl()).into(holder.ivImage);
        holder.itemView.setOnClickListener(v -> listener.onAttractionClick(attraction));
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
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
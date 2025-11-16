package com.luxevista.luxurystayapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> serviceList;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }

    public ServiceAdapter(List<Service> serviceList, OnServiceClickListener listener) {
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.tvName.setText(service.getName());
        holder.tvDescription.setText(service.getDescription());
        holder.tvPrice.setVisibility(View.GONE); // Hide price
        String imageUrl = service.getImageUrl();
        if (imageUrl != null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
            Picasso.get().load(imageUrl).into(holder.ivImage);
        } else if (imageUrl != null) {
            int resId = holder.ivImage.getContext().getResources()
                .getIdentifier(imageUrl.toLowerCase(), "drawable", holder.ivImage.getContext().getPackageName());
            if (resId != 0) {
                holder.ivImage.setImageResource(resId);
            } else {
                holder.ivImage.setImageResource(android.R.color.darker_gray); // fallback
            }
        }
        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) listener.onServiceClick(service);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onServiceClick(service);
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDescription, tvPrice;
        Button btnBook;

        ServiceViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_service_image);
            tvName = itemView.findViewById(R.id.tv_service_name);
            tvDescription = itemView.findViewById(R.id.tv_service_description);
            tvPrice = itemView.findViewById(R.id.tv_service_price);
            btnBook = itemView.findViewById(R.id.btn_book_service);
        }
    }
}
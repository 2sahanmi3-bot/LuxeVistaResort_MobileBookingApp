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

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomBookClickListener listener;

    public interface OnRoomBookClickListener {
        void onBookClick(Room room);
    }

    public RoomAdapter(List<Room> roomList, OnRoomBookClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomType.setText(room.getType());
        holder.tvRoomPrice.setText(String.format("LKR%.2f/night", room.getPrice()));
        holder.tvRoomTaxes.setText("+LKR25.00 taxes");
        String imageUrl = room.getImageUrl();
        if (imageUrl != null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
            Picasso.get().load(imageUrl).into(holder.ivRoomImage);
        } else if (imageUrl != null) {
            int resId = holder.ivRoomImage.getContext().getResources()
                .getIdentifier(imageUrl.toLowerCase(), "drawable", holder.ivRoomImage.getContext().getPackageName());
            if (resId != 0) {
                holder.ivRoomImage.setImageResource(resId);
            } else {
                holder.ivRoomImage.setImageResource(android.R.color.darker_gray); // fallback
            }
        }
        // Date picker logic
        holder.tvRoomDates.setText("Select dates");
        holder.tvRoomDates.setOnClickListener(v -> {
            androidx.core.util.Pair<Long, Long> selection = null;
            com.google.android.material.datepicker.MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Booking Dates");
            com.google.android.material.datepicker.MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();
            picker.show(((androidx.fragment.app.FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager(), "DATE_RANGE_PICKER");
            picker.addOnPositiveButtonClickListener(sel -> {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault());
                String dateText = sdf.format(new java.util.Date(sel.first)) + " - " + sdf.format(new java.util.Date(sel.second));
                holder.tvRoomDates.setText(dateText);
                holder.tvRoomDates.setTag(sel); // store selection
            });
        });
        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                // Pass selected dates if available
                androidx.core.util.Pair<Long, Long> dates = (androidx.core.util.Pair<Long, Long>) holder.tvRoomDates.getTag();
                room.setSelectedDates(dates); // You may need to add this method/field to Room
                listener.onBookClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRoomImage;
        TextView tvRoomType, tvRoomPrice, tvRoomTaxes, tvRoomDates;
        Button btnBook;

        RoomViewHolder(View itemView) {
            super(itemView);
            ivRoomImage = itemView.findViewById(R.id.iv_lodge_image);
            tvRoomType = itemView.findViewById(R.id.tv_lodge_name);
            tvRoomPrice = itemView.findViewById(R.id.tv_lodge_price);
            tvRoomTaxes = itemView.findViewById(R.id.tv_lodge_taxes);
            tvRoomDates = itemView.findViewById(R.id.tv_lodge_dates);
            btnBook = itemView.findViewById(R.id.btn_reserve);
        }
    }
}

package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import androidx.core.util.Pair;

public class RoomsFragment extends Fragment {

    private Spinner spinnerRoomType;
    private RecyclerView rvRooms;
    private List<Room> roomList = new ArrayList<>();
    private RoomAdapter roomAdapter;
    private Long checkInDate = null;
    private Long checkOutDate = null;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        spinnerRoomType = view.findViewById(R.id.spinner_room_type);
        rvRooms = view.findViewById(R.id.rv_rooms);
        rvRooms.setLayoutManager(new LinearLayoutManager(requireContext()));

        roomAdapter = new RoomAdapter(roomList, room -> {
            Bundle bundle = new Bundle();
            bundle.putString("roomType", room.getType());
            bundle.putString("roomDescription", room.getDescription());
            bundle.putDouble("roomPrice", room.getPrice());
            bundle.putString("roomImageUrl", room.getImageUrl());
            if (checkInDate != null && checkOutDate != null) {
                bundle.putLong("checkInDate", checkInDate);
                bundle.putLong("checkOutDate", checkOutDate);
            }
            BookRoomFragment bookRoomFragment = new BookRoomFragment();
            bookRoomFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, bookRoomFragment)
                .addToBackStack(null)
                .commit();
        });
        rvRooms.setAdapter(roomAdapter);

        loadRooms();

        Button btnSelectDates = view.findViewById(R.id.btn_select_dates);
        btnSelectDates.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Booking Dates");
            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
            picker.show(getParentFragmentManager(), "DATE_RANGE_PICKER");
            picker.addOnPositiveButtonClickListener(selection -> {
                if (selection != null) {
                    checkInDate = selection.first;
                    checkOutDate = selection.second;
                    btnSelectDates.setText(picker.getHeaderText());
                }
            });
        });

        return view;
    }

    private void loadRooms() {
        // Dummy data to test
        roomList.clear();
        roomList.add(new Room("Deluxe Room", "Spacious room with sea view", 120.00, "deluxe_room"));
        roomList.add(new Room("Suite", "Luxurious suite with king size bed", 220.00, "suite"));
        roomAdapter.notifyDataSetChanged();
    }
}

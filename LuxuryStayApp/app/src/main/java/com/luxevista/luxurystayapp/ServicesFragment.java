package com.luxevista.luxurystayapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    private RecyclerView rvServices;
    private List<Service> serviceList = new ArrayList<>();
    private ServiceAdapter serviceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services, container, false);

        Button btnOpenCalendar = view.findViewById(R.id.btn_open_calendar);
        btnOpenCalendar.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .build();
            datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        rvServices = view.findViewById(R.id.rv_services);
        rvServices.setLayoutManager(new LinearLayoutManager(getContext()));

        serviceAdapter = new ServiceAdapter(serviceList, service -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", service.getId());
            bundle.putString("name", service.getName());
            bundle.putString("description", service.getDescription());
            bundle.putString("imageUrl", service.getImageUrl());
            ServiceDetailFragment detailFragment = new ServiceDetailFragment();
            detailFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, detailFragment)
                .addToBackStack(null)
                .commit();
        });
        rvServices.setAdapter(serviceAdapter);

        loadServices();

        return view;
    }

    private void loadServices() {
        serviceList.clear();
        serviceList.addAll(ServicesRepository.getServices());
        serviceAdapter.notifyDataSetChanged();
    }
}

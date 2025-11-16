package com.luxevista.luxurystayapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OffersAttractionsPagerAdapter extends FragmentStateAdapter {
    public OffersAttractionsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new OffersListFragment();
        } else {
            return new AttractionsListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
} 
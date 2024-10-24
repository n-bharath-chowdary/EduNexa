package com.edunexa.rur.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.rur.fragments.image;
import com.edunexa.rur.fragments.message;
import com.edunexa.rur.fragments.pdf;
import com.edunexa.rur.fragments.video;

public class ViewPageAdapter extends FragmentStateAdapter {
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new pdf();
            case 1: return new image();
            case 2: return new message();
            case 3: return new video();
            default: return new pdf();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

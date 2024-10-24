package com.edunexa.principal.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.principal.fragments.main_fragments.princi_img;
import com.edunexa.principal.fragments.main_fragments.princi_msg;
import com.edunexa.principal.fragments.main_fragments.princi_pdf;
import com.edunexa.principal.fragments.main_fragments.princi_video;


public class Fragment_adapter_Princi extends FragmentStateAdapter {
    public Fragment_adapter_Princi (@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new princi_pdf();
            case 1: return new princi_img();
            case 2: return new princi_msg();
            case 3: return new princi_video();
            default: return new princi_pdf();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


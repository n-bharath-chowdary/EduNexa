package com.edunexa.principal.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.principal.fragments.recived_fragments.princi_rec_img;
import com.edunexa.principal.fragments.recived_fragments.princi_rec_msg;
import com.edunexa.principal.fragments.recived_fragments.princi_rec_pdf;
import com.edunexa.principal.fragments.recived_fragments.princi_rec_video;

public class recivied_adapter extends FragmentStateAdapter {

    public recivied_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new princi_rec_pdf();
            case 1: return new princi_rec_img();
            case 2: return new princi_rec_msg();
            case 3: return new princi_rec_video();
            default: return new princi_rec_pdf();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

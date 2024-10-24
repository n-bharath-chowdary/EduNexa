package com.edunexa.teacher.adaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.principal.fragments.main_fragments.princi_img;
import com.edunexa.principal.fragments.main_fragments.princi_msg;
import com.edunexa.principal.fragments.main_fragments.princi_pdf;
import com.edunexa.principal.fragments.main_fragments.princi_video;
import com.edunexa.teacher.fragments.main.teacher_img;
import com.edunexa.teacher.fragments.main.teacher_msg;
import com.edunexa.teacher.fragments.main.teacher_pdf;
import com.edunexa.teacher.fragments.main.teacher_vid;

public class teacher_upload_adapter extends FragmentStateAdapter {
    public teacher_upload_adapter (@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new teacher_pdf();
            case 1: return new teacher_img();
            case 2: return new teacher_msg();
            case 3: return new teacher_vid();
            default: return new teacher_pdf();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
package com.edunexa.teacher.adaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.teacher.fragments.received.teacher_img_rec;
import com.edunexa.teacher.fragments.received.teacher_msg_rec;
import com.edunexa.teacher.fragments.received.teacher_pdf_rec;
import com.edunexa.teacher.fragments.received.teacher_vid_rec;

public class teacher_received_adapter extends FragmentStateAdapter {

    public teacher_received_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new teacher_pdf_rec();
            case 1: return new teacher_img_rec();
            case 2: return new teacher_msg_rec();
            case 3: return new teacher_vid_rec();
            default: return new teacher_pdf_rec();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

package com.edunexa.student.adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.student.fragments.student_img_rec;
import com.edunexa.student.fragments.student_msg_rec;
import com.edunexa.student.fragments.student_pdf_rec;
import com.edunexa.student.fragments.student_vid_rec;

public class student_received_adapter extends FragmentStateAdapter {

    public student_received_adapter(@NonNull Activity fragmentActivity) {
        super((FragmentActivity) fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new student_pdf_rec();
            case 1: return new student_img_rec();
            case 2: return new student_msg_rec();
            case 3: return new student_vid_rec();
            default: return new student_pdf_rec();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
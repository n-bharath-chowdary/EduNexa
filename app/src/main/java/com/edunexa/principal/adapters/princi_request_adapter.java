package com.edunexa.principal.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edunexa.principal.fragments.recived_fragments.princi_student_req;
import com.edunexa.principal.fragments.recived_fragments.princi_teacher_req;

public class princi_request_adapter extends FragmentStateAdapter {
    public princi_request_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new princi_student_req();
            case 1: return new princi_teacher_req();
            default: return new princi_student_req();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

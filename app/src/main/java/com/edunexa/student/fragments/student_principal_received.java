package com.edunexa.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.edunexa.R;
import com.edunexa.student.adapters.student_received_adapter;
import com.edunexa.teacher.adaters.teacher_received_adapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class student_principal_received extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private student_received_adapter adapter;
    private Bundle bundle;
    private String course;
    private int year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_received, container, false);
        tabLayout = view.findViewById(R.id.teacher_recived_tab);
        viewPager2 = view.findViewById(R.id.teacher_recived_vp2);
        adapter = new student_received_adapter(getActivity());
        viewPager2.setAdapter(adapter);

        bundle = getArguments();

        course = bundle.getString("course");
        year = bundle.getInt("year");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        tab.setText("PDFs");
                        bundle.putString("From", "Principal");
                        bundle.putString("course", course);
                        bundle.putInt("year", year);
                        break;
                    case 1:
                        tab.setText("Image's");
                        bundle.putString("From", "Principal");
                        bundle.putString("course", course);
                        bundle.putInt("year", year);
                        break;
                    case 2:
                        tab.setText("Message's");
                        bundle.putString("From", "Principal");
                        bundle.putString("course", course);
                        bundle.putInt("year", year);
                        break;
                    case 3:
                        tab.setText("Video's");
                        bundle.putString("From", "Principal");
                        bundle.putString("course", course);
                        bundle.putInt("year", year);
                        break;
                }
            }
        });

        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        return view;
    }
}

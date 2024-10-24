package com.edunexa.teacher.fragments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edunexa.R;
import com.edunexa.principal.adapters.Fragment_adapter_Princi;
import com.edunexa.teacher.adaters.teacher_upload_adapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class teacher_upload extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private teacher_upload_adapter adapter;
    private String des;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_upload, container, false);

        tabLayout = view.findViewById(R.id.teacher_tab);
        viewPager2 = view.findViewById(R.id.teacher_vp2);
        adapter = new teacher_upload_adapter(getActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.setVisibility(View.VISIBLE);

        bundle = getArguments();
        des = bundle.getString("des");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("PDFs");
                        break;
                    case 1:
                        tab.setText("Image's");
                        break;
                    case 2:
                        tab.setText("Message's");
                        break;
                    case 3:
                        tab.setText("Video's");
                        break;
                }
                Bundle args = new Bundle();
                args.putString("des", des);
                Fragment fragment = new Fragment();
                fragment.setArguments(args);
                adapter.createFragment(position);
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
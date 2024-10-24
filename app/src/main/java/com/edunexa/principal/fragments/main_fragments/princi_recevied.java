package com.edunexa.principal.fragments.main_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edunexa.R;
import com.edunexa.principal.adapters.recivied_adapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class princi_recevied extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private recivied_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_princi_recevied, container, false);

        tabLayout = view.findViewById(R.id.Princi_tab);
        viewPager2 = view.findViewById(R.id.Princi_vp2);
        adapter = new recivied_adapter(getActivity());
        viewPager2.setAdapter(adapter);

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
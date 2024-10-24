package com.edunexa.principal;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.edunexa.R;
//import com.edunexa.jd.kalburagi.fragments.kal_home;
//import com.edunexa.jd.kalburagi.fragments.kal_recevied;
//import com.edunexa.jd.kalburagi.fragments.kal_upload;
import com.edunexa.principal.fragments.main_fragments.princi_request;
import com.edunexa.principal.fragments.main_fragments.princi_recevied;
import com.edunexa.principal.fragments.main_fragments.princi_upload;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class princi_main extends AppCompatActivity {
    protected final int request = 1;
    protected final int upload = 2;
    protected final int recevied = 3;
    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_princi_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigation = findViewById(R.id.princi_bottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(upload, R.drawable.upload));
        bottomNavigation.add(new MeowBottomNavigation.Model(request, R.drawable.request));
        bottomNavigation.add(new MeowBottomNavigation.Model(recevied, R.drawable.recevied));

        bottomNavigation.show(recevied, true);

         

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;

                if (model.getId() == 1) {
                    fragment = new princi_request();
                } else if (model.getId() == 2) {
                    fragment = new princi_upload();
                } else if (model.getId()==3) {
                    fragment = new princi_recevied();
                }

                LoadandFragmentchange(fragment);
                return null;
            }
        });
    }

    private void LoadandFragmentchange(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.princi_fragment, fragment, null).
                commit();
    }
}
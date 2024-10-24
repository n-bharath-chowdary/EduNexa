package com.edunexa.rur;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.edunexa.R;
import com.edunexa.rur.fragments.rur_request;
import com.edunexa.rur.fragments.rur_upload;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    protected final int Request = 1;
    protected final int upload = 2;
    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigation = findViewById(R.id.rur_bottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(upload, R.drawable.upload));
        bottomNavigation.add(new MeowBottomNavigation.Model(Request, R.drawable.request));

        bottomNavigation.show(Request, true);


        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;

                if (model.getId() == 1) {
                    fragment = new rur_request();
                } else if (model.getId() == 2) {
                    fragment = new rur_upload();
//                } else if (model.getId() == 3) {
//                    fragment = new princi_recevied();
                }

                LoadandFragmentchange(fragment);
                return null;
            }
        });
    }
    private void LoadandFragmentchange(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.rur_fragment, fragment, null).
                commit();
    }
}
package com.edunexa.teacher;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.edunexa.R;
import com.edunexa.teacher.fragments.main.teacher_received;
import com.edunexa.teacher.fragments.main.teacher_upload;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class teacher_main extends AppCompatActivity {
    protected final int upload = 1;
    protected final int recevied = 2;
    private MeowBottomNavigation bottomNavigation;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        des = getIntent().getStringExtra("des");

        bottomNavigation = findViewById(R.id.teacher_bottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(upload, R.drawable.upload));
        bottomNavigation.add(new MeowBottomNavigation.Model(recevied, R.drawable.recevied));

        bottomNavigation.show(recevied, true);

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putString("des", des);
                fragment.setArguments(bundle);

                if (model.getId() == 1) {
                    fragment = new teacher_upload();
                } else if (model.getId() == 2) {
                    fragment = new teacher_received();
                }

                LoadandFragmentchange(fragment);
                return null;
            }
        });

    }

    private void LoadandFragmentchange(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.teacher_fragment, fragment, null).
                commit();
    }

}
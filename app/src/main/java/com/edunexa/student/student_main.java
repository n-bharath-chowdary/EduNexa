package com.edunexa.student;

import static com.edunexa.R.drawable.back1;
import static com.edunexa.R.drawable.main_background;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.edunexa.R;

import com.edunexa.student.adapters.student_received_adapter;
import com.edunexa.student.fragments.student_RUR_received;
import com.edunexa.student.fragments.student_principal_received;
import com.edunexa.student.fragments.student_teacher_received;
import com.edunexa.teacher.adaters.teacher_received_adapter;
import com.edunexa.teacher.fragments.main.teacher_received;
import com.edunexa.teacher.fragments.main.teacher_upload;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class student_main extends AppCompatActivity {
    private String course;
    private int year;
    private MeowBottomNavigation bottomNavigation;
    protected final int teacher = 1;
    protected final int principal = 2;
    protected final int rur = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        course = getIntent().getStringExtra("course");
        year = getIntent().getIntExtra("year", 1);

        bottomNavigation = findViewById(R.id.student_bottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(teacher, R.drawable.teacher));
        bottomNavigation.add(new MeowBottomNavigation.Model(principal, R.drawable.jd));
        bottomNavigation.add(new MeowBottomNavigation.Model(rur, R.drawable.dce));

        bottomNavigation.show(principal, true);

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putString("course", course);
                bundle.putInt("year", year);
                fragment.setArguments(bundle);

                if (model.getId() == 1) {
                    model.setCount("Teacher");
                    fragment = new student_teacher_received();
                } else if (model.getId() == 2) {
                    model.setCount("Principal");
                    fragment = new student_principal_received();
                } else if (model.getId() == 3) {
                    model.setCount("RUR");
                    fragment = new student_RUR_received();
                }

                LoadandFragmentchange(fragment);
                return null;
            }
        });

    }

    private void LoadandFragmentchange(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.main, fragment, null).
                commit();
    }

}
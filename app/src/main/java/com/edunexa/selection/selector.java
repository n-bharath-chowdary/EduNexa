package com.edunexa.selection;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edunexa.R;
import com.edunexa.rur.Login_rur;
import com.edunexa.principal.principal_login;
import com.edunexa.student.student_login;
import com.edunexa.teacher.teacher_selector;

public class selector extends AppCompatActivity {

    private LinearLayout rur;
    private LinearLayout teacher;
    private LinearLayout student, principal;

    private RelativeLayout rl;
    private AnimationDrawable anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selector);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rl = findViewById(R.id.main);
        anime = (AnimationDrawable) rl.getBackground();
        anime.setEnterFadeDuration(5000);
        anime.setExitFadeDuration(5000);
        anime.start();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        rur = findViewById(R.id.select_rur);
        teacher = findViewById(R.id.select_teacher);
        student = findViewById(R.id.select_student);
        principal = findViewById(R.id.select_principal);

        rur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(selector.this, "Caution..!, Navigating to Login..!", Toast.LENGTH_SHORT).show();
                openrur();
            }
        });

        principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(selector.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                openPrincipal();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(selector.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                openTeacher();
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(selector.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                openStudent();
            }
        });
    }

    private void openPrincipal() {
        startActivity(new Intent(this, principal_login.class));
    }

    private void openrur() {
        startActivity(new Intent(this, Login_rur.class));
    }

    private void openTeacher() {
        startActivity(new Intent(this, teacher_selector.class));
    }

    private void openStudent() {
        startActivity(new Intent(this, student_login.class));
    }
}
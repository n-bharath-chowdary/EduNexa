package com.edunexa.teacher;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edunexa.R;


public class teacher_selector extends AppCompatActivity {

    private RelativeLayout rl;
    private AnimationDrawable anime;
    private LinearLayout guest, permanent, non;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_selector);
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

        guest = findViewById(R.id.guest_teacher);
        permanent = findViewById(R.id.permanent_teacher);
        non = findViewById(R.id.non_teaching);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des = "guest";
                openLogin(des);
            }
        });

        permanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des = "permanent";
                openLogin(des);
            }
        });

        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des = "non";
                openLogin(des);
            }
        });
    }

    private void openLogin(String des) {
        Intent intent = new Intent(teacher_selector.this, teacher_login.class);
        intent.putExtra("des", des);
        startActivity(intent);
    }
}

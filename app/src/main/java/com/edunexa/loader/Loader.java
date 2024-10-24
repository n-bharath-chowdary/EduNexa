package com.edunexa.loader;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.edunexa.R;
import com.edunexa.selection.selector;

public class Loader extends AppCompatActivity {

    private VideoView vv;
    private String path;
    private int vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        vv = findViewById(R.id.video);

        vid = R.raw.hh;
        path = "android.resource://" + getPackageName() + "/raw/" + vid;
        Uri uri = Uri.parse(path);
        vv.setVideoURI(uri);

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });


        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextActivity();
                    }
                });
                mp.start();
            }
        });
    }

    @Override
    protected void onStart() {
        Toast.makeText(this, "Welcome..! to EduNexa", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    private void nextActivity() {
        startActivity(new Intent(this, selector.class));
        finish();
    }
}
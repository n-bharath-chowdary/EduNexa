package com.edunexa.student.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.VideoViewer;
import com.edunexa.data.video_data;
import com.edunexa.student.adapters.student_vid_adapter;
import com.edunexa.teacher.adaters.teacher_vid_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class student_vid_rec extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<video_data> list = new ArrayList<video_data>();
    private DatabaseReference ref, rur_ref,teacher_ref, dref;
    private StorageReference storef;
    private student_vid_adapter adapter;
    private String from, course;
    private int year;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_video, container, false);

        recyclerView = view.findViewById(R.id.getvideo);
        ref = FirebaseDatabase.getInstance().getReference("Principal");
        rur_ref = FirebaseDatabase.getInstance().getReference("RUR");
        teacher_ref = FirebaseDatabase.getInstance().getReference("Teacher");
        storef = FirebaseStorage.getInstance().getReference();

        bundle = getArguments();

        from = bundle.getString("From");
        course = bundle.getString("course");
        year = bundle.getInt("year");

        getVideos();

        recyclerView.setAdapter(adapter);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float y = event.getY();
                    if (y > 50) {
                        getVideos();
                    }
                }
                return true;
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideos();
    }

    private void getVideos() {
        switch (from) {
            case "RUR":
                dref = rur_ref.child("Videos");
                break;
            case "Principal":
                dref = ref.child("Videos");
                break;
            case "Teacher":
                dref = teacher_ref.child("Videos");
                break;
            default:
                Toast.makeText(getContext(), "Data cannot be loaded, re-Login again", Toast.LENGTH_SHORT).show();
                break;
        }
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    video_data data = dataSnapshot.getValue(video_data.class);
                    list.add(0, data);
                }

                adapter = new student_vid_adapter(list, getContext(), new student_vid_adapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int video_id) {
                        viewVideo(video_id);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewVideo(int i) {
        Intent intent = new Intent(getContext(), VideoViewer.class);
        intent.putExtra("Video", list.get(i).getVideo_url());
        getContext().startActivity(intent);
    }
}


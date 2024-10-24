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

import com.edunexa.Image_Viewer;
import com.edunexa.R;
import com.edunexa.data.img_data;
import com.edunexa.student.adapters.student_img_adapter;
import com.edunexa.teacher.adaters.teacher_img_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class student_img_rec extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<img_data> list = new ArrayList<img_data>();
    private DatabaseReference rur_ref,princi_ref, teacher_ref, dref;
    private student_img_adapter adapter;
    private Bundle bundle;
    private String from, course;
    private int year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_img, container, false);

        recyclerView = view.findViewById(R.id.getimage);
        rur_ref = FirebaseDatabase.getInstance().getReference("RUR");
        princi_ref = FirebaseDatabase.getInstance().getReference("Principal");
        teacher_ref = FirebaseDatabase.getInstance().getReference("Teacher");

        bundle = getArguments();

        from = bundle.getString("From");
        course = bundle.getString("course");
        year = bundle.getInt("year");
        getImages();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float y = event.getY();
                    if (y > 50) {
                        getImages();
                    }
                }
                return true;
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        getImages();
    }

    private void getImages() {
        switch (from) {
            case "RUR":
                dref = rur_ref.child("Images");
                break;
            case "Principal":
                dref = princi_ref.child("Images");
                break;
            case "Teacher":
                dref = teacher_ref.child("Images");
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
                    img_data data = dataSnapshot.getValue(img_data.class);
                    list.add(0, data);
                }

                adapter = new student_img_adapter(list, getContext(), new student_img_adapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int imageid) {
                        viewImage(imageid);
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

    private void viewImage(int i) {
        String img_url = list.get(i).getImg_url();
        String file_name = list.get(i).getImg_name();
        Intent intent = new Intent(getContext(), Image_Viewer.class);
        intent.putExtra("Image", img_url);
        intent.putExtra("file name", file_name);
        getContext().startActivity(intent);
    }

}


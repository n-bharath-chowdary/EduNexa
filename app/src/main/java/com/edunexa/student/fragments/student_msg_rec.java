package com.edunexa.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.msg_data;
import com.edunexa.student.adapters.student_msg_adapter;
import com.edunexa.teacher.adaters.teacher_msg_adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class student_msg_rec extends Fragment {
    View view;
    private List<msg_data> list = new ArrayList<msg_data>();
    private student_msg_adapter adapter;
    private RecyclerView recyclerView;
    private MyApi db;
    private String from, course;
    private int year;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_msg, container, false);

        recyclerView = view.findViewById(R.id.getmsg);

        db = RetrofitInstance.getInstance().create(MyApi.class);
        bundle = getArguments();
        from = bundle.getString("From");
        course = bundle.getString("course");
        year = bundle.getInt("year");
        getMessages();

        adapter = new student_msg_adapter(list, getContext());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float y = event.getY();
                    if (y > 50) {
                        getMessages();
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
        getMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessages();
    }

    private void getMessages() {
        Call<List<msg_data>> call = null;
        switch (from) {
            case "RUR":
                call = db.getMessage();
                break;
            case "Principal":
                call = db.get_princi_Message();
                break;
            case "Teacher":
                call = db.get_teacher_Message();
                break;
            default:
                Toast.makeText(getContext(), "Cannot load data, Re-login again", Toast.LENGTH_SHORT).show();
                break;
        }

        if(call == null) {
            Toast.makeText(getContext(), "Cannot load data, Re-login again", Toast.LENGTH_SHORT).show();
        } else {

            call.enqueue(new Callback<List<msg_data>>() {
                @Override
                public void onResponse(Call<List<msg_data>> call, Response<List<msg_data>> response) {
                    List<msg_data> msg = response.body();
                    Collections.reverse(msg);
                    if (msg != null) {
                        list.clear();
                        list.addAll(msg);
                    } else {
                        Toast.makeText(getContext(), "The data is null", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                @Override
                public void onFailure(Call<List<msg_data>> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to get Response", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


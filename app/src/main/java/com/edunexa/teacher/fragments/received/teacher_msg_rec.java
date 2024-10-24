package com.edunexa.teacher.fragments.received;

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
import com.edunexa.teacher.adaters.teacher_msg_adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class teacher_msg_rec extends Fragment {
    View view;
    private List<msg_data> list = new ArrayList<msg_data>();
    private teacher_msg_adapter adapter;
    private RecyclerView recyclerView;
    private MyApi db;
    private String des;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_msg, container, false);

        recyclerView = view.findViewById(R.id.getmsg);

        db = RetrofitInstance.getInstance().create(MyApi.class);
        bundle = getArguments();
        des = bundle.getString("des");
        getMessages();

        adapter = new teacher_msg_adapter(list, getContext());

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
        call = db.getMessage();
        call.enqueue(new Callback<List<msg_data>>() {
            @Override
            public void onResponse(Call<List<msg_data>> call, Response<List<msg_data>> response) {
                List<msg_data> msg = response.body();
                msg_data data = new msg_data();
                Collections.reverse(msg);
                if (msg != null) {
                    String sendTo = data.getSend_to();
                    if (sendTo.equals(des)) {
                        list.clear();
                        list.addAll(msg);
                    } else {
                        Toast.makeText(getContext(), "The data is null", Toast.LENGTH_SHORT).show();
                    }
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
        call = db.get_teacher_Message();
        call.enqueue(new Callback<List<msg_data>>() {
            @Override
            public void onResponse(Call<List<msg_data>> call, Response<List<msg_data>> response) {
                List<msg_data> msg = response.body();
                msg_data data = new msg_data();
                Collections.reverse(msg);
                if (msg != null && data.getDes().equals(des)) {
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

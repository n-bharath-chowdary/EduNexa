package com.edunexa.principal.fragments.recived_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.principal.adapters.received.princi_msg_rec_adapter;
import com.edunexa.data.msg_data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class princi_rec_msg extends Fragment {
    View view;
    private List<msg_data> list = new ArrayList<msg_data>();
    private princi_msg_rec_adapter adapter;
    private RecyclerView recyclerView;
    private MyApi db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_msg, container, false);

        recyclerView = view.findViewById(R.id.getmsg);

        db = RetrofitInstance.getInstance().create(MyApi.class);
        getMessages();

        adapter = new princi_msg_rec_adapter(list, getContext());

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
        Call<List<msg_data>> call = db.getMessage();
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
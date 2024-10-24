package com.edunexa.rur.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.Princi_Users_data;
import com.edunexa.rur.adapters.requestAdapter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rur_request extends Fragment {
    View view;
    private Context context;
    private requestAdapter adapter;
    private Princi_Users_data data;
    private RecyclerView recyclerView;
    private List<Princi_Users_data> list;
    private MyApi db;
    private boolean pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rur_home, container, false);

        context = getContext();
        db = RetrofitInstance.getInstance().create(MyApi.class);

        recyclerView = view.findViewById(R.id.getrequest);

        adapter = new requestAdapter(context, list, new requestAdapter.OnApproveClickListener() {
            @Override
            public void OnApproveClick(int position) {
                approved(position);
            }
        }, new requestAdapter.OnDeclineClickListener() {
            @Override
            public void OnDeclineClick(int position) {
                declined(position);
            }

        });
        recyclerView.setAdapter(adapter);
        getRequests();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getRequests();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRequests();
    }

    private void getRequests() {
        Call<List<Princi_Users_data>> call = db.getPrinci_Users();
        call.enqueue(new Callback<List<Princi_Users_data>>() {
            @Override
            public void onResponse(Call<List<Princi_Users_data>> call, Response<List<Princi_Users_data>> response) {
                List<Princi_Users_data> lst = response.body();
                Collections.reverse(lst);
                if (lst != null) {
                    list.clear();
                    list.addAll(lst);
                } else {
                    Toast.makeText(context, "No Requests Available", Toast.LENGTH_SHORT).show();
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Princi_Users_data>> call, Throwable t) {
                Toast.makeText(context, "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approved(int position) {
        Princi_Users_data data = list.get(position);
        pos = true;
        data.setStatus(pos);
        long id = data.getId();
        String name = data.getName();

        Call<Princi_Users_data> call = db.updatePrinci_Users(id, data);
        call.enqueue(new Callback<Princi_Users_data>() {
            @Override
            public void onResponse(Call<Princi_Users_data> call, Response<Princi_Users_data> response) {
                Toast.makeText(context, name + " Approved", Toast.LENGTH_SHORT).show();
                getRequests();
            }

            @Override
            public void onFailure(Call<Princi_Users_data> call, Throwable t) {
                Toast.makeText(context, "Failed to Approve, Try again later", Toast.LENGTH_SHORT).show();
                getRequests();
            }
        });

    }

    private void declined(int position) {
        Princi_Users_data data = list.get(position);
        long id = data.getId();
        String name = data.getName();
        
        Call<Princi_Users_data> call = db.deletePrinci_Users(id);
        call.enqueue(new Callback<Princi_Users_data>() {
            @Override
            public void onResponse(Call<Princi_Users_data> call, Response<Princi_Users_data> response) {
                Toast.makeText(context, name + " Rejected", Toast.LENGTH_SHORT).show();
                getRequests();
            }

            @Override
            public void onFailure(Call<Princi_Users_data> call, Throwable t) {
                Toast.makeText(context, "Failed to Reject, Try again later", Toast.LENGTH_SHORT).show();
                getRequests();
            }
        });
    }
}
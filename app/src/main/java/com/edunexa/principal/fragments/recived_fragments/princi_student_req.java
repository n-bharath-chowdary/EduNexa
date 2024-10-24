package com.edunexa.principal.fragments.recived_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.Student_users_data;
import com.edunexa.student.adapters.student_users_adapter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class princi_student_req extends Fragment {
    View view;
    private Context context;
    private student_users_adapter adapter;
    private Student_users_data data;
    private RecyclerView recyclerView;
    private List<Student_users_data> list;
    private MyApi db;
    private boolean pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rur_home, container, false);

        context = getContext();
        db = RetrofitInstance.getInstance().create(MyApi.class);

        recyclerView = view.findViewById(R.id.getrequest);

        adapter = new student_users_adapter(context, list, new student_users_adapter.OnApproveClickListener() {
            @Override
            public void OnApproveClick(int position) {
                approved(position);
            }
        }, new student_users_adapter.OnDeclineClickListener() {
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
        Call<List<Student_users_data>> call = db.get_Student_Users();
        call.enqueue(new Callback<List<Student_users_data>>() {
            @Override
            public void onResponse(Call<List<Student_users_data>> call, Response<List<Student_users_data>> response) {
                List<Student_users_data> lst = response.body();
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
            public void onFailure(Call<List<Student_users_data>> call, Throwable t) {
                Toast.makeText(context, "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void declined(int position) {
        Student_users_data data = list.get(position);
        long id = data.getId();
        String name = data.getName();

        Call<Student_users_data> call = db.delete_Student_Users(id);
        call.enqueue(new Callback<Student_users_data>() {
            @Override
            public void onResponse(Call<Student_users_data> call, Response<Student_users_data> response) {
                Toast.makeText(context, name + " Rejected", Toast.LENGTH_SHORT).show();
                getRequests();
            }

            @Override
            public void onFailure(Call<Student_users_data> call, Throwable t) {
                Toast.makeText(context, "Failed to Reject, Try again later", Toast.LENGTH_SHORT).show();
                getRequests();
            }
        });
    }

    private void approved(int position) {
        Student_users_data data = list.get(position);
        pos = true;
        data.setStatus(pos);
        long id = data.getId();
        String name = data.getName();

        Call<Student_users_data> call = db.update_Student_Users(id, data);
        call.enqueue(new Callback<Student_users_data>() {
            @Override
            public void onResponse(Call<Student_users_data> call, Response<Student_users_data> response) {
                Toast.makeText(context, name + " Approved", Toast.LENGTH_SHORT).show();
                getRequests();
            }

            @Override
            public void onFailure(Call<Student_users_data> call, Throwable t) {
                Toast.makeText(context, "Failed to Approve, Try again later", Toast.LENGTH_SHORT).show();
                getRequests();
            }
        });
    }
}

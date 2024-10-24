package com.edunexa.principal.fragments.recived_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edunexa.R;
import com.edunexa.pdfViewer;
import com.edunexa.principal.adapters.received.princi_pdf_rec_adapter;
import com.edunexa.data.pdf_data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class princi_rec_pdf extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<pdf_data> list = new ArrayList<pdf_data>();
    private DatabaseReference ref, dref;
    private StorageReference storef;
    private princi_pdf_rec_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_princi_rec_pdf, container, false);

        recyclerView = view.findViewById(R.id.getpdf);
        ref = FirebaseDatabase.getInstance().getReference("RUR");
        storef = FirebaseStorage.getInstance().getReference();

        adapter = new princi_pdf_rec_adapter(list, getContext(), new princi_pdf_rec_adapter.OnViewClickListener() {
            @Override
            public void onViewClick(int pdfid) {
                viewPdf(pdfid);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float y = event.getY();
                    if (y > 50) {
                        getPdfs();
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
        getPdfs();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPdfs();
    }

    private void getPdfs() {
        dref = ref.child("Files");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    pdf_data data = dataSnapshot.getValue(pdf_data.class);
                    list.add(0, data);
                }

                adapter = new princi_pdf_rec_adapter(list, getContext(), new princi_pdf_rec_adapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int pdfid) {
                        viewPdf(pdfid);
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

    private void viewPdf(int i) {
        Intent intent = new Intent(getContext(), pdfViewer.class);
        intent.putExtra("PDf", list.get(i).getPdf_url());
        getContext().startActivity(intent);
    }
}
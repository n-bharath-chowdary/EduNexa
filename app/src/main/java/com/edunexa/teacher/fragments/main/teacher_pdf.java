package com.edunexa.teacher.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.pdf_data;
import com.edunexa.pdfViewer;
import com.edunexa.rur.adapters.pdfAdapter;
import com.edunexa.rur.adapters.sendToAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class teacher_pdf extends Fragment {
    View view;
    private FloatingActionButton btn;
    private boolean isAddPdfOpen = false;
    private LinearLayout addpdf, getpdf;
    private RecyclerView recyclerView, recyclerView2;
    private List<String> rec = new ArrayList<>();
    private List<pdf_data> list = new ArrayList<pdf_data>();
    private MyApi db;
    private String adding_filename, selected, downloadurl, ukey, des;
    private static final int PICK_FILE_REQUEST = 1;
    private DatabaseReference ref, dref;
    private StorageReference storef;
    private TextView publish;
    private pdfAdapter adapter;
    private int screenHeight;
    private Button select_pdf;
    private Uri uri = null;
    private FirebaseAuth auth;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdf, container, false);

        btn = view.findViewById(R.id.rurPDFfloat);
        addpdf = view.findViewById(R.id.addpdf_layout);
        getpdf = view.findViewById(R.id.getrurPDF);
        recyclerView = view.findViewById(R.id.getpdf);
        recyclerView2 = view.findViewById(R.id.rursend_to);
        select_pdf = view.findViewById(R.id.addpdf_btn);
        publish = view.findViewById(R.id.add_pdf);

        des = getArguments().getString("des");
        context = getContext();

        adapter = new pdfAdapter(list, context, new pdfAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(int pdfid) {
                viewPdf(pdfid);
            }
        }, new pdfAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int pdfid) {
                DeltePdf(pdfid);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Teacher").child(des);
        storef = FirebaseStorage.getInstance().getReference();

        rec.add(getResources().getString(R.string.student));

        db = RetrofitInstance.getInstance().create(MyApi.class);
        getPdfs();

        recyclerView2.setVisibility(View.GONE);
        addpdf.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddPdfOpen) {
                    closeAddPdf();
                } else {
                    openAddPdf();
                }
                isAddPdfOpen = !isAddPdfOpen;
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

//        adapter.setOnViewClickListner(this::viewPdf);
        recyclerView.setAdapter(adapter);
        return view;

    }

    private void viewPdf(int i) {
        Intent intent = new Intent(getContext(), pdfViewer.class);
        intent.putExtra("PDf", list.get(i).getPdf_url());
        getContext().startActivity(intent);
    }

    private void DeltePdf(int i) {
        dref = ref.child("Files").child(list.get(i).getUkey());
        dref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                getPdfs();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
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
        isAddPdfOpen = false;
        closeAddPdf();

        dref = ref.child("Files");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    pdf_data data = dataSnapshot.getValue(pdf_data.class);
                    list.add(0, data);
                }

                adapter = new pdfAdapter(list, context, new pdfAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int pdfid) {
                        viewPdf(pdfid);
                    }
                }, new pdfAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int pdfid) {
                        DeltePdf(pdfid);
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

    private void closeAddPdf() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, screenHeight - (float) addpdf.getHeight() / 2);
        animation.setDuration(500);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addpdf.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                getpdf.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        addpdf.startAnimation(animation);
    }

    private void openAddPdf() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);
//        recyclerView2.setAdapter(sendto_adapter);
        addpdf.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.GONE);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        int layoutHeight = addpdf.getHeight();

        int translationY = screenHeight / 2 - layoutHeight / 2;

        TranslateAnimation animation = new TranslateAnimation(0, 0, translationY, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);

        addpdf.startAnimation(animation);

        select_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFS();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPDFS(adding_filename);
            }
        });
    }

    private void addPDFS(String addingFilename) {
        StorageReference pdfRef = storef.child("Teacher_pdfs/" + addingFilename + "student");
        pdfRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;

                        Uri uri1 = uriTask.getResult();
                        downloadurl = String.valueOf(uri1);
                        uploadData(String.valueOf(uri1));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadData(String s) {
        dref = ref.child("Files");
        ukey = dref.push().getKey();

        pdf_data filedata = new pdf_data(adding_filename, downloadurl, "student", ukey, "Teacher");
        dref.child(ukey).setValue(filedata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                isAddPdfOpen = false;
                closeAddPdf();
                getPdfs();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPDFS() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();

        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContext().getContentResolver().query(uri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    adding_filename = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (uri.toString().startsWith("file://")) {
            adding_filename = new File(uri.toString()).getName();
        }

        select_pdf.setText(adding_filename);

        Toast.makeText(getContext(), "File has been chosen", Toast.LENGTH_SHORT).show();

    }
}


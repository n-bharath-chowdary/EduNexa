package com.edunexa.teacher.fragments.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.edunexa.Image_Viewer;
import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.img_data;
import com.edunexa.rur.adapters.imageAdapter;
import com.edunexa.rur.adapters.sendToAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class teacher_img extends Fragment {

    View view;
    private FloatingActionButton btn;
    private boolean isAddImageOpen = false;
    private LinearLayout addimage, getimage;
    private RecyclerView recyclerView, recyclerView2;
    private List<String> rec = new ArrayList<>();
    private List<img_data> list = new ArrayList<img_data>();
    private MyApi db;
    private String adding_filename, downloadurl, ukey, des;
    private static final int PICK_FILE_REQUEST = 1;
    private DatabaseReference ref, dref;
    private StorageReference storef;
    private TextView publish;
    private imageAdapter adapter;
    private int screenHeight;
    private Button select_image;
    private Uri uri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);

        btn = view.findViewById(R.id.rurImagefloat);
        addimage = view.findViewById(R.id.addimage_layout);
        getimage = view.findViewById(R.id.getrurImage);
        recyclerView = view.findViewById(R.id.getimage);
        recyclerView2 = view.findViewById(R.id.rursend_to);
        select_image = view.findViewById(R.id.addimage_btn);
        publish = view.findViewById(R.id.add_image);

        des = getArguments().getString("des");

        adapter = new imageAdapter(list, getContext(), new imageAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(int imageid) {
                viewImage(imageid);
            }
        }, new imageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int imageid) {
                DelteImage(imageid);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Teacher").child(des);
        storef = FirebaseStorage.getInstance().getReference();

        rec.add(getResources().getString(R.string.student));

        db = RetrofitInstance.getInstance().create(MyApi.class);
        getImages();

        recyclerView2.setVisibility(View.GONE);
        addimage.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddImageOpen) {
                    closeAddImage();
                } else {
                    openAddImage();
                }
                isAddImageOpen = !isAddImageOpen;
            }
        });

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


    private void openAddImage() {

        recyclerView.setAdapter(adapter);
//        recyclerView2.setAdapter(sendto_adapter);
        addimage.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.GONE);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        int layoutHeight = addimage.getHeight();

        int translationY = screenHeight / 2 - layoutHeight / 2;

        TranslateAnimation animation = new TranslateAnimation(0, 0, translationY, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);

        addimage.startAnimation(animation);

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImages(adding_filename);
            }
        });
    }

    private void addImages(String addingFilename) {
        StorageReference videoRef = storef.child("Teacher_images/" + addingFilename + "student");
        videoRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful()) ;

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
        dref = ref.child("Images");
        ukey = dref.push().getKey();

        img_data imgData = new img_data(adding_filename, downloadurl, "student", ukey, "Teacher");
        dref.child(ukey).setValue(imgData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                isAddImageOpen = false;
                closeAddImage();
                getImages();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void closeAddImage() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, screenHeight - (float) addimage.getHeight() / 2);
        animation.setDuration(500);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addimage.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                getimage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        addimage.startAnimation(animation);
    }

    private void getImages() {
        isAddImageOpen = false;
        closeAddImage();

        dref = ref.child("Images");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    img_data data = dataSnapshot.getValue(img_data.class);
                    list.add(0, data);
                }

                adapter = new imageAdapter(list, getContext(), new imageAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int imageid) {
                        viewImage(imageid);
                    }
                }, new imageAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int imageid) {
                        DelteImage(imageid);
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

    private void DelteImage(int i) {
        dref = ref.child("Images").child(list.get(i).getUkey());
        dref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                getImages();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
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

        select_image.setText(adding_filename);

        Toast.makeText(getContext(), "File has been chosen", Toast.LENGTH_SHORT).show();

    }
}

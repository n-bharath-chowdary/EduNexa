package com.edunexa.rur.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.VideoViewer;
import com.edunexa.rur.adapters.sendToAdapter;
import com.edunexa.rur.adapters.video_Adapter;
import com.edunexa.data.video_data;
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

public class video extends Fragment {
    View view;
    private FloatingActionButton btn;
    private boolean isAddVideoOpen = false;
    private LinearLayout addvideo, getvideo;
    private RecyclerView recyclerView, recyclerView2;
    private List<String> rec = new ArrayList<>();
    private List<video_data> list = new ArrayList<video_data>();
    private sendToAdapter sendto_adapter;
    private MyApi db;
    private String adding_filename, selected, downloadurl, ukey;
    private static final int PICK_FILE_REQUEST = 1;
    private DatabaseReference ref, dref;
    private StorageReference storef;
    private TextView publish;
    private video_Adapter adapter;
    private int screenHeight;
    private Button select_video;
    private Uri uri = null;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_video, container, false);
        btn = view.findViewById(R.id.rurVideofloat);
        addvideo = view.findViewById(R.id.addvideo_layout);
        getvideo = view.findViewById(R.id.getrurVideo);
        recyclerView = view.findViewById(R.id.getvideo);
        recyclerView2 = view.findViewById(R.id.rursend_to);
        select_video = view.findViewById(R.id.addvideo_btn);
        publish = view.findViewById(R.id.add_video);

        adapter = new video_Adapter(list, getContext(), new video_Adapter.OnViewClickListener() {
            @Override
            public void onViewClick(int video_id) {
                viewVideo(video_id);
            }
        }, new video_Adapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int video_id) {
                DeleteVideo(video_id);
            }
        });
        sendto_adapter = new sendToAdapter(rec, getContext());

        ref = FirebaseDatabase.getInstance().getReference("RUR");
        storef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        rec.add(getResources().getString(R.string.principal));
        rec.add(",");
        rec.add(getResources().getString(R.string.permanent_teacher));
        rec.add(",");
        rec.add(getResources().getString(R.string.guest_teacher));
        rec.add(",");
        rec.add(getResources().getString(R.string.non_teaching));
        rec.add(",");
        rec.add(getResources().getString(R.string.student));

        db = RetrofitInstance.getInstance().create(MyApi.class);
        getVideos();

        recyclerView2.setVisibility(View.GONE);
        addvideo.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddVideoOpen) {
                    closeAddVideo();
                } else {
                    openAddVideo();
                }
                isAddVideoOpen = !isAddVideoOpen;
            }
        });

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

//        adapter.setOnViewClickListner(this::viewVideo);
        recyclerView.setAdapter(adapter);

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

    private void viewVideo(int i) {
        Intent intent = new Intent(getContext(), VideoViewer.class);
        intent.putExtra("Video", list.get(i).getVideo_url());
        getContext().startActivity(intent);
    }

    private void DeleteVideo(int i) {
        dref = ref.child("Videos").child(list.get(i).getUkey());
        dref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                getVideos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAddVideo() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        sendto_adapter = new sendToAdapter(rec, getContext());
        sendto_adapter.setOnItemSelectedListener(new sendToAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(List<String> selectedRecipients) {
                selected = sendto_adapter.getSelectedRecipientsString();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(sendto_adapter);
        addvideo.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        int layoutHeight = addvideo.getHeight();

        int translationY = screenHeight / 2 - layoutHeight / 2;

        TranslateAnimation animation = new TranslateAnimation(0, 0, translationY, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);

        addvideo.startAnimation(animation);

        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideos();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideos(adding_filename);
            }
        });
    }

    private void addVideos(String addingFilename) {
        StorageReference videoRef = storef.child("RUR_videos/" + addingFilename + selected);
        videoRef.putFile(uri)
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
        dref = ref.child("Videos");
        ukey = dref.push().getKey();

        video_data videodata = new video_data(adding_filename, downloadurl, selected, ukey, "RUR");
        dref.child(ukey).setValue(videodata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                isAddVideoOpen = false;
                closeAddVideo();
                getVideos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectVideos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void closeAddVideo() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, screenHeight - (float) addvideo.getHeight() / 2);
        animation.setDuration(500);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addvideo.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                getvideo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        addvideo.startAnimation(animation);
    }

    private void getVideos() {
        isAddVideoOpen = false;
        closeAddVideo();

        dref = ref.child("Videos");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    video_data data = dataSnapshot.getValue(video_data.class);
                    list.add(0, data);
                }

                adapter = new video_Adapter(list, getContext(), new video_Adapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int video_id) {
                        viewVideo(video_id);
                    }
                }, new video_Adapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int video_id) {
                        DeleteVideo(video_id);
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

        select_video.setText(adding_filename);

        Toast.makeText(getContext(), "File has been chosen", Toast.LENGTH_SHORT).show();

    }
}
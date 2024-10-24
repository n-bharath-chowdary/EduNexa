package com.edunexa.rur.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.rur.adapters.msgAdapter;
import com.edunexa.rur.adapters.sendToAdapter;
import com.edunexa.data.msg_data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class message extends Fragment {
    View view;
    private FloatingActionButton btn;
    private LinearLayout addMsg, getmsg;
    private RecyclerView recyclerView, recyclerView2;
    private List<msg_data> list = new ArrayList<>();
    private TextView txt;
    private sendToAdapter sendto_adapter;
    private List<String> rec = new ArrayList<>();
    private EditText et;
    private String msg, selected;
    private boolean isAddMsgOpen = false;
    private int screenHeight;
    private MyApi db;
    private msgAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.fragment_message, container, false);

        btn = view.findViewById(R.id.rurMSGfloat);
        addMsg = view.findViewById(R.id.addmsg_layout);
        getmsg = view.findViewById(R.id.getrurMsg);
        recyclerView = view.findViewById(R.id.getmsg);
        recyclerView2 = view.findViewById(R.id.rursend_to);
        txt = view.findViewById(R.id.add_msg);
        et = view.findViewById(R.id.rur_msgwrite);

        rec.add(getResources().getString(R.string.principal));
        rec.add(",");
        rec.add(getResources().getString(R.string.permanent_teacher));
        rec.add(",");
        rec.add(getResources().getString(R.string.guest_teacher));
        rec.add(",");
        rec.add(getResources().getString(R.string.non_teaching));
        rec.add(",");
        rec.add(getResources().getString(R.string.student));

        adapter = new msgAdapter(list, getContext());

        recyclerView2.setVisibility(View.GONE);
        addMsg.setVisibility(View.GONE);

        adapter = new msgAdapter(list, getContext());

        db = RetrofitInstance.getInstance().create(MyApi.class);
        getMessages();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddMsgOpen) {
                    closeMsgWrite();
                } else {
                    openMsgWrite();
                }
                isAddMsgOpen = !isAddMsgOpen;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    float y = event.getY();
                    if(y > 50) {
                        getMessages();
                    }
                }
                return true;
            }
        });

        adapter.setOnDeleteClickListener(this::deleteMsg);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void deleteMsg(int msgid) {
        Call<String> deletedRows = db.deleteMessage(msgid);
        deletedRows.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Message deleted successfully", Toast.LENGTH_SHORT).show();
                    list.remove(getMessageIndex(msgid));
                    adapter.notifyDataSetChanged();
                    getMessages();
                } else {
                    Toast.makeText(getContext(), "Failed to delete message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to delete message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getMessageIndex(int msgid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == msgid) {
                return i;
            }
        }
        return -1;
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
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<msg_data>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeMsgWrite() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, screenHeight - (float) addMsg.getHeight() / 2);
        animation.setDuration(500);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                addMsg.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                getmsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        addMsg.startAnimation(animation);
    }

    private void openMsgWrite() {
        addMsg.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        int layoutHeight = addMsg.getHeight();

        int translationY = screenHeight / 2 - layoutHeight / 2;

        TranslateAnimation animation = new TranslateAnimation(0, 0, translationY, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);

        addMsg.startAnimation(animation);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = et.getText().toString();
                addMSG(msg);
                et.setText("");
                closeMsgWrite();
                isAddMsgOpen = false;
            }
        });
    }

    private void addMSG(String s) {
        msg_data msg = new msg_data();
        msg.setMsg(s);
        msg.setSend_to(selected);
        msg.setFrom("RUR");

        Call<String> post = db.addMessage(msg);
        post.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Message sent to" + selected, Toast.LENGTH_SHORT).show();
                getMessages();
                isAddMsgOpen = false;
                closeMsgWrite();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Message not sent" + t.getMessage(), Toast.LENGTH_SHORT).show();
                isAddMsgOpen = false;
                addMsg.setVisibility(View.GONE);
            }
        });
    }
}
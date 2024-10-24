package com.edunexa.teacher.adaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.data.Princi_Users_data;
import com.edunexa.data.teacher_user_data;

import java.util.List;

public class teacher_user_adapter extends RecyclerView.Adapter<teacher_user_adapter.teacher_user_view_adater> {

    private Context context;
    private List<teacher_user_data> list;
    private OnApproveClickListener approve_listener;
    private OnDeclineClickListener decline_listener;

    public teacher_user_adapter(Context context, List<teacher_user_data> list, OnApproveClickListener approve_listener, OnDeclineClickListener decline_listener) {
        this.context = context;
        this.list = list;
        this.approve_listener = approve_listener;
        this.decline_listener = decline_listener;
    }

    public interface OnApproveClickListener {
        void OnApproveClick(int position);
    }

    public interface OnDeclineClickListener {
        void OnDeclineClick(int position);
    }

    @NonNull
    @Override
    public teacher_user_view_adater onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);

        return new teacher_user_view_adater(view);
    }

    @Override
    public void onBindViewHolder(@NonNull teacher_user_view_adater holder, int position) {
        String name = list.get(position).getName();
        String college_name = list.get(position).getDes();

        holder.name.setText(name);
        holder.college_name.setText(college_name);
        holder.section.setText("Role");

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(approve_listener != null) {
                    approve_listener.OnApproveClick(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(decline_listener != null) {
                    decline_listener.OnDeclineClick(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        Princi_Users_data data2 = new Princi_Users_data();
        for (teacher_user_data data : list) {
            if(data.getCollege().equals(data2.getCollege_name())){
                if (data.isStatus()) {
                    count++;
                }
            }
        }
        return count;
    }

    public class teacher_user_view_adater extends RecyclerView.ViewHolder {
        private TextView approve, decline, name, college_name, section;

        public teacher_user_view_adater(@NonNull View itemView) {
            super(itemView);
            approve = itemView.findViewById(R.id.approve);
            decline = itemView.findViewById(R.id.decline);
            name = itemView.findViewById(R.id.request_name);
            college_name = itemView.findViewById(R.id.request_college);
            section = itemView.findViewById(R.id.section);
        }
    }
}

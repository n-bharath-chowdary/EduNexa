package com.edunexa.student.adapters;

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
import com.edunexa.data.Student_users_data;

import java.util.List;

public class student_users_adapter extends RecyclerView.Adapter<student_users_adapter.student_user_view_adapter> {

    private Context context;
    private List<Student_users_data> list;
    private OnApproveClickListener approve_listener;
    private OnDeclineClickListener decline_listener;

    public student_users_adapter(Context context, List<Student_users_data> list, OnApproveClickListener approve_listener, OnDeclineClickListener decline_listener) {
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
    public student_user_view_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);

        return new student_user_view_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull student_user_view_adapter holder, int position) {
        Student_users_data data = list.get(position);
        String name = data.getName();
        String college_name = data.getCollege_name();
        String course = data.getCourse() + "  " + data.getYear();

        holder.name.setText(name);
        holder.college_name.setText(college_name);
        holder.section.setText("College");
        holder.course.setText("Course");
        holder.year.setText(course);

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
        for (Student_users_data data : list) {
            if(data.getCollege_name().equals(data2.getCollege_name())){
                if (data.getStatus()) {
                    count++;
                }
            }
        }
        return count;
    }

    public class student_user_view_adapter extends RecyclerView.ViewHolder {
        private TextView approve, decline, name, college_name, section, course, year;

        public student_user_view_adapter(@NonNull View itemView) {
            super(itemView);
            approve = itemView.findViewById(R.id.approve);
            decline = itemView.findViewById(R.id.decline);
            name = itemView.findViewById(R.id.request_name);
            college_name = itemView.findViewById(R.id.request_college);
            section = itemView.findViewById(R.id.section);
            course = itemView.findViewById(R.id.course);
            year = itemView.findViewById(R.id.year);
        }
    }

}

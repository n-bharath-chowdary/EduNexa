package com.edunexa.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.data.Student_users_data;
import com.edunexa.data.msg_data;
import com.edunexa.teacher.adaters.teacher_msg_adapter;

import java.util.Arrays;
import java.util.List;

public class student_msg_adapter extends RecyclerView.Adapter<student_msg_adapter.student_msg_View_adapter> {

    private List<msg_data> list;
    private Context context;

    public student_msg_adapter(List<msg_data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public student_msg_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_rec, parent, false);
        return new student_msg_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull student_msg_View_adapter holder, int position) {
        msg_data msg = list.get(position);
        holder.msg.setText(msg.getMsg());
        holder.from.setText("From :");
        holder.to.setText(msg.getFrom());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        Student_users_data data = new Student_users_data();
        for(msg_data msg : list){
            String sendTo = msg.getSend_to();
            String college = msg.getCollege_name();
            if (Arrays.asList("student").contains(sendTo) && college.equals(data.getCollege_name())) {
                count++;
            }
        }
        return count;
    }

    public class student_msg_View_adapter extends RecyclerView.ViewHolder {
        private TextView msg, from, to;

        public student_msg_View_adapter(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg_content);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }
}

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
import com.edunexa.data.pdf_data;
import com.edunexa.teacher.adaters.teacher_pdf_adapter;

import java.util.Arrays;
import java.util.List;

public class student_pdf_adapter extends RecyclerView.Adapter<student_pdf_adapter.student_pdf_View_adapter> {

    private List<pdf_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;

    public student_pdf_adapter(List<pdf_data> list, Context context, OnViewClickListener viewClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int pdfid);
    }

    @NonNull
    @Override
    public student_pdf_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_rec, parent, false);
        return new student_pdf_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull student_pdf_View_adapter holder, int position) {
        pdf_data pdf = list.get(position);
        final int pos = position;
        pdf_data currentItem = list.get(pos);
        holder.filename.setText(pdf.getPdf_name());
        holder.from.setText("From");
        holder.to.setText(pdf.getFrom());


        holder.filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewClickListener != null) {
                    viewClickListener.onViewClick(pos);
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for(pdf_data data : list) {
            String sendTo = data.getSend_to();
            if (Arrays.asList("student").contains(sendTo)) {
                count++;
            }
        }
        return count;
    }

    public class student_pdf_View_adapter extends RecyclerView.ViewHolder {
        private TextView filename, from, to;
        public student_pdf_View_adapter(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.pdf_name);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }
}

package com.edunexa.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edunexa.R;
import com.edunexa.data.img_data;
import com.edunexa.teacher.adaters.teacher_img_adapter;

import java.util.Arrays;
import java.util.List;

public class student_img_adapter extends RecyclerView.Adapter<student_img_adapter.student_img_View_adapter> {

    private List<img_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;

    public student_img_adapter(List<img_data> list, Context context, OnViewClickListener viewClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int imageid);
    }

    @NonNull
    @Override
    public student_img_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_rec, parent, false);
        return new student_img_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull student_img_View_adapter holder, int position) {
        img_data img = list.get(position);
        String imageUrl = img.getImg_url();

        holder.from.setText("From :");
        holder.to.setText(img.getFrom());

        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClickListener != null) {
                    viewClickListener.onViewClick(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (img_data data : list) {
            String sendTo = data.getSend_to();
            if (Arrays.asList("student").contains(sendTo)) {
                count++;
            }
        }
        return count;
    }

    public class student_img_View_adapter extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private int pos;
        private Button del_btn;
        private TextView from, to;

        public student_img_View_adapter(@NonNull View itemView) {
            super(itemView);
            del_btn = itemView.findViewById(R.id.delete_btn);
            imageView = itemView.findViewById(R.id.imageView);
            pos = getAdapterPosition();
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }
}

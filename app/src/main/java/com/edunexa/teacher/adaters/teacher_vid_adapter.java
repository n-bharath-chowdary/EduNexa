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
import com.edunexa.data.video_data;
import com.edunexa.principal.adapters.received.princi_vid_rec_adapter;

import java.util.Arrays;
import java.util.List;

public class teacher_vid_adapter extends RecyclerView.Adapter<teacher_vid_adapter.teacher_vid_View_adapter> {

    private List<video_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;

    public teacher_vid_adapter(List<video_data> list, Context context, OnViewClickListener viewClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int video_id);
    }

    @NonNull
    @Override
    public teacher_vid_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_rec, parent, false);
        return new teacher_vid_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull teacher_vid_View_adapter holder, int position) {
        video_data video = list.get(position);
        String videourl = video.getVideo_url();
        holder.video_name.setText(video.getVideo_name());
        holder.from.setText("From : ");
        holder.to.setText(video.getFrom());

        holder.video_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewClickListener != null) {
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
        for(video_data data : list) {
            String sendTo = data.getSend_to();
            if (Arrays.asList("guest", "permanent", "non").contains(sendTo)) {
                count++;
            }
        }
        return count;
    }

    public class teacher_vid_View_adapter extends RecyclerView.ViewHolder {
        private TextView video_name, from, to;

        public teacher_vid_View_adapter(@NonNull View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.video_name);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }
}

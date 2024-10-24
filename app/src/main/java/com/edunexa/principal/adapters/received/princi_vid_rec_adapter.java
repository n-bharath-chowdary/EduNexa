package com.edunexa.principal.adapters.received;

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

import java.util.List;

public class princi_vid_rec_adapter extends RecyclerView.Adapter<princi_vid_rec_adapter.princi_vid_rec_View_adapter> {

    private List<video_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;

    public princi_vid_rec_adapter(List<video_data> list, Context context, OnViewClickListener viewClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int video_id);
    }


    @NonNull
    @Override
    public princi_vid_rec_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_rec, parent, false);
        return new princi_vid_rec_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull princi_vid_rec_View_adapter holder, int position) {
        video_data video = list.get(position);
        String videourl = video.getVideo_url();
        holder.video_name.setText(video.getVideo_name());
        holder.from.setText("From : ");
        holder.to.setText(video.getSend_to());

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
            String send_to = data.getSend_to();
            String[] parts = send_to.split(",");
            for (String part : parts) {
                if (part.trim().equals("Principal")) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public class princi_vid_rec_View_adapter extends RecyclerView.ViewHolder {
        private TextView video_name, from, to;

        public princi_vid_rec_View_adapter(@NonNull View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.video_name);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }

}

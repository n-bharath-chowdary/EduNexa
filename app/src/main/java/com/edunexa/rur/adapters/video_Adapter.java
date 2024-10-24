package com.edunexa.rur.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.data.video_data;

import java.util.List;

public class video_Adapter extends RecyclerView.Adapter<video_Adapter.videoView_Adpater>{

    private List<video_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;
    private OnDeleteClickListener deleteListener;

    public video_Adapter(List<video_data> list, Context context, OnViewClickListener listener, OnDeleteClickListener deleteListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = listener;
        this.deleteListener = deleteListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int video_id);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int video_id);
    }

//    public void setOnViewClickListner(OnViewClickListener listener){
//        this.viewClickListener = listener;
//    }

    @NonNull
    @Override
    public videoView_Adpater onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video, parent, false);
        return new videoView_Adpater(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videoView_Adpater holder, int position) {
        video_data video = list.get(position);
        String videourl = video.getVideo_url();
        holder.video_name.setText(video.getVideo_name());
        holder.from.setText("To : ");
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

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null) {
                    deleteListener.onDeleteClick(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class videoView_Adpater extends RecyclerView.ViewHolder {
        private TextView video_name, from, to;
        private Button del_btn;


        public videoView_Adpater(@NonNull View itemView) {
            super(itemView);
            del_btn = itemView.findViewById(R.id.delete_btn);
            video_name = itemView.findViewById(R.id.video_name);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);

        }
    }
}

package com.edunexa.rur.adapters;

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

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageView_Adapter> {
    private List<img_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;
    private OnDeleteClickListener deleteClickListener;

    public imageAdapter(List<img_data> list, Context context, OnViewClickListener listener, OnDeleteClickListener deleteClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = listener;
        this.deleteClickListener = deleteClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int imageid);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int imageid);
    }

//    public void setOnClickListener(OnViewClickListener listener){
//        this.viewClickListener = listener;
//    }

    @NonNull
    @Override
    public imageView_Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image, parent, false);
        return new imageView_Adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageView_Adapter holder, int position) {
        img_data img = list.get(position);
        String imageUrl = img.getImg_url();
        holder.from.setText("To : ");
        holder.to.setText(img.getSend_to());

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

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(holder.getAdapterPosition());
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

    public class imageView_Adapter extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private int pos;
        private TextView from, to;
        private Button del_btn;

        public imageView_Adapter(@NonNull View itemView) {
            super(itemView);
            del_btn = itemView.findViewById(R.id.delete_btn);
            imageView = itemView.findViewById(R.id.imageView);
            pos = getAdapterPosition();
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);

//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(viewClickListener != null) {
//                        viewClickListener.onViewClick(pos);
//                    } else {
//                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }
}

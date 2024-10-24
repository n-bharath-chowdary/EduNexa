package com.edunexa.principal.adapters.received;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edunexa.R;
import com.edunexa.data.img_data;

import java.util.List;

public class princi_img_rec_adapter extends RecyclerView.Adapter<princi_img_rec_adapter.princi_img_rec_View_adapter> {

    private List<img_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;

    public princi_img_rec_adapter(List<img_data> list, Context context, OnViewClickListener viewClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int imageid);
    }

    @NonNull
    @Override
    public princi_img_rec_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_rec, parent, false);
        return new princi_img_rec_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull princi_img_rec_View_adapter holder, int position) {
        img_data img = list.get(position);
        String imageUrl = img.getImg_url();

        holder.from.setText("From :");
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
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (img_data img : list) {
            String send_to = img.getSend_to();
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

    public class princi_img_rec_View_adapter extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView from, to;

        public princi_img_rec_View_adapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);
        }
    }
}

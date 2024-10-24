package com.edunexa.rur.adapters;

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

import java.util.List;

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.requestView_Adapter> {

    private Context context;
    private List<Princi_Users_data> list;
    private OnApproveClickListener approve_listener;
    private OnDeclineClickListener decline_listener;

    public requestAdapter(Context context, List<Princi_Users_data> list, OnApproveClickListener approve_listener, OnDeclineClickListener decline_listener) {
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
    public requestView_Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);

        return new requestView_Adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requestView_Adapter holder, int position) {
        String name = list.get(position).getName();
        String college_name = list.get(position).getCollege_name();

        holder.name.setText(name);
        holder.college_name.setText(college_name);

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
        for (Princi_Users_data data : list) {
            if (data.getStatus()) {
                count++;
            }
        }
        return count;
    }

    public class requestView_Adapter extends RecyclerView.ViewHolder {

        private TextView approve, decline, name, college_name;

        public requestView_Adapter(@NonNull View itemView) {
            super(itemView);

            approve = itemView.findViewById(R.id.approve);
            decline = itemView.findViewById(R.id.decline);
            name = itemView.findViewById(R.id.request_name);
            college_name = itemView.findViewById(R.id.request_college);
        }
    }
}

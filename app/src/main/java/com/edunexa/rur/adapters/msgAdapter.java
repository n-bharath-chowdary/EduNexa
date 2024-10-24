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
import com.edunexa.data.msg_data;

import java.util.List;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.msgViewAdapter>{
    private List<msg_data> list;
    private Context context;
    private OnDeleteClickListener deleteClickListener;

    public msgAdapter(List<msg_data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int msgId);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public msgViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg, parent, false);
        return new msgViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull msgViewAdapter holder, int position) {
        msg_data msg = list.get(position);
        holder.msg.setText(msg.getMsg());
        holder.position = msg.getId();
        holder.from.setText("To : ");
        holder.to.setText(msg.getSend_to());
    }
    

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class msgViewAdapter extends RecyclerView.ViewHolder {
        private TextView msg, from, to;
        private Button delete;
        private int position;

        public msgViewAdapter(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg_content);
            delete = itemView.findViewById(R.id.delete_btn);
            position = msg.getId();
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteClickListener != null){
                        deleteClickListener.onDeleteClick(position);
                    } else {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
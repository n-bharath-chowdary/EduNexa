package com.edunexa.rur.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.data.pdf_data;

import java.util.List;

public class pdfAdapter extends RecyclerView.Adapter<pdfAdapter.pdfViewAdapter>{
    private List<pdf_data> list;
    private Context context;
    private OnViewClickListener viewClickListener;
    private OnDeleteClickListener deleteClickListener;

    public pdfAdapter(List<pdf_data> list, Context context, OnViewClickListener listener, OnDeleteClickListener deleteClickListener) {
        this.list = list;
        this.context = context;
        this.viewClickListener = listener;
        this.deleteClickListener = deleteClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int pdfid);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int pdfid);
    }

    @NonNull
    @Override
    public pdfViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf, parent, false);
        return new pdfViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pdfViewAdapter holder, int position) {
        pdf_data pdf = list.get(position);
        final int pos = position;
        pdf_data currentItem = list.get(pos);
        holder.filename.setText(pdf.getPdf_name());
        holder.from.setText("To :");
        holder.to.setText(pdf.getSend_to());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(pos);
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        return list.size();
    }

    public class pdfViewAdapter extends RecyclerView.ViewHolder {
        private TextView filename, from, to;
        private Button delete;
        private int position;
        private LinearLayout viewPdf;

        public pdfViewAdapter(@NonNull View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.pdf_name);
            delete = itemView.findViewById(R.id.delete_btn);
            position = getAdapterPosition();
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);

        }
    }
}

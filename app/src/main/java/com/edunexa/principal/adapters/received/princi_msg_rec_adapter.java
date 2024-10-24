package com.edunexa.principal.adapters.received;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;
import com.edunexa.data.Princi_Users_data;
import com.edunexa.data.msg_data;

import java.util.List;

public class princi_msg_rec_adapter extends RecyclerView.Adapter<princi_msg_rec_adapter.princi_msg_rec_View_adapter> {

    private List<msg_data> list;
    private Context context;


    public princi_msg_rec_adapter(List<msg_data> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public princi_msg_rec_View_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_rec, parent, false);
        return new princi_msg_rec_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull princi_msg_rec_View_adapter holder, int position) {
        msg_data msg = list.get(position);
        holder.msg.setText(msg.getMsg());
        holder.from.setText("From : ");
        holder.to.setText(msg.getFrom());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        Princi_Users_data data = new Princi_Users_data();
        for(msg_data msg : list) {
            String send_to = msg.getSend_to();
            String college = msg.getCollege_name();
            String[] parts = send_to.split(",");
            for (String part : parts) {
                if (part.trim().equals("Principal") && college.equals(data.getCollege_name())) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public class princi_msg_rec_View_adapter extends RecyclerView.ViewHolder {
        private TextView msg, from, to;

        public princi_msg_rec_View_adapter(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg_content);
            from = itemView.findViewById(R.id.text_send_to_column);
            to = itemView.findViewById(R.id.text_send_to);

        }
    }

}

package com.edunexa.rur.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edunexa.R;

import java.util.ArrayList;
import java.util.List;

public class sendToAdapter extends RecyclerView.Adapter<sendToAdapter.sendToView> {
    private List<String> recipients;
    private boolean[] selected;
    private Context context;
    private OnItemSelectedListener onItemSelectedListener;

    public sendToAdapter(List<String> recipients, Context context) {
        this.recipients = recipients;
        this.selected = new boolean[recipients.size()];
        this.context = context;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(List<String> selectedRecipients);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
    
    @NonNull
    @Override
    public sendToView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.send_to, parent, false);
        return new sendToAdapter.sendToView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sendToView holder, int position) {
        holder.sendTo.setText(recipients.get(position));
        holder.checkbox.setChecked(selected[position]);
    }

    @Override
    public int getItemCount() {
        return recipients.size();
    }

    public class sendToView extends RecyclerView.ViewHolder {
        private CheckBox checkbox;
        private TextView sendTo;

        public sendToView(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.rur_checkbox);
            sendTo = itemView.findViewById(R.id.rursend_to);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = getAdapterPosition();
                    selected[pos] = isChecked;
                    if (onItemSelectedListener != null) {
                        List<String> selectedRecipients = getSelectedRecipients();
                        onItemSelectedListener.onItemSelected(selectedRecipients);
                    }
                }
            });
        }
    }

    private List<String> getSelectedRecipients() {
        List<String> selectedRecipients = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            if (selected[i]) {
                selectedRecipients.add(recipients.get(i));
            }
        }
        return selectedRecipients;
    }

    public String getSelectedRecipientsString() {
        StringBuilder selectedRecipientsString = new StringBuilder();
        for (int i = 0; i < recipients.size(); i++) {
            if (selected[i]) {
                selectedRecipientsString.append(recipients.get(i));
            }
        }
        return selectedRecipientsString.toString();
    }
}

package com.example.mc_ass1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> implements Filterable {

    private List<Email> emailList;
    private List<Email> emailListFull; // A copy of the full list for filtering

    public EmailAdapter(List<Email> emailList) {
        this.emailList = emailList;
        this.emailListFull = new ArrayList<>(emailList); // Make a copy of the original list
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_email, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        Email email = emailList.get(position);
        holder.avatarImageView.setImageResource(email.getAvatar());
        holder.senderTextView.setText(email.getSender());
        holder.subjectTextView.setText(email.getSubjectPreview());
        holder.timeTextView.setText(email.getTime());
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    @Override
    public Filter getFilter() {
        return emailFilter;
    }

    private Filter emailFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Email> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(emailListFull);  // If there's no query, show all emails
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Email email : emailListFull) {
                    if (email.getSender().toLowerCase().contains(filterPattern)) {
                        filteredList.add(email);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            emailList.clear();
            emailList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView senderTextView;
        TextView subjectTextView;
        TextView timeTextView;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}

package com.example.digibook.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.R;
import com.example.digibook.models.Book;

import java.util.List;

public class profile_fav_RVAdapter extends RecyclerView.Adapter<profile_fav_RVAdapter.MyViewHolder> {

    Context context;
    List<Book> favbooks;

    public profile_fav_RVAdapter(Context ct, List<Book> data) {
        context = ct;
        favbooks = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_fav_rv_item, parent, false);
        return new profile_fav_RVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.likes_count.setText(String.valueOf(favbooks.get(position).getUpvotelist().size()));
        holder.fav_count.setText(String.valueOf(favbooks.get(position).getFavlist().size()));
        holder.title.setText(favbooks.get(position).getBookname());
        holder.authors.setText(favbooks.get(position).getBookauthor().toString());
        Glide.with(context).load(favbooks.get(position).getBookcover()).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return favbooks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView cover;
        TextView likes_count, fav_count, title, authors;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.profile_fav_cover);
            likes_count = itemView.findViewById(R.id.profile_fav_likes_count);
            fav_count = itemView.findViewById(R.id.profile_fav_count);
            title = itemView.findViewById(R.id.profile_fav_title);
            authors = itemView.findViewById(R.id.profile_fav_authors);

        }
    }
}

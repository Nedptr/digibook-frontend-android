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
import com.example.digibook.models.Post;

import java.util.List;

public class profile_posts_RVAdapter extends RecyclerView.Adapter<profile_posts_RVAdapter.MyViewHolder> {

    Context context;
    List<Post> myposts;

    public profile_posts_RVAdapter(Context ct, List<Post> data){
        context = ct;
        myposts = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_posts_rv_item, parent, false);
        return new profile_posts_RVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(myposts.get(position).getName());
        holder.text.setText(myposts.get(position).getText());
        holder.like_count.setText(String.valueOf(myposts.get(position).getLikesList().size()));
        Glide.with(context).load(APIclient.base_url + myposts.get(position).getPicurl()).into(holder.pic);


    }

    @Override
    public int getItemCount() {
        return myposts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,text,like_count;
        ImageView pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profile_posts_name);
            text = itemView.findViewById(R.id.profile_posts_text);
            like_count = itemView.findViewById(R.id.profile_posts_likescount);
            pic = itemView.findViewById(R.id.profile_posts_image);

        }
    }
}

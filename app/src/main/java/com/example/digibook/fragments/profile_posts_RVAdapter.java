package com.example.digibook.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.R;
import com.example.digibook.home_viewcomments;
import com.example.digibook.models.Post;
import com.example.digibook.utilities.CurrentSession;

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
        //holder.like_count.setText(String.valueOf(myposts.get(position).getLikesList().size()));
        Glide.with(context).load(APIclient.base_url + myposts.get(position).getPicurl()).into(holder.pic);

        if(myposts.get(position).getLikesList()!=null) {
            String n = String.valueOf(myposts.get(position).getLikesList().size());
            holder.like_count.setText(n);
            //maybe not hardcode it in frontend and do isPostLiked() route in server
            // like button COLORING
            if(myposts.get(position).getLikesList().contains(CurrentSession.CurrentUser.getEmail())) {
                holder.like_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.like_color)));
                holder.like_count.setTextColor(context.getResources().getColor(R.color.like_color));
            }else{
                holder.like_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_color)));
                holder.like_count.setTextColor(context.getResources().getColor(R.color.grey_color));
            }
        }else{
            holder.like_count.setText("0");
        }

        holder.com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCommentActivity = new Intent(holder.itemView.getContext(), home_viewcomments.class);
                goCommentActivity.putExtra("postID", myposts.get(position).getDate());
                goCommentActivity.putExtra("postEmail", myposts.get(position).getEmail());
                holder.itemView.getContext().startActivity(goCommentActivity);
            }
        });

        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.likePost(CurrentSession.CurrentUser.getEmail(), myposts.get(holder.getAdapterPosition()).getEmail(), myposts.get(holder.getAdapterPosition()).getDate(), holder.like_button, holder.like_count, holder.getAdapterPosition(), context);
            }
        });


    }

    @Override
    public int getItemCount() {
        return myposts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,text,like_count;
        ImageView pic;
        TextView com;
        Button like_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profile_posts_name);
            text = itemView.findViewById(R.id.profile_posts_text);
            like_count = itemView.findViewById(R.id.profile_posts_likescount);
            pic = itemView.findViewById(R.id.profile_posts_image);
            com = itemView.findViewById(R.id.profile_posts_comments);
            like_button = itemView.findViewById(R.id.profile_posts_like_button);

        }
    }
}

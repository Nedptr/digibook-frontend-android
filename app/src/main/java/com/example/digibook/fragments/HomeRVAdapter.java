package com.example.digibook.fragments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.digibook.R;
import com.example.digibook.SearchResultsRVAdapter;
import com.example.digibook.models.Post;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.MyViewHolder> {

    //passed DATA
    Context context;
    public static List<Post> postData;

    public HomeRVAdapter (Context ct, List<Post> data ) {
        context = ct;
        postData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_post_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("position", String.valueOf(position));
        //filling items(post)
        holder.text.setText(postData.get(position).getText());
        if(postData.get(position).getLikesList()!=null) {
            String n = String.valueOf(postData.get(position).getLikesList().size());
            holder.likesCount.setText(n);
            //maybe not hardcode it in frontend and do isPostLiked() route in server
            // like button COLORING
            if(HomeRVAdapter.postData.get(position).getLikesList().contains(CurrentSession.CurrentUserEmail)) {
                holder.likeButton.setBackgroundColor(Color.BLUE);
                holder.likesCount.setTextColor(Color.BLUE);
            }else{
                holder.likeButton.setBackgroundColor(Color.GRAY);
                holder.likesCount.setTextColor(Color.GRAY);
            }
        }else{
            holder.likesCount.setText("0");
        }

        Glide.with(context).load(R.drawable.slide).into(holder.pic);

        // wrong if(holder.likesCount.getText().toString().compareTo("1")==0)

        //handling like button
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debuging", "itemClicked position " + String.valueOf(position));
                Log.d("debuging", "this is item position with getadappos: " + String.valueOf(holder.getAdapterPosition()));
                CurrentSession.likePost(CurrentSession.CurrentUser.getEmail(), postData.get(holder.getAdapterPosition()).getEmail(), postData.get(holder.getAdapterPosition()).getDate(), holder.likeButton, holder.likesCount, holder.getAdapterPosition());
                //notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        return postData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text, likesCount;
        ImageView pic;
        Button likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.homePostText);
            likesCount = itemView.findViewById(R.id.homeLikesCount);
            pic = itemView.findViewById(R.id.homePostUserPicture);
            likeButton = itemView.findViewById(R.id.homeLikeButton);

        }
    }


}

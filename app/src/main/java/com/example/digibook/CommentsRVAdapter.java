package com.example.digibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digibook.models.Comment;

import java.util.List;

public class CommentsRVAdapter extends RecyclerView.Adapter<CommentsRVAdapter.MyViewHolder> {

    // declate data and pass it in the constructor
    Context context;
    public static List<Comment> commentData;

    public CommentsRVAdapter(Context ct, List<Comment> Data){
        context = ct;
        commentData = Data;
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_rv_item, parent, false);
        return new CommentsRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(commentData.get(position).getName());
        holder.text.setText(commentData.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,text;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.commentName);
            text = itemView.findViewById(R.id.commentText);
            image = itemView.findViewById(R.id.commentImage);
        }
    }
}

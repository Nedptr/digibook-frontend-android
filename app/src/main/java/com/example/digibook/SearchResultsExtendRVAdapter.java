package com.example.digibook;

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
import com.example.digibook.models.BookComment;
import com.example.digibook.models.Comment;

import java.util.List;

public class SearchResultsExtendRVAdapter extends RecyclerView.Adapter<SearchResultsExtendRVAdapter.MyViewHolder> {

    Context context;
    public static List<BookComment> bookcommentsData;

    public SearchResultsExtendRVAdapter(Context ct, List<BookComment> data){
        context = ct;
        bookcommentsData= data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_rv_item, parent, false);
        return new SearchResultsExtendRVAdapter.MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(bookcommentsData.get(position).getName());
        holder.text.setText(bookcommentsData.get(position).getText());
        Glide.with(context).load(APIclient.base_url + bookcommentsData.get(position).getPicurl()).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return bookcommentsData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,text;
        ImageView pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentName);
            text = itemView.findViewById(R.id.commentText);
            pic = itemView.findViewById(R.id.commentImage);
        }
    }

}

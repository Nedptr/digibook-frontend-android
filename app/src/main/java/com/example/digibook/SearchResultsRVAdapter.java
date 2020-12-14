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
import com.example.digibook.models.booksearchmodels.Item;

import java.util.List;

public class SearchResultsRVAdapter extends RecyclerView.Adapter<SearchResultsRVAdapter.MyViewHolder> {

    Context context;
    List<Item> bookdata;

    //Adapter Construction that u pass the data into
    public SearchResultsRVAdapter(Context ct, List<Item> data){
        context = ct;
        bookdata = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_results_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set the data with holder.
        holder.title.setText(bookdata.get(position).getVolumeInfo().getTitle());
        holder.author.setText(bookdata.get(position).getVolumeInfo().getAuthors().toString());
        Glide.with(context).load(bookdata.get(position).getVolumeInfo().getImageLinks().getThumbnail()).into(holder.bookimage);
    }

    @Override
    public int getItemCount() {
        return bookdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookimage;
        TextView title;
        TextView author;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            bookimage = itemView.findViewById(R.id.book_thumbnail);
        }
    }
}

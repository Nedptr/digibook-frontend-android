package com.example.digibook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.Book;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.booksearchmodels.Item;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsRVAdapter extends RecyclerView.Adapter<SearchResultsRVAdapter.MyViewHolder> {

    Context context;
    List<Item> bookdata;
    BookSearch rawdata;

    //Adapter Construction that u pass the data into
    public SearchResultsRVAdapter(Context ct, BookSearch data){
        context = ct;
        bookdata = data.getItems();
        rawdata = data;
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
        if(bookdata.get(position).getVolumeInfo().getTitle() != null) {
            holder.title.setText(bookdata.get(position).getVolumeInfo().getTitle());
        }else{
            holder.title.setText("N/A");
        }
        if(bookdata.get(position).getVolumeInfo().getAuthors() != null) {
            holder.author.setText(bookdata.get(position).getVolumeInfo().getAuthors().toString());
        }else{
            holder.author.setText("N/A");
        }
        if(bookdata.get(position).getVolumeInfo().getImageLinks().getThumbnail() != null) {
            Glide.with(context).load(bookdata.get(position).getVolumeInfo().getImageLinks().getThumbnail()).into(holder.bookimage);
        }else{
            Glide.with(context).load(R.drawable.ic_baseline_error_outline_24).into(holder.bookimage);
        }

        // extend handler
        holder.extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent goExtendActivity = new Intent(holder.itemView.getContext(), SearchResultsExtend.class);
                goExtendActivity.putExtra("pos", position);
                goExtendActivity.putExtra("bookinfodata", rawdata);*/

                // extend book call
                Book book = new Book();
                book.setBookname(bookdata.get(position).getVolumeInfo().getTitle());
                //book.setBookauthor(bookdata.get(position).getVolumeInfo().getAuthors().toString());
                book.setBookcover(bookdata.get(position).getVolumeInfo().getImageLinks().getThumbnail());
                book.setBookid(bookdata.get(position).getId());
                Call<Book> extendbookcall = APIclient.apIinterface().addbook(book);
                extendbookcall.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        if(response.isSuccessful()){
                            Intent goExtendActivity = new Intent(holder.itemView.getContext(), SearchResultsExtend.class);
                            goExtendActivity.putExtra("pos", position);
                            goExtendActivity.putExtra("bookinfodata", rawdata);
                            goExtendActivity.putExtra("dbbook", response.body());
                            holder.itemView.getContext().startActivity(goExtendActivity);

                        }else{
                            Log.d("extendtest", "extendaddbook unsucc");
                        }
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                Log.d("bookinfointenttest", rawdata.getClass().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookimage;
        TextView title;
        TextView author;
        TextView extend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            bookimage = itemView.findViewById(R.id.book_thumbnail);
            extend = itemView.findViewById(R.id.book_extend);
        }
    }
}

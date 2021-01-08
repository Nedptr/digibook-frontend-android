package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.Book;
import com.example.digibook.models.BookComment;
import com.example.digibook.models.Comment;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.booksearchmodels.Item;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsExtend extends AppCompatActivity {

    TextView title,author,upvote_count, addcomment, fav_count;
    ImageView cover;
    Button upvote,fav;
    RecyclerView recyclerView;
    EditText addcommentext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_extend);

        title = findViewById(R.id.extendbook_title);
        author = findViewById(R.id.extendbook_author);
        upvote_count = findViewById(R.id.extendbook_upvote_count);
        cover = findViewById(R.id.extendbook_image);
        upvote = findViewById(R.id.extendbook_upvote);
        fav = findViewById(R.id.extendbook_fav);
        addcomment = findViewById(R.id.extendAddComment);
        addcommentext = findViewById(R.id.extendAddCommentText);
        fav_count = findViewById(R.id.extendbook_fav_count);

        // toolbar handle
        Toolbar toolbar = findViewById(R.id.extendToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Extended Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // getting book data intent
        int pos = (int)getIntent().getIntExtra("pos", 1);
        BookSearch bookinfo = (BookSearch)getIntent().getSerializableExtra("bookinfodata");

        Log.d("null5ra", bookinfo.getItems().get(pos).getVolumeInfo().getTitle().toString());
        title.setText(bookinfo.getItems().get(pos).getVolumeInfo().getTitle());
        author.setText(bookinfo.getItems().get(pos).getId());
        if(bookinfo.getItems().get(pos).getVolumeInfo().getImageLinks().getThumbnail() != null) {
            Glide.with(getApplicationContext()).load(bookinfo.getItems().get(pos).getVolumeInfo().getImageLinks().getThumbnail()).into(cover);
        }else{
            Glide.with(getApplicationContext()).load(R.drawable.ic_baseline_error_outline_24).into(cover);
        }

        // getting created db book
        Book db_book = (Book)getIntent().getSerializableExtra("dbbook");
        if(db_book.getFavlist()!=null)
        {
            fav_count.setText(String.valueOf(db_book.getFavlist().size()));
            // fav button color
            if(db_book.getFavlist().contains(CurrentSession.CurrentUser.getEmail())){
                fav.setBackgroundColor(Color.BLUE);
                fav_count.setTextColor(Color.BLUE);
            }else{
                fav.setBackgroundColor(Color.GRAY);
                fav_count.setTextColor(Color.GRAY);
            }
        }else{
            fav_count.setText("0");
        }

        if(db_book.getUpvotelist()!=null)
        {
            upvote_count.setText(String.valueOf(db_book.getUpvotelist().size()));
            // upvote button color
            if(db_book.getUpvotelist().contains(CurrentSession.CurrentUser.getEmail())){
                upvote.setBackgroundColor(Color.BLUE);
                upvote_count.setTextColor(Color.BLUE);
            }else{
                upvote.setBackgroundColor(Color.GRAY);
                upvote_count.setTextColor(Color.GRAY);
            }
        }else{
            upvote_count.setText("0");
        }

        // RV handler
        recyclerView = findViewById(R.id.extendRV);
        Call<List<BookComment>> getbookcommentscall = APIclient.apIinterface().getbookcomments(bookinfo.getItems().get(pos).getId()); // param postID(date);
        getbookcommentscall.enqueue(new Callback<List<BookComment>>() {
            @Override
            public void onResponse(Call<List<BookComment>> call, Response<List<BookComment>> response) {
                if(response.isSuccessful()) {
                    SearchResultsExtendRVAdapter myAdapter = new SearchResultsExtendRVAdapter(getApplicationContext(), response.body());
                    recyclerView.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
                    //layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    //recyclerView.scrollToPosition(0);
                }else{
                    Log.d("homeNet", "unsucc");
                }
            }

            @Override
            public void onFailure(Call<List<BookComment>> call, Throwable t) {
                Log.d("homeNet", t.toString());
                t.printStackTrace();
            }
        }); // pass data that um ight be getting from intent or call

        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.addbookComment(addcommentext.getText().toString(), bookinfo.getItems().get(pos).getId(), (SearchResultsExtendRVAdapter) recyclerView.getAdapter(), recyclerView, addcommentext);
                //CurrentSession.addnotification(getIntent().getStringExtra("postEmail"), "Commented", getIntent().getStringExtra("postID"));
            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.addupvote(CurrentSession.CurrentUser.getEmail(), bookinfo.getItems().get(pos).getId(), upvote, upvote_count);
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.addfav(CurrentSession.CurrentUser.getEmail(), bookinfo.getItems().get(pos).getId(), fav, fav_count);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
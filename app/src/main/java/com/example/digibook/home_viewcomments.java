package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.digibook.Networking.APIclient;
import com.example.digibook.fragments.HomeRVAdapter;
import com.example.digibook.models.Comment;
import com.example.digibook.models.Post;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class home_viewcomments extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView addcomment;
    EditText textComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_viewcomments);

        addcomment = findViewById(R.id.homeAddComment);
        textComment = findViewById(R.id.homeAddCommentText);

        // toolbar handle
        Toolbar toolbar = findViewById(R.id.homeCommentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // RV handler
        recyclerView = findViewById(R.id.homeCommentRV);
        Call<List<Comment>> getallpostcomments = APIclient.apIinterface().getallpostcomments(getIntent().getStringExtra("postID")); // param postID(date);
        getallpostcomments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()) {
                    CommentsRVAdapter myAdapter = new CommentsRVAdapter(getApplicationContext(), response.body());
                    recyclerView.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, true);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.scrollToPosition(0);
                }else{
                    Log.d("homeNet", "unsucc");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d("homeNet", t.toString());
                t.printStackTrace();
            }
        }); // pass data that um ight be getting from intent or call

        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.addComment(textComment.getText().toString(), getIntent().getStringExtra("postID"), (CommentsRVAdapter) recyclerView.getAdapter(), recyclerView, textComment);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
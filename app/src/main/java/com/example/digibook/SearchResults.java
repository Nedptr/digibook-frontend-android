package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.digibook.models.booksearchmodels.BookSearch;

import retrofit2.Response;

public class SearchResults extends AppCompatActivity {

    RecyclerView ResRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // getting Data
        BookSearch data = (BookSearch)getIntent().getSerializableExtra("data");
        assert data != null;
        Log.d("sss", data.getTotalItems().toString());

        //find recyclerView
        ResRecyclerView = findViewById(R.id.SearchResultsRecyclerView);


        // Provide Data with Call.



        // create adapter and pass data into
        SearchResultsRVAdapter myAdapter = new SearchResultsRVAdapter(this, data.getItems());
        ResRecyclerView.setAdapter(myAdapter);
        ResRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
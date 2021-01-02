package com.example.digibook.fragments;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.R;
import com.example.digibook.SearchResultsRVAdapter;
import com.example.digibook.models.Post;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    //HomeRVAdapter myAdapter;
    ImageView profilePic;
    EditText textPost;
    Button submitButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewroot = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = viewroot.findViewById(R.id.homeRecyclerView);
        profilePic = viewroot.findViewById(R.id.homeProfilePicture);
        textPost = viewroot.findViewById(R.id.homeWritePost);
        submitButton = viewroot.findViewById(R.id.homeAddPost);


        //CurrentSession.getAllPosts();
        Call<List<Post>> getPostsCall = APIclient.apIinterface().getallposts();
        getPostsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()) {
                    HomeRVAdapter myAdapter = new HomeRVAdapter(getContext(), response.body());
                    Glide.with(getContext()).load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl()).into(profilePic);
                    recyclerView.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, true);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.scrollToPosition(0);
                }else{
                    Glide.with(getContext()).load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl()).into(profilePic);
                    Log.d("homeNet", "unsucc");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("homeNet", t.toString());
                t.printStackTrace();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSession.addPost(textPost.getText().toString(), (HomeRVAdapter) recyclerView.getAdapter(), recyclerView, textPost);

            }
        });


        // Inflate the layout for this fragment
        return viewroot;
    }
}
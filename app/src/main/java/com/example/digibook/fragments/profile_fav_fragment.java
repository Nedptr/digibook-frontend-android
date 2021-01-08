package com.example.digibook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.digibook.Networking.APIclient;
import com.example.digibook.R;
import com.example.digibook.models.Book;
import com.example.digibook.models.Notification;
import com.example.digibook.utilities.CurrentSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_fav_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_fav_fragment extends Fragment {

    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_fav_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_fav_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_fav_fragment newInstance(String param1, String param2) {
        profile_fav_fragment fragment = new profile_fav_fragment();
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
        // Inflate the layout for this fragment
        View viewroot = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = viewroot.findViewById(R.id.notificationRV);

        // get my fav books call
        Call<List<Book>> getfavbookscall = APIclient.apIinterface().getfavbooks(CurrentSession.CurrentUser.getEmail());
        getfavbookscall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()){
                    Log.d("notif", "succ");
                    profile_fav_RVAdapter myAdapter = new profile_fav_RVAdapter(getContext(), response.body());
                    recyclerView.setAdapter(myAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, true);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    //recyclerView.scrollToPosition(0);
                } else {
                    Log.d("notif", "unsucc notif");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return viewroot;
    }
}
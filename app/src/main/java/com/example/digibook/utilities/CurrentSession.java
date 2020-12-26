package com.example.digibook.utilities;

import android.accessibilityservice.AccessibilityService;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Parcelable;
import android.os.TokenWatcher;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.digibook.LoginActivity;
import com.example.digibook.MainNavActivity;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.RegisterActivity;
import com.example.digibook.SearchResults;
import com.example.digibook.SettingsActivity;
import com.example.digibook.fragments.HomeRVAdapter;
import com.example.digibook.fragments.ProfileFragment;
import com.example.digibook.fragments.SearchFragment;
import com.example.digibook.models.Post;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.likepostResponse;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CurrentSession {


    // static useful variables
    public static User CurrentUser = new User();
    public static String CurrentUserEmail;
    public static String CurrentUserToken;



    public static void getCurrentUser(String email) {
        Call<User> getUserCall = APIclient.apIinterface().getCurrentUser(email);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                CurrentUser = response.body();
                Log.d("CurrentSessionTest", CurrentUser.getName());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CurrentSessionTestF", t.toString());
            }
        });

    }

    // TODO: handle erros and exeptions , for example if totalitems = 0 > NOT FOUND BOOK ...
    public static void uploadImageSearch(String imageurl, Context ct){
        File image = new File(imageurl);
        RequestBody reqbody = RequestBody.create(MediaType.parse("multipart/form-data"),image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("textimage", image.getName() , reqbody);

        Call<BookSearch> uploadCall = APIclient.apIinterface().uploadImage(part);
        uploadCall.enqueue(new Callback<BookSearch>() {
            @Override
            public void onResponse(Call<BookSearch> call, Response<BookSearch> response) {
                if(response.isSuccessful()){
                    Log.d("uploadImageNet", response.body().getTotalItems().toString());
                    Intent data = new Intent( ct, SearchResults.class);
                    data.putExtra("data", response.body());
                    ct.startActivity(data);
                }else {
                    Log.d("uploadImageNet", "unsuc");
                }
            }

            @Override
            public void onFailure(Call<BookSearch> call, Throwable t) {
                Log.d("uploadImageNet", t.toString());
            }
        });
    }

    public static void addPost(String text, HomeRVAdapter myAdapter, RecyclerView recyclerView, EditText textview){
        Post post = new Post();
        post.setEmail(CurrentSession.CurrentUser.getEmail());
        post.setName(CurrentSession.CurrentUser.getName());
        post.setText(text);

        Call<Post> addPostCall = APIclient.apIinterface().addPost(post);
        addPostCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()){
                    Log.d("debuging","itemcount before add : " + String.valueOf(myAdapter.getItemCount()));
                    Log.d("debuging", "firstpost before: " + HomeRVAdapter.postData.get(0).getText());
                    Log.d("debuging", "lastpost before: " + HomeRVAdapter.postData.get(HomeRVAdapter.postData.size() - 1).getText());
                    HomeRVAdapter.postData.add(response.body());
                    myAdapter.notifyDataSetChanged();
                    Log.d("debuging","itemcount after add : " + String.valueOf(myAdapter.getItemCount()));
                    Log.d("debuging", "firstpost after: " + HomeRVAdapter.postData.get(0).getText());
                    Log.d("debuging", "lastpost after: " + HomeRVAdapter.postData.get(HomeRVAdapter.postData.size() - 1).getText());
                    //Log.d("debuging", "current position: " + )
                    recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                    textview.getText().clear();
                    
                }else{
                    Log.d("HomeNet", "unsuc AddPost");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("homeNet", t.toString());
                t.printStackTrace();
            }
        });
    }

    public static void likePost(String useremail, String owneremail, String postid, Button likebutton, TextView likecount, int position){
        Call<likepostResponse> likepostCall = APIclient.apIinterface().likepost(useremail,owneremail,postid);
        likepostCall.enqueue(new Callback<likepostResponse>() {
            @Override
            public void onResponse(Call<likepostResponse> call, Response<likepostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().compareTo("Like")==0) {
                        likebutton.setBackgroundColor(Color.BLUE);
                        likecount.setTextColor(Color.BLUE);
                    }else{
                        likebutton.setBackgroundColor(Color.GRAY);
                        likecount.setTextColor(Color.GRAY);
                    }
                    likecount.setText(response.body().getCount());
                    List<String> newList;
                    newList = response.body().getNewlikeslist();
                    HomeRVAdapter.postData.get(position).setLikesList(newList);

                } else {
                    Log.d("homeNet", "unsucc likepost:  " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<likepostResponse> call, Throwable t) {
                Log.d("homeNet",t.toString());
            }
        });
    }


    //upload profile picture
    public static void uploadProfilePicture(String imageurl, Context ct){
        File image = new File(imageurl);
        RequestBody reqbody = RequestBody.create(MediaType.parse("multipart/form-data"),image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("profilepicture", image.getName() , reqbody);

        Call<String> uploadCall = APIclient.apIinterface().uploadProfilePicture(CurrentUser.getEmail(), part);
        uploadCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Log.d("uploadImageNet", response.body());
/*                    Intent data = new Intent( ct, SearchResults.class);
                    data.putExtra("data", response.body());
                    ct.startActivity(data);*/
                }else {
                    Log.d("uploadImageNet", "unsuc");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("uploadImageNet", t.toString());
            }
        });
    }

    public static void updateUser(User user, Context ct, String beforeUpdateEmail, String beforePassword){
        Call<User> updateCall = APIclient.apIinterface().updateUser(beforeUpdateEmail, beforePassword, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    CurrentSession.CurrentUser = response.body();
                    Toast.makeText(ct,"Updated Successfuly!",Toast.LENGTH_LONG).show();
                    Intent goMainNavAct = new Intent(ct, MainNavActivity.class);
                    goMainNavAct.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    ct.startActivity(goMainNavAct);

                }else{
                    Log.d("updateNet", "unsuc " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("updateNet", "failnet " + t.toString());
                t.printStackTrace();
            }
        });
    }

}

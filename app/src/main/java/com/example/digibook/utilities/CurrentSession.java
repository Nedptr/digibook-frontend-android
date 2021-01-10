package com.example.digibook.utilities;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.TokenWatcher;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digibook.CommentsRVAdapter;
import com.example.digibook.LoginActivity;
import com.example.digibook.MainNavActivity;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.RegisterActivity;
import com.example.digibook.SearchResults;
import com.example.digibook.SearchResultsExtendRVAdapter;
import com.example.digibook.SettingsActivity;
import com.example.digibook.fragments.HomeRVAdapter;
import com.example.digibook.fragments.ProfileFragment;
import com.example.digibook.fragments.SearchFragment;
import com.example.digibook.models.Book;
import com.example.digibook.models.BookComment;
import com.example.digibook.models.Comment;
import com.example.digibook.models.Notification;
import com.example.digibook.models.Post;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.likepostResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static void uploadImageSearch(Bitmap bitmap, Context ct) throws IOException {
        File image = CurrentSession.bitmapToFile(ct, bitmap, "uploadedImageSearch.png");
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
        post.setPicurl(CurrentSession.CurrentUser.getPicurl());
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
                        CurrentSession.addnotification(owneremail, "Liked", postid);

                    }else{
                        likebutton.setBackgroundColor(Color.GRAY);
                        likecount.setTextColor(Color.GRAY);
                        CurrentSession.addnotification(owneremail, "Disliked", postid);

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
    // using the code directly in settings
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
                    CurrentSession.CurrentUser.setPicurl(response.body());
                    Log.d("pictest", response.body().toString());
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

    public static void updateUser(User user, Context ct, String beforeUpdateEmail, String beforePassword, TextView error){
        Call<User> updateCall = APIclient.apIinterface().updateUser(beforeUpdateEmail, beforePassword, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Log.d("crashfix", response.body().getPicurl().toString());
                    CurrentSession.CurrentUser = response.body();
                    Toast.makeText(ct,"Updated Successfuly!",Toast.LENGTH_LONG).show();
                    Intent goMainNavAct = new Intent(ct, MainNavActivity.class);
                    goMainNavAct.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    ct.startActivity(goMainNavAct);

                }else{
                    Log.d("crashfix", "unsuc " + response.errorBody().toString());
                    try {
                        error.setText(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("crashfix", "failnet " + t.toString());
                t.printStackTrace();
            }
        });
    }

    public static void addComment(String text, String postID, CommentsRVAdapter myAdapter, RecyclerView recyclerView, EditText textview){
        Comment newcomment = new Comment();
        newcomment.setEmail(CurrentSession.CurrentUser.getEmail());
        newcomment.setName(CurrentSession.CurrentUser.getName());
        newcomment.setPicurl(CurrentSession.CurrentUser.getPicurl());
        newcomment.setText(text);
        newcomment.setDate(postID);

        Call<Comment> addcommentcall = APIclient.apIinterface().addComment(newcomment);
        addcommentcall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d("debuging", "lastpost before: " + HomeRVAdapter.postData.get(HomeRVAdapter.postData.size() - 1).getText());
                CommentsRVAdapter.commentData.add(response.body());
                myAdapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                textview.getText().clear();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public static void addnotification(String email, String action, String notifid){
        Notification newnotif = new Notification();
        newnotif.setCurrentemail(CurrentSession.CurrentUser.getEmail());
        newnotif.setName(CurrentSession.CurrentUser.getName());
        newnotif.setPicurl(CurrentSession.CurrentUser.getPicurl());
        newnotif.setEmail(email); // post's owner email
        newnotif.setAction(action);
        newnotif.setNotificationid(notifid);


        Call<Notification> addnotifcall = APIclient.apIinterface().addnotification(newnotif);
        addnotifcall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    Log.d("addNotif", "succ add notif");


                } else {
                    Log.d("addNotif", "unsucc add notif");
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void addbookComment(String text, String bookid, SearchResultsExtendRVAdapter myAdapter, RecyclerView recyclerView, EditText textview){
        BookComment newcomment = new BookComment();
        newcomment.setBookid(bookid);
        newcomment.setEmail(CurrentSession.CurrentUser.getEmail());
        newcomment.setName(CurrentSession.CurrentUser.getName());
        newcomment.setPicurl(CurrentSession.CurrentUser.getPicurl());
        newcomment.setText(text);

        Call<BookComment> addcommentcall = APIclient.apIinterface().addbookcomment(newcomment);
        addcommentcall.enqueue(new Callback<BookComment>() {
            @Override
            public void onResponse(Call<BookComment> call, Response<BookComment> response) {
                //Log.d("debuging", "lastpost before: " + HomeRVAdapter.postData.get(HomeRVAdapter.postData.size() - 1).getText());
                SearchResultsExtendRVAdapter.bookcommentsData.add(response.body());
                myAdapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                textview.getText().clear();
            }

            @Override
            public void onFailure(Call<BookComment> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

/*    public static void extendbook(Book book){
        Call<Book> extendbookcall = APIclient.apIinterface().addbook(book);
        extendbookcall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful()){
                    Log.d("extendtest", "extendaddbook succ");

                }else{
                    Log.d("extendtest", "extendaddbook unsucc");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    public static void addupvote(String useremail, String bookid, Button likebutton, TextView likecount){
        Call<likepostResponse> upvotecall = APIclient.apIinterface().addupvote(bookid,useremail);
        upvotecall.enqueue(new Callback<likepostResponse>() {
            @Override
            public void onResponse(Call<likepostResponse> call, Response<likepostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().compareTo("Like")==0) {
                        likebutton.setBackgroundColor(Color.BLUE);
                        likecount.setTextColor(Color.BLUE);
                        //CurrentSession.addnotification(owneremail, "Liked", bookid);

                    }else{
                        likebutton.setBackgroundColor(Color.GRAY);
                        likecount.setTextColor(Color.GRAY);
                        //CurrentSession.addnotification(owneremail, "Disliked", bookid);

                    }
                    likecount.setText(response.body().getCount());
                    //List<String> newList;
                    //newList = response.body().getNewlikeslist();
                    //HomeRVAdapter.postData.get(position).setLikesList(newList);

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

    public static void addfav(String useremail, String bookid, Button likebutton, TextView likecount){
        Call<likepostResponse> addfavcall = APIclient.apIinterface().addbookfav(bookid,useremail);
        addfavcall.enqueue(new Callback<likepostResponse>() {
            @Override
            public void onResponse(Call<likepostResponse> call, Response<likepostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().compareTo("Like")==0) {
                        likebutton.setBackgroundColor(Color.BLUE);
                        likecount.setTextColor(Color.BLUE);
                        //CurrentSession.addnotification(owneremail, "Liked", bookid);

                    }else{
                        likebutton.setBackgroundColor(Color.GRAY);
                        likecount.setTextColor(Color.GRAY);
                        //CurrentSession.addnotification(owneremail, "Disliked", bookid);

                    }
                    likecount.setText(response.body().getCount());
                    //List<String> newList;
                    //newList = response.body().getNewlikeslist();
                    //HomeRVAdapter.postData.get(position).setLikesList(newList);

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

    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }

    // image options
    // not usingthis , using the code directly in settingsAct and SearchFragment with mods
    public static void selectImage(Context context, Activity act) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    act.startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    act.startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

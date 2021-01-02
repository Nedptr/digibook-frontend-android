package com.example.digibook.Networking;


import com.example.digibook.models.Book;
import com.example.digibook.models.BookComment;
import com.example.digibook.models.Comment;
import com.example.digibook.models.Notification;
import com.example.digibook.models.Post;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.likepostResponse;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIinterface {

    @GET("dbtest")
    Call<List<User>> getUsers();

    @GET("hey")
    Call<String> getHey();

    @POST("api/user/register")
    Call<User> registerUser(@Body User user);

    @POST("api/user/login")
    Call<String> loginUser(@Body User user);

    @GET("api/CurrentSession/{email}")
    Call<User> getCurrentUser(@Path("email") String email);

    @POST("booksearch/test")
    Call<BookSearch> searchBook(@Body JSONObject json);

    @Multipart
    @POST("booksearch/search")
    Call<BookSearch> uploadImage(@Part MultipartBody.Part file);

    @GET("api/user/home/getallposts")
    Call<List<Post>> getallposts();

    @POST("api/user/home/addpost")
    Call<Post> addPost(@Body Post post);

    @GET("api/user/home/likepost/{currentUserEmail}/{postOwnerEmail}/{postID}")
    Call<likepostResponse> likepost(@Path("currentUserEmail") String currentUserEmail, @Path("postOwnerEmail") String postOwnerEmail, @Path("postID") String postID);

    @Multipart
    @POST("api/user/profile/uploadpic/{CurrentUserEmail}")
    Call<String> uploadProfilePicture(@Path("CurrentUserEmail") String CurrentUserEmail, @Part MultipartBody.Part file);

    @POST("api/user/profile/update/{email}/{password}")
    Call<User> updateUser(@Path("email") String email, @Path("password") String password, @Body User user);

    @GET("api/user/home/allpostcomments/{date}")
    Call<List<Comment>> getallpostcomments(@Path("date") String date);

    @POST("api/user/home/addcomment")
    Call<Comment> addComment(@Body Comment comment);

    @GET("api/user/notifications/getallnotifications/{email}")
    Call<List<Notification>> getallnotifications(@Path("email") String email);

    @POST("api/user/notifications/addnotification")
    Call<Notification> addnotification(@Body Notification notification);

    // ez

    @POST("booksearch/extendedresults")
    Call<Book> addbook(@Body Book book);

    @POST("booksearch/extendedupvote/{bookid}/{currentUserEmail}")
    Call<likepostResponse> addupvote(@Path("bookid") String bookid, @Path("currentUserEmail") String currentUserEmail);

    @GET("booksearch/extendedallcomments/{bookid}")
    Call<List<BookComment>> getbookcomments(@Path("bookid") String bookid);

    @POST("booksearch/extendedaddcomment")
    Call<BookComment> addbookcomment(@Body BookComment bookcomment);

    @POST("booksearch/extendedaddfav/{bookid}/{currentUserEmail}")
    Call<likepostResponse> addbookfav(@Path("bookid") String bookid, @Path("currentUserEmail") String currentUserEmail);

    @GET("api/user/profile/allfavbooks/{email}")
    Call<Book> getfavbooks(@Path("email") String email);

}

package com.example.digibook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    @SerializedName("bookid")
    @Expose
    private String bookid;
    @SerializedName("bookname")
    @Expose
    private String bookname;
    @SerializedName("bookauthor")
    @Expose
    private String bookauthor;
    @SerializedName("bookcover")
    @Expose
    private String bookcover;
    @SerializedName("upvotelist")
    @Expose
    private List<Object> upvotelist = null;
    @SerializedName("favlist")
    @Expose
    private List<Object> favlist = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookcover() {
        return bookcover;
    }

    public void setBookcover(String bookcover) {
        this.bookcover = bookcover;
    }

    public List<Object> getUpvotelist() {
        return upvotelist;
    }

    public void setUpvotelist(List<Object> upvotelist) {
        this.upvotelist = upvotelist;
    }

    public List<Object> getFavlist() {
        return favlist;
    }

    public void setFavlist(List<Object> favlist) {
        this.favlist = favlist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}

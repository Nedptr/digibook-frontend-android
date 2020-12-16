package com.example.digibook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class likepostResponse {
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("likestatus")
    @Expose
    private String likestatus;
    @SerializedName("newlikeslist")
    @Expose
    private List<String> newlikeslist;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return likestatus;
    }

    public void setStatus(String status) {
        this.likestatus = status;
    }

    public List<String> getNewlikeslist() {
        return newlikeslist;
    }

    public void setNewlikeslist(List<String> newlikeslist) {
        this.newlikeslist = newlikeslist;
    }
}

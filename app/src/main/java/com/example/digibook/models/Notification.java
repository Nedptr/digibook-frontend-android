package com.example.digibook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("picurl")
    @Expose
    private String picurl;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("currentemail")
    @Expose
    private String currentemail;
    @SerializedName("notificationid")
    @Expose
    private String notificationid;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCurrentemail() {
        return currentemail;
    }

    public void setCurrentemail(String currentemail) {
        this.currentemail = currentemail;
    }

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}

package com.doviesfitness.ui.room_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class LocalStream implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @ColumnInfo(name = "user_id")
    private String userid;

    @ColumnInfo(name = "user_name")
    private String username;


    @ColumnInfo(name = "w_list")
    private String WList;



    public String getWList() {
        return WList;
    }

    public void setWList(String WList) {
        this.WList = WList;
    }

    /*
     * Getters and Setters
     * */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}

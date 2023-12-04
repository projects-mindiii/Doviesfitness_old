package com.doviesfitness.ui.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class LocalStreamVideoDataModal : Serializable {

    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0

    fun getUserid(): String? {
        return userid
    }

    fun setUserid(userid: String) {
        this.userid = userid
    }

    @ColumnInfo(name = "user_id")
    private var userid: String? = null

    @ColumnInfo(name = "user_name")
    private var username: String? = null


    @ColumnInfo(name = "w_list")
    private var WList: String? = null


    fun getWList(): String? {
        return WList
    }

    fun setWList(WList: String) {
        this.WList = WList
    }

    /*
     * Getters and Setters
     * */
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }


    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

}
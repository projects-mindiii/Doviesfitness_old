package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import com.google.gson.annotations.SerializedName;

public class HlsVideo{

    @SerializedName("vHlsMasterPlaylist")
    private String vHlsMasterPlaylist;

    @SerializedName("vHls720p")
    private String vHls720p;

    @SerializedName("vHls2K")
    private String vHls2K;

    @SerializedName("vHls1080p")
    private String vHls1080p;

    @SerializedName("vHls480p")
    private String vHls480p;

    @SerializedName("vHls360p")
    private String vHls360p;

    public void setVHlsMasterPlaylist(String vHlsMasterPlaylist){
        this.vHlsMasterPlaylist = vHlsMasterPlaylist;
    }

    public String getVHlsMasterPlaylist(){
        return vHlsMasterPlaylist;
    }

    public void setVHls720p(String vHls720p){
        this.vHls720p = vHls720p;
    }

    public String getVHls720p(){
        return vHls720p;
    }

    public void setVHls2K(String vHls2K){
        this.vHls2K = vHls2K;
    }

    public String getVHls2K(){
        return vHls2K;
    }

    public void setVHls1080p(String vHls1080p){
        this.vHls1080p = vHls1080p;
    }

    public String getVHls1080p(){
        return vHls1080p;
    }

    public void setVHls480p(String vHls480p){
        this.vHls480p = vHls480p;
    }

    public String getVHls480p(){
        return vHls480p;
    }

    public void setVHls360p(String vHls360p){
        this.vHls360p = vHls360p;
    }

    public String getVHls360p(){
        return vHls360p;
    }
}
package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import com.google.gson.annotations.SerializedName;

public class Mp4Video{

    @SerializedName("vMpeg360p")
    private String vMpeg360p;

    @SerializedName("vMpeg480p")
    private String vMpeg480p;

    @SerializedName("vMpeg720p")
    private String vMpeg720p;

    @SerializedName("vMpeg2K")
    private String vMpeg2K;

    @SerializedName("vMpeg1080p")
    private String vMpeg1080p;

    public void setVMpeg360p(String vMpeg360p){
        this.vMpeg360p = vMpeg360p;
    }

    public String getVMpeg360p(){
        return vMpeg360p;
    }

    public void setVMpeg480p(String vMpeg480p){
        this.vMpeg480p = vMpeg480p;
    }

    public String getVMpeg480p(){
        return vMpeg480p;
    }

    public void setVMpeg720p(String vMpeg720p){
        this.vMpeg720p = vMpeg720p;
    }

    public String getVMpeg720p(){
        return vMpeg720p;
    }

    public void setVMpeg2K(String vMpeg2K){
        this.vMpeg2K = vMpeg2K;
    }

    public String getVMpeg2K(){
        return vMpeg2K;
    }

    public void setVMpeg1080p(String vMpeg1080p){
        this.vMpeg1080p = vMpeg1080p;
    }

    public String getVMpeg1080p(){
        return vMpeg1080p;
    }
}
package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import com.google.gson.annotations.SerializedName;

public class TvHlsVideo{

    @SerializedName("settings")
    private Settings settings;

    public void setSettings(Settings settings){
        this.settings = settings;
    }

    public Settings getSettings(){
        return settings;
    }
}
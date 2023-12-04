package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

data class StreamTagModel(
    val settings: Settings
) {
    data class Settings(
        val `data`: List<Data>,
        val message: String,
        val success: String
    ) {
        data class Data(
            val eStatus: String,
            val iStreamTagId: String,
            val vStreamTagName: String,
            var isSelected: Boolean =false
        )
    }
}
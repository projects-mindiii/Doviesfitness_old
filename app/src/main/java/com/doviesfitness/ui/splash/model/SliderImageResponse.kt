package com.doviesfitness.ui.splash.model

data class SliderImageResponse(
    val settings: Settings
)

data class Settings(
    val `data`: List<Data>,
    val message: String,
    val success: String
)

data class Data(
    val ipadImage: String,
    val mobileImage: String,
    val sliderImageId: String,
    val staticImage:Int


    )
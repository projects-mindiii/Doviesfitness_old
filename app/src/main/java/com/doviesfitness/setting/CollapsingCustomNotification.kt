package com.doviesfitness.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.doviesfitness.R

class CollapsingCustomNotification :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collapsing_custom_notification)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
       // app_bar_layout.setExpanded(false)

/*
        app_bar_layout.addOnOffsetChangedListener(object :AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {
                ////
                val totalScrollRange = appBarLayout!!.getTotalScrollRange()
                Log.d("Collapsing toolbar","...scroll value..."+i)

                if (totalScrollRange != 0) {
                    if (i < -200) {
                        imageView_teamLogoHome.setScaleY(1f)

                        //  moveView(0.559544f, 0.559544f, 95f, 5f)
                    } else if (i < -190) {
                        imageView_teamLogoHome.setScaleY(1f)

                        // moveView(0.559544f, 0.559544f, 92f, 4.9f)
                    } else if (i < -185) {
                        imageView_teamLogoHome.setScaleY(1f)

                        // moveView(0.565644f, 0.565644f, 90f, 4.9f)
                    } else if (i < -180) {
                        imageView_teamLogoHome.setScaleY(1f)

                        // moveView(0.579544f, 0.579544f, 88f, 4.9f)
                    } else if (i < -175) {
                        imageView_teamLogoHome.setScaleY(1f)

                        // moveView(0.580544f, 0.580544f, 86f, 4.8f)
                    } else if (i < -170) {
                        imageView_teamLogoHome.setScaleY(1f)

                        //   moveView(0.595044f, 0.595044f, 85f, 4.8f)
                    } else if (i < -165) {
                        imageView_teamLogoHome.setScaleY(1f)

                        //  moveView(0.609500f, 0.609500f, 84f, 4.8f)
                    } else if (i < -160) {
                        imageView_teamLogoHome.setScaleY(1f)

                        //  moveView(0.610544f, 0.610544f, 82f, 4.7f)
                    } else if (i < -160) {
                        imageView_teamLogoHome.setScaleY(1f)

                        // moveView(0.629544f, 0.629544f, 80f, 4.7f)
                    } else if (i < -155) {
                        imageView_teamLogoHome.setScaleY(1.01f)

                        //moveView(0.639544f, 0.639544f, 78f, 4.7f)
                    } else if (i < -150) {
                        imageView_teamLogoHome.setScaleY(1.02f)

                        // moveView(0.649544f, 0.649544f, 76f, 4.6f)
                    } else if (i < -145) {
                        imageView_teamLogoHome.setScaleY(1.03f)

                        // moveView(0.659544f, 0.659544f, 74f, 4.6f)
                    } else if (i < -140) {
                        imageView_teamLogoHome.setScaleY(1.04f)

                        // moveView(0.669544f, 0.669544f, 70f, 4.6f)
                    } else if (i < -135) {
                        imageView_teamLogoHome.setScaleY(1.05f)

                        //moveView(0.674544f, 0.674544f, 69f, 4.5f)
                    } else if (i < -130) {
                        imageView_teamLogoHome.setScaleY(1.06f)

                        //moveView(0.684544f, 0.684544f, 68f, 4.5f)
                    } else if (i < -125) {
                        imageView_teamLogoHome.setScaleY(1.07f)

                        //  moveView(0.699544f, 0.699544f, 68f, 4.5f)
                    } else if (i < -120) {
                        imageView_teamLogoHome.setScaleY(1.08f)

                        //  moveView(0.704544f, 0.704544f, 67f, 4.4f)
                    } else if (i < -115) {
                        imageView_teamLogoHome.setScaleY(1.09f)

                        // moveView(0.724544f, 0.724544f, 65f, 4.4f)
                    } else if (i < -110) {
                        imageView_teamLogoHome.setScaleY(1.10f)

                        // moveView(0.739544f, 0.739544f, 64f, 4.3f)
                    } else if (i < -105) {
                        imageView_teamLogoHome.setScaleY(1.15f)

                        //  moveView(0.749544f, 0.749544f, 63f, 4.3f)
                    } else if (i < -100) {
                        imageView_teamLogoHome.setScaleY(1.18f)

                        //  moveView(0.754544f, 0.754544f, 62f, 4.2f)
                    } else if (i < -95) {
                        imageView_teamLogoHome.setScaleY(1.20f)

                        // moveView(0.764544f, 0.764544f, 60f, 4.1f)
                    } else if (i < -90) {
                        imageView_teamLogoHome.setScaleY(1.24f)

                        // moveView(1f, 0.774544f, 58f, 4.0f)
                    } else if (i < -85) {
                        imageView_teamLogoHome.setScaleY(1.26f)

                        //  moveView(1f, 0.774544f, 55f, 3.8f)
                    } else if (i < -80) {
                        imageView_teamLogoHome.setScaleY(1.29f)

                        //  moveView(1f, 0.784544f, 52f, 3.6f)
                    } else if (i < -75) {
                        imageView_teamLogoHome.setScaleY(1.31f)

                        //moveView(1f, 0.804544f, 50f, 3.4f)
                    } else if (i < -70) {
                        imageView_teamLogoHome.setScaleY(1.33f)

                        //   moveView(1f, 0.824544f, 48f, 3.2f)
                    } else if (i < -65) {
                        imageView_teamLogoHome.setScaleY(1.36f)

                        //  moveView(1f, 0.839544f, 45f, 3.1f)
                    } else if (i < -60) {
                        imageView_teamLogoHome.setScaleY(1.38f)

                        // moveView(1f, 0.84544f, 42f, 3.0f)
                    } else if (i < -55) {
                        imageView_teamLogoHome.setScaleY(1.40f)

                        //   moveView(1f, 0.85544f, 40f, 2.8f)
                    } else if (i < -50) {
                        imageView_teamLogoHome.setScaleY(1.43f)

                        // moveView(1f, 0.86544f, 38f, 2.8f)
                    } else if (i < -45) {
                        imageView_teamLogoHome.setScaleY(1.46f)

                        //   moveView(1f, 0.87544f, 35f, 2.6f)
                    } else if (i < -40) {
                        imageView_teamLogoHome.setScaleY(1.48f)

                        // moveView(1f, 0.884544f, 32f, 2.4f)
                    } else if (i < -35) {
                        imageView_teamLogoHome.setScaleY(1.50f)

                        //moveView(1f, 0.894544f, 30f, 2.2f)
                    } else if (i < -30) {
                        imageView_teamLogoHome.setScaleY(1.51f)

                        // moveView(1f, 0.904544f, 28f, 2.0f)
                    } else if (i < -25) {
                        imageView_teamLogoHome.setScaleY(1.53f)

                        //  moveView(1f, 0.914544f, 25f, 1.8f)
                    } else if (i < -20) {
                        imageView_teamLogoHome.setScaleY(1.54f)

                        //  moveView(1f, 0.924544f, 22f, 1.4f)
                    } else if (i < -15) {
                        imageView_teamLogoHome.setScaleY(1.54f)

                        //  moveView(1f, 0.934544f, 20f, 1f)
                    } else if (i < -10) {
                        imageView_teamLogoHome.setScaleY(1.55f)

                     //   moveView(1f, 0.9491454f, 15f, 0.8f)
                    } else if (i < -5) {
                        imageView_teamLogoHome.setScaleY(1.56f)

                        //  moveView(1f, 0.954544f, 10f, 0.5f)
                    } else {
                        imageView_teamLogoHome.setScaleY(1.56f)

                        // moveView(1f, 1f, 5f, 0f)
                    }

                }



                ////
            }
        })
*/



    }



/*
    private fun moveView(scaleX: Float, scaleY: Float, transX: Float, transY: Float) {
        imageView_teamLogoHome.setScaleX(1f)
        imageView_teamLogoHome.setScaleY(scaleY)
        imageView_teamLogoHome.setTranslationX(1f)
        imageView_teamLogoHome.setTranslationY(1f)

       // binding.score.setTranslationY(-transY)
    }
*/



}
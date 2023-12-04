package com.doviesfitness.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity


class SplashScreenActivity : BaseActivity() {

   private var mRunnable: Runnable? = null
   private val mHandlers = Handler()
    val delay: Long = 3000
    lateinit var binding: com.doviesfitness.databinding.ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && getIntent().getAction().equals(Intent.ACTION_MAIN)
        ) {

            finish()
            return
        }
        else if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0){
            finish()
            return
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        mRunnable = Runnable {
            if (getDataManager().isLoggedIn()) {
                intent = Intent(this@SplashScreenActivity, HomeTabActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this@SplashScreenActivity, IntroSliderActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        mHandlers.postDelayed(mRunnable!!, delay)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mRunnable?.let { mHandlers.removeCallbacks(it) }
    }

}

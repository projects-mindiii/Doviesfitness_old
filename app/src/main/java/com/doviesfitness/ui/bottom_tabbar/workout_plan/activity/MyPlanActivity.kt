package com.doviesfitness.ui.bottom_tabbar.workout_plan.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import kotlinx.android.synthetic.main.activity_my_plan.*

class MyPlanActivity : BaseActivity(), View.OnClickListener{

    private var fromWhichScreen: String = ""
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_plan)
        intItView()
    }

    private fun intItView() {
        setOnClick(iv_back, txt_create_plan, txt_inbuild_plan)
        if(intent!!.hasExtra("fromProfile")){
            if(intent!!.getStringExtra("fromProfile") != null){
                fromWhichScreen = intent!!.getStringExtra("fromProfile")!!
            }
        }
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.txt_create_plan ->{

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0") &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                    startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))
                } else {
                    val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
                    startActivity(intent)
                }
            }

            R.id.txt_inbuild_plan ->{

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                val homeTabintent = Intent(this, HomeTabActivity::class.java)
                homeTabintent.putExtra("fromMyPlanActvity", "fromMyPlanActvity")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeTabintent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(!fromWhichScreen.isEmpty() && fromWhichScreen.equals("fromProfile")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorWhite))
            }
        }
    }
}

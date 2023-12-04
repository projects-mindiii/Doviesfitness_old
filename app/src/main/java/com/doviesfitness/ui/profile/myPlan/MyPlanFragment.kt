package com.doviesfitness.ui.profile.myPlan

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentMyPlanBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty


class MyPlanFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentMyPlanBinding
    private var fromWhichScreen: String = ""
    private var mLastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = activity!!.window.decorView
        view.systemUiVisibility = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorBlack
                )
            )
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        //hideNavigationBar()
        setOnClick(
            binding.ivBack,
            binding.txtCreatePlan,
            binding.txtInbuildPlan,
            binding.containerId
        )
        /* if(intent!!.hasExtra("fromProfile")){
             if(intent!!.getStringExtra("fromProfile") != null){
                 fromWhichScreen = intent!!.getStringExtra("fromProfile")
             }
         }*/
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_back -> {
                activity!!.onBackPressed()
            }

            R.id.container_id -> {
                hideNavigationBar()
                view.systemUiVisibility =
                    view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            R.id.txt_create_plan -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                //CHECK LGANA HE YHA NORMAL  USER CREAT NA KRE

                var rhjeshr = Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)
                var admin = Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)

                Log.v("rhjeshr",""+rhjeshr +"----------------"+admin)


                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0") &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                    startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))
                } else {
                    val intent = Intent(mContext, CreateWorkOutPlanActivty::class.java)
                    startActivity(intent)
                }
            }

            R.id.txt_inbuild_plan -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                val homeTabintent = Intent(mContext, HomeTabActivity::class.java)
                homeTabintent.putExtra("fromMyPlanActvity", "fromMyPlanActvity")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeTabintent)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorBlack
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!fromWhichScreen.isEmpty() && fromWhichScreen.equals("fromProfile")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity!!.window.setStatusBarColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.colorBlack
                    )
                )
            }
        }
    }


    /* // Call Back method  to get the Message form other Activity
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         // check if the request code is same as what is passed  here it is 2
         if (requestCode == 2) {
             val message = data!!.getStringExtra("MESSAGE")
             //if(message.equals("back")){
                 //activity!!.onBackPressed()
               *//*  val intent = Intent();
                intent.putExtra("MESSAGE","back");
                activity!!.setResult(1,intent);
                activity!!.finish()*//*
        }
    }*/
}

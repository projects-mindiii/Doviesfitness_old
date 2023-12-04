package com.doviesfitness.ui.bottom_tabbar


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.setting.ContactUsActivity
import com.doviesfitness.setting.InviteFriendsActivity
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.LogoutDialog
import kotlinx.android.synthetic.main.activity_home_tab.*
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import android.content.ActivityNotFoundException
import android.net.Uri
import com.doviesfitness.ui.setting.SettingActivity
import com.doviesfitness.ui.setting.faq.FAQActivity


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NavigationDrawerFragment : BaseFragment(), View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var activity: Activity
    private var homeTabActivity: HomeTabActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeTabActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_logout.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
        main_layout.setOnClickListener(this)
        upgrade_layout.setOnClickListener(this)
        invite_layout.setOnClickListener(this)
        contact_us.setOnClickListener(this)
        rate_us_layout.setOnClickListener(this)
        facebook_icon.setOnClickListener(this)
        insta_icon.setOnClickListener(this)
        twitter_icon.setOnClickListener(this)
        dovies_icon.setOnClickListener(this)
        faq_layout.setOnClickListener(this)
        setting_layout.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavigationDrawerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }

    fun setData(){
        tv_name.text = getDataManager().getUserInfo().customer_full_name
        tv_user_name.text = "@" + getDataManager().getUserInfo().customer_user_name
        Glide.with(iv_profile.context).load(getDataManager().getUserInfo().customer_profile_image).placeholder(R.drawable.user_placeholder).into(iv_profile)

        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true))
        {
            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0")){

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_SUBSCRIPTION_TITLE).equals("UPGRADE TO PREMIUM")){
                    subscription_title.text="Upgrade To Premium"
                    Glide.with(iv_profile.context).load(R.drawable.lock_ico).into(subscription_lock_icon)
                    upgrade_layout.background=resources.getDrawable(R.drawable.green_btn_bg)
                }
                else {
                    subscription_title.text="UPDATE TO PREMIUM"
                    Glide.with(iv_profile.context).load(R.drawable.lock_ico).into(subscription_lock_icon)
                    upgrade_layout.background=resources.getDrawable(R.drawable.green_btn_bg)
                }
            }
            else{
                subscription_title.text=""+getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_SUBSCRIPTION_TITLE)
                upgrade_layout.background=resources.getDrawable(R.drawable.brown_btn_bg)
                Glide.with(iv_profile.context).load(R.drawable.ico_upgrade).into(subscription_lock_icon)
            }
            subscription_lock_icon.visibility=View.VISIBLE
        }
        else{
            subscription_title.text=resources.getString(R.string.premium_account)
            upgrade_layout.background=resources.getDrawable(R.drawable.green_btn_bg)
            subscription_lock_icon.visibility=View.GONE


        }

    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_logout -> {
                fragmentManager?.let {
                    LogoutDialog.newInstance("", "").show(it)
                }
                //getDataManager().logout(activity)
            }
            R.id.upgrade_layout -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    ) &&
                    getDataManager().getUserStringData(com.doviesfitness.data.local.AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "No",
                        true
                    )
                ) {
                    startActivity(Intent(getBaseActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                    )
                }
            }
            R.id.iv_profile -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                activity.my_profile_ll.callOnClick()
            }
            R.id.rate_us_layout -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                val uri = Uri.parse("market://details?id=" + context!!.getPackageName())
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.getPackageName())
                        )
                    )
                }

            }
            R.id.contact_us -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                startActivity(Intent(getBaseActivity(), ContactUsActivity::class.java))


            }
            R.id.invite_layout -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                startActivity(Intent(getBaseActivity(), InviteFriendsActivity::class.java))


            }
            R.id.main_layout -> {

            }
            R.id.setting_layout -> {

                activity.drawer_layout.closeDrawer(Gravity.START)
                val intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.faq_layout -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                var   intent = Intent(context, FAQActivity::class.java)
                intent.putExtra("from", "privacy policy")
                startActivity(intent)
            }
            R.id.facebook_icon -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                socialView("https://www.facebook.com/doviesfitness/")
            }
            R.id.insta_icon -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                socialView("https://www.instagram.com/Doviesfitness/")
            }
            R.id.twitter_icon -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                socialView("https://twitter.com/Doviesfitness")
            }
            R.id.dovies_icon -> {
                activity.drawer_layout.closeDrawer(Gravity.START)
                socialView("https://dev.doviesfitness.com/")
            }

        }
    }
    private  fun socialView(url:String){
        val uri = Uri.parse(url)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

}

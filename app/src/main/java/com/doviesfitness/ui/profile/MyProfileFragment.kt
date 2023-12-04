package com.doviesfitness.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentMyProfileBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity.Companion.fromNotificationClick
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.MyDietPlanFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.CreateWorkoutActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.MyWorkoutLogFragment
import com.doviesfitness.ui.profile.editProfile.EditProfileFragment
import com.doviesfitness.ui.profile.favourite.MyFavouriteFragment
import com.doviesfitness.ui.profile.inbox.activity.InboxActivity
import com.doviesfitness.ui.profile.myPlan.MyPlanFragment
import com.doviesfitness.ui.profile.myWorkOut.MyWorkoutFragment
import com.doviesfitness.ui.profile.profile_my_plan.MyPlanFromProfileFragment
import com.doviesfitness.ui.setting.SettingFragmanet
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.fragment_my_profile.my_workout_plan_ll

class MyProfileFragment : BaseFragment(), View.OnClickListener,
    ItemListDialogFragment.DialogEventListener {

    private lateinit var myworkOutLogFragment: MyWorkoutLogFragment
    private lateinit var myworkOutFragment: MyWorkoutFragment
    private lateinit var myFavouriteFragment: MyFavouriteFragment
    private lateinit var myPlanFromProfileFragment: MyPlanFromProfileFragment
    private lateinit var myDietPlanFragment: MyDietPlanFragment
    private lateinit var settingFragmanet: SettingFragmanet
    lateinit var binding: FragmentMyProfileBinding
    private var width: Int = 0
    private lateinit var homeTabActivity: HomeTabActivity
    lateinit var myProfileFragment: MyProfileFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.splash_screen_color
                )
            )
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        allFragmentsInitilize()
        myProfileFragment = this
        arguments?.let {
            val module_id = arguments!!.getString("module_id", "")
            if (!module_id.isEmpty()) {

                val intent = Intent(mContext, InboxActivity::class.java)
                startActivity(intent)
            }
        }

        val params = binding.topView.getLayoutParams() as RelativeLayout.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(mContext as Activity)
        params.height = dpWidth + 80
        binding.topView.setLayoutParams(params)

        setOnClick(
            binding.ivSettingProfile,
            binding.ivContextMenu,
            binding.dietPlanLl,
            binding.myWorkoutLl,
            binding.myWorkoutPlanLl,
            binding.ivAddBtn,
            binding.favouriteLl,
            binding.inboxLnr,
            binding.workoutLogLnr,
            binding.container
        )

        getProfileDataFromSession()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* if(arguments!!.getString("whichScreen") != null){
             val openMyPlan = arguments!!.getString("whichScreen")
             if(openMyPlan!!.equals("WhenPlanCreate")){
                 my_workout_plan_ll.callOnClick()
             }
         }*/
    }

    // to get data  when we come from notification
    fun newInstance(module_id: String): MyProfileFragment {
        val myFragment = MyProfileFragment()
        val args = Bundle()
        args.putString("module_id", module_id)
        myFragment.setArguments(args)

        return myFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeTabActivity = activity as HomeTabActivity
        if (homeTabActivity != null) {
            if (homeTabActivity.comeFromCreatPlan == true) {
                // for one my plan fragment india back ground
                homeTabActivity.comeFromCreatPlan == false
                my_workout_plan_ll.callOnClick()
            }
        }
    }

    private fun getProfileDataFromSession() {


        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            val userInfoBean = getDataManager().getUserInfo()
         //   binding.txtUserName.text = userInfoBean.customer_full_name
            var name= CommanUtils.capitaliseName(userInfoBean.customer_full_name)

            binding.txtUserName.text = ""+name
            binding.tvNickName.text = "@" + userInfoBean.customer_user_name

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT).equals("0")) {
                binding.txtNotificationCount.visibility = View.GONE
            } else {
                binding.txtNotificationCount.visibility = View.VISIBLE
            }

            if (CommanUtils.isValidContextForGlide(mContext)) {
                Glide.with(binding.ivUserProfile.context).load(userInfoBean.customer_profile_image)
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.ivUserProfile)
            }

        } else {
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }

    public fun updateCount(){
        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT).equals("0")) {
            binding.txtNotificationCount.visibility = View.GONE
        } else {
            binding.txtNotificationCount.visibility = View.VISIBLE
        }

    }


    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        updateCount()
    }


    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_setting_profile -> {
                editProfileDialog()
            }

            R.id.container -> {
                hideNavigationBar()
            }

            R.id.favourite_ll -> {
                CommanUtils.lastClick()
               // homeTabActivity.addFragment(myFavouriteFragment, R.id.container_id, true)
              //  homeTabActivity.addFragment2(myFavouriteFragment, R.id.container_id, false)

               if ( homeTabActivity.addFragment2(myFavouriteFragment, R.id.container_id, true)){
                   myFavouriteFragment = MyFavouriteFragment()
                   homeTabActivity.addFragment(myFavouriteFragment, R.id.container_id, true)
               }


            }

            R.id.my_workout_ll -> {
                CommanUtils.lastClick()
                val args = Bundle()
                myworkOutFragment.setArguments(args)
               // homeTabActivity.addFragment(myworkOutFragment, R.id.container_id, true)

                if ( homeTabActivity.addFragment2(myworkOutFragment, R.id.container_id, true)){
                    myworkOutFragment = MyWorkoutFragment()
                    val args = Bundle()
                    myworkOutFragment.setArguments(args)
                    homeTabActivity.addFragment(myworkOutFragment, R.id.container_id, true)
                }


            }

            R.id.workout_log_lnr -> {
                CommanUtils.lastClick()
               // val args = Bundle()
               // myworkOutLogFragment.setArguments(args)
              //  homeTabActivity.addFragment(myworkOutLogFragment, R.id.container_id, true)

                if ( homeTabActivity.addFragment2(myworkOutLogFragment, R.id.container_id, true)){
                    myworkOutLogFragment = MyWorkoutLogFragment()
                    val args = Bundle()
                    myworkOutFragment.setArguments(args)
                    homeTabActivity.addFragment(myworkOutLogFragment, R.id.container_id, true)
                }

            }

            R.id.inbox_lnr -> {
                CommanUtils.lastClick()
                fromNotificationClick="2"
                val intent = Intent(mContext, InboxActivity::class.java)
                startActivity(intent)
            }

            R.id.iv_context_menu -> {
                CommanUtils.lastClick()
                homeTabActivity.addFragment1(settingFragmanet, R.id.container_id, true)
               // homeTabActivity.showSettingFragment(settingFragmanet)
              //  showFragment(settingFragmanet)
            }

            R.id.iv_add_btn -> {
                showCreatePlanAndWorkOutDialog()
            }

            R.id.my_workout_plan_ll -> {

                CommanUtils.lastClick()
                // send "" in new instance becose we also go here from WorkOutPlanDetailActivity where we redirect on back icon on profile tab so be care full
            //    getBaseActivity()?.addFragment(myPlanFromProfileFragment.newInstance(""), R.id.container_id, true)

                if ( homeTabActivity.addFragment2(myPlanFromProfileFragment.newInstance(""), R.id.container_id, true)){
                    myPlanFromProfileFragment = MyPlanFromProfileFragment()
                    homeTabActivity.addFragment(myPlanFromProfileFragment.newInstance(""), R.id.container_id, true)
                }


            }

            R.id.diet_plan_ll -> {
                CommanUtils.lastClick()
             //   val args = Bundle()
              //  myDietPlanFragment.setArguments(args)
               // homeTabActivity.addFragment(myDietPlanFragment, R.id.container_id, true)
                //////
                if ( homeTabActivity.addFragment2(myDietPlanFragment, R.id.container_id, true)){
                    myDietPlanFragment = MyDietPlanFragment()
                    val args = Bundle()
                    myDietPlanFragment.setArguments(args)
                    homeTabActivity.addFragment(myDietPlanFragment, R.id.container_id, true)
                }



            }
        }
    }

    private fun allFragmentsInitilize() {
        settingFragmanet = SettingFragmanet()
        myDietPlanFragment = MyDietPlanFragment()
        myPlanFromProfileFragment = MyPlanFromProfileFragment()
        myFavouriteFragment = MyFavouriteFragment()
        myworkOutFragment = MyWorkoutFragment()
        myworkOutLogFragment = MyWorkoutLogFragment()
    }

    private fun showCreatePlanAndWorkOutDialog() {
        val menus = mutableListOf<DialogMenu>()
        menus.add(DialogMenu("Create a Plan", R.drawable.ico_pr_plan))
        menus.add(DialogMenu("Create a Workout", R.drawable.ico_pr_workout))
        val dialogFragment = ItemListDialogFragment.newInstance("forSelectPlan")
        dialogFragment.addMenu(menus)
        dialogFragment.addDialogEventListener(this)
        dialogFragment.show(fragmentManager!!, "forSelectPlan")
    }

    private fun editProfileDialog() {
        val menus = mutableListOf<DialogMenu>()
        menus.add(DialogMenu("Edit profile", R.drawable.ic_edit_workout_white))
        val dialogFragment = ItemListDialogFragment.newInstance("EditProfile")
        dialogFragment.addMenu(menus)
        dialogFragment.addDialogEventListener(this)
        dialogFragment.show(fragmentManager!!, "EditProfile")
    }

    public fun updateProfileData(customerProfileImage: String) {
        Log.v("myworkout", "myworkout")
        Glide.with(binding.ivUserProfile.context).load(customerProfileImage)
            .placeholder(R.drawable.user_placeholder)
            .into(binding.ivUserProfile)
    }

    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {
        if ("EditProfile".equals(mCategoryTag)) {
            if (mMenuTag.equals("Edit profile")) {
                val editProfile = EditProfileFragment()
                val args = Bundle()
                editProfile.setArguments(args)
                getBaseActivity()?.addFragment(
                    editProfile.newInstance(""),
                    R.id.profile_container_id,
                    true
                )
            }
        } else if ("forSelectPlan".equals(mCategoryTag)) {
            if (mMenuTag.equals("Create a Plan")) {
                val myPlanFragment = MyPlanFragment()
                val args = Bundle()
                myPlanFragment.setArguments(args)
                getBaseActivity()?.addFragment(myPlanFragment, R.id.container_id, true)

            } else if (mMenuTag.equals("Create a Workout")) {
                startActivity(
                    Intent(getActivity(), CreateWorkoutActivity::class.java).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "")
                )
            }
        }
    }

    override fun onDialogDismiss() {
    }
}
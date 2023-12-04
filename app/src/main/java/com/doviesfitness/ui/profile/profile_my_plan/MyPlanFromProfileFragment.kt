package com.doviesfitness.ui.profile.profile_my_plan

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter.AddToWorkOutPLanAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.AddToWorkOutPLanModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import com.doviesfitness.databinding.FragmentMyPlanFromProfileBinding
import com.doviesfitness.ui.bottom_tabbar.workout_plan.activity.MyPlanActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.profile.myPlan.MyPlanFragment
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.utils.CommanUtils
import eightbitlab.com.blurview.RenderScriptBlur


class MyPlanFromProfileFragment : BaseFragment(), View.OnClickListener,
    AddToWorkOutPLanAdapter.AddToWorkOutPlanistener,
    ItemListDialogFragment.DialogEventListener {

    private var fromWhichScreen: String = ""
    lateinit var binding: FragmentMyPlanFromProfileBinding
    lateinit var adapter: AddToWorkOutPLanAdapter
    private var addToWorkPlnList = ArrayList<AddToWorkOutPLanModal.Data>()
    private var page: Int = 1
    private var nextPage: Int = 0
    private var isAdmin = "No"

    private var mData: AddToWorkOutPLanModal.Data? = null
    private var mPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  activity!!.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        view.setZ(1.0f);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_from_profile, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {

        setOnClick(binding.ivBack, binding.ivAdd, binding.btnAddPlan, binding.containerId)

        arguments?.let {
            fromWhichScreen = arguments!!.getString("fromWhichScreen")!!
        }

        val windowBackground =  activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        adapter = AddToWorkOutPLanAdapter(mContext, addToWorkPlnList, this)
//        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        val layoutManager1 = GridLayoutManager(context, 2)
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
//        binding.addToMyWopRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))

        binding.addToMyWopRv.layoutManager = layoutManager1
        binding.addToMyWopRv.adapter = adapter
        binding.rlLoader.visibility=View.VISIBLE
        getAddWorkOutPlanApi(page)

        //*Swipe to refresh*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(false,  resources.getDimension(R.dimen._80sdp).toInt(), resources.getDimension(R.dimen._120sdp).toInt())
        }
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 1
            addToWorkPlnList.clear()
            getAddWorkOutPlanApi(page)
        })

      /*  if (addToWorkPlnList.size > 0) {
            binding.ivAdd.visibility = View.VISIBLE
        } else {
            binding.ivAdd.visibility = View.GONE
        }*/

        binding.myWopMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
//                        page = page + 1
//                        if (page != 0 && nextPage > 0) {
//                            adapter.showLoading(true)
//                            adapter.notifyDataSetChanged()
//                            getAddWorkOutPlanApi(page)
//                        }
                    }
                }
            }
        })
    }

    // to get data  when we come from notification
    fun newInstance(fromWhichScreen: String): MyPlanFromProfileFragment {
        val myFragment = MyPlanFromProfileFragment()
        val args = Bundle()
        args.putString("fromWhichScreen", fromWhichScreen)
        myFragment.setArguments(args)

        return myFragment
    }

    private fun getAddWorkOutPlanApi(page_index: Int) {
        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            val param = HashMap<String, String>()
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.device_type, StringConstant.Android)
            //param.put(StringConstant.page_index, "" + page_index)
            param.put(StringConstant.device_token, "")

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getCustomPlan(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rlLoader.visibility=View.GONE
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        nextPage = page_index+10

                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            binding.noPlanFound.visibility = View.GONE
                            binding.swipeRefresh.setRefreshing(false)
                            //addToWorkPlnList.clear()
                            val addToWorkOutPLanModal =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    AddToWorkOutPLanModal::class.java
                                )
                            addToWorkPlnList.addAll(addToWorkOutPLanModal!!.data);
                            hideFooterLoiader()

                            if (addToWorkPlnList.size > 0) {
                                binding.ivAdd.visibility = View.VISIBLE
                            } else {
                                binding.ivAdd.visibility = View.GONE
                            }
                        }else{
                            if (addToWorkPlnList.size == 0) {
                                binding.ivAdd.visibility = View.GONE
                                binding.noPlanFound.visibility = View.VISIBLE
                            }
                        }

                        if (addToWorkPlnList.size == 0) {
                            binding.ivAdd.visibility = View.GONE
                            binding.noPlanFound.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.d("on Error-->",anError.toString())
                        binding.rlLoader.visibility=View.GONE
                        hideFooterLoiader()
                        Constant.errorHandle(anError, mContext as Activity)
                    }
                })

        }else{
            binding.rlLoader.visibility=View.GONE
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }

    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        val view =  activity!!.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        //getAddWorkOutPlanApi(page)
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_back -> {

                if("WorkOutPlanDetailActivity".equals(fromWhichScreen)){
                    val homeTabintent = Intent(getBaseActivity(), HomeTabActivity::class.java)
                    homeTabintent.putExtra("fromWhichScreen", "WhenWorkoutPlanDetailActivity")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(homeTabintent)
                }else{
                    mActivity!!.onBackPressed()
                }
            }

            R.id.container_id -> {
                hideNavigationBar()
                val view =  activity!!.window.decorView
                view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            R.id.iv_add -> {

                val editProfile = MyPlanFragment()
                val args = Bundle()
                editProfile.setArguments(args)
                getBaseActivity()?.addFragment(editProfile, R.id.container_id, true)
            }

            R.id.btn_add_Plan -> {
                val intent = Intent(mContext, MyPlanActivity::class.java)
                intent.putExtra("fromProfile", "fromProfile")
                startActivity(intent)
            }
        }
    }

    override fun getAddWorkoutPlanInfo(
        data: AddToWorkOutPLanModal.Data,
        position: Int,
        onClick: String
    ) {

        if (onClick.equals("setting")) {
            isAdmin =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
            if ("Yes".equals(isAdmin)) {

                if (position !== -1) {
                    mPosition = position
                    mData = data
                    val menus = mutableListOf<DialogMenu>()
                    menus.add(DialogMenu("Edit", R.drawable.ic_edit_workout))
                    menus.add(DialogMenu("Publish to app", R.drawable.ico_publish))

                    if ("1".equals(data.status)) {
                        menus.add(DialogMenu("Active", R.drawable.ico_active_ico))
                    } else {
                        menus.add(DialogMenu("Active", R.drawable.ico_inactive_ico))
                    }
                    menus.add(DialogMenu("Delete", R.drawable.ic_recycling_bin))

                    val dialogFragment = ItemListDialogFragment.newInstance("Admin")
                    dialogFragment.addMenu(menus)
                    dialogFragment.addDialogEventListener(this)
                    dialogFragment.show(fragmentManager!!, "Admin")
                }

                // WorkPlanDialogForFragment.newInstance(this, data, position).show(childFragmentManager)
            } else {

                if (position !== -1) {
                    mPosition = position
                    mData = data
                    val menus = mutableListOf<DialogMenu>()
                    menus.add(DialogMenu("Edit", R.drawable.ic_edit_workout_white))
                    menus.add(DialogMenu("Share", R.drawable.ic_share_white))

                    if ("1".equals(data.program_fav_status)) {
                        menus.add(DialogMenu("Unfavourite", R.drawable.ic_star_active_white))
                    } else {
                        menus.add(DialogMenu("Favourite", R.drawable.ic_starinactivie_white))
                    }

                    menus.add(DialogMenu("Delete", R.drawable.ic_recycling_bin_white))
                    val dialogFragment = ItemListDialogFragment.newInstance("NormalUser")
                    dialogFragment.addMenu(menus)
                    dialogFragment.addDialogEventListener(this)
                    dialogFragment.show(fragmentManager!!, "NormalUser")
                }

                //WorkPLanForNormalUserDialog.newInstance(this, data, position).show(childFragmentManager)
            }

        }
        else if (onClick.equals("container")) {

            val intent = Intent(mContext, ShowDietPlanDetailActivity::class.java)
            intent.putExtra("FromListToSeeDetail", data.program_id)
            startActivity(intent)
        }
    }

    /*.............................//Custom dialog for Delete....................................*/
    fun showDeleteDialog(activity: Activity, dataValue: AddToWorkOutPLanModal.Data, pos: Int) {
        val dialog = Dialog(activity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.delete_diet_plan_view)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deletePlanApi(dataValue, pos)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deletePlanApi(dataValue: AddToWorkOutPLanModal.Data, pos: Int) {
        /*Delete plan api*/
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.program_id, dataValue.program_id)
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deletePlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            addToWorkPlnList.removeAt(pos)
                            adapter.notifyDataSetChanged()

                            if(addToWorkPlnList.size == 0){
                                getAddWorkOutPlanApi(page)
                            }

                            val toast = Toast.makeText(mContext, "Done", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })

    }

    private fun addToFavApi(dataValue: AddToWorkOutPLanModal.Data, pos: Int) {
        /*Delete plan api*/
        val param = HashMap<String, String>()

        param.put(StringConstant.module_id, dataValue.program_id)
        param.put(StringConstant.module_name, "PROGRAM")
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().addToMyPlanFavApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            val addToWorkPlanModalData = addToWorkPlnList.get(pos)
                            if ("1".equals(addToWorkPlanModalData.program_fav_status)) {
                                addToWorkPlanModalData.program_fav_status = "0"
                            } else {
                                addToWorkPlanModalData.program_fav_status = "1"
                            }
                            addToWorkPlnList.set(pos, addToWorkPlanModalData)
                            adapter.notifyItemChanged(pos)

                        } else {
                            Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.splash_screen_color
                )
            )
        }
    }

    class MySpacesItemDecoration(space: Int) :
        androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.bottom = space * 5
            } else {
                outRect.bottom = 0
            }
        }
    }


    private fun publishWorkPlanApi(program_id: String) {
        val param = HashMap<String, String>()

        param.put(StringConstant.added_by_type, "Admin")
        param.put(StringConstant.program_id, program_id)
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().addToMyPlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {
                        getAddWorkOutPlanApi(page)
                        val toast = Toast.makeText(mContext, "Done", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        Constant.showCustomToast(mContext, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    private fun forActivePost(program_id: String, mPosition: Int) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.program_id, program_id)
        param.put(StringConstant.device_token, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().activePlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {

                        val addToFavWorkPlanModalData = addToWorkPlnList.get(mPosition)

                        if ("1".equals(addToFavWorkPlanModalData.status)) {
                            addToFavWorkPlanModalData.status = "0"
                        } else {
                            addToFavWorkPlanModalData.status = "1"
                        }
                        addToWorkPlnList.set(mPosition, addToFavWorkPlanModalData)
                        adapter.notifyItemChanged(mPosition)

                    } else {
                        Constant.showCustomToast(mContext, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {

        if ("Admin".equals(mCategoryTag)) {
            if (mMenuTag.equals("Edit")) {

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0") &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                    startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))
                } else {

                    val intent = Intent(mContext, CreateWorkOutPlanActivty::class.java)
                    intent.putExtra("FromListToSeeDetail", mData!!.program_id)
                    intent.putExtra("programName", mData!!.program_name)
                    startActivity(intent)
                }
            } else if (mMenuTag.equals("Publish to app")) {
                publishWorkPlanApi(mData!!.program_id)
            } else if (mMenuTag.equals("Active")) {
                forActivePost(mData!!.program_id, mPosition)
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(mContext as Activity, mData!!, mPosition)
            } else {
                Constant.showCustomToast(mContext, mMenuTag)
            }
        } else if ("NormalUser".equals(mCategoryTag)) {

            if (mMenuTag.equals("Edit")) {

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0") &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                    startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))
                } else {
                    val intent = Intent(mContext, CreateWorkOutPlanActivty::class.java)
                    intent.putExtra("FromListToSeeDetail", mData!!.program_id)
                    intent.putExtra("programName", mData!!.program_name)
                    startActivity(intent)
                }

            } else if (mMenuTag.equals("Share")) {
                mActivity!!.sharePost(mData!!.program_share_url)
            }
            else if (mMenuTag.equals("Favourite")) {
                addToFavApi(mData!!, mPosition!!)
            } else if ("Unfavourite".equals(mMenuTag)) {
                addToFavApi(mData!!, mPosition!!)
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(mContext as Activity, mData!!, mPosition!!)
            } else {
                Constant.showCustomToast(mContext, mMenuTag)
            }
        }
    }

    override fun onDialogDismiss() {

    }
}

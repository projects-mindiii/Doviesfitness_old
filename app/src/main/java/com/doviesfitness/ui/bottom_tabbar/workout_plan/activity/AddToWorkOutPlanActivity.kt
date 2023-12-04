package com.doviesfitness.ui.bottom_tabbar.workout_plan.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import androidx.core.widget.NestedScrollView
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter.AddToWorkOutPLanAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.AddToWorkOutPLanModal
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_add_to_work_out_plan.*
import org.json.JSONObject

class AddToWorkOutPlanActivity : BaseActivity(), View.OnClickListener,
    AddToWorkOutPLanAdapter.AddToWorkOutPlanistener,
    ItemListDialogFragment.DialogEventListener {

    private var isAdmin: String = "No"
    lateinit var adapter: AddToWorkOutPLanAdapter
    private var addToWorkPlnList = ArrayList<AddToWorkOutPLanModal.Data>()
    private var page: Int = 1
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    private var mData: AddToWorkOutPLanModal.Data? = null
    private var mPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_work_out_plan)
        inItView()
    }

    private fun inItView() {

        setOnClick(iv_back, iv_add, btn_add_Plan)
        if (intent.hasExtra("data")) {
            if (intent.getStringExtra("data") != null) {
                val showButton = intent.getStringExtra("data")!!
                if (!showButton.isEmpty()) {
                    val toast = Toast.makeText(this, "Data", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }

        adapter = AddToWorkOutPLanAdapter(this, addToWorkPlnList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(this)

        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        addToMy_wop_rv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))

        addToMy_wop_rv.layoutManager = layoutManager1
        addToMy_wop_rv.adapter = adapter
        getAddWorkOutPlanApi(page)

        //*Swipe to refresh*//*
        swipe_refresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            //binding.swipeRefresh.setRefreshing(false)
            page = 1
            addToWorkPlnList.clear()
            getAddWorkOutPlanApi(page)

        })

        my_wop_main.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage > 0) {
                            //adapter.showLoading(true)
                            getAddWorkOutPlanApi(page)
                        }
                    }
                }
            }
        })
    }

    private fun getAddWorkOutPlanApi(page_index: Int) {

        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.page_index, "" + page_index)
            param.put(StringConstant.device_token, "")

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getCustomPlan(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val next_page = json?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()
                        val count = json?.get(StringConstant.count)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            swipe_refresh.setRefreshing(false)
                            no_Plan_found.visibility = View.GONE
                            addToWorkPlnList.clear()
                            val addToWorkOutPLanModal =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    AddToWorkOutPLanModal::class.java
                                )
                            addToWorkPlnList.addAll(addToWorkOutPLanModal!!.data);
                            adapter.notifyDataSetChanged()
                        }

                        if (addToWorkPlnList.size == 0) {
                            swipe_refresh.setRefreshing(false)
                            no_Plan_found.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@AddToWorkOutPlanActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
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


            R.id.btn_add_Plan -> {
                val intent = Intent(this, MyPlanActivity::class.java)
                startActivity(intent)
            }

            R.id.iv_add -> {
                val intent = Intent(this, MyPlanActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun getAddWorkoutPlanInfo(
        data: AddToWorkOutPLanModal.Data,
        position: Int,
        onClick: String
    ) {
        lastClick()

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
                    dialogFragment.show(supportFragmentManager, "Admin")
                }

                // WorkPlanDialogForFragment.newInstance(this, data, position).show(childFragmentManager)
            } else {

                if (position !== -1) {
                    mPosition = position
                    mData = data
                    val menus = mutableListOf<DialogMenu>()
                    menus.add(DialogMenu("Edit", R.drawable.ic_edit_workout))
                    menus.add(DialogMenu("Share", R.drawable.ic_share))
                    if ("1".equals(data.program_fav_status)) {
                        menus.add(DialogMenu("Unfavourite", R.drawable.ic_starinactivie))
                    } else {
                        menus.add(DialogMenu("Favourite", R.drawable.ic_star_active))
                    }

                    menus.add(DialogMenu("Delete", R.drawable.ic_recycling_bin))
                    val dialogFragment = ItemListDialogFragment.newInstance("NormalUser")
                    dialogFragment.addMenu(menus)
                    dialogFragment.addDialogEventListener(this)
                    dialogFragment.show(supportFragmentManager, "NormalUser")
                }


                //WorkPLanForNormalUserDialog.newInstance(this, data, position).show(childFragmentManager)
            }

        } else if (onClick.equals("container")) {

            val intent = Intent(this, ShowDietPlanDetailActivity::class.java)
            intent.putExtra("FromListToSeeDetail", data.program_id)
            startActivity(intent)
        }

        /*if (onClick.equals("setting")) {
            isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
            if ("Yes".equals(isAdmin)) {
                WorkPLanDialog.newInstance(this, data, position).show(supportFragmentManager)
            } else {
                WorkPLanForNormalUserDialogFragments.newInstance(this, data, position).show(supportFragmentManager)
            }

        } else if (onClick.equals("container")) {
            val intent = Intent(this, ShowDietPlanDetailActivity::class.java)
            intent.putExtra("FromListToSeeDetail", data.program_id)
            startActivity(intent)
        }*/
    }


    private fun lastClick() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }

    private fun publishWorkPlanApi(program_id: String) {

        if (CommanUtils.isNetworkAvailable(this)!!) {
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

                        } else {
                            Constant.showCustomToast(this@AddToWorkOutPlanActivity, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@AddToWorkOutPlanActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun forActivePost(program_id: String, mPosition: Int) {
        if (CommanUtils.isNetworkAvailable(this)!!) {
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
                            Constant.showCustomToast(this@AddToWorkOutPlanActivity, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@AddToWorkOutPlanActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
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


        if (CommanUtils.isNetworkAvailable(this)!!) {

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
                                adapter.notifyItemRemoved(pos)
                                getAddWorkOutPlanApi(page)

                            } else {
                                Constant.showCustomToast(this@AddToWorkOutPlanActivity, "" + msg)
                            }
                        } catch (ex: Exception) {
                            Constant.showCustomToast(
                                this@AddToWorkOutPlanActivity,
                                getString(R.string.something_wrong)
                            )
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@AddToWorkOutPlanActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun addToFavApi(dataValue: AddToWorkOutPLanModal.Data, pos: Int) {

        if (CommanUtils.isNetworkAvailable(this)!!) {
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
                                Constant.showCustomToast(this@AddToWorkOutPlanActivity, "" + msg)
                            }
                        } catch (ex: Exception) {
                            Constant.showCustomToast(
                                this@AddToWorkOutPlanActivity,
                                getString(R.string.something_wrong)
                            )
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@AddToWorkOutPlanActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
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
                outRect.bottom = space
            } else {
                outRect.bottom = 0
            }
        }
    }

    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {
        if ("Admin".equals(mCategoryTag)) {
            if (mMenuTag.equals("Edit")) {
                val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
                intent.putExtra("FromListToSeeDetail", mData!!.program_id)
                intent.putExtra("programName", mData!!.program_name)
                startActivity(intent)
            } else if (mMenuTag.equals("Publish to app")) {
                publishWorkPlanApi(mData!!.program_id)
            } else if (mMenuTag.equals("Active")) {
                forActivePost(mData!!.program_id, mPosition)
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(this, mData!!, mPosition)
            } else {
                Constant.showCustomToast(this, mMenuTag)
            }
        } else if ("NormalUser".equals(mCategoryTag)) {

            if (mMenuTag.equals("Edit")) {
                val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
                intent.putExtra("FromListToSeeDetail", mData!!.program_id)
                intent.putExtra("programName", mData!!.program_name)
                startActivity(intent)
            } else if (mMenuTag.equals("Share")) {
                sharePost(mData!!.program_share_url)
            } else if (mMenuTag.equals("Favourite")) {
                addToFavApi(mData!!, mPosition!!)
            } else if ("Unfavourite".equals(mMenuTag)) {
                addToFavApi(mData!!, mPosition!!)
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(this, mData!!, mPosition!!)
            } else {
                Constant.showCustomToast(this, mMenuTag)
            }
        }
    }

    override fun onDialogDismiss() {


    }

}

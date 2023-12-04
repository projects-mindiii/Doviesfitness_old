package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityMyWorkoutBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.profile.myWorkOut.adapter.NewWorkoutListAdapter
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.dialog_delete.*
import org.json.JSONObject

class ActivityMyWorkoutList : BaseActivity(), View.OnClickListener, NewWorkoutListAdapter.MyWorkOutDeletAndRedirectListener,SaveEditWorkoutDialog.CommentCallBack {

    private var isAdmin: String = ""
    lateinit var binding: ActivityMyWorkoutBinding
    //lateinit var adapter: WorkoutListAdapter
    lateinit var adapter: NewWorkoutListAdapter
    var workOutList = ArrayList<WorkOutListModal.Data>()
    private var page: Int = 1
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    var idPos = -1
    private lateinit var newWorkoutModal: WorkOutListModal.Data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = DataBindingUtil.setContentView(this, R.layout.fragment_my_workout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_workout)
        initialization()
    }

    private fun initialization() {
        setOnClick(binding.ivBack, binding.ivAdd)
        binding.ivAdd.visibility=View.VISIBLE
        //ese krna he  9 /dec/2019

        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        adapter = NewWorkoutListAdapter(this@ActivityMyWorkoutList, workOutList, this,isAdmin)
        binding.myWorkoutRv.adapter = adapter
        getWorkOutApi(page, "0")

        /*Swipe to refresh*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._40sdp).toInt(),
                resources.getDimension(R.dimen._80sdp).toInt()
            )
        }

        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 1
            workOutList.clear()
            getWorkOutApi(page, "1")
        })

        binding.myWorkoutMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage ==1) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getWorkOutApi(page, "1")
                        }
                    }
                }
            }
        })
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

            R.id.iv_add -> {
                startActivity(
                    Intent(
                        getActivity(),
                        CreateWorkoutActivity::class.java
                    ).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "")
                )
            }
        }
    }

    private fun getWorkOutApi(pageCount: Int, fromRefresh: String) {

        if (fromRefresh.equals("1")) {
            binding.loader.visibility = View.GONE
        } else {
            binding.loader.visibility = View.VISIBLE
        }

        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.page_index, "" + pageCount)
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().getCustomerWorkoutAPi(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val status: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                val next_page = jsonObject?.getString(StringConstant.next_page)
                nextPage = next_page!!.toInt()

                if (status!!.equals("1")) {
                    binding.loader.visibility = View.GONE
                    binding.noPlanFound.visibility = View.GONE
                    binding.swipeRefresh.setRefreshing(false)
                    val workOutListModal = getDataManager().mGson?.fromJson(response.toString(), WorkOutListModal::class.java)

                    workOutList.addAll(workOutListModal!!.data);
                    hideFooterLoiader()
                }

                if (workOutList.size == 0 && workOutList.isEmpty()) {
                    binding.swipeRefresh.setRefreshing(false)
                    binding.noPlanFound.visibility = View.VISIBLE
                }

                /*else {
                    binding.swipeRefresh.setRefreshing(false)
                    binding.noPlanFound.visibility = View.VISIBLE
                    binding.loader.visibility = View.GONE
                }*/
            }

            override fun onError(anError: ANError) {
                hideFooterLoiader()
                Constant.errorHandle(anError, this@ActivityMyWorkoutList as Activity)
                binding.loader.visibility = View.GONE
            }
        })
    }


    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }


    override fun getWorkOutData(data: WorkOutListModal.Data, whichClick: String, pos: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        if (whichClick.equals("0")) {
            showDeleteDialog(this@ActivityMyWorkoutList as Activity, data, pos)
        } else {
            val intent = Intent(this@ActivityMyWorkoutList, WorkOutDetailActivity::class.java)
            intent.putExtra("PROGRAM_DETAIL", data.workout_id)
            intent.putExtra("isMyWorkout", "yes")
            intent.putExtra("fromDeepLinking", "")
            startActivity(intent)
        }
    }


    /*Delete comment api*/
    private fun deleteSavePost(data: WorkOutListModal.Data, pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.workout_id, data.workout_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            workOutList.removeAt(pos)
                            adapter.notifyDataSetChanged()

                            if(workOutList.size == 0){
                                getWorkOutApi(page, "1")
                            }
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(this@ActivityMyWorkoutList, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@ActivityMyWorkoutList as Activity)
                }
            })
    }

    /*.............................//Custom dialog for Delete....................................*/
    fun showDeleteDialog(activity: Activity, data: WorkOutListModal.Data, pos: Int) {
        val dialog = Dialog(activity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteSavePost(data, pos)
            dialog.dismiss()
        }

        dialog.show()
    }

/*
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
*/

    override fun publishWorkout(pos: Int, workoutModal : WorkOutListModal.Data) {
        idPos = pos
        newWorkoutModal = workoutModal
        SaveEditWorkoutDialog.newInstance("publish", this).show(supportFragmentManager)
    }

    override fun textOnClick1(type: String) {
        ///////publish
        val intent = Intent(getActivity(), ActivityGoodFor::class.java)
        intent.putExtra("from", "publish")
        intent.putExtra("workoutId", workOutList.get(idPos).workout_id)
        startActivity(intent)
    }

    override fun overwriteClick(type: String) {
        ///////////delete
        showDeleteDialog(idPos, newWorkoutModal)
    }
    fun showDeleteDialog(pos: Int, data: WorkOutListModal.Data) {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        dialog.tv_header.setText("Are you sure you want to delete this Workout?")
        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteWorkoutPlan(pos, data)
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun deleteWorkoutPlan(pos: Int, data: WorkOutListModal.Data) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("workout_id", data.workout_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiVersion, "1")
        header.put(StringConstant.apiKey, "HBD")

        getDataManager().deleteMyWorkOut(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            workOutList.removeAt(pos)
                            adapter.notifyDataSetChanged()
                            //workOutList.clear()
                            //getWorkOutApi(page, "0")
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(this@ActivityMyWorkoutList, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }

}
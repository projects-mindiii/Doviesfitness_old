package com.doviesfitness.ui.bottom_tabbar.workout_plan

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.databinding.FragmentWorkoutPlanBinding
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutPlanDetailActivity
import com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter.NewWorkOutPlanAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject

class WorkoutPlanFragment : BaseFragment(), View.OnClickListener,
    NewWorkOutPlanAdapter.WorkOutPlanistener {

    private var isAdmin: String = ""
    lateinit var binding: FragmentWorkoutPlanBinding
    private var page: Int = 1
    private var nextPage: Int = 0
    lateinit var adapter: NewWorkOutPlanAdapter
    private var mLastClickTime: Long = 0

    private var workoutPlanList = ArrayList<WorkOutPlanModal.Data.GetAllProgram>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_workout_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        hideNavigationBar()
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        arguments?.let {
            val module_id = arguments!!.getString("module_id", "")
            val fromDeepLinking = arguments!!.getString("fromDeepLinking", "")
            if (!module_id.isEmpty()) {
                val intent = Intent(mContext, WorkOutPlanDetailActivity::class.java)
                intent.putExtra("FromDirectNotification", module_id)
                intent.putExtra("fromDeepLinking", fromDeepLinking)
                mContext.startActivity(intent)
            }
        }

        binding.ivNavigationDiet.setOnClickListener(this)
        val layoutManager1 = GridLayoutManager(context, 2)
//        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        val spacingInPixels2 = resources.getDimensionPixelSize(R.dimen._6sdp)
//        binding.workOutPlanRv.addItemDecoration(MySpacesItemDecoration(2,spacingInPixels1,spacingInPixels2))
        binding.workOutPlanRv.layoutManager = layoutManager1
        adapter =
            NewWorkOutPlanAdapter(binding.workOutPlanRv.context, workoutPlanList, this, isAdmin)
        binding.workOutPlanRv.adapter = adapter

        workoutPlanList.clear()
        binding.rltvLoader.visibility=View.VISIBLE
        getWorkOutPlanApi(page)
       // getWorkOutPlanApi(page)

        /*Swipe to refresh*/

        //*Swipe to refresh*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._80sdp).toInt(),
                resources.getDimension(R.dimen._120sdp).toInt()
            )
        }
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 0
            workoutPlanList.clear()
            getWorkOutPlanApi(page)

        })

        binding.workOutPlanMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    page = page + 1
                    if (page != 0 && nextPage == 1) {
                        adapter.showLoading(true)
                        adapter.notifyDataSetChanged()
                        getWorkOutPlanApi(page)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
       /* workoutPlanList.clear()
        binding.rltvLoader.visibility=View.VISIBLE
        getWorkOutPlanApi(page)*/
    }

    // to get data  when we come from notification
    fun newInstance(module_id: String, fromDeepLinking: String): WorkoutPlanFragment {
        val myFragment = WorkoutPlanFragment()
        val args = Bundle()
        args.putString("module_id", module_id)
        args.putString("fromDeepLinking", fromDeepLinking)
        myFragment.setArguments(args)

        return myFragment
    }

    private fun getWorkOutPlanApi(pageIndex: Int) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            val param = HashMap<String, String>()

            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.device_token,
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
            )
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.page_index, "" + pageIndex)

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")
            Log.d("sdfhsf", "hadder : ${header.toString()}")
            Log.d("sdfhsf", "params : ${param.toString()}")
            getDataManager().getWorkoutPlanAPi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rltvLoader.visibility = View.GONE
                        Log.d("sdfhsf", "response: "+response)

                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val next_page = json?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            if (pageIndex == 0) {
                                workoutPlanList.clear()
                                adapter.notifyDataSetChanged()
                            }

                            binding.swipeRefresh.setRefreshing(false)
                            //workoutPlanList.clear()
                            val workOutPlanModal =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    WorkOutPlanModal::class.java
                                )
                            workoutPlanList.addAll(workOutPlanModal!!.data.get_all_programs);
                            hideFooterLoiader()

                        } else {
                            //Constant.showCustomToast(context!!, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.rltvLoader.visibility = View.GONE

                        hideFooterLoiader()
                        Constant.errorHandle(anError, mContext as Activity)
                    }
                })
        } else {
            binding.rltvLoader.visibility=View.GONE

            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }


    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_navigation_diet -> {

                if ((activity as HomeTabActivity).drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    (activity as HomeTabActivity).drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    (activity as HomeTabActivity).drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }
    }



    override fun getWorkoutPlanInfo(data: WorkOutPlanModal.Data.GetAllProgram, position: Int) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(mContext, WorkOutPlanDetailActivity::class.java)
        intent.putExtra("FROM_WORKOUT_PLAN", data)
        intent.putExtra("from", "other media")
        mContext.startActivity(intent)
    }




    class MySpacesItemDecoration(val spanCount: Int,val spacing: Int,val leftRightSide: Int) :
        androidx.recyclerview.widget.RecyclerView.ItemDecoration() {



        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {

            val getNewHeight = (parent.measuredWidth/2)*1.34
            view.layoutParams.height = getNewHeight.toInt()



            val position = parent.getChildAdapterPosition(view)
            val column: Int = position % spanCount
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            Log.d("fnaskfnkas", "${parent.getAdapter()!!.getItemCount()} getItemOffsets: ${parent.childCount} ${position}")
            outRect.bottom = 0
            if(position == parent.childCount-1){
                outRect.bottom = spacing
            }else{
                outRect.bottom = 0
            }




          /*  val position = parent.getChildAdapterPosition(view)
            if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.bottom = space * 6
            } else {
                outRect.bottom = 0
            }*/
        }
    }
}
package com.doviesfitness.ui.profile.favourite.fragment

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FavFragmentWorkOutPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.profile.favourite.adapter.MyFavWorkPlanAdapter
import com.doviesfitness.ui.profile.favourite.modal.FavWorkoutPlanModel
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.MyItemDecoration
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject
import java.lang.Exception

class WorkOutPlanFragment : BaseFragment(), MyFavWorkPlanAdapter.MyWorkOutPlanListener,
    View.OnClickListener {

    lateinit var binding: FavFragmentWorkOutPlanBinding
    lateinit private var adapter: MyFavWorkPlanAdapter
    private var myWorkOutPlanList = ArrayList<FavWorkoutPlanModel.Data>()
    private var page: Int = 1
    private var mLastClickTime: Long = 0
    private var nextPage: Int = 0
    private lateinit var homeTabActivity: HomeTabActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fav_fragment_work_out_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        homeTabActivity = activity as HomeTabActivity
        val loutManager =
            androidx.recyclerview.widget.GridLayoutManager(binding.myFavWorkoutRv.context, 2);
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._1sdp)
        binding.myFavWorkoutRv.addItemDecoration(MyItemDecoration(spacingInPixels))
        binding.myFavWorkoutRv.layoutManager = loutManager


        adapter = MyFavWorkPlanAdapter(mContext, myWorkOutPlanList, this)
        binding.myFavWorkoutRv.adapter = adapter

        getDietPlanApi()

        /* *//*Swipe to refresh*//*
        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            myWorkOutPlanList.clear()
            getDietPlanApi()
        })*/

        /* binding.favWorkoutNested.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
             override fun onScrollChange(
                 v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
             ) {
                 if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                     if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                         //code to fetch more data for endless scrolling
                         page = page + 1
                         if (page != 0 && nextPage == 1) {
                             adapter.notifyDataSetChanged()
                             getDietPlanApi()
                         }
                     }
                 }
             }
         })*/

        setOnClick(binding.btnAddWorkoutPlan)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getDietPlanApi()
        }
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_add_workout_plan -> {
                homeTabActivity.workout_plan_ll.callOnClick()
            }
        }
    }

    private fun getDietPlanApi() {

        try {
            if (CommanUtils.isNetworkAvailable(mContext)!!) {
                val param = HashMap<String, String>()
                param.put(StringConstant.device_token, "")
                param.put(StringConstant.device_id, "")
                param.put(StringConstant.device_type, StringConstant.Android)
                param.put(StringConstant.module_type, "Program")
                param.put(
                    StringConstant.auth_customer_id,
                    getDataManager().getUserInfo().customer_auth_token
                )
                val header = HashMap<String, String>()
                header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
                header.put(StringConstant.apiKey, "HBDEV")
                header.put(StringConstant.apiVersion, "1")

                getDataManager().getCustomerFavourites(param, header)
                    ?.getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            val jsonObject: JSONObject? =
                                response?.getJSONObject(StringConstant.settings)
                            val success: String? = jsonObject?.getString(StringConstant.success)
                            val message: String? = jsonObject?.getString(StringConstant.message)
                            binding.loader.visibility = View.GONE
                            if (success.equals("1")) {
                                myWorkOutPlanList.clear()
                                //binding.swipeRefresh.setRefreshing(false)
                                binding.noWorkoutPlanFound.visibility = View.GONE
                                val favWorkoutPlanModel =
                                    getDataManager().mGson?.fromJson(
                                        response.toString(),
                                        FavWorkoutPlanModel::class.java
                                    )
                                myWorkOutPlanList.addAll(favWorkoutPlanModel!!.data);

                                adapter.notifyDataSetChanged()
                            }
                            if (myWorkOutPlanList.isEmpty()) {
                                //binding.swipeRefresh.setRefreshing(false)
                                binding.noWorkoutPlanFound.visibility = View.VISIBLE
                            }
                        }

                        override fun onError(anError: ANError) {
                            //binding.swipeRefresh.setRefreshing(false)
                            binding.loader.visibility = View.GONE
                            Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                            //binding.noWorkoutPlanFound.visibility = View.VISIBLE
                        }
                    })
            } else {
                Constant.showInternetConnectionDialog(mContext as Activity)
            }

        }
        catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    override fun getFavWorkPlanInfo(
        data: FavWorkoutPlanModel.Data,
        position: Int,
        whichClick: String
    ) {

        if (data != null) {

            val intent = Intent(mContext, ShowDietPlanDetailActivity::class.java)
            intent.putExtra("FromListToSeeDetail", data.program_id)
            intent.putExtra("from", "favorite")
            startActivityForResult(intent,1122)


/* val intent = Intent(mContext, CreateWorkOutPlanActivty::class.java)
intent.putExtra("FromListToSeeDetail", data.program_id)
intent.putExtra("programName", data.program_name)
startActivity(intent)*/
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == Activity.RESULT_OK ) {
            getDietPlanApi()
        }
    }

}

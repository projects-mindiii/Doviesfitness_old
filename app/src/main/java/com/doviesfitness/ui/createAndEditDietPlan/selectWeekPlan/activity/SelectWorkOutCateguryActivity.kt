package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.activity

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivitySelectWorkOutCateguryBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters.Select_WC_CateAdapter
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.modal.WorkoutCollectionCategoryModel
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject

class SelectWorkOutCateguryActivity : BaseActivity(), View.OnClickListener,
    Select_WC_CateAdapter.MyWorkOutDeletAndRedirectListener {

    private lateinit var binding: ActivitySelectWorkOutCateguryBinding
    lateinit var adapter: Select_WC_CateAdapter
    private var workoutList = ArrayList<WorkoutCollectionCategoryModel.Data>()
    var workout_group_id: String = ""
    private var workoutColltionData : WorkoutCollectionCategoryModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_work_out_categury)
        inItView()
    }

    private fun inItView() {

        if (intent.getStringExtra("workout_group_id") != null) {
            workout_group_id = intent.getStringExtra("workout_group_id")!!
            var heading_name  = intent.getStringExtra("heading_name")!!
            binding.dpTitleName.text = heading_name
            getWorkoutData(workout_group_id)
        }

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._8sdp)
        // binding.rvWorkout.addItemDecoration(WorkoutCollection.MySpacesItemDecoration(spacingInPixels1))
        binding.myWorkoutRv.layoutManager = layoutManager
        adapter = Select_WC_CateAdapter(this, workoutList, this)
        binding.myWorkoutRv.adapter = adapter

       /* // or create a new SkeletonLayout from a given View
        skeleton = binding.skeletonLayout.createSkeleton()
        skeleton = binding.myWorkoutRv.applySkeleton(R.layout.workout_select_listview, 5)

        skeleton.showSkeleton()*/

        setOnClick(binding.ivBack, binding.txtDone)
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

            R.id.txt_done -> {
                workoutColltionData?.let {
                    val intent = Intent()
                    intent.putExtra("SelectMyWorkOutCollection",workoutColltionData)
                    //intent.putExtra("position", position)
                    setResult(102, intent)
                    finish()
                }
            }
        }
    }

    private fun getWorkoutData(workout_group_id: String) {

        val param = HashMap<String, String>()
        param.put(StringConstant.group_id, workout_group_id)
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.type, "List")
        param.put(StringConstant.auth_customer_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().featuredWorkoutApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    //binding.swipeRefresh.isRefreshing = false
                    binding.txtNoDataFound.visibility = View.GONE
                    val workoutData =
                        getDataManager().mGson?.fromJson(
                            response.toString(),
                            WorkoutCollectionCategoryModel::class.java
                        )
                    workoutList.addAll(workoutData!!.data)
                    adapter.notifyDataSetChanged()

                } else {
                    binding.txtNoDataFound.visibility = View.VISIBLE
                }
            }

            override fun onError(anError: ANError) {
                binding.txtNoDataFound.visibility = View.GONE
                Constant.errorHandle(anError, this@SelectWorkOutCateguryActivity)
            }
        })
    }

    override fun getWorkOutData(data: WorkoutCollectionCategoryModel.Data, whichClick: String, pos: Int) {
        workoutColltionData = data
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

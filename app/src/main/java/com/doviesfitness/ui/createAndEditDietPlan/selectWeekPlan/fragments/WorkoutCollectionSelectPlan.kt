package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentWorkoutCollectionSelectPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.activity.SelectWorkOutCateguryActivity
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters.SelectWorkoutCollectionAdapter
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.modal.WorkoutCollectionCategoryModel
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.ItemOffsetDecoration
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject


class WorkoutCollectionSelectPlan : BaseFragment() ,SelectWorkoutCollectionAdapter.OnItemClick{

    private var position: String= ""
    private lateinit var binding: FragmentWorkoutCollectionSelectPlanBinding
    private var workoutList = ArrayList<WorkoutCollectionResponse.Data>()
    private lateinit var adapter: SelectWorkoutCollectionAdapter
    private var mLastClickTime: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_collection_select_plan, container, false)
        inItView()
        return binding.root
    }

    private fun inItView() {

        position = arguments!!.getString("position") as String
        binding.rvSelectWorkout.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.rvSelectWorkout.context, 2)
        binding.rvSelectWorkout.addItemDecoration(ItemOffsetDecoration(mContext, R.dimen._8sdp))

        adapter = SelectWorkoutCollectionAdapter(mContext,workoutList,this)
        binding.rvSelectWorkout.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            workoutList.clear()
            getWorkoutData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            getWorkoutData()
        }
    }

    private fun getWorkoutData() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.type, "Featured")
        param.put(StringConstant.auth_customer_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().featuredWorkoutApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                     binding.swipeRefresh.isRefreshing = false
                     workoutList.clear()
                     // Constant.showCustomToast(mContext!!, "success..." + message)
                     val workoutData =
                         getDataManager().mGson?.fromJson(response.toString(), WorkoutCollectionResponse::class.java)
                     workoutList.addAll(workoutData!!.data);

                     adapter.notifyDataSetChanged()

                } else {
                    //binding.txtAllWorkoutCollection.visibility = View.GONE
                }
            }

            override fun onError(anError: ANError) {
                // binding.txtAllWorkoutCollection.visibility = View.GONE
                Constant.errorHandle(anError, activity!!)
                //Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
            }
        })
    }

    override fun onItemClick(data: WorkoutCollectionResponse.Data, pos: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
        }

        val intent = Intent(mContext, SelectWorkOutCateguryActivity ::class.java)
        intent.putExtra("workout_group_id",data.workout_group_id)
        intent.putExtra("heading_name",data.workout_group_name)
        startActivityForResult(intent,102)

    }

    // Call get back data fro MyWorkout Fragment on Done Button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 102
        if (requestCode == 102 && data != null) {
            val workOutDataCollection = data.getSerializableExtra("SelectMyWorkOutCollection") as WorkoutCollectionCategoryModel.Data

            Log.v("workOutDataCollection",""+workOutDataCollection)
                val intent = Intent()
                intent.putExtra("SelectMyWorkOutCollection",workOutDataCollection)
                intent.putExtra("position", position)
                activity!!.setResult(101, intent)
                activity!!.finish()
        }
    }
}

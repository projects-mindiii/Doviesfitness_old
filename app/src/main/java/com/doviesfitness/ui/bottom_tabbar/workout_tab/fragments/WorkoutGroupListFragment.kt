package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.databinding.FragmentWorkoutGrouplistBinding
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.GroupItemAdapter.OnWorkoutClick
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.WorkoutGroupListAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutGroupListResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.json.JSONObject

class WorkoutGroupListFragment: BaseFragment(),  OnWorkoutClick ,View.OnClickListener{


    private lateinit var binding: FragmentWorkoutGrouplistBinding
    private var workoutGroupList = ArrayList<WorkoutGroupListResponse.Data>()
    var workout_group_id = ""
    lateinit var adapter: WorkoutGroupListAdapter
    lateinit var  workoutData: WorkoutCollectionResponse.Data
    private var mLastClickTime: Long = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     //  activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     //   activity!!.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_grouplist, container, false);
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.ivBack.setOnClickListener(this)
     //  activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        workout_group_id = arguments!!.getString("workout_group_id")!!
        workoutData = arguments!!.getSerializable("data") as WorkoutCollectionResponse.Data
        Log.d("description...","description...."+workoutData.workout_group_description)
        if (!workoutData.workout_group_description.isEmpty())
        binding.description.text=CommanUtils.capitaliseOnlyFirstLetter(workoutData.workout_group_description)
        else
            binding.description.visibility=View.GONE
        binding.workoutName.text=CommanUtils.capitalize(workoutData.workout_group_name)

        if (workoutData.group_workout_count.toInt()>0 && workoutData.group_workout_count.toInt()==1)
        binding.levelName.text=CommanUtils.capitalize(workoutData.workout_group_level)+" - "+CommanUtils.capitalize(workoutData.group_workout_count+" Workout")
       else if (workoutData.group_workout_count!=null&&workoutData.group_workout_count.toInt()>1)
        binding.levelName.text=workoutData.workout_group_level+" - "+workoutData.group_workout_count+" Workouts"
        else
        binding.levelName.text=workoutData.workout_group_level+" - "+"Coming soon"



        if (workoutData!=null&&workoutData.workout_group_image!=null)
        Glide.with(mContext).load(workoutData.workout_group_image)
            .error(ContextCompat.getDrawable(mContext,R.drawable.app_icon))
            .placeholder(ContextCompat.getDrawable(mContext,R.drawable.app_icon))
            .into(binding.workoutImg)
        else{}
        var layoutManager= androidx.recyclerview.widget.LinearLayoutManager(
            mContext,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        adapter= WorkoutGroupListAdapter(mContext,workoutGroupList,this)
        binding.workoutGroupRv.layoutManager=layoutManager
        binding.workoutGroupRv.adapter= adapter

        binding.swipeRefresh.setOnRefreshListener {

            workoutGroupList.clear()
            getWorkoutGrouplist()
        }

        getWorkoutGrouplist()
    }

    override fun onWorkoutClick(workoutID: String, pos: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();

        }
        val intent = Intent(mContext, WorkOutDetailActivity::class.java)
        intent.putExtra("PROGRAM_DETAIL",workoutID)
        intent.putExtra("fromDeepLinking", "")
        intent.putExtra("isMyWorkout","no")
        startActivity(intent)
    }

    /*
    *
    * URL==https://dev.doviesfitness.com/WS/group_workout_list
Param===Optional(["workout_group_id": "5", "device_type": "Ios", "device_id": "7565183F-C7F9-40C1-B021-13513F00E585", "device_token": diSB317yNW8:APA91bF5NJb84Sohi0693mH07LIFDLKBRW4NrUAOuQPgp3tiaSB9gz7R0YGj9AA-yS2qQxTKyYCIvorPJ11zb808gryzrL2Tm3ssc2tn0_gYjeQt2e6hpNjGcj9Ccm8r0xJcCxbI7RvT])
headers===Optional(["APIVERSION": "1", "APIKEY": "HBDEV", "AUTHTOKEN": "dab7f6aafe6375f0fa2cc2534e734d6b78e8ad533a2b31c76e6dd6e691e1c781"])
    * */

    private fun getWorkoutGrouplist() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.workout_group_id, workout_group_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        Log.d("TAG", "group Param...."+ "device_type->"+StringConstant.Android+"\n"+
                "workout_group_id->"+StringConstant.Android+"\n"+"authToken->"+getDataManager().getUserInfo().customer_auth_token)

        getDataManager().workoutGroupListApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "group response...."+ response)

                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                binding.swipeRefresh.isRefreshing=false
                binding.rltvLoader.visibility=View.GONE
                binding.llparent.visibility=View.VISIBLE
                if (success.equals("1")) {

                   // Constant.showCustomToast(mContext!!, "success..." + message)
                    val workoutGroupData = getDataManager().mGson?.fromJson(response.toString(), WorkoutGroupListResponse::class.java)
                    workoutGroupList.addAll(workoutGroupData!!.data)
                    adapter.notifyDataSetChanged()

                } else {
                    binding.rltvLoader.visibility=View.GONE

//                     Constant.showCustomToast(mContext!!, "" + message)
                }
            }
            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, activity!!)
                binding.rltvLoader.visibility=View.GONE

                Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
        }
    }

}
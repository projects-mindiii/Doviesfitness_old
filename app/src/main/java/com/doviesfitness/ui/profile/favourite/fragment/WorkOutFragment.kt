package com.doviesfitness.ui.profile.favourite.fragment

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.doviesfitness.R
import com.doviesfitness.databinding.FavFragmentWorkOutBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.profile.favourite.adapter.Fav_WorkoutAdapter
import com.doviesfitness.ui.profile.favourite.modal.FavWorkoutModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.ItemOffsetDecoration
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject
import java.lang.Exception

class WorkOutFragment : BaseFragment(), Fav_WorkoutAdapter.OnFavItemClick, View.OnClickListener {

    lateinit var binding: FavFragmentWorkOutBinding
    private var workoutList = ArrayList<FavWorkoutModel.Data>()
    private lateinit var adapter: Fav_WorkoutAdapter
    private var mLastClickTime: Long = 0
    private lateinit var homeTabActivity : HomeTabActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fav_fragment_work_out, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        homeTabActivity = activity as HomeTabActivity
        binding.myFavWorkoutRv.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(binding.myFavWorkoutRv.context, 2)
       //binding.myFavWorkoutRv.addItemDecoration(ItemOffsetDecoration(mContext, R.dimen._10sdp))

        // adapter = SelectWorkoutCollectionAdapter(mContext,workoutList,this)
        adapter = Fav_WorkoutAdapter(mContext, workoutList, this)
        binding.myFavWorkoutRv.adapter = adapter

      /*  binding.swipeRefresh.setOnRefreshListener {
            workoutList.clear()
            getFavWorkoutData()
        }*/

        setOnClick(binding.btnAddWorkout)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            getFavWorkoutData()
        }
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_add_workout->{
                homeTabActivity.fromWitchScreen("fromFavWorkout")
                homeTabActivity.workout_ll.callOnClick()
            }
        }
    }

    private fun getFavWorkoutData() {
        try {
            if (CommanUtils.isNetworkAvailable(mContext)!!) {

                val param = HashMap<String, String>()
                param.put(StringConstant.device_token, "")
                param.put(StringConstant.device_id, "")
                param.put(StringConstant.device_type, StringConstant.Android)
                param.put(StringConstant.module_type, "Workout")
                param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
                val header = HashMap<String, String>()
                header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
                header.put(StringConstant.apiKey, "HBDEV")
                header.put(StringConstant.apiVersion, "1")

                getDataManager().getCustomerFavourites(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        binding.loader.visibility=View.GONE
                        if (success.equals("1")) {
                            workoutList.clear()
                            //binding.swipeRefresh.setRefreshing(false)
                            binding.noWorkoutFound.visibility = View.GONE
                            val favWorkoutModel =
                                getDataManager().mGson?.fromJson(response.toString(), FavWorkoutModel::class.java)
                            workoutList.addAll(favWorkoutModel!!.data);


                            adapter.notifyDataSetChanged()
                        }
                        if (workoutList.isEmpty()){
                            binding.noWorkoutFound.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.loader.visibility=View.GONE
                        Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                        try {
                            Constant.errorHandle(anError!!, activity)
                        }
                        catch (e:Exception){
                        }
                        //binding.noWorkoutFound.visibility = View.VISIBLE
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

    override fun onFavItemClick(data: FavWorkoutModel.Data, pos: Int) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
        }

        val intent = Intent(mContext, WorkOutDetailActivity::class.java)
        intent.putExtra("PROGRAM_DETAIL",data.workout_id)
        intent.putExtra("isMyWorkout","yes")
        intent.putExtra("fromDeepLinking", "")
        mContext.startActivity(intent)
    }
}

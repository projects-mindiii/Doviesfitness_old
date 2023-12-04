package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentWorkoutCollectionBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.GroupItemAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.TrainersChoiceAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.WorkoutCollectionAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import com.doviesfitness.utils.SpacesItemDecoration


class WorkoutCollection : BaseFragment(), WorkoutCollectionAdapter.OnItemClick,
    GroupItemAdapter.OnWorkoutClick {

    private lateinit var binding: FragmentWorkoutCollectionBinding
    private lateinit var adapter: WorkoutCollectionAdapter
    private lateinit var trainersChoiceAdapter: TrainersChoiceAdapter;
    private var workoutList = ArrayList<WorkoutCollectionResponse.Data>()
    private var trainerChoiceList = ArrayList<WorkoutCollectionResponse.Data.Workout>()
    private var mLastClickTime: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_workout_collection,
            container,
            false
        );
        initialization()
        return binding.root
    }

    private fun initialization() {
        trainersChoiceAdapter = TrainersChoiceAdapter(context!!, trainerChoiceList, this)
        val gridLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvFeatured.layoutManager = gridLayoutManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._14sdp)
    //    binding.rvFeatured.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        binding.rvFeatured.adapter = trainersChoiceAdapter

        getWorkoutData()
        adapter = WorkoutCollectionAdapter(binding.rvWorkout.context, workoutList, this)
        /*var layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )*/
         var layoutManager = androidx.recyclerview.widget.GridLayoutManager(context,2)

        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._8sdp)
        //binding.rvWorkout.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        binding.rvWorkout.layoutManager = layoutManager
        binding.rvWorkout.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            binding.llWorket.visibility = View.GONE

            workoutList.clear()
            trainerChoiceList.clear()

            trainersChoiceAdapter.notifyDataSetChanged()
            adapter.notifyDataSetChanged()
            getWorkoutData()
        }

    }

    override fun onWorkoutClick(workoutID: String, pos: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();

        }

        val intent = Intent(mContext, WorkOutDetailActivity::class.java)
        intent.putExtra("PROGRAM_DETAIL", workoutID)
        intent.putExtra("isMyWorkout", "no")
        intent.putExtra("fromDeepLinking", "")
        startActivity(intent)
    }

    override fun onItemClick(data: WorkoutCollectionResponse.Data, pos: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
        }

        val groupFragment = WorkoutGroupListFragment()
        val args = Bundle()
        args.putSerializable("data", data)
        args.putString("workout_group_id", data.workout_group_id)
        groupFragment.setArguments(args)
        getBaseActivity()?.addFragment(groupFragment, R.id.container_id1, true)
    }

    private fun getWorkoutData() {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
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
            getDataManager().featuredWorkoutApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("TAG", "response...." + response!!.toString(4))
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            binding.txtAllWorkoutCollection.visibility = View.VISIBLE
                            binding.llWorket.visibility = View.VISIBLE
                            binding.swipeRefresh.isRefreshing = false
                            // Constant.showCustomToast(mContext!!, "success..." + message)
                            val workoutData =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    WorkoutCollectionResponse::class.java
                                )
                            workoutList.addAll(workoutData!!.data);
                            //   binding.levelName.setText("" + workoutList.get(0).workout_group_level)
                            binding.levelName.setText("All Levels")
                            binding.firstCollectionName.setText("" + workoutList.get(0).workout_group_name)
                            if (workoutList != null && workoutList.size > 0 && trainerChoiceList != null && workoutList.get(
                                    0
                                ) != null
                                && workoutList.get(0).workout_list != null && workoutList.get(0).workout_list!!.size > 0
                            ) {
                                trainerChoiceList.addAll(workoutList.get(0).workout_list!!)
                                workoutList.removeAt(0);

                                workoutList.forEach { it.isTrainerListIsEmpty=true }

                                binding.firstCollectionName.visibility = View.VISIBLE
                                binding.levelName.visibility = View.VISIBLE
                                binding.rlFeatured.visibility = View.VISIBLE
                                trainersChoiceAdapter.notifyDataSetChanged()
                            } else {
                                //   binding.firstCollectionName.visibility = View.GONE
                                //    binding.levelName.visibility = View.GONE
                                binding.txtAllWorkoutCollection.visibility = View.GONE
                                workoutList.forEach { it.isTrainerListIsEmpty=false }
                                binding.rlFeatured.visibility = View.GONE
                                binding.rlFeatured.visibility = View.GONE
                            }
                            adapter.notifyDataSetChanged()
                            //  Log.d("TAG", "response...list size..."+ workoutList.toString())

                        } else {
                            binding.txtAllWorkoutCollection.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.txtAllWorkoutCollection.visibility = View.GONE
                        Constant.errorHandle(anError, activity!!)
                        Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
                    }
                })
        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
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
                outRect.bottom = space * 7
            } else {
                outRect.bottom = 0
            }
        }
    }
}
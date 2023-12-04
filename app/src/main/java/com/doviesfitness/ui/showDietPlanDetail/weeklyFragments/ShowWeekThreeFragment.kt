package com.doviesfitness.ui.showDietPlanDetail.weeklyFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentShowWeekThreeBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.ui.showDietPlanDetail.adapter.ShowWeekAdapter
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject

class ShowWeekThreeFragment : BaseFragment(), ShowDietPlanDetailActivity.WeekThreeLinstenr,
    ShowWeekAdapter.ChangeStatusOfWorkout {
    private var isUpdateOrNOt: String =""
    private var program_id: String = ""
    private var forUpdatePostionModalData: WorkOutListModal.Data? = null
    private var forUpdatePostion: Int = 0
    private lateinit var binding: FragmentShowWeekThreeBinding
    lateinit var adapter: ShowWeekAdapter
    private var weekPlanList = ArrayList<WorkOutListModal.Data>()
    private lateinit var showDietPlanDetailActivity: ShowDietPlanDetailActivity
    public companion object{
        var forweek = "0"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_show_week_three, container, false)
        initialization()
        return binding.root
    }


    private fun initialization() {
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.week3Rv.addItemDecoration(DietPlanFragment.MySpacesItemDecoration(spacingInPixels1))
        adapter = ShowWeekAdapter(binding.week3Rv.context, weekPlanList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.week3Rv.layoutManager = layoutManager1
        binding.week3Rv.adapter = adapter
        forUpdatePostionModalData = WorkOutListModal.Data()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // get instance of activity
        showDietPlanDetailActivity = context as ShowDietPlanDetailActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showDietPlanDetailActivity != null) {
            showDietPlanDetailActivity.setWeekThreeDataListenr(this)
            program_id = showDietPlanDetailActivity.program_id
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            isUpdateOrNOt =
                getDataManager().getUserStringData(AppPreferencesHelper.COMPLETE_WORKOUT_PLAN_STATUS)!!
        }
    }


    override fun getWeekThreeData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts) {
        weekPlanList.clear()
        val weekThreeDataArray = allWeekData.Week3

        if (weekThreeDataArray != null) {
            for (i in weekThreeDataArray.indices) {
                val weekDataObject = weekThreeDataArray.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_access_level = "",
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    isSelected = "",
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number,
                    program_WorkOut_id = weekDataObject.program_workout_id
                )

                weekPlanList.add(myWorkOutData)
            }
            adapter.notifyDataSetChanged()
        }
    }


    override fun changeStatus(data: WorkOutListModal.Data, pos: Int, status: String) {
        forUpdatePostionModalData = data
        forUpdatePostion = pos

        if ("UpdateStatus".equals(status)) {
            /*updateStatus of plan*/
            updatePlanStatusApi(data, pos)

        } else {
            val intent = Intent(context, WorkOutDetailActivity::class.java)
            intent.putExtra("PROGRAM_DETAIL", data.workout_id)
            intent.putExtra("from_ProgramPlan", data.program_WorkOut_id)
            intent.putExtra("program_id", program_id)
            intent.putExtra("from_which_frament", "FromWeekThree")
            intent.putExtra("isMyWorkout", "no")
            intent.putExtra("fromDeepLinking", "")
            context!!.startActivity(intent)
        }
    }

    fun updatePlanStatusApi(data: WorkOutListModal.Data, pos: Int) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put("program_id", data.workout_id)
        data.program_WorkOut_id?.let { param.put("program_workout_id", it) }
        param.put(
            StringConstant.device_token,
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().updateProgramPlanStatus(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            //ChangeStatusCode
                            val workoutPlanModal = weekPlanList.get(pos)
                            if ("1".equals(workoutPlanModal.workout_fav_status)) {
                                workoutPlanModal.workout_fav_status = "0"
                            } else {
                                workoutPlanModal.workout_fav_status = "1"
                            }
                            weekPlanList.set(pos, workoutPlanModal)
                            adapter.notifyItemChanged(pos)

                        } else {
                            Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            mContext,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }


    override fun onResume() {
        super.onResume()

        isUpdateOrNOt =
            getDataManager().getUserStringData(AppPreferencesHelper.COMPLETE_WORKOUT_PLAN_STATUS)!!

        forUpdatePostionModalData.let {

            if(forUpdatePostionModalData != null){
                if (forweek.equals("1")) {
                    val UpdateModalDataForStatus = WorkOutListModal.Data(
                        workout_name = forUpdatePostionModalData!!.workout_name,
                        workout_category = forUpdatePostionModalData!!.workout_category,
                        workout_time = forUpdatePostionModalData!!.workout_time,
                        workout_image = forUpdatePostionModalData!!.workout_image,
                        workout_access_level = "",
                        workout_fav_status = "1",
                        workout_id = forUpdatePostionModalData!!.workout_id,
                        workout_share_url = forUpdatePostionModalData!!.workout_share_url,
                        workout_time1 = forUpdatePostionModalData!!.workout_time1,
                        isSelected = "",
                        forDay = forUpdatePostionModalData!!.forDay,
                        whichWeek = forUpdatePostionModalData!!.whichWeek,
                        program_WorkOut_id = forUpdatePostionModalData!!.program_WorkOut_id
                    )
                    weekPlanList.set(forUpdatePostion, UpdateModalDataForStatus)
                    adapter.notifyDataSetChanged()
                    /* getDataManager().setUserStringData(
                         AppPreferencesHelper.COMPLET_WORKOUT_PLAN_STAUS,
                         "Not_update"
                     )*/

                    forweek = "0"
                }
            }
        }

        getDataManager().setUserStringData(
            AppPreferencesHelper.COMPLETE_WORKOUT_PLAN_STATUS,
            "Not_update"
        )
    }
}

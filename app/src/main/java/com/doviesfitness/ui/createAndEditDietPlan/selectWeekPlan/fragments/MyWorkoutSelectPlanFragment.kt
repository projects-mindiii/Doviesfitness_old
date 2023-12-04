package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import com.doviesfitness.databinding.FragmentMyWorkoutSelectPlanBinding
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.activity.SelectWorkOutPlanActivity
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters.SelectWorkoutListAdapter

class MyWorkoutSelectPlanFragment : BaseFragment(), SelectWorkoutListAdapter.MyWorkOutDeletAndRedirectListener{

    private lateinit var binding: FragmentMyWorkoutSelectPlanBinding
    lateinit var adapter: SelectWorkoutListAdapter
    var workOutList = ArrayList<WorkOutListModal.Data>()
    private lateinit var selectWorkOutPlanActivity : SelectWorkOutPlanActivity
    private var page: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_workout_select_plan, container, false)
        inItView()
        return binding.root
    }

    private fun inItView() {

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._8sdp)
        // binding.rvWorkout.addItemDecoration(WorkoutCollection.MySpacesItemDecoration(spacingInPixels1))
        binding.myWorkoutRv.layoutManager = layoutManager
        adapter = SelectWorkoutListAdapter(mContext, workOutList, this)
        binding.myWorkoutRv.adapter = adapter

        selectWorkOutPlanActivity = getBaseActivity() as SelectWorkOutPlanActivity


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWorkOutApi(page)
    }

   /* override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            getWorkOutApi(page)
        }
    }*/

    private fun getWorkOutApi(pageCount: Int) {

        binding.loaderView.visibility = View.VISIBLE
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.page_index, "" + pageCount)
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().getCustomerWorkoutAPi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val status: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                val next_page = jsonObject?.getString(StringConstant.next_page)
                //nextPage = next_page!!.toInt()

                if (status!!.equals("1")) {
                    workOutList.clear()
                    binding.loaderView.visibility = View.GONE
                    binding.txtNoDataFound.visibility = View.GONE
                    val workOutListModal =
                        getDataManager().mGson?.fromJson(response.toString(), WorkOutListModal::class.java)

                    workOutList.addAll(workOutListModal!!.data);
                    adapter.notifyDataSetChanged()

                } else {
                    binding.txtNoDataFound.visibility = View.VISIBLE
                    binding.loaderView.visibility = View.GONE
                }
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, activity)
                binding.loaderView.visibility = View.GONE
            }
        })
    }

    override fun getWorkOutData(data: WorkOutListModal.Data, whichClick: String, pos: Int) {
        if(whichClick.equals("1")){
            selectWorkOutPlanActivity.getMyWorkOutData(data)
        }
    }
}

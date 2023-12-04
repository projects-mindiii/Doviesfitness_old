package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityGoodForBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.CustomerListAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SelectGoodForAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.CustomerListModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_filter_exercise.exercise_name
import kotlinx.android.synthetic.main.activity_filter_exercise.exercise_rv
import kotlinx.android.synthetic.main.activity_filter_exercise.iv_back
import kotlinx.android.synthetic.main.activity_good_for.done_btn
import kotlinx.android.synthetic.main.activity_good_for.et_search
import org.json.JSONArray
import org.json.JSONObject


class ActivityGoodFor : BaseActivity(), View.OnClickListener , CustomerListAdapter.OnSelectUser {
    override fun onUserSelect(pos: Int, user: CustomerListModel.Data.User) {
      if (tempAllowedUserList!=null){
          if (tempAllowedUserList.size>0){
              var isEqual=false
              for (i in 0..tempAllowedUserList.size-1){
                  if (tempAllowedUserList.get(i).iCustomerId.equals(user.iCustomerId)){
                      if (user.isCheck==false){
                          tempAllowedUserList.removeAt(i)
                      }
                      isEqual=true
                      break
                  }
              }
              if (!isEqual){
                  tempAllowedUserList.add(user)
              }
          }
          else{
              tempAllowedUserList.add(user)
          }
      }
    }

    private lateinit var goodForText: List<String>
    private lateinit var binding: ActivityGoodForBinding
    private var itemList = ArrayList<FilterExerciseResponse.Data.X>()
    private var tempList = ArrayList<FilterExerciseResponse.Data.X>()
    private var list = ArrayList<FilterExerciseResponse.Data.X>()
    private var customerList = ArrayList<CustomerListModel.Data.User>()
    private var tempcustomerList = ArrayList<CustomerListModel.Data.User>()
    private var tempAllowedUserList = ArrayList<CustomerListModel.Data.User>()
    private var guestList = ArrayList<CustomerListModel.Data.DoviesGuest>()
    private var WorkoutGroupList = ArrayList<CustomerListModel.Data.WorkoutGroup>()
    private var tempGuestList = ArrayList<CustomerListModel.Data.DoviesGuest>()
    private lateinit var goodForAdapter: SelectGoodForAdapter
    private lateinit var customerAdapter: CustomerListAdapter
    var from = ""
    var searchStr = ""
    var createById = ""
    var workoutCollectionId = ""
    var workoutId = ""
    var nextPage = 0
    var pageCount = 1
    var layoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_good_for)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        initialization()

    }

    private fun initialization() {
        iv_back.setOnClickListener(this)
        done_btn.setOnClickListener(this)
        binding.clearTxt.setOnClickListener(this)
        binding.cancelAction.setOnClickListener(this)
        goodForText=ArrayList()
        if (intent.getStringExtra("SelectedExerciseGoodFor")!=null){
              goodForText=intent.getStringExtra("SelectedExerciseGoodFor").toString().split(" | ")
            Log.d("kdfhfjkdsg", "initialization: listGoodFor${goodForText.toString()}")
        }


        done_btn.visibility = View.VISIBLE
        from = intent.getStringExtra("from")!!
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        if (from == "created") {
            exercise_name.text = "CREATED BY"
            createById=intent.getStringExtra("createById")!!
            binding.searchLayout.visibility = View.GONE
            getWorkoutCustomer(pageCount)
            customerAdapter = CustomerListAdapter(getActivity(), guestList, customerList, WorkoutGroupList,from,this)
            exercise_rv.layoutManager = layoutManager
            exercise_rv.adapter = customerAdapter
        }
        else if (from == "users") {
            exercise_name.text = "ALLOW USER"
            tempAllowedUserList = intent.getSerializableExtra("itemList") as ArrayList<CustomerListModel.Data.User>
            binding.progressLayout.visibility = View.VISIBLE
            getWorkoutCustomer(pageCount)
            binding.searchLayout.visibility = View.VISIBLE
            exercise_rv.layoutManager = layoutManager
            customerAdapter = CustomerListAdapter(getActivity(), guestList, customerList,WorkoutGroupList, from,this)
            exercise_rv.adapter = customerAdapter
        }
        else if (from == "publish") {
            pageCount=1
            exercise_name.text = "WORKOUT GROUP"
            workoutId = intent.getStringExtra("workoutId")!!
            binding.searchLayout.visibility = View.GONE
            done_btn.text=getString(R.string.publish)
            getWorkoutCustomer(pageCount)
            customerAdapter = CustomerListAdapter(getActivity(), guestList, customerList,WorkoutGroupList, from,this)
            exercise_rv.layoutManager = layoutManager
            exercise_rv.adapter = customerAdapter
        }

       else {
            binding.searchLayout.visibility = View.GONE
            itemList = intent.getSerializableExtra("itemList") as ArrayList<FilterExerciseResponse.Data.X>
            exercise_name.text = itemList[0].group_name
            exercise_rv.layoutManager = layoutManager
            goodForAdapter = SelectGoodForAdapter(getActivity(), itemList)
            exercise_rv.adapter = goodForAdapter
        }

        val delay: Long = 1000 // 1 seconds after user stops typing
        val last_text_edit = longArrayOf(0)
        val handler = Handler()
        val input_finish_checker = Runnable {
            if (System.currentTimeMillis() > last_text_edit[0] + delay - 500) {
                pageCount = 1
                customerList.clear()
                guestList.clear()
                customerAdapter.notifyDataSetChanged()
                getWorkoutCustomer(pageCount)
            }
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                handler.removeCallbacks(input_finish_checker)
            }
            override fun afterTextChanged(editable: Editable) {
                if (editable != null && editable.toString().isNotEmpty()) {
                    binding.clearTxt.visibility= View.VISIBLE
                    searchStr = editable.toString()
                }
                else {
                    searchStr = ""
                    binding.clearTxt.visibility= View.GONE
                }
                last_text_edit[0] = System.currentTimeMillis()
                handler.postDelayed(input_finish_checker, delay)
            }
        })

        exercise_rv.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView) {
                if (page != 0 && nextPage > 0 && from.equals("users")) {
                    pageCount += 1
                    getWorkoutCustomer(pageCount)
                }
            }
        })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clear_txt->{
                et_search.setText("")
                hideKeyboard()
            }
         R.id.cancel_action->{

                hideKeyboard()
            }

            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.done_btn -> {
                if (from == "users") {
                   /* tempcustomerList.clear()
                    for (i in 0..customerList.size - 1) {
                        if (customerList.get(i).isCheck)
                            tempcustomerList.add(customerList.get(i))
                    }*/
                    val intent = Intent()
                    intent.putExtra("list", tempAllowedUserList)
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                }
                else if (from == "created") {
                    tempGuestList.clear()
                    for (i in 0..guestList.size - 1) {
                        if (guestList.get(i).isCheck)
                            tempGuestList.add(guestList.get(i))
                    }
                    val intent = Intent()
                    intent.putExtra("list", tempGuestList)
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                }
                else if (from == "publish") {

                    var isWGId=false
                    for (i in 0..WorkoutGroupList.size - 1) {
                        if (WorkoutGroupList.get(i).isCheck)
                        {
                            workoutCollectionId=WorkoutGroupList.get(i).iWorkoutGroupMasterId
                            isWGId=true
                            break
                        }
                    }
                    if (isWGId){
                        publishWorkout()
                    }
                    else{
                        onBackPressed()
                    }
                }
                else {
                    tempList.clear()
                    for (i in 0..itemList.size - 1) {
                        if (itemList.get(i).isCheck)
                            tempList.add(itemList.get(i))
                    }
                    val intent = Intent()
                    intent.putExtra("list", tempList)
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                }

            }
        }
    }

    private fun getWorkoutCustomer(pageCount: Int) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put("allowed_user_id", "")
        param.put("search_query", searchStr)
        param.put(StringConstant.page_index, pageCount.toString())

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().workoutGetCustomerList(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val jsonDataObject: JSONObject? = response?.getJSONObject(StringConstant.data)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    // Constant.showCustomToast(getActivity(), "success..." + message)
                    binding.progressLayout.visibility = View.GONE
                    if (jsonDataObject!!.get("users") is JSONArray && from.equals("users")) {
                        val customerData =
                            getDataManager().mGson?.fromJson(response.toString(), CustomerListModel::class.java)
                        nextPage = customerData!!.settings.next_page.toInt()
                        customerList.addAll(customerData!!.data.users)
                        if (tempAllowedUserList != null && tempAllowedUserList.size > 0) {
                            var userList = ArrayList<CustomerListModel.Data.User>()
                            var newUserList = ArrayList<CustomerListModel.Data.User>()

                            for (i in 0..tempAllowedUserList.size - 1) {
                                var j = 0
                                while (j < customerList.size) {
                                    if (tempAllowedUserList.get(i).iCustomerId.equals(customerList.get(j).iCustomerId)) {
                                        customerList.get(j).isCheck = true
                                        userList.add(customerList.get(j))
                                        customerList.removeAt(j)
                                    }
                                    else
                                    j++
                                }
                            }
                            newUserList.addAll(customerList)
                            customerList.clear()
                          //  customerList= ArrayList<CustomerListModel.Data.User>()
                            customerList.addAll(userList)
                            customerList.addAll(newUserList)

                           /* for (i in 0..tempAllowedUserList.size - 1) {
                                for ( j in 0..customerList.size - 1) {
                                    if (tempAllowedUserList.get(i).iCustomerId.equals(customerList.get(j).iCustomerId)) {
                                        customerList.get(j).isCheck = true

                                        userList.add(customerList.get(j))
                                        customerList.removeAt(j)
                                    }
                                }
                            }*/

                        }
                        //   guestList.addAll(customerData!!.data.doviesGuest)
                        customerAdapter.notifyDataSetChanged()
                    }
                    else if (jsonDataObject!!.get("doviesGuest") is JSONArray && from.equals("created")) {
                        val customerData =
                            getDataManager().mGson?.fromJson(response.toString(), CustomerListModel::class.java)
                        nextPage = customerData!!.settings.next_page.toInt()
                        //  customerList.addAll(customerData!!.data.users)
                        guestList.addAll(customerData!!.data.doviesGuest)
                        if (createById!=null&&!createById.isEmpty()&&guestList.size>0)
                        {
                            for (i in 0..guestList.size-1){
                                if (guestList.get(i).iDeviosGuestId.equals(createById))
                                {
                                    guestList.get(i).isCheck=true
                                }
                            }
                        }
                        customerAdapter.notifyDataSetChanged()
                    }
                    else if (jsonDataObject!!.get("workoutGroup") is JSONArray && from.equals("publish")) {
                        val customerData = getDataManager().mGson?.fromJson(response.toString(), CustomerListModel::class.java)
                        nextPage = customerData!!.settings.next_page.toInt()
                        WorkoutGroupList.addAll(customerData!!.data.workoutGroup)
                        customerAdapter.notifyDataSetChanged()
                    }
                    else {
                        customerAdapter.notifyDataSetChanged()
                    }

                } else {
                    Constant.showCustomToast(getActivity(), "fail..." + message)
                    binding.progressLayout.visibility = View.GONE
                }
            }

            override fun onError(anError: ANError) {
                binding.progressLayout.visibility = View.GONE
                Constant.errorHandle(anError, getActivity()!!)
                Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
            }
        })
    }
    private fun publishWorkout() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put("workout_collection_id", workoutCollectionId)
        param.put("workout_id", workoutId)
        param.put(StringConstant.page_index, pageCount.toString())

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().publishWorkoutApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    binding.progressLayout.visibility = View.GONE
                    Constant.showCustomToast(getActivity(), "" + message)
                    finish()
                } else {
                    Constant.showCustomToast(getActivity(), "fail..." + message)
                    binding.progressLayout.visibility = View.GONE
                }
            }
            override fun onError(anError: ANError) {
                binding.progressLayout.visibility = View.GONE
                Constant.errorHandle(anError, getActivity()!!)
                Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
            }
        })
    }
}
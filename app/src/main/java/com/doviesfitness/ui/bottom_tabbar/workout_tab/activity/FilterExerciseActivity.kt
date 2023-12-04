package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.databinding.ActivityFilterExerciseBinding
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.FilterExerciseAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_filter_exercise.*
import org.json.JSONObject
import java.util.ArrayList

class FilterExerciseActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFilterExerciseBinding
    private var exerciseList = ArrayList<FilterExerciseResponse.Data>()
    private var tempList = ArrayList<FilterExerciseResponse.Data.X>()
    private var list = ArrayList<FilterExerciseResponse.Data.X>()
    private lateinit var FilterAdapter: FilterExerciseAdapter
    private var selectedList = ArrayList<ExerciseListingResponse.Data>()
    private var forReplace: String? = ""

    var create=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_exercise)
        initialization()

    }

    private fun initialization() {

        hideNavigationBar()
        val windowBackground = window.decorView.background
        topBlurView.setupWith(containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        iv_back.setOnClickListener(this)
        apply_tv.setOnClickListener(this)

        FilterAdapter = FilterExerciseAdapter(getActivity(), exerciseList)
        exercise_rv.adapter = FilterAdapter

        if (intent.hasExtra("forReplace")) {
            forReplace = intent.getStringExtra("forReplace")
        }
        else{
            forReplace=""
        }

        create=  intent.getStringExtra("create")!!
        if (create!=null&&!create.isEmpty()){}
        else create=""

        if (intent.getSerializableExtra("list") != null) {
            list = intent.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
        }

        getFilterWorkoutData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                // setResult(101,intent)
                onBackPressed()
            }
            R.id.apply_tv -> {

                tempList.clear()
                for (i in 0..exerciseList.size - 1) {
                    for (j in 0..exerciseList.get(i).list.size - 1) {
                        if (exerciseList.get(i).list.get(j).isCheck)
                            tempList.add(exerciseList.get(i).list.get(j))
                    }
                }
                if (intent.getStringExtra("category_id") != null && intent.getStringExtra("category_id")!!.isEmpty()) {
/*
                    startActivity(
                        Intent(getActivity(), ExerciseDetailListActivity::class.java)
                            .putExtra("category_id", "").putExtra("list", tempList)
                    )
*/
                        startActivityForResult(
                            Intent(getActivity(), ExerciseDetailListActivityNew::class.java)
                                .putExtra("category_id", "").putExtra(
                                    "list",
                                    tempList
                                ).putExtra("create", create)
                                .putExtra("forReplace", forReplace), 103
                        )

                }
                else {
                    val intent = Intent()
                    intent.putExtra("list", tempList)
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                }
            }

        }
    }

    private fun getFilterWorkoutData() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.module_type, "")
        param.put(StringConstant.auth_customer_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().filterWorkoutApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                   // Constant.showCustomToast(getActivity(), "success..." + message)
                    val exerciseData =
                        getDataManager().mGson?.fromJson(response.toString(), FilterExerciseResponse::class.java)
                    exerciseList.addAll(exerciseData!!.data)
                    exerciseList.removeAt(exerciseList.size-1)
                    exerciseList.removeAt(exerciseList.size-1)

                    if (list != null && list.size > 0) {
                        for (k in 0..list.size - 1) {
                            for (i in 0..exerciseList.size - 1) {
                                for (j in 0..exerciseList.get(i).list.size - 1) {
                                    if (list.get(k).id.equals(exerciseList.get(i).list.get(j).id)) {
                                        exerciseList.get(i).list.get(j).isCheck = true
                                    }
                                }
                            }
                        }
                    }
                    FilterAdapter.notifyDataSetChanged()
                    Log.d("TAG", "response...list size..." + exerciseData.toString())

                } else Constant.showCustomToast(getActivity()!!, "fail..." + message)
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, getActivity()!!)
                Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (create.equals("create")) {
                data?.let {
                    selectedList.clear()
                    val list=it.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>?
                    list?.let {
                        selectedList.addAll(it)
                    }

                    val intent = Intent()
                    intent.putExtra("list", selectedList)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            }


           else if (requestCode == 103 && data != null) {

                if (data!!.getStringExtra("whenComeFromOut") != null && data.getStringExtra("whenComeFromOut").equals(
                        "yes",
                        true
                    )
                ) {
                    onBackPressed()
                }
            }
        }
    }

}
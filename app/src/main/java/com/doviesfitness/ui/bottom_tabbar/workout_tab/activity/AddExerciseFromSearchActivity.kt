package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentSearchLayoutBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SearchExerciseAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import java.util.ArrayList

class AddExerciseFromSearchActivity:BaseActivity(),View.OnClickListener, SearchExerciseAdapter.OnItemClick {
    override fun onItemClick(s: String, tempList: ArrayList<FilterExerciseResponse.Data.X>, s1: String, s2: String) {
        startActivityForResult(
            Intent(getActivity(), ExerciseDetailListActivityNew::class.java)
                .putExtra("category_id", "")
                .putExtra("forReplace", forReplace)
                .putExtra("list", tempList)
                .putExtra("from","search")
                .putExtra("create","create"),7)

    }

    lateinit var binding: FragmentSearchLayoutBinding
    lateinit var searchAdapter: SearchExerciseAdapter
    lateinit var exerciseList: ArrayList<FilterExerciseResponse.Data>
    lateinit var exercises: ArrayList<FilterExerciseResponse.Data.X>
    lateinit var exercises1: ArrayList<FilterExerciseResponse.Data.X>
    lateinit var currentFilter :Editable
    private var selectedList = ArrayList<ExerciseListingResponse.Data>()
    private var forReplace: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_search_layout)
        initialization()
    }

    private fun initialization() {

        if (intent.hasExtra("forReplace")) {
            forReplace = intent.getStringExtra("forReplace")!!
        }
        else{
            forReplace=""
        }
        binding.clearTxt.setOnClickListener(this)
        binding.cancelAction.setOnClickListener(this)
        binding.searchRv.setOnClickListener(this)
        binding.mainLayout.setOnClickListener(this)
        binding.statusLayout.visibility=View.VISIBLE
        var linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity());
        binding.searchRv.layoutManager = linearLayoutManager
        exerciseList = ArrayList<FilterExerciseResponse.Data>()
        exercises = ArrayList<FilterExerciseResponse.Data.X>()
        exercises1 = ArrayList<FilterExerciseResponse.Data.X>()
        exerciseList = getDataManager().getFilterExerciseList()!!
        searchAdapter = SearchExerciseAdapter(getActivity(), exercises,"create",this)
        binding.searchRv.adapter = searchAdapter
        if (exerciseList != null && exerciseList.size > 0) {
            for (i in 0..exerciseList.size - 1) {
                if (exerciseList.get(i).group_name.equals("Good For", true)) {
                    exerciseList.removeAt(i)
                    break
                }
            }
            for (i in 0..exerciseList.size - 1) {
                exercises.addAll(exerciseList.get(i).list)
            }


            searchAdapter.notifyDataSetChanged()
        } else {
            getFilterWorkoutData()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                currentFilter = editable
                if(currentFilter!=null&&currentFilter.toString().length>0)
                    binding.clearTxt.visibility= View.VISIBLE
                else
                    binding.clearTxt.visibility= View.GONE
                searchAdapter.getFilter().filter(currentFilter)
            }
        })

        binding.etSearch.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    startActivityForResult(
                        Intent(getActivity(), ExerciseDetailListActivityNew::class.java)
                            .putExtra("category_id", "").putExtra("list", "").putExtra("from", ""
                            ).putExtra("create", "create").putExtra("keyword",  ""+binding.etSearch.text.toString().trim()), 7
                    )

                    return true;
                }
                return false;
            }

        })


        Handler().postDelayed(
            {
                showKeyboard(binding.etSearch)
            }, 100
        )


     /*   binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                  Log.v("dsfdhs","chla bhai")
                }
                return false
            }
        })*/

    }

    fun showKeyboard(etSearch: EditText) {
        etSearch.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v = getCurrentFocus()
        if (v != null)
            imm!!.showSoftInput(v, 0)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.clear_txt->{
                binding.etSearch.setText("")
                hideKeyboard()
            }
            R.id.cancel_action->{ onBackPressed()
                hideKeyboard()}
            R.id.search_rv->{

            }
            R.id.main_layout->{

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
                    val exerciseData =
                        getDataManager().mGson?.fromJson(response.toString(), FilterExerciseResponse::class.java)
                    exerciseList.addAll(exerciseData!!.data)
                    getDataManager().saveFilterExerciseList(exerciseList)
                } else {
                }
            }

            override fun onError(anError: ANError) {

            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == Activity.RESULT_OK && data != null) {
            selectedList.clear()
            selectedList.addAll(data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>)
            val intent = Intent()
            intent.putExtra("list", selectedList)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}
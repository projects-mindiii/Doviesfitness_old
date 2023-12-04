package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentSearchLayoutBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SearchExerciseAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.view.KeyEvent
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ExerciseDetailListActivityNew


class SearchExerciseFragment : BaseFragment(), View.OnClickListener,
    SearchExerciseAdapter.OnItemClick {
    override fun onItemClick(
        s: String,
        tempList: ArrayList<FilterExerciseResponse.Data.X>,
        s1: String,
        s2: String
    ) {
        startActivityForResult(
            Intent(getActivity(), ExerciseDetailListActivityNew::class.java)
                .putExtra("category_id", "").putExtra("list", tempList).putExtra(
                    "from",
                    "search"
                ).putExtra("create", ""), 7
        )

    }

    lateinit var binding: FragmentSearchLayoutBinding
    lateinit var searchAdapter: SearchExerciseAdapter
    lateinit var exerciseList: ArrayList<FilterExerciseResponse.Data>
    lateinit var exercises: ArrayList<FilterExerciseResponse.Data.X>
    lateinit var exercises1: ArrayList<FilterExerciseResponse.Data.X>
    lateinit var currentFilter: Editable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_layout, container, false)
        initialisation()
        return binding.root
    }

    private fun initialisation() {

        binding.clearTxt.setOnClickListener(this)
        binding.mainLayout.setOnClickListener(this)
        binding.cancelAction.setOnClickListener(this)
        binding.searchRv.setOnClickListener(this)
        var linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext);
        binding.searchRv.layoutManager = linearLayoutManager
        exerciseList = ArrayList<FilterExerciseResponse.Data>()
        exercises = ArrayList<FilterExerciseResponse.Data.X>()
        exercises1 = ArrayList<FilterExerciseResponse.Data.X>()
        exerciseList = getDataManager().getFilterExerciseList()!!
        searchAdapter = SearchExerciseAdapter(mContext, exercises, "", this)
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
                if (currentFilter != null && currentFilter.toString().length > 0)
                    binding.clearTxt.visibility = View.VISIBLE
                else
                    binding.clearTxt.visibility = View.GONE
                searchAdapter.getFilter().filter(currentFilter)
            }
        })


        Handler().postDelayed(
            {
                showKeyboard(binding.etSearch)
            }, 100
        )

        binding.etSearch.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    startActivityForResult(
                        Intent(getActivity(), ExerciseDetailListActivityNew::class.java)
                            .putExtra("category_id", "").putExtra("list", "").putExtra("from", ""
                            ).putExtra("create", "").putExtra("keyword",  ""+binding.etSearch.text.toString().trim()), 7
                    )

                    return true;
                }
                return false;
            }

        })



   /*     binding.etSearch.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                 if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                     Log.v("erhfuydg","erhfuydg")

           // onSearchAction(v);
            return true;
        }
                return false
            }
        })*/

    }


    fun showKeyboard(etSearch: EditText) {
        etSearch.requestFocus()
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v = activity!!.getCurrentFocus()
        if (v != null)
            imm!!.showSoftInput(v, 0)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.clear_txt -> {
                binding.etSearch.setText("")
                hideKeyboard()
            }
            R.id.cancel_action -> {
                activity?.onBackPressed()
                hideKeyboard()
                hideNavigationBar()
            }

            R.id.main_layout -> {
                hideKeyboard()
            }
            R.id.search_rv -> {

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
        getDataManager().filterWorkoutApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("TAG", "filter response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val exerciseData =
                            getDataManager().mGson?.fromJson(
                                response.toString(),
                                FilterExerciseResponse::class.java
                            )
                        exerciseList.addAll(exerciseData!!.data)
                        getDataManager().saveFilterExerciseList(exerciseList)
                    } else {
                    }
                }

                override fun onError(anError: ANError) {}
            })
    }

}
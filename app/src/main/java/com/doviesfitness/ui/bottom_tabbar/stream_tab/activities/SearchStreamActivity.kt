package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.SearchStreamAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.TemplateSearchAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.popularStreamWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.SearchStreamModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamTagModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_fav_stream.fav_rv
import kotlinx.android.synthetic.main.activity_search_stream.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SearchStreamActivity :BaseActivity() , popularStreamWorkoutAdapter.OnItemCLick,View.OnClickListener,
    TemplateSearchAdapter.OnTagClick {

    private var streamList = ArrayList<SearchStreamModel.Settings.Data>()
    private var tagList = ArrayList<StreamTagModel.Settings.Data>()
    lateinit var adapter: SearchStreamAdapter
    lateinit var HAdapter:TemplateSearchAdapter
    private var mLastClickTime: Long = 0
    var tagIds=""
    var editableStr=""
    private var timer = Timer()
    private val DELAY: Long = 1000 // milliseconds
    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_search_stream)
        initialisation()
    }

    fun initialisation(){
        clear_txt.setOnClickListener(this)
        cancel.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        main_layout.setOnClickListener(this)
        var hLayoutManager= LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false)
        horizontal_rv.layoutManager=hLayoutManager
        HAdapter= TemplateSearchAdapter(getActivity(),tagList,this)
        horizontal_rv.adapter= HAdapter

        var layoutManager= androidx.recyclerview.widget.GridLayoutManager(getActivity(), 3);
        fav_rv.layoutManager=layoutManager
        adapter=SearchStreamAdapter(getActivity(),streamList,this)
        fav_rv.adapter= adapter
        val spacing = Constant.deviceSize(getActivity()) / 2

// apply spacing
        fav_rv.setPadding(spacing, spacing, spacing, spacing)
        fav_rv.setClipToPadding(false)
        fav_rv.setClipChildren(false)
        fav_rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })
        getSearchStream("","")
        getTagListApi()

/*
        et_search.setOnFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, focus: Boolean) {

                if (!focus){
                    hideKeyboard()
                }
             }
        })
*/

        et_search.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(editable: Editable?) {
                editableStr =  editable.toString()
                if (editableStr.length>0){
                    cancel.visibility=View.VISIBLE
                }
                else{
                    cancel.visibility=View.GONE
                }
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            if (!tagIds.isEmpty())
                            getSearchStream(editable.toString(),tagIds)
                            else
                            getSearchStream(editable.toString(),"")
                        }
                    },
                    DELAY
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        Handler().postDelayed(
            {
                showKeyboard(et_search)
            }, 100
        )

    }

    fun showKeyboard(etSearch: EditText) {
        etSearch.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v = getCurrentFocus()
        if (v != null)
            imm!!.showSoftInput(v, 0)
    }


    override fun onTagClick(pos: Int) {
        hideKeyboard()
        tagIds=""
        if (tagList.size>0){
            for (i in 0..tagList.size-1){
                if (tagList.get(i).isSelected){
                    tagIds=tagIds+tagList.get(i).iStreamTagId+","
                }
            }
        }
        if(tagIds.length>0 && tagIds.endsWith(","))
        {
            tagIds = tagIds.substring(0,tagIds.length - 1);
        }

        if (!tagIds.isEmpty()){
            clear_txt.visibility=View.VISIBLE
        }
        else{
            clear_txt.visibility=View.GONE
        }

        if (!editableStr.isEmpty())
        getSearchStream(editableStr,tagIds)
        else
        getSearchStream("",tagIds)

    }

    override fun onItemCLick(pos: Int, str: String, workoutID: String) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(getActivity(), StreamDetailActivity::class.java)
        var data= StreamDataModel.Settings.Data.RecentWorkout(streamList.get(pos).access_level,streamList.get(pos).display_new_tag,
            streamList.get(pos).display_new_tag_text,streamList.get(pos).stream_workout_access_level,"",
                streamList.get(pos).stream_workout_id,streamList.get(pos).stream_workout_image,streamList.get(pos).stream_workout_image_url,
                streamList.get(pos).stream_workout_name,"",streamList.get(pos).stream_workout_subtitle,"","")

        intent.putExtra("data", data)
        intent.putExtra("from", str)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back->{
                hideKeyboard()
                onBackPressed()
            }
            R.id.main_layout->{
                hideKeyboard()
            }
            R.id.cancel->{
                et_search.setText("")
                editableStr=""
                if (editableStr.isEmpty()&&tagIds.isEmpty())
                    txt_no_data_found.visibility=View.GONE
            }
            R.id.clear_txt->{
                tagIds=""
                if (tagList!=null&&tagList.size>0){
                    for (i in 0..tagList.size-1){
                        tagList.get(i).isSelected=false
                    }
                    HAdapter.notifyDataSetChanged()
                }
                clear_txt.visibility=View.GONE
                if (editableStr.isEmpty()&&tagIds.isEmpty())
                    txt_no_data_found.visibility=View.GONE


                getSearchStream(editableStr,"")
               // et_search.setText("")
                //editableStr=""
              //  hideKeyboard()
               // onBackPressed()
            }
        }
    }


    private fun getSearchStream(searchTerm:String,ids:String) {
        if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            val params = HashMap<String, String>()
            params.put("search_term", searchTerm)
            params.put("tag_ids", ids)
            var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType!=null&& !customerType!!.isEmpty()){
                params.put(StringConstant.auth_customer_subscription, customerType)
            }
            else{
                params.put(StringConstant.auth_customer_subscription, "free")
            }
            params.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_id
            )


            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            getDataManager().streamSearch( params,header)?.getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("TAG", "response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        streamList.clear()
                        txt_no_data_found.visibility=View.GONE
                        val StreamModel = getDataManager().mGson?.fromJson(response.toString(), SearchStreamModel::class.java)
                        streamList.addAll(StreamModel!!.settings.data)
                        adapter.notifyDataSetChanged()
                       // hideKeyboard()

                    } else {
                        streamList.clear()
                        adapter.notifyDataSetChanged()
                        if (searchTerm.isEmpty()&&ids.isEmpty())
                        txt_no_data_found.visibility=View.GONE
                        else
                        txt_no_data_found.visibility=View.VISIBLE
                       // Constant.showCustomToast(getActivity(), ""+message)
                       // hideKeyboard()
                    }
                }
                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })
        } else {
            Constant.showInternetConnectionDialog(getActivity())
        }
    }
    private fun getTagListApi() {
        if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            getDataManager().getStreamTagList( header)?.getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("TAG", "response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val streamTagModel = getDataManager().mGson?.fromJson(response.toString(), StreamTagModel::class.java)
                        tagList.addAll(streamTagModel!!.settings.data)
                        HAdapter.notifyDataSetChanged()

                    } else {

                    }
                }
                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })
        } else {
            Constant.showInternetConnectionDialog(getActivity())
        }
    }
}
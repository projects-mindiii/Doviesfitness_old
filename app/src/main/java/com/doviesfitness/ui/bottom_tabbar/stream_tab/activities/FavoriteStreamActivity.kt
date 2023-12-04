package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.FavStreamAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.popularStreamWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.FavStreamModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_fav_stream.*
import kotlinx.android.synthetic.main.activity_fav_stream.iv_back
import org.json.JSONObject

class FavoriteStreamActivity :BaseActivity() , popularStreamWorkoutAdapter.OnItemCLick,View.OnClickListener{
    private var favStreamList = ArrayList<FavStreamModel.Settings.Data.Favourite>()
    lateinit var adapter:FavStreamAdapter
    private var mLastClickTime: Long = 0
    override fun onResume() {
        super.onResume()

        if (favStreamList!=null)
            favStreamList.clear()
        hideNavigationBar()
        getFavWorkout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_fav_stream)
        initialisation()
    }

    fun initialisation(){
        iv_back.setOnClickListener(this)
        btn_explore.setOnClickListener(this)
        var layoutManager= androidx.recyclerview.widget.GridLayoutManager(getActivity(), 3)
        fav_rv.layoutManager=layoutManager
        adapter=FavStreamAdapter(getActivity(),favStreamList,this)
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

        swipe_refresh.setOnRefreshListener {
            favStreamList.clear()
            adapter.notifyDataSetChanged()
            getFavWorkout()
        }

    }





    override fun onItemCLick(pos: Int, str: String, workoutID: String) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(getActivity(), StreamDetailActivity::class.java)
        var data=
            StreamDataModel.Settings.Data.RecentWorkout(favStreamList.get(pos).access_level,favStreamList.get(pos).display_new_tag,favStreamList.get(pos).display_new_tag_text,"",
                favStreamList.get(pos).stream_workout_access_level, favStreamList.get(pos).stream_workout_id,favStreamList.get(pos).stream_workout_image,favStreamList.get(pos).stream_workout_image_url,
            favStreamList.get(pos).stream_workout_name,"",favStreamList.get(pos).stream_workout_subtitle,"","")

        intent.putExtra("data", data)
        intent.putExtra("from", str)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back->{
                onBackPressed()
            }
            R.id.btn_explore->{
                onBackPressed()
            }
        }
    }

    private fun getFavWorkout() {
        if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            val param = HashMap<String, String>()
            var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType!=null&& !customerType!!.isEmpty()){
                param.put(StringConstant.auth_customer_subscription, customerType)
            }
            else{
                param.put(StringConstant.auth_customer_subscription, "free")
            }
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_id
            )

            //  header.put("auth-token", "92fe82ff35704210abae42365bd96f9daea35cdfa09124239bde0dcfa07bc489")
            getDataManager().getFavStreamWorkout( header,param)?.getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    swipe_refresh.setRefreshing(false)
                    Log.d("TAG", "response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        no_data_found_layout.visibility=View.GONE
                        btn_explore.visibility=View.GONE
                        fav_rv.visibility=View.VISIBLE
                        val favStreamModel = getDataManager().mGson?.fromJson(response.toString(), FavStreamModel::class.java)
                        favStreamList.addAll(favStreamModel!!.settings.data.favourite_list)
                        adapter.notifyDataSetChanged()

                        if (favStreamList.size==0){
                            no_data_found_layout.visibility=View.VISIBLE
                            btn_explore.visibility=View.VISIBLE
                            fav_rv.visibility=View.GONE
                        }

                    } else {
                        no_data_found_layout.visibility=View.VISIBLE
                        btn_explore.visibility=View.VISIBLE
                        fav_rv.visibility=View.GONE
                    }
                }
                override fun onError(anError: ANError) {
                    swipe_refresh.setRefreshing(false)
                    Constant.errorHandle(anError, getActivity())
                }
            })
        } else {
            swipe_refresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(getActivity())
        }
    }
}
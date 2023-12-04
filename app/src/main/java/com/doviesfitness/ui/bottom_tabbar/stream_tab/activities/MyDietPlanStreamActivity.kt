package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.core.widget.NestedScrollView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentMyDietPlanBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.MyDietPlanAdapterExample
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.DeleteMyDietPlan
import com.doviesfitness.ui.createAndEditDietPlan.activity.create_DietPlanWebViewActivity
import com.doviesfitness.utils.*
import eightbitlab.com.blurview.RenderScriptBlur
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class MyDietPlanStreamActivity : BaseActivity(), MyDietPlanAdapterExample.MyDietPlanLIstener,
    DeleteMyDietPlan.MyDietPlanCallBack,
    View.OnClickListener {

    private var isAdmin: String = ""
    private var dataValue: Int = 0
    lateinit var binding: FragmentMyDietPlanBinding

    lateinit private var adapter: MyDietPlanAdapterExample
    private var myDietPlanList = ArrayList<MyDietPlan.Data>()
    private var page: Int = 0
    private var mLastClickTime: Long = 0
    private var nextPage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        binding = DataBindingUtil.setContentView(this,R.layout.fragment_my_diet_plan)
        initialization()
    }


    private fun initialization() {
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(getActivity()))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        binding.ivBack.setOnClickListener(this)
        binding.ivAdd.setOnClickListener(this)
        binding.containerId.setOnClickListener(this)
        binding.btnAddDietPlan.setOnClickListener(this)

        binding.myDpRv.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(binding.myDpRv.context, 2)
        binding.myDpRv.addItemDecoration(ItemOffsetDecoration(getActivity(), R.dimen._10sdp))

        if (isAdmin.equals("Yes")) {
            if (myDietPlanList.size > 0) {
                binding.ivAdd.visibility = View.VISIBLE
            }
        } else {
            binding.ivAdd.visibility = View.GONE
        }

        /* val layoutManager= GridLayoutManager(binding.myDpRv.context, 2)
         layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
             override fun getSpanSize(position: Int): Int {
                 return when (position) {
                     0 -> 1
                     else -> 2
                 }
             }
         }
         binding.myDpRv.layoutManager = layoutManager*/


        //adapter = MyDietPlanAdapter(mContext, myDietPlanList, this)
        adapter = MyDietPlanAdapterExample(getActivity(), myDietPlanList, this)
        binding.myDpRv.adapter = adapter

        //getDietPlanApi(page)

        //*Swipe to refresh*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._80sdp).toInt(),
                resources.getDimension(R.dimen._120sdp).toInt()
            )
        }
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 0
            myDietPlanList.clear()
            getDietPlanApi(page)
        })

        binding.myDpMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage == 1) {
                            adapter.notifyDataSetChanged()
                            getDietPlanApi(page)
                        }
                    }
                }
            }
        })
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                EventBus.getDefault().post("Yes");
                onBackPressed()
            }


            R.id.container_id -> {
                hideNavigationBar()
                val view = window.decorView
                view.systemUiVisibility =
                    view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            R.id.iv_add -> {
                val homeTabintent = Intent(getActivity(), HomeTabActivity::class.java)
                homeTabintent.putExtra("fromMyDietPlanActvity", "fromMyDietPlanActvity")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeTabintent)
            }

            R.id.btn_add_dietPlan -> {
                val homeTabintent = Intent(getActivity(), HomeTabActivity::class.java)
                homeTabintent.putExtra("fromMyDietPlanActvity", "fromMyDietPlanActvity")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeTabintent)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        page = 0
        myDietPlanList.clear()
        getDietPlanApi(page)
        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }


    private fun getDietPlanApi(pageCount: Int) {

        if (CommanUtils.isNetworkAvailable(getActivity())!!) {

            val param = HashMap<String, String>()
            param.put(StringConstant.device_token, "")
            param.put(StringConstant.page_index, "" + pageCount)
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getCustomerDietAPi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val status: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        val count: String? = jsonObject?.getString(StringConstant.count)
                        val strNextPage: String? = jsonObject?.getString(StringConstant.next_page)
                        nextPage = strNextPage!!.toInt()
                        val countInt = count!!.toInt()
                        Log.d("response", "nextPage......" + response)
                        if (status!!.equals("1")) {

                            if (pageCount == 0) {
                                myDietPlanList.clear()
                                adapter.notifyDataSetChanged()
                            }
                            //myDietPlanList.clear()
                            binding.noDietPlanFound.visibility = View.GONE
//                            binding.noRecordFound.visibility = View.GONE
                            binding.swipeRefresh.setRefreshing(false)
                            val myDietPlan = getDataManager().mGson?.fromJson(
                                response.toString(),
                                MyDietPlan::class.java
                            )
                            myDietPlanList.addAll(myDietPlan!!.data)
                            adapter.notifyDataSetChanged()


                            if (myDietPlanList.size > 0) {
                                binding.ivAdd.visibility = View.VISIBLE
                            } else {
                                binding.ivAdd.visibility = View.GONE
                            }
                        } else {
                            if (myDietPlanList.size == 0 && countInt == 0) {
                                binding.swipeRefresh.setRefreshing(false)
                                binding.ivAdd.visibility = View.GONE
                                binding.noDietPlanFound.visibility = View.VISIBLE
//                                binding.noRecordFound.visibility = View.GONE
                            }
                            binding.swipeRefresh.setRefreshing(false)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError,getActivity())
                        //binding.noRecordFound.visibility = View.VISIBLE
                    }
                })
        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(getActivity())
        }
    }

    override fun getDietPlanInfo(data: MyDietPlan.Data, position: Int, whichClick: String) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        // this velue send from adapters india 0 and 1 forms
        // when equal to 1 means you can delete on 0 you can go other fragments
       if (whichClick.equals("0")) {
          /*  val dietPlanWebViewFragment = DietPlanWebViewFragment()
            val args = Bundle()
            args.putParcelable("webViewData", data)
            dietPlanWebViewFragment.setArguments(args)
            getBaseActivity()?.addFragment(dietPlanWebViewFragment, R.id.container_id1, true)
*/

          var Mdata= DietPlanSubCategoryModal.Data.DietListing(data.diet_plan_access_level,"","",
              "",data.diet_plan_image,data.diet_plan_id,data.diet_plan_image,data.diet_plan_pdf,"",
              data.diet_plan_title,"")
           val intent = Intent(this, create_DietPlanWebViewActivity::class.java)
           intent.putExtra("weburl", Mdata)
           startActivity(intent)
        }

        if (whichClick.equals("1")) {
            DeleteMyDietPlan.newInstance(this, data, position).show(supportFragmentManager)
        }
    }


    override fun textOnClick1(type: String, id: MyDietPlan.Data, pos: Int) {

        if (type.equals(getString(R.string.delete))) {
            showDeleteDialog(getActivity(), id.diet_plan_id, pos)
        } else {
            //Delete Dialog
            Constant.showCustomToast(getActivity(), type)
        }
    }

    /*.............................//Custom dialog for Delete....................................*/
    fun showDeleteDialog(activity: Activity, id: String, pos: Int) {
        val dialog = Dialog(activity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.delete_diet_plan_view)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteDietPlan(id, pos)
            dialog.dismiss()
        }

        dialog.show()
    }

    /*Delete comment api*/
    private fun deleteDietPlan(id: String, pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.diet_plan_id, id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteMyDietAPi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            myDietPlanList.removeAt(pos)
                            adapter.notifyDataSetChanged()

                            if (myDietPlanList.size == 0) {
                                getDietPlanApi(page)
                            }

                            val toast = Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        } else {
                            Constant.showCustomToast(getActivity(), "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })
    }
}

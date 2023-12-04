package com.doviesfitness.ui.bottom_tabbar.diet_plan

import android.app.Activity
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.core.widget.NestedScrollView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentDietPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.NewDietPlanCategoryAdapter
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPCategoryDetailFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject


class DietPlanFragment : BaseFragment(), View.OnClickListener,
    NewDietPlanCategoryAdapter.DietPlanCategoryListener {

    lateinit var binding: FragmentDietPlanBinding
    private var dietPLanCategoryList = ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>()
    //lateinit var adapter: DietPlanCategoryAdapter
    lateinit var adapter: NewDietPlanCategoryAdapter
    private var page: Int = 0
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    private var fromAddDietPlan: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diet_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        //fretgh
        hideNavigationBar()
        binding.ivNavigationDiet.setOnClickListener(this)
        binding.containerId.setOnClickListener(this)

        arguments?.let {
            fromAddDietPlan = arguments!!.getString("FromAddDietPlan")
        }

        arguments?.let {
            val module_id = arguments!!.getString("module_id", "")
            if (!module_id.isEmpty()) {
                val dietPsubcategory = DietPCategoryDetailFragment()
                val args = Bundle()
                args.putString("module_id", module_id)
                dietPsubcategory.setArguments(args)
                getBaseActivity()?.addFragment(dietPsubcategory, R.id.container_id1, true)
            }
        }

        //getDietPlanApi(page)



        if (fromAddDietPlan != null) {
            fromAddDietPlan = "0"
        } else {
            fromAddDietPlan = ""
        }

         val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
         binding.dietPlanRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        adapter = NewDietPlanCategoryAdapter(binding.dietPlanRv.context, dietPLanCategoryList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.dietPlanRv.layoutManager = layoutManager1

        binding.dietPlanRv.adapter = adapter

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
            dietPLanCategoryList.clear()
            getDietPlanApi(page)
        })


        binding.dpMain.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage > 0) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getDietPlanApi(page)
                        }
                    }
                }
            }
        })

        ///********Pagination Feed List*********////
        binding.dietPlanRv.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager1) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: androidx.recyclerview.widget.RecyclerView
            ) {
                if (page != 0) {
                    if (page != 0 && nextPage > 0) {
                        adapter.showLoading(true)
                        adapter.notifyDataSetChanged()
                        getDietPlanApi(page)

                    }
                }
            }
        })
    }

    // to get data  when we come from notification
    fun newInstance(module_id: String): DietPlanFragment {
        val myFragment = DietPlanFragment()
        val args = Bundle()
        args.putString("module_id", module_id)
        myFragment.setArguments(args)

        return myFragment
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_navigation_diet -> {
                if ((activity as HomeTabActivity).drawer_layout.isDrawerOpen(Gravity.START)) {
                    (activity as HomeTabActivity).drawer_layout.closeDrawer(Gravity.START)
                } else {
                    (activity as HomeTabActivity).drawer_layout.openDrawer(Gravity.START)
                }
            }

            R.id.container_id -> {
                hideNavigationBar()
            }
        }
    }

    private fun getDietPlanApi(pageIndex: Int) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            val param = HashMap<String, String>()

            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.device_token,
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
            )
            param.put(StringConstant.page_index, "" + pageIndex)

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().dietPlanCategoriesListApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val next_page = json?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)

                        if (status!!.equals("1")) {
                            if(pageIndex == 0){
                                dietPLanCategoryList.clear()
                                adapter.notifyDataSetChanged()
                            }
                            binding.swipeRefresh.setRefreshing(false)
                            //dietPLanCategoryList.clear()
                            val dietPlanCateGoriesModal =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    DietPlanCateGoriesModal::class.java
                                )
                            dietPLanCategoryList.addAll(dietPlanCateGoriesModal!!.data.diet_plan_categories);



                             hideFooterLoiader()

                        } else {
                            //Constant.showCustomToast(context!!, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideFooterLoiader()
                        Constant.errorHandle(anError, mContext as Activity)
                    }
                })
        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }


    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }

    // get data onclick of dietCategory
    override fun getDietCategoryDetailsInfo(
        data: DietPlanCateGoriesModal.Data.DietPlanCategory,
        position: Int
    ) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val dietPsubcategory = DietPCategoryDetailFragment()
        val args = Bundle()
        args.putParcelable("data", data)
        args.putString("fromAddDietPlan", fromAddDietPlan)
        dietPsubcategory.setArguments(args)
        getBaseActivity()?.addFragment(dietPsubcategory, R.id.container_id1, true)
    }

    override fun onResume() {
        super.onResume()
        dietPLanCategoryList.clear()
        getDietPlanApi(page)
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
                outRect.bottom = space * 6
            } else {
                outRect.bottom = 0
            }
        }
    }
}









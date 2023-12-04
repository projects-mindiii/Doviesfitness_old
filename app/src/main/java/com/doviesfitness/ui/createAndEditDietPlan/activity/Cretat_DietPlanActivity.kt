package com.doviesfitness.ui.createAndEditDietPlan.activity

import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.DietPlanCategoryAdapter
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPCategoryDetailFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject

class Cretat_DietPlanActivity : BaseActivity(), View.OnClickListener, DietPlanCategoryAdapter.DietPlanCategoryListener {

    private lateinit var binding: com.doviesfitness.databinding.ActivityCretatDietPlanBinding
    private var addDietPlanList = ArrayList<DietPlanSubCategoryModal.Data.DietListing>()
    private var dietPLanCategoryList = ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>()
    lateinit var adapter: DietPlanCategoryAdapter
    private var page: Int = 1
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    private var fromAddDietPlan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cretat__diet_plan)
        initialization()
    }

    private fun initialization() {
        hideNavigationBar()

        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        binding.ivBack.setOnClickListener(this)
        if (intent.getStringExtra("FromAddDietPlan") != null) {
            fromAddDietPlan = intent.getStringExtra("FromAddDietPlan")!!
            if (!fromAddDietPlan.isEmpty()) {
                fromAddDietPlan = "0"
            }
        } else if (intent.getParcelableArrayListExtra<DietPlanSubCategoryModal.Data.DietListing>("addDietPlanList") != null) {
            addDietPlanList = intent.getParcelableArrayListExtra<DietPlanSubCategoryModal.Data.DietListing>("addDietPlanList")!!
            fromAddDietPlan = "0"
        }

        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.dietPlanRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        adapter = DietPlanCategoryAdapter(binding.dietPlanRv.context, dietPLanCategoryList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.dietPlanRv.layoutManager = layoutManager1

        binding.dietPlanRv.adapter = adapter
        getDietPlanApi(page)

        //*Swipe to refresh*//*
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 1
            dietPLanCategoryList.clear()
            getDietPlanApi(page)
        })

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager1) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView) {

                nextPage += 1
                getDietPlanApi(nextPage)
            }
        }
        binding.dietPlanRv.addOnScrollListener(scrollListener)
    }

    private fun getDietPlanApi(pageIndex: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
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
                        binding.swipeRefresh.setRefreshing(false)
                        dietPLanCategoryList.clear()
                        val dietPlanCateGoriesModal =
                            getDataManager().mGson?.fromJson(response.toString(), DietPlanCateGoriesModal::class.java)
                        dietPLanCategoryList.addAll(dietPlanCateGoriesModal!!.data.diet_plan_categories);
                        adapter.notifyDataSetChanged()

                    } else {
                        Constant.showCustomToast(this@Cretat_DietPlanActivity, "" + msg)
                    }

                    //please check this
                    if (nextPage != 0) {
                        page = page + 1
                        getDietPlanApi(page)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@Cretat_DietPlanActivity)
                }
            })
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    // get data onclick of dietCategory
    override fun getDietCategoryDetailsInfo(data: DietPlanCateGoriesModal.Data.DietPlanCategory, position: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        // when we come from crate plan then use this container id // R.id.diet_container_id //

        if (addDietPlanList.size != 0) {
            val dietPsubcategory = DietPCategoryDetailFragment()
            val args = Bundle()
            args.putParcelable("data", data)
            args.putString("fromAddDietPlan", fromAddDietPlan)
            args.putParcelableArrayList("addDietPlanList", addDietPlanList)
            dietPsubcategory.setArguments(args)
            addFragment(dietPsubcategory, R.id.diet_container_id, true)
        } else {
            val dietPsubcategory = DietPCategoryDetailFragment()
            val args = Bundle()
            args.putParcelable("data", data)
            args.putString("fromAddDietPlan", fromAddDietPlan)
            dietPsubcategory.setArguments(args)
            addFragment(dietPsubcategory, R.id.diet_container_id, true)
        }
    }

    class MySpacesItemDecoration(space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.bottom = space
            } else {
                outRect.bottom = 0
            }
        }
    }
}

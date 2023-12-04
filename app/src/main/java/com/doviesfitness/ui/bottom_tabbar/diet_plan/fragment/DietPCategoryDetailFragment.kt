package com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment

import android.app.Activity
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import androidx.core.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentDietPCategoryNewDetailBinding
import com.doviesfitness.databinding.FragmentDietPcategoryDetailBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.DietPalnSubCategoryAdapter
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.ItemOffsetDecoration
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject

class DietPCategoryDetailFragment : BaseFragment(), View.OnClickListener,
    DietPalnSubCategoryAdapter.DietPSubCategoryListener {


    private var isAdmin: String = ""
    private lateinit var dietPlanSubCatgory: DietPlanCateGoriesModal.Data.DietPlanCategory
    private var dietPSubCategoryList = ArrayList<DietPlanSubCategoryModal.Data.DietListing>()
    lateinit var adapter: DietPalnSubCategoryAdapter
    lateinit var binding: FragmentDietPCategoryNewDetailBinding
    private var adddietPLanCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>? = null

    private var page: Int = 0
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    private var fromAddDietPlan: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diet_p_category_new_detail, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        binding.ivBack.setOnClickListener(this)

        //workout_group_id = arguments!!.getString("workout_group_id")
        dietPlanSubCatgory = (arguments!!.getParcelable("data") as? DietPlanCateGoriesModal.Data.DietPlanCategory)!!
        fromAddDietPlan = arguments!!.getString("fromAddDietPlan") as String

        // when list has item then get also list for check status otherwise null
        adddietPLanCategoryList =
            arguments!!.getParcelableArrayList<DietPlanSubCategoryModal.Data.DietListing>("addDietPlanList")

        if (fromAddDietPlan.equals("0")) {
            fromAddDietPlan = "0"
        } else {
            fromAddDietPlan = ""
        }

        if (!dietPlanSubCatgory.diet_plan_category_id.isEmpty()) {
            binding.dpTitleName.setText(dietPlanSubCatgory.diet_plan_category_name)
            //getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_category_id, page, "0")
        }

        binding.dpSubCategoryRv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.dpSubCategoryRv.context, 2)
      //   binding.dpSubCategoryRv.addItemDecoration(ItemOffsetDecoration(mContext, R.dimen._5sdp))

        adapter = DietPalnSubCategoryAdapter(context!!, dietPSubCategoryList, this, isAdmin)
        binding.dpSubCategoryRv.adapter = adapter

        /*Swipe to refresh*/
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            //binding.swipeRefresh.setRefreshing(false)
            page = 0
            dietPSubCategoryList.clear()
            getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_category_id, page, "1")
        })

        binding.dpcdMain.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage > 0) {
                            //adapter.showLoading(true)
                            getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_category_id, page, "0")
                        }
                    }
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        dietPSubCategoryList.clear()
        page = 0
        getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_category_id, page, "0")
    }

    private fun getDietPlanDetailsApi(diet_plan_category_id: String, pageIndex: Int, whenRefresh: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            if (whenRefresh.equals("1")) {
                binding.loader.visibility = View.GONE
            } else {
                binding.loader.visibility = View.VISIBLE
            }

            val param = HashMap<String, String>()

            param.put(StringConstant.device_token, "")
            param.put(StringConstant.diet_plan_category_id, diet_plan_category_id)
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
            var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType!=null&& !customerType!!.isEmpty()){
                param.put(StringConstant.auth_customer_subscription, customerType)
            }
            else{
                param.put(StringConstant.auth_customer_subscription, "Paid")
            }
            param.put(StringConstant.page_index, "" + pageIndex)
            param.put(StringConstant.device_id, "")
            param.put(StringConstant.page_index, "1")

            val header = HashMap<String, String>()
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiVersion, "1")

            getDataManager().dietPlanApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            if(pageIndex == 0){
                                dietPSubCategoryList.clear()
                                adapter.notifyDataSetChanged()
                            }
                            binding.loader.visibility = View.GONE

                            binding.swipeRefresh.setRefreshing(false)
                            binding.noRecordFound.visibility = View.GONE
                            val dietPlanSubCateGoriesModal =
                                getDataManager().mGson?.fromJson(response.toString(), DietPlanSubCategoryModal::class.java)

                            if (!dietPlanSubCateGoriesModal!!.data.diet_static_content[0].content.isEmpty()) {
                                binding.txtDietDiscription.visibility = View.VISIBLE
                                binding.txtDietDiscription.setText(dietPlanSubCateGoriesModal.data.diet_static_content[0].content)
                            } else {
                                binding.txtDietDiscription.visibility = View.GONE
                            }

                            binding.dpTitleName.setText(dietPlanSubCateGoriesModal.data.diet_static_content[0].title)

                            dietPSubCategoryList.addAll(dietPlanSubCateGoriesModal.data.diet_listing);
                            adapter.notifyDataSetChanged()

                        } else {
                            //Constant.showCustomToast(context!!, "" + msg)
                            binding.swipeRefresh.setRefreshing(false)
                            binding.noRecordFound.visibility = View.VISIBLE
                            binding.loader.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        //Constant.errorHandle(anError, activity)
                        binding.loader.visibility = View.GONE
                    }
                })
        }else{
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
        }
    }


    override fun getDietPCategoryDetailsInfo(data: DietPlanSubCategoryModal.Data.DietListing, position: Int) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }


        // when we come from crate plan then use this container id // R.id.diet_container_id //
        if (fromAddDietPlan.equals("0")) {

            if (adddietPLanCategoryList != null && adddietPLanCategoryList?.size != 0) {
                val dietDetailFragment = DietDetailFragment()
                val args = Bundle()
                args.putParcelable("data", data)
                args.putString("fromAddDietPlan", fromAddDietPlan)
                args.putParcelableArrayList("addDietPlanList", adddietPLanCategoryList)
                dietDetailFragment.setArguments(args)
                getBaseActivity()?.addFragment(dietDetailFragment, R.id.diet_container_id, true)
            } else {
                val dietDetailFragment = DietDetailFragment()
                val args = Bundle()
                args.putParcelable("data", data)
                args.putString("fromAddDietPlan", fromAddDietPlan)
                dietDetailFragment.setArguments(args)
                getBaseActivity()?.addFragment(dietDetailFragment, R.id.diet_container_id, true)
            }

        } else {
            // when we come from crate plan then use this container id // R.id.container_id1 //
            val dietDetailFragment = DietDetailFragment()
            val args = Bundle()
            args.putParcelable("data", data)
            args.putString("fromAddDietPlan", fromAddDietPlan)
            dietDetailFragment.setArguments(args)
            getBaseActivity()?.addFragment(dietDetailFragment, R.id.container_id1, true)
        }
    }

    class MyItemDecoration(space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space / 2
        }


        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position == 0 || position == 1) {
            } else {
                outRect.top = space * 2
            }
            if (position % 2 == 0) {
                outRect.right = space
            } else {
                outRect.left = space
            }

            if (position == parent.getAdapter()!!.getItemCount() - 1 || position == parent.getAdapter()!!.getItemCount() - 2) {
                outRect.bottom = 12
            } else {
                outRect.bottom = 0
            }
        }
    }
}

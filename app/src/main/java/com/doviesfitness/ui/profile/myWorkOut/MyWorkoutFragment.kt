package com.doviesfitness.ui.profile.myWorkOut

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentMyWorkoutBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ActivityGoodFor
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.CreateWorkoutActivity
import com.doviesfitness.ui.profile.myWorkOut.adapter.NewWorkoutListAdapter
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.dialog_delete.*
import org.json.JSONObject

class MyWorkoutFragment : BaseFragment(), View.OnClickListener,
    NewWorkoutListAdapter.MyWorkOutDeletAndRedirectListener, SaveEditWorkoutDialog.CommentCallBack {

    private var intpos: Int = 0
    private lateinit var newWorkoutModal: WorkOutListModal.Data
    public var isAdmin: String = ""
    lateinit var binding: FragmentMyWorkoutBinding
    lateinit var adapter: NewWorkoutListAdapter
    var workOutList = ArrayList<WorkOutListModal.Data>()
    private var page: Int = 1
    private var nextPage: Int = 0
    private var mLastClickTime: Long = 0
    var idPos = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = activity!!.window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_workout, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.loader.visibility = View.VISIBLE
        val windowBackground = activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        setOnClick(binding.ivBack, binding.ivAdd, binding.btnAddWorkout, binding.containerId)
        page = 1
        workOutList.clear()
        adapter = NewWorkoutListAdapter(mContext, workOutList, this, isAdmin)
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.myWorkoutRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        binding.myWorkoutRv.adapter = adapter

        getWorkOutApi(page, "1")


        // Swipe to refresh
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._80sdp).toInt(),
                resources.getDimension(R.dimen._120sdp).toInt()
            )
        }

        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            page = 1
            workOutList.clear()
            getWorkOutApi(page, "1")
        })

        binding.myWorkoutMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v!!.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    page += 1
                    if (page != 0 && nextPage == 1) {
                        adapter.showLoading(true)
                        adapter.notifyDataSetChanged()
                        getWorkOutApi(page, "1")
                    }
                }
            }
        })
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

/*
    override fun onResume() {
        super.onResume()
        workOutList.clear()
        getWorkOutApi(page, "0")
    }
*/

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }

            R.id.container_id -> {
                hideNavigationBar()
                val view = activity!!.window.decorView
                view.systemUiVisibility =
                    view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            R.id.iv_add -> {
/*
                startActivity(
                    Intent(getActivity(), CreateWorkoutActivity::class.java).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "").putExtra("myWorkoutFragment", "myWorkoutFragment")
                )
*/
                startActivityForResult(
                    Intent(activity, CreateWorkoutActivity::class.java).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "").putExtra("myWorkoutFragment", "myWorkoutFragment")
               ,1234 )
            }
            R.id.btn_add_workout -> {
/*
                startActivity(
                    Intent(getActivity(), CreateWorkoutActivity::class.java).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "").putExtra("myWorkoutFragment", "myWorkoutFragment")
                )
*/

                startActivityForResult(
                    Intent(activity, CreateWorkoutActivity::class.java).putExtra("edit", "")
                        .putExtra("fromDeepLinking", "").putExtra("myWorkoutFragment", "myWorkoutFragment")
                    ,1234 )


            }
        }
    }

    private fun getWorkOutApi(pageCount: Int, fromRefresh: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            if (fromRefresh.equals("1")) {
                binding.loader.visibility = View.GONE
            } else {
                binding.loader.visibility = View.VISIBLE
            }

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

            getDataManager().getCustomerWorkoutAPi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val status: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        val next_page = jsonObject?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()

                        if (status!!.equals("1")) {
                            if(pageCount == 0){
                                workOutList.clear()
                            }
                            binding.loader.visibility = View.GONE
                            binding.noPlanFound.visibility = View.GONE
                            binding.swipeRefresh.isRefreshing = false
                            val workOutListModal = getDataManager().mGson?.fromJson(
                                response.toString(),
                                WorkOutListModal::class.java
                            )

                            workOutList.addAll(workOutListModal!!.data);
                            hideFooterLoiader()
                        }

                        if (workOutList.size == 0 && workOutList.isEmpty()) {
                            binding.swipeRefresh.isRefreshing = false
                            binding.noPlanFound.visibility = View.VISIBLE
                            binding.loader.visibility = View.GONE
                        }

                        if (workOutList.size > 0) {
                            binding.ivAdd.visibility = View.VISIBLE
                        } else {
                            binding.ivAdd.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideFooterLoiader()
                        Constant.errorHandle(anError, mContext as Activity)
                        //binding.loader.visibility = View.GONE
                    }
                })
        } else {
            binding.swipeRefresh.isRefreshing = false
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }


    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }

    override fun getWorkOutData(data: WorkOutListModal.Data, whichClick: String, pos: Int) {

        intpos = pos

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        if (whichClick == "0") {
            showDeleteDialog(mContext as Activity, data, pos)
        } else {
            //here we come from workout plan to see exesciting plan in list that why
            if (data.workout_access_level == "LOCK") {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    ) &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "No",
                        true
                    )
                ) {
                    startActivity(
                        Intent(
                            activity,
                            SubscriptionActivity::class.java
                        ).putExtra("home", "no")
                    )
                } else {
                    val intent = Intent(mContext, WorkOutDetailActivity::class.java)
                    intent.putExtra("PROGRAM_DETAIL", data.workout_id)
                    intent.putExtra("From_WORKOUTPLAN", "Workout")
                    intent.putExtra("isMyWorkout", "yes")
                        .putExtra("myWorkoutFragment", "myWorkoutFragment")
                    intent.putExtra("fromDeepLinking", "")
                    startActivityForResult(intent, 20);// Activity is started with requestCode 2
                }
            } else {
                val intent = Intent(mContext, WorkOutDetailActivity::class.java)
                intent.putExtra("PROGRAM_DETAIL", data.workout_id)
                intent.putExtra("From_WORKOUTPLAN", "Workout")
                intent.putExtra("isMyWorkout", "yes")
                    .putExtra("myWorkoutFragment", "myWorkoutFragment")
                intent.putExtra("fromDeepLinking", "")
                //mContext.startActivity(intent)
                startActivityForResult(intent, 20);// Activity is started with requestCode 2
            }
        }
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if (data != null) {
            var message = ""
            if (data.hasExtra("MESSAGE"))
             message = data.getStringExtra("MESSAGE")!!
            if (data.hasExtra("myFrag")) {
                var myFrag = data.getStringExtra("myFrag")!!

                if (!myFrag.isEmpty() && myFrag == "myFrag") {
                    page=1
                    workOutList.clear()
                    getWorkOutApi(page, "1")
                }
            }
            if (message.isNotEmpty()) {
                workOutList.removeAt(intpos)
                adapter.notifyDataSetChanged()
            }
        }
    }

    /*Delete comment api*/
    private fun deleteSavePost(data: WorkOutListModal.Data, pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.workout_id, data.workout_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            workOutList.removeAt(pos)
                            adapter.notifyDataSetChanged()

                            if (workOutList.size == 0) {
                                page=1
                                getWorkOutApi(page, "1")
                            }
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    /*.............................//Custom dialog for Delete....................................*/
    fun showDeleteDialog(activity: Activity, data: WorkOutListModal.Data, pos: Int) {
        val dialog = Dialog(activity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)
        dialog.tv_header.setText("Are you sure you want to delete this Workout?")
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteSavePost(data, pos)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.splash_screen_color
                )
            )
        }
    }

    override fun publishWorkout(pos: Int, workoutModal: WorkOutListModal.Data) {
        idPos = pos
        newWorkoutModal = workoutModal

        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes",
                true
            )
        ) {
            SaveEditWorkoutDialog.newInstance("publish", this)
                .show(activity!!.supportFragmentManager)
        }
    }

    override fun textOnClick1(type: String) {
        ///////publish
        val intent = Intent(getActivity(), ActivityGoodFor::class.java)
        intent.putExtra("from", "publish")
        intent.putExtra("workoutId", workOutList.get(idPos).workout_id)
        startActivity(intent)
    }

    override fun overwriteClick(type: String) {
        ///////////delete////////////////
        showDeleteDialog(idPos, newWorkoutModal)
    }

    fun showDeleteDialog(pos: Int, data: WorkOutListModal.Data) {
        val dialog = Dialog(mContext)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        dialog.tv_header.setText("Are you sure you want to delete this Workout?")
        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteWorkoutPlan(pos, data)
            dialog.dismiss()
        }

        dialog.show()
    }

    // this Api only call in Admin case
    private fun deleteWorkoutPlan(pos: Int, data: WorkOutListModal.Data) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("workout_id", data.workout_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiVersion, "1")
        header.put(StringConstant.apiKey, "HBD")

        getDataManager().deleteMyWorkOut(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            workOutList.removeAt(pos)
                            adapter.notifyDataSetChanged()
                            //workOutList.clear()
                            //getWorkOutApi(page, "0")
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
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

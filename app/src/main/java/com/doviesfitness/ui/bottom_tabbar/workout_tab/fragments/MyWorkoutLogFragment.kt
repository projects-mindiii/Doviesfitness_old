package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentMyNewWorkoutlogBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.WorkoutLogActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.MyWorkoutLogAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.WorkoutLogEditModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.MyWorkoutLogModel
import com.doviesfitness.ui.date_picker.DatePickerPopWin
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.filter_fitness_dialog_view.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyWorkoutLogFragment : BaseFragment(), View.OnClickListener, MyWorkoutLogAdapter.OnNoteClick,
    SaveEditWorkoutDialog.CommentCallBack  {
    private var intpos: Int = 0
    lateinit var binding: FragmentMyNewWorkoutlogBinding
    lateinit var adapter: MyWorkoutLogAdapter
    var workOutList = ArrayList<MyWorkoutLogModel.Data>()
    private var page: Int = 1
    private var nextPage: Int = 0
    lateinit var dialog: Dialog
    private var width: Int = 0
    private var mLastClickTime: Long = 0
    private var editDeletePosition: Int = 0
    var unitStr = ""
    var logDate = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = activity!!.window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_new_workoutlog, container, false)
        initialization()
        return binding.root
    }

/*
    override fun onResume() {
        super.onResume()
        page=0
        workOutList.clear()
        adapter.notifyDataSetChanged()
        getWorkOutApi(page, "0")
    }
*/

    private fun initialization() {
        val windowBackground = activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        setOnClick(binding.ivBack, binding.ivAdd, binding.containerId,binding.btnCalenderClearFilter)
     //   binding.ivAdd.visibility = View.GONE
        binding.mWTitleName.text = getString(R.string.my_workout_log)

        Glide.with(activity!!).load(R.drawable.date_ico).into( binding.ivAdd)
        unitStr = getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
        page=1
        workOutList.clear()
        adapter = MyWorkoutLogAdapter(activity!!, workOutList, this, unitStr)
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.myWorkoutRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        binding.myWorkoutRv.adapter = adapter

        val displaymetrics = DisplayMetrics()
        activity!!.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels
        logDate=""
        getWorkOutApi(page, "0",logDate)

        /*Swipe to refresh*/
        //*Swipe to refresh*//*
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
            adapter.notifyDataSetChanged()
//            logDate=""
            getWorkOutApi(page, "1",logDate)
        })

        binding.myWorkoutMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage == 1) {
                            if (page != 0 && nextPage == 1) {
                                adapter.showLoading(true)
                                adapter.notifyDataSetChanged()
                                getWorkOutApi(page, "1",logDate)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onNoteClick(workoutDescription: String) {
        NotesDialog(workoutDescription)
    }


    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }
            R.id.btn_calender_clear_filter->{
                logDate=""
                getWorkOutApi(page, "1",logDate)
            }
            R.id.iv_add -> {
                var cal = Calendar.getInstance()
                var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                var str = dateFormat.format(cal.getTime())

                openDatePickerDialog(str)
            }

            R.id.container_id -> {
                hideNavigationBar()
                val view = activity!!.window.decorView
                view.systemUiVisibility =
                    view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

        }
    }

    override fun getWorkOutLogData(data: MyWorkoutLogModel.Data, whichClick: String, pos: Int) {

        intpos = pos

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(context, WorkOutDetailActivity::class.java)
        intent.putExtra("PROGRAM_DETAIL", data.workout_id)
        intent.putExtra("From_WORKOUTPLAN", "")
        intent.putExtra("isMyWorkout", "yes")
        intent.putExtra("fromDeepLinking", "")
        //mContext.startActivity(intent)
        startActivityForResult(intent, 20);// Activity is started with requestCode 2
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 20) {
            if (data != null) {
                val message = data.getStringExtra("MESSAGE")
                if (!message!!.isEmpty()) {
                    workOutList.removeAt(intpos)
                    adapter.notifyDataSetChanged()
                }
            }
        }
       else if (requestCode == 30 && resultCode==Activity.RESULT_OK) {
            if (data != null) {
                val item = data.getSerializableExtra("item") as WorkoutLogEditModel.Data
                var log=  workOutList.get(editDeletePosition)
                log.customer_calorie= item.customer_calorie
                log.customer_weight= item.customer_weight
                log.feedback_status= item.feedback_status
                log.log_date= item.log_date
                log.workout_description= item.workout_description
                log.workout_id= item.workout_id
                log.workout_log_id= item.workout_log_id
                log.workout_image= item.workout_image
                log.workout_name= item.workout_name
                log.workout_note= item.workout_note
                log.workout_total_time= item.workout_total_time
                log.workout_log_images= item.workout_log_images
                adapter.notifyItemChanged(editDeletePosition)

            }
        }
    }

    override fun onDelete(pos: Int) {
        showDeleteDialog(pos)
    }

    override fun onEditDeleteClick(pos: Int) {
        editDeletePosition=pos
        SaveEditWorkoutDialog.newInstance("log", this).show(childFragmentManager)
    }

    fun showDeleteDialog(pos: Int) {
        val dialog = context?.let { Dialog(it) }

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog?.setContentView(R.layout.dialog_delete)
        val tv_no = dialog?.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog?.findViewById<TextView>(R.id.tv_delete)

        tv_no?.setOnClickListener { v -> dialog.dismiss() }

        tv_delete?.setOnClickListener { v ->
            deleteLog(pos)
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun deleteLog(pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.workout_log_id, workOutList.get(pos).workout_log_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteLog(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            workOutList.removeAt(pos)
                            adapter.notifyDataSetChanged()
                        } else {
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }

    private fun getWorkOutApi(pageCount: Int, fromRefresh: String,logDate:String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            if (fromRefresh.equals("1")) {
                binding.loader.visibility = View.GONE
            } else {
                binding.loader.visibility = View.VISIBLE
            }
            if(!logDate.isEmpty()){
                binding.btnCalenderClearFilter.visibility=View.VISIBLE
            }else{
                binding.btnCalenderClearFilter.visibility=View.GONE

            }

            val param = HashMap<String, String>()
            param.put(StringConstant.device_token, "")
            param.put(StringConstant.page_index, "" + pageCount)
            param.put(StringConstant.date, "" + logDate)
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

            getDataManager().getCustomerWorkoutLog(param, header)?.getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    val next_page = jsonObject?.getString(StringConstant.next_page)
                    nextPage = next_page!!.toInt()

                    if (status!!.equals("1")) {

                        binding.loader.visibility = View.GONE
                        binding.noWorkoutFound.visibility = View.GONE
                        binding.swipeRefresh.setRefreshing(false)
                        val myWorkoutLogModel = getDataManager().mGson?.fromJson(
                            response.toString(),
                            MyWorkoutLogModel::class.java
                        )

                        workOutList.addAll(myWorkoutLogModel!!.data);
                        hideFooterLoiader()
                    }

                    if (workOutList.size == 0 && workOutList.isEmpty()) {
                        binding.swipeRefresh.setRefreshing(false)
                        binding.noWorkoutFound.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                    }

                    /*else {
                        binding.swipeRefresh.setRefreshing(false)
                        binding.noWorkoutFound.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                    }*/
                }

                override fun onError(anError: ANError) {
                    hideFooterLoiader()
                    Constant.errorHandle(anError, activity!!)
                    binding.loader.visibility = View.GONE
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

    private fun NotesDialog(description: String) {
        dialog = context?.let { Dialog(it, R.style.MyTheme_Transparent) }!!
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface?) {
                binding.viewTransParancy.visibility = View.GONE
            }
        })

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.log_notes_dialog_layout);
        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)
        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
            dialog.findViewById(R.id.txt_overView_discritpion) as TextView
        dialog_Heading.text = "Notes"
        dialog_overViewDiscription.text = description
        dialog_overViewDiscription.visibility = View.VISIBLE
        binding.viewTransParancy.visibility = View.VISIBLE
        dialog.show()
        dialog.iv_cancle_dialog.setOnClickListener {
            binding.viewTransParancy.visibility = View.GONE
            dialog.dismiss()
        }
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

    override fun textOnClick1(type: String) {
        //edit
        var workout= workOutList.get(editDeletePosition)
        startActivityForResult(
            Intent(getActivity(), WorkoutLogActivity::class.java)
                .putExtra("from", "edit")
                .putExtra("WDetail", workout)
        ,30)
    }

    override fun overwriteClick(type: String) {
//delete
        showDeleteDialog(editDeletePosition)

    }

    fun openDatePickerDialog(str: String) {
        hideKeyboard()

        val pickerPopWin = DatePickerPopWin.Builder(getActivity(),
            object : DatePickerPopWin.OnDatePickedListener {
                override fun onDatePickCompleted(year: Int, month: Int, day: Int, dateDesc: String) {

                    var  selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, (month-1))
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)
                    val sdf1 = SimpleDateFormat("dd MMM, yyyy")
                    val sdf2 = SimpleDateFormat("yyyy-MM-dd")
                    logDate = sdf2.format(selectedDate.getTime())
                    hideNavigationBar()
                    page = 0

                    workOutList.clear()
                    adapter.notifyDataSetChanged()
                    getWorkOutApi(page, "0",logDate)
                }
            }).textConfirm("Done") //text of confirm button
            .textCancel("Cancel") //text of cancel button
            //.btnTextSize(resources.getDimension(R.dimen._5sdp).toInt()) // button text size
            .viewTextSize(resources.getDimension(R.dimen._6sdp).toInt()) // pick view text size
            .colorCancel(Color.parseColor("#232323")) //color of cancel button
            .colorConfirm(Color.parseColor("#232323"))//color of confirm button
            .minYear(1950) //min year in loop
            .maxYear(2030) // max year in loop
            .dateChose(str) // date chose when init popwindow
            .build()

        pickerPopWin.showPopWin(activity)
        pickerPopWin.cancelBtn.setOnClickListener {
            logDate=""
            pickerPopWin.dismissPopWin()
        }
        pickerPopWin.setOnDismissListener {  hideNavigationBar() }
    }

}

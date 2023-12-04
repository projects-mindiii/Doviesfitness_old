package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityStreamLogHistoryBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamLogHistoryPagerAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamHistoryFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamLogFragment
import com.doviesfitness.ui.date_picker.DatePickerPopWin
import com.doviesfitness.utils.FragmentListener
import com.google.android.material.tabs.TabLayout
import eightbitlab.com.blurview.RenderScriptBlur
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StreamLogHistoryActivity : BaseActivity(), View.OnClickListener,FragmentListener {
    private lateinit var binding: ActivityStreamLogHistoryBinding
    private lateinit var pagerAdapter: StreamLogHistoryPagerAdapter
    private var mLastClickTime: Long = 0
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    private val streamLogFragment:StreamLogFragment = StreamLogFragment.newInstance()
    var logDate = ""
    var selectedDate = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_log_history)
        initialization()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    private fun initialization() {
        fragments.add(streamLogFragment)
        fragments.add(StreamHistoryFragment())
        binding.ivBack.setOnClickListener(this)
        binding.ivCalender.setOnClickListener(this)

        binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.ivCalender.visibility = View.VISIBLE
                    binding.mWTitleName.setText("Workout Log")
                } else {
                    binding.ivCalender.visibility = View.GONE
                    binding.mWTitleName.setText("Played History")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })



        val windowBackground = window?.decorView?.background
        binding.transparentBlurView.setupWith(binding.containerId)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(RenderScriptBlur(getActivity()))
                .setBlurRadius(10f)
                .setHasFixedTransformationMatrix(true)
        pagerAdapter = StreamLogHistoryPagerAdapter(supportFragmentManager, getActivity(), fragments)
        binding.viewpager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
        binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                } else {
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ivCalender->{

                var cal = Calendar.getInstance()
                var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                var str = dateFormat.format(cal.getTime())

                openDatePickerDialog(str)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("result", "result: activity-> requestCode..." + requestCode + "...resultCode..." + resultCode)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            notifyData()
        } else {

        }
    }

    fun notifyData() {
        binding.viewpager.setCurrentItem(0)
        var fragment = fragments.get(0) as StreamLogFragment
        fragment.loadData()
    }

    fun showHideBlurView(isShow: Boolean) {
        if (isShow) {
            binding.transparentBlurView.visibility = View.VISIBLE
        } else {
            binding.transparentBlurView.visibility = View.GONE

        }

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
                        streamLogFragment.eventTrigger(logDate)
//                        hideNavigationBar()
//                        page = 1
//                        logList.clear()
//                        adapter.notifyDataSetChanged()
//                        getWorkOutApi(page, "1",logDate)
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

        pickerPopWin.showPopWin(getActivity())
        pickerPopWin.cancelBtn.setOnClickListener {
            logDate=""
            pickerPopWin.dismissPopWin()
        }
        pickerPopWin.setOnDismissListener {  hideNavigationBar() }
    }

    override fun onEventOccur(selectedDate_: String) {
        TODO("Not yet implemented")
        selectedDate = selectedDate_
    }

}
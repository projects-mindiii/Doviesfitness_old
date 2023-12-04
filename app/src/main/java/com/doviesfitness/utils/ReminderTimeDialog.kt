package com.doviesfitness.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_timer.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


open class ReminderTimeDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = ReminderTimeDialog::class.java.name
    var callBack: ReminderCallBack? = null
    var strList = ArrayList<String>()
    var hourList = ArrayList<String>()
    var Hour = ""
    var Min = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reminder_time_dialog, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            list: ArrayList<String>,
            hList: ArrayList<String>,
            callBack: ReminderCallBack,
            mContext: Context,
            hour: String,
            min: String
        ) =
            ReminderTimeDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList = list
                    hourList = hList
                    Hour = hour
                    Min = min
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: ReminderCallBack) {
        this.callBack = callBack
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = hourList.size - 1
        no_picker.minValue = 0
        no_picker.value = Hour.toInt()
        no_picker.wrapSelectorWheel = true
        no_picker.setDisplayedValues(hourList.toTypedArray())
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        no_picker1.maxValue = strList.size - 1
        no_picker1.minValue = 0
        no_picker1.value = Min.toInt()
        no_picker1.wrapSelectorWheel = true
        no_picker1.setDisplayedValues(strList.toTypedArray())
        no_picker1.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {

                callBack?.timeOnClick(
                    no_picker.value,
                    hourList[no_picker.value],
                    no_picker1.value,
                    strList[no_picker1.value]
                )
                dismiss()
            }
            R.id.tv_cancel -> {
                dismiss()
            }
        }

    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    interface ReminderCallBack {
        fun timeOnClick(index: Int, value: String, index1: Int, value1: String)
    }
}

package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.authentication.signup.activity.HeightAndWeightActivity
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_timer.*

open class TimerDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = TimerDialog::class.java.name
    var callBack: HeightWeightCallBack? = null
    var strList = ArrayList<String>()
    var Min=""
    var Sec=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_timer, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(list:ArrayList<String> , callBack: HeightWeightCallBack,mContext: Context,min:String,sec:String) =
            TimerDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList=list
                    Min=min
                    Sec=sec
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightWeightCallBack) {
        this.callBack = callBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = strList.size-1
        no_picker.minValue = 0
        no_picker.value=Min.toInt()
        no_picker.wrapSelectorWheel = true
        no_picker.setDisplayedValues(strList.toTypedArray())
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        no_picker1.maxValue = strList.size-1
        no_picker1.minValue = 0
        no_picker1.value=Sec.toInt()
        no_picker1.wrapSelectorWheel = false
        no_picker1.setDisplayedValues(strList.toTypedArray())
        no_picker1.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {

                callBack?.timeOnClick(no_picker.value,strList[no_picker.value],no_picker1.value,strList[no_picker1.value])
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

    interface HeightWeightCallBack {
        fun timeOnClick(index: Int, value: String,index1: Int, value1: String)
    }
}

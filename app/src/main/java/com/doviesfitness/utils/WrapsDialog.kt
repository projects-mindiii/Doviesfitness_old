package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_timer.no_picker
import kotlinx.android.synthetic.main.dialog_timer.no_picker1
import kotlinx.android.synthetic.main.dialog_timer.tv_cancel
import kotlinx.android.synthetic.main.dialog_timer.tv_done
import kotlinx.android.synthetic.main.dialog_timer.tv_minutes
import kotlinx.android.synthetic.main.dialog_timer.tv_seconds

open class WrapsDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = WrapsDialog::class.java.name
    var callBack: HeightWeightCallBack? = null
    var strList = ArrayList<String>()
    var Min = ""
    var Sec = ""
    var callFor = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_timer, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            list: ArrayList<String>,
            callBack: HeightWeightCallBack,
            mContext: Context,
            min: String,
            sec: String
        ) =
            WrapsDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList = list
                    Min = min
                    Sec = sec
                }
            }

        @JvmStatic
        fun newInstance1(
            list: ArrayList<String>,
            callBack: HeightWeightCallBack,
            mContext: Context,
            min: String,
            sec: String,
            pourpose: String
        ) =
            WrapsDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList = list
                    Min = min
                    Sec = sec
                    callFor = pourpose
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightWeightCallBack) {
        this.callBack = callBack
    }

    private fun getValueForSecondWheel(number: Int): java.util.ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in number..59) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = strList.size - 1
        no_picker.minValue = 0
        no_picker.value = Min.toInt()
        no_picker.wrapSelectorWheel = true
        no_picker.setDisplayedValues(strList.toTypedArray())
        no_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            if (callFor == "repsInSetAndReps") {
                //no_picker1.displayedValues = getValueForSecondWheel(newVal).toTypedArray()
                try {
                    no_picker1.displayedValues = getValueForSecondWheel(newVal).toTypedArray()
                    no_picker1.maxValue = strList.size - 1
                    no_picker1.minValue = newVal
                    no_picker1.value = newVal
                    no_picker1.wrapSelectorWheel = true
                    //   no_picker1.displayedValues = getValueForSecondWheel(newVal).toTypedArray()
                } catch (e: Exception) {
                    Log.d("sdsgdhsfjk", "onViewCreated: ${e.printStackTrace()}")
                    no_picker1.maxValue = strList.size - 1
                    no_picker1.minValue = newVal
                    no_picker1.value = newVal
                    no_picker1.wrapSelectorWheel = true
                    no_picker1.displayedValues = getValueForSecondWheel(newVal).toTypedArray()
                }

            }
        }
        /* no_picker.setOnValueChangedListener { numberPicker, i, i1 ->
             println("onValueChange: $numberPicker $i $i1")

             Handler().postDelayed({
                 if (callFor == "repsInSetAndReps") {
                     no_picker1.maxValue = strList.size - 1
                     no_picker1.minValue = i1
                     no_picker1.value = i1
                     no_picker1.wrapSelectorWheel = true
                     no_picker1.displayedValues = getValueForSecondWheel(i1).toTypedArray()
                 }
             }, 1000)


         }*/

        if (callFor == "repsInSetAndReps") {

            no_picker1.maxValue = strList.size - 1

            //no_picker1.minValue = Min.toInt()


            if (Min.toInt() == Sec.toInt()){
                no_picker1.minValue = Min.toInt()
                no_picker1.value = Min.toInt()
                no_picker1.wrapSelectorWheel = false
                no_picker1.displayedValues = getValueForSecondWheel(Min.toInt()).toTypedArray()
            }
            else {
                no_picker1.value = Sec.toInt()
                no_picker1.minValue = Min.toInt()
                no_picker1.wrapSelectorWheel = false
                no_picker1.displayedValues = getValueForSecondWheel(Min.toInt()).toTypedArray()
            }


        } else {
            no_picker1.maxValue = strList.size - 1
            no_picker1.minValue = 0
            no_picker1.value = Sec.toInt()
            no_picker1.wrapSelectorWheel = false
            no_picker1.setDisplayedValues(strList.toTypedArray())
        }






        no_picker1.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        tv_minutes.visibility = View.GONE
        tv_seconds.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {

                callBack?.timeOnClick(
                    no_picker.value,
                    strList[no_picker.value],
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

    interface HeightWeightCallBack {
        fun timeOnClick(index: Int, value: String, index1: Int, value1: String)
    }
}

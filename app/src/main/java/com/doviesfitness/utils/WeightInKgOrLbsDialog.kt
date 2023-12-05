package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.utils.number_picker.NumberPicker
import kotlinx.android.synthetic.main.dialog_timer.no_picker
import kotlinx.android.synthetic.main.dialog_timer.no_picker1
import kotlinx.android.synthetic.main.dialog_timer.tv_cancel
import kotlinx.android.synthetic.main.dialog_timer.tv_done
import kotlinx.android.synthetic.main.dialog_timer.tv_minutes
import kotlinx.android.synthetic.main.dialog_timer.tv_seconds
import kotlinx.android.synthetic.main.weight_dialog_ui.weight

open class WeightInKgOrLbsDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    private var selected = 12
    val TAG = WrapsDialog::class.java.name
    var callBack: WeightInKgOrLbsDialogCallBack? = null
    var strList = ArrayList<String>()
    var Min = ""
    var Sec = ""
    var callFor = ""
    var mTitleDialog = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weight_dialog_ui, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            list: ArrayList<String>,
            callBack: WeightInKgOrLbsDialogCallBack,
            mContext: Context,
            min: String,
            sec: String,mTitle: String = ""
        ) =
            WeightInKgOrLbsDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList = list
                    Min = min
                    Sec = sec
                    mTitleDialog = mTitle
                }
            }

        @JvmStatic
        fun newInstance1(
            list: ArrayList<String>,
            callBack: WeightInKgOrLbsDialogCallBack,
            mContext: Context,
            min: String,
            sec: String,
            pourpose: String
        ) =
            WeightInKgOrLbsDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList = list
                    Min = min
                    Sec = sec
                    callFor = pourpose
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: WeightInKgOrLbsDialogCallBack) {
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

        no_picker.minValue = 1
        no_picker.maxValue = 600
        no_picker.wrapSelectorWheel = false

        if (Min.isNotEmpty())
            no_picker.value = Min.toInt()
        else
            no_picker.value = 0

        no_picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        no_picker1.minValue = 0
        no_picker1.maxValue = 1

        if (Sec.isNotEmpty()) {
            if (Sec  == "lbs")
                no_picker1.value = 0
            else
                no_picker1.value = 1
        }

        no_picker1.displayedValues = arrayOf<String>("lbs", "kg")

        weight.text = mTitleDialog

        if (mTitleDialog.isEmpty()) {
            weight.visibility = View.GONE
        } else {
            weight.visibility = View.VISIBLE
        }

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

    interface WeightInKgOrLbsDialogCallBack {
        fun timeOnClick(index: Int, value: String, index1: Int, value1: String)
    }
}

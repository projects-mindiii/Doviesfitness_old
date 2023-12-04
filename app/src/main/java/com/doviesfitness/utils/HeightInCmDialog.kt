package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.utils.WorkoutLevelDialog
import kotlinx.android.synthetic.main.dialog_height_and_weight.*

class HeightInCmDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = WorkoutLevelDialog::class.java.name
    var callBack: HeightInCmCallBack? = null
    var strList = ArrayList<String>()
    var SelectedIndex=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(list:ArrayList<String> , callBack: HeightInCmCallBack,mContext: Context,selectedIndex:Int) =
            HeightInCmDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList=list
                    SelectedIndex=selectedIndex
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightInCmCallBack) {
        this.callBack = callBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = strList.size-1
        no_picker.minValue = 0
        no_picker.value=SelectedIndex
        no_picker.wrapSelectorWheel = false
        no_picker.setDisplayedValues(strList.toTypedArray())

        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }
        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                //Constant.showCustomToast(mActivity, "" + no_picker.value)
                callBack?.CmValueOnClick(no_picker.value,strList[no_picker.value])
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


    interface HeightInCmCallBack {
        fun CmValueOnClick(index: Int, value: String)
    }
}
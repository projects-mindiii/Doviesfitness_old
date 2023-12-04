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
import kotlinx.android.synthetic.main.dialog_height_and_weight.*

open class WorkoutLevelDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = WorkoutLevelDialog::class.java.name
    var callBack: HeightWeightCallBack? = null
    var strList = ArrayList<String>()
    var SelectedIndex=0
    var from=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(list:ArrayList<String> , callBack: HeightWeightCallBack,mContext: Context,selectedIndex:Int) =
            WorkoutLevelDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList=list
                    SelectedIndex=selectedIndex
                }
            }
        @JvmStatic
        fun newInstance1(list:ArrayList<String> , callBack: HeightWeightCallBack,mContext: Context,selectedIndex:Int,from1:String) =
            WorkoutLevelDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList=list
                    from=from1
                    SelectedIndex=selectedIndex
                }
            }
        @JvmStatic
        fun newInstance2(list:ArrayList<String> , callBack: HeightWeightCallBack,mContext: Context,selectedIndex:Int,from1:String) =
            WorkoutLevelDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    strList=list
                    from=from1
                    SelectedIndex=selectedIndex
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
        no_picker.value=SelectedIndex-1
        no_picker.wrapSelectorWheel = false
        no_picker.displayedValues = strList.toTypedArray()

        if (from!=null&&from.isNotEmpty()&& from == "calorie"){
            weight.visibility=View.VISIBLE
            weight.text = "Calorie"
        }

        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }
        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                 callBack?.levelOnClick(no_picker.value,strList[no_picker.value])
                dismiss()
            }
            R.id.tv_cancel -> {
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    interface HeightWeightCallBack {
        fun levelOnClick(index: Int, str: String)
    }
}

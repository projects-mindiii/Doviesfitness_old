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

open class WeightDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = WeightDialog::class.java.name
    var minValue: Int = 0
    var maxValue: Int = 0
    var typeValue : String = ""
    val MAXVALUE = "MAXVALUE"
    val MINVALUE = "MINVALUE"
    val TYPEVALUE = "TYPEVALUE"
    var callBack: HeightWeightCallBack? = null
    var s = getDisplayValueArray(13,227)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(min: Int, max: Int,type : String, callBack: HeightWeightCallBack,mContext: Context) =
            WeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    putInt(MINVALUE, min)
                    putInt(MAXVALUE, max)
                    putString(TYPEVALUE, type)
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightWeightCallBack) {
        this.callBack = callBack
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        typeValue = arguments!!.getString(TYPEVALUE)!!
        maxValue = arguments!!.getInt(MAXVALUE)
        Log.e(TAG, "" + maxValue)
        arguments?.getInt(MINVALUE)?.let {
            minValue = it
            Log.e(TAG, "" + minValue)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = s.size-1
        no_picker.minValue = 0
        no_picker.wrapSelectorWheel = false
        no_picker.setDisplayedValues(s)
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        weight.visibility=View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                //Constant.showCustomToast(mActivity, "" + no_picker.value)
                callBack?.valueOnClick(no_picker.value,s[no_picker.value])
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
        fun valueOnClick(index: Int, value: String)
    }

    fun getDisplayValueArray(min: Int, max: Int): Array<String> {
        val values = arrayListOf<String>()
        var unit="Kgs"
        if (typeValue.equals("WeightInLbs")){
            unit="Lbs"
        }
        else{
            unit="Kgs"
        }
        for (min in min..max) {
            values.add("$min "+unit)
        }
        return values.toTypedArray()
    }
}

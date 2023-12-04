package com.doviesfitness.ui.authentication.signup.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.doviesfitness.R
import com.doviesfitness.ui.authentication.signup.activity.HeightAndWeightActivity
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_height_and_weight.*

class HeightAndWeightDialog : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = HeightAndWeightDialog::class.java.name
    var minValue: Int = 0
    var maxValue: Int = 0
    var typeValue : String = ""
    val maxValue1 = "MAXVALUE"
    val minValue1 = "MINVALUE"
    val typeValue1 = "TYPEVALUE"
    var callBack: HeightWeightCallBack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(min: Int, max: Int,type : String, heightAndWeightActivity: HeightAndWeightActivity) =
            HeightAndWeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(heightAndWeightActivity)
                    putInt(minValue1, min)
                    putInt(maxValue1, max)
                    putString(typeValue1, type)
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightWeightCallBack) {
        this.callBack = callBack
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        typeValue = arguments!!.getString(typeValue1)!!
        maxValue = arguments!!.getInt(maxValue1)
        Log.e(TAG, "" + maxValue)
        arguments?.getInt(minValue1)?.let {
            minValue = it
            Log.e(TAG, "" + minValue)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = maxValue
        no_picker.minValue = minValue
        no_picker.wrapSelectorWheel = false
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                //Constant.showCustomToast(mActivity, "" + no_picker.value)
                callBack?.valueOnClick(no_picker.value,typeValue)
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
      public  fun valueOnClick(value: Int, type: String)
    }
}

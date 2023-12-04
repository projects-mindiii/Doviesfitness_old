package com.doviesfitness.ui.authentication.signup.dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.WorkoutLogActivity
import com.doviesfitness.ui.profile.editProfile.EditProfileFragment
import kotlinx.android.synthetic.main.dialog_height_and_weight.*

class CreateLogWeightDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = EditProfileFragment::class.java.name
    var minValue: Int = 0
    var maxValue: Int = 0
    var typeValue= ""
    var selectedVelue: Int = 0
    val MAXVALUE = "MAXVALUE"
    val MINVALUE = "MINVALUE"
    val SELECTEDVELUE = "SELECTEDVELUE"
    val TYPEVALUE = "TYPEVALUE"
    var callBack: WeightCallBack? = null
    lateinit var s : Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(min: Int, max: Int, selectValue : Int,  type: String, workoutLogActivity: WorkoutLogActivity) =
            CreateLogWeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(workoutLogActivity)
                    putInt(MINVALUE, min)
                    putInt(MAXVALUE, max)
                    putInt(SELECTEDVELUE, selectValue)
                    putString(TYPEVALUE, type)
                    typeValue=type
                    s= getDisplayValueArray(min, max,typeValue)
                }
            }
        @JvmStatic
        fun newInstance1(min: Int, max: Int, selectValue : Int,  type: String, activity: Activity) =
            CreateLogWeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener1(activity as WeightCallBack)
                    putInt(MINVALUE, min)
                    putInt(MAXVALUE, max)
                    putInt(SELECTEDVELUE, selectValue)
                    putString(TYPEVALUE, type)
                    typeValue=type
                    s= getDisplayValueArray(min, max,typeValue)
                }
            }

    }

    fun setOnCalvingAddEditListener(callBack: WeightCallBack) {
        this.callBack = callBack
    }
    fun setOnCalvingAddEditListener1(callBack: WeightCallBack) {
        this.callBack = callBack
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        typeValue = arguments!!.getString(TYPEVALUE)!!
        selectedVelue = arguments!!.getInt(SELECTEDVELUE)
        maxValue = arguments!!.getInt(MAXVALUE)
        Log.e(TAG, "" + maxValue)

        arguments?.getInt(MINVALUE)?.let {
            minValue = it
            Log.e(TAG, "" + minValue)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weight.visibility=View.VISIBLE
        no_picker.maxValue = maxValue
        no_picker.minValue = minValue
        no_picker.value = selectedVelue
        no_picker.wrapSelectorWheel = false
        no_picker.setDisplayedValues(s)
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                //Constant.showCustomToast(mActivity, "" + no_picker.value)
                // callBack?.valueOnClick(no_picker.value,typeValue)
               // if (s.size>no_picker.value)
                callBack?.WeightValueOnClick(no_picker.value)
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

    interface WeightCallBack {
        fun WeightValueOnClick(index: Int)
    }

    fun getDisplayValueArray(min: Int, max: Int, typeValue: String): Array<String> {
        val values = arrayListOf<String>()
        for (min in min..max) {
            values.add("$min "+typeValue)
        }
        return values.toTypedArray()
    }
}
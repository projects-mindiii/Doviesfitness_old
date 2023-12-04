package com.doviesfitness.ui.authentication.signup.dialog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.ui.profile.editProfile.EditProfileFragment
import kotlinx.android.synthetic.main.dialog_height_and_weight.*

class EditHeightDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = EditProfileFragment::class.java.name
    var minValue: Int = 0
    var maxValue: Int = 0
    var selectedValue: Int = 0
    var typeValue : String = ""
    val MAXVALUE = "MAXVALUE"
    val MINVALUE = "MINVALUE"
    val TYPEVALUE = "TYPEVALUE"
    val SelectedValue = "SelectedValue"
    var callBack: HeightCallBack? = null
    var s = getDisplayValueArray(91, 242)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_height_and_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(min: Int, max: Int, selectedValue: Int,type : String, editProfileFragment: EditProfileFragment) =
            EditHeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(editProfileFragment)
                    putInt(MINVALUE, min)
                    putInt(MAXVALUE, max)
                    putInt(SelectedValue, selectedValue)
                    putString(TYPEVALUE, type)
                }
            }
    }

    fun setOnCalvingAddEditListener(callBack: HeightCallBack) {
        this.callBack = callBack
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        typeValue = arguments!!.getString(TYPEVALUE)!!
        maxValue = arguments!!.getInt(MAXVALUE)
        selectedValue = arguments!!.getInt(SelectedValue)
        Log.e(TAG, "" + maxValue)
        arguments?.getInt(MINVALUE)?.let {
            minValue = it
            Log.e(TAG, "" + minValue)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        no_picker.maxValue = maxValue
        no_picker.minValue = minValue
        no_picker.value = selectedValue

        no_picker.wrapSelectorWheel = false
        no_picker.setDisplayedValues(s)
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                try {
                    callBack?.HeightValueOnClick(no_picker.value,s[no_picker.value])
                }
                catch (e:Exception){

                }
               // callBack?.HeightValueOnClick(no_picker.value,s[no_picker.value])
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

    interface HeightCallBack {
        fun HeightValueOnClick(index: Int, value: String)
    }


    fun getDisplayValueArray(min: Int, max: Int): Array<String> {
        val values = arrayListOf<String>()
        for (min in min..max) {
            values.add("$min cm")
        }
        return values.toTypedArray()
    }
}
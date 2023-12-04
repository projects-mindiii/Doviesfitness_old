package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_height_and_weight.*
import kotlinx.android.synthetic.main.dialog_timer.*
import kotlinx.android.synthetic.main.dialog_timer.no_picker
import kotlinx.android.synthetic.main.dialog_timer.tv_cancel
import kotlinx.android.synthetic.main.dialog_timer.tv_done

class HeightAndWeightDialog() : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = HeightAndWeightDialog::class.java.name
    var callBack: HeightAndWeightCallBack? = null
    // var strList = ArrayList<String>()
    var Min = ""
    var Sec = ""
    var selectedFeet = 0
    var selectedInch = 0
    lateinit var feetList: Array<String>
    lateinit var inchList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.dialog_timer, container, false)
        return inflater.inflate(R.layout.dialog_height_weight, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(callBack: HeightAndWeightCallBack, mContext: Context, min: String, sec: String) =
            HeightAndWeightDialog().apply {
                arguments = Bundle().apply {
                    setOnCalvingAddEditListener(callBack)
                    // strList = list


                   /* if (min!=null&&!min.isEmpty()&&min.contains("'"))
                    {
                        Min=  min.replace("'","")
                    }
                    if (sec!=null&&!sec.isEmpty()&&sec.contains("''"))
                    {
                        Sec= sec.replace("''","")
                    }
*/

                    Min = min
                    Sec = sec


                    inchList = getDisplayValueArray(0, 11, "''")
                    feetList = getDisplayValueArray(3, 7, "'")
                    for (i in 0..feetList.size - 1) {
                        Log.v("vel", ""+feetList.get(i))
                        if ((Min+" '").equals(feetList.get(i))) {
                            selectedFeet = i
                        }
                    }
                    for (i in 0..inchList.size - 1) {
                        if ((Sec+" ''").equals(inchList.get(i))) {
                            selectedInch = i
                        }
                    }
                }
            }
    }


    fun setOnCalvingAddEditListener(callBack: HeightAndWeightCallBack) {
        this.callBack = callBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        no_picker.maxValue = feetList.size - 1
        no_picker.minValue = 0
        no_picker.value = selectedFeet
        no_picker.wrapSelectorWheel = false
        no_picker.setDisplayedValues(feetList)
        no_picker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        no_picker1.maxValue = inchList.size - 1
        no_picker1.minValue = 0
        no_picker1.value = selectedInch
        no_picker1.wrapSelectorWheel = false
        no_picker1.setDisplayedValues(inchList)
        no_picker1.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }

        tv_done.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_done -> {
                callBack?.timeOnClick(
                    no_picker.value,
                    feetList[no_picker.value],
                    no_picker1.value,
                    inchList[no_picker1.value]
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

    interface HeightAndWeightCallBack {
        fun timeOnClick(index: Int, value: String, index1: Int, value1: String)
    }

    fun getDisplayValueArray(min: Int, max: Int, str: String): Array<String> {
        val values = arrayListOf<String>()
        for (min in min..max) {
            values.add("$min " + str)
        }
        return values.toTypedArray()
    }
}
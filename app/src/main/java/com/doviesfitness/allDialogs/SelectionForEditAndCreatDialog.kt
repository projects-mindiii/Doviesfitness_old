package com.doviesfitness.allDialogs

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.AddToWorkOutPLanModal
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import kotlinx.android.synthetic.main.dialog_image_picker.*
import kotlinx.android.synthetic.main.work_plan_for_edit_update.*
import kotlinx.android.synthetic.main.work_plan_for_edit_update.tv_cancel

class SelectionForEditAndCreatDialog : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = SelectionForEditAndCreatDialog::class.java.name
    var callBack: WorkPlanEditAndCreatCallBack? = null
    var DATAVALUE = "DataValue"
    var positionKey = "Position"

    var pos: Int = 0
    lateinit var dataValue: AddToWorkOutPLanModal.Data

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.work_plan_for_edit_update, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_for_new_plan.setOnClickListener(this)
        ll_for_overwrite.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        /* iv_fav.setOnClickListener {
             if(dataValue.program_fav_status.equals("1")){
                 iv_fav.setImageResource(R.drawable.dots_image)
             }else{
                 iv_fav.setImageResource(R.drawable.ic_add_user)
             }
         }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(baseActivity: CreateWorkOutPlanActivty) =
            SelectionForEditAndCreatDialog().apply {
                arguments = Bundle().apply {
                    setOnTextListener(baseActivity)
                   /* putSerializable(DATAVALUE, data)
                    putInt(positionKey, position)*/
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (arguments!!.getSerializable(DATAVALUE) != null) {
            //dataValue = arguments!!.getSerializable(DATAVALUE) as AddToWorkOutPLanModal.Data
           // pos = arguments!!.getInt(positionKey)
        }
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    private fun setOnTextListener(callBack: WorkPlanEditAndCreatCallBack) {
        this.callBack = callBack
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_for_new_plan -> {
                callBack?.workPlanEditAndCreaOnClick("FORNEWPLAN")
                dismiss()
            }
            R.id.ll_for_overwrite -> {
                callBack?.workPlanEditAndCreaOnClick("FOROVERWRITE")
                dismiss()
            }

            R.id.tv_cancel -> {
                dismiss()
            }
        }
    }

    interface WorkPlanEditAndCreatCallBack {
        fun workPlanEditAndCreaOnClick(type: String)
    }
}
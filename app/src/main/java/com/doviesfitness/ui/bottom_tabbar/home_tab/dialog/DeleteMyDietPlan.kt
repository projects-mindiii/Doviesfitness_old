package com.doviesfitness.ui.bottom_tabbar.home_tab.dialog

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.MyDietPlanFragment
import kotlinx.android.synthetic.main.dialog_comment_more.*
import kotlinx.android.synthetic.main.dialog_image_picker.tv_cancel

class DeleteMyDietPlan : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = DeleteMyDietPlan::class.java.name
    var callBack: MyDietPlanCallBack? = null
    var TEXTVALUE = "TEXTVALUE"
    var positionKey = "Position"
    var pos: Int = 0
    lateinit var typeValue: MyDietPlan.Data


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_comment_more, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_status.text = getString(R.string.delete)
        iv_common.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_recycling_bin))
        tv_cancel.setOnClickListener(this)
        tv_status.setOnClickListener(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(baseActivity: MyDietPlanCallBack, value: MyDietPlan.Data, position: Int) =
            DeleteMyDietPlan().apply { arguments = Bundle().apply { setOnTextListener(baseActivity)
                    putParcelable(TEXTVALUE, value)
                    putInt(positionKey, position)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        typeValue = arguments!!.getParcelable<MyDietPlan.Data>(TEXTVALUE) as MyDietPlan.Data
        pos = arguments!!.getInt(positionKey)
    }


    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    private fun setOnTextListener(callBack: MyDietPlanCallBack) {
        this.callBack = callBack
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_status -> {
                callBack?.textOnClick1(getString(R.string.delete), typeValue, pos)
                dismiss()
            }
            R.id.tv_cancel -> {
                dismiss()
            }
        }
    }

    interface MyDietPlanCallBack {
        fun textOnClick1(type: String, id: MyDietPlan.Data, pos: Int)
    }
}
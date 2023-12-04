package com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog


import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseDialog
import com.doviesfitness.utils.WorkoutLevelDialog
import kotlinx.android.synthetic.main.dialog_logout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogoutDialog.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CancelDialog : BaseDialog(), View.OnClickListener {
    val TAG = CancelDialog::class.java.name
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var listener: OnTextClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_cancel, container, false)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_logout.text="Yes"
        tv_cancel.text="No"
        tv_header.text="Are you sure you want to cancel logging your workout?"
        tv_logout.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_logout -> {
                if (param1.equals("createlog"))
                {
                    listener?.onYesClick()
                }
                else
                getBaseActivity().finish()
            }
            R.id.tv_cancel -> {

                dialog?.dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onClickView()
        super.onDismiss(dialog)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutDialog.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String,listener1: Activity) =
            CancelDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    setTextClickListener(listener1 as OnTextClickListener )
                }
            }



    }
    fun setTextClickListener(listener1:OnTextClickListener){
        listener=listener1
    }

    interface OnTextClickListener{
        fun onClickView()
        fun onYesClick()
    }
}

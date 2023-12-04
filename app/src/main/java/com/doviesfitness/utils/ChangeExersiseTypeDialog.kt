package com.doviesfitness.utils

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R

import com.doviesfitness.ui.base.BaseDialog
import java.lang.Exception
import kotlinx.android.synthetic.main.dialog_cancel.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
/**
 * A simple [Fragment] subclass.
 * Use the [LogoutDialog.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChangeExersiseTypeDialog : BaseDialog(), View.OnClickListener {
    val TAG = ChangeExersiseTypeDialog::class.java.name
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getString(ARG_PARAM4)

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
        tv_logout.text=param1
        tv_cancel.text=param2
        tv_header.text=param3
        tv_logout.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        if(param1!!.isEmpty()){
            tv_logout.visibility= View.GONE
            seperator.visibility= View.GONE
        }
        else{
            tv_logout.visibility= View.VISIBLE
            seperator.visibility= View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_logout -> {
                    mListener?.isYesClicked(param4)
                    dialog?.dismiss()

            }
            R.id.tv_cancel -> {
                    dialog?.dismiss()
            }
        }
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
        fun newInstance(param1: String, param2: String,param3: String,param4: String) =
                ChangeExersiseTypeDialog().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                        putString(ARG_PARAM3, param3)
                        putString(ARG_PARAM4, param4)
                    }
                }
    }
    private var mListener: IsYesClickedForTypeChanged? = null
    public fun setListener(listener: IsYesClickedForTypeChanged) {
        this.mListener = listener;
    }
    interface IsYesClickedForTypeChanged{
        public fun isYesClicked(type: String?)
    }
}
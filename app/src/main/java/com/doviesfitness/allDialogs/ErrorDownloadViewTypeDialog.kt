package com.doviesfitness.allDialogs.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_download_type_only.*

const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"
const val ARG_PARAM3 = "param3"

class ErrorDownloadViewTypeDialog : BaseDialog(), View.OnClickListener {
    val TAG = ErrorDownloadViewTypeDialog::class.java.name
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_download_type_only, container, false)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // tv_logout.text=param1
//        iv_cancle_dialog.text=param2
        if(param1.equals("2"))
        {
            watch_icon.setBackgroundResource(R.drawable.ic_video_play)
        }else{
            watch_icon.setBackgroundResource(R.drawable.download_log_ico)
        }

        txt_overView_discritpion.text=param3
        //  tv_logout.setOnClickListener(this)
        iv_cancle_dialog.setOnClickListener(this)
        /* if(param1!!.isEmpty()){
             tv_logout.visibility= View.GONE
             seperator.visibility= View.GONE
         }
         else{
           //  tv_logout.visibility= View.VISIBLE
           //  seperator.visibility= View.VISIBLE
         }*/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
/*
            R.id.tv_logout -> {
                if (tv_logout.text.toString().equals("Delete")){
                    mListener?.isOk()
                    dialog?.dismiss()
                }
                else{
                    getBaseActivity().finish()
                }
            }
*/
            R.id.iv_cancle_dialog -> {
                mListener?.isOk()
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
        fun newInstance(param1: String, param2: String,param3: String) =
            ErrorDownloadViewTypeDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
    private var mListener: IsOK? = null
    public fun setListener(listener:IsOK) {
        this.mListener = listener;
    }

    interface IsOK{
        public fun isOk()
    }
}

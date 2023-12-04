package com.doviesfitness.allDialogs

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseDialog

class ExclusiveWorkoutDialog : BaseDialog(), View.OnClickListener {
    val TAG = ExclusiveWorkoutDialog::class.java.name

    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(com.doviesfitness.allDialogs.menu.ARG_PARAM1)
            param2 = it.getString(com.doviesfitness.allDialogs.menu.ARG_PARAM2)
            param3 = it.getString(com.doviesfitness.allDialogs.menu.ARG_PARAM3)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_exclusive_workout, container, false)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_add_dietPlan).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.iv_cancle_dialog).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_dietPlan->{
                dialog?.dismiss()
                param1?.let { exclusive_navigation(it) }
            }
            R.id.iv_cancle_dialog -> {
                dialog?.dismiss()
            }
        }
    }

    private fun exclusive_navigation(url: String) {
        val uri = Uri.parse(url)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        ContextCompat.startActivity(requireContext(), goToMarket, null)


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
            ExclusiveWorkoutDialog().apply {
                arguments = Bundle().apply {
                    putString(com.doviesfitness.allDialogs.menu.ARG_PARAM1, param1)
                    putString(com.doviesfitness.allDialogs.menu.ARG_PARAM2, param2)
                    putString(com.doviesfitness.allDialogs.menu.ARG_PARAM3, param3)
                }
            }
    }
}
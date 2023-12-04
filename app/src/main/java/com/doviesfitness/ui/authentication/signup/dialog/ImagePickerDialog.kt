package com.doviesfitness.ui.authentication.signup.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_image_picker.*

class ImagePickerDialog : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = ImagePickerDialog::class.java.name
    var callBack: ImagePickerCallBack? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_image_picker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_camera.setOnClickListener(this)
        tv_gallery.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(baseActivity: BaseActivity) =
            ImagePickerDialog().apply {
                arguments = Bundle().apply {
                    setOnTextListener(baseActivity)
                }
            }


    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    private fun setOnTextListener(callBack: BaseActivity) {
        this.callBack = callBack
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_camera -> {
                callBack?.textOnClick("Camera")
                dismiss()
            }
            R.id.tv_gallery -> {
                callBack?.textOnClick("Gallery")
                dismiss()
            }
            R.id.tv_cancel -> {
                dismiss()
            }
        }

    }

    interface ImagePickerCallBack {
        fun textOnClick(type: String)
    }
}
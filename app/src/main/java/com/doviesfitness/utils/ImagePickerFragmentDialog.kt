package com.doviesfitness.utils

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.profile.editProfile.EditProfileFragment
import kotlinx.android.synthetic.main.dialog_image_picker.*

class ImagePickerFragmentDialog: BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = ImagePickerFragmentDialog::class.java.name
    var callBack: ImagePickerFragmentCallBack? = null



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
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(baseFragment: EditProfileFragment) =
            ImagePickerFragmentDialog().apply {
                arguments = Bundle().apply {
                    setOnTextListener(baseFragment)
                    // putString(TEXTVALUE, type)
                }
            }
        @JvmStatic
        fun newInstance1(baseFragment: Context) =
            ImagePickerFragmentDialog().apply {
                arguments = Bundle().apply {
                    setOnTextListener1(baseFragment as ImagePickerFragmentCallBack )
                    // putString(TEXTVALUE, type)
                }
            }

    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    private fun setOnTextListener(callBack: EditProfileFragment) {
        this.callBack = callBack
    }
    private fun setOnTextListener1(callBack: ImagePickerFragmentCallBack) {
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

    interface ImagePickerFragmentCallBack {
        fun textOnClick(type: String)
    }
}
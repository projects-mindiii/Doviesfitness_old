package com.doviesfitness.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout

open class BaseDialog : androidx.fragment.app.DialogFragment() {
    var mActivity = BaseActivity()
    var isBottom : Boolean = false

     fun getBaseActivity(): BaseActivity {
        return mActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)


        if (isBottom) {
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        } else {
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // creating the fullscreen dialog
        val dialog = Dialog(mActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (isBottom) {
                dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            } else {
                dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        if (isBottom)
           // dialog.window!!.attributes.windowAnimations = R.style.DialogAnimationUpDown
        else
           // dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        dialog.setCanceledOnTouchOutside(false)

        return dialog

    }

    protected fun setIsBottomTrue() {
        this.isBottom = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
    }

    public fun dismissDialog(tag: String) {
        hideKeyboard()
        dismiss()
    }


    protected fun hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard()
        }
    }

    protected fun setLoading(isLoading: Boolean) {
        if (mActivity != null) {
            mActivity.setLoading(isLoading)
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun show(fragmentManager : androidx.fragment.app.FragmentManager, tag: String?) {
        val transaction = fragmentManager?.beginTransaction()
        val prevFragment = fragmentManager?.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction?.remove(prevFragment)
        }
        transaction?.addToBackStack(null)
        show(transaction, tag)
    }
}
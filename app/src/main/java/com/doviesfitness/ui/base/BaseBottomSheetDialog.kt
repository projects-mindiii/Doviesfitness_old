package com.doviesfitness.ui.base

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.Doviesfitness
import com.doviesfitness.data.AppDataManager



open class BaseBottomSheetDialog : BottomSheetDialogFragment(), View.OnFocusChangeListener {
    var mActivity = BaseActivity()

    protected val dataManager: AppDataManager
        get() = Doviesfitness.getDataManager()


    fun getBaseActivity(): BaseActivity {
        return mActivity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    protected fun hideKeyboard() {
            mActivity.hideKeyboard()

    }

    protected fun setLoading(isLoading: Boolean) {
            mActivity.setLoading(isLoading)
    }
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (!hasFocus) {
            hideKeyboard()
        }
    }
}

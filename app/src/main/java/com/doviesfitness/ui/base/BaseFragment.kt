package com.doviesfitness.ui.base


import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.AppDataManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
open class BaseFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //private var dataManager : AppDataManager? = null
    var mActivity: BaseActivity? = null
    lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText(" ")
        }
    }


    fun getDataManager(): AppDataManager {
        return Doviesfitness.getDataManager()
    }


    fun hideNavigationBar() {
        activity!!.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = activity!!.window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }*/
    }




    /*
 replace fragment
 //1 @param - fragmentHolder : Fragment Name
 //2 @param- layoutId : Container Id(FrameLayout)
 //3 @param - addToBackStack : true/false (add to back stack)
 */
    fun addFragment(fragment: androidx.fragment.app.Fragment, layoutId: Int, addToBackStack: Boolean) {
        try {
            val fragmentName = fragment.javaClass.name

            val fragmentTransaction = fragmentManager!!.beginTransaction()

            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity!!.window.enterTransition = null
            }
            fragmentTransaction.add(layoutId, fragment, fragmentName)
            if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName)
            fragmentTransaction.commit()

            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is BaseActivity) {
            mActivity = context
        }
    }

    protected fun getBaseActivity(): BaseActivity? {
        return mActivity
    }

    fun setLoading(isload: Boolean) {
        mActivity?.setLoading(isload)
    }

    fun setVideoLoading(isload: Boolean) {
        mActivity?.setVideoLoading(isload)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BaseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }

    fun hideKeyboard() {
        if (activity!!.currentFocus == null) return
        val inputMethodManager =
            activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
    }

}

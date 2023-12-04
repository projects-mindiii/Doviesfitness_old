package com.doviesfitness.ui.createAndEditDietPlan.fragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import android.view.MotionEvent
import android.annotation.SuppressLint


class PagerPlanOverViewFragment : BaseFragment(), CreateWorkOutPlanActivty.GetOverViewLitener {

    lateinit var binding : com.doviesfitness.databinding.FragmentPagerPlanOverViewBinding
    private lateinit var createWorkOutPlanActivty : CreateWorkOutPlanActivty

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager_plan_over_view, container, false)
        initialization()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialization() {
        binding.tvMyWorkoutOverview.addTextChangedListener(object : TextWatcher {
           override fun afterTextChanged(s: Editable?) {
               if (!s.toString().isEmpty()) {
                   // set data india method which call on activity
                   createWorkOutPlanActivty.fragmentthree_overview(s.toString())
               }
           }
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
       })

        binding.tvMyWorkoutOverview.setOnTouchListener({ v, event ->
            if (binding.tvMyWorkoutOverview.hasFocus()) {
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
                when (event.getAction() and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                        binding.scrollView.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                }
            }
            false
        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createWorkOutPlanActivty = activity as CreateWorkOutPlanActivty
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (createWorkOutPlanActivty != null) {
            createWorkOutPlanActivty.setOverViewListenr(this)
        }
    }

    override fun getDietPlanData(overView: String) {
        if(overView != null && !overView.isEmpty()){
            binding.tvMyWorkoutOverview.setText(""+overView)
        }
    }

}

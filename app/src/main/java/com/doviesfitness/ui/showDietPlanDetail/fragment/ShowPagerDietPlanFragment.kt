package com.doviesfitness.ui.showDietPlanDetail.fragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentShowPagerDietPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPlanWebViewFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.adapter.AddDietPlanAdapter
import com.doviesfitness.ui.createAndEditDietPlan.fragment.PagerDietPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.ui.showDietPlanDetail.adapter.ShowAddDietPlanAdapter
import com.doviesfitness.utils.ItemOffsetDecoration

class ShowPagerDietPlanFragment : BaseFragment(),
    ShowDietPlanDetailActivity.GetProgramDietPlanLitener,
    ShowAddDietPlanAdapter.MyDietPlanLIstener {

    lateinit var binding: FragmentShowPagerDietPlanBinding
    private lateinit var showDietPlanDetailActivity: ShowDietPlanDetailActivity
    private lateinit var adapter: ShowAddDietPlanAdapter
    private var addDietPlanList = ArrayList<DietPlanSubCategoryModal.Data.DietListing>()

    private var mLastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_pager_diet_plan,
            container,
            false
        )
        initialization()
        return binding.root
    }

    private fun initialization() {
        /* val loutManager =
             androidx.recyclerview.widget.GridLayoutManager(binding.addDpRv.context, 2);
         val spacingInPixels = resources.getDimensionPixelSize(R.dimen._1sdp)*/

        binding.addDpRv.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(binding.addDpRv.context, 2)
        binding.addDpRv.addItemDecoration(ItemOffsetDecoration(mContext, R.dimen._8sdp))

        /*binding.addDpRv.addItemDecoration(MyItemDecoration(spacingInPixels))
        binding.addDpRv.layoutManager = loutManager*/

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showDietPlanDetailActivity != null) {
            showDietPlanDetailActivity.setDietPlanListenr(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showDietPlanDetailActivity = context as ShowDietPlanDetailActivity
    }

    //saperation
    class MyItemDecoration(space: Int) :
        androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space / 2
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == 0 || position == 1) {
            } else {
                outRect.top = space * 2
            }
            if (position % 2 == 0) {
                outRect.right = space
            } else {
                outRect.left = space
            }

            if (position == parent.getAdapter()!!.getItemCount() - 1 || position == parent.getAdapter()!!.getItemCount() - 2) {
                outRect.bottom = 10
            } else {
                outRect.bottom = 0
            }
        }
    }

    override fun getDietPlanData(dietPlanData: List<ShowWorkoutDetailModel.Data.GetProgramDietPlan>) {
        addDietPlanList.clear()
        if (dietPlanData != null) {

            for (i in dietPlanData.indices) {
                val dietPlanList = dietPlanData.get(i)

                val dietDietPlanLISt = DietPlanSubCategoryModal.Data.DietListing(
                    diet_plan_id = dietPlanList.program_diet_id,
                    diet_plan_title = dietPlanList.program_diet_name,
                    diet_plan_pdf = dietPlanList.diet_plan_pdf,
                    diet_plan_full_image = dietPlanList.program_diet_image,
                    diet_plan_image = dietPlanList.program_diet_image
                )

                addDietPlanList.add(dietDietPlanLISt)
            }

            if (addDietPlanList == null || addDietPlanList.size == 0) {
                binding.txtNoDietPlan.visibility = View.VISIBLE
            } else {
                binding.txtNoDietPlan.visibility = View.GONE
            }

            adapter = ShowAddDietPlanAdapter(mContext, addDietPlanList, this)
            binding.addDpRv.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun getDietPlanInfo(
        data: DietPlanSubCategoryModal.Data.DietListing,
        position: Int,
        whichClick: String
    ) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        // this velue send from adapters india 0 and 1 forms
        // when equal to 1 means you can delete on 0 you can go other fragments
        if (whichClick.equals("0")) {
            //val dietPlanWebViewFragment = DietPlanWebViewFragment()
            val showWebviewFragment = ShowWebviewFragment()
            val args = Bundle()
            args.putParcelable("fromShowDetail", data)
            //args.putParcelable("webViewData", data)
            showWebviewFragment.setArguments(args)
            getBaseActivity()?.addFragment(showWebviewFragment, R.id.container_id, true)
        }
    }
}

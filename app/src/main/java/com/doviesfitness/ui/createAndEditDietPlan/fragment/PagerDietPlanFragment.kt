package com.doviesfitness.ui.createAndEditDietPlan.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentPagerDietPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.activity.Cretat_DietPlanActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.create_DietPlanWebViewActivity
import com.doviesfitness.ui.createAndEditDietPlan.adapter.AddDietPlanAdapter
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.utils.ItemOffsetDecoration


class PagerDietPlanFragment : BaseFragment(), View.OnClickListener, AddDietPlanAdapter.AddDietPlanLIstener ,CreateWorkOutPlanActivty.GetProgramDietPlanLitener {

    lateinit var binding: FragmentPagerDietPlanBinding
    private lateinit  var createWorkOutPlanActivty: CreateWorkOutPlanActivty
    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    private var mLastClickTime: Long = 0

    private lateinit var adapter: AddDietPlanAdapter
    private var addDietPlanList = ArrayList<DietPlanSubCategoryModal.Data.DietListing>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createWorkOutPlanActivty = context as CreateWorkOutPlanActivty
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager_diet_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        // get instance of activity
        createWorkOutPlanActivty = activity as CreateWorkOutPlanActivty

        /*val loutManager = androidx.recyclerview.widget.GridLayoutManager(binding.addDpRv.context, 2);
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._1sdp)
*/

        binding.addDpRv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.addDpRv.context, 2)
        binding.addDpRv.addItemDecoration(ItemOffsetDecoration(mContext, R.dimen._8sdp))
        //binding.addDpRv.layoutManager = loutManager

        binding.ivApple.setOnClickListener(this)
        binding.ivAddBtn.setOnClickListener(this)

        // for check list data and show icon according to list
        if (addDietPlanList.size != 0) {
            binding.ivApple.visibility = View.GONE
            binding.tvMyWorkout.visibility = View.GONE
            binding.ivAddBtn.visibility = View.VISIBLE
        } else {
            binding.ivApple.visibility = View.VISIBLE
            binding.tvMyWorkout.visibility = View.VISIBLE
            binding.ivAddBtn.visibility = View.GONE
        }

        //createWorkOutPlanActivty.fragmenttwo_dietPlan()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (createWorkOutPlanActivty != null) {
            createWorkOutPlanActivty.setDietPlanListenr(this)
        }
    }


    override fun onResume() {
        super.onResume()
        // for check list data and show icon according to list
        // for check list data and show icon according to list
        if (addDietPlanList.size != 0) {
            binding.ivApple.visibility = View.GONE
            binding.tvMyWorkout.visibility = View.GONE
            binding.ivAddBtn.visibility = View.VISIBLE
        } else {
            binding.ivApple.visibility = View.VISIBLE
            binding.tvMyWorkout.visibility = View.VISIBLE
            binding.ivAddBtn.visibility = View.GONE
        }
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_apple -> {

                // if list size more then 0 then send list for check status otherwise no need
                if (addDietPlanList.size != 0) {
                    val intent = Intent(mContext, Cretat_DietPlanActivity::class.java)
                    intent.putExtra("addDietPlanList", addDietPlanList)
                    startActivityForResult(intent, 2)
                } else {
                    val intent = Intent(mContext, Cretat_DietPlanActivity::class.java)
                    intent.putExtra("FromAddDietPlan", "0")
                    startActivityForResult(intent, 2)
                }
            }


            R.id.iv_add_btn -> {
                // if list size more then 0 then send list for check status otherwise no need
                if (addDietPlanList.size != 0) {
                    val intent = Intent(mContext, Cretat_DietPlanActivity::class.java)
                    intent.putExtra("addDietPlanList", addDietPlanList)
                    startActivityForResult(intent, 2)
                } else {
                    val intent = Intent(mContext, Cretat_DietPlanActivity::class.java)
                    intent.putExtra("FromAddDietPlan", "0")
                    startActivityForResult(intent, 2)
                }
            }
        }
    }

    //saperation
    class MyItemDecoration(space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space / 2
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && data != null) {
            dietPlanSubCatgory = data.getParcelableExtra<DietPlanSubCategoryModal.Data.DietListing>("addDietPlan")!!

            if (dietPlanSubCatgory.isAdd.equals("isAdd")) {
                addDietPlanList.add(dietPlanSubCatgory)
                adapter = AddDietPlanAdapter(mContext, addDietPlanList, this)
                binding.addDpRv.adapter = adapter
                // set data for activity
                createWorkOutPlanActivty.fragmenttwo_dietPlan(addDietPlanList)
            } else {
                for (i in 0..addDietPlanList.size - 1) {
                    if (addDietPlanList.get(i).diet_plan_id.equals(dietPlanSubCatgory.diet_plan_id)) {
                        addDietPlanList.removeAt(i)
                        adapter = AddDietPlanAdapter(mContext, addDietPlanList, this)
                        binding.addDpRv.adapter = adapter
                        break
                    }
                }

                // for check list data and show icon according to list
                if (addDietPlanList.size != 0) {
                    binding.ivApple.visibility = View.GONE
                    binding.tvMyWorkout.visibility = View.GONE
                    binding.ivAddBtn.visibility = View.VISIBLE
                } else {
                    binding.ivApple.visibility = View.VISIBLE
                    binding.tvMyWorkout.visibility = View.VISIBLE
                    binding.ivAddBtn.visibility = View.GONE
                }
            }

            createWorkOutPlanActivty.fragmenttwo_dietPlan(addDietPlanList)
            adapter.notifyDataSetChanged()
        }
    }

    //listener get data from adapter
    override fun getAddDietPlanInfo(data: DietPlanSubCategoryModal.Data.DietListing, position: Int, whichClick: String) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
        }

        if (whichClick.equals("0")) {
            val intent = Intent(mContext, create_DietPlanWebViewActivity::class.java)
            intent.putExtra("weburl", data)
            mContext.startActivity(intent)
        } else if (whichClick.equals("1")) {
            addDietPlanList.removeAt(position)
            adapter.notifyDataSetChanged()

            if (addDietPlanList.size != 0) {
                binding.ivApple.visibility = View.GONE
                binding.tvMyWorkout.visibility = View.GONE
                binding.ivAddBtn.visibility = View.VISIBLE
            } else {
                binding.ivApple.visibility = View.VISIBLE
                binding.tvMyWorkout.visibility = View.VISIBLE
                binding.ivAddBtn.visibility = View.GONE
            }
        }


        createWorkOutPlanActivty.fragmenttwo_dietPlan(addDietPlanList)

    }

    override fun getDietPlanData(dietPlanData: List<ShowWorkoutDetailModel.Data.GetProgramDietPlan>) {
        if(dietPlanData != null){

            for(i in dietPlanData.indices){
                val dietPlanList = dietPlanData.get(i)

                val dietDietPlanLISt = DietPlanSubCategoryModal.Data.DietListing(
                    diet_plan_id = dietPlanList.program_diet_id,
                    diet_plan_title = dietPlanList.program_diet_name,
                    diet_plan_pdf = dietPlanList.diet_plan_pdf,
                    diet_plan_full_image = dietPlanList.program_diet_image,
                    diet_plan_access_level = "",
                    diet_plan_amount = "",
                    diet_plan_amount_display = "",
                    diet_plan_description = "",
                    diet_plan_image = dietPlanList.program_diet_image,
                    diet_plan_share_url = "",
                    isAdd ="")

                addDietPlanList.add(dietDietPlanLISt)
            }

            adapter = AddDietPlanAdapter(mContext, addDietPlanList, this)
            binding.addDpRv.adapter = adapter
            adapter.notifyDataSetChanged()

            if (addDietPlanList.size != 0) {
                binding.ivApple.visibility = View.GONE
                binding.tvMyWorkout.visibility = View.GONE
                binding.ivAddBtn.visibility = View.VISIBLE
            } else {
                binding.ivApple.visibility = View.VISIBLE
                binding.tvMyWorkout.visibility = View.VISIBLE
                binding.ivAddBtn.visibility = View.GONE
            }


            createWorkOutPlanActivty.fragmenttwo_dietPlan(addDietPlanList)
        }
    }
}

package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentOtherMediaStreamBinding
import com.doviesfitness.databinding.FragmentStreamVideoBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanDetailActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanStreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPCategoryDetailFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutPlanDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.otherMediaList
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.OtherMediaAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanModal
import com.doviesfitness.ui.createAndEditDietPlan.activity.OtherMediaDietPlanWebViewActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.create_DietPlanWebViewActivity
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.ui.showDietPlanDetail.fragment.ShowWebviewFragment
import com.doviesfitness.utils.Constant

class OtherMediaFragment : BaseFragment(), OtherMediaAdapter.MediaClick {
    override fun onMediaClick(pos:Int) {
        if (otherMediaList.get(pos).type.equals("1")){
            val intent = Intent(mContext, WorkOutDetailActivity::class.java)
            intent.putExtra("PROGRAM_DETAIL",otherMediaList.get(pos).media_id)
            intent.putExtra("isMyWorkout","no")
            intent.putExtra("fromDeepLinking", "")
            mContext.startActivity(intent)
            }
        else if (otherMediaList.get(pos).type.equals("2")){
            ///:Diet Plan

            var MData=otherMediaList.get(pos)

            var  data=DietPlanCateGoriesModal.Data.DietPlanCategory("",MData.media_id,"",MData.media_name)

            var d1=DietPlanSubCategoryModal.Data.DietListing(MData.exercise_access_level,MData.exercise_amount,MData.exercise_amount_display,MData.media_description,
                MData.media_image,MData.media_id,MData.media_image,MData.pdf,"",MData.media_name,"")
            val intent = Intent(mContext, DietPlanStreamDetailActivity::class.java)
            intent.putExtra("data", d1)
            mContext.startActivity(intent)



        }
       else if (otherMediaList.get(pos).type.equals("3")){
            ///Program
           /* val intent = Intent(mContext, ShowDietPlanDetailActivity::class.java)
            intent.putExtra("FromListToSeeDetail", otherMediaList.get(pos).media_id)
            startActivity(intent)*/

           var data= WorkOutPlanModal.Data.GetAllProgram("","",otherMediaList.get(pos).media_id,otherMediaList.get(pos).media_image,
               otherMediaList.get(pos).media_name,"","")
            val intent = Intent(mContext, WorkOutPlanDetailActivity::class.java)
            intent.putExtra("FROM_WORKOUT_PLAN", data)
            intent.putExtra("from", "other media")
            mContext.startActivity(intent)

            }

        else if (otherMediaList.get(pos).type.equals("5")){
            var mediaData=otherMediaList.get(pos);
            ///Other PDF
            var data=DietPlanSubCategoryModal.Data.DietListing("","","",mediaData.media_description,
                "",mediaData.media_id,"",mediaData.pdf,"",mediaData.media_name,"")

            val intent = Intent(mContext, OtherMediaDietPlanWebViewActivity::class.java)
            intent.putExtra("weburl", data)
            mContext.startActivity(intent)
            }
        else{

        }
    }


    lateinit var binding: FragmentOtherMediaStreamBinding
   lateinit var adapter:OtherMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_media_stream, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {

        if (otherMediaList.size>0){
            if (otherMediaList.get(0).type.equals("4")){

            }

            else{
                var layoutManager= GridLayoutManager(context, 2)
                adapter = OtherMediaAdapter(mContext, otherMediaList,this)
                binding.videoRv.layoutManager = layoutManager
                binding.videoRv.adapter = adapter
                val spacing = Constant.deviceSize(activity!!) / 2
              //  binding.videoRv.setPadding(spacing, spacing, spacing, spacing)
            //    binding.videoRv.setClipToPadding(false)
             //   binding.videoRv.setClipChildren(false)
/*
                binding.videoRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.set(spacing, spacing, spacing, spacing)
                    }
                })
*/

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val streamDetailActivity = context as StreamDetailActivity
        if (streamDetailActivity != null) {
           // streamDetailActivity.setUpdateVideoListListener(this)
        }
    }
}

package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.StreamOverviewFragmentBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.utils.Utility

class StreamOverviewFragment :BaseFragment(){
    lateinit var binding:StreamOverviewFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.stream_overview_fragment, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
      /*  Log.d("heights__", "for fullcell: ${Utility.convertPixelToSdp(activity!!,330)}")
        Log.d("heights__", "for headder : ${Utility.convertPixelToSdp(activity!!,44)}")
        Log.d("heights__", "for desc  : ${Utility.convertPixelToSdp(activity!!,34)}")
        Log.d("heights__", "for divider to hadding   : ${Utility.convertPixelToSdp(activity!!,74)}")
        Log.d("heights__", "for in desc lines gapgs   : ${Utility.convertPixelToSdp(activity!!,31)}")
        Log.d("heights__", "for b/w hadder and desc    : ${Utility.convertPixelToSdp(activity!!,41)}")*/

      /*Utility.convertPixelToSdpAndSetToLinerLayout(activity!!,330,binding.levelLayout)
      Utility.convertPixelToSdpAndSetToTextView(activity!!,44,binding.tvLevel)
      Utility.convertPixelToSdpAndSetToTextView(activity!!,34,binding.tvLevelDesc)*/


    }

    public fun setData(overViewTrailerData: StreamWorkoutDetailModel.Settings.Data) {

        if (overViewTrailerData.good_for_name!=null&&!overViewTrailerData.good_for_name.isEmpty())
        {
            binding.goodForLayout.visibility=View.VISIBLE
            binding.tvGoodFor.text=(overViewTrailerData.good_for_name.replace("|",", "))
        }
        else{
            binding.goodForLayout.visibility=View.GONE
        }
        if (overViewTrailerData.equipment_name!=null&&!overViewTrailerData.equipment_name.isEmpty()) {
            binding.equipmentLayout.visibility = View.VISIBLE
            binding.tvEquipment.text=(overViewTrailerData.equipment_name.replace("|",", "))
        }
        else{
            binding.equipmentLayout.visibility = View.GONE
        }
        if (overViewTrailerData.workout_level!=null&&!overViewTrailerData.workout_level.isEmpty()) {
            binding.levelLayout.visibility = View.VISIBLE
            binding.tvLevel.text=(overViewTrailerData.workout_level.replace("|",", "))
        }
        else{
            binding.levelLayout.visibility = View.GONE
        }
        if (overViewTrailerData.stream_workout_description!=null&&!overViewTrailerData.stream_workout_description.isEmpty()) {
            binding.descriptionLayout.visibility = View.VISIBLE
            binding.tvDescription.text=overViewTrailerData.stream_workout_description
        }
        else{
            binding.descriptionLayout.visibility = View.GONE
        }

    }
}
package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.activity_filter_exercise.*
import kotlinx.android.synthetic.main.filter_item_layout.view.*

class FilterExerciseAdapter(
    context: Context,
    exerciseList: ArrayList<FilterExerciseResponse.Data>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FilterExerciseAdapter.MyViewHolder>() {
    private var context: Context
    private var exerciseList = ArrayList<FilterExerciseResponse.Data>()

    init {
        this.context = context
        this.exerciseList = exerciseList
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
       val exerciseModal =  exerciseList.get(pos)

        val group_name = CommanUtils.capitalize(exerciseModal.group_name)

        holder.exerciseName.setText(group_name)
        if (exerciseModal.list.size > 0) {
            holder.equipment_rv.visibility = View.VISIBLE

            holder.equipment_rv.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(holder.equipment_rv.context, 2)
            holder.equipment_rv.addItemDecoration(ItemOffsetDecoration(context, R.dimen._2sdp))

            holder.equipment_rv.adapter = FilterEquipmentAdapter(context, exerciseModal.list as ArrayList<FilterExerciseResponse.Data.X>)
        } else {
            holder.equipment_rv.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.filter_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val equipment_rv = view.equipment_rv
        val exerciseName = view.exercise_name
    }
}




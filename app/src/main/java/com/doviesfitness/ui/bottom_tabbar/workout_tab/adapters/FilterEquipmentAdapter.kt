package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import kotlinx.android.synthetic.main.equipment_item_layout.view.*
import android.text.InputType
import com.doviesfitness.utils.CommanUtils


class FilterEquipmentAdapter(
    context: Context,
    exerciseList: ArrayList<FilterExerciseResponse.Data.X>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FilterEquipmentAdapter.MyViewHolder>() {
    private var context: Context
    private var exerciseList = ArrayList<FilterExerciseResponse.Data.X>()

    init {
        this.context = context
        this.exerciseList = exerciseList
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        val exerciseModal = exerciseList.get(pos)

        val  display_name  = CommanUtils.capitalize(exerciseModal.display_name)
        holder.exerciseName.setText(display_name)

        if (exerciseModal.isCheck) {
            //holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack1))
            holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
        } else {
            //holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorGray2))
            holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.boundry_checkbox))
        }
        holder.itemLayout.setOnClickListener {
            if (exerciseModal.isCheck) {
                //holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorGray2))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.boundry_checkbox))
                exerciseModal.isCheck = false
            } else {
                //holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack1))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
                exerciseModal.isCheck = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.equipment_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val exerciseName = view.exercise_name
        val checkbox = view.checkbox
        val itemLayout = view.item_layout
    }
}

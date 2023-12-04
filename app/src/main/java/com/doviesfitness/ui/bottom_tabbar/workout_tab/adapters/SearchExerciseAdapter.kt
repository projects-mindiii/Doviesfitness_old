package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import kotlinx.android.synthetic.main.search_item_layout.view.*
import java.util.ArrayList

class SearchExerciseAdapter(context: Context, exerciseList: ArrayList<FilterExerciseResponse.Data.X>,create:String,listener:OnItemClick): androidx.recyclerview.widget.RecyclerView.Adapter<SearchExerciseAdapter.MyViewHolder>(),
    Filterable {
    private var context: Context
   private  var exerciseList=ArrayList<FilterExerciseResponse.Data.X>()
   private  var exerciseTempList=ArrayList<FilterExerciseResponse.Data.X>()
   private  var tempList=ArrayList<FilterExerciseResponse.Data.X>()
    lateinit var listener:OnItemClick
    var create=""
    init {
        this.context=context
        this.create=create
        this.listener=listener
      //  this.exerciseList=exerciseList
        this.exerciseTempList=exerciseList
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.displayName.setText(exerciseList.get(pos).display_name)
        holder.groupName.setText(exerciseList.get(pos).group_name)

        holder.itemLayout.setOnClickListener(){
            tempList.clear()
            tempList.add(exerciseList.get(pos))
            listener.onItemClick("",tempList,"","")

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.search_item_layout,parent,false))
    }
    override fun getItemCount(): Int {
       // return  10
        return exerciseList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  displayName = view.display_name
        val  groupName = view.group_name
        val  itemLayout = view.item_layout
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val filterResults = Filter.FilterResults()
                if (constraint != null) {
                    if (constraint.length > 0) {
                        val mExerciseFilter = ArrayList<FilterExerciseResponse.Data.X>()
                        for (exercise in exerciseTempList) {
                            if (exercise.display_name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                mExerciseFilter.add(exercise)
                            }
                        }
                        filterResults.values = mExerciseFilter
                    }
                    else {
                        filterResults.values =  ArrayList<FilterExerciseResponse.Data.X>()

                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                exerciseList = results.values as ArrayList<FilterExerciseResponse.Data.X>
                notifyDataSetChanged()
            }
        }
    }
interface OnItemClick{
    public fun onItemClick(
        s: String,
        tempList: ArrayList<FilterExerciseResponse.Data.X>,
        s1: String,
        s2: String
    )
}
}
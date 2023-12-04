package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.exercise_listing.ExerciseListActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ExerciseDetailListActivityNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseLibResponse
import kotlinx.android.synthetic.main.featured_view.view.*
import kotlinx.android.synthetic.main.item_exercise_library1.view.*

class ExerciseLibraryAdapter(
    context: Context,
    exerciseList: ArrayList<ExerciseLibResponse.Data>,
    from: String,
    listener: OnItemCLick,
    hideView: View
):
    androidx.recyclerview.widget.RecyclerView.Adapter<ExerciseLibraryAdapter.MyViewHolder>(){
    private var context:Context
    private  var exerciseList=ArrayList<ExerciseLibResponse.Data>()
    private var mLastClickTime: Long = 0
    var from=""
    lateinit var listener:OnItemCLick
    lateinit var hideView: View

    init {
        this.context=context
        this.exerciseList=exerciseList
        this.from=from
        this.listener=listener
        this.hideView=hideView
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
       holder.exerciseName.setText(exerciseList.get(pos).display_name.toUpperCase())
        Glide.with(holder.img).load(exerciseList.get(pos).image).into(holder.img)
        holder.itemLayout.setOnClickListener {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

            } else {
                mLastClickTime = SystemClock.elapsedRealtime();

                if (exerciseList.size>0)
                {
                    if (from.equals("create")) {
                        listener.onItemClick(
                            exerciseList.get(pos).exercise_category_id,
                            exerciseList.get(pos).display_name
                        )
                    }
                    else {
                        context.startActivity(
                            Intent(context, ExerciseDetailListActivityNew::class.java)
                                .putExtra("category_id", exerciseList.get(pos).exercise_category_id)
                                .putExtra("name", exerciseList.get(pos).display_name)
                                .putExtra("create", from)
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.item_exercise_library1,parent,false))
    }
    override fun getItemCount(): Int {
        return exerciseList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  img = view.exercise_img
        val  exerciseName = view.exercise_name
        val  itemLayout = view.item_layout
        val  tvNew = view.tv_new
    }

    interface OnItemCLick{
        public fun onItemClick(exercise_category_id: String, display_name: String)
    }
}




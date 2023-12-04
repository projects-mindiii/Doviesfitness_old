package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.CustomerListModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import kotlinx.android.synthetic.main.goodfor_item_layout.view.*

class CustomerListAdapter(context: Context, guestList: ArrayList<CustomerListModel.Data.DoviesGuest>,
                          usertList:ArrayList<CustomerListModel.Data.User>, WorkoutGroupList : java.util.ArrayList<CustomerListModel.Data.WorkoutGroup>, from:String,listener:OnSelectUser) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CustomerListAdapter.MyViewHolder>() {
    private var context: Context
    private var guestList = ArrayList<CustomerListModel.Data.DoviesGuest>()
    private var usertList = ArrayList<CustomerListModel.Data.User>()
    private var WorkoutGroupList = ArrayList<CustomerListModel.Data.WorkoutGroup>()
    var from=""
   lateinit var listener:OnSelectUser
    init {
        this.context = context
        this.guestList = guestList
        this.usertList = usertList
        this.WorkoutGroupList=WorkoutGroupList
        this.from=from
        this.listener=listener
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (from.equals("users")){
            holder.exerciseName.setText(usertList.get(pos).vName)
            if (usertList.get(pos).isCheck) {
              //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
            }
            else {
              //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive_check_box))
            }
            holder.itemLayout.setOnClickListener() {
                if (usertList.get(pos).isCheck) {
                 //   holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                    holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive_check_box))
                    usertList.get(pos).isCheck=false
                    listener.onUserSelect(pos,usertList.get(pos))
                }
                else {
                 //   holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                    holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
                    usertList.get(pos).isCheck=true
                    listener.onUserSelect(pos,usertList.get(pos))
                }
            }
        }
        else if (from.equals("publish")){
            holder.exerciseName.setText(WorkoutGroupList.get(pos).vGroupName)
            if (WorkoutGroupList.get(pos).isCheck) {
                //   holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
            }
            else {
                //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive_check_box))
            }
            holder.itemLayout.setOnClickListener() {
                if (WorkoutGroupList.get(pos).isCheck) {
                    //    holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                    holder.checkbox.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_inactiv_check_box
                        )
                    )
                    WorkoutGroupList.get(pos).isCheck = false
                } else {
                    for (i in 0..WorkoutGroupList.size - 1) {
                        if (i != pos) {
                            WorkoutGroupList.get(i).isCheck = false
                        }
                    }
                    //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                    holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
                    WorkoutGroupList.get(pos).isCheck = true
                    notifyDataSetChanged()
                }
            }
        }
        else{
            holder.exerciseName.setText(guestList.get(pos).vGuestName)
            if (guestList.get(pos).isCheck) {
             //   holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
            }
            else {
              //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive_check_box))
            }
            holder.itemLayout.setOnClickListener() {
                if (guestList.get(pos).isCheck) {
                //    holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorGray2))
                    holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive_check_box))
                    guestList.get(pos).isCheck=false
                }
                else {
                    for (i in 0..guestList.size-1){
                        if (i!=pos){
                            guestList.get(i).isCheck=false
                        }
                    }
                  //  holder.exerciseName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack1))
                    holder.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.right_checkbox))
                    guestList.get(pos).isCheck=true
                    notifyDataSetChanged()
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.goodfor_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        if (from.equals("users")){
         return usertList.size
        }
        else if (from.equals("publish")){
         return  WorkoutGroupList.size
        }
        else{
          return guestList.size
        }
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val exerciseName = view.exercise_name
        val checkbox = view.checkbox
        val itemLayout = view.item_layout
    }

    interface OnSelectUser{
        public fun onUserSelect(pos:Int,user:CustomerListModel.Data.User);
    }
}

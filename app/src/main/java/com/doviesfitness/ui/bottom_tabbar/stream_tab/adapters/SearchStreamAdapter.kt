package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.FavStreamModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.SearchStreamModel
import kotlinx.android.synthetic.main.collection_group_item_new_view.view.*

class SearchStreamAdapter(context: Context, StreamList:ArrayList<SearchStreamModel.Settings.Data>, listener:popularStreamWorkoutAdapter.OnItemCLick):
    androidx.recyclerview.widget.RecyclerView.Adapter<SearchStreamAdapter.MyViewHolder>(){
    private var context:Context
     var listener:popularStreamWorkoutAdapter.OnItemCLick
    lateinit var StreamList:ArrayList<SearchStreamModel.Settings.Data>

    init {
        this.context=context
        this.StreamList=StreamList
       this.listener=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.exerciseName.text=StreamList.get(pos).stream_workout_name
        holder.tvLevel.text=StreamList.get(pos).stream_workout_subtitle
        if (StreamList.get(pos).stream_workout_image!=null)
            if (StreamList.get(pos).display_new_tag.equals("YES",true)){
                holder.tvNewStream.visibility=View.VISIBLE
                holder.tvNewStream.text=""+StreamList.get(pos).display_new_tag_text
            }
            else{
                holder.tvNewStream.visibility=View.GONE
            }

        if((!StreamList.get(pos).stream_workout_name.isEmpty())&&(!StreamList.get(pos).stream_workout_subtitle.isEmpty()))
        {
            holder.rlGradient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)
        }

        Glide.with(context)
//            .load(StreamList.get(pos).stream_workout_image_url+"medium/"+StreamList.get(pos).stream_workout_image)
            .load(StreamList.get(pos).stream_workout_image_url+StreamList.get(pos).stream_workout_image)
            .error(ContextCompat.getDrawable(context,R.drawable.app_icon))
            .into(holder.img)

/*
        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
            ||"OPEN".equals(StreamList.get(pos).stream_workout_access_level, true) ) {
            holder.lockIcon.visibility = View.GONE
        }
        else {
            holder.lockIcon.visibility = View.VISIBLE

        }
*/


        holder.itemLayout.setOnClickListener(){
            listener.onItemCLick(pos,"",StreamList.get(pos).stream_workout_id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.collection_group_item_new_view,parent,false))

    }
    override fun getItemCount(): Int {
        return StreamList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  img = view.iv_featured
        val  exerciseName = view.tv_featured
        val  itemLayout = view.item_layout
        val  tvLevel = view.tv_level
        val  tvNewStream = view.tv_new_stream
        val  lockIcon = view.lock_icon
        val  tvNew = view.tv_new
        val  rlGradient = view.rl_gradient
    }

}




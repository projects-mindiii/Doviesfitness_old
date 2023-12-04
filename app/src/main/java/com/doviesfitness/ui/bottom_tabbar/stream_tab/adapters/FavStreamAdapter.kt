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
import kotlinx.android.synthetic.main.collection_group_item_new_view.view.*

class FavStreamAdapter(context: Context,favStreamList:ArrayList<FavStreamModel.Settings.Data.Favourite>,listener:popularStreamWorkoutAdapter.OnItemCLick):
    androidx.recyclerview.widget.RecyclerView.Adapter<FavStreamAdapter.MyViewHolder>(){
    private var context:Context
     var listener:popularStreamWorkoutAdapter.OnItemCLick
    lateinit var favStreamList:ArrayList<FavStreamModel.Settings.Data.Favourite>

    init {
        this.context=context
        this.favStreamList=favStreamList
       this.listener=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.exerciseName.text=favStreamList.get(pos).stream_workout_name
        holder.tvLevel.text=favStreamList.get(pos).stream_workout_subtitle

        if (favStreamList.get(pos).display_new_tag.equals("YES",true)){
            holder.tvNewStream.visibility=View.VISIBLE
            holder.tvNewStream.text=""+favStreamList.get(pos).display_new_tag_text
        }
        else{
            holder.tvNewStream.visibility=View.GONE

        }

/*
        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
            ||"OPEN".equals(favStreamList.get(pos).stream_workout_access_level, true) ) {
            holder.lockIcon.visibility = View.GONE
        }
        else {
            holder.lockIcon.visibility = View.VISIBLE

        }
*/
        if((favStreamList.get(pos).stream_workout_name.isEmpty())&&(favStreamList.get(pos).stream_workout_subtitle.isEmpty()))
        {

        }else{
            holder.rlGradient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)
        }


        if (favStreamList.get(pos).stream_workout_image!=null)
        Glide.with(context)
//            .load(favStreamList.get(pos).stream_workout_image_url+"medium/"+favStreamList.get(pos).stream_workout_image)
            .load(favStreamList.get(pos).stream_workout_image_url+favStreamList.get(pos).stream_workout_image)
            .error(ContextCompat.getDrawable(context,R.drawable.app_icon))
            .into(holder.img)

        holder.itemLayout.setOnClickListener(){
            if (pos<favStreamList.size)
            listener.onItemCLick(pos,"",favStreamList.get(pos).stream_workout_id)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.collection_group_item_new_view,parent,false))

    }
    override fun getItemCount(): Int {
        return favStreamList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  img = view.iv_featured
        val  exerciseName = view.tv_featured
        val  itemLayout = view.item_layout
        val  tvLevel = view.tv_level
        val  lockIcon = view.lock_icon
        val  tvNewStream = view.tv_new_stream
        val  rlGradient = view.rl_gradient
    }


}




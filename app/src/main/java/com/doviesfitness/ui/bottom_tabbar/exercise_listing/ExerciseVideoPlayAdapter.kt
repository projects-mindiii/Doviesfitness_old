package com.doviesfitness.ui.bottom_tabbar.exercise_listing

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import kotlinx.android.synthetic.main.play_video_layout.view.*
import java.io.File

class ExerciseVideoPlayAdapter(
    context: Context,
    exerciseListing: ArrayList<ExerciseListingResponse.Data>,
    listener: OnItemClick,
    myHolder: ExerciseListAdapter.MyViewHolder,parentPos:Int
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {


    private var context: Context;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    var listener:OnItemClick
    var  myHolder: ExerciseListAdapter.MyViewHolder
    var parentPos:Int

    init {
        this.context = context
        this.exerciseListing = exerciseListing
        this.listener=listener
        this.myHolder=myHolder
        this.parentPos=parentPos
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return exerciseListing.size;
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.play_video_layout, parent, false)
        )
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        val holder = rvHolder as MyViewHolder
        val lastIndex = exerciseListing.get(pos).exercise_video.lastIndexOf("/")

        if (lastIndex > -1) {
            val downloadFileName = exerciseListing.get(pos).exercise_video.substring(lastIndex + 1)
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)

            ExerciseListActivity.curroptedFile=f
            ExerciseListActivity.curroptedUrl=exerciseListing.get(pos).exercise_video


            if (!f.exists()) {
                holder.loaderLayout.visibility=View.VISIBLE
                holder.fl_vv.visibility=View.GONE
                var uri = Uri.parse(exerciseListing.get(pos).exercise_video)
                listener.videoDownload(true, exerciseListing.get(pos), holder,myHolder, pos, true,parentPos)
            }
            else {
                var uri = Uri.parse(path)
                if (getDataManager().getStringData(f.absolutePath) != null && !getDataManager().getStringData(f.absolutePath)!!.isEmpty() && f.length().toString().equals(getDataManager().getStringData(f.absolutePath))
                ) {
                    holder.loaderLayout.visibility=View.GONE
                    holder.fl_vv.visibility=View.VISIBLE
                    holder.dividerView.requestFocus()
                    listener.videoPlayClick(false,uri,pos,holder,myHolder,false,parentPos)

                } else {
                    holder.loaderLayout.visibility=View.VISIBLE
                    holder.fl_vv.visibility=View.GONE
                    listener.calculateVideoSize(exerciseListing.get(pos).exercise_video,uri,pos,holder,myHolder,f,parentPos)

                }
            }

        }
    }


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }
        val main_layout = view.main_layout
           val loaderLayout = view.loader_layout
           val fl_vv = view.fl_vv
           val dividerView = view.divider_view
    }

    public interface OnItemClick {
        fun videoPlayClick(isScroll: Boolean, data:Uri, position: Int, view: MyViewHolder,  myHolder: ExerciseListAdapter.MyViewHolder,isLoad: Boolean
        ,parentPos:Int)

        fun videoDownload(isScroll: Boolean, data: ExerciseListingResponse.Data, view: MyViewHolder, myHolder: ExerciseListAdapter.MyViewHolder, position: Int, isLoad: Boolean
        ,parentPos:Int)

        fun calculateVideoSize(videoUrl:String,uri:Uri,position: Int,holder:MyViewHolder, myHolder: ExerciseListAdapter.MyViewHolder,file:File,
                               parentPos:Int)

    }

}






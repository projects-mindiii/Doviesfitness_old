package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.stream_collection_dynamic_item_view.view.*

class StreamCollectionAdapter(context: Context, workoutList: ArrayList<StreamDataModel.Settings.Data.Collection>, listener: OnCollectionClick, dynamicListener: DynamicStreamWorkoutAdapter.OnDynamicWorkoutCLick) :
        androidx.recyclerview.widget.RecyclerView.Adapter<StreamCollectionAdapter.MyViewHolder>() {

    var mContext: Context
    lateinit var collectionList: ArrayList<StreamDataModel.Settings.Data.Collection>
    lateinit var listener: OnCollectionClick
    var dynamicListener: DynamicStreamWorkoutAdapter.OnDynamicWorkoutCLick

    init {
        this.mContext = context
        this.collectionList = workoutList;
        this.listener = listener
        this.dynamicListener = dynamicListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.stream_collection_dynamic_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return   collectionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (collectionList.size > 0) {

            if (collectionList.get(pos).show_in.equals("Square", true)) {
                holder.RitemView.visibility = View.VISIBLE
                holder.horizontalView.visibility = View.GONE


                //  holder.tvFeatured.setText(workoutList.get(pos).stream_workout_collection_title)
                if (collectionList.get(pos).stream_workout_collection_title != null && !collectionList.get(
                                pos
                        ).stream_workout_collection_title.isEmpty()
                ) {
                    holder.workoutCollectionName.visibility = View.VISIBLE
                    var name = CommanUtils.capitaliseName(collectionList.get(pos).stream_workout_collection_title)
                    holder.tvFeatured.setText("" + name)
                    holder.workoutCollectionName.setText("" + name)

                } else {
                    holder.workoutCollectionName.visibility = View.GONE
                    holder.tvFeatured.visibility = View.GONE
                }

                if (collectionList.get(pos).title_show_in_app.equals("Yes")) {
                    holder.tvFeatured.visibility = View.VISIBLE
                } else {
                    holder.tvFeatured.visibility = View.INVISIBLE

                }
                if (collectionList.get(pos).subtitle_show_in_app.equals("Yes")) {
                    holder.levelName.visibility = View.VISIBLE

                } else {
                    holder.levelName.visibility = View.INVISIBLE

                }
                if ((collectionList.get(pos).stream_workout_collection_subtitle.isEmpty()) && (collectionList.get(pos).stream_workout_collection_title.isEmpty())) {

                } else {
                    holder.ivGredientview.visibility = View.VISIBLE
                }



                if (collectionList.get(pos).stream_workout_collection_subtitle != null
                        && !collectionList.get(pos).stream_workout_collection_subtitle.isEmpty()
                ) {
                    var name =
                            (CommanUtils.capitaliseName(collectionList.get(pos).stream_workout_collection_subtitle))
                    holder.levelName.text = "" + name

                } else {
                    holder.levelName.visibility = View.INVISIBLE
                }

                Glide.with(holder.ivFeatured).load(
//                    collectionList.get(pos).stream_workout_collection_image_url + "medium/" + collectionList.get(
                        collectionList.get(pos).stream_workout_collection_image_url + collectionList.get(
                                pos
                        ).stream_workout_collection_image
                )
                        .into(holder.ivFeatured)
            } else {
                if (collectionList.get(pos).workout_list != null && collectionList.get(pos).workout_list.size > 0) {
                    holder.horizontalView.visibility = View.VISIBLE
                    holder.RitemView.visibility = View.GONE
                    holder.collectionName.text = "" + collectionList.get(pos).stream_workout_collection_title
                    var workoutList = ArrayList<StreamDataModel.Settings.Data.PopularCollectionWorkouts.Workout>()
                    workoutList.addAll(collectionList.get(pos).workout_list)
                    var popularWorkoutAdapter1 = DynamicStreamWorkoutAdapter(mContext!!, workoutList, dynamicListener, pos)

//                    val spacingInPixels = mContext.resources.getDimensionPixelSize(R.dimen._10sdp)
//                    val spacingInPixels2 = mContext.resources.getDimensionPixelSize(R.dimen._2sdp)

                    val recentLayoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(mContext,
                            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)

//                    holder.rvWorkout.addItemDecoration(
//                        StreamCollectionNew.SpacesItemDecoration(
//                            spacingInPixels,
//                            spacingInPixels2
//                        )
//                    )

                    holder.rvWorkout.layoutManager = recentLayoutManager1
                    holder.rvWorkout.adapter = popularWorkoutAdapter1
                } else {
                    holder.horizontalView.visibility = View.GONE
                    holder.RitemView.visibility = View.GONE
                }
            }

            holder.RitemView.setOnClickListener() {
                if (collectionList.size > 0)
                    listener.onCollectionClick(collectionList.get(pos), pos)
            }
            holder.viewAll.setOnClickListener() {
                if (collectionList.size > 0)
                    listener.onCollectionClick(collectionList.get(pos), pos)
            }
        }
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivFeatured = view.exercise_img
        val tvFeatured = view.exercise_name
        val levelName = view.level_name
        val tvNew = view.tv_new_stream
        val RitemView = view.item_view
        val horizontalView = view.horizontal_view
        val rvWorkout = view.rv_workout
        val viewAll = view.view_all
        val collectionName = view.first_collection_name
        val collectionNameLayout = view.collection_name_layout
        val workoutCollectionName = view.workout_collection_name
        val ivGredientview = view.iv_gredientview
    }

    interface OnCollectionClick {
        public fun onCollectionClick(data: StreamDataModel.Settings.Data.Collection, pos: Int)
    }

}

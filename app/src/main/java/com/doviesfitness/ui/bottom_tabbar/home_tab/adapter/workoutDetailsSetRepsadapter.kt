package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.R
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.ExerciseTransData
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutGroupsData
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.workout_details_exercise_itemview.view.rv_exercises
import kotlinx.android.synthetic.main.workout_details_exercise_itemview.view.tv_roundName
import kotlinx.android.synthetic.main.workout_details_exercise_itemview.view.tv_set_and_reps_count
import kotlinx.android.synthetic.main.workout_details_exercise_itemview.view.tv_title_roundNumber
import java.io.File

class workoutDetailsSetRepsadapter(
    context: WorkOutDetailActivity,
    setAndRepsExerciseList: java.util.ArrayList<WorkoutGroupsData>,
    listener: WorkOutItemClickListener,
    videowidth: Int,
    subListener: IsSubscribed,
    stopScroll: StopScroll

) :

    androidx.recyclerview.widget.RecyclerView.Adapter<workoutDetailsSetRepsadapter.MyViewHolder>() {
    private lateinit var exerciseAdapter: WorkoutDetailExerciseSetAndRepsAdapter
    var mContext: WorkOutDetailActivity

    // lateinit var workoutList: ArrayList<WorkoutExercise>
    var listener: WorkOutItemClickListener
    val videowidth: Int
    var tempPos = -1
    var tempRoundPos = -1
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var subListener: IsSubscribed
    var workoutList: java.util.ArrayList<WorkoutGroupsData>
    val stopScroll: StopScroll
    var tempExerciseItem: ExerciseTransData? = null
    lateinit var tempHolder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder

    init {
        this.mContext = context
        this.workoutList = setAndRepsExerciseList
        this.stopScroll = stopScroll
        this.listener = listener
        this.videowidth = videowidth
        this.subListener = subListener
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.workout_details_exercise_itemview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val roundData = workoutList[position]



        if (roundData.iGroupType == "0") {
            holder.tv_roundName.text = ""
            holder.tv_title_roundNumber.text = "Round ${position + 1}"
        } else if (roundData.iGroupType == "1") {
            holder.tv_title_roundNumber.text = "Round $${position + 1} - "
            holder.tv_roundName.text = "Superset"
        } else if (roundData.iGroupType == "2") {
            holder.tv_title_roundNumber.text = "Round ${position + 1} - "
            holder.tv_roundName.text = "Left & Right"
        }

        holder.tv_set_and_reps_count.text =
            "${roundData.iTargetSets} Sets Of ${roundData.iTargetReps} Reps"

        var list = roundData.groupSetsData[0].exerciseTransData
        setExerciseAdapter(holder, list, position)
    }

    private fun setExerciseAdapter(
        holder: MyViewHolder,
        list: List<ExerciseTransData>,
        parentPosition: Int
    ) {
        exerciseAdapter = WorkoutDetailExerciseSetAndRepsAdapter(
            mContext,
            list as ArrayList<ExerciseTransData>,
            object :
                WorkoutDetailExerciseSetAndRepsAdapter.WorkOutItemClickListener {
                override fun videoPlayClick(
                    isScroll: Boolean,
                    data: ExerciseTransData,
                    position: Int,
                    view: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
                    isLoad: Boolean,
                    player: Player?
                ) {
                    try {


                        if (player != null) {
                            player?.stop()
                            player?.release()
                            view.fl_vv.removeAllViews()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    listener.videoPlayClick(
                        isScroll,
                        data,
                        parentPosition,
                        position,
                        view,
                        isLoad,
                        player!!
                    )
                }

                override fun shareURL(data: ExerciseTransData) {
                    listener.shareURL(data)
                }

                override fun hidePlayer(
                    data: ExerciseTransData,
                    position: Int,
                    view: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
                    isLoad: Boolean,
                    player: Player?
                ) {

/*
                    if (position != tempPos || position != tempRoundPos) {
                        if (tempPos != -1 || tempRoundPos != -1) {
                            if (tempExerciseItem != null) {
                                if (tempPos != -1 && tempExerciseItem!!.isPlaying || tempRoundPos != -1) {
                                    //try {
                                    removeVideoView(tempPos, tempHolder)

                                    //For Open new Video Cell
                                    tempPos = position
                                    tempHolder = view
                                    tempRoundPos = position
                                    tempExerciseItem = data
                                }
                            }
                        }
                    }
*/

                    /*                    try {
                                            var flag =
                                                workoutList.get(parentPosition).groupSetsData[0].exerciseTransData[position].isPlaying
                                            for (i in 0 until workoutList.size) {
                                                var model = workoutList[i].groupSetsData[0].exerciseTransData
                                                for (j in model.indices) {
                                                    if (model[j].isPlaying) {
                                                        model[j].isPlaying = false
                                                    }
                                                }
                                            }
                                            workoutList[parentPosition].groupSetsData[0].exerciseTransData[position].isPlaying =
                                                !flag
                                            notifyDataSetChanged()
                                        } catch (E: Exception) {
                                            E.printStackTrace()
                                        }*/
                }

                override fun setFavUnfav(data: ExerciseTransData, position: Int, view: ImageView) {
                    listener.setFavUnfav(data, position, view)

                }

                override fun isDownloadedVideo(
                    workoutExerciseVideo: String,
                    childPosition: Int,
                    holder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
                    yPositionOnScreen: Float
                ) {


                    if (childPosition != tempPos || parentPosition != tempRoundPos) {
                        if (tempPos != -1 || tempRoundPos != -1) {
                            if (tempExerciseItem != null) {
                                if (tempPos != -1 && tempExerciseItem!!.isPlaying || tempRoundPos != -1) {
                                    //try {
                                    removeVideoView(tempPos, tempHolder)

                                    //For Open new Video Cell
                                    tempPos = childPosition
                                    tempHolder = holder
                                    tempRoundPos = parentPosition
                                    tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data
                                }else{
                                    //For Open new Video Cell
                                    tempPos = childPosition
                                    tempHolder = holder
                                    tempRoundPos = parentPosition
                                    tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data

                                }
                            }else{
                                //For Open new Video Cell
                                tempPos = childPosition
                                tempHolder = holder
                                tempRoundPos = parentPosition
                                tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data

                            }
                        }else{
                            //For Open new Video Cell
                            tempPos = childPosition
                            tempHolder = holder
                            tempRoundPos = parentPosition
                            tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data

                        }
                    }else{
                        //For Open new Video Cell
                        tempPos = childPosition
                        tempHolder = holder
                        tempRoundPos = parentPosition
                        tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data

                    }
                    if (tempExerciseItem==null){
                        tempExerciseItem = workoutList.get(parentPosition).groupSetsData[0].exerciseTransData.get(childPosition)//data
                    }





                    is_DownloadedVideo(
                        workoutExerciseVideo,
                        parentPosition,
                        childPosition,
                        holder,
                        yPositionOnScreen
                    )
                }

            },
            videowidth,
            object : IsSubscribed {
                override fun isSubscribed() {

                }
            })
        var layoutManager = LinearLayoutManager(mContext)
        holder.rvExercuise.layoutManager = layoutManager
        holder.rvExercuise.adapter = exerciseAdapter
    }


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var rvExercuise = view.rv_exercises
        var tv_title_roundNumber = view.tv_title_roundNumber
        var tv_roundName = view.tv_roundName
        var tv_set_and_reps_count = view.tv_set_and_reps_count


    }


    fun removeVideoView(pos: Int, holder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder) {

        if (player != null) {
            player?.release()
            tempExerciseItem!!.isPlaying = false

            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()

            if (this::exerciseAdapter.isInitialized){
                exerciseAdapter.notifyDataSetChanged()
            }

            //notifyadapters()
        }
    }


    interface WorkOutItemClickListener {
        fun videoPlayClick(
            isScroll: Boolean,
            data: ExerciseTransData,
            parentPosotion: Int,
            childPosition: Int,
            view: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
            isLoad: Boolean,
            player: Player?
        )

        fun shareURL(data: ExerciseTransData)
        fun setFavUnfav(data: ExerciseTransData, position: Int, view: ImageView)
    }

    interface StopScroll {
        fun stopScrolling(isScroll: Boolean)
        fun scrollToPosition(position: Int, y: Float)
    }


    fun is_DownloadedVideo(
        videoUrl: String,
        parentPosition: Int,
        childPosition: Int,
        holder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
        yPositionOnScreen: Float
    ) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    mContext.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)


            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)

                try {
                    if (player != null) {
                        player?.release()
                        holder.fl_vv.removeAllViews()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                intializePlayer(uri, parentPosition, childPosition, holder, true, yPositionOnScreen)
            } else {
                var uri = Uri.parse(path)
                try {
                    if (player != null) {
                        player?.release()
                        holder.fl_vv.removeAllViews()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                intializePlayer(
                    uri,
                    parentPosition,
                    childPosition,
                    holder,
                    false,
                    yPositionOnScreen
                )
            }
        }
    }

    fun intializePlayer(
        news_video: Uri,
        parentPosition: Int,
        childPosition: Int,
        holder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
        isload: Boolean,
        yPositionOnScreen: Float
    ) {

        var playerView = PlayerView(mContext)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (!workoutList[parentPosition].groupSetsData[0].exerciseTransData[childPosition].isPlaying) {
            holder.fl_vv.visibility = View.VISIBLE

            holder.iv_lock.rotation = 180f
            holder.fl_vv.addView(playerView)
            holder.fl_vv.requestFocus()
            playerView.setOnClickListener {
                holder.rl_exercise.performClick()
            }
            playerView.layoutParams.height = videowidth
            playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT

            trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            mediaDataSourceFactory =
                DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "mediaPlayerSample"))


            val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(news_video)

            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector!!)

            with(player!!) {
                prepare(mediaSource, false, false)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
                videoScalingMode =
                    Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;//repeat play video
            }

            playerView.setShutterBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorBlack
                )
            )
            playerView.player = player
            playerView.requestFocus()
            if (isload)
                playerView.setShowBuffering(true)
            lastSeenTrackGroupArray = null

            stopScroll.scrollToPosition(playerView.layoutParams.height, yPositionOnScreen)
            stopScroll.stopScrolling(false)

            listener.videoPlayClick(
                true,
                workoutList[parentPosition].groupSetsData[0].exerciseTransData[childPosition],
                parentPosition,
                childPosition,
                holder,
                isload,
                player!!
            )


        }
        else {
            holder.fl_vv.visibility = View.GONE
            listener.videoPlayClick(
                false,
                workoutList[parentPosition].groupSetsData[0].exerciseTransData[childPosition],
                parentPosition,
                childPosition,
                holder,
                false,
                player!!
            )
            player!!.release()
            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()

        }
        workoutList[parentPosition].groupSetsData[0].exerciseTransData.get(childPosition).isPlaying = !workoutList[parentPosition].groupSetsData[0].exerciseTransData.get(childPosition).isPlaying

        //listener.hidePlayer( workoutList[position], position, holder, false, player!!)

        /*    var flag = workoutList[position].isPlaying
            workoutList.forEach { it.isPlaying = false }
            // workoutList[position].isPlaying = !workoutList[position].isPlaying
            workoutList[position].isPlaying = !flag*/
    }
}

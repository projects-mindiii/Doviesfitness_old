package com.doviesfitness.chromecast.settings

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.doviesfitness.utils.CommanUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.chrome_cast_custom_dialog.*
import kotlinx.android.synthetic.main.custom_chrome_cast_item.view.*
import java.util.*

open class CustomChromeCastBottomSheetDialogNew(context: Context) : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = CustomChromeCastBottomSheetDialogNew::class.java.name
    var callBack: ChromeCastListener? = null
    private var mSelector = MediaRouteSelector.EMPTY
    private var mAdapter: CustomMediaAdapter? = null
    private var mAttachedToWindow = false
   var isCastConnected=false
    var isitemAdded = false
    var mcontext:Context?=null
    var deviceName=""
    var videoTitle=""
    var videoSubtitle=""
    var isPlaying:Boolean?=null
   var imageUri: Uri?=null
    var temproutes=ArrayList<MediaRouter.RouteInfo> ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chrome_cast_custom_dialog, container, false)
    }
    companion object {
        @JvmStatic
        var sInstance : RouteComparator? = null

        @JvmStatic
        fun newInstance(callBack:ChromeCastListener,context: Context,isConnected:Boolean,tempList:ArrayList<MediaRouter.RouteInfo>) =
            CustomChromeCastBottomSheetDialogNew(context).apply {
                arguments = Bundle().apply {
                    mAdapter = CustomMediaAdapter(context)
                    mcontext=context
                    temproutes=tempList
                    //mRouter = MediaRouter.getInstance(context)
                   // mCallback = MediaRouterCallback()
                    isCastConnected=isConnected
                    isitemAdded=false
                    setCastListener(callBack as ChromeCastListener)
                }
            }
    }

    fun setCastListener(callBack: ChromeCastListener) {
        this.callBack = callBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dialog?.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialog: DialogInterface?) {
                val d: BottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheetInternal: View = d.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED)

            }

        })

        disconnect.setOnClickListener(this)
        iv_cancle_dialog.setOnClickListener(this)
        stop_casting.setOnClickListener(this)
        top_layout.setOnClickListener(this)
        disconnect1.setOnClickListener(this)
        play_pause.setOnClickListener(this)
        mAdapter = CustomMediaAdapter(mcontext!!)
        //   mListView = findViewById<View>(R.id.list_view) as ListView
        var layoutManager=LinearLayoutManager(mcontext)
        recycle_view.layoutManager=layoutManager
        recycle_view.setAdapter(mAdapter!!)
        data_layout.visibility = View.VISIBLE
        no_device_found_layout.visibility = View.GONE
        loader_layout.visibility = View.GONE

        if (isCastConnected){
            disconnect.visibility=View.VISIBLE
            title.visibility=View.GONE
            seperator_line.visibility=View.GONE
            media_layout.visibility=View.VISIBLE

            device_name.setText(CommanUtils.capitalize(deviceName))
            if (videoTitle.isNotEmpty()||videoSubtitle.isNotEmpty()||imageUri!=null||isPlaying!=null) {
                cast_title.setText(CommanUtils.capitalize(videoTitle))
                subtitle.setText(CommanUtils.capitalize(videoSubtitle))
                try {
                    Glide.with(getBaseActivity()).load(imageUri).into(video_thumb)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                try {
                    if (isPlaying!!) {
                        Glide.with(getBaseActivity()).load(R.drawable.ic_pause_ico).into(play_pause)
                    } else {
                        Glide.with(getBaseActivity()).load(R.drawable.play_ico).into(play_pause)
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
            else{
                top_layout.visibility=View.INVISIBLE
                stop_casting.visibility=View.GONE
            }
        }
        else{
            disconnect.visibility=View.GONE
            title.visibility=View.VISIBLE
            seperator_line.visibility=View.VISIBLE
            if (temproutes!=null&& temproutes.size>0)
            {
                data_layout.visibility = View.VISIBLE
                no_device_found_layout.visibility = View.GONE
            }
            else{
                data_layout.visibility = View.GONE
                no_device_found_layout.visibility = View.VISIBLE
            }

        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.disconnect -> {
               // mRouter!!.unselect(0)
                callBack?.chromeCastListener("disconnect")

                dismiss()
            }
            R.id.disconnect1 -> {
              //  mRouter!!.unselect(0)
                callBack?.chromeCastListener("disconnect")
                dismiss()
            }
            R.id.iv_cancle_dialog -> {
                dismiss()
            }
            R.id.stop_casting -> {
                callBack?.chromeCastListener("stop casting")
                dismiss()
            }
            R.id.top_layout -> {
                callBack?.chromeCastListener("start play")
                dismiss()
            }
            R.id.play_pause -> {
                callBack?.chromeCastListener("play pause")

            }
        }
    }
    open fun onFilterRoute(route: MediaRouter.RouteInfo): Boolean {
        return !route.isDefault && route.matchesSelector(mSelector)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    interface ChromeCastListener {
        fun chromeCastListener(message:String)
        fun setRouter(route:MediaRouter.RouteInfo)
    }

    public fun refreshRoutes2(list:ArrayList<MediaRouter.RouteInfo>) {
        if (mAttachedToWindow) {
            temproutes=list
            mAdapter?.notifyDataSetChanged()
        }
    }

    fun setDisconnectData(friendlyName: String?, title: String?, subtitle: String?, playing: Boolean?,uri: Uri?) {
        deviceName=""+friendlyName
        videoTitle=""+title
        videoSubtitle=""+subtitle
        if (uri!=null)
        imageUri=uri
        if (playing != null) {
            isPlaying=playing
        }
    }

    fun setPlayPauseIcon(playPause: String) {
        if (playPause.equals("play")){
          //  Glide.with(getBaseActivity()).load(R.drawable.exo_icon_play).into(play_pause)
            Glide.with(getBaseActivity()).load(R.drawable.play_ico).into(play_pause)
        }
        else{
            Glide.with(getBaseActivity()).load(R.drawable.ic_pause_ico).into(play_pause)
        }

    }

    public inner class RouteComparator : Comparator<MediaRouter.RouteInfo?> {
        init {
             sInstance = RouteComparator()
        }


        override fun compare(lhs: MediaRouter.RouteInfo?, rhs: MediaRouter.RouteInfo?): Int {
            return lhs?.name?.compareTo(rhs!!.name)!!
        }
    }

inner class CustomMediaAdapter(context: Context): androidx.recyclerview.widget.RecyclerView.Adapter<CustomMediaAdapter.MyViewHolder>(){
    private var context:Context
    init {
        this.context=context
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        var item = temproutes.get(pos)
        holder.title.setText("" + item.name)
        // holder.subtitle.setText(""+item.description)
        holder.itemLayout.setOnClickListener {
            if (item!!.isEnabled) {
                // route.select();
                if (!isCastConnected) {
                    loader.visibility = View.VISIBLE
                    callBack?.setRouter(item);
                } else
                    dismiss()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.custom_chrome_cast_item,parent,false))

    }

    override fun getItemCount(): Int {
        return temproutes.size
    }
   inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  title = view.title
        val  subtitle = view.subtitle
        val  itemLayout = view.item_layout
    }
}

}

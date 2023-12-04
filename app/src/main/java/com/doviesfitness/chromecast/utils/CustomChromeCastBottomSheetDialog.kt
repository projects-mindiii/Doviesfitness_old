package com.doviesfitness.chromecast.utils

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.google.android.exoplayer2.util.Log
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.chrome_cast_custom_dialog.*
import java.util.*

open class CustomChromeCastBottomSheetDialog( context: Context) : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = CustomChromeCastBottomSheetDialog::class.java.name
    var callBack: ChromeCastListener? = null
    ///
    private var mRouter: MediaRouter? = null
    private var mCallback: MediaRouterCallback? = null
    private var mSelector = MediaRouteSelector.EMPTY
    private var mAdapter: RouteAdapter? = null
 //   private val mListView: ListView? = null
    private var mAttachedToWindow = false
   var isCastConnected=false
    var isitemAdded = false
    var mcontext:Context?=null
    var deviceName=""
    var videoTitle=""
    var videoSubtitle=""
    var isPlaying:Boolean?=null
   var imageUri: Uri?=null

    init {
        mRouter = MediaRouter.getInstance(context)
        mCallback = MediaRouterCallback()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chrome_cast_custom_dialog, container, false)

    }


    companion object {
        @JvmStatic
        var sInstance : RouteComparator? = null


        @JvmStatic
        fun newInstance(callBack:ChromeCastListener,context: Context,isConnected:Boolean) =
            CustomChromeCastBottomSheetDialog(context).apply {
                arguments = Bundle().apply {
                    mAdapter = RouteAdapter(context)
                    mcontext=context
                    mRouter = MediaRouter.getInstance(context)
                    mCallback = MediaRouterCallback()
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
        mAdapter = RouteAdapter(context)
        //   mListView = findViewById<View>(R.id.list_view) as ListView
        list_view.setAdapter(mAdapter)
        list_view.setOnItemClickListener(mAdapter)
      //  data_layout.visibility = View.GONE
     //   no_device_found_layout.visibility = View.VISIBLE
        if (isCastConnected){
            disconnect.visibility=View.VISIBLE
            title.visibility=View.GONE
            seperator_line.visibility=View.GONE
            media_layout.visibility=View.VISIBLE

            device_name.setText(deviceName)
            if (videoTitle.isNotEmpty()||videoSubtitle.isNotEmpty()||imageUri!=null||isPlaying!=null) {
                cast_title.setText(videoTitle)
                subtitle.setText(videoSubtitle)
                try {
                    Glide.with(getBaseActivity()).load(imageUri).into(video_thumb)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                try {
                    if (isPlaying!!) {
                        Glide.with(getBaseActivity()).load(R.drawable.ic_pause_ico).into(play_pause)
                    } else {
                        Glide.with(getBaseActivity()).load(R.drawable.exo_icon_play).into(play_pause)
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

        }
        //loader_layout.visibility=View.VISIBLE
        Handler().postDelayed(Runnable {
            if (data_layout!=null) {
                if (!isCastConnected) {
                    if (isitemAdded) {
                        //show in list
                        if (data_layout != null) {
                            data_layout.visibility = View.VISIBLE
                            no_device_found_layout.visibility = View.GONE
                        }
                    } else {
                        if (data_layout != null) {
                            data_layout.visibility = View.GONE
                            no_device_found_layout.visibility = View.VISIBLE
                        }
                        // show no device message
                    }
               }
                //  data_layout.visibility = View.GONE
                //  no_device_found_layout.visibility = View.VISIBLE
                loader_layout.visibility = View.GONE
            }

        },3000)


    }
     fun getRouteSelector(): MediaRouteSelector? {
        return mSelector
    }

     fun setRouteSelector(selector: MediaRouteSelector?) {
        requireNotNull(selector) { "selector must not be null" }
        if (mSelector != selector) {
            mSelector = selector
          //  if (mAttachedToWindow) {
                mCallback?.let { mRouter!!.removeCallback(it) }
                mCallback?.let {
                    mRouter!!.addCallback(selector,
                        it, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN)
                }
           // }
            refreshRoutes(true)
        }
    }

    override fun onDetach() {
        mAttachedToWindow = false
        mRouter!!.removeCallback(mCallback!!)
        super.onDetach()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAttachedToWindow = true
        mRouter!!.addCallback(mSelector,
            mCallback!!,
            MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
        )
        refreshRoutes(true)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.disconnect -> {
                mRouter!!.unselect(0)
                callBack?.chromeCastListener("disconnect")

                dismiss()
            }
            R.id.disconnect1 -> {
                mRouter!!.unselect(0)
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
    }

    private inner class RouteAdapter(context: Context?) :
        ArrayAdapter<MediaRouter.RouteInfo?>(context!!, 0),
        AdapterView.OnItemClickListener {
        private val mInflater: LayoutInflater
        fun update(isStart:Boolean) {
            clear()
            val routes: List<MediaRouter.RouteInfo> = mRouter!!.getRoutes()
            val count = routes.size
            for (i in 0 until count) {
                val route = routes[i]
                if (onFilterRoute(route)) {
                    add(route)
                    isitemAdded = true
                }
            }
            sort(sInstance!!)
            notifyDataSetChanged()
        }

        override fun areAllItemsEnabled(): Boolean {
            return false
        }

        override fun isEnabled(position: Int): Boolean {
            return getItem(position)!!.isEnabled
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            var view = convertView
            if (view == null) {
                view = mInflater.inflate(R.layout.custom_chrome_cast_item, parent, false)
            }
            val route = getItem(position)
            val text1 = view!!.findViewById<View>(R.id.title) as TextView
            val text2 = view.findViewById<View>(R.id.subtitle) as TextView
            val loader = view.findViewById<View>(R.id.loader) as ProgressBar
            val connectedIcon = view.findViewById<View>(R.id.connected_icon) as ImageView
            text1.text = route!!.name
           /* val description = route.description
          // Glide.with(mActivity).load(route.iconUri).into(connectedIcon)
            if (TextUtils.isEmpty(description)) {
                text2.visibility = View.GONE
                text2.text = ""
            } else {
                text2.visibility = View.VISIBLE
                text2.text = description
            }*/
            view.isEnabled = route.isEnabled
            view.setOnClickListener {
                val route = getItem(position)
                if (route!!.isEnabled) {
                    // route.select();
                    if (!isCastConnected) {
                        loader.visibility=View.VISIBLE
                        mRouter!!.selectRoute(route)
                    }
                    else
                        dismiss()
                 //   mRouter!!.unselect(0)
                 //    dismiss();
                }
            }
            return view
        }

        override fun onItemClick(
            parent: AdapterView<*>?,
            view: View,
            position: Int,
            id: Long
        ) {
            val route = getItem(position)
            if (route!!.isEnabled) {
                route.select()
                dismiss()
            }
        }

        init {
            mInflater = LayoutInflater.from(context)
        }
    }

    public fun refreshRoutes(isStart:Boolean) {
        if (mAttachedToWindow) {
            mAdapter?.update(isStart)
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
            Glide.with(getBaseActivity()).load(R.drawable.exo_icon_play).into(play_pause)
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

    private inner class MediaRouterCallback : MediaRouter.Callback() {
        override fun onRouteAdded(router: MediaRouter, info: MediaRouter.RouteInfo) {
            Log.d("root selected", "root added")

            refreshRoutes(false)
        }

        override fun onRouteRemoved(router: MediaRouter, info: MediaRouter.RouteInfo) {
            Log.d("root selected", "root Removed")

            refreshRoutes(false)
        }

        override fun onRouteChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {
            Log.d("root selected", "root canged")

            refreshRoutes(false)
        }

        override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo) {
            Log.d("root selected", "root selected")
            callBack?.chromeCastListener("selected")

        }
    }


}

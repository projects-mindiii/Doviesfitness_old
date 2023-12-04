package com.doviesfitness.chromecast.utils

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.chrome_cast_disconnect_stop_dialog.*

open class CustomChromeDisconnectStopDialog(context: Context) : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = CustomChromeDisconnectStopDialog::class.java.name
    var callBack: ChromeCastListener? = null

   var isCastConnected=false
    var isitemAdded = false
var deviceName=""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chrome_cast_disconnect_stop_dialog, container, false)

    }

    companion object {
        @JvmStatic
        fun newInstance(callBack:ChromeCastListener,context: Context,isConnected:Boolean,deviceName1:String) =
            CustomChromeDisconnectStopDialog(context).apply {
                arguments = Bundle().apply {

                    isCastConnected=isConnected
                    isitemAdded=false
                    deviceName=deviceName1
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
        title.setText(""+deviceName)

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callBack?.dismisDialog()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.disconnect -> {
                callBack?.chromeCastListener("disconnect")
                dismiss()
            }
            R.id.stop_casting -> {
                callBack?.chromeCastListener("stop")
                dismiss()
            }
            R.id.iv_cancle_dialog -> {
                dismiss()
            }
        }
    }
    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    interface ChromeCastListener {
        fun chromeCastListener(message:String)
        fun dismisDialog()
    }
}

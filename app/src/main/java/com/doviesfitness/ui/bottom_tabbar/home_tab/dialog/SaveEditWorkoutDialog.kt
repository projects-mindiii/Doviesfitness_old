package com.doviesfitness.ui.bottom_tabbar.home_tab.dialog


import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseBottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_image_picker.tv_cancel
import kotlinx.android.synthetic.main.dialog_save_edit_workout.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SaveEditWorkoutDialog : BaseBottomSheetDialog(), View.OnClickListener {
    val TAG = SaveEditWorkoutDialog::class.java.name
    var callBack: CommentCallBack? = null
    var TEXTVALUE = "TEXTVALUE"
    var positionKey = "Position"
    var from = ""
    //  lateinit var typeValue : Data1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_save_edit_workout, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_cancel.setOnClickListener(this)
        rl_saveworkout.setOnClickListener(this)
        edit_layout.setOnClickListener(this)

        if (from.equals("workout")) {
            tv_editworkout.text = getString(R.string.edit_workout)
            tv_saveworkout.text = getString(R.string.save_to_my_workout)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.dumble_dor).into(delete_icon)
        }
        else if (from.equals("myWorkout")) {
            tv_editworkout.text = getString(R.string.edit_workout)
            tv_saveworkout.text = getString(R.string.delete)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)


        }
        else if (from.equals("log")) {
            tv_editworkout.text = getString(R.string.edit)
            tv_saveworkout.text = getString(R.string.delete)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)


        }
        else if (from.equals("history create")) {

            tv_editworkout.text = getString(R.string.create_workout_log)
            tv_saveworkout.text = getString(R.string.delete)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)
        }
        else if (from.equals("history")) {
            edit_layout.visibility=View.GONE
            seperator.visibility=View.GONE
            tv_editworkout.text = getString(R.string.edit)
            tv_saveworkout.text = getString(R.string.delete)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)


        }
        else if (from.equals("image")) {
            tv_editworkout.text = getString(R.string.camera)
            tv_saveworkout.text = getString(R.string.gallery)
            Glide.with(mActivity).load(R.drawable.ic_photo_camera_stream).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_gallery_black).into(delete_icon)


        }
        else if (from.equals("publish")){
            tv_editworkout.text = getString(R.string.publish_to_app)
            tv_saveworkout.text = getString(R.string.delete)
            Picasso.with(mActivity).load(R.drawable.ico_publish).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)
            // Picasso.with(mActivity).load(R.drawable.ic_delete).into(delete_icon)

        }
        else {
            tv_editworkout.text = getString(R.string.save_as_new_workout)
            tv_saveworkout.text = getString(R.string.overwite_current_workout)
            Glide.with(mActivity).load(R.drawable.dumble_dor).into(edit_publish_icon)
            Glide.with(mActivity).load(R.drawable.ic_edit_workout).into(delete_icon)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(from: String, callBack: CommentCallBack) =
            SaveEditWorkoutDialog().apply {
                arguments = Bundle().apply {
                    setOnTextListener(callBack)
                    // putSerializable(TEXTVALUE, value)
                    putString(positionKey, from)
                }
            }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //typeValue = arguments!!.getSerializable(TEXTVALUE) as Data1
        from = arguments!!.getString(positionKey)!!

        if (from.equals("edit")){
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BlurredDialog)
        }else{
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }


    }


    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    private fun setOnTextListener(callBack: CommentCallBack) {
        this.callBack = callBack
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                dismiss()
            }
            R.id.rl_saveworkout -> {
                if (from.equals("edit", true)) {
                    callBack?.overwriteClick("overwrite")
                }
                else if (from.equals("myWorkout")) {
                    callBack?.overwriteClick("delete")
                }
                else if (from.equals("publish", true)) {
                    callBack?.overwriteClick("delete")
                }
                else if (from.equals("log", true)) {
                    callBack?.overwriteClick("delete")
                }
                else if (from.contains("history",true)) {
                    callBack?.overwriteClick("delete")
                }
                else if (from.equals("image", true)) {
                    callBack?.overwriteClick("gallery")
                }
                else if (tv_saveworkout.text.toString().equals("Save to my workout",true)){
                    callBack?.overwriteClick("Save to my workout")
                }
                else{
                    callBack?.overwriteClick("workout")
                }
                dismiss()
            }
            R.id.edit_layout -> {
                if (from.equals("publish", true)) {
                    callBack?.textOnClick1("publish")
                }
                else if (from.equals("log", true)) {
                    callBack?.textOnClick1("edit")
                }
                else if (from.contains("history", true)) {
                    callBack?.textOnClick1("edit")
                }
                else if (from.equals("image", true)) {
                    callBack?.textOnClick1("camera")
                }
                else
                    callBack?.textOnClick1("edit")
                dismiss()
            }
        }
    }

    interface CommentCallBack {
        fun textOnClick1(type: String)
        fun overwriteClick(type: String)
    }
}
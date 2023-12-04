package com.doviesfitness.allDialogs.download_video_in_formate

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.doviesfitness.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DownloadVideoInpxlFragment : BottomSheetDialogFragment() {

    private var status: String = ""
    private var mDialogEventListener: DownloadDialogEventListener? = null
    private var dialogMenus: MutableList<DownLoadModal>? = null
    private var mTag = "default"

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MenuBottomSheetDialog)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.download_video_in_pixl_dialog_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.pixel_list)
        val tvText = view.findViewById<TextView>(R.id.tvText)
        view.findViewById<ImageView>(R.id.ivImage).setOnClickListener({ v -> dismiss() })

        recyclerView.setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
        val bundle = arguments
        if (bundle != null) {
            val tag = bundle.getString(PARAM_TAG)
            status = bundle.getString(Process_Status)!!
            if (tag != null) {
                mTag = tag
                tvText.text = mTag
            }
        }

        recyclerView.setAdapter(ItemAdapter(dialogMenus!!))
    }

    override fun onDetach() {
        mDialogEventListener = null
        super.onDetach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mDialogEventListener != null) {
            mDialogEventListener!!.onDialogDismiss()
        }
    }

    fun addMenu(dialogMenus: MutableList<DownLoadModal>) {
        this.dialogMenus = dialogMenus
    }

    fun addDialogEventListener(downloadDialogEventListener: DownloadDialogEventListener) {
        this.mDialogEventListener = downloadDialogEventListener
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.pixcel_child_view_layout, parent, false)) {
        internal val tvText: TextView
        internal val ll_container: LinearLayout

        init {
            tvText = itemView.findViewById<TextView>(R.id.tvText)
            ll_container = itemView.findViewById<LinearLayout>(R.id.ll_container)

           /* itemView.setOnClickListener { v ->
                if (mDialogEventListener != null) {
                    mDialogEventListener!!.onItemClicked(mTag, adapterPosition, status)
                    dismiss()
                }
            }*/
        }
    }

    private inner class ItemAdapter internal constructor(private val dialogMenus: MutableList<DownLoadModal>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvText.setText(dialogMenus[position].title)

            holder.ll_container.setOnClickListener {

                if (mDialogEventListener != null) {
                    mDialogEventListener!!.onItemClicked(dialogMenus[position].title, position, status)
                    dismiss()
                }
            }
        }

        override fun getItemCount(): Int {
            return dialogMenus.size
        }
    }

    interface DownloadDialogEventListener {
        fun onItemClicked(pixelType: String, position: Int, status: String)
        fun onDialogDismiss()
    }

    companion object {
        private val PARAM_TAG = "tag_parameter"
        private val Process_Status = "ProcessStatus"

        fun newInstance(tag: String, status: String): DownloadVideoInpxlFragment {
            val fragment = DownloadVideoInpxlFragment()
            val args = Bundle()
            args.putString(PARAM_TAG, tag)
            args.putString(Process_Status, status)
            fragment.arguments = args
            return fragment
        }
    }
}
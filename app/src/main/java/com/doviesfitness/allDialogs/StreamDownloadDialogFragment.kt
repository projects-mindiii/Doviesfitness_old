package com.doviesfitness.allDialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.ExchangDialogMenu
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StreamDownloadDialogFragment : BottomSheetDialogFragment() {

    private var mDialogEventListener: DialogEventListener? = null
    private var dialogMenus: MutableList<ExchangDialogMenu>? = null
    private var mTag = "default"

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MenuBottomSheetDialog)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
       // return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
        return inflater.inflate(R.layout.fragment_item_exchange_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.cancel).setOnClickListener({ v -> dismiss() })
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.list)
        recyclerView.setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
        val bundle = arguments
        if (bundle != null) {
            val tag = bundle.getString(PARAM_TAG)
            if (tag != null) {
                mTag = tag
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

    fun addMenu(dialogMenus: MutableList<ExchangDialogMenu>) {
        this.dialogMenus = dialogMenus
    }

    fun addDialogEventListener(dialogEventListener: DialogEventListener) {
        this.mDialogEventListener = dialogEventListener
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.single_item_list_view, parent, false)) {
        internal val text: TextView
        internal val vDivider: View

        init {
            text = itemView.findViewById(R.id.tvText)
            vDivider = itemView.findViewById<View>(R.id.vDivider)
            itemView.setOnClickListener { v ->
                if (mDialogEventListener != null) {
                    mDialogEventListener!!.onItemClicked(mTag, text.text.toString(), adapterPosition)
                    dismiss()
                }
            }
        }
    }


    //adapter
    private inner class ItemAdapter internal constructor(private val dialogMenus: MutableList<ExchangDialogMenu>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //holder.text.setText(dialogMenus[position].title)

            if(dialogMenus[position].itemCount.equals("1")){
                holder.text.setTextColor(ContextCompat.getColor(context!!, R.color.colorGray1));
                holder.text.setText(dialogMenus[position].title)
            }else{
                holder.text.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack));
                holder.text.setText(dialogMenus[position].title)
            }


            if (dialogMenus.size- 1 == position) {
                holder.vDivider.setVisibility(View.GONE)
            } else {
                holder.vDivider.setVisibility(View.VISIBLE)
            }
        }

        override fun getItemCount(): Int {
            return dialogMenus.size
        }
    }

    interface DialogEventListener {
        fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int)
        fun onDialogDismiss()
    }

    companion object {
        private val PARAM_TAG = "tag_parameter"

        fun newInstance(tag: String): StreamDownloadDialogFragment {
            val fragment = StreamDownloadDialogFragment()
            val args = Bundle()
            args.putString(PARAM_TAG, tag)
            fragment.arguments = args
            return fragment
        }
    }

}
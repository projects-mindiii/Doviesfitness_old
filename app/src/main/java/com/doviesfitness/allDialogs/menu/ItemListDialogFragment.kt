package com.doviesfitness.allDialogs.menu


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import com.doviesfitness.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ItemListDialogFragment : BottomSheetDialogFragment() {

    private var mDialogEventListener: DialogEventListener? = null
    private var dialogMenus: MutableList<DialogMenu>? = null
    private var mTag = "default"

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MenuBottomSheetDialog)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.cancel).setOnClickListener({ v -> dismiss() })
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.list)
        /*recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 0;
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 1;
                if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount()-1) {
                    outRect.bottom = 0;
                }
            }
        });*/
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

    fun addMenu(dialogMenus: MutableList<DialogMenu>) {
        this.dialogMenus = dialogMenus
    }

    fun addDialogEventListener(dialogEventListener: DialogEventListener) {
        this.mDialogEventListener = dialogEventListener
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false)) {
        internal val image: ImageView
        internal val text: TextView
        internal val vDivider: View

        init {
            image = itemView.findViewById(R.id.ivImage)
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

    private inner class ItemAdapter internal constructor(private val dialogMenus: MutableList<DialogMenu>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.setText(dialogMenus[position].title)
            holder.image.setImageResource(dialogMenus[position].image)
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


        fun newInstance(tag: String): ItemListDialogFragment {
            val fragment = ItemListDialogFragment()
            val args = Bundle()
            args.putString(PARAM_TAG, tag)
            fragment.arguments = args
            return fragment
        }
    }

}
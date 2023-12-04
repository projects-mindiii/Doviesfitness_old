package com.doviesfitness.subscription

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import kotlinx.android.synthetic.main.static_content_item_layout.view.*

class StaticContentAdapter(
    context: Context, contentList: ArrayList<PackageListModel.Data.GetStaticContent>
) : RecyclerView.Adapter<StaticContentAdapter.MyViewHolder>() {
    var mContext: Context
    var contentList: ArrayList<PackageListModel.Data.GetStaticContent>

    init {
        this.mContext = context
        this.contentList = contentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.static_content_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.title.text = contentList.get(pos).title
        holder.content.text = contentList.get(pos).content


        if (contentList.get(pos).title.equals("Title", true)) {
            holder.content.setLineSpacing(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5.0f,
                    mContext.resources.getDisplayMetrics()
                ), 2.0f
            )
        }
        else{
            holder.content.setLineSpacing(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5.0f,
                    mContext.resources.getDisplayMetrics()
                ), 1.2f
            )

        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.title
        var content = view.content


    }
}

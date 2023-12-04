package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamTagModel
import kotlinx.android.synthetic.main.stream_template_item_view.view.*

class TemplateSearchAdapter(context: Context,tagList : ArrayList<StreamTagModel.Settings.Data>,listener:OnTagClick):
    androidx.recyclerview.widget.RecyclerView.Adapter<TemplateSearchAdapter.MyViewHolder>(){
    private var listener:OnTagClick
    private var context:Context
    private var tagList : ArrayList<StreamTagModel.Settings.Data>
    private var mLastClickTime: Long = 0

    init {
        this.context=context
        this.tagList=tagList
        this.listener=listener
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.searchTxt.text=tagList.get(pos).vStreamTagName
        if (tagList.get(pos).isSelected!=null){
            if (tagList.get(pos).isSelected){
                holder.searchTxt.setBackgroundResource(R.drawable.white_tag_bg)
            }
            else{
                holder.searchTxt.setBackgroundResource(R.drawable.unselected_tag_bg)
            }
        }
        else{
            holder.searchTxt.setBackgroundResource(R.drawable.unselected_tag_bg)
        }

        holder.searchTxt.setOnClickListener(){

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            }
            else {
                if (tagList.get(pos).isSelected!=null){
                    if (tagList.get(pos).isSelected){
                        holder.searchTxt.setBackgroundResource(R.drawable.unselected_tag_bg)
                        tagList.get(pos).isSelected=false
                    }
                    else{
                        holder.searchTxt.setBackgroundResource(R.drawable.white_tag_bg)
                        tagList.get(pos).isSelected=true
                    }
                }
                else{
                    holder.searchTxt.setBackgroundResource(R.drawable.white_tag_bg)
                    tagList.get(pos).isSelected=true
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                listener.onTagClick(pos)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.stream_template_item_view,parent,false))

    }
    override fun getItemCount(): Int {
        return tagList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  searchTxt = view.search_txt
    }

    interface OnTagClick{
        fun onTagClick(pos: Int)
    }
}




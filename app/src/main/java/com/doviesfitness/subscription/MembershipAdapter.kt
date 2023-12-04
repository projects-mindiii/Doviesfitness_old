package com.doviesfitness.subscription

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.R
import kotlinx.android.synthetic.main.membership_item_layout.view.*

class MembershipAdapter(
    context: Context,
    allPackageList: ArrayList<PackageListModel.Data.GetAllPackage>,listener:OnClickListener
) : RecyclerView.Adapter<MembershipAdapter.MyViewHolder>() {
    var mContext: Context
   var allPackageList: ArrayList<PackageListModel.Data.GetAllPackage>
    var listener:OnClickListener
    init {
        this.mContext = context
        this.allPackageList=allPackageList
        this.listener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.membership_item_layout, parent, false))
    }
    override fun getItemCount(): Int {
        return allPackageList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
       holder.time.text= allPackageList.get(pos).package_name
       holder.timeDescription.text= allPackageList.get(pos).description
       holder.perTime.text= allPackageList.get(pos).package_text
       holder.price.text= "$"+allPackageList.get(pos).package_amount
        holder.cardView.setCardBackgroundColor(Color.parseColor(allPackageList.get(pos).color_code))
        holder.ll.setBackgroundColor(Color.parseColor(allPackageList.get(pos).color_code))
        holder.itemView.setOnClickListener{
            listener.onItemCLick(pos)
        }
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val  time = view.time
        val  price = view.price
        val  perTime = view.per_time
        val  timeDescription = view.time_des
        val  cardView = view.cardview
        val  ll = view.ll

    }

    interface OnClickListener{
        public fun onItemCLick(pos: Int)
    }
}

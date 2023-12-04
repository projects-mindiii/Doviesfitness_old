package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetSModel
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.set_number_item_view.view.rootLayoutnew
import kotlinx.android.synthetic.main.set_number_item_view.view.tv_sets_name


class SetsAdapter(
    var context: Context,
    var mSetSModelList: List<SetSModel>,
    var listner: () -> Unit,
    var listner1: (Int) -> Unit
) : RecyclerView.Adapter<SetsAdapter.MyView>() {
    var flag = true


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): MyView {
        return MyView(
            LayoutInflater.from(p0.context!!).inflate(
                R.layout.set_number_item_view,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mSetSModelList.size
        // return 3
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        /** setting width of set item view progremtically so that at time only 4 items can be displaye on scren*/
        val params = holder.rootLayout.layoutParams
        val dpWidth = CommanUtils.getWidthOnly(context as Activity)
        val layoutParams = RelativeLayout.LayoutParams(dpWidth, LayoutParams.WRAP_CONTENT)
        holder.rootLayout.layoutParams = layoutParams
        /**---------------------------------------------------------------------------------------------*/


        val item = mSetSModelList[position]
        if (item.setName.isNotEmpty())
        holder.tv_sets_name.text = item.setName
       else
        holder.tv_sets_name.text = "SET ${position+1}"

        holder.tv_sets_name.setOnClickListener {
            mSetSModelList.forEach { it.isSelected = false }
            mSetSModelList[position].isSelected = true
            notifyDataSetChanged()
            listner1.invoke(position)
        }

        if (mSetSModelList[position].isSelected) {
            holder.tv_sets_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        } else {
            holder.tv_sets_name.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.deselected_sets_color
                )
            )
        }

        if (position == 0 && mSetSModelList[position].isSelected && !mSetSModelList[position].isFirstAutometicallyPerformedListener!!) {
            //  mSetSModelList[position].isFirstAutometicallyPerformedListener=true
            // if (flag){
            //    flag=false
            //    Handler().postDelayed({
            // listner1.invoke(0)
            // }, 2000)
            // }
        }

    }


    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var tv_sets_name = itemView.tv_sets_name
        var rootLayout = itemView.rootLayoutnew
    }


}

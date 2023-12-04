package com.doviesfitness.ui.setting.faq

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.faq_cell.view.*

import android.view.animation.AnimationUtils
import com.doviesfitness.R


class FaqAdapter(context: Context, faqList: ArrayList<FAQModel.Data>) : androidx.recyclerview.widget.RecyclerView.Adapter<FaqAdapter.MyViewHolder>() {
    private var context: Context
    var faqList: ArrayList<FAQModel.Data>

    init {
        this.context = context
        this.faqList = faqList
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val data = faqList.get(pos)

        holder.tvQuestion.setText(data.faq_question)
        holder.tvAns.setText((data.faq_answer))

        if ("Yes".equals(data.isOpen)) {
            holder.iv_collaps.setImageResource(R.drawable.ic_down_arrow)

          /*  val animSlideDown =
                AnimationUtils.loadAnimation(context, R.anim.slide_down)
            holder.rlAns.startAnimation(animSlideDown)*/
            holder.rlAns.setVisibility(View.VISIBLE)

        } else {
            holder.iv_collaps.setImageResource(R.drawable.ic_down_arrow)
            holder.rlAns.setVisibility(View.GONE)
        }

        holder.rltv_question.setOnClickListener{
            for (i in faqList.indices) {
                if (i != pos)
                    faqList.get(i).isOpen = "NO"
            }

            if ("NO".equals(data.isOpen)) {
                data.isOpen = "Yes"
            } else {
                data.isOpen  = "NO"
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.faq_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val tvQuestion = view.tv_question
        val rlAns = view.rl_ans
        val tvAns = view.tv_ans
        val iv_collaps = view.iv_collaps
        val rltv_question = view.rltv_question
    }
}


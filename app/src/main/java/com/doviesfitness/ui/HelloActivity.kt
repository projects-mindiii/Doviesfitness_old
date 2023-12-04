package com.doviesfitness.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.R
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperAdapter
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperViewHolder
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.rv_swap.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.abc_item.view.*
import kotlinx.android.synthetic.main.activity_hello.*
import java.util.*

class HelloActivity : AppCompatActivity(), OnStartDragListener {

    private var mItemTouchHelper: ItemTouchHelper? = null
    private val mList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        initList()
        val adapter = HelloAdapter(mList,this)
        rvDemo.setHasFixedSize(true)
        val lm = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvDemo.layoutManager = lm

        val callback = SimpleItemTouchHelperCallback(adapter,null)
        mItemTouchHelper = ItemTouchHelper(callback)

        mItemTouchHelper?.attachToRecyclerView(rvDemo)
        rvDemo.adapter = adapter
    }

    override fun onStartDrag(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder);
    }

    private fun initList() {
        for (i in 0..15) {
            mList.add("Hello World $i")
        }
    }

}

class HelloAdapter(val mList: MutableList<String>, val mDragStartListener: OnStartDragListener): androidx.recyclerview.widget.RecyclerView.Adapter<HelloAdapter.MyView>(), ItemTouchHelperAdapter {
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(mList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyView {
        return MyView(LayoutInflater.from(p0.context!!).inflate(R.layout.create_exercise_item_view,p0,false))
    }

    override fun getItemCount(): Int {
        return  mList.size
    }

    override fun onBindViewHolder(p0: MyView, p1: Int) {
        //p0.tvText.setText(mList.get(p1))
    }

    inner class MyView(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder (itemView), ItemTouchHelperViewHolder {
        override fun onItemSelected() {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onItemClear() {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        val tvText = itemView.tvText
        init {
            itemView.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    mDragStartListener.onStartDrag(this@MyView)
                    return false;
                }
            })
        }
    }
}

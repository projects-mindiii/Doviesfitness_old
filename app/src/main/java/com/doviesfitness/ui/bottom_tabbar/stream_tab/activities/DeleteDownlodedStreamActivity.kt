package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.DeleteStreamAdapter
import kotlinx.android.synthetic.main.activity_fav_stream.*

class DeleteDownlodedStreamActivity:BaseActivity() {
    lateinit var adapter:DeleteStreamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_fav_stream)
        initialisation()
    }

    private fun initialisation() {
        var layoutManager= LinearLayoutManager(getActivity())
        fav_rv.layoutManager=layoutManager
        adapter= DeleteStreamAdapter(getActivity())
        fav_rv.adapter= adapter
    }

}
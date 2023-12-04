package com.doviesfitness.allDialogs.menu

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.doviesfitness.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExerciseMarkAsDialogFragment( var selectedListSize:Int, var listner:(Int)-> Unit) :
    BottomSheetDialogFragment() {


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MenuBottomSheetDialog)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_item_exchange_list_dialog_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.cancel).setOnClickListener { v -> dismiss() }
        var tvLandR = view.findViewById<TextView>(R.id.tv_LandR)
        var tvSuperset = view.findViewById<TextView>(R.id.tv_superset)
        if (selectedListSize==2){
            tvLandR.isEnabled=true
            tvLandR.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite));
            tvSuperset.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite));
        }else{
            tvLandR.isEnabled=false
            tvLandR.setTextColor(ContextCompat.getColor(context!!, R.color.deselected_sets_color));
            tvSuperset.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite));
        }

        tvLandR.setOnClickListener {
            listner.invoke(1)
            dismiss()
        }

        tvSuperset.setOnClickListener {
            listner.invoke(2)
            dismiss()
        }

  /*      val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.list)
        recyclerView.setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))*/

     }

    override fun onDetach() {
         super.onDetach()
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }


}
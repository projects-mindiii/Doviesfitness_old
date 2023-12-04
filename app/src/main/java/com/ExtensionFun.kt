package com

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast

fun Context.showTost(msg: String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun View.showVisibility(shouldShowView: Boolean){
    this.visibility = if(shouldShowView) View.VISIBLE else View.GONE
}

fun ImageView.setMyImageResource(img: Int) {
    this.setImageResource(img)
}
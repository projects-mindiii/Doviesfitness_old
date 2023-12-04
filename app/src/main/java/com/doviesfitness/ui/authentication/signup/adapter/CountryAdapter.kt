package com.doviesfitness.ui.authentication.signup.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.authentication.signup.model.Country
import kotlinx.android.synthetic.main.country_view.view.*
import com.doviesfitness.utils.Utility
import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager

class CountryAdapter(
    mContext1: Context,
    mCountries: ArrayList<Country>
) : RecyclerView.Adapter<CountryAdapter.MyViewHolder>() {


    var mContext: Context? = null
    var mCountries: ArrayList<Country>? = null
    private val flags = Utility.countryFlags
    private var globleCountry: ArrayList<Country>? = null

    init {
        mContext = mContext1
        this.mCountries = mCountries
        this.globleCountry = ArrayList()
        if (this.mCountries != null) {
            globleCountry!!.addAll(mCountries)
        }

        for (i in 0 until mCountries.size) {
            globleCountry?.get(i)?.flag =flags[i]
        }
    }


    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase()
        mCountries?.clear()
        if (charText.length == 0) {
            globleCountry?.let { mCountries?.addAll(it) }
        } else {
            for (i in 0 until globleCountry!!.size) {
                if (globleCountry?.get(i)?.country_name?.toLowerCase()?.startsWith(charText)!!) {
                    mCountries?.add(globleCountry!!.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mContext?.let { Glide.with(it).load(mCountries?.get(position)?.flag).into(holder.ivCountryImage) }
        holder.tvCountryName.text = mCountries?.get(position)?.country_name
        holder.tvCountryCode.text = mCountries?.get(position)?.phone_code

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.country_view, parent, false))
    }

    override fun getItemCount(): Int {
        return mCountries!!.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val ivCountryImage = view.ivCountryImage
        val tvCountryName = view.tvCountryName
        val tvCountryCode = view.tvCountryCode
        val cvCountryLayout = view.cvCountryLayout
        init {
            cvCountryLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
            R.id.cvCountryLayout -> {
                val resultIntent = Intent()
                resultIntent.putExtra("country_flag", mCountries?.get(adapterPosition)?.flag)
                resultIntent.putExtra("country_phone_code", mCountries?.get(adapterPosition)?.phone_code)
                resultIntent.putExtra("country_Name", mCountries?.get(adapterPosition)?.country_name)
                resultIntent.putExtra("country_code", mCountries?.get(adapterPosition)?.code)
                (mContext as Activity).setResult(Activity.RESULT_OK, resultIntent)
                (mContext as Activity).finish()
                hideKeyboard()
                }
            }

        }
    }

    fun hideKeyboard() {
        if ((mContext as Activity).getCurrentFocus()== null) return
        val inputMethodManager = mContext!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((mContext as Activity).getCurrentFocus()!!.windowToken, 0)
    }

}
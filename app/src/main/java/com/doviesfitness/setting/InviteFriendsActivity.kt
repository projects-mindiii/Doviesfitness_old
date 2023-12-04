package com.doviesfitness.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityInviteFriendBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.BuildConfig


class InviteFriendsActivity :BaseActivity(),View.OnClickListener{


    lateinit var binding:ActivityInviteFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=   DataBindingUtil.setContentView(this, R.layout.activity_invite_friend)
        binding.inviteBtn.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.invite_btn->{
                share()
            }
            R.id.iv_back->{
              onBackPressed()
            }
        }
    }

    private fun share() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dovies")
        var shareMessage = "\nCome and join me. Download the Doviesfitness app and let's workout together. Install the Doviesfitness app here\n\n"
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

}
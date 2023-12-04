package com.doviesfitness.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_reset_password2.*

class ResetPassword2Activity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password2)

       val email =  intent.getStringExtra("Email")!!
        tv_email.text = email
        initViews()
    }

    private fun initViews() {
        btn_back_to_login.setOnClickListener(this)
        iv_close.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back_to_login -> {
                finish()
            }
            R.id.iv_close -> {
                val intent = Intent(this, ResetPassword1Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

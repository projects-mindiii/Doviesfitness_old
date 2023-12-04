package com.doviesfitness.ui.authentication.signup.activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_select_gender.*
import kotlinx.android.synthetic.main.activity_select_gender.iv_back

class SelectGenderActivity : BaseActivity(), View.OnClickListener {
    private var mLastClickTime: Long = 0
    lateinit var signUpInfo: SignUpInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()


        setContentView(R.layout.activity_select_gender)

        signUpInfo = intent.getSerializableExtra("SignUpInfo") as SignUpInfo
        initialiseView()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    private fun initialiseView() {
        btn_female.setOnClickListener(this)
        btn_male.setOnClickListener(this)
        iv_back.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_male -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }

                heightWeightActivityIntent("Male")
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_female -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }


                heightWeightActivityIntent("Female")
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun heightWeightActivityIntent(gender: String) {
        signUpInfo.setGender(gender)
        Log.e("EMAIL", signUpInfo.getEmail()!!)

        val intent = Intent(this, HeightAndWeightActivity::class.java)
        intent.putExtra("SignUpInfo", signUpInfo)
        startActivity(intent)
       // finish()
    }
}

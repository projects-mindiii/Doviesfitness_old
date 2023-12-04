package com.doviesfitness.ui.authentication.login

import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.widget.EditText
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.message
import com.doviesfitness.utils.StringConstant.Companion.settings
import com.doviesfitness.utils.StringConstant.Companion.success
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.error_email
import kotlinx.android.synthetic.main.activity_reset_password.et_email
import kotlinx.android.synthetic.main.activity_reset_password.iv_close
import kotlinx.android.synthetic.main.activity_reset_password.main_layout
import java.lang.Exception

class ResetPassword1Activity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initViews()
        tvReturnToLogin()
    }

    private fun tvReturnToLogin() {
        val builder = SpannableStringBuilder()
        val text1 = SpannableString(getString(R.string.or_return_to)+" ")

        builder.append(text1)
        val login = SpannableString(getString(R.string.log_in_))

        login.setSpan(StyleSpan(Typeface.BOLD), 0, login.length, 0)
        login.setSpan(UnderlineSpan(), 0, login.length, 0)
        login.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onBackPressed()
            } }, 0, login.length, 0)
        login.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)), 0, login.length,
            0
        )
        builder.append(login)
        tv_return_login.setMovementMethod(LinkMovementMethod.getInstance())
        tv_return_login.text = builder
    }

    private fun initViews() {
        Slidr.attach(this)
        iv_close.setOnClickListener(this)
        btn_reset_pass.setOnClickListener(this)
        main_layout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_reset_pass -> {
                Constant.hideSoftKeyBoard(this, et_email)
                emailValidation()
            }
            R.id.iv_close -> {
                onBackPressed()
            }
            R.id.main_layout -> {
                Constant.hideSoftKeyBoard(this, et_email)
            }
        }
    }

    private fun emailValidation() {
        if (!emailValidation(et_email)){
            error_email.visibility = View.VISIBLE
        } else {
            resetPasswordApi()
        }
    }

    private fun resetPasswordApi() {
        if (Constant.isNetworkAvailable(this, main_layout)) {
            setLoading(true)
            val param = HashMap<String, String>()
            param.put(StringConstant.email,et_email.text.toString().trim())
            getDataManager().resetPassword(param)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        setLoading(false)
                        val json: JSONObject? = response?.getJSONObject(settings)
                        val status = json!!.get(success)
                        val msg = json.get(message)
                        if (status.equals("1")) {
                            val intent = Intent(this@ResetPassword1Activity, ResetPassword2Activity::class.java)
                            intent.putExtra("Email",et_email.text.toString().trim())
                            startActivity(intent)
                            finish()
                            error_email.visibility = View.GONE
                            btn_reset_pass.isEnabled = true
                        } else {
                            setLoading(false)
                            btn_reset_pass.isEnabled = true
                            error_email.visibility = View.GONE
                            Constant.showCustomToast(this@ResetPassword1Activity,msg.toString())
                        }
                    } catch (exception: Exception) {
                        setLoading(false)
                        btn_reset_pass.isEnabled = true
                        Constant.showCustomToast(this@ResetPassword1Activity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    setLoading(false)
                    btn_reset_pass.isEnabled = true
                    Constant.showCustomToast(this@ResetPassword1Activity, getString(R.string.something_wrong))
                }
            })
        }
    }

    private fun emailValidation(email: EditText): Boolean {
        if (email.text.trim().isEmpty()) {
            error_email.visibility = View.VISIBLE
            error_email.text = getString(R.string.please_enter_email_address)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            error_email.visibility = View.VISIBLE
            error_email.text = getString(R.string.please_enter_valid_email_address)
            return false
        } else {
            error_email.visibility = View.GONE
            return true
        }
    }
}

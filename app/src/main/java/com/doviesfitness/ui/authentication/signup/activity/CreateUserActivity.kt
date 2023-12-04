package com.doviesfitness.ui.authentication.signup.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.activity_create_user.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
class CreateUserActivity : BaseActivity(), View.OnClickListener {
    private val userNamePattern = "^(?=.*[a-z A-Z]).{1,30}\$"
    lateinit var signUpInfo: SignUpInfo
    var isUsername: Boolean = false
     var timer :Timer?= null
    var searchText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()

        setContentView(R.layout.activity_create_user)
        initView()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        iv_next_btn.isEnabled = true
    }

    private fun initView() {
        iv_next_btn.isEnabled = false
        signUpInfo = intent.getSerializableExtra("SignUpInfo") as SignUpInfo

        iv_next_btn.setOnClickListener(this)
        main_layout1.setOnClickListener(this)
        iv_back.setOnClickListener(this)


        et_username.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            val DELAY: Long = 800
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString().trim()
                timer.cancel()
                timer = Timer()

                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            userNameValidation(searchText)
                        }
                    },
                    DELAY
                )

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onClick(v: View?) {
        when (v?.id) {
            /* @param:isUsername : false "so it will not go next screen"
            * @param:isUsername : true "so it will go next screen"
            * */
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_next_btn -> {
                if (!isUserNameValid(et_username)) {
                    tv_username_status.visibility = View.GONE
                    tv_username_status.setTextColor(ContextCompat.getColor(this@CreateUserActivity, R.color.colorOrange))
                    tv_username_status.text = getString(R.string.please_enter_your_user_name)

                } else if (isUsername && et_username.text.toString().trim().length > 2) {
                    signUpInfo.setUser_name(et_username.text.toString().trim())
                    signUpInfo.setDevicToken(getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
                    val intent = Intent(this, AddProfilePhotoActivity::class.java)
                    intent.putExtra("SignUpInfo", signUpInfo)
                    startActivity(intent)
                    iv_next_btn.isEnabled = false
                }
            }
            R.id.main_layout1 -> {
                Constant.hideSoftKeyBoard(this, et_username)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

   private fun isUserNameValid(textView: EditText): Boolean {
        val pattern = Pattern.compile(userNamePattern)
        val matcher = pattern.matcher(textView.text.toString().trim())
        val bool = matcher.matches()
        if (!bool) {
            tv_username_status.visibility = View.VISIBLE
            tv_username_status.text = getString(R.string.please_enter_your_user_name)
        }
        return bool
    }

    /*check the user name is available or not
    * @param: user_name
    * @GetValue from server
    * @ is_available: 1 "so you can go to next step"
    * @ is_available: 0 "enter your username"
    * */
    private fun userNameValidation(etUserName: String) {
        isUsername = false
        //iv_next_btn.isEnabled = false
        if (etUserName.trim().length > 0) {
            val pram = HashMap<String, String>()
            pram.put("user_name", etUserName.trim())
            pram.put("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
            getDataManager().checkUserNameAvailability(pram)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    hideNavigationBar()
                    try {
                        val json: JSONObject? = response?.getJSONObject("data")
                        if (json!!.get("is_available").equals("1")) {
                            tv_username_status.visibility = View.VISIBLE
                            tv_username_status.setTextColor(ContextCompat.getColor(this@CreateUserActivity, R.color.colorGreen))
                            tv_username_status.text = json.getString("message")
                            tv_username_status.visibility = View.VISIBLE
                            iv_next_btn.setBackgroundResource(R.drawable.white_next_btn_selector)

                            if (et_username.text.toString().trim().equals(etUserName)){
                                isUsername = true
                                iv_next_btn.isEnabled = true
                            }

                        } else {
                            iv_next_btn.setBackgroundResource(R.drawable.ic_left_arrow_gray)
                            tv_username_status.visibility = View.VISIBLE
                            tv_username_status.setTextColor(ContextCompat.getColor(this@CreateUserActivity, R.color.colorOrange))
                            tv_username_status.text = json.getString("message")
                            isUsername = false
                            iv_next_btn.isEnabled = false
                        }
                    } catch (exception: Exception) {
                        iv_next_btn.isEnabled = true
                        Constant.showCustomToast(this@CreateUserActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    hideNavigationBar()
                    iv_next_btn.isEnabled = true
                    Constant.showCustomToast(this@CreateUserActivity, getString(R.string.something_wrong))
                    Log.e("Error", "" + anError?.localizedMessage)
                }

            })
        }

    }

}

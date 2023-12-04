package com.doviesfitness.ui.authentication.signup.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.widget.EditText
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_signup.*
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.text.*
import android.util.Log
import android.view.*
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.LoginResponce
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.date_picker.DatePickerPopWin
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.SECOND_ACTIVITY_REQUEST_CODE
import com.doviesfitness.utils.Constant.Companion.isNetworkAvailable
import com.doviesfitness.utils.Constant.Companion.showCustomToast
import com.doviesfitness.utils.OnSwipeListener
import com.doviesfitness.utils.StringConstant.Companion.Android
import com.doviesfitness.utils.StringConstant.Companion.FB
import com.doviesfitness.utils.StringConstant.Companion.data
import com.doviesfitness.utils.StringConstant.Companion.is_available
import com.doviesfitness.utils.StringConstant.Companion.message
import com.doviesfitness.utils.StringConstant.Companion.settings
import com.doviesfitness.utils.StringConstant.Companion.success
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_signup.error_pass
import kotlinx.android.synthetic.main.activity_signup.et_pass
import org.json.JSONObject
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class SignUpActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener {
    private var imageUrl: String? = ""
    private var personName: String? = ""
    private var email: String? = ""
    private var fbId: String = ""
    private var graphObject: JSONObject? = null
    lateinit var dataRequest: GraphRequest
    private lateinit var gestureDetector: GestureDetector
    private var mLastClickTime: Long = 0
    var myPhoneCountryCode: String = ""
    var myCountryCode: String = ""
    var myCountryName: String = ""
    val eMail = "email"
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(
                getResources()
                    .getColor(R.color.colorBlack)
            )
        }
        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()


        setContentView(R.layout.activity_signup)
//        hideNavigationBar()
        initialise()
        privacyPolicyAndTermsOfUse()
        fieldTextWatcher()
    }

    private fun fieldTextWatcher() {
        et_fullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    fullNameValidation(et_fullname)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })


        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    emailValidation(et_email)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

        })

        et_pass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    passValidation(et_pass)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

        })

        et_confirm_pass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    confirmPassValidation(et_pass, et_confirm_pass)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

        })


        gestureDetector = GestureDetector(this, object : OnSwipeListener() {
            override fun onSwipe(direction: Direction): Boolean {
                if (direction == Direction.left) {
                }
                if (direction == Direction.right) {
                    finish()
                    overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right)

                }
                if (direction == Direction.down) {

                }
                if (direction == Direction.up) {

                }
                return true
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    /* privacy policy and terms of use of font family underline
     //and
    click of both text:- "privacy policy" and  "Terms Of Use"*/
    private fun privacyPolicyAndTermsOfUse() {
        val builder = SpannableStringBuilder()
        val text1 =
            SpannableString(getString(R.string.by_creating_an_account_you_agree_to_doviesfitness) + " ")

        builder.append(text1)
        val privacyPolicy = SpannableString(getString(R.string.privacy_policy))

        privacyPolicy.setSpan(StyleSpan(Typeface.BOLD), 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(UnderlineSpan(), 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //showCustomToast(this@SignupActivity,"Privacy Policy")
                startTCActivity("Privacy Policy")
            }
        }, 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)),
            0,
            privacyPolicy.length,
            0
        )
        builder.append(privacyPolicy)

        val and = SpannableString(" " + getString(R.string.and) + " ")
        builder.append(and)

        val terms = SpannableString(getString(R.string.terms_of_use))

        terms.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //showCustomToast(this@SignupActivity,"Terms of use")
                startTCActivity("tnc")
            }
        }, 0, terms.length, 0)
        terms.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)), 0,
            terms.length,
            0
        )
        terms.setSpan(UnderlineSpan(), 0, terms.length, 0)

        terms.setSpan(StyleSpan(Typeface.BOLD), 0, terms.length, 0)
        builder.append(terms)

        tv_terms_and_privacy.setText(builder)
        tv_terms_and_privacy.setMovementMethod(LinkMovementMethod.getInstance())
    }

    private fun startTCActivity(str: String) {
        intent = Intent(this, PrivacyPolicyAndTACActivity::class.java)
        if (str.equals("tnc")) {
            intent.putExtra("from", "tnc")
        } else {
            intent.putExtra("from", "privacy policy")

        }
        startActivity(intent)
    }

    private fun initialise() {
        Slidr.attach(this)
        getFirebaseTocan()
        if (intent.getSerializableExtra("SignupInfo") != null) {
            val signUpInfo = intent.getSerializableExtra("SignupInfo") as SignUpInfo
            et_pass.visibility = View.GONE
            error_pass.visibility = View.GONE
            error_confirm_pass.visibility = View.GONE
            et_confirm_pass.visibility = View.GONE
            et_fullname.isEnabled = false
            et_fullname.text = signUpInfo.getName()?.toEditable()
            fbId = signUpInfo.getSocialNetworkId().toString()
            if (signUpInfo.getEmail() != null && !signUpInfo.getEmail()!!.isEmpty()) {
                et_email.text = signUpInfo.getEmail()?.toEditable()
                et_email.isEnabled = false
            }
            btn_fb_create_acc.visibility = View.VISIBLE
            btn_create_acc.visibility = View.GONE
        }

        login_button.setReadPermissions(Arrays.asList(eMail))//fb email permission
        callbackManager = CallbackManager.Factory.create()

        btn_create_acc.setOnClickListener(this)
        ll_country.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        ll_dob.setOnClickListener(this)
        fb_btn.setOnClickListener(this)
        login_button.setOnClickListener(this)
        btn_fb_create_acc.setOnClickListener(this)
        // tv_terms_and_privacy.setOnClickListener(this)

        main_layout.setOnClickListener(this)
        main_layout.setOnTouchListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_create_acc -> {
                signUpValidation()
            }
            R.id.ll_country -> {
                intent = Intent(this, CountrySelectionActivity::class.java)
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
            }
            R.id.iv_close -> {
                onBackPressed()
            }
            R.id.login_button -> {
                fbResponse()
            }
            R.id.fb_btn -> {
                fbLogout()
                login_button.performClick()
            }
            R.id.ll_dob -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
                var str = "1993-10-22"
                if (!tv_dob.text.toString().isEmpty() && !tv_dob.text.toString().equals(
                                "Date of birth (Optional)",
                                true
                        )
                ) {
                    var dStr = tv_dob.text.toString()
//                    var dStr ="mm/dd/yyyy"
                    var date = Date(dStr)
                    val sdf2 = SimpleDateFormat("yyyy-MM-dd")
                    str = sdf2.format(date)
                    Log.d("selected date", "selected date..." + str)
                }else {
                    //str = "1993-10-22"

                    var cal = Calendar.getInstance()
                    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    str = dateFormat.format(cal.getTime())

                }


                openDatePickerDialog(str)
                // openStartDateDialog()
            }
            R.id.main_layout -> {
                Constant.hideSoftKeyBoard(this, et_fullname)
            }
            R.id.btn_fb_create_acc -> {
                fbSignUpValidation()
            }
            /* R.id.tv_terms_and_privacy -> {
                 intent = Intent(this, PrivacyPolicyAndTACActivity::class.java)
                 startActivity(intent)
             }*/
        }
    }


    fun openDatePickerDialog(str: String) {
        hideKeyboard()

        val pickerPopWin = DatePickerPopWin.Builder(getActivity(),
            object : DatePickerPopWin.OnDatePickedListener {
                override fun onDatePickCompleted(
                    year: Int,
                    month: Int,
                    day: Int,
                    dateDesc: String
                ) {
                    // Toast.makeText(getActivity(), dateDesc, Toast.LENGTH_SHORT).show()

                    var selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, (month - 1))
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)

                    val sdf1 = SimpleDateFormat("dd MMM yyyy")
                    tv_dob.setText(sdf1.format(selectedDate.getTime()))
                    tv_dob.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite))
                }
            })

            .textConfirm("Done") //text of confirm button
            .textCancel("Cancel") //text of cancel button
           // .btnTextSize(resources.getDimension(R.dimen._5sdp).toInt()) // button text size
            .viewTextSize(resources.getDimension(R.dimen._6sdp).toInt()) // pick view text size
            .colorCancel(Color.parseColor("#C7C7C7")) //color of cancel button
            .colorConfirm(Color.parseColor("#E74C38"))//color of confirm button
            .minYear(1950) //min year in loop
            .maxYear(2051) // max year in loop
            .dateChose(str) // date chose when init pop up window
            .build()


        pickerPopWin.showPopWin(this)
    }


    private fun fbSignUpValidation() {
        if (!emailValidation(et_email)) {
            error_email.visibility = View.VISIBLE
        } else if (!countryValidation()) {
            error_country.visibility = View.VISIBLE
        } else {
            if (isNetworkAvailable(this, main_layout)) {
                checkEmailAvailability()
                btn_fb_create_acc.isEnabled = false
            }
        }
    }

    private fun fbResponse() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                //To change body of created functions use File | Settings | File Templates.
                getUserDetails(result)
            }

            override fun onCancel() {
                //showCustomToast(this@SignupActivity, getString(R.string.something_wrong))
                //Log.e(TAG, "fb Cancel")
            }

            override fun onError(error: FacebookException?) {
                //showCustomToast(this@SignupActivity, getString(R.string.something_wrong))
            }

        })
    }

    private fun getUserDetails(result: LoginResult?) {
        dataRequest = GraphRequest.newMeRequest(
            result?.accessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                    graphObject = response?.getJSONObject()
                    try {
                        fbId = graphObject!!.getString("id")
                        personName = graphObject!!.getString("name")
                        if (graphObject!!.getString("picture") != null) {
                            imageUrl = `object`!!.getJSONObject("picture").getJSONObject("data")
                                .getString("url")
                        }
                        if (graphObject!!.has("email")) {
                            email = graphObject!!.getString("email")
                        }


                        fbLoginApi()

                    } catch (ex: Exception) {
                        showCustomToast(this@SignUpActivity, getString(R.string.something_wrong))
                    }
                }
            })
        val bundle = Bundle()
        bundle.putString("fields", "id, name, email, picture.type(large)")
        dataRequest.setParameters(bundle)
        dataRequest.executeAsync()
    }


    /*FACEBOOK sign up / login
    * @success = 1 "Fb Log in"
    * @success = 2 "Fb sign up" so hide password and change password
    * */
    private fun fbLoginApi() {
        val header = HashMap<String, String>()
        header.put("device_id", "")
        header.put("device_type", Android)
        header.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        header.put("social_network_id", fbId)
        header.put("social_network_type", FB)
        setLoading(true)
        getDataManager().doLogin(header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                try {
                    setLoading(false)
                    val jsonObject: JSONObject? = response?.getJSONObject(settings)
                    val success: String? = jsonObject?.getString(success)
                    val message: String? = jsonObject?.getString(message)
                    if (success.equals("1")) {

                        val data = getDataManager().mGson!!.fromJson(
                            response.toString(),
                            LoginResponce::class.java
                        )
                        val userInfoBean = UserInfoBean(
                            data.data.get(0).is_admin
                            , data.data.get(0).customer_id
                            , data.data.get(0).customer_profile_image
                            , data.data.get(0).customer_auth_token
                            , data.data.get(0).customer_email_verified
                            , data.data.get(0).customer_status
                            , data.data.get(0).customer_notification
                            , data.data.get(0).customer_full_name
                            , data.data.get(0).customer_weight

                            , data.data.get(0).customer_height
                            , data.data.get(0).customer_mobile_number
                            , data.data.get(0).customer_gender
                            , data.data.get(0).customer_email
                            , data.data.get(0).customer_social_network
                            , data.data.get(0).customer_social_network_id
                            , data.data.get(0).customer_notify_remainder
                            , data.data.get(0).customer_user_name
                            , data.data.get(0).customer_units
                            , data.data.get(0).customer_country_id
                            , data.data.get(0).customer_country_name
                            , data.data.get(0).customer_isd_code
                            , data.data.get(0).notification_status
                            , data.data.get(0).dob
                            , data.data.get(0).title
                            , data.data.get(0).is_subscribed,
                            "" + getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)
                        )
                        getDataManager().setUserInfo(userInfoBean)
                        fbLogout()
                        finishAffinity()
                        showCustomToast(this@SignUpActivity, "You've successfully logged in.")
                        intent = Intent(this@SignUpActivity, HomeTabActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (success.equals("2")) {
                        btn_fb_create_acc.visibility = View.VISIBLE
                        btn_create_acc.visibility = View.GONE
                        setFbInfo()
                    } else {
                        btn_fb_create_acc.isEnabled = true
                        showCustomToast(this@SignUpActivity, message!!)
                    }
                } catch (exception: Exception) {
                    btn_fb_create_acc.isEnabled = true
                    setLoading(false)
                    showCustomToast(this@SignUpActivity, getString(R.string.something_wrong))
                }
            }

            override fun onError(anError: ANError?) {
                btn_fb_create_acc.isEnabled = true
                setLoading(false)
            }
        })
    }

    private fun setFbInfo() {
        et_pass.visibility = View.GONE
        error_pass.visibility = View.GONE
        error_confirm_pass.visibility = View.GONE
        et_confirm_pass.visibility = View.GONE
        et_fullname.isEnabled = false
        et_fullname.text = personName?.toEditable()
        if (!email.isNullOrEmpty()) {
            et_email.text = email?.toEditable()
            et_email.isEnabled = false
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


    /****FACEBOOK Logout****/
    fun fbLogout() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired()

        if (isLoggedIn) {
            LoginManager.getInstance().logOut()
        }
    }

    /*get the result from the CountrySelectionActivity.class
     * @param :- get the country flag
      * @param :- get the  country code
      * @param:- get the  country name
      * */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Constant.hideSoftKeyBoard(this, et_email)

                val countryFlag = data!!.getIntExtra("country_flag", -1)
                myPhoneCountryCode = data.getStringExtra("country_phone_code")!!
                myCountryCode = data.getStringExtra("country_code")!!
                myCountryName = data.getStringExtra("country_Name")!!
                val countryCode = "+$myPhoneCountryCode"
                tv_country.text = myCountryName
                tv_country.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                countryValidation()
                println("*******$countryFlag" + "myName")
                }
        }
    }

    /*SIGN UP validation
    * in this sign up all the validation in If condition
    * */
    private fun signUpValidation() {

        fullNameValidation(et_fullname)
        emailValidation(et_email)
        countryValidation()
        passValidation(et_pass)
        confirmPassValidation(et_pass, et_confirm_pass)

        checkAllValidation()
    }

    private fun checkAllValidation() {
        if (!fullNameValidation(et_fullname)) {
            error_fullname.visibility = View.VISIBLE
        } else if (!emailValidation(et_email)) {
            error_email.visibility = View.VISIBLE
        } else if (!countryValidation()) {
            error_country.visibility = View.VISIBLE
        } else if (!passValidation(et_pass)) {
            error_pass.visibility = View.VISIBLE
        } else if (!confirmPassValidation(et_pass, et_confirm_pass)) {
            error_confirm_pass.visibility = View.VISIBLE
        } else {
            if (isNetworkAvailable(this, main_layout)) {
                checkEmailAvailability()
                btn_create_acc.isEnabled = false
            }
        }
    }

    private fun checkEmailAvailability() {
        val param = HashMap<String, String>()
        param.put("email", et_email.text.toString())
        param.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        getDataManager().checkEmailAvailability(param)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(data)
                        if (json!!.get(is_available).equals("1")) {
                            btn_create_acc.isEnabled = false
                            btn_fb_create_acc.isEnabled = false

                            val signUpInfo = SignUpInfo()
                            signUpInfo.setName(et_fullname.text.toString().trim())
                            signUpInfo.setEmail(et_email.text.toString().trim())
                            signUpInfo.setDob(tv_dob.text.toString().trim())
                            signUpInfo.setCountry_id(myCountryCode)
                            signUpInfo.setIsd_code(myPhoneCountryCode)
                            signUpInfo.setPassword(et_confirm_pass.text.toString().trim())
                            signUpInfo.setDevicType(Android)
                            signUpInfo.setDeviceId("")
                            signUpInfo.setDevicToken("")

                            if (!fbId.isEmpty()) {
                                signUpInfo.setSocialNetworkId(fbId)
                                signUpInfo.setSocialNetworkType("FB")
                            }

                            val intent =
                                Intent(this@SignUpActivity, SelectGenderActivity::class.java)
                            intent.putExtra("SignUpInfo", signUpInfo)
                            startActivity(intent)
                            //   finish()
                            error_email.visibility = View.GONE
                        } else {
                            btn_fb_create_acc.isEnabled = true
                            btn_create_acc.isEnabled = true
                            error_email.visibility = View.VISIBLE
                            error_email.text = json.getString("message")
                        }
                    } catch (exception: Exception) {
                        btn_fb_create_acc.isEnabled = true
                        btn_create_acc.isEnabled = true
                        showCustomToast(this@SignUpActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    btn_fb_create_acc.isEnabled = true
                    btn_create_acc.isEnabled = true
                    showCustomToast(this@SignUpActivity, getString(R.string.something_wrong))
                    Log.e("Error", "" + anError?.localizedMessage)
                }
            })
    }

    private fun fullNameValidation(etFullName: EditText): Boolean {
        if (etFullName.text.trim().toString().isEmpty()) {
            error_fullname.visibility = View.VISIBLE
            error_fullname.text = getString(R.string.please_enter_your_full_name)
            return false
        } else {
            error_fullname.visibility = View.GONE
            return true
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

    private fun passValidation(pass: EditText): Boolean {
        if (pass.text.toString().trim().isEmpty()) {
            error_pass.visibility = View.VISIBLE
            error_pass.text = getString(R.string.please_enter_a_password)
            return false
        } else if (pass.text.toString().trim().isEmpty()) {
            error_pass.visibility = View.VISIBLE
            error_pass.text = getString(R.string.please_enter_a_password)
            return false
        } else {
            error_pass.visibility = View.GONE
            return true
        }
    }


    private fun confirmPassValidation(pass: EditText, cPass: EditText): Boolean {
        if (cPass.text.toString().trim().isEmpty()) {
            error_confirm_pass.visibility = View.VISIBLE
            error_confirm_pass.text = getString(R.string.re_enter_password)
            return false
        } else if (!pass.text.toString().trim().equals(cPass.text.toString().trim())) {
            error_confirm_pass.visibility = View.VISIBLE
            error_confirm_pass.text = getString(R.string.pass_does_not_match_the_confirm_pass)
            return false
        } else {
            error_confirm_pass.visibility = View.GONE
            return true
        }
    }

    private fun countryValidation(): Boolean {
        if (tv_country.text.toString().equals("Country")) {
            error_country.visibility = View.VISIBLE
            return false
        } else {
            error_country.visibility = View.GONE
            return true
        }
    }

    override fun onResume() {
        super.onResume()
        btn_create_acc.isEnabled = true
    }

}

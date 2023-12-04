package com.doviesfitness.ui.authentication.login


import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.clubz.helper.SyncWorker
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.LoginResponce
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.authentication.signup.activity.PrivacyPolicyAndTACActivity
import com.doviesfitness.ui.authentication.signup.activity.SignUpActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.GithubTypeConverters
import com.doviesfitness.ui.room_db.LocalStreamVideoDataModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.showCustomToast
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.message
import com.doviesfitness.utils.StringConstant.Companion.settings
import com.doviesfitness.utils.StringConstant.Companion.success
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.error_pass
import kotlinx.android.synthetic.main.activity_login.et_pass
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class LoginActivity : BaseActivity(), View.OnClickListener {
    private var firebaseToken: String = ""
    private var mLastClickTime: Long = 0
    lateinit var callbackManager: CallbackManager
    private var graphObject: JSONObject? = null
    lateinit var dataRequest: GraphRequest


    val eMail = "email"
    private val usernamePattern = "^(?=.*[a-z A-Z]).{1,30}\$"
    private var personName: String = ""
    private var email: String = ""
    private var fbId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        hideNavigationBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(R.layout.activity_login_new)
        inItView()
        privacyPolicyAndTermsOfUse()


    }

    private fun getString(textView: TextView): String {
        return textView.text.toString().trim { it <= ' ' }
    }


    fun inItView() {
        Slidr.attach(this)
        getFirebaseTocan()
        Log.v("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
        firebaseToken =
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        Log.v("firebaseToken..", "" + firebaseToken)
        login_button1.setReadPermissions(Arrays.asList(eMail))//fb email permission
        callbackManager = CallbackManager.Factory.create()

        btn_login.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_new_account.setOnClickListener(this)
        sub_main_layout.setOnClickListener(this)
        login_button1.setOnClickListener(this)
        fb_btn.setOnClickListener(this)
        tv_forgotpass.setOnClickListener(this)



    }
//    @Throws(IOException::class)
//    fun foo() {
//        throw IOException()
//    }

    private fun privacyPolicyAndTermsOfUse() {
        val builder = SpannableStringBuilder()
        val text1 =  SpannableString(getString(R.string.by_login_an_account_you_agree_to_doviesfitness) + " ")



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
        notMember_joins()
    }
    private fun notMember_joins() {
        val builder = SpannableStringBuilder()
        val text1 =   SpannableString(getString(R.string.not_member) + " ")
        builder.append(text1)
        val privacyPolicy = SpannableString(getString(R.string.join_us))

        privacyPolicy.setSpan(StyleSpan(Typeface.BOLD), 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(UnderlineSpan(), 0, privacyPolicy.length, 0)

        privacyPolicy.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)),
                0,
                privacyPolicy.length,
                0
        )
        builder.append(privacyPolicy)

        tv_new_account.setText(builder)
        tv_new_account.setMovementMethod(LinkMovementMethod.getInstance())
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
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_login -> {
//                foo()
                loginValidation()

            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.tv_new_account -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
                intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.sub_main_layout -> {
                Constant.hideSoftKeyBoard(this, et_username)
            }
            R.id.login_button1 -> {
                fbResponse()
            }
            R.id.fb_btn -> {
                fbLogout()
                login_button1.performClick()
            }
            R.id.tv_forgotpass -> {
                val intent = Intent(this, ResetPassword1Activity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun fbResponse() {
        login_button1.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                getUserDetails(result)
            }

            override fun onCancel() {

                }

            override fun onError(error: FacebookException?) {

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
                            val imageUrl = `object`!!.getJSONObject("picture").getJSONObject("data")
                                .getString("url")
                        }
                        if (graphObject!!.has("email")) {
                            email = graphObject!!.getString("email")
                        }

                        fbLoginApi()

                    } catch (ex: java.lang.Exception) {
                        showCustomToast(this@LoginActivity, getString(R.string.something_wrong))
                    }
                }
            })
        val bundle = Bundle()
        bundle.putString("fields", "id, name, email, picture.type(large)")
        dataRequest.setParameters(bundle)
        dataRequest.executeAsync()
    }

    private fun fbLoginApi() {
        if (Constant.isNetworkAvailable(this, sv_main)) {
            setLoading(true)
            val header = HashMap<String, String>()
            header.put("device_id", "")
            header.put("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
            header.put("device_type", "Android")
            if (graphObject != null) {
                header.put("social_network_id", fbId)
                header.put("social_network_type", "FB")
            }
            getDataManager().doLogin(header)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("login response","login response..."+response.toString())
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
                                ""
                            )
                            getDataManager().setUserInfo(userInfoBean)

                            if (getDataManager().isLoggedIn()&&
                                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                                val mWorkManager = WorkManager.getInstance()
                                mWorkManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
                            }
                            intent = Intent(this@LoginActivity, HomeTabActivity::class.java)
                            startActivity(intent)
                            fbLogout()
                            finishAffinity()
                            finish()
                        } else if (success.equals("2")) {
                            val signUpInfo = SignUpInfo()
                            signUpInfo.setSocialNetworkId(fbId)
                            signUpInfo.setName(personName)
                            signUpInfo.setEmail(email)
                            fbLogout()
                            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                            intent.putExtra("SignUpInfo", signUpInfo)
                            startActivity(intent)
                            finish()
                        } else {
                            btn_login.isEnabled = true
                            showCustomToast(this@LoginActivity, message!!)
                        }
                    } catch (exception: Exception) {
                        btn_login.isEnabled = true
                        setLoading(false)
                        showCustomToast(this@LoginActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    btn_login.isEnabled = true
                    setLoading(false)
                }
            })
        }
    }

    /****FACEBOOK Logout****/
    fun fbLogout() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired()

        if (isLoggedIn) {
            LoginManager.getInstance().logOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginValidation() {
        userNameValidation(et_username)
        passValidation(et_pass)

        checkAllValidation()
    }

    private fun checkAllValidation() {
        if (!userNameValidation(et_username)) {
            error_username.visibility = View.VISIBLE

        } else if (!isUserNameValid(et_username)) {
            error_username.visibility = View.VISIBLE
        } else if (!passValidation(et_pass)) {
            error_pass.visibility = View.VISIBLE
        } else {
            if (Constant.isNetworkAvailable(this, sv_main)) {
                loginApi()
                btn_login.isEnabled = false
            }
        }
    }

    private fun userNameValidation(et_username: EditText): Boolean {
        if (et_username.text.toString().trim().isEmpty()) {
            error_username.visibility = View.VISIBLE
            error_username.text = getString(R.string.please_enter_your_user_name)
            return false
        } else if (et_username.text.toString().trim().length < 3) {
            error_username.visibility = View.VISIBLE
            error_username.text = getString(R.string.please_enter_your_user_name)
            return false
        } else {
            error_username.visibility = View.GONE
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


    fun isUserNameValid(textView: EditText): Boolean {
        val pattern = Pattern.compile(usernamePattern)
        val matcher = pattern.matcher(getString(textView))
        val bool = matcher.matches()
        if (!bool) {
            error_username.visibility = View.VISIBLE
            error_username.text = getString(R.string.please_enter_your_user_name)
        }
        return bool
    }

    private fun loginApi() {

        val header = HashMap<String, String>()
        header.put(StringConstant.device_id, "")
        header.put(StringConstant.device_type, "Android")
        header.put(StringConstant.device_token, getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        header.put(StringConstant.password, et_pass.text.toString().trim())
        header.put(StringConstant.user_name, et_username.text.toString().trim())
        if (graphObject != null) {
            header.put(StringConstant.user_name, et_username.text.toString().trim())
        }

        setLoading(true)
        getDataManager().doLogin(header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("login response","login response..."+response.toString())
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
                            firebaseToken)
                        getDataManager().setUserInfo(userInfoBean)
                        if (getDataManager().isLoggedIn()&&
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
                            val mWorkManager = WorkManager.getInstance()
                            mWorkManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
                        }

                        GetData(userInfoBean.customer_user_name,userInfoBean.customer_id,this@LoginActivity).execute()



                    } else {
                        btn_login.isEnabled = true
                        showCustomToast(this@LoginActivity, message!!)
                    }
                } catch (exception: Exception) {
                    btn_login.isEnabled = true
                    setLoading(false)
                    showCustomToast(this@LoginActivity, getString(R.string.something_wrong))
                }
            }

            override fun onError(anError: ANError?) {
                btn_login.isEnabled = true
                setLoading(false)
            }
        })
    }


    internal class GetData(UName: String, UserId: String, loginActivity: LoginActivity):
        AsyncTask<Void, Void, String>() {
        var uName=UName
        var userId=UserId
        var loginActivity=loginActivity

        override fun doInBackground(vararg params: Void): String {
            var dList = ArrayList<VideoCategoryModal>()
            try {
                var downloadList: ArrayList<LocalStreamVideoDataModal>? = ArrayList()
                downloadList = DatabaseClient.getInstance(Doviesfitness.instance).appDatabase
                    .taskDao()
                    .getDownloadedList(uName, userId) as ArrayList<LocalStreamVideoDataModal>?
                if (downloadList != null && downloadList.size > 0) {
                    val download = downloadList[0]
                 //   dList = GithubTypeConverters.stringToDownloadList(download.getWList()) as ArrayList<DownloadedModal>
                    dList = GithubTypeConverters.stringToDownloadList1(download.getWList()) as ArrayList<VideoCategoryModal>

                    if (dList!=null&&dList.size>0)
                    //    DownloadUtil.setDownloadedData(dList)
                        DownloadVideosUtil.setDownloadedData(dList)

                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return ""
        }
        override fun onPostExecute(list:String) {
            val intent = Intent(loginActivity, HomeTabActivity::class.java)
            loginActivity.startActivity(intent)
            loginActivity.finishAffinity()
            loginActivity.finish()
        }
    }

}
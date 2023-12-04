package com.doviesfitness.ui.setting

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivitySettingBinding
import com.doviesfitness.setting.ContactUsActivity
import com.doviesfitness.setting.InviteFriendsActivity
import com.doviesfitness.setting.recivers.MyNotificationPublisher
import com.doviesfitness.ui.authentication.ChangePasswordActivity
import com.doviesfitness.ui.authentication.signup.activity.CountrySelectionActivity
import com.doviesfitness.ui.authentication.signup.activity.PrivacyPolicyAndTACActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.DeleteAccountDialog
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.LogoutDialog
import com.doviesfitness.ui.profile.editProfile.EditProfileFragment
import com.doviesfitness.ui.setting.faq.FAQActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.ReminderTimeDialog
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.Utility
import com.doviesfitness.utils.WorkoutLevelDialog
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_signup.error_country
import kotlinx.android.synthetic.main.activity_signup.tv_country
import org.json.JSONObject
import java.util.Calendar


class SettingActivity : BaseActivity(), View.OnClickListener,
    WorkoutLevelDialog.HeightWeightCallBack,DeleteAccountDialog.IsAcDelete,
    ReminderTimeDialog.ReminderCallBack {
    companion object {
        public val NOTIFICATION_CHANNEL_ID = "10001"
        private val default_notification_channel_id = "default"
    }

    private var previewStatus: String? =""
    var myPhoneCountryCode: String = ""
    var myCountryCode: String = ""
    var myCountryName: String = ""
    internal var isNotification = ""
    var unitStr = ""
    private var mLastClickTime: Long = 0
    lateinit var dialog: DeleteAccountDialog
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        inItView()
    }

    private fun inItView() {

        hideNavigationBar()

        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!.equals("")) {
            binding.unitValue.text =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
            unitStr = getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
        } else {
            binding.unitValue.text = "Metric"
            unitStr = "Metric"
        }
        if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_COUNTRY_NAME)!!.equals(
                ""
            )
        ) {
            binding.countryName.text =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_COUNTRY_NAME)!!
        } else {

        }
        if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION)!!.equals(
                ""
            )
        ) {
            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION)!!.equals(
                    "yes"
                )
            ) {
                isNotification = "yes"
                binding.ivNotification.setImageDrawable(resources.getDrawable(R.drawable.new_active_toogle_ico))

            } else {
                isNotification = "no"
                binding.ivNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_inactive_toggle_ico))

            }
        } else {
            /* isNotification = "no"
             binding.ivNotification.setImageDrawable(resources.getDrawable(R.drawable.new_inactive_toggle_ico))*/

            isNotification = "yes"
            binding.ivNotification.setImageDrawable(resources.getDrawable(R.drawable.new_active_toogle_ico))
            getDataManager().setUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION, "yes")

        }
        if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.equals("")) {
            var str =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(
                    ":"
                )
            if (str[0].toInt() > 12) {
                var hr = (str[0].toInt() - 12)
                binding.reminderTime.text = hr.toString() + ":" + str[1] + " PM"
            } else if (str[0].toInt() == 12) {
                binding.reminderTime.text = str[0] + ":" + str[1] + " PM"
            } else {
                binding.reminderTime.text = str[0] + ":" + str[1] + " AM"
            }
        } else {
            getDataManager().setUserStringData(
                AppPreferencesHelper.PREF_REMINDER_TIME,
                "07:00"
            )
        }


        // preview Status Manage case
        previewStatus = getDataManager().getPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS)
        if (previewStatus != null) {
            if (previewStatus.equals("On")) {
                binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.new_active_toogle_ico))
            }else{
                binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_inactive_toggle_ico))
            }
        }

        setOnClick(
            binding.ivBack, binding.rlEditProfile,
            binding.rlCountry, binding.rlChangePassword,
            binding.rlTermsandcondition, binding.rlAppfaq,
            binding.rlPrivacypolicy, binding.rlContectus,
            binding.ivNotification, binding.ivPreview, binding.rlRatesUsaap,
            binding.rlInvitefriend, binding.rlUnits,
            binding.rlRemindertime,
            binding.rlDeleteAc,
            binding.rlAboutApplication, binding.rlLogout
        )
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.rl_edit_profile -> {
                val editProfile = EditProfileFragment()
                val args = Bundle()
                editProfile.setArguments(args)
                addFragment(editProfile.newInstance("FromSetting"), R.id.container_id1, true)
            }

            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.rl_invitefriend -> {
                startActivity(Intent(this@SettingActivity, InviteFriendsActivity::class.java))
            }

            R.id.rl_aboutApplication -> {
                handleDoubleClick()
                startActivity(Intent(this@SettingActivity, AboutApplicationActivity::class.java))
            }

            R.id.rl_ratesUsaap -> {
                val uri = Uri.parse("market://details?id=" + getPackageName())
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())
                        )
                    )
                }
            }

            R.id.iv_notification -> {
                handleDoubleClick()
                if (isNotification.equals("no")) {
                    binding.ivNotification.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.new_active_toogle_ico))
                    getDataManager().setUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION, "yes")
                    isNotification = "yes"
                    if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.equals("")) {
                        var str =
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(":")
                        Utility.scheduleNotification(this, str[0], str[1])
                    } else {
                        Utility.scheduleNotification(this, "7", "0")
                    }

                    //isPublished = false
                    //publishedProfile("1")
                } else {
                    binding.ivNotification.setImageDrawable(
                        ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.ic_inactive_toggle_ico
                        )
                    )
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.PREF_IS_NOTIFICATION,
                        "no"
                    )
                    isNotification = "no"
                    val myIntent = Intent(this, MyNotificationPublisher::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarmManager = getSystemService(Application.ALARM_SERVICE) as AlarmManager
                    alarmManager.cancel(pendingIntent)
                    //publishedProfile("0")
                }
            }

            R.id.iv_preview -> {
                handleDoubleClick()
                previewStatus = getDataManager().getPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS)
                if (previewStatus.equals("On")) {
                    activieInactivePreview("Off")
                } else {
                    activieInactivePreview("On")
                }
            }


            R.id.rl_country -> {
                handleDoubleClick()
                intent = Intent(this, CountrySelectionActivity::class.java)
                startActivityForResult(intent, Constant.SECOND_ACTIVITY_REQUEST_CODE)
            }

            R.id.rl_change_password -> {
                handleDoubleClick()
                intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.rl_termsandcondition -> {
                handleDoubleClick()
                intent = Intent(this, PrivacyPolicyAndTACActivity::class.java)
                intent.putExtra("from", "tnc")
                startActivity(intent)
            }

            R.id.rl_privacypolicy -> {
                handleDoubleClick()
                intent = Intent(this, PrivacyPolicyAndTACActivity::class.java)
                intent.putExtra("from", "privacy policy")
                startActivity(intent)
            }

            R.id.rl_appfaq -> {
                handleDoubleClick()
                intent = Intent(this, FAQActivity::class.java)
                intent.putExtra("from", "privacy policy")
                startActivity(intent)
            }

            R.id.rl_contectus -> {
                handleDoubleClick()
                startActivity(Intent(this@SettingActivity, ContactUsActivity::class.java))
            }
            R.id.rl_remindertime -> {
                var values = arrayListOf<String>()
                var Hvalues = arrayListOf<String>()
                values = getValue()
                Hvalues = getHourValue()

                Log.v(
                    "str",
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!
                )
                val str =
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(
                        ":"
                    )
                val openDialog = ReminderTimeDialog.newInstance(
                    values,
                    Hvalues,
                    this,
                    getActivity(),
                    str[0],
                    str[1]
                )


                openDialog.isCancelable = false
                openDialog.show(supportFragmentManager)

            }

            R.id.rl_units -> {
                val values = arrayListOf<String>()
                values.add("Imperial")
                values.add("Metric")
                values.toTypedArray()
                var index = 0
                if (unitStr.equals("Imperial"))
                    index = 0
                else index = 1
                val openDialog = WorkoutLevelDialog.newInstance(values, this, getActivity(), index)
                    .show(supportFragmentManager)
            }
            R.id.rl_deleteAc->{
                binding.rlBlurlayout.visibility=View.VISIBLE
                dialog= DeleteAccountDialog.newInstance("Delete", resources.getString(R.string.delete_account_txt))
                dialog.setListener(this)
                dialog.show(supportFragmentManager!!)

            }
            R.id.rl_logout -> {
                LogoutDialog.newInstance("", "").show(supportFragmentManager)
            }
        }
    }

    private fun activieInactivePreview(status: String) {
        val param = HashMap<String, String>()
        param.put("status", status)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)

        getDataManager().updatePreviewStatus(header, param)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    Log.v("response", "" + response)
                    val mesgStatus = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (mesgStatus!!.equals("1")) {
                        if (status.equals("On")) {
                            binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.new_active_toogle_ico))
                            getDataManager().setPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS, "On")
                        } else {
                            binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_inactive_toggle_ico))
                            getDataManager().setPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS, "Off")
                        }
                    } else {
                        Constant.showCustomToast(this@SettingActivity, ""+msg)
                    }
                }
                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@SettingActivity)
                }
            })
    }

    fun handleDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }


    override fun timeOnClick(index: Int, value: String, index1: Int, value1: String) {
        if (value.toInt() > 12) {
            val hr = (value.toInt() - 12)
            binding.reminderTime.text = hr.toString() + ":" + value1 + " PM"
        } else if (value.toInt() == 12) {
            binding.reminderTime.text = value + ":" + value1 + " PM"
        } else {
            binding.reminderTime.text = value + ":" + value1 + " AM"
        }
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, value.toInt())
        calendar.set(Calendar.MINUTE, value1.toInt())
        calendar.set(Calendar.SECOND, 0)
        getDataManager().setUserStringData(
            AppPreferencesHelper.PREF_REMINDER_TIME,
            "" + value + ":" + value1
        )
        if (isNotification.equals("yes"))
            Utility.scheduleNotification(this, value, value1)

    }

    override fun levelOnClick(index: Int, str: String) {
        binding.unitValue.text = str
        unitStr = str
        getDataManager().setUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE, str)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getValue(): ArrayList<String> {
        val values = arrayListOf<String>()
        for (i in 0..59) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }

    private fun getHourValue(): ArrayList<String> {
        val values = arrayListOf<String>()
        for (i in 0..23) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }

    /*get the result from the CountrySelectionActivity.class
    * @param :- get the country flag
     * @param :- get the  country code
     * @param:- get the  country name
     * */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val country_flag = data!!.getIntExtra("country_flag", -1)
                myPhoneCountryCode = data.getStringExtra("country_phone_code")!!
                myCountryCode = data.getStringExtra("country_code")!!
                myCountryName = data.getStringExtra("country_Name")!!
                val country_code = "+$myPhoneCountryCode"
                binding.countryName.text = myCountryName
                //   Log.d("isd_code","isd_code..."+)
                getDataManager().setUserStringData(
                    AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_COUNTRY_NAME,
                    myCountryName
                )
                updateCountry()
                //countryValidation()
                //   println("*******$country_flag" + "myName")
                /*if (country_flag != -1) {
                    ivCountryFlag.setImageResource(country_flag)
                }*/
            }
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

    private fun scheduleNotification(delay: Long) {
        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    or PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            delay,
            AlarmManager.INTERVAL_HALF_HOUR,
            pendingIntent
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)

    }

    private fun updateCountry() {
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        val param = HashMap<String, String>()
        param.put("isd_code", "" + myPhoneCountryCode)
        param.put("country_code", "" + myCountryCode)
        param.put("auth_customer_id", getDataManager().getUserInfo().customer_auth_token)

        getDataManager().updateCountry(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        setLoading(false)
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            Log.d("Country Error", "Country Erro success...." + message)
                        } else {
                        }
                    } catch (exce: Exception) {
                        Log.d("Country Error", "Country Erro catch...." + exce!!.printStackTrace())

                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("Country Error", "Country Erro...." + anError!!.printStackTrace())
                    try {
                        Constant.errorHandle(anError!!, getActivity())
                    } catch (e: java.lang.Exception) {
                    }
                }
            })
    }

    override fun isAcDelete() {
        binding.rlBlurlayout.visibility=View.GONE
    }


}

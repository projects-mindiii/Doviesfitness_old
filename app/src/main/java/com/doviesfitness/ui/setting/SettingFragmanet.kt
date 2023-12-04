package com.doviesfitness.ui.setting

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentSettingFragmanetBinding
import com.doviesfitness.setting.ContactUsActivity
import com.doviesfitness.setting.InviteFriendsActivity
import com.doviesfitness.setting.recivers.MyNotificationPublisher
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.authentication.ChangePasswordActivity
import com.doviesfitness.ui.authentication.signup.activity.CountrySelectionActivity
import com.doviesfitness.ui.authentication.signup.activity.PrivacyPolicyAndTACActivity
import com.doviesfitness.ui.base.BaseFragment
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
import org.json.JSONObject
import java.util.Calendar


class SettingFragmanet : BaseFragment(), View.OnClickListener,
    WorkoutLevelDialog.HeightWeightCallBack,
    ReminderTimeDialog.ReminderCallBack,DeleteAccountDialog.IsAcDelete {

    private lateinit var intent: Intent
    var myPhoneCountryCode: String ?= ""
    var myCountryCode: String = ""
    var myCountryName: String = ""
    internal var isNotification = ""
    private var isPreview: String = ""
    var unitStr = ""
    private var previewStatus: String? = ""

    lateinit var binding: FragmentSettingFragmanetBinding
    private var mLastClickTime: Long = 0
    lateinit var dialog: DeleteAccountDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //binding = DataBindingUtil.inflate(inflater, R.layout.activity_setting, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setting_fragmanet, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {

        hideNavigationBar()


        val windowBackground = activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
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
            isNotification = "no"
            binding.ivNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_inactive_toggle_ico))

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
        previewStatus =
            getDataManager().getPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS)
        if (previewStatus != null) {
            if (previewStatus.equals("On")) {
                binding.ivPreview.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.new_active_toogle_ico
                    )
                )
            } else {
                binding.ivPreview.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_inactive_toggle_ico
                    )
                )
            }
        }
        if(getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes"))
        {
            binding.rlManageSubcr.visibility=View.GONE
            binding.vvSubLine.visibility=View.GONE
        }else{

            binding.rlManageSubcr.visibility=View.VISIBLE
            binding.vvSubLine.visibility=View.VISIBLE
        }

        setOnClick(
            binding.ivBack, binding.rlEditProfile,
            binding.rlCountry, binding.rlChangePassword,
            binding.rlTermsandcondition, binding.rlAppfaq,
            binding.rlPrivacypolicy, binding.rlContectus,
            binding.ivNotification, binding.ivPreview, binding.rlRatesUsaap,
            binding.rlInvitefriend, binding.rlUnits,
            binding.rlRemindertime,
            binding.rlDeleteAc,binding.rlManageSubcr,
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
                addFragment(editProfile.newInstance("FromSetting"), R.id.container_id, true)
            }

            R.id.iv_back -> {
                activity!!.onBackPressed()
            }

            R.id.rl_invitefriend -> {
                startActivity(Intent(mContext, InviteFriendsActivity::class.java))
            }

            R.id.rl_aboutApplication -> {
                startActivity(Intent(mContext, AboutApplicationActivity::class.java))
            }

            R.id.rl_manage_subcr -> {


            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0")
                ) {

                startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))

            }else{
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions")
                )
                startActivity(browserIntent)
            }
            }

            R.id.rl_ratesUsaap -> {
                val uri = Uri.parse("market://details?id=" + mContext.getPackageName())
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
                            Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())
                        )
                    )
                }
            }

            R.id.iv_notification -> {
                if (isNotification.equals("no")) {
                    binding.ivNotification.setImageDrawable(
                        ContextCompat.getDrawable(
                            mContext,
                            R.drawable.new_active_toogle_ico
                        )
                    )
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.PREF_IS_NOTIFICATION,
                        "yes"
                    )
                    isNotification = "yes"
                    if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.equals(
                            ""
                        )
                    ) {
                        var str =
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(
                                ":"
                            )
                        Utility.scheduleNotification(context!!, str[0], str[1])
                    } else {
                        Utility.scheduleNotification(context!!, "7", "0")
                    }

                } else {
                    binding.ivNotification.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_inactive_toggle_ico)
                    )
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.PREF_IS_NOTIFICATION,
                        "no"
                    )
                    isNotification = "no"
                    val myIntent = Intent(context, MyNotificationPublisher::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarmManager =
                        context!!.getSystemService(Application.ALARM_SERVICE) as AlarmManager
                    alarmManager.cancel(pendingIntent)

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
                intent = Intent(mContext, CountrySelectionActivity::class.java)
                startActivityForResult(intent, Constant.SECOND_ACTIVITY_REQUEST_CODE)
            }

            R.id.rl_change_password -> {
                intent = Intent(mContext, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.rl_termsandcondition -> {
                intent = Intent(mContext, PrivacyPolicyAndTACActivity::class.java)
                intent.putExtra("from", "tnc")
                startActivity(intent)
            }

            R.id.rl_privacypolicy -> {
                intent = Intent(mContext, PrivacyPolicyAndTACActivity::class.java)
                intent.putExtra("from", "privacy policy")
                startActivity(intent)
            }

            R.id.rl_appfaq -> {
                intent = Intent(mContext, FAQActivity::class.java)
                intent.putExtra("from", "privacy policy")
                startActivity(intent)
            }

            R.id.rl_contectus -> {
                startActivity(Intent(mContext, ContactUsActivity::class.java))
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
                val openDialog =
                    ReminderTimeDialog.newInstance(values, Hvalues, this, mContext, str[0], str[1])
                openDialog.isCancelable = false
                openDialog.show(fragmentManager!!)

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
                val openDialog = WorkoutLevelDialog.newInstance(values, this, mContext, index)
                    .show(fragmentManager!!)
            }
            R.id.rl_deleteAc->{
               binding.rlBlurlayout.visibility=View.VISIBLE
              dialog= DeleteAccountDialog.newInstance("Delete", resources.getString(R.string.delete_account_txt))
                dialog.setListener(this)
                   dialog.show(fragmentManager!!)

            }
            R.id.rl_logout -> {
                LogoutDialog.newInstance("", "").show(fragmentManager!!)
            }
        }
    }


    fun handleDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
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
                            binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.new_active_toogle_ico))
                            getDataManager().setPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS, "On")
                        } else {
                            binding.ivPreview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_inactive_toggle_ico))
                            getDataManager().setPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS, "Off")
                        }

                    } else {
                        Constant.showCustomToast(mContext, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
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
                getDataManager().setUserStringData(
                    AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_COUNTRY_NAME,
                    myCountryName
                )
                updateCountry()
                //countryValidation()
                println("*******$country_flag" + "myName")
                /*if (country_flag != -1) {
                    ivCountryFlag.setImageResource(country_flag)
                }*/
            }
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
            Utility.scheduleNotification(context!!, value, value1)

    }

    override fun levelOnClick(index: Int, str: String) {
        binding.unitValue.text = str
        unitStr = str
        getDataManager().setUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE, str)
    }

    private fun scheduleNotification(delay: Long) {
        val notificationIntent = Intent(mContext, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext.applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    or PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            delay,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        );
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

        getDataManager().updateCountry(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                try {
                    setLoading(false)
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                    } else {
                    }
                } catch (exce: Exception) {
                }
            }

            override fun onError(anError: ANError?) {
                try {
                    Constant.errorHandle(anError!!, activity)
                } catch (e: java.lang.Exception) {
                }
            }
        })
    }

    override fun isAcDelete() {
        binding.rlBlurlayout.visibility=View.GONE
    }

}

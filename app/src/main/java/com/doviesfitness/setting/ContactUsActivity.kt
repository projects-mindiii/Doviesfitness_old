package com.doviesfitness.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityContactUsBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.WorkoutLevelDialog
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject

class ContactUsActivity : BaseActivity(), View.OnClickListener,
    WorkoutLevelDialog.HeightWeightCallBack {


    lateinit var binding: ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        initialization()
    }

    private fun initialization() {

        hideNavigationBar()
        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        binding.inviteBtn.setOnClickListener(this)
        binding.subjectLayout.setOnClickListener(this)
        binding.facebookIcon.setOnClickListener(this)
        binding.instaIcon.setOnClickListener(this)
        binding.twitterIcon.setOnClickListener(this)
        binding.doviesIcon.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

        binding.nameTxt.hint = (Html.fromHtml(getString(R.string.name)));
        binding.emailTxt.hint = (Html.fromHtml(getString(R.string.email_address)));
        binding.subjectTxt.hint = (Html.fromHtml(getString(R.string.subject_feedback)));
        binding.messageTxt.hint = (Html.fromHtml(getString(R.string.message_feedback)));
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.invite_btn -> {
                if (isValidData())
                    contactUs()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.facebook_icon -> {
                socialView("https://www.facebook.com/doviesfitness/")
            }
            R.id.insta_icon -> {
                socialView("https://www.instagram.com/Doviesfitness/")
            }
            R.id.twitter_icon -> {
                socialView("https://twitter.com/Doviesfitness")
            }
            R.id.dovies_icon -> {
                socialView("https://dev.doviesfitness.com/")
            }
            R.id.subject_layout -> {
                var values = arrayListOf<String>()
                values.add("Report an issue")
                values.add("General Feedback")
                values.add("Advertisement And Promo")
                values.toTypedArray()
                val openDialog = WorkoutLevelDialog.newInstance(values, this, getActivity(), 0)
                    .show(supportFragmentManager)
            }
        }
    }

    private fun isValidData(): Boolean {
        if (binding.nameEdt.text.toString().trim().isEmpty()) {
            binding.errorNameTxt.text = "Please enter name"
            binding.errorNameTxt.visibility = View.VISIBLE
            return false
        } else if (binding.emailEdt.text.toString().trim().isEmpty()) {
            binding.errorEmailTxt.text = "Please enter email address"
            binding.errorNameTxt.visibility = View.GONE
            binding.errorEmailTxt.visibility = View.VISIBLE
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdt.text).matches()) {
            binding.errorEmailTxt.text = "Email address is invalid"
            binding.errorNameTxt.visibility = View.GONE
            binding.errorEmailTxt.visibility = View.VISIBLE
            return false
        } else if (binding.subjectValue.text.toString().trim().isEmpty()) {
            binding.errorSubjectTxt.text = "Please select subject"
            binding.errorNameTxt.visibility = View.GONE
            binding.errorEmailTxt.visibility = View.GONE
            binding.errorSubjectTxt.visibility = View.VISIBLE
            return false
        } else if (binding.message.text.toString().trim().isEmpty()) {
            binding.errorMessageTxt.text = "Please enter your feedback"
            binding.errorNameTxt.visibility = View.GONE
            binding.errorEmailTxt.visibility = View.GONE
            binding.errorSubjectTxt.visibility = View.GONE
            binding.errorMessageTxt.visibility = View.VISIBLE
            return false
        } else {
            binding.errorNameTxt.visibility = View.GONE
            binding.errorEmailTxt.visibility = View.GONE
            binding.errorSubjectTxt.visibility = View.GONE
            binding.errorMessageTxt.visibility = View.GONE
            return true
        }

    }

    override fun levelOnClick(index: Int, str: String) {
        binding.subjectValue.text = str
    }

    private fun contactUs() {
        binding.progressLayout.visibility = View.VISIBLE
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.email, binding.emailEdt.text.toString().trim())
        param.put("subject", binding.subjectValue.text.toString().trim())
        param.put("contactName", binding.nameEdt.text.toString().trim())
        param.put("message", binding.message.text.toString().trim())
        param.put("iCustomerId", getDataManager().getUserInfo().customer_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().contactUs(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    binding.progressLayout.visibility = View.GONE
                    Constant.showCustomToast(getActivity(), "" + message)
                    finish()
                } else {
                    Constant.showCustomToast(getActivity(), "fail..." + message)
                    binding.progressLayout.visibility = View.GONE
                }
            }

            override fun onError(anError: ANError) {
                binding.progressLayout.visibility = View.GONE
                Constant.errorHandle(anError, getActivity()!!)
                Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
            }
        })
    }

    private fun socialView(url: String) {
        val uri = Uri.parse(url)
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
                    Uri.parse(url)
                )
            )
        }
    }

}
package com.doviesfitness.ui.authentication.signup.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.LoginResponce
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.showCustomToast
import com.doviesfitness.utils.PermissionAll
import com.doviesfitness.utils.StringConstant.Companion.message
import com.doviesfitness.utils.StringConstant.Companion.settings
import com.doviesfitness.utils.StringConstant.Companion.success
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_profile_photo.*
import kotlinx.android.synthetic.main.activity_add_profile_photo.iv_back
import kotlinx.android.synthetic.main.activity_add_profile_photo.iv_next_btn
import org.json.JSONObject
import java.io.File

import kotlin.collections.HashMap

class AddProfilePhotoActivity : BaseActivity(), View.OnClickListener {
    var userImageFile: File? = null
    lateinit var signUpInfo: SignUpInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()

        setContentView(R.layout.activity_add_profile_photo)
        initViews()

    }

    private fun initViews() {
        signUpInfo = intent.getSerializableExtra("SignUpInfo") as SignUpInfo
        tv_skip.setOnClickListener(this)
        iv_next_btn.setOnClickListener(this)
        iv_upload.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        val permission = PermissionAll()
        permission.RequestMultiplePermission(this@AddProfilePhotoActivity)
    }

    override fun onClick(v: View?) {
        val imageFile = HashMap<String, File?>()

        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.tv_skip -> {
                signUpApi(imageFile)
            }
            R.id.iv_next_btn -> {
                iv_next_btn.isEnabled = false
                if (userImageFile != null) {
                    imageFile.put("profile_pic", userImageFile)
                }
                signUpApi(imageFile)
            }
            R.id.iv_upload -> {
                getImagePickerDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        iv_next_btn.isEnabled = true
    }

    override fun onBackPressed() {
        finish()
    }

    /*Sign up api calling*/
    private fun signUpApi(imageFile: HashMap<String, File?>) {
        if (Constant.isNetworkAvailable(this, rl_main)) {
            setLoading(true)
            getDataManager().doSignUp(signUpInfo, imageFile)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    setLoading(false)
                    try {
                        val json: JSONObject? = response?.getJSONObject(settings)
                        val status = json?.get(success)
                        val msg = json?.get(message)
                        if (status!!.equals("1")) {
                            val data = getDataManager().mGson!!.fromJson(response.toString(), LoginResponce::class.java)
                            val userInfoBean = UserInfoBean(
                                "No"
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
                                , ""
                                , data.data.get(0).dob
                                , ""
                                , "0",""
                            )

                            getDataManager().setUserInfo(userInfoBean)
                            getDataManager().setUserStringData(
                                AppPreferencesHelper.PREF_IS_NOTIFICATION, "yes")

                            finishAffinity()
                            intent = Intent(this@AddProfilePhotoActivity, WelcomeActivity::class.java)
                            intent.putExtra("UserName", data!!.data.get(0).customer_user_name)
                            startActivity(intent)
                            finish()

                        } else {
                            iv_next_btn.isEnabled = true
                            setLoading(false)
                            showCustomToast(this@AddProfilePhotoActivity, "" + msg)
                        }
                    } catch (ec: Exception) {
                        iv_next_btn.isEnabled = true
                        showCustomToast(this@AddProfilePhotoActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    iv_next_btn.isEnabled = true
                    showCustomToast(this@AddProfilePhotoActivity, getString(R.string.something_wrong))
                    setLoading(false)
                }
            })
        }
    }

    /*Image picker code*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            tmpUri = data.data!!
            var time= System.currentTimeMillis()
            var str=time.toString()
            var destinationPath=str+"dovies.jpg"
            val options = UCrop.Options()
            options.setHideBottomControls(true)
            UCrop.of(tmpUri, Uri.fromFile(File(cacheDir, destinationPath)))
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .start(getActivity())


        }
        else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                handleCropResult(data)
            }
        }
        else if (requestCode == Constant.CAMERA && resultCode == Activity.RESULT_OK) {
            val imageFile = getTemporalFile(this)
            val photoURI = Uri.fromFile(imageFile)
            var time= System.currentTimeMillis()
            var str=time.toString()
            var destinationPath=str+"dovies.jpg"
            val options = UCrop.Options()
            options.setHideBottomControls(true)
            UCrop.of(photoURI, Uri.fromFile(File(cacheDir, destinationPath)))
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .start(getActivity())
        }
    }

    private fun handleCropResult(@NonNull result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            Picasso.with(getActivity()).load(resultUri).into(profile_img)
            iv_placeholder.visibility = View.GONE
            profile_img.borderWidth = 0
            userImageFile=File(resultUri.path)
        } else {
            Toast.makeText(getActivity(), ""+resources.getString(R.string.cannot_retrieve_cropped_image), Toast.LENGTH_SHORT).show()
        }
    }

}

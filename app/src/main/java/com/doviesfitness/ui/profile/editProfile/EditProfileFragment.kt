package com.doviesfitness.ui.profile.editProfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doviesfitness.BuildConfig
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.remote.Webservice
import com.doviesfitness.databinding.FragmentEditProfileBinding
import com.doviesfitness.ui.authentication.signup.dialog.EditHeightDialog
import com.doviesfitness.ui.authentication.signup.dialog.EditWeightDialog
import com.doviesfitness.ui.authentication.signup.model.UserInfoModal
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.date_picker.DatePickerPopWin
import com.doviesfitness.utils.*
import com.facebook.FacebookSdk.getCacheDir
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : BaseFragment(), View.OnClickListener, EditHeightDialog.HeightCallBack,
    EditWeightDialog.WeightCallBack, HeightAndWeightDialog.HeightAndWeightCallBack,
    GenderDialog.HeightWeightCallBack, ImagePickerFragmentDialog.ImagePickerFragmentCallBack,
    HeightInCmDialog.HeightInCmCallBack {
    override fun CmValueOnClick(index: Int, value: String) {
        binding.txtHeight.text = "" + value + " cm"
        setHeight = value.toInt()
        SelectedValue = index
        hideNavigationBar()
    }

    private var dobforApi: String = ""
    private lateinit var homeTabActivity: HomeTabActivity
    private lateinit var strArray: List<String>
    private var fromWhichScreen: String = ""
    private var selectedIndex: Int = 0
    private var mCurrentPhotoPath: String? = ""
    private var setHeight: Int = 0
    var SelectedValue = 0
    private var selectedIndexOfWeight: String = "0"
    private var selectedIndexOfHeight: String = ""
    private var withoutComa: String = ""
    lateinit var binding: FragmentEditProfileBinding
    var searchText = "";
    var lastText = "";
    var isUsername: Boolean = false
    lateinit var tmpUri: Uri
    var userImageFile: File? = null
    private var startDateTime = Calendar.getInstance()
    var mDay: Int = 0
    var mMonth: Int = 0
    private var mLastClickTime: Long = 0
    var mYear: Int = 0
    var weight = 0
    var weightInKg = 0.0
    var height = 0
    var unitStr = ""
    var userImageUrl = ""
    private var pop_up_option: Pop_Up_Option? = null
    private var finishEditListener: FinishEditListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideNavigationBar()
        if (context is FinishEditListener) {
            finishEditListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /* val view = activity!!.window.decorView
         view.systemUiVisibility = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity!!.window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {

        val windowBackground = activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        arguments?.let {
            fromWhichScreen = arguments!!.getString("fromWhichScreen", "")
        }

        val permissin = PermissionAll()
        permissin.RequestMultiplePermission(mContext as Activity?)

        setOnClick(
            binding.ivBack, binding.selectIvCamera, binding.btnUpdate,
            binding.txtGender, binding.txtBirthday, binding.txtHeight, binding.txtWeight,
            binding.containerId
        )

        fieldTextWatcher()

        getProfileDataFromSession()
        /*call to get user detail data and show india fields*/
    }

    // to get data  when we come from notification
    fun newInstance(fromWhichScreen: String): EditProfileFragment {
        val myFragment = EditProfileFragment()
        val args = Bundle()
        args.putString("fromWhichScreen", fromWhichScreen)
        //args.putSerializable("fromWhichScreen", fromWhichScreen)
        myFragment.setArguments(args)
        return myFragment
    }

    private fun getProfileDataFromSession() {

        val userInfoBean = getDataManager().getUserInfo()
        userImageUrl = userInfoBean.customer_profile_image
        binding.etUsername.setText(userInfoBean.customer_user_name)
        binding.etFullName.setText(userInfoBean.customer_full_name)
        binding.txtGender.text = userInfoBean.customer_gender

        getImageInFile(userInfoBean.customer_profile_image)

        val dob: String = userInfoBean.dob
        if (!dob.isEmpty()) {
            val spiltString = dob.split(" ")
            spiltString[0]
            spiltString[1]
            spiltString[2]

            binding.txtBirthday.text = spiltString[1] + " " + spiltString[0] + ", " + spiltString[2]
            dobforApi = spiltString[1] + " " + spiltString[0] + " " + spiltString[2]
        }

        if (userInfoBean.customer_user_name.equals(binding.etUsername.text.toString())) {
            binding.etUsername.addTextChangedListener(object : TextWatcher {

                var timer = Timer()
                val DELAY: Long = 800
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //To change body of created functions use File | Settings | File Templates.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //To change body of created functions use File | Settings | File Templates.
                }

                override fun afterTextChanged(s: Editable?) {
                    /*  searchText = s.toString().trim();
                    if (searchText != lastText) {
                        lastText = searchText
                        userNameValidation(searchText)
                    }*/

                    searchText = s.toString().trim();
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
                    hideNavigationBar()
                }
            })
        }

        // "dob":"Oct 05 2019"
        // 01 jan,2017

        binding.etEmail.setText(userInfoBean.customer_email)
        if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
                .equals("") && getDataManager().getUserStringData(
                AppPreferencesHelper.PREF_UNIT_VALUE
            )!!.equals("Imperial")
        ) {

            //feet and intch
            if (!userInfoBean.customer_height.isEmpty() && !userInfoBean.customer_height.equals(".00")) {
                val cmInIntches = ConvertUnits.centimeterToFeet(userInfoBean.customer_height)
                withoutComa = cmInIntches
                binding.txtHeight.text = cmInIntches
            }

            //lbs
            if (!userInfoBean.customer_weight.isEmpty()) {
                val inkgsFronLbs =
                    ConvertUnits.getKgsToLbs2(userInfoBean.customer_weight.toDouble())
                selectedIndexOfWeight = inkgsFronLbs.toString()
                binding.txtWeight.text =
                    selectedIndexOfWeight.toDouble().toInt().toString() + " lbs"
            } else {
                binding.txtWeight.text = "NA"
            }

            unitStr = "Imperial"
        } else {
            // kgs
            if (userInfoBean.customer_weight.isEmpty() || userInfoBean.customer_weight.equals("0")) {
                binding.txtWeight.text = "0.00" + " kgs"
            } else {
                //var weight  = ConverUnits.getKgsToLbs(userInfoBean.customer_weight.toInt())
                var wstr =
                    "" + (Math.round(userInfoBean.customer_weight.toDouble() * 100)) / 100 + " kgs"
                if (wstr.contains("."))
                    binding.txtWeight.text =
                        "" + (Math.round(userInfoBean.customer_weight.toDouble() * 100)) / 100 + " kgs"
                else {
                    binding.txtWeight.text =
                        "" + (Math.round(userInfoBean.customer_weight.toDouble() * 100)) / 100 + ".00 kgs"

                }
            }
            selectedIndexOfWeight = userInfoBean.customer_weight


            //cm
            if (userInfoBean.customer_height.isEmpty() || userInfoBean.customer_height.equals("0")) {
                //binding.txtHeight.text = "NA"
                if (userInfoBean.customer_height.equals("0")) {
                    binding.txtHeight.text = userInfoBean.customer_height + " cm"
                } else {
                    binding.txtHeight.text = "NA"
                }

            } else {
                binding.txtHeight.text = userInfoBean.customer_height + " cm"
            }

            unitStr = "Metric"
            try {
                if (!userInfoBean.customer_height.isEmpty() && !userInfoBean.customer_height.equals(
                        ".00"
                    )
                ) {
                    setHeight = userInfoBean.customer_height.toInt()
                    SelectedValue = (userInfoBean.customer_height.toInt() - 91)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun getImageInFile(program_image: String) {

        Glide.with(mContext).asBitmap().placeholder(R.drawable.user_placeholder).load(program_image)
            .listener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.profileImageView.setImageBitmap(resource)
                    userImageFile = File(getCacheDir(), "dovies");
                    userImageFile!!.createNewFile();
                    var bitmap = resource
                    var bos = ByteArrayOutputStream();
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    var bitmapdata = bos.toByteArray();
                    var fos = FileOutputStream(userImageFile);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    return true
                }
            }).into(binding.profileImageView)
    }

    private fun fieldTextWatcher() {

        val userInfoBean = getDataManager().getUserInfo()
        if (userInfoBean.customer_user_name.equals(binding.etUsername.text.toString())) {
            binding.etUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //To change body of created functions use File | Settings | File Templates.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //To change body of created functions use File | Settings | File Templates.
                }

                override fun afterTextChanged(s: Editable?) {
                    searchText = s.toString().trim();
                    if (searchText != lastText) {
                        lastText = searchText
                        userNameValidation(searchText)
                    }
                    hideNavigationBar()
                }
            })
        }

        binding.etFullName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    fullNameValidation(binding.etFullName)
                }
                hideNavigationBar()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    emailValidation(binding.etEmail)
                }
                hideNavigationBar()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    private fun userNameValidation(et_username: EditText): Boolean {
        if (et_username.text.trim().toString().isEmpty()) {
            binding.tvUsernameStatus.visibility = View.VISIBLE
            binding.tvUsernameStatus.text = getString(R.string.please_enter_your_user_name)
            return false
        } else {
            binding.tvUsernameStatus.visibility = View.GONE
            return true
        }
    }

    private fun fullNameValidation(et_fullName: EditText): Boolean {
        if (et_fullName.text.trim().toString().isEmpty()) {
            binding.errorFullname.visibility = View.VISIBLE
            binding.errorFullname.text = getString(R.string.please_enter_your_user_name)
            return false
        } else {
            binding.errorFullname.visibility = View.GONE
            return true
        }
    }

    private fun emailValidation(email: EditText): Boolean {
        if (email.text.trim().isEmpty()) {
            binding.errorEmail.visibility = View.VISIBLE
            binding.errorEmail.text = getString(R.string.please_enter_email_address)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            binding.errorEmail.visibility = View.VISIBLE
            binding.errorEmail.text = getString(R.string.please_enter_valid_email_address)
            return false
        } else {
            binding.errorEmail.visibility = View.GONE
            return true
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive()) {
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)

            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {

            R.id.iv_back -> {
                //finishEditListener?.onEditFinished();
                hideSoftKeyboard(activity!!);
                activity!!.onBackPressed()
            }

            R.id.container_id -> {
                hideNavigationBar()
                view.systemUiVisibility =
                    view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            R.id.select_iv_camera -> {
                val permissin = PermissionAll()
                if (permissin.RequestMultiplePermission(mContext as Activity?)) {
                    ImagePickerFragmentDialog.newInstance(this).show(childFragmentManager)
                } else {

                }
            }

            R.id.btn_update -> {
                checkValidationForEditProfile()
            }

            R.id.txt_gender -> {
                val openDialog =
                    GenderDialog.newInstance(1, 2, "GenderForUSer", this, mContext)
                        .show(childFragmentManager)
            }

            R.id.txt_birthday -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
                var str = "1993-10-22"



                if (!binding.txtBirthday.text.toString()
                        .isEmpty() && !binding.txtBirthday.text.toString().equals(
                        "Birthday",
                        true
                    )
                ) {
                    var DStr = binding.txtBirthday.text.toString()
                    var date = Date(DStr)
                    val sdf2 = SimpleDateFormat("yyyy-MM-dd")
                    str = sdf2.format(date)
                    Log.d("selected date", "selected date..." + str)
                } else {
                    var cal = Calendar.getInstance()
                    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    str = dateFormat.format(cal.getTime())

                    // str="1993-10-22"
                }
                openDatePickerDialog(str)
                // openStartDateDialog()
            }

            R.id.txt_height -> {
                if (unitStr.equals("Metric")) {
                    var values = arrayListOf<String>()
                    values = getValue()
                    val openDialog =
                        HeightInCmDialog.newInstance(values, this, mContext, SelectedValue)
                            .show(childFragmentManager)
                } else {
                    if (!withoutComa.isEmpty()) {

                        //withoutComa = "6 6"
                        //strArray = withoutComa.split(" ")
                        var feet_intches = withoutComa.split(" ")
                        var feet = feet_intches[0]
                        var intches = feet_intches[1]
                        if (feet != null && !feet.isEmpty() && feet.contains("'")) {
                            feet = feet.replace("'", "")
                        }
                        if (intches != null && !intches.isEmpty() && intches.contains("''")) {
                            intches = intches.replace("''", "")
                        }

                        val openDialog =
                            HeightAndWeightDialog.newInstance(this, mContext, feet, intches)
                                .show(childFragmentManager)
                    } else {
                        withoutComa = "0 0"
                        strArray = withoutComa.split(" ")
                        val openDialog = HeightAndWeightDialog.newInstance(
                            this,
                            mContext,
                            strArray[0],
                            strArray[1]
                        ).show(childFragmentManager)
                    }
                }
            }

            R.id.txt_weight -> {
                if (unitStr.equals("Metric")) {
                    val openDialog = EditWeightDialog.newInstance(
                        13,
                        227,
                        selectedIndexOfWeight.toDouble().toInt(),
                        "kgs",
                        this
                    ).show(childFragmentManager)
                } else {
                    var impWStr = selectedIndexOfWeight
                    if (selectedIndexOfWeight.toDouble() < 30) {
                        selectedIndexOfWeight = "30"
                    }
                    //    selectedIndexOfWeight.toDouble().toInt()

                    val openDialog = EditWeightDialog.newInstance(
                        30,
                        500,
                        selectedIndexOfWeight.toDouble().toInt(),
                        "lbs",
                        this
                    ).show(childFragmentManager)
                }
            }
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

                    var selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, (month - 1))
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)
                    val sdf1 = SimpleDateFormat("dd MMM, yyyy")
                    val sdf2 = SimpleDateFormat("dd MMM yyyy")
                    binding.txtBirthday.setText(sdf1.format(selectedDate.getTime()))
                    dobforApi = sdf2.format(selectedDate.getTime())
                    hideNavigationBar()
                }
            }).textConfirm("Done") //text of confirm button
            .textCancel("Cancel") //text of cancel button
            //  .btnTextSize(resources.getDimension(R.dimen._5sdp).toInt()) // button text size
            .viewTextSize(resources.getDimension(R.dimen._6sdp).toInt()) // pick view text size
            .colorCancel(Color.parseColor("#232323")) //color of cancel button
            .colorConfirm(Color.parseColor("#232323"))//color of confirm button
            .minYear(1950) //min year in loop
            .maxYear(2051) // max year in loop
            .dateChose(str) // date chose when init popwindow
            .build()

        pickerPopWin.showPopWin(activity)

        pickerPopWin.setOnDismissListener { hideNavigationBar() }
    }


    private fun getValue(): ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 91..242) {
            values.add("" + i)
        }
        return values
    }

    /*select date of birth of the user*/
    private fun openStartDateDialog() {
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        Constant.hideSoftKeyBoard(mContext, et_email)
        val startDateDialog = DatePickerDialog(
            mContext,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                startDateTime = Calendar.getInstance()
                startDateTime.set(Calendar.YEAR, year)
                startDateTime.set(Calendar.MONTH, month)
                startDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //******** Date time Format **************//
                val sdf1 = SimpleDateFormat("dd MMM yyyy")
                val startDateString = sdf1.format(startDateTime.getTime())

                binding.txtBirthday.setText(sdf1.format(startDateTime.getTime()))
                dobforApi = sdf1.format(startDateTime.getTime())

                hideNavigationBar()

            }, mYear, mMonth, mDay
        )

        startDateDialog.datePicker.maxDate = System.currentTimeMillis() - 1000

        startDateDialog.show()
    }

    private fun checkValidationForEditProfile() {
        if (!userNameValidation(binding.etUsername)) {
            binding.tvUsernameStatus.visibility = View.VISIBLE
        } else if (!fullNameValidation(binding.etFullName)) {
            binding.errorFullname.visibility = View.VISIBLE
        } else if (!emailValidation(binding.etEmail)) {
            binding.errorEmail.visibility = View.VISIBLE
        } else {
            if (Constant.isNetworkAvailable(mContext, binding.llMainLayout)) {
                // binding.btnUpdate.isEnabled = false
                updateProfile()
            }
        }
    }

    private fun updateProfile() {
        binding.loader.visibility = View.VISIBLE
        val param = AndroidNetworking.upload(Webservice.UPDATE_CUSTOMER_DETAIL)

        // val weightInInt = selectedIndexOfWeight.toInt()
        if (unitStr.equals("Imperial")) {
            //in imperial here we convert lbs to kg
            weightInKg = ConvertUnits.getLbsToKgs2(selectedIndexOfWeight.toDouble())
            setHeight = ConvertUnits.getHeightFromFeetToCm(withoutComa)
        } else {
            //in metrix here we convert kg to lbs
            weightInKg = selectedIndexOfWeight.toDouble()
        }

        if (userImageFile != null) {
            param.addMultipartFile(StringConstant.profile_pic, userImageFile!!)
        } else {
            param.addMultipartParameter(StringConstant.profile_pic, userImageUrl)
        }

        //2000-12-19
        param.addMultipartParameter("name", "" + binding.etFullName.text.toString().trim())
        //param.addMultipartParameter("dob", "" + binding.txtBirthday.text)
        param.addMultipartParameter("dob", "" + dobforApi)
        param.addMultipartParameter("user_name", "" + binding.etUsername.text.toString().trim())
        param.addMultipartParameter("country_id", "91")
        param.addMultipartParameter("gender", "" + binding.txtGender.text.toString().trim())
        param.addMultipartParameter("email", "" + binding.etEmail.text.toString().trim())
        param.addMultipartParameter("height", "" + setHeight)
        param.addMultipartParameter(
            "weight",
            "" + weightInKg
        ) //weight only consider india kg so covert it kg when it is india lbs
        param.addMultipartParameter(StringConstant.device_token, "")
        param.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        param.addMultipartParameter(StringConstant.device_id, "")

        param.addHeaders(StringConstant.apiKey, "HBDEV")
        param.addHeaders(StringConstant.accept, "application/json")
        param.addHeaders(
            StringConstant.authToken,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.addHeaders(StringConstant.apiVersion, "1")
        param.addHeaders(StringConstant.contentType, "application/json")
        param.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.e("response", "response...." + response.to(4))

                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val status: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)

                if (status!!.equals("1")) {
                    binding.loader.visibility = View.GONE
                    val userInfoModal = getDataManager().mGson?.fromJson(
                        response.toString(),
                        UserInfoModal::class.java
                    )

                    val parseUserData = userInfoModal!!.data.get(0)
                    val userInfoBean = getDataManager().getUserInfo()
                    userInfoBean.customer_full_name = parseUserData.customer_full_name
                    userInfoBean.customer_user_name = parseUserData.customer_user_name
                    userInfoBean.customer_profile_image = parseUserData.customer_profile_image

                    val previewStatus = parseUserData.workout_preview_status
                    getDataManager().setPreferanceStatus(
                        AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS,
                        previewStatus
                    )

                    userInfoBean.customer_gender = parseUserData.customer_gender
                    userInfoBean.customer_email = parseUserData.customer_email
                    userInfoBean.dob = parseUserData.dob
                    userInfoBean.customer_height = parseUserData.customer_height
                    userInfoBean.customer_weight = parseUserData.customer_weight
                    getDataManager().setUserInfo(userInfoBean)

                    /* homeTabActivity = getBaseActivity() as HomeTabActivity
                    if(homeTabActivity != null){
                        homeTabActivity.onResume()
                    }*/

                    val homeTabintent = Intent(getBaseActivity(), HomeTabActivity::class.java)
                    homeTabintent.putExtra("fromWhichScreen", "WhenWorkoutPlanDetailActivity")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(homeTabintent)

                    //finishEditListener?.onEditFinished();
                    //activity!!.onBackPressed()
                    Constant.showCustomToast(context!!, "Done")
                } else {
                    Constant.showCustomToast(context!!, "" + message)
                    binding.loader.visibility = View.GONE
                }
            }

            override fun onError(anError: ANError) {
                binding.loader.visibility = View.GONE
                Constant.showCustomToast(context!!, "please set profile photo")
                Constant.errorHandle(anError, mContext as Activity)
            }
        })
    }

    /*chcek the user name is available or not
   * @param: user_name
   * @GetValue from server
   * @ is_available: 1 "so you can go to next step"
   * @ is_available: 0 "enter your username"
   * */
    private fun userNameValidation(etusername: String) {
        isUsername = false
        //iv_next_btn.isEnabled = false
        if (etusername.trim().length > 2) {
            val pram = HashMap<String, String>()
            pram.put("user_name", etusername.trim())
            pram.put(
                "device_token",
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
            )
            getDataManager().checkUserNameAvailability(pram)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            val json: JSONObject? = response?.getJSONObject("data")
                            if (json!!.get("is_available").equals("1")) {
                                binding.tvUsernameStatus.visibility = View.VISIBLE
                                binding.tvUsernameStatus.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.colorGreen
                                    )
                                )
                                binding.tvUsernameStatus.text = json.getString("message")
                                binding.tvUsernameStatus.visibility = View.VISIBLE

                                if (et_username.text.toString().trim().equals(etusername)) {
                                    isUsername = true
                                }
                            }

                            if (json!!.get("is_available").equals("0")) {
                                binding.tvUsernameStatus.visibility = View.VISIBLE
                                binding.tvUsernameStatus.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.colorOrange
                                    )
                                )
                                binding.tvUsernameStatus.text = json.getString("message")
                                isUsername = false
                            }
                        } catch (exce: Exception) {

                            //Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                        }
                    }

                    override fun onError(anError: ANError?) {
                        iv_next_btn.isEnabled = true
                        Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                        Log.e("Error", "" + anError?.localizedMessage)
                        try {
                            Constant.errorHandle(anError!!, activity)
                        } catch (e: java.lang.Exception) {
                        }
                    }
                })
        }
    }

    override fun genderOnClick(index: Int, value: String) {
        binding.txtGender.text = value;
        hideNavigationBar()
    }

    override fun HeightValueOnClick(index: Int, value: String) {
        binding.txtHeight.text = "" + index + " cm"
        setHeight = index
        SelectedValue = index
        hideNavigationBar()
    }

    override fun WeightValueOnClick(index: Int) {
        if (unitStr.equals("Metric")) {
            binding.txtWeight.text = "" + index + ".00" + " kgs"
        } else {
            binding.txtWeight.text = "" + index + " lbs"
        }
        selectedIndexOfWeight = "" + index
        hideNavigationBar()
    }

    override fun textOnClick(type: String) {

        if (type.equals("Camera")) {
            dispatchTakePictureIntent()
        } else if (type.equals("Gallery")) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                    callIntent(AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL)
                } else {
                    callIntent(AppConstants.RESULT_LOAD)
                }
            } else {
                callIntent(AppConstants.RESULT_LOAD)
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //for  select image
    fun callIntent(caseId: Int) {

        when (caseId) {
            AppConstants.INTENT_CAMERA -> try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, AppConstants.REQUEST_CAMERA)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            AppConstants.RESULT_LOAD -> {
                val photoPickerIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photoPickerIntent, AppConstants.RESULT_LOAD)
            }

            AppConstants.REQUEST_CAMERA -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    AppConstants.MY_PERMISSIONS_REQUEST_CAMERA
                )
            }

            AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL
                )
            }
        }
    }

    // New Code for pic image from camera
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    mContext,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, AppConstants.REQUEST_CAMERA)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != 0) {
            when (requestCode) {
                AppConstants.RESULT_LOAD -> {
                    val temPhoto = data?.data
                    // New Code
                    if (temPhoto != null) {

                        try {
                            /* val bitmap = MediaStore.Images.Media.getBitmap(
                                 mContext.contentResolver,
                                 temPhoto
                             )
                             userImageFile = Constant.savebitmap(
                                 mContext,
                                 bitmap,
                                 UUID.randomUUID().toString() + ".jpg"
                             )
                             binding.profileImageView.setImageBitmap(bitmap)*/
                            var time = System.currentTimeMillis();
                            var str = time.toString()
                            var destinatiomPath = str + "dovies.jpg"
                            val options = UCrop.Options()
                            options.setHideBottomControls(true)
                            UCrop.of(temPhoto, Uri.fromFile(File(getCacheDir(), destinatiomPath)))
                                .withAspectRatio(1f, 1f)
                                .withOptions(options)
                                .start(mContext, this)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        // Calling Image Cropper
                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                UCrop.REQUEST_CROP -> {
                    if (data != null) {
                        handleCropResult(data)
                    }
                }

                AppConstants.REQUEST_CAMERA -> {
                    // New Code
                    val photoURI = Uri.fromFile(File(mCurrentPhotoPath))
                    if (photoURI != null) {

                        try {
                            /* var bm = Constant.getImageResized(mContext, photoURI) ///Image resizer
                             val rotation = ImageRotator.getRotation(mContext, photoURI, true)
                             bm = ImageRotator.rotate(bm, rotation)
                             val profileImagefile =
                                 File(
                                     mContext.externalCacheDir,
                                     UUID.randomUUID().toString() + ".jpg"
                                 )
                             tmpUri = FileProvider.getUriForFile(
                                 mContext,
                                 mContext.applicationContext.packageName + ".fileprovider",
                                 profileImagefile
                             )
                             userImageFile = Constant.savebitmap(
                                 mContext,
                                 bm,
                                 StringBuilder().append(UUID.randomUUID()).append(".jpg").toString()
                             )
                             binding.profileImageView.setImageBitmap(bm)*/
                            var time = System.currentTimeMillis();
                            var str = time.toString()
                            var destinatiomPath = str + "dovies.jpg"
                            val options = UCrop.Options()
                            options.setHideBottomControls(true)
                            UCrop.of(photoURI, Uri.fromFile(File(getCacheDir(), destinatiomPath)))
                                .withAspectRatio(1f, 1f)
                                .withOptions(options)
                                .start(mContext, this)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
        hideNavigationBar()
    }


    private fun handleCropResult(@NonNull result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            Picasso.with(getActivity()).load(resultUri).into(binding.profileImageView)
            userImageFile = File(resultUri.path)
        } else {
            Toast.makeText(getActivity(), "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT)
                .show()
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////

    override fun onResume() {
        super.onResume()
    }


    override fun timeOnClick(index: Int, value: String, index1: Int, value1: String) {
        val valueInFeet = value.split(" '")
        val valueInIntch = value1.split(" ''")

        binding.txtHeight.text = value + " " + value1
        withoutComa = valueInFeet[0] + " " + valueInIntch[0]
        hideNavigationBar()
    }

    public interface FinishEditListener {
        fun onEditFinished();
    }

}

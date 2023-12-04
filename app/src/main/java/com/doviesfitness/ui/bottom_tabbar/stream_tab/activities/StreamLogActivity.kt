package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.text.InputType
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.NonNull
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityWorkoutLogBinding
import com.doviesfitness.setting.CameraViewActivity
import com.doviesfitness.ui.authentication.signup.dialog.CreateLogWeightDialog
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogUpdatedModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamPlayedHistoryModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ProgressPicAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.CancelDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.CaloriModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ProgressPicsModel
import com.doviesfitness.utils.*
import com.facebook.FacebookSdk
import com.yalantis.ucrop.UCrop
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class StreamLogActivity : BaseActivity(), ProgressPicAdapter.selectImage, View.OnClickListener,
    WeightDialog.HeightWeightCallBack, CreateLogWeightDialog.WeightCallBack,
    ImagePickerFragmentDialog.ImagePickerFragmentCallBack
    , WorkoutLevelDialog.HeightWeightCallBack, SaveEditWorkoutDialog.CommentCallBack ,
    FinishActivityDialog.IsDelete, FinishActivityDialog.IsYesClick,CancelDialog.OnTextClickListener{

    lateinit var binding: ActivityWorkoutLogBinding
    var feedbackStatus = "great"
    lateinit var adapter: ProgressPicAdapter
    lateinit var picList: ArrayList<ProgressPicsModel>
    lateinit var Alist: ArrayList<File>
    var duration = ""
    var streamImage = ""
    var weight = ""
    var LogID = ""
    private var mLastClickTime: Long = 0
    var unitStr = ""
    var from = ""
    var videoId = ""
    var isLast = ""
    var streamWorkoutName = ""
    var workoutId = ""
    var historyId = ""
    var caloriString = ""
    var deletedatachId = ""
    lateinit var streamLog: StreamLogModel.Data
    lateinit var streamHistory: StreamPlayedHistoryModel.Data
    var caloriList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout_log)
        initialisation()
    }

    fun initialisation() {

        val windowBackground = window.decorView.background
/*
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)
*/
        binding.transparentBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(10f)
            .setHasFixedTransformationMatrix(true)

        binding.addWeightLayout.setOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)
        binding.containerId.setOnClickListener(this)
        binding.cancle.setOnClickListener(this)
        binding.progressLayout.setOnClickListener(this)
        binding.addCaloriLayout.setOnClickListener(this)
        if (CommanUtils.isNetworkAvailable(this)!!) {
            getCaloriList()
        }
        val horizontalLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            getActivity(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.progressRv.layoutManager = horizontalLayoutManager
        picList = ArrayList()
        adapter = ProgressPicAdapter(getActivity(), this, picList)
        binding.progressRv.adapter = adapter
        binding.title.text = "WORKOUT LOG"

        if (intent.hasExtra("workoutId")!!) {
            workoutId = intent.getStringExtra("workoutId")!!
        }
        if (intent.hasExtra("from")!!) {
            from = intent.getStringExtra("from")!!
        }
        if (intent.hasExtra("videoId")!!) {
            videoId = intent.getStringExtra("videoId")!!
        }
        if (intent.hasExtra("isLast")!!) {
            isLast = intent.getStringExtra("isLast")!!
        }

        if (intent.hasExtra("historyId")!!) {
            historyId = intent.getStringExtra("historyId")!!
        }

        if (intent.hasExtra("name")!!) {
            streamWorkoutName = intent.getStringExtra("name")!!
            binding.workoutName.text = streamWorkoutName

        }
        if (intent.hasExtra("duration")!!) {
            duration = intent.getStringExtra("duration")!!
            setDuration()
        }

        if (intent.hasExtra("streamImage")!!) {
            streamImage = intent.getStringExtra("streamImage")!!
            if (streamImage.isNotEmpty())
                Glide.with(getActivity()).load(streamImage).into(binding.workoutImg)

        }

        if (from.equals("edit")) {
            if (intent.hasExtra("streamLog")!!) {
                streamLog = intent.getSerializableExtra("streamLog")!! as StreamLogModel.Data

                binding.duration.text = "Duration: " + streamLog.stream_workout_time
                workoutId = streamLog.stream_workout_id
                videoId = streamLog.video_id
                binding.workoutName.text = streamLog.video_title
                Glide.with(getActivity()).load(streamLog.video_image)
                    .into(binding.workoutImg)
                binding.currentWeight.setText(streamLog.customer_weight)
                weight = streamLog.customer_weight.split(" ").get(0)
                duration = ""
                feedbackStatus = streamLog.feedback_status.toLowerCase()
                if (feedbackStatus.equals("great", true))
                    binding.great.isChecked = true
                else if (feedbackStatus.equals("good", true))
                    binding.good.isChecked = true
                else if (feedbackStatus.equals("reasonable", true))
                    binding.reasonable.isChecked = true
                else if (feedbackStatus.equals("bad", true))
                    binding.bad.isChecked = true
                binding.addNote.setText(streamLog.note)
                if (streamLog.customer_calorie.isNotEmpty()) {
                    binding.caloriBurn.setText(streamLog.customer_calorie)
                    caloriString = streamLog.customer_calorie
                }
                var list = ArrayList<LinkedHashMap<String, String>>()

                try {
                    list = streamLog.workout_log_images as ArrayList<LinkedHashMap<String, String>>
                    if (list.size > 0) {
                        for (i in 0..list.size - 1) {
                            var str = list.get(i).get("log_image")
                            var atachId = list.get(i).get("attachment_id")
                            picList.add(
                                ProgressPicsModel(
                                    File(""),
                                    "",
                                    str ?: "",
                                    "",
                                    from,
                                    "" + atachId
                                )
                            )
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                LogID = streamLog.log_id
            }

        }
        else if (from.equals("history")){
            if (intent.hasExtra("streamHistory")){
                streamHistory = intent.getSerializableExtra("streamHistory") as StreamPlayedHistoryModel.Data
                duration = streamHistory.play_time
                setDuration()
               // binding.duration.text = streamHistory.play_time
                binding.workoutName.text = streamHistory.video_title
                if ( streamHistory.video_image.isNotEmpty())
                    Glide.with(getActivity()).load( streamHistory.video_image).into(binding.workoutImg)
                videoId= streamHistory.video_id
                workoutId=   streamHistory.stream_workout_id
                historyId=   streamHistory.history_id

            }
        }
        unitStr = getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
        binding.addNote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.addNote.setRawInputType(InputType.TYPE_CLASS_TEXT);

        binding.addNote.setOnTouchListener({ v, event ->
            if (binding.addNote.hasFocus()) {
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
                when (event.getAction() and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        binding.scrollView.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                }
            }
            false
        })

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.great) {
                binding.great.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorWhite
                    )
                )
                binding.good.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.reasonable.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.bad.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray11))
                feedbackStatus = "great"
            }
            if (checkedId == R.id.good) {
                feedbackStatus = "good"
                binding.good.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite))
                binding.great.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.reasonable.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.bad.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray11))
            }
            if (checkedId == R.id.reasonable) {
                feedbackStatus = "reasonable"
                binding.reasonable.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorWhite
                    )
                )
                binding.great.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.good.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.bad.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray11))
            }
            if (checkedId == R.id.bad) {
                feedbackStatus = "bad"
                binding.bad.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite))
                binding.great.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.good.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
                binding.reasonable.setTextColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorGray11
                    )
                )
            }
        })
    }

    fun setDuration(){
        if (duration.isNotEmpty()) {
            try {
                var tokens = duration.split(":")
                if (tokens[0].equals("00")) {
                    if (tokens[1].equals("00")) {
                        if (tokens[2].equals("00")) {
                            binding.duration.text = "Duration: " + tokens[2] + " secs"
                        } else {
                            binding.duration.text = "Duration: " + tokens[2] + " secs"
                        }
                    } else {
                        binding.duration.text =
                            "Duration: " + tokens[1] + ":" + tokens[2] + " mins"
                    }
                } else {
                    if (tokens != null) {
                        if (tokens.size == 3)
                            binding.duration.text =
                                "Duration: " + tokens[0] + ":" + tokens[1] + ":" + tokens[2] + " Hour"
                        else if (tokens.size == 2)
                            binding.duration.text =
                                "Duration: " + tokens[0] + ":" + tokens[1] + " Hour"
                        else if (tokens.size == 1)
                            binding.duration.text = "Duration: " + tokens[0] + " Hour"
                        else binding.duration.text = ""
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.add_weight_layout -> {
                if (!unitStr!!.equals("") && unitStr!!.equals("Imperial")) {
                    val openDialog = CreateLogWeightDialog.newInstance1(30, 500, 30, "lbs", this)
                        .show(supportFragmentManager)
                } else {
                    val openDialog = CreateLogWeightDialog.newInstance1(13, 227, 13, "kgs", this)
                        .show(supportFragmentManager)
                }
            }
            R.id.add_calori_layout -> {
                if (caloriList.size > 0)
                    WorkoutLevelDialog.newInstance1(caloriList, this, getActivity(), 0,"calorie")
                        .show(supportFragmentManager)

            }
            R.id.containerId -> {
                hideKeyboard()
                hideNavigationBar()
            }
            R.id.submit_btn -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }

                if (CommanUtils.isNetworkAvailable(this)!!) {
                    if (from.isNotEmpty() && from.equals("edit"))
                        updateLog()
                    else
                        submitLog()

                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }
            R.id.cancle -> {
          //      binding.transparentLayout.visibility=View.VISIBLE
                binding.transparentBlurView.visibility=View.VISIBLE
                CancelDialog.newInstance("createlog", "",this).show(supportFragmentManager)
            }
            R.id.progress_layout -> {
            }
        }
    }

    override fun WeightValueOnClick(index: Int) {
        if (!unitStr!!.equals("") && unitStr!!.equals("Imperial")) {
            binding.currentWeight.text = "" + index + " lbs"
            val inkgsFronLbs = ConvertUnits.getLbsToKgs(index)
            weight = inkgsFronLbs.toString()

        } else {
            binding.currentWeight.text = "" + index + " Kgs"
            weight = index.toString()
        }

        hideNavigationBar()
    }
    override fun deleteImg(pos: Int) {
        if (picList.get(pos).atachId.isNotEmpty())
            deletedatachId = deletedatachId + picList.get(pos).atachId + ","
        picList.removeAt(pos)
        adapter.notifyDataSetChanged()
    }
    override fun selectImage() {
        val permissin = PermissionAll()
        if (permissin.RequestMultiplePermission(getActivity())) {
            SaveEditWorkoutDialog.newInstance("image", this).show(supportFragmentManager)
        } else {

        }
    }
    override fun textOnClick1(type: String) {
        //camera
        val intent = Intent(getActivity(), CameraViewActivity::class.java)
        startActivityForResult(intent, 1111)

    }
    override fun overwriteClick(type: String) {
//galery
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                callIntent(AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL)
            } else {
                callIntent(AppConstants.RESULT_LOAD)
            }
        } else {
            callIntent(AppConstants.RESULT_LOAD)
        }


    }
    override fun valueOnClick(index: Int, value: String) {
        binding.currentWeight.text = value
        val arr = value.split(" ")
        weight = arr[0]
        //  Toast.makeText(getActivity(),"value.."+value,Toast.LENGTH_SHORT).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            if (data?.hasExtra("img_file")!!) {
                try {
                    var ImageFile = data!!.getSerializableExtra("img_file") as File
                    val c = Calendar.getInstance()
                    System.out.println("Current time => " + c.time)
                    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = df.format(c.time)
                    picList.add(ProgressPicsModel(ImageFile!!, formattedDate, "", "", "", ""))
                    adapter.notifyDataSetChanged()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        } else if (resultCode != 0) {
            when (requestCode) {
                AppConstants.RESULT_LOAD -> {
                    val temPhoto = data?.data
                    // New Code
                    if (temPhoto != null) {
                        try {
                            var time = System.currentTimeMillis();
                            var str = time.toString()
                            var destinatiomPath = str + "dovies.jpg"
                            val options = UCrop.Options()
                            options.setHideBottomControls(true)
                            UCrop.of(
                                temPhoto,
                                Uri.fromFile(File(FacebookSdk.getCacheDir(), destinatiomPath))
                            )
                                .withAspectRatio(1f, 1f)
                                .withOptions(options)
                                .start(getActivity())

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        // Calling Image Cropper
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                UCrop.REQUEST_CROP -> {
                    if (data != null) {
                        handleCropResult(data)
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }
    private fun handleCropResult(@NonNull result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            var ImageFile = File(resultUri.path)
            val c = Calendar.getInstance()
            System.out.println("Current time => " + c.time)
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formattedDate = df.format(c.time)
            picList.add(ProgressPicsModel(ImageFile!!, formattedDate, "", "", "", ""))
            adapter.notifyDataSetChanged()

        } else {
            Toast.makeText(getActivity(), "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun submitLog() {
        binding.progressLayout.visibility = View.VISIBLE
        Alist = ArrayList()
        var ImgDate = ""
        for (i in 0..picList.size - 1) {
            Alist.add(picList.get(i).fileName)
            ImgDate = ImgDate + picList.get(i).picTime + ","
        }
        if (!ImgDate.isEmpty()) {
            ImgDate = ImgDate.substring(0, ImgDate.length - 1);
        }

        var Note = ""
        if (!binding.addNote.text.toString().isEmpty()) {
            Note = binding.addNote.text.toString().trim()
        }
        val param = HashMap<String, String>()
        param.put("note", Note)
        param.put("device_type", StringConstant.Android)
        //    param.put("device_token", "")
        param.put("weight", "" + weight)
        param.put("stream_workout_time", duration)
        param.put("feedback_status", feedbackStatus)
        param.put("stream_workout_id", workoutId)
        param.put("calorie", caloriString)
        param.put("video_id", videoId)
        param.put("history_id", ""+historyId)
        param.put("workout_image_date", ImgDate)
        // param.put("auth_customer_id", getDataManager().getUserInfo().customer_auth_token)
        //  param.put("device_id", "")
        val header = HashMap<String, String>()

        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        header.put(StringConstant.accept, "application/json")
        header.put(StringConstant.contentType, "application/json")
        getDataManager().submitStreamLog(param, header, Alist.toList())
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response", "response .." + response?.toString(4))

                    try {
                        binding.progressLayout.visibility = View.GONE

                        val json: JSONObject? = response?.getJSONObject("settings")
                        val success = json!!.getString("success")
                        if (isLast.equals("no")) {
                       //     binding.transparentLayout.visibility=View.VISIBLE
                            binding.transparentBlurView.visibility=View.VISIBLE

                            val dialog = FinishActivityDialog.newInstance(
                                "Yes",
                                "No",
                                "Do you want to play the next workout?"
                            )
//                            dialog.setListener(this@StreamLogActivity)
                            dialog.setListener2(this@StreamLogActivity)
                            dialog.show(supportFragmentManager)
                        }
                        else{
                            setResult(Activity.RESULT_OK,Intent().putExtra("createlog","finish").putExtra("isLast",isLast))
                            finish()
//                            val intent = Intent(this@StreamLogActivity, StreamLogHistoryActivity::class.java)
//                            startActivity(intent)
//                            finish()
                        }

                    } catch (exce: Exception) {
                        binding.progressLayout.visibility = View.GONE
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    binding.progressLayout.visibility = View.GONE
                    Log.e("Error", "" + anError?.localizedMessage)
                    Constant.errorHandle(anError!!, this@StreamLogActivity)
                }
            })
    }
    private fun updateLog() {
        binding.progressLayout.visibility = View.VISIBLE
        Alist = ArrayList()
        var ImgDate = ""
        for (i in 0..picList.size - 1) {
            if (picList.get(i).imageUrl.isEmpty() && !picList.get(i).from.equals("edit")) {
                Alist.add(picList.get(i).fileName)
                ImgDate = ImgDate + picList.get(i).picTime + ","
            }
        }
        if (!ImgDate.isEmpty()) {
            ImgDate = ImgDate.substring(0, ImgDate.length - 1);
        }
        if (!deletedatachId.isEmpty()) {
            deletedatachId = deletedatachId.substring(0, deletedatachId.length - 1);
        }

        var Note = ""
        if (!binding.addNote.text.toString().isEmpty()) {
            Note = binding.addNote.text.toString().trim()
        }
        val param = HashMap<String, String>()
        param.put("note", Note)
        param.put("weight", "" + weight)
        param.put("feedback_status", feedbackStatus)
        param.put("stream_workout_id", workoutId)
        param.put("calorie", caloriString)
        param.put("workout_image_date", ImgDate)
        param.put("log_id", "" + LogID)
        param.put("video_id", videoId)
        param.put("deleted_attachment_id", "" + deletedatachId)

        val header = HashMap<String, String>()

        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        header.put(StringConstant.accept, "application/json")
        header.put(StringConstant.contentType, "application/json")
        getDataManager().updateStreamLog(param, header, Alist.toList())
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response", "response .." + response?.toString(4))

                    try {
                        binding.progressLayout.visibility = View.GONE

                        val json: JSONObject? = response?.getJSONObject("settings")
                        val status = json!!.getString("success")
                        val message = json!!.getString("message")
                        Constant.showCustomToast(getActivity(), message)

                        if (status!!.equals("1")) {

                            val logModel = getDataManager().mGson?.fromJson(
                                response.toString(),
                                StreamLogUpdatedModel::class.java
                            )
                            setResult(Activity.RESULT_OK, Intent().putExtra("item", logModel?.data))
                            finish()
                        }

                    } catch (exce: Exception) {
                        binding.progressLayout.visibility = View.GONE
                        // Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    binding.progressLayout.visibility = View.GONE
                    Log.e("Error", "" + anError?.localizedMessage)
                    Constant.errorHandle(anError!!, this@StreamLogActivity)
                }
            })
    }
    private fun getCaloriList() {
        binding.progressLayout.visibility = View.VISIBLE

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        header.put(StringConstant.accept, "application/json")
        header.put(StringConstant.contentType, "application/json")
        getDataManager().getCaloriList(header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response", "response calori list..." + response?.toString(4))
                    try {
                        binding.progressLayout.visibility = View.GONE
                        val caloryModel = getDataManager().mGson?.fromJson(
                            response.toString(),
                            CaloriModel::class.java
                        )
                        caloriList.clear()
                        caloriList.addAll(caloryModel?.data?.calorie_list!!)

                    } catch (exce: Exception) {
                        binding.progressLayout.visibility = View.GONE
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    binding.progressLayout.visibility = View.GONE
                    Log.e("Error", "" + anError?.localizedMessage)
                    Constant.errorHandle(anError!!, this@StreamLogActivity)
                }
            })
    }
    override fun textOnClick(type: String) {

        if (type.equals("Camera")) {
            val intent = Intent(getActivity(), CameraViewActivity::class.java)
            startActivityForResult(intent, 1111)
        } else if (type.equals("Gallery")) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                    callIntent(AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL)
                } else {
                    callIntent(AppConstants.RESULT_LOAD)
                }
            } else {
                callIntent(AppConstants.RESULT_LOAD)
            }
        }
    }
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
    override fun levelOnClick(index: Int, str: String) {
        binding.caloriBurn.text = str
        caloriString = str
    }
    override fun isDelete() {
//yes
        setResult(Activity.RESULT_OK,Intent().putExtra("from","resume"))
        finish()
    }
    override fun isYesClick() {
        //no
        setResult(Activity.RESULT_OK,Intent().putExtra("createlog","finish").putExtra("isLast",isLast))
        finish()

    }

    override fun onClickView() {
       binding.transparentBlurView.visibility=View.GONE
    }

    override fun onYesClick() {
        setResult(Activity.RESULT_OK,Intent().putExtra("createlog","finish").putExtra("isLast",isLast))
        finish()
    }

    override fun hideTransparentView() {
        binding.transparentBlurView.visibility=View.GONE
    }

}
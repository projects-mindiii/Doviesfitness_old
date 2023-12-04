package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityStreamWorkoutCompleteBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.databinding.ActivityWorkoutCompleteBinding
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject
import java.util.ArrayList

class StreamCompleteActivity : BaseActivity(), View.OnClickListener,FinishActivityDialog.IsDelete,FinishActivityDialog.IsYesClick {
    lateinit var binding: ActivityStreamWorkoutCompleteBinding
    var workout_id = ""
    lateinit var exerciseList: ArrayList<WorkoutExercise>
    var workout_name = ""
    var duration = ""
    var videoId = ""
    var workoutId = ""
    var streamImage = ""
    var isLast = ""
    var from = ""
    var historyId = ""
    var streamWorkoutName = ""
    var orientation: Int = -10

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        hideNavStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_workout_complete)
        initialisation()
    }

    fun initialisation() {
        val windowBackground = window.decorView.background
        binding.transparentBlurView.setupWith(binding.containerId)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(RenderScriptBlur(this))
                .setBlurRadius(10f)
                .setHasFixedTransformationMatrix(true)

        if (intent.hasExtra("workout_id")!!) {
            workoutId = intent.getStringExtra("workout_id")!!
        }
        if (intent.hasExtra("name")!!) {
            streamWorkoutName = intent.getStringExtra("name")!!
        }
        if (intent.hasExtra("duration")!!) {
            duration = intent.getStringExtra("duration")!!
        }
        if (intent.hasExtra("isLast")!!) {
            isLast = intent.getStringExtra("isLast")!!
        }
        if (intent.hasExtra("historyId")!!) {
            historyId = intent.getStringExtra("historyId")!!
        }
        if (intent.hasExtra("video_id")!!) {
            videoId = intent.getStringExtra("video_id")!!
        }
        if (intent.hasExtra("streamImage")!!) {
            streamImage = intent.getStringExtra("streamImage")!!
        }
        if (intent.hasExtra("from")!!) {
            from = intent.getStringExtra("from")!!
        }
        Log.d("player state", "player state...api...save history"+videoId+"..wid"+workoutId+"..historyid.."+historyId+"..duration.."+duration)
        createPlayedHistory()
        binding.noBtn.setOnClickListener(this)
        binding.yesBtn.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        if (from.isNotEmpty() && from.equals("end",true)){
            binding.ivBack.visibility=View.GONE
        }
    }
    fun hideNavStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
    override fun onResume() {
        super.onResume()
        hideNavStatusBar()
    }
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent().putExtra("from", "back").putExtra("isLast", isLast))
        super.onBackPressed()
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.no_btn -> {
                if (isLast.equals("")) {
                    finish()

                }else if (isLast.equals("no")) {
                    binding.transparentLayout.visibility=View.VISIBLE
                    binding.transparentBlurView.visibility=View.VISIBLE
                    val dialog = FinishActivityDialog.newInstance(
                            "Yes",
                            "No",
                            "Do you want to play the next workout?"
                    )
                    dialog.setListener(this@StreamCompleteActivity)
                    dialog.setListener2(this@StreamCompleteActivity)
                    dialog.show(supportFragmentManager)
                }
                else{
                    setResult(Activity.RESULT_OK,Intent()
                            .putExtra("from","resume")
                            .putExtra("isLast",isLast))
                    finish()
                }
            }
            R.id.yes_btn -> {
                startActivityForResult(
                        Intent(getActivity(), StreamLogActivity::class.java)
                                .putExtra("workoutId", workoutId)
                                .putExtra("duration", duration)
                                .putExtra("name", streamWorkoutName)
                                .putExtra("videoId", videoId)
                                .putExtra("isLast", isLast)
                                .putExtra("historyId", historyId)
                                .putExtra("streamImage", streamImage)
                                .putExtra("from", "create"),50)
//                finish()
            }
        }
    }
    private fun createPlayedHistory() {
        val param = java.util.HashMap<String, String>()
        param.put("video_id", videoId)
        param.put("stream_workout_id", workoutId)
        param.put("play_time", duration)
        param.put("history_id", historyId)
        val header = java.util.HashMap<String, String>()
        header.put(StringConstant.AuthToken1, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().createPlayedHistory(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("response", "response .." + response?.toString(4))
                        try {
                            val json: JSONObject? = response?.getJSONObject("settings")
                            val success = json!!.getString("success")
                            if (success.equals("1"))
                            {
                                if (response.has("data")) {
                                    val data: JSONObject? = response?.getJSONObject("data")
                                    if (data?.has("history_id")!!) {
                                        val historyId = data?.getString("history_id")
                                    }
                                }
                            }
                        }
                        catch (exce: java.lang.Exception) {
                        }
                    }
                    override fun onError(anError: ANError?) {
                        Log.e("Error", "" + anError?.localizedMessage)
                    }
                })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50 && resultCode== Activity.RESULT_OK) {
            if (data!=null&&data.hasExtra("from"))
            {
                setResult(Activity.RESULT_OK,Intent().putExtra("from","create log").putExtra("isLast",isLast))
                finish()
            }
            else if (data!=null&&data.hasExtra("createlog"))
            {
                setResult(Activity.RESULT_OK,Intent().putExtra("createlog","finish").putExtra("isLast",isLast))
                finish()
            }
            else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
    override fun isDelete() {
//yes
        setResult(Activity.RESULT_OK,Intent().putExtra("from","create log").putExtra("isLast",isLast))
        finish()
    }

    override fun isYesClick() {
        //no
        setResult(Activity.RESULT_OK,Intent().putExtra("from","resume").putExtra("isLast",isLast))
        finish()
    }
    override fun hideTransparentView() {
        binding.transparentLayout.visibility=View.GONE
        binding.transparentBlurView.visibility=View.GONE
    }
}
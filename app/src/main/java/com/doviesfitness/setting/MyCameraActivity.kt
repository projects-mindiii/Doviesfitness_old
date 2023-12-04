package com.doviesfitness.setting

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.util.DisplayMetrics
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ProgressPicsModel


class MyCameraActivity :BaseActivity(), CameraPreview.GetBitmap {
    override fun getImageBitmap(bitmap: Bitmap?) {
        Log.d(LOG_TAG,"bitmap path...."+bitmap)
        saveImage(bitmap!!)
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/"
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "$timeStamp.jpg"
        val file = File(path, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intent = Intent()
        intent.putExtra("img_file",file)
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    companion object{
        public  var LOG_TAG = "NiceCameraExample"
    }


    /**
     * 'camera' is the object that references the hardware device
     * installed on your Android phone.
     */
    private var camera: Camera? = null

    /**
     * Phone can have multiple cameras, so 'cameraID' is a
     * useful variable to store which one of the camera is active.
     * It starts with value -1
     */
    private var cameraID: Int = 0

    /**
     * 'camPreview' is the object that prints the data
     * coming from the active camera on the GUI, that is... frames!
     * It's an instance of the 'CameraPreview' class, more information
     * india [CameraPreview]
     */
    private var camPreview: CameraPreview? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_camera)

        if (setCameraInstance() == true) {

           // camera!!.parameters.setPictureSize(400,400)
            // everything's OK, we can go further and create the preview object
            this.camPreview = CameraPreview(this, this.camera, this.cameraID,this)
        } else {
            // error here! we can print something or just cry.
            this.finish()
        }

        // if the preview is set, we add it to the contents of our activity.
        val preview = findViewById<View>(R.id.preview_layout) as RelativeLayout
        preview.addView(this.camPreview)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
       // val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        // also we set some layout properties
        val previewLayout = camPreview!!.getLayoutParams() as RelativeLayout.LayoutParams

        /*previewLayout.width = FrameLayout.LayoutParams.MATCH_PARENT
        previewLayout.height = FrameLayout.LayoutParams.MATCH_PARENT*/
        previewLayout.width = width
        previewLayout.height =width
        this.camPreview!!.setLayoutParams(previewLayout)

        // on the main activity there's also a "capture" button, we set its behavior
        // when it gets clicked here
        val captureButton = findViewById<View>(R.id.button_capture) as ImageView
        val cancel = findViewById<View>(R.id.cancel) as TextView
        captureButton.setOnClickListener {
            camera!!.takePicture(null, null, camPreview) // request a picture
        }
        cancel.setOnClickListener {
           finish()
        }
        fixElementsPosition(resources.configuration.orientation)
    }

    override fun onResume() {
        super.onResume()
        if (setCameraInstance() == true) {
            // TODO: camPreview.refresh...
        } else {
            Log.e(LOG_TAG, "onResume(): can't reconnect the camera")
            this.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        releaseCameraInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCameraInstance()
    }

    /**
     * This method is added india order to detect changes india orientation.
     * If we want we can react on this and change the position of
     * some GUI elements (see [.fixElementsPosition]
     * method).
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        fixElementsPosition(newConfig.orientation)
    }


    private fun setCameraInstance(): Boolean {
        if (this.camera != null) {
            // do the job only if the camera is not already set
            Log.i(LOG_TAG, "setCameraInstance(): camera is already set, nothing to do")
            return true
        }

        // warning here! starting from API 9, we can retrieve one from the multiple
        // hardware cameras (ex. front/back)
        if (Build.VERSION.SDK_INT >= 9) {

            if (this.cameraID < 0) {
                // at this point, it's the first time we request for a camera
                val camInfo = Camera.CameraInfo()
                for (i in 0 until Camera.getNumberOfCameras()) {
                    Camera.getCameraInfo(i, camInfo)

                    if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        // india this example we'll request specifically the back camera
                        try {
                            this.camera = Camera.open(i)
                            this.cameraID = i // assign to cameraID this camera's ID (O RLY?)
                            return true
                        } catch (e: RuntimeException) {
                            // something bad happened! this camera could be locked by other apps
                            Log.e(
                                LOG_TAG,
                                "setCameraInstance(): trying to open camera #$i but it's locked", e
                            )
                        }

                    }
                }
            }
            else {
                // at this point, a previous camera was set, we try to re-instantiate it
                try {
                    this.camera = Camera.open(this.cameraID)
                } catch (e: RuntimeException) {
                    Log.e(
                        LOG_TAG,
                        "setCameraInstance(): trying to re-open camera #" + this.cameraID + " but it's locked",
                        e
                    )
                }

            }
        }

        // we could reach this point india two cases:
        // - the API is lower than 9
        // - previous code block failed
        // hence, we try the classic method, that doesn't ask for a particular camera
        if (this.camera == null) {
            try {
                this.camera = Camera.open()
                this.cameraID = 0
            } catch (e: RuntimeException) {
                // this is REALLY bad, the camera is definitely locked by the system.
                Log.e(
                    LOG_TAG,
                    "setCameraInstance(): trying to open default camera but it's locked. " + "The camera is not available for this app at the moment.",
                    e
                )
                return false
            }

        }

        // here, the open() went good and the camera is available
        Log.i(
            LOG_TAG,
            "setCameraInstance(): successfully set camera #" + this.cameraID
        )
        return true
    }


    private fun releaseCameraInstance() {
        if (this.camera != null) {
            try {
                this.camera!!.stopPreview()
            } catch (e: Exception) {
                Log.i(
                    LOG_TAG,
                    "releaseCameraInstance(): tried to stop a non-existent preview, this is not an error"
                )
            }

            this.camera!!.setPreviewCallback(null)
            this.camera!!.release()
            this.camera = null
            this.cameraID = -1
            Log.i(LOG_TAG, "releaseCameraInstance(): camera has been released.")
        }
    }

    private fun fixElementsPosition(orientation: Int) {
        val captureButton = findViewById<View>(R.id.button_capture) as ImageView
        val layout = captureButton.layoutParams as FrameLayout.LayoutParams

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> layout.gravity = Gravity.RIGHT or Gravity.CENTER
            Configuration.ORIENTATION_PORTRAIT -> layout.gravity = Gravity.BOTTOM or Gravity.CENTER
        }
    }

    fun getCamera(): Camera {
        return this.camera!!
    }

    fun getCameraID(): Int {
        return this.cameraID
    }
}
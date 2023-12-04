package com.doviesfitness.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.FileCallback
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.android.synthetic.main.activity_camera_view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraViewActivity :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_camera_view)
        camera_view.setLifecycleOwner(this)
        camera_view.addCameraListener(object:CameraListener(){
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
/*
                var path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                        packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/"
*/
             //
                var path = Environment.getExternalStorageDirectory().absolutePath
                var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                var fname = "$timeStamp.jpg"
                var file = File(path, fname)
                    result.toFile(file,object:FileCallback{
                        override fun onFileReady(file: File?) {
                            val intent = Intent()
                            intent.putExtra("img_file",file)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }

                    })
            }
        })
        rotate_camera.setOnClickListener{
            if (camera_view.facing==Facing.FRONT)
            camera_view.facing=Facing.BACK
            else
                camera_view.facing=Facing.FRONT
        }
        button_capture.setOnClickListener{
            camera_view.takePictureSnapshot()
        }
        cancel.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()

    }
}
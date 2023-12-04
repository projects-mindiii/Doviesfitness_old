package com.doviesfitness.utils

import android.hardware.Camera
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.ShutterCallback
import android.view.SurfaceView
import android.widget.TextView
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class VideoServer:AppCompatActivity(), SurfaceHolder.Callback{
    var testView: TextView? = null

    var camera: Camera? = null
    var surfaceHolder: SurfaceHolder? = null
    var rawCallback: PictureCallback? = null
    var shutterCallback: ShutterCallback? = null
    var jpegCallback: PictureCallback? = null
    private val tag = "VideoServer"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // biniding=DataBindingUtil.setContentView(this,R.layout.activity_my_camera);

       /* biniding.start.setOnClickListener(
        {
                start_camera()
        })
        biniding.stop.setOnClickListener(
        {
                stop_camera()
        })
        biniding.capture.setOnClickListener({
                captureImage()
        })

       // surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceHolder = biniding.surfaceView1.getHolder();*/
        surfaceHolder!!.addCallback(this);
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
/*
        rawCallback= PictureCallback(object : (data: ByteArray, camera: Camera) -> Unit {
            override fun invoke(data: ByteArray, camera: Camera) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }



        })
*/

        shutterCallback =  ShutterCallback(object: () -> Unit{
            override fun invoke() {

                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



      /*  rawCallback = PictureCallback() {
              onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };*/
/*        shutterCallback =  ShutterCallback() {
              onShutter() {
                Log.i("Log", "onShutter'd");
            }
        }*/

/*
        jpegCallback =  PictureCallback(object:(data: ByteArray, camera: Camera) -> Unit{
            override fun invoke(data: ByteArray, camera: Camera) {
                try {
                   var outStream =  FileOutputStream(String.format(
                        "/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.size);
                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                } catch (e: IOException) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");

            }

        })
*/



/*
        jpegCallback =  PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format(
                        "/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        }
*/


    }

    private fun captureImage() {
        // TODO Auto-generated method stub
        camera!!.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    private fun start_camera()
    {
        try{
            camera = Camera.open();
        }catch(e:RuntimeException ){
            Log.e(tag, "init_camera: " + e);
            return;
        }
       var param = camera!!.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        param.setPreviewSize(176, 144);
        camera!!.setParameters(param);
        try {
            camera!!.setPreviewDisplay(surfaceHolder);
            camera!!.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (e:Exception ) {
            Log.e(tag, "init_camera: " + e);
            return;
        }
    }

    private fun stop_camera()
    {
        camera!!.stopPreview();
        camera!!.release();
    }

    public override fun surfaceChanged(arg0:SurfaceHolder, arg1: Int, arg2:Int, arg3: Int ) {

    }

    public override fun surfaceCreated(holder:SurfaceHolder ) {
        // TODO Auto-generated method stub
    }

    public override fun surfaceDestroyed(holder:SurfaceHolder ) {
        // TODO Auto-generated method stub
    }
}
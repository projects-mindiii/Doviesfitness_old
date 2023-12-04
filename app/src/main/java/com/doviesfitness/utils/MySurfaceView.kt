package com.doviesfitness.utils

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

class MySurfaceView : SurfaceView, Callback, Camera.PreviewCallback {

    private var mHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var isPreviewRunning = false
    private val rgbbuffer = ByteArray(256 * 256)
    private val rgbints = IntArray(256 * 256)

    protected val rectanglePaint = Paint()

    constructor( context:Context, attrs:AttributeSet) : super(context,attrs) {
        rectanglePaint.setARGB(100, 200, 0, 0);
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.strokeWidth=2f

        mHolder = getHolder();
        mHolder!!.addCallback(this);
        mHolder!!.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(
            Rect(
                Math.random().toInt() * 100,
                Math.random().toInt() * 100, 200, 200
            ), rectanglePaint
        )
        Log.w(this.javaClass.name, "On Draw Called")
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        synchronized(this) {
            this.setWillNotDraw(false) // This allows us to make our own draw
            // calls to this canvas

            mCamera = Camera.open()

            val p = mCamera!!.parameters
            p.setPreviewSize(240, 160)
            mCamera!!.parameters = p


            //try { mCamera.setPreviewDisplay(holder); } catch (IOException e)
            //  { Log.e("Camera", "mCamera.setPreviewDisplay(holder);"); }

            mCamera!!.startPreview()
            mCamera!!.setPreviewCallback(this)

        }
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        synchronized(this) {
            try {
                if (mCamera != null) {
                    mCamera!!.stopPreview()
                    isPreviewRunning = false
                    mCamera!!.release()
                }
                else{

                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Camera", it) }
            }

        }
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        Log.d("Camera", "Got a camera frame")

        var c: Canvas? = null

        if (mHolder == null) {
            return
        }

        try {
            synchronized(mHolder!!) {
                c = mHolder!!.lockCanvas(null)

                // Do your drawing here
                // So this data value you're getting back is formatted india YUV format and you can't do much
                // with it until you convert it to rgb
                var bwCounter = 0
                var yuvsCounter = 0
                for (y in 0..159) {
                    System.arraycopy(data, yuvsCounter, rgbbuffer, bwCounter, 240)
                    yuvsCounter = yuvsCounter + 240
                    bwCounter = bwCounter + 256
                }

                for (i in 0 until rgbints.size) {
                    rgbints[i] = rgbbuffer[i] as Int
                }

                //decodeYUV(rgbbuffer, data, 100, 100);
                c!!.drawBitmap(rgbints, 0, 256, 0, 0, 256, 256, false, Paint())

                Log.d("SOMETHING", "Got Bitmap")

            }
        } finally {
            // do this india a finally so that if an exception is thrown
            // during the above, we don't leave the Surface india an
            // inconsistent state
            if (c != null) {
                mHolder!!.unlockCanvasAndPost(c)
            }
        }
    }
}
package com.doviesfitness.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class Constant {

    companion object {
        val TAG: String = Constant::class.java.name


        val CAMERA = 5
        val GALLERY = 6
        val COMMENT_COUNT_CONSTANT = 10
        val LIKE_COMMENT_COUNT_CONSTANT = 11
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 13

        val SECOND_ACTIVITY_REQUEST_CODE = 0

        val DEFAULT_MIN_WIDTH_QUALITY = 400
        val DEFAULT_MIN_HEIGHT_QUALITY = 400
        val CHORME_EXPANDED_CODE = 432
        val minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY
        val minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY


        val mOnAudioFocusChangeListener = object : AudioManager.OnAudioFocusChangeListener {
            override fun onAudioFocusChange(focusChange: Int) {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
                    Log.i(TAG, "AUDIO FOCUS GAIN")
                //Play or restart your sound
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
                    Log.e(TAG, "AUDIO FOCUS LOSS")
                //Loss of audio focus for a long time
                //Mostly stop playing the sound
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
                    Log.e(TAG, "AUDIO FOCUS LOSS TRANSIENT")
                //Loss of audio focus for a short time
                //Mostly pause playing the sound
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                    Log.e(TAG, "AUDIO FOCUS LOSS TRANSIENT CAN DUCK")
                //Loss of audio focus for a short time.
                //But one can duck .Mostly lower the volume of playing the sound
            }

        }

        fun deviceSize(context: Activity): Int {
            var space = context.resources.getDimensionPixelSize(R.dimen._10sdp)
            val metrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(metrics)

            if (metrics.heightPixels == 3040 || metrics.widthPixels == 1440 || metrics.heightPixels > 3040 || metrics.widthPixels > 1440) {
                //Toast.makeText(context,"greater than 1440",Toast.LENGTH_SHORT).show()
                space = context.resources.getDimensionPixelSize(R.dimen._14sdp)
            } else if (metrics.widthPixels == 720 && metrics.heightPixels > 1280) {
                space = context.resources.getDimensionPixelSize(R.dimen._8sdp)

                // Toast.makeText(context,"equal to 720",Toast.LENGTH_SHORT).show()

            } else if (metrics.widthPixels <= 720 && metrics.heightPixels <= 1280) {
                //  Toast.makeText(context,"less to 720",Toast.LENGTH_SHORT).show()
                space = context.resources.getDimensionPixelSize(R.dimen._6sdp)

            } else if (metrics.widthPixels == 1080 && metrics.heightPixels > 1280) {
                space = context.resources.getDimensionPixelSize(R.dimen._10sdp)

                //Toast.makeText(context,"equal to 1080",Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "nothing", Toast.LENGTH_SHORT).show()

            }
            return space


        }

        fun getStreamDuration(inputString: String): String {

            var str = ""
            var tokens = inputString.split(":")
            var seconds = Integer.parseInt(tokens[2])
            var minutes = Integer.parseInt(tokens[1])
            var hours = Integer.parseInt(tokens[0])

            if (hours > 0) {
                str = "" + hours + "h"
                if (minutes > 0) {
                    str = "" + hours + "h " + minutes + "m"
                }
            } else if (minutes > 0) {
                str = "" + minutes + "m"
                if (seconds > 0) {
                    str = "" + minutes + "m " + seconds + "s"
                }
            } else {
                str = "" + seconds + "s"
            }
            return str
        }


        fun requestAudioFocusForMyApp(context: Context): Boolean {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            // Request audio focus for playback
            val result = am.requestAudioFocus(
                mOnAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d(TAG, "Audio focus received")
                return true
            } else {
                Log.d(TAG, "Audio focus NOT received")
                return false
            }
        }

        fun requestAudioFocusClose(context: Context): Boolean {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            // Request audio focus for playback
            val result = am.requestAudioFocus(
                mOnAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN
            )
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d(TAG, "Audio focus received")
                return true
            } else {
                Log.d(TAG, "Audio focus NOT received")
                return false
            }
        }

        fun releaseAudioFocusForMyApp(context: Context) {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val result = am.abandonAudioFocus(mOnAudioFocusChangeListener)
        }

        fun showCustomToast(context: Context, msg: String) {
            Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show()
        }

        fun showCustomToastShort(context: Context, msg: String) {
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show()
        }


        /*.............................//Custom dialog for Delete....................................*/
        fun showInternetConnectionDialog(activity: Activity) {
            val dialog = Dialog(activity)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            dialog.setContentView(R.layout.no_internate_connection_view)
            val tvOk = dialog.findViewById<TextView>(R.id.tv_ok)
            val linearLayoutOk = dialog.findViewById<LinearLayout>(R.id.ll_ok)

            linearLayoutOk.setOnClickListener { v ->
                dialog.dismiss()
            }

            dialog.show()


        }


        fun getExerciseTime(inputString: String): Int {
            try {
                var tokens = inputString.split(":")
                var seconds = Integer.parseInt(tokens[2])
                var minutes = Integer.parseInt(tokens[1]) * 60
                var hours = Integer.parseInt(tokens[0]) * 3600
                var total = seconds + minutes + hours
                return total
            } catch (e: Exception) {
                e.printStackTrace()
            }

           return 0
        }

        fun getRestTimeToken(inputString: String): List<String> {

            var tokens = inputString.split(":")
            var seconds = Integer.parseInt(tokens[2])
            var minutes = Integer.parseInt(tokens[1]) * 60
            var hours = Integer.parseInt(tokens[0]) * 3600
            var total = seconds + minutes + hours
            return tokens
        }


        //*****************check for network connection******************//
        fun isNetworkAvailable(context: Context, coordinatorLayout: View): Boolean {

            val connectivityManager = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            // Check for network connections
            if (activeNetworkInfo != null) {
                // if connected with internet
                return true

            } else {

                val snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", { })
                snackbar.setActionTextColor(Color.RED)
                val sbView = snackbar.getView()
                val textView =
                    sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(Color.YELLOW)
                snackbar.show()
                return false
            }
            return false
        }


        //""""""""image Resize"""""""""/
        fun getImageResized(context: Context, selectedImage: Uri): Bitmap? {
            var bm: Bitmap?
            val sampleSizes = intArrayOf(5, 3, 2, 1)
            var i = 0
            do {
                bm = decodeBitmap(context, selectedImage, sampleSizes[i])
                i++
            } while (bm != null
                && (bm.width < minWidthQuality || bm.height < minHeightQuality)
                && i < sampleSizes.size
            )
            Log.i("FragmentFeed", "Final bitmap width = " + (bm?.width ?: "No final bitmap"))
            return bm
        }

        fun decodeBitmap(context: Context, theUri: Uri, sampleSize: Int): Bitmap? {
            var actuallyUsableBitmap: Bitmap? = null
            var fileDescriptor: AssetFileDescriptor? = null
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize

            try {
                fileDescriptor = context.contentResolver.openAssetFileDescriptor(theUri, "r")
                actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor!!.fileDescriptor,
                    null,
                    options
                )
                if (actuallyUsableBitmap != null) {
                    Log.i(
                        TAG, "Trying sample size " + options.inSampleSize + "\t\t"
                                + "Bitmap width: " + actuallyUsableBitmap.width
                                + "\theight: " + actuallyUsableBitmap.height
                    )
                }
                fileDescriptor.close()
            } catch (e: FileNotFoundException) {
                e.message?.let { Log.d("Image Rotator", it) }
            } catch (e: IOException) {
                e.message?.let { Log.d(TAG, it) }
            }

            return actuallyUsableBitmap
        }

        //**************hide keyboard********************//
        fun hideSoftKeyBoard(context: Context, view: View) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }


        fun errorHandle(error: ANError, activity: Activity?) {
            var errorMessage = "Unknown error"
            val json: JSONObject? = JSONObject(error.errorBody.toString())
            val json1: JSONObject? = json?.getJSONObject(StringConstant.settings)
            val status = json1?.get(StringConstant.success)
            val msg = json1?.getString(StringConstant.message)
            try {
                if (status == "300") {
                    if (activity != null) {
                        getDataManager().logout(activity)
                    }
                }

                Log.e("Error Status", "" + status)
                Log.e("Error Message", msg!!)

                if (status == "404") {
                    errorMessage = activity?.resources?.getString(R.string.resource_not_found)
                        ?: "Resource not found"
                } else if (status == "401") {
                    if (activity != null) {
                        getDataManager().logout(activity)
                    }
                    errorMessage = msg + " Please login again"
                } else if (status == "400") {
                    errorMessage = msg + " Check your inputs"
                } else if (status == "500") {
                    errorMessage = msg + " Something is getting wrong"
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }


    }
}
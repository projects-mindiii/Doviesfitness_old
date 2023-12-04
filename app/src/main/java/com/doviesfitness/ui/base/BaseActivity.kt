package com.doviesfitness.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.AppDataManager
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.authentication.signup.dialog.ImagePickerDialog
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.ProgressDialog
import com.doviesfitness.utils.ProgressVideoDialog
import com.google.firebase.iid.FirebaseInstanceId
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


open class BaseActivity : AppCompatActivity(), ImagePickerDialog.ImagePickerCallBack {

    private var FirebaseToken: String = ""
    lateinit var tmpUri: Uri
    val tag: String = BaseActivity::class.java.name
    private var progressDialog: ProgressDialog? = null
    private var progressVideoDialog: ProgressVideoDialog? = null

    private var activity: Activity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        progressVideoDialog = ProgressVideoDialog(this)
    }

    fun setLoading(isLoading: Boolean) {
        try {

            if (isLoading) progressDialog?.show() else progressDialog?.hide()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideNavigationBar() {
        activity.getWindow().getDecorView().setSystemUiVisibility(
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


    fun setVideoLoading(isLoading: Boolean) {
        if (isLoading) progressVideoDialog?.show() else progressVideoDialog?.dismiss()
    }

    fun getActivity(): Activity {
        return activity
    }

    fun getDataManager(): AppDataManager {
        return Doviesfitness.getDataManager()
    }
/**
 * done by santosh 26/june/2023
 *  getting devise current volume
 * */
    fun getDeviseVolume():Int{
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
         val currentVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume: Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
// Calculate the volume percentage
        val volumePercentage: Int = (currentVolume.toFloat() / maxVolume.toFloat() * 100).toInt()
// Determine if the device is muted
        val isMuted: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.isStreamMute(AudioManager.STREAM_RING)
        } else {
            TODO("VERSION.SDK_INT < M")
        }

// Log the volume status
        println("awaj --Current Volume: $currentVolume")
        println("awaj Max Volume: $maxVolume")
        println("awaj Volume Percentage: $volumePercentage%")
        println("awaj Is Muted: $isMuted")
        return currentVolume
    }

    fun getFirebaseTocan() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("OutloadApplicationClass", "getInstanceId failed", task.exception)
                        return@addOnCompleteListener
                    }

                    // Get new Instance ID token
                    FirebaseToken = task.result!!.token

                    val userInfoBean = Doviesfitness.getDataManager().getUserInfo()
                    userInfoBean.firebaseToken = FirebaseToken
                    getDataManager().setUserStringData(
                            AppPreferencesHelper.PREF_FIREBASE_TOKEN,
                            FirebaseToken
                    )

                    Log.d("token", FirebaseToken)
                }
    }


    /*
    replace fragment
    //1 @param - fragmentHolder : Fragment Name
    //2 @param- layoutId : Container Id(FrameLayout)

    */
    fun replaceFragment(
            fragmentHolder: Fragment,
            layoutId: Int?,
            addToBackStack: Boolean
    ) {
        try {
            val fragmentManager = supportFragmentManager
            fragmentManager.popBackStackImmediate(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            val fragmentName = fragmentHolder.javaClass.name
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(layoutId!!, fragmentHolder, fragmentName)

            if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName)

            fragmentTransaction.commit()
            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /*
   replace fragment
   //1 @param - fragmentHolder : Fragment Name
   //2 @param- layoutId : Container Id(FrameLayout)
   //3 @param - addToBackStack : true/false (add to back stack)
   */
    fun addFragment(
            fragment: Fragment,
            layoutId: Int,
            addToBackStack: Boolean
    ) {
        try {
            val fragmentName = fragment.javaClass.name

            val fragmentTransaction = supportFragmentManager.beginTransaction()

            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.enterTransition = null
            }
            fragmentTransaction.add(layoutId, fragment, fragmentName)
            if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName)
            fragmentTransaction.commit()
            //  }
            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addFragment1(
            fragment: Fragment,
            layoutId: Int,
            addToBackStack: Boolean
    ) {
        try {
            val fragmentName = fragment.javaClass.name

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.enterTransition = null
            }

            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment)
                fragmentTransaction.commit()
            } else {
                fragmentTransaction.add(layoutId, fragment, fragmentName)
                if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName)
                fragmentTransaction.commit()
            }
            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addFragment2(
            fragment: Fragment,
            layoutId: Int,
            addToBackStack: Boolean
    ): Boolean {
        var returnVal = false
        try {
            val fragmentName = fragment.javaClass.name

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.enterTransition = null
            }

            if (fragment.isAdded()) {
                //  supportFragmentManager.popBackStack()
                fragmentTransaction.remove(fragment)
                fragmentTransaction.commit()

                // fragmentTransaction.add(layoutId, fragment, fragmentName)
                //  fragmentTransaction.commit()
                returnVal = true
            } else {
                fragmentTransaction.add(layoutId, fragment, fragmentName)
                if (addToBackStack) fragmentTransaction.addToBackStack(fragmentName)
                fragmentTransaction.commit()
                returnVal = false
            }
            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return returnVal
    }

    fun removeFragment3(
            fragment: Fragment,
            layoutId: Int,
            addToBackStack: Boolean
    ) {
        var returnVal = false
        try {

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (fragment.isAdded()) {
                fragmentTransaction.remove(fragment)
                fragmentTransaction.commit()

            }

            hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }
// return  returnVal
    }


    fun hideKeyboard() {
        if (currentFocus == null) return
        val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }


     fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun getImagePickerDialog() {
        ImagePickerDialog.newInstance(this).show(supportFragmentManager)
        //  builder.show()
    }

    override fun textOnClick(type: String) {
        if (type.equals("Camera")) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //***** Ensure that there's a camera activity to handle the intent ****//
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                //** Create the File where the photo should go *****//
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.d(tag, ex.message!!)
                }
                if (photoFile != null) {
                    tmpUri = FileProvider.getUriForFile(
                            this, this.applicationContext.packageName + ".fileprovider",
                            getTemporalFile(this)
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpUri)
                    startActivityForResult(takePictureIntent, Constant.CAMERA)
                    //content@ //media/external/images/media/15
                }
            }
        } else if (type.equals("Gallery")) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, Constant.GALLERY)
        } else {

        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = externalCacheDir
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        return image
    }

    fun getTemporalFile(baseActivity: Context): File {
        val tempFile = File(baseActivity.getExternalCacheDir(), "tmp.jpg")
        return tempFile
    }

    fun sharePost(url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        sendIntent.type = "text/plain"
        //  sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        startActivity(Intent.createChooser(sendIntent, "choose one"))
    }
}
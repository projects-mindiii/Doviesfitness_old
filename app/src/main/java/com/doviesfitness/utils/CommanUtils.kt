package com.doviesfitness.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import com.doviesfitness.BuildConfig
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.ExerciseTransData
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SelectedExerciseAdapterSetAndReps
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SetsAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.new_player.adapter.NewPlayerExerciseSetAndRepsAdapter
import java.io.File
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class CommanUtils {

    companion object {


        fun removeDuplicates(input: String): String {
            val items = input.split(",").map { it.trim() }.toSet()
            return items.joinToString(",")
        }


        /**making duplicate exercise object*/
        fun addDuplicateExercise(data: ExerciseListingResponse.Data): ExerciseListingResponse.Data {
            // var data3=data.deepCopy()
            var exerciseReps = "Select"
            if (data.selected_exercise_reps_number != null)
                exerciseReps = data.selected_exercise_reps_number!!

            var exerciseWeight = "Select"
            if (data.selected_exercise_weight_number != null)
                exerciseWeight = data.selected_exercise_weight_number!!

            var data1 = ExerciseListingResponse.Data(
                data.exercise_level,
                data.exercise_amount,
                data.exercise_amount_display,
                data.exercise_body_parts,
                data.exercise_description,
                data.exercise_equipments,
                data.exercise_id,
                data.exercise_image,
                data.exercise_is_favourite,
                data.exercise_level,
                data.exercise_name,
                data.exercise_share_url,
                data.exercise_tags,
                data.exercise_video,
                data.is_liked,
                false,
                false,
                false,
                data.exercise_timer_time,
                data.exercise_reps_time,
                data.exercise_reps_number,
                "$exerciseReps",
                "$exerciseWeight",
                data.exercise_rest_time,
                false,
                false,
                false,
                0,
                false,
                data.leftAndRightOrSuperSetOrAddExercise,
                false
            )

            return data1.deepCopy()
        }

        fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

        /**making duplicate exercise object*/
        fun addDuplicateExercise1(data: ExerciseListingResponse.Data): ExerciseListingResponse.Data {
            //  var data2=data.deepCopy()
            var exerciseReps = "Select"
            if (data.selected_exercise_reps_number != null)
                exerciseReps = data.selected_exercise_reps_number!!

            var exerciseWeight = "Select"
            if (data.selected_exercise_weight_number != null)
                exerciseWeight = data.selected_exercise_weight_number!!

            var data1 = ExerciseListingResponse.Data(
                data.exercise_level,
                data.exercise_amount,
                data.exercise_amount_display,
                data.exercise_body_parts,
                data.exercise_description,
                data.exercise_equipments,
                data.exercise_id,
                data.exercise_image,
                data.exercise_is_favourite,
                data.exercise_level,
                data.exercise_name,
                data.exercise_share_url,
                data.exercise_tags,
                data.exercise_video,
                data.is_liked,
                false,
                false,
                false,
                data.exercise_timer_time,
                data.exercise_reps_time,
                data.exercise_reps_number,
                "$exerciseReps",
                "$exerciseWeight",
                data.exercise_rest_time,
                false,
                false,
                false,
                0,
                false,
                data.leftAndRightOrSuperSetOrAddExercise,
                false
            )

            return data1.deepCopy()
        }

        /**making duplicate exercise object for workout detail screen*/
        fun addDuplicateExercise2(data: ExerciseTransData): ExerciseListingResponse.Data {
            //  var data2=data.deepCopy()
            var exerciseReps = "Select"
            if (data.iReps != null)
                exerciseReps = data.iReps!!

            var exerciseWeight = "Select"
            if (data.iWeight != null)
                exerciseWeight = data.iWeight!!

            var data1 = ExerciseListingResponse.Data(
                exercise_level = data.workout_exercise_level,
                exercise_amount = "0",
                exercise_amount_display = ""/*data.exercise_amount_display*/,
                exercise_body_parts = data.workout_exercise_body_parts_id,
                exercise_description = data.workout_exercise_description,
                exercise_equipments = data.workout_exercise_equipments_id,
                exercise_id = data.workout_exercises_id,
                exercise_image = data.workout_exercise_image,
                exercise_is_favourite = data.workout_exercise_is_favourite,
                exercise_access_level = data.exercise_access_level,
                exercise_name = data.workout_exercise_name,
                exercise_share_url = data.exercise_share_url,
                exercise_tags = data.newtag,
                exercise_video = data.workout_exercise_video,
                is_liked = data.is_liked,
                isPlaying = false,
                isSelected = false,
                isReps = false,
                exercise_timer_time = "0"/*data.exercise_timer_time*/,
                exercise_reps_time = "0"/*data.exercise_reps_time*/,
                exercise_reps_number = "0"/*data.exercise_reps_number*/,
                selected_exercise_reps_number = "$exerciseReps",
                selected_exercise_weight_number = "$exerciseWeight",
                exercise_rest_time = "0"/*data.exercise_rest_time*/,
                isValid = false,
                showLoader = false,
                isClicked = false,
                videoLength = 0,
                isSelectedExercise = false,
                leftAndRightOrSuperSetOrAddExercise = "0"/*data.leftAndRightOrSuperSetOrAddExercise*/,
                isExercisePopupVisible = false
            )

            return data1.deepCopy()
        }
       /* *//**making duplicate exercise object for workout detail screen for player*//*
        fun addDuplicateExercise3(data: ExerciseTransData): WorkoutExercise {
            //  var data2=data.deepCopy()
            var exerciseReps = "Select"
            if (data.iReps != null)
                exerciseReps = data.iReps!!

            var exerciseWeight = "Select"
            if (data.iWeight != null)
                exerciseWeight = data.iWeight!!

            var data1 = WorkoutExercise(
                exercise_level = data.workout_exercise_level,
                exercise_amount = "0",
                exercise_amount_display = ""*//*data.exercise_amount_display*//*,
                exercise_body_parts = data.workout_exercise_body_parts_id,
                exercise_description = data.workout_exercise_description,
                exercise_equipments = data.workout_exercise_equipments_id,
                exercise_id = data.workout_exercises_id,
                exercise_image = data.workout_exercise_image,
                exercise_is_favourite = data.workout_exercise_is_favourite,
                exercise_access_level = data.exercise_access_level,
                exercise_name = data.workout_exercise_name,
                exercise_share_url = data.exercise_share_url,
                exercise_tags = data.newtag,
                exercise_video = data.workout_exercise_video,
                is_liked = data.is_liked,
                isPlaying = false,
                isSelected = false,
                isReps = false,
                exercise_timer_time = "0"*//*data.exercise_timer_time*//*,
                exercise_reps_time = "0"*//*data.exercise_reps_time*//*,
                exercise_reps_number = "0"*//*data.exercise_reps_number*//*,
                selected_exercise_reps_number = "$exerciseReps",
                selected_exercise_weight_number = "$exerciseWeight",
                exercise_rest_time = "0"*//*data.exercise_rest_time*//*,
                isValid = false,
                showLoader = false,
                isClicked = false,
                videoLength = 0,
                isSelectedExercise = false,
                leftAndRightOrSuperSetOrAddExercise = "0"*//*data.leftAndRightOrSuperSetOrAddExercise*//*,
                isExercisePopupVisible = false
            )

            return data1.deepCopy()
        }*/

        /* isValid: Boolean = true,
                var showLoader: Boolean = false,
                var isClicked:*/
        fun notifyExerciseList(
            adapter: SelectedExerciseAdapterSetAndReps?,
            setsAdapter: SetsAdapter,
            exerciseList: ArrayList<ExerciseListingResponse.Data>,
            SetsAndRepsRoundList: ArrayList<SetAndRepsModel>
        ) {
            try {
                for (j in 0 until exerciseList.size) {
                    val model = exerciseList[j]
                    if (model.isExercisePopupVisible) {
                        model.isExercisePopupVisible = false
                        adapter?.notifyItemChanged(j, model)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        fun notifyExerciseListForPlayer(
            adapter: NewPlayerExerciseSetAndRepsAdapter?,
            exerciseList: ArrayList<ExerciseListingResponse.Data>,
            SetsAndRepsRoundList: ArrayList<SetAndRepsModel>
        ) {
            try {
                for (j in 0 until exerciseList.size) {
                    val model = exerciseList[j]
                    if (model.isExercisePopupVisible) {
                        model.isExercisePopupVisible = false
                        adapter?.notifyItemChanged(j, model)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun deletePopupForSetAndReps(
            context: Context,
            message: String,
            listner: (Dialog) -> Unit
        ) {

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.custome_dialog_ui_for_alert)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val tv_header = dialog.findViewById<TextView>(R.id.tv_header)
            val tv_cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
            val tv_logout = dialog.findViewById<TextView>(R.id.tv_logout)
            tv_header.text = "$message"
            tv_logout.setOnClickListener { v: View? ->
                listner.invoke(dialog)
            }

            tv_cancel.setOnClickListener { v: View? ->
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }


        fun getValue(): ArrayList<String> {
            var values = arrayListOf<String>()
            for (i in 0..59) {
                if (i < 10) {
                    values.add("0" + i)
                } else {
                    values.add("" + i)
                }

            }
            return values
        }


        private var mLastClickTime: Long = 0
        fun getWidthAndHeight(activity: Activity): Int {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val dpHeight = outMetrics.heightPixels
            val dpWidth = outMetrics.widthPixels
            Log.v("dpWidth", "$dpWidth--------$dpHeight")
            return dpWidth
        }


        fun getWidthOnly(activity: Activity): Int {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val dpHeight = outMetrics.heightPixels
            val dpWidth = outMetrics.widthPixels
            Log.v("dpWidth", "$dpWidth--------$dpHeight")
            return dpWidth / 4
        }


        fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity = context as Activity?
                if (activity!!.isDestroyed || activity.isFinishing) {
                    return false
                }
            }
            return true
        }

        fun lastClick() {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return
            } else {
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }

        fun getPickImageResultUri(data: Intent?, mContext: Context): Uri? {
            var isCamera = true
            if (data != null && data.data != null) {
                val action = data.action
                isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
            }
            return if (isCamera) getCaptureImageOutputUri(mContext) else data!!.data
        }

        fun getCaptureImageOutputUri(mContext: Context): Uri? {
            var outputFileUri: Uri? = null
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                val getImage = mContext.externalCacheDir
                if (getImage != null) {
                    outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
                }
            } else {
                val getImage = mContext.externalCacheDir
                if (getImage != null) {
                    outputFileUri = FileProvider.getUriForFile(
                        mContext, BuildConfig.APPLICATION_ID + ".provider",
                        File(getImage.path, "pickImageResult.jpeg")
                    )
                }
            }
            return outputFileUri
        }

        /*
                fun beginCrop(source: Uri, mContext: Context) {
                    val pro = File(MakeDir("dovies", mContext), System.currentTimeMillis().toString() + ".jpg")
                    val destination1 = Uri.fromFile(pro)
                    Crop.of(source, destination1).asSquare().withAspect(0, 0).start(mContext as Activity)

                }
        */

        fun MakeDir(filepath: String, appContext: Context): String {
            val path: File
            if (!isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                path = File(SaveFileIntoDir(filepath, appContext), filepath)
                if (!path.exists()) {
                    path.mkdirs()
                }
            } else {
                path = File(appContext.getExternalFilesDir(filepath), filepath)
                if (!path.exists()) {
                    path.mkdirs()
                }
            }
            return path.toString()
        }

        fun isExternalStorageReadOnly(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                true
            } else false
        }

        fun isExternalStorageAvailable(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == extStorageState) {
                true
            } else false
        }

        fun SaveFileIntoDir(filepath: String, appContext: Context): File {
            return appContext.getDir(filepath, Context.MODE_PRIVATE)
        }

        /*
                fun handleCropimage(resultCode: Int, result: Intent, mContext: Context): File? {
                    var f: File? = null
                    var profile_pic_ = ""
                    if (resultCode == RESULT_OK) {
                        f = File(Crop.getOutput(result).path!!)
                        val croped_file = "" + Crop.getOutput(result).path!!
                        //  PicturePath = croped_file;
                        val bmOptions = BitmapFactory.Options()
                        var bitmap = BitmapFactory.decodeFile(f.absolutePath, bmOptions)
                        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
                        bitmap = rotateImage(bitmap, f)
                        profile_pic_ = croped_file

                    } else if (resultCode == Crop.RESULT_ERROR) {
                        Toast.makeText(mContext, Crop.getError(result).message, Toast.LENGTH_SHORT).show()
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_SHORT).show()
                    }
                    return f
                }
        */

        fun rotateImage(bitmap: Bitmap?, fileWithExifInfo: File): Bitmap? {
            if (bitmap == null) {
                return null
            }
            var rotatedBitmap: Bitmap = bitmap
            var orientation = 0
            try {
                orientation = getImageOrientation(fileWithExifInfo.absolutePath)
                if (orientation != 0) {
                    val matrix = Matrix()
                    matrix.postRotate(
                        orientation.toFloat(),
                        bitmap.width.toFloat() / 2,
                        bitmap.height.toFloat() / 2
                    )
                    rotatedBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    bitmap.recycle()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return rotatedBitmap
        }

        @Throws(IOException::class)
        fun getImageOrientation(file: String): Int {
            val exif = ExifInterface(file)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> return 0
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
                else -> return 0
            }
        }


        // Function to remove duplicates from an ArrayList
        fun <T> removeDuplicates(list: MutableList<T>): List<T> {

            // Create a new LinkedHashSet
            // Add the elements to set
            val set = LinkedHashSet(list)
            // Clear the list
            list.clear()
            // add the elements of set
            // with no duplicates to the list
            list.addAll(set)
            // return the list
            return list
        }


        fun capitalize(capString: String): String {
            val capBuffer = StringBuffer()
            val capMatcher =
                Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
            while (capMatcher.find()) {
                capMatcher.appendReplacement(
                    capBuffer,
                    capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase()
                )
            }
            return capMatcher.appendTail(capBuffer).toString()
        }

        public fun isNetworkAvailable(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI
                )!!.state == NetworkInfo.State.CONNECTED
            ) {
                return true
            }

            return false
        }

        ////////
        public fun capitaliseName(name: String): String? {
            val collect = name.split(" ".toRegex()).toTypedArray()
            var returnName = ""
            for (i in collect.indices) {
                collect[i] = collect[i].trim { it <= ' ' }.toLowerCase()
                if (collect[i].isEmpty() == false) {
                    returnName =
                        returnName + collect[i].substring(0, 1).toUpperCase() + collect[i]
                            .substring(1) + " "
                }
            }
            return returnName.trim()
        }

        public fun capitaliseOnlyFirstLetter(data: String): String? {
            return data.substring(0, 1).toUpperCase() + data.substring(1)
        }

    }
    ///////////////////////////////RemoveDuplicates///////////////////////////////////

}
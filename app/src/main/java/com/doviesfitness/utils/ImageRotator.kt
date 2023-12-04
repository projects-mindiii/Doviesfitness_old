package com.doviesfitness.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

import java.io.FileNotFoundException
import java.io.IOException

/**
 * Author: Dharmraj Acharya
 * Date: 06/09/2016
 * Email: dharmrajacharya@gmail.com
 */

object ImageRotator {

    private val TAG = ImageRotator::class.java.simpleName

    /**
     * Get rotation degrees of the selected image. E.g.: 0º, 90º, 180º, 240º.
     *
     * @param context    context.
     * @param imageUri   URI of image which will be analyzed.
     * @param fromCamera true if the image was taken from camera,
     * false if it was selected from the gallery.
     * @return degrees of rotation.
     */
   fun getRotation(context: Context, imageUri: Uri, fromCamera: Boolean): Int {
        val rotation: Int
        if (fromCamera) {
            rotation = getRotationFromCamera(context, imageUri)
        } else {
            rotation = getRotationFromGallery(context, imageUri)
        }
        Log.i(TAG, "Image rotation: $rotation")
        return rotation
    }

    fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
        var rotate = 0
        try {

            context.contentResolver.notifyChange(imageFile, null)
            val exif = imageFile.path?.let { ExifInterface(it) }
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                else -> rotate = 0
            }
        } catch (e: Exception) {
            e.message?.let { Log.d("ImageRotator", it) }
        }

        return rotate
    }

    private fun getRotationFromGallery(context: Context, imageUri: Uri): Int {
        var result = 0
        val columns = arrayOf(MediaStore.Images.Media.ORIENTATION)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(imageUri, columns, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val orientationColumnIndex = cursor.getColumnIndex(columns[0])
                result = cursor.getInt(orientationColumnIndex)
            }
        } catch (e: Exception) {
            //Do nothing
        } finally {
            cursor?.close()
        } //End of try-catch block
        return result
    }

    /**
     * Rotate image X degrees.
     */
    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var bitmap = bitmap
        if (bitmap != null && degrees != 0) {
            val matrix = Matrix()
            matrix.postRotate(degrees.toFloat())
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }


    @Throws(IOException::class)
    fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap {

        val ei = selectedImage.path?.let { ExifInterface(it) }
        val orientation = ei?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(img, 270)
            else -> return img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun decodeBitmap(context: Context, theUri: Uri, sampleSize: Int): Bitmap? {
        var actuallyUsableBitmap: Bitmap? = null
        var fileDescriptor: AssetFileDescriptor? = null
        val options = BitmapFactory.Options()
        options.inSampleSize = sampleSize

        try {
            fileDescriptor = context.contentResolver.openAssetFileDescriptor(theUri, "r")
            actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor!!.fileDescriptor, null, options)
            if (actuallyUsableBitmap != null) {
                Log.i(
                    TAG, "Trying sample size " + options.inSampleSize + "\t\t"
                            + "Bitmap width: " + actuallyUsableBitmap.width
                            + "\theight: " + actuallyUsableBitmap.height
                )
            }
            fileDescriptor.close()
        } catch (e: FileNotFoundException) {
            e.message?.let { Log.d("ImageRotator", it) }
        } catch (e: IOException) {
            e.message?.let { Log.d(TAG, it) }
        }

        return actuallyUsableBitmap
    }

}// not called

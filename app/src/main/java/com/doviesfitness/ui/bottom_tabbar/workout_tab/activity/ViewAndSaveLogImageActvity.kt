package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityViewLogImagesBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.LogImageAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.MyWorkoutLogModel
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import com.doviesfitness.utils.Constant
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import android.content.Intent
import android.net.Uri
import androidx.viewpager.widget.ViewPager
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.LogImagePagerAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.LogImageModel
import androidx.core.content.FileProvider
import android.os.Build
import android.os.Handler
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogModel


class ViewAndSaveLogImageActvity : BaseActivity(), LogImageAdapter.OnSelectImage ,View.OnClickListener{
    lateinit var binding: ActivityViewLogImagesBinding
    lateinit var imageList: ArrayList<LogImageModel>
    lateinit var adapter: LogImageAdapter
    lateinit var workoutModel:MyWorkoutLogModel.Data
    lateinit var streamModel: StreamLogModel.Data
    var list= ArrayList<LinkedHashMap<String,String>>()
    lateinit var pagerAdapter:LogImagePagerAdapter
    var imageFile: File? = null
    var from=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ViewAndSaveLogImageActvity, R.layout.activity_view_log_images)
        initialisation()
    }
    fun initialisation() {
        binding.downloadIcon.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        imageList = ArrayList()

        if (intent.hasExtra("from"))
            from=intent.getStringExtra("from")!!

        if (from.isNotEmpty()) {
            streamModel = intent.getSerializableExtra("image_list") as StreamLogModel.Data

            if (streamModel.customer_weight.equals("0 Kg",true))
                binding.weight.text="NA"
            else
                binding.weight.text=streamModel.customer_weight
            binding.logDate.text=streamModel.log_created_date
            list= streamModel.workout_log_images as ArrayList<LinkedHashMap<String, String>>

        }
        else {
            workoutModel = intent.getSerializableExtra("image_list") as MyWorkoutLogModel.Data

            if (workoutModel.customer_weight.equals("0 Kg",true))
                binding.weight.text="NA"
            else
                binding.weight.text=workoutModel.customer_weight
            binding.logDate.text=workoutModel.log_date
            list= workoutModel.workout_log_images as ArrayList<LinkedHashMap<String, String>>

        }

        for (i in 0..list.size-1){
            var str=list.get(i).get("log_image")
            if (i==0){
                var item=LogImageModel(str!!,i,true)
                imageList.add(item)
            }
            else{
                var item=LogImageModel(str!!,i,false)
                imageList.add(item)
            }
        }

        val horizontalLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            getActivity(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        pagerAdapter = LogImagePagerAdapter(this@ViewAndSaveLogImageActvity, imageList)
        binding.viewPager.setAdapter(pagerAdapter)

        binding.viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                for (i in 0..imageList.size-1){
                    if (i==position)
                    { imageList.get(i).isSelected=true }
                    else{ imageList.get(i).isSelected=false }
                }
                adapter.notifyDataSetChanged()
            }
        })
        binding.rvImages.layoutManager = horizontalLayoutManager
        adapter = LogImageAdapter(getActivity(), imageList, this)
        binding.rvImages.adapter = adapter
    }
    override fun onClick(v: View?) {
    when(v!!.id){
        R.id.download_icon->{
            takeScreenshot("download")
        }
        R.id.cancel_button->{
            onBackPressed()
        }
        R.id.iv_share->{
            binding.ivShare.isEnabled=false
            takeScreenshot("share")
            Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

        }
    }
    }
    override fun onSelectImage(pos: Int) {
        for (i in 0..imageList.size-1){
            if (i==pos)
            { imageList.get(i).isSelected=true }
            else{ imageList.get(i).isSelected=false }
        }
        binding.viewPager.currentItem=pos
        adapter.notifyDataSetChanged()
    }
    private fun takeScreenshot(from: String) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        try {
            var str=now.toString()
            str.replace(" ","_")
            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png"
            binding.frameLayout.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(binding.frameLayout.drawingCache)
            binding.frameLayout.isDrawingCacheEnabled = false
             imageFile = File(mPath)
            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            val filePath = imageFile!!.getPath()
            val ssbitmap = BitmapFactory.decodeFile(imageFile!!.getAbsolutePath())
            if(from.equals("download"))
            Constant.showCustomToast(getActivity(),"Saved")
            else{
                share()
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    private fun share() {
        val uri: Uri
        val sharIntent = Intent(Intent.ACTION_SEND)
        val ext = imageFile!!.getName().substring(imageFile!!.getName().lastIndexOf(".") + 1)
        val mime = MimeTypeMap.getSingleton()
        val type = mime.getMimeTypeFromExtension(ext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", imageFile!!)
            sharIntent.setDataAndType(uri, type)
        } else {
            uri = Uri.fromFile(imageFile)
            sharIntent.setDataAndType(uri, type)
        }
        sharIntent.type = "image/png"
        sharIntent.putExtra(Intent.EXTRA_STREAM, uri)
        sharIntent.putExtra(Intent.EXTRA_SUBJECT, "Dovies")
        startActivity(Intent.createChooser(sharIntent, "Share:"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorWhite
                )
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
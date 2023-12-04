package com.doviesfitness.ui.splash


import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.authentication.login.LoginActivity
import com.doviesfitness.ui.authentication.signup.activity.SignUpActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.splash.model.Data
import com.doviesfitness.ui.splash.model.SliderImageResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_intro_slider.btn_login
import kotlinx.android.synthetic.main.activity_intro_slider.btn_signup
import kotlinx.android.synthetic.main.activity_intro_slider.intro_bullets
import kotlinx.android.synthetic.main.activity_intro_slider.intro_view_pager
import kotlinx.android.synthetic.main.activity_intro_slider.ll_button
import kotlinx.android.synthetic.main.activity_intro_slider.splash_logo
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import java.util.Timer
import java.util.TimerTask

class IntroSliderActivity : BaseActivity(), View.OnClickListener {

    private var introViewPager: ViewPager? = null
    private var introViewPagerAdapter: SliderOnlineImage? = null
    private var introViewPagerAdapterr: SliderOfflineImage? = null
    private var introBullets: Array<TextView>? = null
    private var introBulletsLayout: LinearLayout? = null
    private var introSliderLayouts: IntArray? = null
    private var imageList = ArrayList<Data>()
    private var mLastClickTime: Long = 0
    lateinit var info: PackageInfo
    private var currentPage = 0
    private val numPages = 4
    private val delayMS: Long = 1000//delay in milliseconds before task is to be executed
    private val periodsMS: Long = 5000
    private var setData: String = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()


        setContentView(R.layout.activity_intro_slider)

        initialize()
        getDietPlanApi()
        printHashKey()

    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    private fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d("KeyHash:", Base64.getEncoder().encodeToString(md.digest()))
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    private fun initialize() {


        btn_login.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
        introViewPager = intro_view_pager
        introBulletsLayout = intro_bullets
        //Get the intro slides
        introSliderLayouts = intArrayOf(
            R.layout.intro_screen_3,
            R.layout.intro_screen_2,
            R.layout.intro_screen_1
        )


        val handler = Handler()
        val update = Runnable {

            if (currentPage == numPages - 1) {
                currentPage = 0
            }
            introViewPager!!.setCurrentItem(currentPage++, true)
        }

        val timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(update)
            }
        }, delayMS, periodsMS)
    }

    private fun setAdapter(imageList: ArrayList<Data>) {
        ll_button.visibility = View.VISIBLE
        introViewPagerAdapter = SliderOnlineImage(imageList, this)
        introViewPager!!.adapter = introViewPagerAdapter
        introViewPager!!.addOnPageChangeListener(introViewPagerListener)
        makeIIntroBullets(0)
    }


    private fun setOfflineAdapter() {
        ll_button.visibility = View.VISIBLE
        introViewPagerAdapterr = SliderOfflineImage(this, introSliderLayouts)
        introViewPager!!.adapter = introViewPagerAdapterr
        introViewPager!!.addOnPageChangeListener(introViewPagerListener)
    }

    private fun makeIIntroBullets(currentPage: Int) {

        val arraySize = imageList!!.size
        introBullets = Array(arraySize) {
            textBoxInit()
        }
        val colorsActive = resources.getIntArray(R.array.array_intro_bullet_active)
        val colorsInactive = resources.getIntArray(R.array.array_intro_bullet_inactive)


        introBulletsLayout!!.removeAllViews()
        for (i in 0 until introBullets!!.size) {
            introBullets!![i] = TextView(this)
            introBullets!![i].text = Html.fromHtml("&#9679;")
            introBullets!![i].textSize = 12F
            introBullets!![i].setTextColor(colorsInactive[currentPage])
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            param.setMargins(8, 0, 8, 0)
            introBullets!![i].layoutParams = param
            introBulletsLayout!!.addView(introBullets!![i])
        }
        if (introBullets!!.isNotEmpty())
            introBullets!![currentPage].setTextColor(colorsActive[currentPage])
    }


    private fun makeIIntroBulletsOffline(currentPage: Int) {

        val arraySize = introSliderLayouts!!.size
        introBullets = Array(arraySize) {
            textBoxInit()
        }
        val colorsActive = resources.getIntArray(R.array.array_intro_bullet_active)
        val colorsInactive = resources.getIntArray(R.array.array_intro_bullet_inactive)


        introBulletsLayout!!.removeAllViews()
        for (i in 0 until introBullets!!.size) {
            introBullets!![i] = TextView(this)
            introBullets!![i].text = Html.fromHtml("&#9679;")
            introBullets!![i].textSize = 12F
            introBullets!![i].setTextColor(colorsInactive[currentPage])
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            param.setMargins(8, 0, 8, 0)
            introBullets!![i].layoutParams = param
            introBulletsLayout!!.addView(introBullets!![i])
        }
        try {
            if (introBullets!!.isNotEmpty())
                introBullets!![currentPage].setTextColor(colorsActive[currentPage])
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun textBoxInit(): TextView {
        return TextView(applicationContext)
    }


    private var introViewPagerListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                makeIIntroBullets(position)
                makeIIntroBulletsOffline(position)
                /*Based on the page position change the button text*/
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                //Do nothing for now
            }

            override fun onPageScrollStateChanged(arg0: Int) {
                //Do nothing for now
            }
        }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()

                }
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }

            R.id.btn_signup -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()

                }
                intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }

        }

    }

    private fun getDietPlanApi() {
        if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            val param = HashMap<String, String>()
            param.put("image_type", "APP")
            getDataManager().homeSliderImageListApi(param)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)

                        if (success!!.equals("1")) {
                            splash_logo.visibility=View.GONE
                            val imageResponse = getDataManager().mGson?.fromJson(
                                response.toString(),
                                SliderImageResponse::class.java
                            )
                            imageList.clear()
                            imageList.addAll(imageResponse!!.settings.data)
                            if (imageList.isEmpty()) {
                                setOfflineAdapter()
                                makeIIntroBulletsOffline(0)

                            } else
                                setAdapter(imageList)

                        } else {
                            setOfflineAdapter()
                            makeIIntroBulletsOffline(0)
                        }

                    }

                    override fun onError(anError: ANError) {
                        setOfflineAdapter()
                        makeIIntroBulletsOffline(0)

                        //   Constant.errorHandle(anError, getActivity() as Activity)
                    }
                })
        } else {

            setOfflineAdapter()
            makeIIntroBulletsOffline(0)
            // Constant.showInternetConnectionDialog(getActivity() as Activity)
        }

    }
}




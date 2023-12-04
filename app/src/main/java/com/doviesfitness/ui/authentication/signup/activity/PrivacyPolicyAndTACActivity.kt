package com.doviesfitness.ui.authentication.signup.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_privacypolicy_and_tc.*

class PrivacyPolicyAndTACActivity :BaseActivity(), View.OnClickListener {
    var url:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        setContentView(R.layout.activity_privacypolicy_and_tc)
        initView()
    }

    private fun initView() {

        hideNavigationBar()
        val windowBackground = window.decorView.background
        topBlurView.setupWith(containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        iv_back.setOnClickListener(this)
        val name = intent.getStringExtra("from")!!
        if (name.equals("tnc")){
            tv_title.text=resources.getString(R.string.terms_and_conditions)
            url="https://www.doviesfitness.com/terms-conditions.html"
        }
        else{
            tv_title.text=resources.getString(R.string.privacy_policy)
            url="https://www.doviesfitness.com/privacy-policy.html"
        }
        webView.setBackgroundColor(resources.getColor(R.color.colorBlack))
        webView.webViewClient = WebViewClient()

        webView.settings.loadsImagesAutomatically = true
        webView.getSettings().setJavaScriptEnabled(true)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

    }
    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            loader.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
              onBackPressed()
            }
        }
    }
}

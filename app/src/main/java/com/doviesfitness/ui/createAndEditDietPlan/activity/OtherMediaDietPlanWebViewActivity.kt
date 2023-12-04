package com.doviesfitness.ui.createAndEditDietPlan.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentDietPlanWebViewBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import eightbitlab.com.blurview.RenderScriptBlur

class OtherMediaDietPlanWebViewActivity : BaseActivity(), View.OnClickListener {


    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    lateinit var binding: FragmentDietPlanWebViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_diet_plan_web_view)
        initialization()
    }

    private fun initialization() {
        hideNavigationBar()

        val windowBackground =  window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(getActivity()))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        binding.ivBack.setOnClickListener(this)


        if(intent.getParcelableExtra<DietPlanSubCategoryModal.Data.DietListing>("weburl") != null){
            dietPlanSubCatgory = intent.getParcelableExtra<DietPlanSubCategoryModal.Data.DietListing>("weburl")!!
            if (!dietPlanSubCatgory.diet_plan_title.isEmpty()) {
                binding.dpTitleName.text = dietPlanSubCatgory.diet_plan_title

                binding.webView.webViewClient = WebViewClient()
                binding.webView.settings.setSupportZoom(true)
                binding.webView.settings.javaScriptEnabled = true
                val url = dietPlanSubCatgory.diet_plan_pdf
                binding.webView.settings.setBuiltInZoomControls(true)

                binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
                binding.webView.setWebViewClient(object:WebViewClient() {
                    //once the page is loaded get the html element by class or id and through javascript hide it.
                    override fun onPageFinished(view: WebView, url:String) {
/*
                        super.onPageFinished(view, url)
*/
                        if (view.contentHeight==0)
                            view.reload();
/*                        binding.webView.loadUrl("javascript:(function() { " +
                                "document.querySelector('[role=\"toolbar\"]').remove();})()")*/;
                    }
                })
            }
        }

    }


    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

}

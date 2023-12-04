package com.doviesfitness.ui.createAndEditDietPlan.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal

class create_DietPlanWebViewActivity : BaseActivity(), View.OnClickListener {


    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    lateinit var binding: com.doviesfitness.databinding.ActivityCreateDietPlanWebViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create__diet_plan_web_view)
        initialization()
    }

    private fun initialization() {

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
                binding.webView.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        return true
                    }
                })
                binding.webView.setLongClickable(false)
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

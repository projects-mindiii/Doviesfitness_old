package com.doviesfitness.ui.showDietPlanDetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentShowWebviewBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import eightbitlab.com.blurview.RenderScriptBlur



class ShowWebviewFragment : BaseFragment(), View.OnClickListener {

    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    lateinit var binding: FragmentShowWebviewBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_webview, container, false)
        initialization()
        return binding.root
    }


    private fun initialization() {

        hideNavigationBar()

        val windowBackground =  activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        binding.ivBack.setOnClickListener(this)
        //binding.progressBar.visibility = View.VISIBLE

        dietPlanSubCatgory = (arguments!!.getParcelable("fromShowDetail") as? DietPlanSubCategoryModal.Data.DietListing)!!
        if (!dietPlanSubCatgory.diet_plan_title.isEmpty()) {
            binding.dpTitleName.text = dietPlanSubCatgory.diet_plan_title
            binding.webView.webViewClient = WebViewClient()
            //binding.webView.webViewClient  = MyWebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.javaScriptEnabled = true
            val url = dietPlanSubCatgory.diet_plan_pdf
            binding.webView.setBackgroundColor(resources.getColor(R.color.colorBlack))
//            binding.webView.settings.setBuiltInZoomControls(true)
//            binding.webView.getSettings().setDisplayZoomControls(false);
//
            binding.webView.settings.setBuiltInZoomControls(true);
            binding.webView.settings.setDisplayZoomControls(false);


         //   binding.webView.settings.setLoadWithOverviewMode(true)
         //   binding.webView.settings.setUseWideViewPort(true)
//           binding.webView.settings.setDisplayZoomControls(false)
//
//            binding.webView.settings.setBuiltInZoomControls(true)

            binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
/*
            binding.webView.setWebViewClient(object:WebViewClient() {
                //once the page is loaded get the html element by class or id and through javascript hide it.
                override fun onPageFinished(view: WebView, url:String) {
                    super.onPageFinished(view, url)
                    binding.webView.loadUrl("javascript:(function() { " +
                            "document.querySelector('[role=\"toolbar\"]').remove();})()");
                }
            })
*/

            binding.webView.setWebViewClient(object:WebViewClient() {
                //once the page is loaded get the html element by class or id and through javascript hide it.
                override fun onPageFinished(view: WebView, url:String) {
                    if (view.contentHeight==0)
//                        view.reload();
                        binding.webView.loadUrl("javascript:(function() {document.querySelector('[class=\"ndfHFb-c4YZDc-Wrql6b\"]').remove();})()")
                    binding.progressBar.visibility = View.GONE
                    binding.webView.visibility=View.VISIBLE

                    /* super.onPageFinished(view, url)
                     binding.webView.loadUrl("javascript:(function() { " +
                             "document.querySelector('[role=\"toolbar\"]').remove();})()");*/
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

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

//     inner class MyWebViewClient : WebViewClient() {
//
//         override fun onPageFinished(view: WebView, url: String) {
//             super.onPageFinished(view, url)
//             binding.progressBar.visibility = View.GONE
//         }
//
//         override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//             super.onPageStarted(view, url, favicon)
//             binding.progressBar.visibility = View.GONE
//         }
//
//         override fun onPageCommitVisible(view: WebView, url: String) {
//             super.onPageCommitVisible(view, url)
//         }
//     }




}

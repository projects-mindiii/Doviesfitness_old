package com.doviesfitness.ui.setting.faq

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityFaqBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.DietPlanCategoryAdapter
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject
import java.lang.Exception

class FAQActivity : BaseActivity(), View.OnClickListener {


    private lateinit var faqAdapter: FaqAdapter
    private lateinit var binding: ActivityFaqBinding
    private var faqList = ArrayList<FAQModel.Data>()
    lateinit var adapter: DietPlanCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq)
        inItView()
    }

    private fun inItView() {

        hideNavigationBar()
        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        faqAdapter = FaqAdapter(this@FAQActivity, faqList)
        binding.rvFaq.setAdapter(faqAdapter)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvFaq.layoutManager = layoutManager1

        setOnClick(binding.ivBack)
        getFAQData()
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    private fun getFAQData() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.device_token,
            "" + getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().getFAQS(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {

                    val fAQModel =
                        getDataManager().mGson?.fromJson(response.toString(), FAQModel::class.java)
                    faqList.addAll(fAQModel!!.data)
                    faqAdapter.notifyDataSetChanged()
                }
            }
            override fun onError(anError: ANError) {
                Constant.showCustomToast(binding.rvFaq.context!!, getString(R.string.something_wrong))
                try {
                    Constant.errorHandle(anError!!, getActivity())
                }
                catch (e: Exception){
                }
            }
        })
    }
}

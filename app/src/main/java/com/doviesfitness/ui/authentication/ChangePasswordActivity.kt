package com.doviesfitness.ui.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityChangePasswordActvityBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding  : ActivityChangePasswordActvityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_change_password_actvity)

        inItView()
    }

    private fun inItView() {
        hideNavigationBar()

        setOnClick(binding.ivBack, binding.btnUpdate)

        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        binding.etOldPassword.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>0){
                    binding.errorPasswordTxt.visibility=View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.etNewPass.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>0){
                    binding.errorNewPasswordTxt.visibility=View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.etReTypePassword.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>0){
                    binding.errorReenterPasswordTxt.visibility=View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }


    override fun onClick(view: View?) {

        when(view!!.id){

            R.id.iv_back ->{
                onBackPressed()
            }

            R.id.btn_update ->{
                if (isValidData()) {
                    hideKeyboard()
                    changePassword()
                }
            }
        }
    }


    private fun isValidData():Boolean{
        if (binding.etNewPass.text.toString().trim().isEmpty()&&binding.etOldPassword.text.toString().trim().isEmpty()
            &&binding.etReTypePassword.text.toString().trim().isEmpty()){
            binding.errorNewPasswordTxt.visibility=View.VISIBLE
            binding.errorPasswordTxt.visibility=View.VISIBLE
            binding.errorReenterPasswordTxt.visibility=View.VISIBLE
            return false
        }
        else if (binding.etOldPassword.text.toString().trim().isEmpty()){
            binding.errorPasswordTxt.visibility=View.VISIBLE
            return false
        }
        else if (binding.etNewPass.text.toString().trim().isEmpty()){
            binding.errorNewPasswordTxt.visibility=View.VISIBLE
            return false
        }
        else if (binding.etReTypePassword.text.toString().trim().isEmpty()){
            binding.errorReenterPasswordTxt.visibility=View.VISIBLE
            return false
        }
        else if (!binding.etReTypePassword.text.toString().trim().equals(binding.etNewPass.text.toString().trim())){
            binding.errorReenterPasswordTxt.visibility=View.VISIBLE
            binding.errorReenterPasswordTxt.text=resources.getString(R.string.password_does_not_match_with_confirm_password)
            return false
        }
        else{
            binding.errorNewPasswordTxt.visibility=View.GONE
            binding.errorPasswordTxt.visibility=View.GONE
            binding.errorReenterPasswordTxt.visibility=View.GONE
            return true
        }
    }

    private fun changePassword() {
        binding.progressLayout.visibility = View.VISIBLE
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.newPassword, binding.etNewPass.text.toString().trim())
        param.put(StringConstant.oldPassword, binding.etOldPassword.text.toString().trim())
        param.put(StringConstant.newConfirmPassword, binding.etReTypePassword.text.toString().trim())

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, StringConstant.HBDEV)
        header.put(StringConstant.apiVersion, StringConstant.apiVersionValue)
        header.put(StringConstant.accept, StringConstant.applicationJson)
        header.put(StringConstant.contentType, StringConstant.applicationJson)

        getDataManager().changePassword(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    Constant.showCustomToast(getActivity(), "" + message)
                    binding.progressLayout.visibility = View.GONE
                    onBackPressed()
                }
                else {
                    Constant.showCustomToast(getActivity(), "" + message)
                    binding.progressLayout.visibility = View.GONE
                }
            }
            override fun onError(anError: ANError) {
                binding.progressLayout.visibility = View.GONE
                Constant.errorHandle(anError, getActivity())
                Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
            }
        })
    }

}

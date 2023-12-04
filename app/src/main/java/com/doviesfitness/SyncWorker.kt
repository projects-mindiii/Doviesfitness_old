package com.clubz.helper

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.SubscriptionModel
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject

class SyncWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val TAG = SyncWorker::class.java.simpleName
    override fun doWork(): Result {
        getSubscriptionStatus()
        return Result.success()
    }

    private fun getSubscriptionStatus() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().getSubscriptionStatus(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                Log.d("Subscription ", "Subscription syncWorker response...." + response.toString())
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    val SubsResponse = getDataManager().mGson?.fromJson(
                        response.toString(),
                        SubscriptionModel::class.java
                    )
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED,
                        SubsResponse!!.data.is_subscribed
                    )
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.PREF_KEY_APP_SUBSCRIPTION_TITLE,
                        SubsResponse!!.data.title
                    )

                    var rhjeshr =
                        getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)
                    var admin =
                        getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)

                    Log.v("rhjeshr", "" + rhjeshr + "----------------" + admin)
                } else {
                }
            }

            override fun onError(anError: ANError) {
            }
        })
    }
}
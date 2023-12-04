package com.doviesfitness.subscription

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.doviesfitness.R
import kotlinx.android.synthetic.main.activity_purchase.*
import com.android.billingclient.api.Purchase

import com.android.billingclient.api.PurchasesUpdatedListener

import com.doviesfitness.MainActivity

import com.android.billingclient.api.BillingClient




abstract class PurchaseActivity : AppCompatActivity(), PurchasesUpdatedListener{

    //[SkuDetails: {"skuDetailsToken":"AEuhp4I9iH5NS1t05cVgawESYf2wlgJy_Gu9nc2w6X74E3BB86sui6O1webJLo4wC8s=","productId":"one_month","type":"subs","price":"₹70.00","price_amount_micros":70000000,"price_currency_code":"INR","subscriptionPeriod":"P1M","freeTrialPeriod":"P1W1D","title":"1 month (Dovies)","description":"one month membership"}, SkuDetails: {"skuDetailsToken":"AEuhp4J4bOPZcCON8-z-S3O_3cgrESD-2eHg6TJffhtuROFP2l4FqpcQweTst6nPlJo=","productId":"two_month","type":"subs","price":"₹120.00","price_amount_micros":120000000,"price_currency_code":"INR","subscriptionPeriod":"P3M","freeTrialPeriod":"P1W1D","title":"2 month (Dovies)","description":"two month description"}]


    private lateinit var billingClient: BillingClient
  //  private val skuList = listOf("m_one_month", "one_month", "two_month")
    private val skuList = listOf("dovies.subscription3months", "one_month", "two_month")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        setupBillingClient()
    }

    private fun setupBillingClient() {

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    logger("Setup Billing Done")
                    loadAllSKUs()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                logger("Failed")

            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)
            .build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                for (skuDetails in skuDetailsList!!) {
                    if (skuDetails.sku == "one_month")
                        buttonBuyProduct.setOnClickListener {
                            val billingFlowParams = BillingFlowParams
                                .newBuilder()
                                .setSkuDetails(skuDetails)
                                .build()
                            billingClient.launchBillingFlow(this, billingFlowParams)
                        }
                }
            }
            if (skuDetailsList!!.size>0) {
                logger("Desc: "+skuDetailsList.get(0).description)
                logger("Skus..: "+skuDetailsList.toString())
            } else{
                logger("Skus: "+skuDetailsList.toString())
            }
        }

    } else {
        println("Billing Client not ready")
    }

    override fun onPurchasesUpdated(p0: BillingResult, purchases: MutableList<Purchase>?) {
        if (p0?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                logger("purchaseToken..."+purchase.purchaseToken)
                //purchaseToken...fibdbdacjelmophkmmgcmlbg.AO-J1Ox04MmLqk9qKxJrf6WfMHsjwoRZVlP30Ht0zoiO7X8ilj6xyjSB11KU98-C8BjgY860eHvUH10zsV-dOVCtQQn_OPjoEgP_GeKc9EejenU2Te37zFI
                logger("purchase data..."+purchase.toString())
                logger("purchase developerPayload..."+purchase.developerPayload.toString())
                //PurchaseActivity: purchase data...Purchase. Json: {"orderId":"GPA.3305-6067-9804-66518","packageName":"com.doviesfitness","productId":"one_month","purchaseTime":1573283949977,"purchaseState":0,"purchaseToken":"fibdbdacjelmophkmmgcmlbg.AO-J1Ox04MmLqk9qKxJrf6WfMHsjwoRZVlP30Ht0zoiO7X8ilj6xyjSB11KU98-C8BjgY860eHvUH10zsV-dOVCtQQn_OPjoEgP_GeKc9EejenU2Te37zFI","autoRenewing":true,"acknowledged":false}

                acknowledgePurchase(purchase.purchaseToken)
            }
        } else if (p0?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            logger("User Cancelled")
            logger(p0?.debugMessage.toString())
        } else {
            logger(p0?.debugMessage.toString())
            // Handle any other error codes.
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            logger("debugMessage..."+debugMessage)
            logger("responseCode.."+responseCode)
        }
    }
    //com.android.billingclient.api.BillingResult@79c9ce2
}

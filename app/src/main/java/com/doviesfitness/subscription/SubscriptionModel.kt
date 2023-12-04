package com.doviesfitness.subscription

data class SubscriptionModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val is_subscribed: String,
        val subscription_end_date: String,
        val subscription_start_date: String,
        val title: String
    )

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}
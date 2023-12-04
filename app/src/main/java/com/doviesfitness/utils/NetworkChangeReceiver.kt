package com.doviesfitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.net.ConnectivityManager


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (checkInternet(context)) {
            Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show()
        }
    }

    internal fun checkInternet(context: Context): Boolean {
        val serviceManager = ServiceManager(context)
        return if (serviceManager.isNetworkAvailable) {
            true
        } else {
            false
        }
    }
}

class ServiceManager(internal var context: Context) {

    val isNetworkAvailable: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
}
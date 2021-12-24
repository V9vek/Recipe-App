package com.vivek.recipeapp.ui.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.activity.ComponentActivity.CONNECTIVITY_SERVICE
import androidx.lifecycle.LiveData
import com.vivek.recipeapp.interactors.app.DoesNetworkHaveInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val validNetworks: MutableSet<Network> = HashSet()

    // Check if we have overall connectivity and valid network with internet
    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        super.onActive()
        networkCallback = createNetworkCallback()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }


    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        // Called when the framework connects and has declared a new network is ready for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            println("onAvailable: $network")

            // check if network has capability to have internet
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)

            if (hasInternetCapability == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    // now check if network has internet
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)

                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            println("onAvailable: This network has internet: $network")

                            // adding to list of valid networks if having internet
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        // Called when a network disconnects
        override fun onLost(network: Network) {
            super.onLost(network)
            println("onLost: $network")

            validNetworks.remove(network)
            checkValidNetworks()
        }
    }
}



























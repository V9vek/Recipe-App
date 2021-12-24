package com.vivek.recipeapp.ui.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import com.vivek.recipeapp.BaseApplication
import javax.inject.Inject
import javax.inject.Singleton

// This CustomConnectivityManager is singleton because it will be instantiated only once, in the Main Activity
// and that object will be injected wherever we want

@Singleton
class CustomConnectivityManager @Inject constructor(
    application: BaseApplication
) {
    private val connectionLiveData = ConnectionLiveData(context = application)

    val isNetworkAvailable = mutableStateOf(false)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.observe(lifecycleOwner) { isConnected ->
            isConnected?.let { isNetworkAvailable.value = it }
        }
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}

























package com.vivek.recipeapp.ui.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import com.vivek.recipeapp.BaseApplication
import javax.inject.Inject
import javax.inject.Singleton

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

























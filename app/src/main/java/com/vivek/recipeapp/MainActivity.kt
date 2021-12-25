package com.vivek.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.vivek.recipeapp.datastore.SettingsDataStore
import com.vivek.recipeapp.navigation.RecipeAppNavigation
import com.vivek.recipeapp.ui.theme.RecipeAppTheme
import com.vivek.recipeapp.ui.util.CustomConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var customConnectivityManager: CustomConnectivityManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onStart() {
        super.onStart()
        customConnectivityManager.registerConnectionObserver(lifecycleOwner = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        customConnectivityManager.unregisterConnectionObserver(lifecycleOwner = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecipeAppTheme(darkTheme = settingsDataStore.isDark.value) {
                Surface(color = MaterialTheme.colors.background) {
                    RecipeAppNavigation(
                        isNetworkAvailable = customConnectivityManager.isNetworkAvailable.value,
                        isDarkTheme = settingsDataStore.isDark.value,
                        onToggleTheme = { settingsDataStore.toggleTheme() }
                    )
                }
            }
        }
    }
}
















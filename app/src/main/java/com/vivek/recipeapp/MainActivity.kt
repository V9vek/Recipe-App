package com.vivek.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vivek.recipeapp.navigation.RecipeAppNavigation
import com.vivek.recipeapp.ui.theme.RecipeAppTheme
import com.vivek.recipeapp.ui.util.CustomConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var customConnectivityManager: CustomConnectivityManager

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
            var isDarkTheme by remember { mutableStateOf(false) }

            RecipeAppTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colors.background) {
                    RecipeAppNavigation(
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}
















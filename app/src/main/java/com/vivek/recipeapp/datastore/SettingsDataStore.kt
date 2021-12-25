package com.vivek.recipeapp.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vivek.recipeapp.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    private val app: BaseApplication
) {
    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey(name = "dark_theme_key")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val scope = CoroutineScope(Main)
    val isDark = mutableStateOf(false)

    init {
        observeDataStore()
    }

    fun toggleTheme() {
        scope.launch {
            app.dataStore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY] ?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    private fun observeDataStore() {
        app.dataStore.data.onEach { preferences ->
            preferences[DARK_THEME_KEY]?.let { isDarkThemeSet ->
                isDark.value = isDarkThemeSet
            }
        }.launchIn(scope)
    }
}


























package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.ui.screens.recipe.RecipeEvent.GetRecipeEvent
import com.vivek.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_RECIPE_ID = "recipe.state.recipe_id"

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val token: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val recipe = mutableStateOf<Recipe?>(null)

    val isLoading = mutableStateOf(false)

    init {
        // retrieving state key-value after process death
        savedStateHandle.get<Int>(STATE_KEY_RECIPE_ID)?.let { id ->
            onTriggerEvent(event = GetRecipeEvent(recipeId = id))
        }
    }

    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetRecipeEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.recipeId)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.cause)
            }
        }
    }

    private suspend fun getRecipe(id: Int) {
        isLoading.value = true
        delay(1000)         // because api is too fast

        val result = repository.get(
            token = token,
            id = id
        )
        recipe.value = result

        // saving state
        savedStateHandle.set(STATE_KEY_RECIPE_ID, result.id)

        isLoading.value = false
    }
}

























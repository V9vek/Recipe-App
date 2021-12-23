package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.interactors.recipe.GetRecipe
import com.vivek.recipeapp.ui.screens.recipe.RecipeEvent.GetRecipeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_RECIPE_ID = "recipe.state.recipe_id"

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipe: GetRecipe,
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

    private fun getRecipe(id: Int) {
        getRecipe.execute(token = token, recipeId = id)
            .onEach { dataState ->
                isLoading.value = dataState.loading

                dataState.data?.let { data ->
                    recipe.value = data
                    savedStateHandle.set(STATE_KEY_RECIPE_ID, data.id)
                }

                dataState.error?.let { error ->
                    println("ERROR: getRecipe: $error")
                    // TODO: handle error
                }
            }.launchIn(viewModelScope)
    }
}

























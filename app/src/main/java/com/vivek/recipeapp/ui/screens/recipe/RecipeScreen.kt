package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.recipeapp.navigation.RecipeEvent.GetRecipeEvent
import com.vivek.recipeapp.ui.components.CircularIndeterminateProgressBar
import com.vivek.recipeapp.ui.components.RecipeView

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    recipeId: Int
) {

    DisposableEffect(key1 = Unit) {
        viewModel.onTriggerEvent(event = GetRecipeEvent(recipeId = recipeId))
        onDispose { }
    }

    val recipe = viewModel.recipe.value
    val isLoading = viewModel.isLoading.value

    Box(modifier = Modifier) {
        if (isLoading && recipe == null) {
            // TODO: Show Shimmer Animation
        } else {
            recipe?.let { RecipeView(recipe = it) }
        }

        CircularIndeterminateProgressBar(isDisplayed = isLoading)
    }
}





























package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    LaunchedEffect(key1 = Unit) {
        viewModel.onTriggerEvent(event = GetRecipeEvent(recipeId = recipeId))
    }

    val recipe = viewModel.recipe.value
    val isLoading = viewModel.isLoading.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && recipe == null) {
            // TODO: Show Shimmer Animation
        } else {
            recipe?.let { RecipeView(recipe = it) }
        }

        CircularIndeterminateProgressBar(isDisplayed = isLoading)
    }
}





























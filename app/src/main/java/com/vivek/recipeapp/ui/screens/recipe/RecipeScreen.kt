package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.recipeapp.ui.components.CircularIndeterminateProgressBar
import com.vivek.recipeapp.ui.components.ConnectivityMonitor
import com.vivek.recipeapp.ui.components.IMAGE_HEIGHT
import com.vivek.recipeapp.ui.components.RecipeShimmerAnimation
import com.vivek.recipeapp.ui.components.RecipeView
import com.vivek.recipeapp.ui.screens.recipe.RecipeEvent.GetRecipeEvent
import com.vivek.recipeapp.ui.screens.recipe_list.ProcessDialogQueue


@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    recipeId: Int,
    isNetworkAvailable: Boolean,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.onTriggerEvent(event = GetRecipeEvent(recipeId = recipeId))
    }

    val recipe = viewModel.recipe.value
    val isLoading = viewModel.isLoading.value
    val dialogQueue = viewModel.dialogQueue

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && recipe == null) {
            RecipeShimmerAnimation(
                cardHeight = IMAGE_HEIGHT.dp,
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.9f),
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 0.9f)
                ),
                gradientWidth = 350f
            )
        } else {
            recipe?.let { RecipeView(recipe = it) }
        }

        CircularIndeterminateProgressBar(isDisplayed = isLoading)

        ProcessDialogQueue(queue = dialogQueue.queue.value)

        ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
    }
}





























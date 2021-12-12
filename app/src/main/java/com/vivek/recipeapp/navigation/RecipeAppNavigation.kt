package com.vivek.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivek.recipeapp.navigation.ScreenRouting.*
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListScreen
import com.vivek.recipeapp.ui.screens.recipe.RecipeScreen

@Composable
fun RecipeAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RecipeList.route) {
        composable(route = RecipeList.route) {
            RecipeListScreen(
                onRecipeClicked = { navController.navigate(route = Recipe.route) }
            )
        }

        composable(route = Recipe.route) {
            RecipeScreen()
        }
    }
}
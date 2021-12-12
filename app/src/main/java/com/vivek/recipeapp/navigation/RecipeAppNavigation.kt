package com.vivek.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vivek.recipeapp.navigation.ScreenRouting.Recipe
import com.vivek.recipeapp.navigation.ScreenRouting.RecipeList
import com.vivek.recipeapp.ui.screens.recipe.RecipeScreen
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListScreen

@Composable
fun RecipeAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RecipeList.route) {
        composable(route = RecipeList.route) {
            RecipeListScreen(
                onRecipeClicked = { recipeId ->
                    navController.navigate(route = "${Recipe.route}/$recipeId")
                }
            )
        }

        composable(
            route = "${Recipe.route}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("recipeId")?.let { recipeId ->
                RecipeScreen(recipeId = recipeId)
            }
        }
    }
}











package com.vivek.recipeapp.navigation

sealed class ScreenRouting(val route: String) {

    object RecipeList : ScreenRouting(route = "RecipeListScreen")

    object Recipe : ScreenRouting(route = "RecipeScreen")
}